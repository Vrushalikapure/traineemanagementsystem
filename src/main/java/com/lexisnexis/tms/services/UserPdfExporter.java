package com.lexisnexis.tms.services;

import com.lowagie.text.pdf.PdfPTable;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UserPdfExporter {

    public void writeHeader(PdfPTable ptable);

    public void writeData(PdfPTable ptable);

    public void export(HttpServletResponse response) throws  IOException;

}
