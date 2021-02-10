package in.nit.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import in.nit.model.PurchaseOrder;
import in.nit.model.Uom;

public class PurchaseOrderExcelView extends AbstractXlsxView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, 
										Workbook workbook, 
										HttpServletRequest request,
										HttpServletResponse response) 
												throws Exception {
		List<PurchaseOrder> list=null;
		Sheet sheet=null;
		
		//Download and gives the file name
		response.addHeader("Content-Disposition","attachment;filename=PurchaseOrders.xlsx");
		
		//read data from controller
		//@SuppressWarnings("unchecked")
		list=(List<PurchaseOrder>)model.get("obs");
		
		//create new Sheet
		sheet=workbook.createSheet("PURCHASE_ORDERS");
		
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
		row.createCell(3).setCellValue("QUALITY CHECK");
		row.createCell(4).setCellValue(" STATUS");
		row.createCell(5).setCellValue("DESCRIPTION");
	}
	//Create User defined setBody method
	private void setBody(Sheet sheet,List<PurchaseOrder> list) {
		int rowNum=1;
		for(PurchaseOrder st:list) {
			Row row=null;
			
			row=sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(st.getId());
			row.createCell(1).setCellValue(st.getOrderCode());
			row.createCell(2).setCellValue(st.getReferenceNumber());
			row.createCell(3).setCellValue(st.getQualityCheck());
			row.createCell(4).setCellValue(st.getStatus());
			row.createCell(5).setCellValue(st.getDescription());
		}
	}
}
