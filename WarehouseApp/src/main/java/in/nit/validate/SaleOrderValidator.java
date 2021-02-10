package in.nit.validate;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.nit.model.SaleOrder;
import in.nit.service.ISaleOrderService;
import in.nit.service.IShipmentTypeService;
import in.nit.service.IWhUserTypeService;

@Component
public class SaleOrderValidator {
	@Autowired
	private ISaleOrderService service;
	
	@Autowired
	private IShipmentTypeService shipmentTypeservice;
	
	@Autowired
	private IWhUserTypeService whUserTypeservice;
	
	public Map<String,String> validate(SaleOrder saleOrder){
		Map<String,String> errors=new HashMap<>();
		
		if (saleOrder.getOrderCode()==null || saleOrder.getOrderCode().isEmpty()) {
			errors.put("saleOrderCode","Invalid SaleOrder Code Entered");
		} else if (service.isSaleOrderExist((saleOrder.getId()))) {
			errors.put("saleOrderCode","SaleOrder Code already exist Entered");
		}
		
		//Integration Validation
		if (saleOrder.getShipmentType()==null || saleOrder.getShipmentType().getId()<=0) {
			errors.put("shipmentType","Invalid ShipmentType Id provided");
		} else if(!shipmentTypeservice.isShipmentTypeExist(saleOrder.getShipmentType().getId())){
			errors.put("shipmentType","ShipmentType Id not exist");
		}
		
		if (saleOrder.getWhUserType()==null || saleOrder.getWhUserType().getId()<=0) {
			errors.put("whUserType","Invalid WhUserType Id provided");
		} else if(!whUserTypeservice.isWhUserTypeExist(saleOrder.getWhUserType().getId())){
			errors.put("whUserType","WhUserType Id not exist");
		}
		return errors;
	}
}
