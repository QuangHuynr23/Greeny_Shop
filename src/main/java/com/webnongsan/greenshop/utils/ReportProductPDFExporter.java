package com.webnongsan.greenshop.utils;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.webnongsan.greenshop.model.response.StatisticalOrderDetail;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ReportProductPDFExporter {
	private List<StatisticalOrderDetail> listReportCommon ;

	public ReportProductPDFExporter(List<StatisticalOrderDetail> listReportCommon) {
		this.listReportCommon = listReportCommon;
	}
	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		Font font = FontFactory.getFont("Fonts/TIMES.TTF", BaseFont.IDENTITY_H, 12.5f);
		font.setColor(Color.WHITE);
		PdfPCell cell1 = new PdfPCell(new Phrase("Sản Phẩm", font));
		cell1.setBackgroundColor(Color.BLUE);
		cell1.setPadding(5);
		table.addCell(cell1);
		PdfPCell cell2 = new PdfPCell(new Phrase("Số Lượng bán ra", font));
		cell2.setBackgroundColor(Color.BLUE);
		cell2.setPadding(5);
		table.addCell(cell2);
		cell.setPhrase(new Phrase("Doanh thu", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Giá bán trung bình", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Giá bán thấp nhất", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Giá bán cao nhất", font));
		table.addCell(cell); 
	}
	private void writeTableData(PdfPTable table) {
		for (StatisticalOrderDetail statisticalOrderDetailOfProduct : listReportCommon) {
			Font font = FontFactory.getFont("Fonts/ARIAL.TTF", BaseFont.IDENTITY_H, 11.5f);
			table.addCell(new Phrase(statisticalOrderDetailOfProduct.getName(),font));
			table.addCell(String.valueOf(statisticalOrderDetailOfProduct.getQuantity()));
			table.addCell(String.valueOf(statisticalOrderDetailOfProduct.getSumPrice()));
			table.addCell(String.valueOf(statisticalOrderDetailOfProduct.getAveragePrice()));
			table.addCell(String.valueOf(statisticalOrderDetailOfProduct.getMinimumPrice()));
			table.addCell(String.valueOf(statisticalOrderDetailOfProduct.getMaximumPrice()));
		}
	}
	public void export(HttpServletResponse response) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD,18f);
		font.setColor(Color.RED);
		Paragraph title = new Paragraph("List All Report Product",font);
		title.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(title);
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);
		table.setWidths(new float[] {3.1f, 2.5f, 2.9f, 2.9f, 2.9f,2.9f});
		table.setSpacingBefore(15);
		writeTableHeader(table);
		writeTableData(table);
		document.add(table);
		document.close();
	}
}
