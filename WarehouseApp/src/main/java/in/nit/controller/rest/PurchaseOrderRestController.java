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

import in.nit.model.PurchaseOrder;
import in.nit.service.IPurchaseOrderService;
import in.nit.validate.PurchaseOrderValidator;

@RestController
@RequestMapping("/rest/purchaseorder")
public class PurchaseOrderRestController {
	
	//Create Logger Object
	private Logger log=LoggerFactory.getLogger(PurchaseOrderRestController.class);
	
	@Autowired
	private IPurchaseOrderService service;
	
	@Autowired
	private PurchaseOrderValidator validator;
	
	//1.Get all Records From DB
		@GetMapping("/all")
		public ResponseEntity<List<PurchaseOrder>> getAll(){
			List<PurchaseOrder> list=null;
			
			try {
				log.info("Enter into ResponseEntity getAll method Of PurchaseOrder");
				list=service.getAllPurchaseOrders();	
			} catch (Exception e) {
				log.error("Enable to process ResponseEntity "+e.getMessage());
				e.printStackTrace();
			}
			
			//Return ResponseEntity.ok(list)
			log.info("Return Response Entity Object");
			return new ResponseEntity<List<PurchaseOrder>>(list,
											HttpStatus.OK);
		}
		
		//2.Get One Record from DB
		@GetMapping("/one/{id}")
		public ResponseEntity<?> getOnePurchaseOrder(@PathVariable Integer id){
			Optional<PurchaseOrder> opt=null;
			ResponseEntity<?> resp=null;
			
			try {
				log.info("Enter into ResponseEntity GetOnePurchaseOrder method "+id);
				opt=service.getOnePurchaseOrder(id);
				
				if (opt.isPresent()) {
					resp=new ResponseEntity<PurchaseOrder>(opt.get(),
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
		public ResponseEntity<String> deleteOnePurchaseOrder(@PathVariable Integer id){
			ResponseEntity<String> resp=null;
			
			try {
				log.info("Enter into ResponseEntity deleteOnePurchaseOrder method ");
				if (service.isPurchaseOrderExist(id)) {
					try {
						service.deletePurchaseOrder(id);
						resp=new ResponseEntity<String>("PurchaseOrder Deleted", HttpStatus.OK);
					} catch (Exception e) {
						resp=new ResponseEntity<String>("PurchaseOrder Can not be deleted",HttpStatus.BAD_REQUEST);
					}
				} else {
					resp=new ResponseEntity<String>("PurchaseOrder not exist",HttpStatus.NOT_FOUND);
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
	public ResponseEntity<?> savePurchaseOrder(@RequestBody PurchaseOrder PurchaseOrder){
		ResponseEntity<?> resp=null;
		Map<String,String> errors=null;
		Integer id=null;
		
		errors=validator.validate(PurchaseOrder);
		if (errors.isEmpty()) {
			id=service.savePurchaseOrder(PurchaseOrder);
			resp=new ResponseEntity<>("PurchaseOrder "+id+" saved ",HttpStatus.CREATED);
		} else {
			resp=new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
		}
		
		return resp;
	}
	
	//5.update one record into DB
		@PutMapping("/modify")
		public ResponseEntity<String > updatePurchaseOrder(@RequestBody PurchaseOrder PurchaseOrder){
			ResponseEntity<String> resp=null;
			try {
				log.info("Enter into ResponseEntity updatePurchaseOrder method ");
				if(PurchaseOrder.getId()==null || !service.isPurchaseOrderExist(PurchaseOrder.getId()) ) {
					resp = new ResponseEntity<String>(
							"Given PurchaseOrder not exist in DB",
							HttpStatus.NOT_FOUND);
				}else {
					service.updatePurchaseOrder(PurchaseOrder);
					resp = new ResponseEntity<String>(
							"PurchaseOrder '"+PurchaseOrder.getId()+"' Updated",
							HttpStatus.OK);
				}
			} catch (Exception e) {
				log.error("Enable to process ResponseEntity "+e.getMessage());
				e.printStackTrace();
			}
			return resp;
		}
}
