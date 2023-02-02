package com.lexisnexis.tms.serviceImpl;

import com.lexisnexis.tms.entity.User;
import com.lexisnexis.tms.entity.WorkHistory;
import com.lexisnexis.tms.exception.UserNotFoundException;
import com.lexisnexis.tms.services.UserPdfExporter;
import com.lexisnexis.tms.services.UserService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// @Service is omitted is this service is constructed manually rather than being autowired
public class UserPdfExporterImpl implements UserPdfExporter {

    private final UserService userService;

    private final List<WorkHistory> listWorkHistory;

    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    private final Boolean async;

    public UserPdfExporterImpl(List<WorkHistory> listWorkHistory, UserService userService, Boolean async) {

        this.listWorkHistory = listWorkHistory;
        this.userService = userService;
        this.async = async;
    }

    @Override
    public void writeHeader(PdfPTable ptable) {
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase("User Name"));
        ptable.addCell(cell);
        cell.setPhrase(new Phrase("Working Area"));
        ptable.addCell(cell);
        cell.setPhrase(new Phrase("Comments"));
        ptable.addCell(cell);
        cell.setPhrase(new Phrase("Location"));
        ptable.addCell(cell);
    }

    @Override
    public void writeData(PdfPTable ptable) throws UserNotFoundException {

        logger.info("getting data in {} mode for writing to pdf", async ? "Async" : "Sync");

        long timeBefore = System.currentTimeMillis();

        Map<String, CompletableFuture<User>> completableFutureUserMap = new HashMap<>();

        if(async)
        {
            for (WorkHistory workHistory: listWorkHistory){
                completableFutureUserMap.put(workHistory.getUserName(), userService.getUserByUserName(workHistory.getUserName()));
            }
        }

        for (WorkHistory user : listWorkHistory) {
            ptable.addCell(user.getUserName());
            ptable.addCell(user.getWorkingArea());
            ptable.addCell(user.getComments());
            if(async) {
                try {
                    ptable.addCell(completableFutureUserMap.get(user.getUserName()).get().getLocation());
                } catch (InterruptedException | ExecutionException e) {
                    System.out.printf("got an exception trying to fetch user information for %s", user.getUserName());
                }
            }
            else {
                ptable.addCell(userService.getDataByUserName(user.getUserName()).getLocation());
            }
        }

        logger.info(MarkerManager.getMarker("ASYNC"), "writing data to pdf took {} milliseconds", System.currentTimeMillis() - timeBefore);
    }

    @Override
    public void export(HttpServletResponse response) throws DocumentException, IOException, UserNotFoundException {
        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        document.add(new Paragraph("list of users"));
        PdfPTable ptable = new PdfPTable(4);
        ptable.setWidthPercentage(100);
        writeHeader(ptable);
        writeData(ptable);
        document.add(ptable);
        document.close();

    }

}
