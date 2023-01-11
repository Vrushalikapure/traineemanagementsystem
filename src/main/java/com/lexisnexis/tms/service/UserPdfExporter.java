package com.lexisnexis.tms.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lexisnexis.tms.entity.WorkHistory;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;



public class UserPdfExporter {

	private List<WorkHistory> listWorkHistory;

	public UserPdfExporter(List<WorkHistory> listWorkHistory) {

		this.listWorkHistory = listWorkHistory;
	}

	public void writeHeader(PdfPTable ptable) {
		PdfPCell cell = new PdfPCell();
		 cell.setPhrase(new Phrase("userName"));
		 ptable.addCell(cell);
		 cell.setPhrase(new Phrase("working Area"));
		 ptable.addCell(cell);
		 cell.setPhrase(new Phrase("Comments"));
		 ptable.addCell(cell);
//		 cell.setPhrase(new Phrase("mobileNo"));
//		 ptable.addCell(cell);
//		 cell.setPhrase(new Phrase("email"));
//		 ptable.addCell(cell);
//		 cell.setPhrase(new Phrase("location"));
//		 ptable.addCell(cell);
	}

	public void writeData(PdfPTable ptable) {
		
		for(WorkHistory user:listWorkHistory) {
			ptable.addCell(user.getUserName());
			ptable.addCell(user.getWorkingArea());
			ptable.addCell(user.getComments());
//			ptable.addCell(user.getCreatedTimestamp());
//			ptable.addCell(user.getLastUpdatedTimestamp());
			
		}

	}
	
	public void export(HttpServletResponse response) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4);
		
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		document.add(new Paragraph ("list of users"));
		PdfPTable ptable =new PdfPTable(3);
		ptable.setWidthPercentage(100);
		writeHeader(ptable);
		writeData(ptable);
		document.add(ptable);
		document.close();
		
	}
	
}
