package in.nit.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.nit.model.SaleOrder;

public interface SaleOrderRepository extends JpaRepository<SaleOrder, Integer> {

	//Stage#6 Confirm Button Operation with status conditions
	@Modifying
	@Query("UPDATE SaleOrder SET defaultStatus=:status WHERE id=:id")
	public void updateSaleOrderStatus(String status,Integer id);

}
