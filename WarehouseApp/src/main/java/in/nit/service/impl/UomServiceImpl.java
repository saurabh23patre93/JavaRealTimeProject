package in.nit.service.impl;

import java.lang.reflect.Array;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.nit.model.Uom;
import in.nit.repo.UomRepository;
import in.nit.service.IUomService;

@Service
public class UomServiceImpl  implements IUomService{
	@Autowired
	private UomRepository repo;
	
	@Transactional
	public String saveUom(Uom uom) {
		String id=null;
		
		id=repo.save(uom).getId();
		return id;
	}
	
	@Transactional
	public void updateUom(Uom uom) {
		
		repo.save(uom);
	}
	
	@Transactional
	public void deleteUom(String id) {
		
		repo.deleteById(id);
	}
	
	@Transactional(readOnly = true)
	public Optional<Uom> getOneUom(String id) {
		Optional<Uom> opt=null;
		
		opt=repo.findById(id);
//		if (opt.isPresent()) {
//			return opt.get();
//		}
		return opt;
	}
	
	@Transactional(readOnly = true)
	public List<Uom> getAllUoms() {
		List<Uom> list=null;
		
		list=repo.findAll();
		return list;
	}
	
	@Transactional(readOnly = true)
	public boolean isUomExist(String id) {
		boolean exist=repo.existsById(id);
		return exist;
	}

	
	@Override
	@Transactional(readOnly = true)
	public boolean isUomModelExist(String uomModel) {
		int count=0;
		
		count=repo.getUomModelCount(uomModel);
		boolean flag=(count>0 ? true:false);
		return flag;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Object[]> getUomModelCount() {
		
		return repo.getUomModelCount();
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, String> getUomIdAndModel() {
		List<Object[]> list=null;
		
		//Get map Object from repo
		//Convert Map object into List object using stream in java 8
		/*Map<String,String> map=repo.getUomIdAndModel().
							stream().
							collect(Collectors.toMap(
													array->array[0].toString(),
													array->array[1].toString()));	
		*/
		//Convert Map object into List array using for loop
		Map<String,String> map=new LinkedHashMap<>();
		list=repo.getUomIdAndModel();
		for(Object[] object:list) {
			map.put(object[0].toString(),object[1].toString());
		}
		return map;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Uom> getAllUoms(Pageable pageable) {
		
		return repo.findAll(pageable);
	}
}
