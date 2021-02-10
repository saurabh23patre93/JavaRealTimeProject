package in.nit.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import in.nit.model.Uom;
import in.nit.model.WhUserType;

public class WhUserTypeExcelView extends AbstractXlsxView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, 
										Workbook workbook, 
										HttpServletRequest request,
										HttpServletResponse response) 
												throws Exception {
		List<WhUserType> list=null;
		Sheet sheet=null;
		
		//Download and gives the file name
		response.addHeader("Content-Disposition","attachment;filename=Uoms.xlsx");
		
		//read data from controller
		//@SuppressWarnings("unchecked")
		list=(List<WhUserType>) model.get("obs");
		
		//create new Sheet
		sheet=workbook.createSheet("UOMS");
		
		//programmer define method
		setHead(sheet);
		setBody(sheet,list);
	}
	
	//For row ##0 creation
	private void setHead(Sheet sheet) {
		Row row=null;
		
		row=sheet.createRow(0);
		row.createCell(0).setCellValue("ID");
		row.createCell(1).setCellValue("USER TYPE");
		row.createCell(2).setCellValue("USER CODE");
		row.createCell(3).setCellValue("USER FOR");
		row.createCell(4).setCellValue("USER EMAIL");
		row.createCell(5).setCellValue("USER CONTACT");
		row.createCell(6).setCellValue("USER ID TYPE");
		row.createCell(7).setCellValue("USER IF OTHER");
		row.createCell(8).setCellValue("USER ID Number");
	}
	//Create User defined setBody method
	private void setBody(Sheet sheet,List<WhUserType> list) {
		int rowNum=1;
		for(WhUserType wh:list) {
			Row row=null;
			
			row=sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(wh.getId());
			row.createCell(1).setCellValue(wh.getUserType());
			row.createCell(2).setCellValue(wh.getUserCode());
			row.createCell(3).setCellValue(wh.getUserFor());
			row.createCell(4).setCellValue(wh.getUserEmail());
			row.createCell(5).setCellValue(wh.getUserContact());
			row.createCell(6).setCellValue(wh.getUserIdType());
			row.createCell(7).setCellValue(wh.getIfOther());
			row.createCell(8).setCellValue(wh.getIdNumber());
		}
	}
}
