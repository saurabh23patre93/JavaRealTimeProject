package in.nit.controller.rest;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import in.nit.model.WhUserType;
import in.nit.service.IWhUserTypeService;

@RestController
@RequestMapping("/rest/whusertype")
public class WhUserTypeRestController {
	//Create Logger Object
	private Logger log=LoggerFactory.getLogger(WhUserTypeRestController.class);
	
	@Autowired
	private IWhUserTypeService service;
	
	//1.Get all Records From DB
	@GetMapping("/all")
	public ResponseEntity<List<WhUserType>> getAll(){
		List<WhUserType> list=null;
		
		try {
			log.info("Enter into ResponseEntity getAll method Of WhUserType");
			list=service.getAllWhUserTypes();	
		} catch (Exception e) {
			log.error("Enable to process ResponseEntity "+e.getMessage());
			e.printStackTrace();
		}
		
		//Return ResponseEntity.ok(list)
		log.info("Return Response Entity Object");
		return new ResponseEntity<List<WhUserType>>(list,
										HttpStatus.OK);
	}
	
	//2.Get One Record from DB
	@GetMapping("/one/{id}")
	public ResponseEntity<?> getOneWhUserType(@PathVariable Integer id){
		Optional<WhUserType> opt=null;
		ResponseEntity<?> resp=null;
		
		try {
			log.info("Enter into ResponseEntity GetOneWhUserType method "+id);
			opt=service.getOneWhUserType(id);
			
			if (opt.isPresent()) {
				resp=new ResponseEntity<WhUserType>(opt.get(),
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
	public ResponseEntity<String> deleteOneWhUserType(@PathVariable Integer id){
		ResponseEntity<String> resp=null;
		
		try {
			log.info("Enter into ResponseEntity deleteOneWhUserType method ");
			if (service.isWhUserTypeExist(id)) {
				try {
					service.deleteWhUserType(id);
					resp=new ResponseEntity<String>("WhUserType Deleted", HttpStatus.OK);
				} catch (Exception e) {
					resp=new ResponseEntity<String>("WhUserType Can not be deleted",HttpStatus.BAD_REQUEST);
				}
			} else {
				resp=new ResponseEntity<String>("WhUserType not exist",HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Enable to process ResponseEntity "+e.getMessage());
			e.printStackTrace();
		}
		
		log.info("Return Response Entity Object");
		return resp;
	}
	
	//4.save one record into DB
	@PostMapping("/insert")
	public ResponseEntity<String> saveWhUserType(@RequestBody WhUserType WhUserType){
		ResponseEntity<String> resp=null;
		Integer id=null;
		
		try {
			log.info("Enter into ResponseEntity saveWhUserType method ");
			id=service.saveWhUserType(WhUserType);
			resp=new ResponseEntity<String>("WhUserType "+id+"Saved With id",HttpStatus.CREATED);
		} catch (Exception e) {
			resp=new ResponseEntity<String>("WhUserType unable to save ",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Return Response Entity Object");
		return resp;
	}
	
	//5.update one record into DB
	@PutMapping("/modify")
	public ResponseEntity<String > updateWhUserType(@RequestBody WhUserType WhUserType){
		ResponseEntity<String> resp=null;
		try {
			log.info("Enter into ResponseEntity updateWhUserType method ");
			if(WhUserType.getId()==null || !service.isWhUserTypeExist(WhUserType.getId()) ) {
				resp = new ResponseEntity<String>(
						"Given WhUserType not exist in DB",
						HttpStatus.NOT_FOUND);
			}else {
				service.updateWhUserType(WhUserType);
				resp = new ResponseEntity<String>(
						"WhUserType '"+WhUserType.getId()+"' Updated",
						HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error("Enable to process ResponseEntity "+e.getMessage());
			e.printStackTrace();
		}
		return resp;
	}
	
}
