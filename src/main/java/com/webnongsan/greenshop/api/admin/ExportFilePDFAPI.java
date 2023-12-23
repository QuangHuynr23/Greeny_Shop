package com.webnongsan.greenshop.api.admin;

import com.webnongsan.greenshop.Enum.StatisticalType;
import com.webnongsan.greenshop.model.response.StatisticalOrderDetail;
import com.webnongsan.greenshop.service.OrderDetailServiceImpl;
import com.webnongsan.greenshop.utils.ReportProductPDFExporter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller(value = "ExportFile")
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExportFilePDFAPI {
	private final OrderDetailServiceImpl orderDetailService;
	
	@GetMapping(value ="/export/pdf/reportProduct")
	public void exportAllProductToPDF(HttpServletResponse response) throws IOException {
		exportToPDF(response, orderDetailService.findOrderDetailOfProduct(StatisticalType.Other.getValue()));
	}

	@GetMapping(value = "/export/pdf/reportCategory")
	public void exportAllCategoryToPDF(HttpServletResponse response) throws IOException {
		exportToPDF(response, orderDetailService.statisticalCategory(StatisticalType.Other.getValue()));
	}

	@GetMapping(value = "/export/pdf/reportCustomer")
	public void exportAllCustomerToPDF(HttpServletResponse response) throws IOException {
		exportToPDF(response, orderDetailService.statisticalCustomer(StatisticalType.Other.getValue()));
	}

	private void exportToPDF(HttpServletResponse response, List<StatisticalOrderDetail> listReportCommon) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/pdf;charset=UTF-8");
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss_dd-MM-yyyy");
		String currentDateTime = dateFormat.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment;filename=ReportProduct_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);
		ReportProductPDFExporter exporter = new ReportProductPDFExporter(listReportCommon);
		exporter.export(response);
	}
}
