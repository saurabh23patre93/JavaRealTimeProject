package in.nit.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import in.nit.model.Document;
import in.nit.service.IDocumentService;

@Controller
@RequestMapping("/documents")
public class DocumentController {
	@Autowired 
	private IDocumentService service;
	
	//1.Get All doc UI
	@GetMapping("/all")
	public String showDocs() {
		return "Documents";
	}
	
	//2.save the doc 
	@PostMapping("/save")
	public String upload(@RequestParam Integer fileId,
						@RequestParam MultipartFile fileOb
						) {
		Document doc=null;
		
		if (fileOb!=null && fileId!=null) {
			doc=new Document();
			doc.setDocId(fileId);
			doc.setDocName(fileOb.getOriginalFilename());//file name
			try {
				doc.setDocData(fileOb.getBytes());//file data
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "Documents";
	}
	
	//3.download uploaded doc
	@GetMapping("/download/{id}")
	public void downloadDoc(@PathVariable Integer id,
							HttpServletResponse resp) throws IOException {
		Optional<Document> opt=null;
		Document doc=null;
		
		//Get data from db using id
		opt=service.findDocument(id);
		
		if (opt.isPresent()) {
			//Read Document object
			doc=opt.get();
			
			//add header parameter(key-val) to response
			resp.addHeader("Content-Disposition", "attachment;filename="+doc.getDocName());
			
			try {
				//copy byte[] data from model class object ot servlet output stream
				
				FileCopyUtils.copy(doc.getDocData()//from
							,  resp.getOutputStream()//to
							);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
