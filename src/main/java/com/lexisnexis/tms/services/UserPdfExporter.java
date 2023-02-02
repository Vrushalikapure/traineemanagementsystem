package com.lexisnexis.tms.services;

import com.lexisnexis.tms.exception.UserNotFoundException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfPTable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UserPdfExporter {

    public void writeHeader(PdfPTable ptable);

    public void writeData(PdfPTable ptable) throws UserNotFoundException;

    public void export(HttpServletResponse response) throws DocumentException, IOException, UserNotFoundException;

}
