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

public class UomExcelView extends AbstractXlsxView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, 
										Workbook workbook, 
										HttpServletRequest request,
										HttpServletResponse response) 
												throws Exception {
		List<Uom> list=null;
		Sheet sheet=null;
		
		//Download and gives the file name
		response.addHeader("Content-Disposition","attachment;filename=Uoms.xlsx");
		
		//read data from controller
		//@SuppressWarnings("unchecked")
		list=(List<Uom>)model.get("obs");
		
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
		row.createCell(1).setCellValue("UOM TYPE");
		row.createCell(2).setCellValue("UOM MODEL");
		row.createCell(3).setCellValue("DESCRIPTION");
	}
	//Create User defined setBody method
	private void setBody(Sheet sheet,List<Uom> list) {
		int rowNum=1;
		for(Uom st:list) {
			Row row=null;
			
			row=sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(st.getId());
			row.createCell(1).setCellValue(st.getUomType());
			row.createCell(2).setCellValue(st.getUomModel());
			row.createCell(3).setCellValue(st.getDescription());
		}
	}
}
