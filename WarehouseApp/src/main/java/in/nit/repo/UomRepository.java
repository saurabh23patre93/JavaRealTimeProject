package in.nit.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.nit.model.Uom;

public interface UomRepository extends JpaRepository<Uom, String> {

	//For Jquery Ajax validation code
	@Query("SELECT COUNT(UM.uomModel) FROM Uom UM WHERE UM.uomModel=:uomModel")
	public Integer getUomModelCount(String uomModel);
	
	//for PIE chart and BAR chart
	@Query("SELECT UM.uomModel,COUNT(UM.uomModel) FROM Uom UM GROUP BY UM.uomModel")
	public List<Object[]> getUomModelCount();
	
	//For Model intergartion with Part model
	@Query("SELECT id,uomModel FROM Uom")
	List<Object[]> getUomIdAndModel();
	
	//For Pagination purpose
	Page<Uom> findAll(Pageable pageable);
}
