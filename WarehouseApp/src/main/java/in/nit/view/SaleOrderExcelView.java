package in.nit.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import in.nit.model.SaleOrder;


public class SaleOrderExcelView extends AbstractXlsxView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, 
										Workbook workbook, 
										HttpServletRequest request,
										HttpServletResponse response) 
												throws Exception {
		List<SaleOrder> list=null;
		Sheet sheet=null;
		
		//Download and gives the file name
		response.addHeader("Content-Disposition","attachment;filename=SaleOrders.xlsx");
		
		//read data from controller
		//@SuppressWarnings("unchecked")
		list=(List<SaleOrder>)model.get("obs");
		
		//create new Sheet
		sheet=workbook.createSheet("SALE_ORDERS");
		
		//programmer define method
		setHead(sheet);
		setBody(sheet,list);
	}
	
	//For row ##0 creation
	private void setHead(Sheet sheet) {
		Row row=null;
		
		row=sheet.createRow(0);
		row.createCell(0).setCellValue("ID");
		row.createCell(1).setCellValue("ORDER CODE");
		row.createCell(2).setCellValue("REFERENCE NUMBER");
		row.createCell(3).setCellValue("STOCK MODE");
		row.createCell(4).setCellValue("STOCK SOURCE");
		row.createCell(5).setCellValue("DEFAULT STATUS");
		row.createCell(6).setCellValue("DESCRIPTION");
	}
	//Create User defined setBody method
	private void setBody(Sheet sheet,List<SaleOrder> list) {
		int rowNum=1;
		for(SaleOrder st:list) {
			Row row=null;
			
			row=sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(st.getId());
			row.createCell(1).setCellValue(st.getOrderCode());
			row.createCell(2).setCellValue(st.getReferenceNumber());
			row.createCell(3).setCellValue(st.getStockMode());
			row.createCell(4).setCellValue(st.getStockSource());
			row.createCell(5).setCellValue(st.getDefaultStatus());
			row.createCell(6).setCellValue(st.getDescription());
		}
	}
}
