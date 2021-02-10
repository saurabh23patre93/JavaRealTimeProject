package in.nit.service;

import java.util.List;
import java.util.Optional;

import in.nit.model.Grn;


public interface IGrnService {
	
	public Integer saveGrn(Grn grn);
	public void updateGrn(Grn grn);
	public void deleteGrn(Integer id);
	public Optional<Grn> getOneGrn(Integer id);
	public List<Grn> getAllGrns();
	public boolean isGrnExist(Integer id);
}
