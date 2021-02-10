package in.nit.service;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import in.nit.model.WhUserType;

public interface IWhUserTypeService {
	
	public Integer saveWhUserType(WhUserType wut);
	public void updateWhUserType(WhUserType wut);
	
	public void deleteWhUserType(Integer id);
	public Optional<WhUserType> getOneWhUserType(Integer id);
	
	public List<WhUserType> getAllWhUserTypes();
	public boolean isWhUserTypeExist(Integer id);
	
	//For WhUserType code validation
	boolean isWhUserTypeCodeExist(String userCode);
	
	//For User MailPurpose
	boolean isWhUserTypeEmailExist(String mail);
	//Model Integration With Purchase Order Model
	Map<Integer,String> getWhUserTypeIdAndCode(String userType);
}
