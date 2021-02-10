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

import in.nit.controller.PartController;
import in.nit.model.ShipmentType;
import in.nit.service.IShipmentTypeService;

@RestController
@RequestMapping("/rest/shipmenttype")
public class ShipmentTypeRestController {
	//Create Logger Object
	private Logger log=LoggerFactory.getLogger(ShipmentTypeRestController.class);
	
	@Autowired
	private IShipmentTypeService service;
	
	//1.Get all Records From DB
	@GetMapping("/all")
	public ResponseEntity<List<ShipmentType>> getAll(){
		List<ShipmentType> list=null;
		
		try {
			log.info("Enter into ResponseEntity getAll method Of ShipmentType");
			list=service.getAllShipmentTypes();	
		} catch (Exception e) {
			log.error("Enable to process ResponseEntity "+e.getMessage());
			e.printStackTrace();
		}
		
		//Return ResponseEntity.ok(list)
		log.info("Return Response Entity Object");
		return new ResponseEntity<List<ShipmentType>>(list,
										HttpStatus.OK);
	}
	
	//2.Get One Record from DB
	@GetMapping("/one/{id}")
	public ResponseEntity<?> getOneShipmentType(@PathVariable Integer id){
		Optional<ShipmentType> opt=null;
		ResponseEntity<?> resp=null;
		
		try {
			log.info("Enter into ResponseEntity GetOneShipmentType method "+id);
			opt=service.getOneShipmentType(id);
			
			if (opt.isPresent()) {
				resp=new ResponseEntity<ShipmentType>(opt.get(),
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
	public ResponseEntity<String> deleteOneShipmentType(@PathVariable Integer id){
		ResponseEntity<String> resp=null;
		
		try {
			log.info("Enter into ResponseEntity deleteOneShipmentType method ");
			if (service.isShipmentTypeExist(id)) {
				try {
					service.deleteShipmentType(id);
					resp=new ResponseEntity<String>("ShipmentType Deleted", HttpStatus.OK);
				} catch (Exception e) {
					resp=new ResponseEntity<String>("ShipmentType Can not be deleted",HttpStatus.BAD_REQUEST);
				}
			} else {
				resp=new ResponseEntity<String>("ShipmentType not exist",HttpStatus.NOT_FOUND);
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
	public ResponseEntity<String> saveShipmentType(@RequestBody ShipmentType ShipmentType){
		ResponseEntity<String> resp=null;
		Integer id=null;
		
		try {
			log.info("Enter into ResponseEntity saveShipmentType method ");
			id=service.saveShipmentType(ShipmentType);
			resp=new ResponseEntity<String>("ShipmentType "+id+"Saved With id",HttpStatus.CREATED);
		} catch (Exception e) {
			resp=new ResponseEntity<String>("ShipmentType unable to save ",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Return Response Entity Object");
		return resp;
	}
	
	//5.update one record into DB
	@PutMapping("/modify")
	public ResponseEntity<String > updateShipmentType(@RequestBody ShipmentType ShipmentType){
		ResponseEntity<String> resp=null;
		try {
			log.info("Enter into ResponseEntity updateShipmentType method ");
			if(ShipmentType.getId()==null || !service.isShipmentTypeExist(ShipmentType.getId()) ) {
				resp = new ResponseEntity<String>(
						"Given ShipmentType not exist in DB",
						HttpStatus.NOT_FOUND);
			}else {
				service.updateShipmentType(ShipmentType);
				resp = new ResponseEntity<String>(
						"ShipmentType '"+ShipmentType.getId()+"' Updated",
						HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error("Enable to process ResponseEntity "+e.getMessage());
			e.printStackTrace();
		}
		return resp;
	}
	
}
