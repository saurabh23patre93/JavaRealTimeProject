package in.nit.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.nit.model.PurchaseOrder;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {
	//for Query Order  code validation purpose
	@Query("SELECT COUNT(ST.orderCode) FROM PurchaseOrder ST WHERE ST.orderCode=:code")
	public Integer getOrderCodeCount(String code);
	
	//For pie chart and bar chart
	@Query("SELECT ST.orderCode,count(ST.orderCode) FROM PurchaseOrder ST GROUP BY ST.orderCode")
	public List<Object[]> getOrderCodeCount();
	
	//Stage#6 Confirm Button Operation with status conditions
	@Modifying
	@Query("UPDATE PurchaseOrder SET status=:status WHERE id=:id")
	public void updatePurchaseOrderStatus(String status,Integer id);
	
	//For Status query invoiced
	@Query("SELECT PO.id,PO.orderCode FROM PurchaseOrder PO WHERE PO.status=:status")
	public List<Object[]> getPoIdAndCodeByStatus(String status);
}
