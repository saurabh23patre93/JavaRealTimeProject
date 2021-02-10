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

import in.nit.model.SaleOrder;
import in.nit.service.ISaleOrderService;
import in.nit.validate.SaleOrderValidator;

@RestController
@RequestMapping("/rest/saleorder")
public class SaleOrderRestController {
	
	//Create Logger Object
	private Logger log=LoggerFactory.getLogger(SaleOrderRestController.class);
	
	@Autowired
	private ISaleOrderService service;
	
	@Autowired
	private SaleOrderValidator validator;
	
	//1.Get all Records From DB
		@GetMapping("/all")
		public ResponseEntity<List<SaleOrder>> getAll(){
			List<SaleOrder> list=null;
			
			try {
				log.info("Enter into ResponseEntity getAll method Of SaleOrder");
				list=service.getAllSaleOrders();	
			} catch (Exception e) {
				log.error("Enable to process ResponseEntity "+e.getMessage());
				e.printStackTrace();
			}
			
			//Return ResponseEntity.ok(list)
			log.info("Return Response Entity Object");
			return new ResponseEntity<List<SaleOrder>>(list,
											HttpStatus.OK);
		}
		
		//2.Get One Record from DB
		@GetMapping("/one/{id}")
		public ResponseEntity<?> getOneSaleOrder(@PathVariable Integer id){
			Optional<SaleOrder> opt=null;
			ResponseEntity<?> resp=null;
			
			try {
				log.info("Enter into ResponseEntity GetOneSaleOrder method "+id);
				opt=service.getOneSaleOrder(id);
				
				if (opt.isPresent()) {
					resp=new ResponseEntity<SaleOrder>(opt.get(),
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
		public ResponseEntity<String> deleteOneSaleOrder(@PathVariable Integer id){
			ResponseEntity<String> resp=null;
			
			try {
				log.info("Enter into ResponseEntity deleteOneSaleOrder method ");
				if (service.isSaleOrderExist(id)) {
					try {
						service.deleteSaleOrder(id);
						resp=new ResponseEntity<String>("SaleOrder Deleted", HttpStatus.OK);
					} catch (Exception e) {
						resp=new ResponseEntity<String>("SaleOrder Can not be deleted",HttpStatus.BAD_REQUEST);
					}
				} else {
					resp=new ResponseEntity<String>("SaleOrder not exist",HttpStatus.NOT_FOUND);
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
	public ResponseEntity<?> saveSaleOrder(@RequestBody SaleOrder saleOrder){
		ResponseEntity<?> resp=null;
		Map<String,String> errors=null;
		Integer id=null;
		
		errors=validator.validate(saleOrder);
		if (errors.isEmpty()) {
			id=service.saveSaleOrder(saleOrder);
			resp=new ResponseEntity<>("SaleOrder "+id+" saved ",HttpStatus.CREATED);
		} else {
			resp=new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
		}
		
		return resp;
	}
	
	//5.update one record into DB
		@PutMapping("/modify")
		public ResponseEntity<String > updateSaleOrder(@RequestBody SaleOrder SaleOrder){
			ResponseEntity<String> resp=null;
			try {
				log.info("Enter into ResponseEntity updateSaleOrder method ");
				if(SaleOrder.getId()==null || !service.isSaleOrderExist(SaleOrder.getId()) ) {
					resp = new ResponseEntity<String>(
							"Given SaleOrder not exist in DB",
							HttpStatus.NOT_FOUND);
				}else {
					service.updateSaleOrder(SaleOrder);
					resp = new ResponseEntity<String>(
							"SaleOrder '"+SaleOrder.getId()+"' Updated",
							HttpStatus.OK);
				}
			} catch (Exception e) {
				log.error("Enable to process ResponseEntity "+e.getMessage());
				e.printStackTrace();
			}
			return resp;
		}
}
