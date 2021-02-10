package in.nit.view;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import in.nit.model.Part;

public class PartExcelView extends AbstractXlsxView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, 
										Workbook workbook, 
										HttpServletRequest request,
										HttpServletResponse response) 
												throws Exception {
		List<Part> list=null;
		Sheet sheet=null;
		
		//Download and gives the file name
		response.addHeader("Content-Disposition","attachment;filename=Parts.xlsx");
		
		//read data from controller
		//@SuppressWarnings("unchecked")
		list=(List<Part>)model.get("obs");
		
		//create new Sheet
		sheet=workbook.createSheet("Parts");
		
		//programmer define method
		setHead(sheet);
		setBody(sheet,list);
	}
	
	//For row ##0 creation
	private void setHead(Sheet sheet) {
		Row row=null;
		
		row=sheet.createRow(0);
		row.createCell(0).setCellValue("ID");
		row.createCell(1).setCellValue("CODE");
		row.createCell(2).setCellValue("WIDTH");
		row.createCell(3).setCellValue("LENGTH");
		row.createCell(4).setCellValue("HIGHT");
		row.createCell(5).setCellValue("COST");
		row.createCell(6).setCellValue("CURRENCY");
		row.createCell(7).setCellValue("UOM");
		row.createCell(8).setCellValue("DESCRIPTION");
	}
	//Create User defined setBody method
	private void setBody(Sheet sheet,List<Part> list) {
		int rowNum=1;
		for(Part part:list) {
			Row row=null;
			
			row=sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(part.getId());
			row.createCell(1).setCellValue(part.getPartCode());
			row.createCell(2).setCellValue(part.getPartWidth());
			row.createCell(3).setCellValue(part.getPartLength());
			row.createCell(4).setCellValue(part.getPartHight());
			row.createCell(5).setCellValue(part.getBaseCost());
			row.createCell(6).setCellValue(part.getBaseCurrency());
			row.createCell(7).setCellValue(part.getUom().getUomModel());
			row.createCell(8).setCellValue(part.getDescription());
		}
	}
}
