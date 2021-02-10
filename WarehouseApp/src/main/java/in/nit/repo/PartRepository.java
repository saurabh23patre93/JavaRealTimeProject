package in.nit.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.nit.model.Part;


public interface PartRepository extends JpaRepository<Part, String> {

	//1.For Query partCode validation query
	@Query("SELECT COUNT(PT.partCode) FROM Part PT WHERE PT.partCode=:partCode")
	public Integer getPartCodeCount(String partCode);
	
	//2.For PIE and Bar Chart presantion query
	@Query("SELECT PT.partCode,COUNT(PT.partCode) FROM Part PT GROUP BY PT.partCode")
	public List<Object[]> getPartCodeCount();
	
	//For Inbound/OUTBOND Operation purpose to add Part
	@Query("SELECT P.id,P.partCode FROM Part P")
	public List<Object[]> getPartIdAndCode();

}
