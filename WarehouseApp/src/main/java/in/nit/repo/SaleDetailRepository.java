package in.nit.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.nit.model.SaleDetail;

public interface SaleDetailRepository extends JpaRepository<SaleDetail,Integer> {
	
	//Stage#4 Display all Parts which are added to PO as a HTML table below to FORM
	//with dummy Remove button
	@Query("SELECT SDTL FROM SaleDetail SDTL INNER JOIN SDTL.so AS SO WHERE SO.id=:saleId")
	public List<SaleDetail> getSaleDtlWithSoId(Integer saleId);
	
	//Stage#6 Confirm Button Operation with status conditions
	@Query("SELECT count(SDTL.id) FROM SaleDetail SDTL INNER JOIN SDTL.so AS SO WHERE SO.id=:saleId")
	 public Integer getSaleDetailCountWithSoId(Integer saleId);

}
