package com.lexisnexis.tms.serviceimpl;

import com.lexisnexis.tms.entity.WorkHistory;
import com.lexisnexis.tms.services.UserPdfExporter;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class UserPdfExporterImpl implements UserPdfExporter {
    private static final Logger LOGGER = LogManager.getLogger(UserPdfExporterImpl.class);
    final private List<WorkHistory> listWorkHistory;

    public UserPdfExporterImpl(List<WorkHistory> listWorkHistory) {
        this.listWorkHistory = listWorkHistory;
    }

    @Override
    public void writeHeader(PdfPTable ptable) {
        final PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase("User Name"));
        ptable.addCell(cell);
        cell.setPhrase(new Phrase("Working Area"));
        ptable.addCell(cell);
        cell.setPhrase(new Phrase("Comments"));
        ptable.addCell(cell);
    }

    @Override
    public void writeData(PdfPTable ptable) {
        for (final WorkHistory user : listWorkHistory) {
            ptable.addCell(user.getUserName());
            ptable.addCell(user.getWorkingArea());
            ptable.addCell(user.getComments());
        }
    }

    @Override
    public void export(HttpServletResponse response ) throws IOException {
        try (Document document=new Document(PageSize.A4)){
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            document.add(new Paragraph("list of users"));
            final PdfPTable ptable = new PdfPTable(3);
            ptable.setWidthPercentage(100);
            writeHeader(ptable);
            writeData(ptable);
            document.add(ptable);
        } catch (DocumentException documentException) {
            LOGGER.info(documentException.getMessage());
        }
        finally {
            LOGGER.info("doc closed");
        }
        LOGGER.info(Thread.currentThread().getName());
    }
}
