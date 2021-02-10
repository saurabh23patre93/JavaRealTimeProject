package in.nit.controller.rest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.nit.model.Part;
import in.nit.service.IPartService;
import in.nit.validate.PartValidator;

@RestController
@RequestMapping("/rest/part")
public class PartRestController {
	
	//Create Logger Object
	private Logger log=LoggerFactory.getLogger(PartRestController.class);
	
	@Autowired
	private IPartService service;
	
	@Autowired
	private PartValidator validator;
	
	//1.Get all Records From DB
		@GetMapping("/all")
		public ResponseEntity<List<Part>> getAll(){
			List<Part> list=null;
			
			try {
				log.info("Enter into ResponseEntity getAll method Of Part");
				list=service.getAllParts();	
			} catch (Exception e) {
				log.error("Enable to process ResponseEntity "+e.getMessage());
				e.printStackTrace();
			}
			
			//Return ResponseEntity.ok(list)
			log.info("Return Response Entity Object");
			return new ResponseEntity<List<Part>>(list,
											HttpStatus.OK);
		}
		
		//2.Get One Record from DB
		@GetMapping("/one/{id}")
		public ResponseEntity<?> getOnePart(@PathVariable String id){
			Optional<Part> opt=null;
			ResponseEntity<?> resp=null;
			
			try {
				log.info("Enter into ResponseEntity GetOnePart method "+id);
				opt=service.getOnePart(id);
				
				if (opt.isPresent()) {
					resp=new ResponseEntity<Part>(opt.get(),
												HttpStatus.OK);
				} else {
					resp=new ResponseEntity<String>("Data Not Found",
													HttpStatus.BAD_REQUEST);
				}
				
			} catch (Exception e) {
				log.error("Enable to process ResponseEntity "+e.getMessage());
				e.printStackTrace();
			}
			
			log.info("Return Response Entity Object");
			return resp;
		}
		
		//3.Delete one record from DB
		@DeleteMapping("/remove/{id}")
		public ResponseEntity<String> deleteOnePart(@PathVariable String id){
			ResponseEntity<String> resp=null;
			
			try {
				log.info("Enter into ResponseEntity deleteOnePart method ");
				if (service.isPartExist(id)) {
					try {
						service.deletePart(id);
						resp=new ResponseEntity<String>("Part Deleted", HttpStatus.OK);
					} catch (Exception e) {
						resp=new ResponseEntity<String>("Part Can not be deleted",HttpStatus.BAD_REQUEST);
					}
				} else {
					resp=new ResponseEntity<String>("Part not exist",HttpStatus.NOT_FOUND);
				}
			} catch (Exception e) {
				log.error("Enable to process ResponseEntity "+e.getMessage());
				e.printStackTrace();
			}
			
			log.info("Return Response Entity Object");
			return resp;
		}
	
	//4.save one record into DB
	@PostMapping("/save")
	public ResponseEntity<?> savePart(@RequestBody Part part){
		ResponseEntity<?> resp=null;
		Map<String,String> errors=null;
		String id=null;
		
		errors=validator.validate(part);
		if (errors.isEmpty()) {
			id=service.savePart(part);
			resp=new ResponseEntity<>("Part "+id+" saved ",HttpStatus.CREATED);
		} else {
			resp=new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
		}
		
		return resp;
	}
	
	//5.update one record into DB
		@PutMapping("/modify")
		public ResponseEntity<String > updatePart(@RequestBody Part Part){
			ResponseEntity<String> resp=null;
			try {
				log.info("Enter into ResponseEntity updatePart method ");
				if(Part.getId()==null || !service.isPartExist(Part.getId()) ) {
					resp = new ResponseEntity<String>(
							"Given Part not exist in DB",
							HttpStatus.NOT_FOUND);
				}else {
					service.updatePart(Part);
					resp = new ResponseEntity<String>(
							"Part '"+Part.getId()+"' Updated",
							HttpStatus.OK);
				}
			} catch (Exception e) {
				log.error("Enable to process ResponseEntity "+e.getMessage());
				e.printStackTrace();
			}
			return resp;
		}
}
