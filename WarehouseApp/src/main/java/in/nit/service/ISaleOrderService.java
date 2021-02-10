package in.nit.service;

import java.util.List;
import java.util.Optional;

import in.nit.model.SaleDetail;
import in.nit.model.SaleOrder;

public interface ISaleOrderService {
	
	public Integer saveSaleOrder(SaleOrder saleOrder);
	public void updateSaleOrder(SaleOrder saleOrder);
	
	public void deleteSaleOrder(Integer id);
	public Optional<SaleOrder> getOneSaleOrder(Integer id);
	
	public List<SaleOrder> getAllSaleOrders();
	public boolean isSaleOrderExist(Integer id);
	
	//=====For OUTBond model intergaration
	public Integer addItemToSo(SaleDetail dtl);
	//Stage 4:Disply items in screen2
	public List<SaleDetail> getSaleDtlWithSoId(Integer saleId);
	
	//Define one method to delete row in IPurchaseOrderService and PurchaseServiceImpl
	public void deleteSaleDtl(Integer id);
	
	public void updateSaleOrderStatus(String status, Integer id);
	public Integer getSaleDtlCountWithSoId(Integer saleId);
}
