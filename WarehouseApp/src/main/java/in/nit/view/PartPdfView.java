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

import in.nit.model.Part;

public class PartPdfView extends AbstractPdfView {

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
		List<Part> list=null;
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
		list=(List<Part>) model.get("list");
		
		//Crate one table with no of columns to be display
		table=new PdfPTable(8);//no of columns
		table.setSpacingBefore(4.0f);
		table.setTotalWidth(new float[] {1.0f,1.0f,1.5f,1.0f,1.0f,1.5f,1.5f,1.5f});
		
		//Add data to columns using addCell method
		//once 6 cells done then it will change row automatically
		table.addCell("ID");
		table.addCell("CODE");
		table.addCell("WIDTH");
		table.addCell("LENGTH");
		table.addCell("HIGHT");
		table.addCell("COST");
		table.addCell("CURRENCY");
		table.addCell("UOM");
		table.addCell("DESCRIPTION");
		
		//add list data to table
		for(Part part:list) {
			table.addCell(part.getId());
			table.addCell(part.getPartCode());
			table.addCell(String.valueOf(part.getPartWidth()));
			table.addCell(String.valueOf(part.getPartLength()));
			table.addCell(String.valueOf(part.getPartHight()));
			table.addCell(String.valueOf(part.getBaseCost()));
			table.addCell(part.getBaseCurrency());
			table.addCell(part.getUom().getUomModel());
			table.addCell(part.getDescription());
		}
		//add table to document
		document.add(table);
	}

}
