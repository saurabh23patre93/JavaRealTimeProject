package in.nit.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.nit.model.WhUserType;

public interface WhUserTypeRepository extends JpaRepository<WhUserType,Integer> {
	//For user mail
	@Query("SELECT COUNT(WH.userEmail)FROM WhUserType WH WHERE WH.userEmail=:mail")
	public Integer getWhUserTypeCount(String mail);
	
	//For Model integration with WhUserType Model
	@Query("SELECT WU.id,WU.userCode FROM WhUserType WU WHERE WU.userType=:userType")
	List<Object[]> getWhUserTypeIdAndCode(String userType);
	
	//for Jquery WhUserType code validation purpose
	@Query("SELECT COUNT(ST.userCode) FROM WhUserType ST WHERE ST.userCode=:code")
	public Integer getUserCodeCount(String code);
}
