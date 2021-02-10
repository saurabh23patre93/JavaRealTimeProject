package in.nit.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import in.nit.model.Uom;

public interface IUomService {
	
	public String saveUom(Uom uom);
	public void updateUom(Uom uom);
	
	public void deleteUom(String id);
	public Optional<Uom> getOneUom(String id);
	
	public List<Uom> getAllUoms();
	public boolean isUomExist(String id);
	
	//For uomModel AJAX validation
	boolean isUomModelExist(String uomModel);
	public List<Object[]> getUomModelCount();
	
	
	//Getting Map object from DB For Part model integration
	Map<String,String> getUomIdAndModel();
	//For Pagination Purpose
	Page<Uom> getAllUoms(Pageable pageable);
	
}
