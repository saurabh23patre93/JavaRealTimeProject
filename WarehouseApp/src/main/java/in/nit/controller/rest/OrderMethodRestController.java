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
import in.nit.model.OrderMethod;
import in.nit.service.IOrderMethodService;

@RestController
@RequestMapping("/rest/ordermethod")
public class OrderMethodRestController {
	//Create Logger Object
	private Logger log=LoggerFactory.getLogger(OrderMethodRestController.class);
	
	@Autowired
	private IOrderMethodService service;
	
	//1.Get all Records From DB
	@GetMapping("/all")
	public ResponseEntity<List<OrderMethod>> getAll(){
		List<OrderMethod> list=null;
		
		try {
			log.info("Enter into ResponseEntity getAll method Of OrderMethod");
			list=service.getAllOrderMethods();	
		} catch (Exception e) {
			log.error("Enable to process ResponseEntity "+e.getMessage());
			e.printStackTrace();
		}
		
		//Return ResponseEntity.ok(list)
		log.info("Return Response Entity Object");
		return new ResponseEntity<List<OrderMethod>>(list,
										HttpStatus.OK);
	}
	
	//2.Get One Record from DB
	@GetMapping("/one/{id}")
	public ResponseEntity<?> getOneOrderMethod(@PathVariable String id){
		Optional<OrderMethod> opt=null;
		ResponseEntity<?> resp=null;
		
		try {
			log.info("Enter into ResponseEntity GetOneOrderMethod method "+id);
			opt=service.getOneOrderMethod(id);
			
			if (opt.isPresent()) {
				resp=new ResponseEntity<OrderMethod>(opt.get(),
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
	public ResponseEntity<String> deleteOneOrderMethod(@PathVariable String id){
		ResponseEntity<String> resp=null;
		
		try {
			log.info("Enter into ResponseEntity deleteOneOrderMethod method ");
			if (service.isOrderMethodExist(id)) {
				try {
					service.deleteOrderMethod(id);
					resp=new ResponseEntity<String>("OrderMethod Deleted", HttpStatus.OK);
				} catch (Exception e) {
					resp=new ResponseEntity<String>("OrderMethod Can not be deleted",HttpStatus.BAD_REQUEST);
				}
			} else {
				resp=new ResponseEntity<String>("OrderMethod not exist",HttpStatus.NOT_FOUND);
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
	public ResponseEntity<String> saveOrderMethod(@RequestBody OrderMethod OrderMethod){
		ResponseEntity<String> resp=null;
		String id=null;
		
		try {
			log.info("Enter into ResponseEntity saveOrderMethod method ");
			id=service.saveOrderMethod(OrderMethod);
			resp=new ResponseEntity<String>("OrderMethod "+id+"Saved With id",HttpStatus.CREATED);
		} catch (Exception e) {
			resp=new ResponseEntity<String>("OrderMethod unable to save ",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Return Response Entity Object");
		return resp;
	}
	
	//5.update one record into DB
	@PutMapping("/modify")
	public ResponseEntity<String > updateOrderMethod(@RequestBody OrderMethod OrderMethod){
		ResponseEntity<String> resp=null;
		try {
			log.info("Enter into ResponseEntity updateOrderMethod method ");
			if(OrderMethod.getId()==null || !service.isOrderMethodExist(OrderMethod.getId()) ) {
				resp = new ResponseEntity<String>(
						"Given OrderMethod not exist in DB",
						HttpStatus.NOT_FOUND);
			}else {
				service.updateOrderMethod(OrderMethod);
				resp = new ResponseEntity<String>(
						"OrderMethod '"+OrderMethod.getId()+"' Updated",
						HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error("Enable to process ResponseEntity "+e.getMessage());
			e.printStackTrace();
		}
		return resp;
	}
	
}
