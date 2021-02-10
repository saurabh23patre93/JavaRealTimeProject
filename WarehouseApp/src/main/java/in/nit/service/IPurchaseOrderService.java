package in.nit.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import in.nit.model.PurchaseDetail;
import in.nit.model.PurchaseOrder;

public interface IPurchaseOrderService {
	
	public Integer savePurchaseOrder(PurchaseOrder purchaseOrder);
	public void updatePurchaseOrder(PurchaseOrder purchaseOrder);
	
	public void deletePurchaseOrder(Integer id);
	public Optional<PurchaseOrder> getOnePurchaseOrder(Integer id);
	
	public List<PurchaseOrder> getAllPurchaseOrders();
	public boolean isPurchaseOrderExist(Integer id);
	
	boolean isPurchaseOrderCodeExist(String orderCode);
	public List<Object[]> getOrderCodeCount();
	
	///==========================================================
	//For screen 2 operations
	public Integer addPartToPo(PurchaseDetail dtl);
	
	//To Display all part to po
	public List<PurchaseDetail> getPurchaseDetailWithPoId(Integer purchaseId);
	
	//In screen 2 operation for Remove button 
	public void deletePurchaseDetail(Integer id);
	
	//Stage#6 Confirm Button Operation with status conditions
	public void updatePurchaseOrderStatus(String status,Integer id);
	public Integer getPurchaseDetailCountWithPoId(Integer purchaseId);
	
	//For GRN controller used
	public Map<String,String> getPoIdAndCodeByStatus(String status);
}
