package in.nit.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.nit.model.Grn;
import in.nit.repo.GrnRepository;
import in.nit.service.IGrnService;
@Service
public class GrnServiceImpl implements IGrnService {
	
	@Autowired
	private GrnRepository repo;
	
	@Override
	@Transactional
	public Integer saveGrn(Grn grn) {
		
		return repo.save(grn).getId();
	}

	@Override
	@Transactional
	public void updateGrn(Grn grn) {
		repo.save(grn);
		
	}

	@Override
	@Transactional
	public void deleteGrn(Integer id) {
		repo.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Grn> getOneGrn(Integer id) {
		
		return repo.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Grn> getAllGrns() {
		
		return repo.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isGrnExist(Integer id) {
		
		return repo.existsById(id);
	}

}
