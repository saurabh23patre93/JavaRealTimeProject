package in.nit.view;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;

import in.nit.model.ShipmentType;

public class OrderMethodPdfView extends AbstractPdfView {

	//Override build Pdf Document
	@SuppressWarnings("unchecked")
	@Override
	protected void buildPdfDocument(Map<String, Object> model, 
									Document document, 
									PdfWriter writer,
									HttpServletRequest req, 
									HttpServletResponse resp) throws Exception {
		
		Font font=null;
		Paragraph paragraph=null;
		List<ShipmentType> list=null;
		PdfPTable table=null;
		
		//Download document
		resp.addHeader("Content-Disposition","attachment;filename=shipments.pdf");
		
		//Provide custom Font Details object
		font=new Font(Font.HELVETICA,20,Font.BOLD,Color.BLUE);
		
		//Create element>>paragraph with Font
		paragraph=new Paragraph("WELCOME TO SHIPMENT TYPE",font);
		paragraph.setAlignment(Element.ALIGN_CENTER);
		
		//add element to document else it will not display at PDF
		document.add(paragraph);
		
		//Reading data from Controller using Model
		list=(List<ShipmentType>) model.get("list");
		
		//Crate one table with no of columns to be display
		table=new PdfPTable(6);//no of columns
		table.setSpacingBefore(4.0f);
		table.setTotalWidth(new float[] {1.0f,1.0f,1.5f,1.0f,1.0f,1.5f});
		
		//Add data to columns using addCell method
		//once 6 cells done then it will change row automatically
		table.addCell("ID");
		table.addCell("MODE");
		table.addCell("CODE");
		table.addCell("ENABLE");
		table.addCell("GRADE");
		table.addCell("DESCRIPTION");
		
		//add list data to table
		for(ShipmentType st:list) {
			table.addCell(st.getId().toString());
			table.addCell(st.getShipmentMode());
			table.addCell(st.getShipmentCode());
			table.addCell(st.getEnableShipment());
			table.addCell(st.getShipmentGrade());
			table.addCell(st.getDescription());
		}
		//add table to document
		document.add(table);
	}

}
