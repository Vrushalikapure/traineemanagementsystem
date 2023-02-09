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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class UserPdfExporterImpl implements UserPdfExporter {

    @Value("${pdf.exporter.async:false}")
    Boolean async;

    @Autowired
    UserService userService;

    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Override
    public void export(HttpServletResponse response, List<WorkHistory> workHistoryList) throws UserNotFoundException {

        try(Document document = new Document(PageSize.A4)) {
            PdfWriter.getInstance(document, response.getOutputStream());

            PdfPTable ptable = new PdfPTable(4);
            ptable.setWidthPercentage(100);
            writeHeader(ptable);
            writeData(ptable, workHistoryList);

            document.open();
            document.add(new Paragraph("list of users"));
            document.add(ptable);
        }
        catch (IOException exception){
            logger.error(exception.getMessage());
        }
    }

    private void writeHeader(PdfPTable ptable) {

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

    private void writeData(PdfPTable ptable, List<WorkHistory> workHistoryList) throws UserNotFoundException {

        logger.info("getting data in {} mode for writing to pdf", async ? "Async" : "Sync");
        long timeBefore = System.currentTimeMillis();

        Map<String, CompletableFuture<User>> completableFutureUserMap = new HashMap<>();

        if(async)
        {
            for (WorkHistory workHistory: workHistoryList){
                completableFutureUserMap.put(workHistory.getUserName(), userService.getUserByUserName(workHistory.getUserName()));
            }
        }

        for (WorkHistory user : workHistoryList) {
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
}
