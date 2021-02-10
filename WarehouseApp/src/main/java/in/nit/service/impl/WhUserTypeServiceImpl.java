package in.nit.service.impl;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.nit.model.WhUserType;
import in.nit.repo.WhUserTypeRepository;
import in.nit.service.IWhUserTypeService;

@Service
public class WhUserTypeServiceImpl  implements IWhUserTypeService{
	@Autowired
	private WhUserTypeRepository repo;
	
	@Transactional
	public Integer saveWhUserType(WhUserType wut) {
		Integer id=null;
		
		id=repo.save(wut).getId();
		return id;
	}
	
	@Transactional
	public void updateWhUserType(WhUserType wut) {
		
		repo.save(wut);
	}
	
	@Transactional
	public void deleteWhUserType(Integer id) {
		
		repo.deleteById(id);
	}
	
	@Transactional(readOnly = true)
	public Optional<WhUserType> getOneWhUserType(Integer id) {
		Optional<WhUserType> opt=null;
		
		opt=repo.findById(id);
//		if (opt.isPresent()) {
//			return opt.get();
//		}
		return opt;
	}
	
	@Transactional(readOnly = true)
	public List<WhUserType> getAllWhUserTypes() {
		List<WhUserType> list=null;
		
		list=repo.findAll();
		return list;
	}
	
	@Transactional(readOnly = true)
	public boolean isWhUserTypeExist(Integer id) {
		boolean exist=repo.existsById(id);
		return exist;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isWhUserTypeEmailExist(String mail) {
		// TODO Auto-generated method stub
		return repo.getWhUserTypeCount(mail)>0?true:false;
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Integer, String> getWhUserTypeIdAndCode(String userType) {
		/*
		Map<String,String> map=repo.getWhUserTypeIdAndModel()
								.stream()
								.collect(Collectors.toMap(
										array->array[0].toString(),
										array->array[1].toString()));
		*/
		Map<Integer,String> map=new LinkedHashMap<>();
		List<Object[]> list=repo.getWhUserTypeIdAndCode(userType);
		for(Object[] ob:list) {
			map.put(Integer.valueOf(ob[0].toString()),ob[1].toString());
		}
		return map;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isWhUserTypeCodeExist(String userCode) {
		int count=repo.getUserCodeCount(userCode);
		boolean flag=(count>0?true:false);
		return flag;
	}
}
