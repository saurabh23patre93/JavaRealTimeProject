package in.nit.view;

import java.awt.Color;
import java.util.Date;
import java.util.List;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;

import in.nit.model.PurchaseDetail;
import in.nit.model.PurchaseOrder;

public class VendorInvoicePdfView extends AbstractPdfView {

	//Override build Pdf Document
	@SuppressWarnings("unchecked")
	@Override
	protected void buildPdfMetadata(Map<String, Object> model, 
									Document document, 
									HttpServletRequest request) {
		
		HeaderFooter header=null;
		HeaderFooter footer=null;
		
		//For  header
		header=new HeaderFooter(new Phrase("VENDOR INVOICE REPORT"),false);
		//For footer
		footer=new HeaderFooter(new Phrase("Copyrights @SaurabhIt |" + new Phrase(new Date().toString() +"| PAGE NUMBER#")), new Phrase("."));
		
		//For header set alignment
		header.setAlignment(1);//0 Left,1 Middle, 2 Right
		header.setBorder(Rectangle.TOP);
		//for footer set alignment
		footer.setAlignment(2);
		footer.setBorder(Rectangle.TOP);
		
		//Add header to document
		document.setHeader(header);
		//Add footer to document
		document.setFooter(footer);
		
	}
	
	//Override build Pdf Document
	@SuppressWarnings("unchecked")
	@Override
	protected void buildPdfDocument(Map<String, Object> model, 
									Document document, 
									PdfWriter writer,
									HttpServletRequest req, 
									HttpServletResponse resp) throws Exception {
		
		Font font=null,fontTitle=null,font2=null;
		Paragraph paragraph=null;
		List<PurchaseDetail> dtls=null;
		double finalCost=0.0f;
		PurchaseOrder po=null;
		PdfPTable table=null;
		Image image=null;
		
		po=(PurchaseOrder) model.get("po");
		//---------------------Image adding into PDF------------------------------------
		image=Image.getInstance(new ClassPathResource("/static/images/logo.png").getURL());
		//Set Image alignments
		image.setAlignment(Image.MIDDLE);
		image.scaleAbsolute(300.0f,80.0f);
		image.setBorderWidth(5.0f);//for border width
		image.enableBorderSide(Rectangle.BOTTOM+Rectangle.TOP+Rectangle.LEFT+Rectangle.RIGHT);
		//Add image into document
		document.add(image);
		
		//-----------------For Title font in column----------------------------------------
		//Font with Color:
		//fontTitle=new Font(Font.HELVETICA,25,Font.BOLD,Color.PINK);
		fontTitle= new Font(Font.HELVETICA, 25, Font.BOLD, new Color(222, 161, 20));
		//Create element>>paragraph with Font
		paragraph=new Paragraph("VENDOR INVOICE CODE"+po.getWhUserType().getUserCode()+"-"+po.getOrderCode(),fontTitle);
		paragraph.setAlignment(Element.ALIGN_CENTER);
		//add element to document else it will not display at PDF
		document.add(paragraph);
		
		
		//Download document
		resp.addHeader("Content-Disposition","attachment;filename=PO-"+po.getOrderCode()+".pdf");
		
		//Provide custom Font Details object
		font=new Font(Font.HELVETICA,12,Font.BOLD,Color.BLUE);
		font2=new Font(Font.TIMES_ROMAN,12,Font.BOLD,Color.ORANGE);
		
		//Reading data from Controller using Model
		dtls=po.getDtls();
		
		finalCost=dtls.stream()
				.mapToDouble(ob->ob.getQty()*ob.getPart().getBaseCost())
				.sum();
		
		//Crate one table with no of columns to be display
		table=new PdfPTable(4);//no of columns
		table.setSpacingBefore(10.0f);
		table.setTotalWidth(new float[] {1.5f,3.0f,3.0f,3.0f});
		
		//Add data to columns using addCell method
		//once 6 cells done then it will change row automatically
		table.addCell(new Phrase("PART CODE",font));
		table.addCell(new Phrase("BASE COST",font));
		table.addCell(new Phrase("QTY",font));
		table.addCell(new Phrase("LINE TOTAL",font));
		
				
		//add dtls data to table
		for(PurchaseDetail st:dtls) {
			table.addCell(new Phrase(st.getPart().getPartCode(),font2));
			table.addCell(new Phrase(st.getPart().getBaseCost().toString(),font2));
			table.addCell(new Phrase(st.getQty().toString(),font2));
			table.addCell(new Phrase(String.valueOf(st.getPart().getBaseCost()*st.getQty()),font2));
			
		}
		//add table to document
		document.add(table);
		document.add(new Phrase());
	}

}
