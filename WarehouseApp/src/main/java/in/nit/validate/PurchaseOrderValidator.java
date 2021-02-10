package in.nit.validate;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.nit.model.PurchaseOrder;
import in.nit.service.IPurchaseOrderService;
import in.nit.service.IShipmentTypeService;
import in.nit.service.IWhUserTypeService;

@Component
public class PurchaseOrderValidator {
	@Autowired
	private IPurchaseOrderService service;
	
	@Autowired
	private IShipmentTypeService shipmentTypeservice;
	
	@Autowired
	private IWhUserTypeService whUserTypeservice;
	
	public Map<String,String> validate(PurchaseOrder purchaseOrder){
		Map<String,String> errors=new HashMap<>();
		
		if (purchaseOrder.getOrderCode()==null || purchaseOrder.getOrderCode().isEmpty()) {
			errors.put("purchaseOrderCode","Invalid PurchaseOrder Code Entered");
		} else if (service.isPurchaseOrderCodeExist(purchaseOrder.getOrderCode())) {
			errors.put("purchaseOrderCode","PurchaseOrder Code already exist Entered");
		}
		
		//Integration Validation
		if (purchaseOrder.getShipmentType()==null || purchaseOrder.getShipmentType().getId()<=0) {
			errors.put("shipmentType","Invalid ShipmentType Id provided");
		} else if(!shipmentTypeservice.isShipmentTypeExist(purchaseOrder.getShipmentType().getId())){
			errors.put("shipmentType","ShipmentType Id not exist");
		}
		
		if (purchaseOrder.getWhUserType()==null || purchaseOrder.getWhUserType().getId()<=0) {
			errors.put("whUserType","Invalid WhUserType Id provided");
		} else if(!whUserTypeservice.isWhUserTypeExist(purchaseOrder.getWhUserType().getId())){
			errors.put("whUserType","WhUserType Id not exist");
		}
		return errors;
	}
}
