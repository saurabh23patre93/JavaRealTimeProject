package in.nit.controller.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import in.nit.controller.PartController;
import in.nit.model.Uom;
import in.nit.service.IUomService;

@RestController
@RequestMapping("/rest/uom")
public class UomRestController {
	//Create Logger Object
	private Logger log=LoggerFactory.getLogger(UomRestController.class);
	
	@Autowired
	private IUomService service;
	
	//1.Get all Records From DB
	@GetMapping("/all")
	public ResponseEntity<List<Uom>> getAll(){
		List<Uom> list=null;
		
		try {
			log.info("Enter into ResponseEntity getAll method Of Uom");
			list=service.getAllUoms();	
		} catch (Exception e) {
			log.error("Enable to process ResponseEntity "+e.getMessage());
			e.printStackTrace();
		}
		
		//Return ResponseEntity.ok(list)
		log.info("Return Response Entity Object");
		return new ResponseEntity<List<Uom>>(list,
										HttpStatus.OK);
	}
	
	//2.Get One Record from DB
	@GetMapping("/one/{id}")
	public ResponseEntity<?> getOneUom(@PathVariable String id){
		Optional<Uom> opt=null;
		ResponseEntity<?> resp=null;
		
		try {
			log.info("Enter into ResponseEntity GetOneUom method "+id);
			opt=service.getOneUom(id);
			
			if (opt.isPresent()) {
				resp=new ResponseEntity<Uom>(opt.get(),
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
	public ResponseEntity<String> deleteOneUom(@PathVariable String id){
		ResponseEntity<String> resp=null;
		
		try {
			log.info("Enter into ResponseEntity deleteOneUom method ");
			if (service.isUomExist(id)) {
				try {
					service.deleteUom(id);
					resp=new ResponseEntity<String>("Uom Deleted", HttpStatus.OK);
				} catch (Exception e) {
					resp=new ResponseEntity<String>("Uom Can not be deleted",HttpStatus.BAD_REQUEST);
				}
			} else {
				resp=new ResponseEntity<String>("Uom not exist",HttpStatus.NOT_FOUND);
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
	public ResponseEntity<?> saveUom(@Valid @RequestBody Uom uom,
										BindingResult errors){
		ResponseEntity<?> resp=null;
		String id=null;
		HashMap<String,String> errorsMap=null;
		
		try {
			log.info("Enter into ResponseEntity saveUom method ");
			if (errors.hasErrors()) {
				errorsMap=new HashMap<>();
				for (FieldError err :errors.getFieldErrors()) {
					errorsMap.put(err.getField(),err.getDefaultMessage());
				}
				resp=new ResponseEntity<HashMap<String,String>>(errorsMap,HttpStatus.BAD_REQUEST);
			} else {
				//Save data
				id=service.saveUom(uom);
				resp=new ResponseEntity<String>("Uom "+id+"Saved With id",HttpStatus.CREATED);
			}
		} catch (Exception e) {
			resp=new ResponseEntity<String>("Uom unable to save ",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Return Response Entity Object");
		return resp;
	}
	
	//5.update one record into DB
	@PutMapping("/modify")
	public ResponseEntity<String > updateUom(@RequestBody Uom uom){
		ResponseEntity<String> resp=null;
		try {
			log.info("Enter into ResponseEntity updateUom method ");
			if(uom.getId()==null || !service.isUomExist(uom.getId()) ) {
				resp = new ResponseEntity<String>(
						"Given Uom not exist in DB",
						HttpStatus.NOT_FOUND);
			}else {
				service.updateUom(uom);
				resp = new ResponseEntity<String>(
						"Uom '"+uom.getId()+"' Updated",
						HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error("Enable to process ResponseEntity "+e.getMessage());
			e.printStackTrace();
		}
		return resp;
	}
	
}
