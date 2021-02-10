package in.nit.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import in.nit.model.Part;

public interface IPartService {
	
	public String savePart(Part part);
	public void updatePart(Part part);
	
	public void deletePart(String id);
	public Optional<Part> getOnePart(String id);
	
	public List<Part> getAllParts();
	public boolean isPartExist(String id);
	
	//1.For AJAX client side validation
	boolean isPartCodeExist(String partCode);
	//2.For Represention PIE and BAR chart
	public List<Object[]> getPartCodeCount();
	
	//For inbound operation purpose to add part
	 Map<String,String> getPartIdAndCode();

}
