package in.nit.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.nit.model.ShipmentType;
import in.nit.repo.ShipmentTypeRepository;
import in.nit.service.IShipmentTypeService;
@Service
public class ShipmentTypeServiceImpl  implements IShipmentTypeService{
	@Autowired
	private ShipmentTypeRepository repo;
	
	@Transactional
	public Integer saveShipmentType(ShipmentType st) {
		Integer id=null;
		
		id=repo.save(st).getId();
		return id;
	}
	
	@Transactional
	public void updateShipmentType(ShipmentType st) {
		
		repo.save(st);
	}
	
	@Transactional
	public void deleteShipmentType(Integer id) {
		
		repo.deleteById(id);
	}
	
	@Transactional(readOnly = true)
	public Optional<ShipmentType> getOneShipmentType(Integer id) {
		Optional<ShipmentType> opt=null;
		
		opt=repo.findById(id);
//		if (opt.isPresent()) {
//			return opt.get();
//		}
		return opt;
	}
	
	@Transactional(readOnly = true)
	public List<ShipmentType> getAllShipmentTypes() {
		List<ShipmentType> list=null;
		
		list=repo.findAll();
		return list;
	}
	
	@Transactional(readOnly = true)
	public boolean isShipmentTypeExist(Integer id) {
		boolean exist=repo.existsById(id);
		return exist;
	}

	@Transactional(readOnly = true)
	public boolean isShipmentTypeCodeExist(String shipmentCode) {
		int count=repo.getShipmentCodeCount(shipmentCode);
		boolean flag = (count>0 ? true: false);  
		return flag;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Object[]> getShipmentModeCount() {
		
		return repo.getShipmentModeCount();
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Integer, String> getShipmentTypeIdAndModel() {
		/*
		Map<String,String> map=repo.getShipmentTypeIdAndModel()
								.stream()
								.collect(Collectors.toMap(
										array->array[0].toString(),
										array->array[1].toString()));
		*/
		Map<Integer,String> map=new LinkedHashMap<>();
		List<Object[]> list=repo.getShipmentTypeIdAndModel();
		for(Object[] ob:list) {
			map.put(Integer.valueOf( ob[0].toString()),ob[1].toString());
		}
		return map;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ShipmentType> getAllShipmentTypes(Pageable pageable) {
		
		return repo.findAll(pageable);
	}
}
