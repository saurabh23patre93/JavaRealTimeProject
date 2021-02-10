package in.nit.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.nit.model.Part;
import in.nit.repo.PartRepository;
import in.nit.service.IPartService;
@Service
public class PartServiceImpl  implements IPartService{
	@Autowired
	private PartRepository repo;
	
	@Transactional
	public String savePart(Part part) {
		String id=null;
		
		id=repo.save(part).getId();
		return id;
	}
	
	@Transactional
	public void updatePart(Part part) {
		
		repo.save(part);
	}
	
	@Transactional
	public void deletePart(String id) {
		
		repo.deleteById(id);
	}
	
	@Transactional(readOnly = true)
	public Optional<Part> getOnePart(String id) {
		Optional<Part> opt=null;
		
		opt=repo.findById(id);
//		if (opt.isPresent()) {
//			return opt.get();
//		}
		return opt;
	}
	
	@Transactional(readOnly = true)
	public List<Part> getAllParts() {
		List<Part> list=null;
		
		list=repo.findAll();
		return list;
	}
	
	@Transactional(readOnly = true)
	public boolean isPartExist(String id) {
		boolean exist=repo.existsById(id);
		return exist;
	}

	//For bar and PIE chart 
	@Override
	@Transactional(readOnly = true)
	public List<Object[]> getPartCodeCount() {
		
		return repo.getPartCodeCount();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isPartCodeExist(String partCode) {
		int count=repo.getPartCodeCount(partCode);
		boolean flag=(count>0? true:false);
		return flag;
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, String> getPartIdAndCode() {
		return repo.getPartIdAndCode()
				.stream()
				.collect(Collectors.toMap(
		  ob->ob[0].toString(), 
		  ob->ob[1].toString())
				);
		
	}
}
