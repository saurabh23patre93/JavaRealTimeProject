package in.nit.validate;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.nit.model.Part;
import in.nit.service.IPartService;
import in.nit.service.IUomService;

@Component
public class PartValidator {
	
	@Autowired
	private IPartService service;
	
	@Autowired
	private IUomService uomservice;
	
	public Map<String,String> validate(Part part){
		Map<String,String> errors=new HashMap<>();
		
		if (part.getPartCode()==null || part.getPartCode().isEmpty()) {
			errors.put("partCode","Invalid Part Code Entered");
		} else if (service.isPartCodeExist(part.getPartCode())) {
			errors.put("partCode","Part Code already exist Entered");
		}
		
		//Integration Validation
		if (part.getUom()==null || part.getUom().getId().isEmpty()) {
			errors.put("uom","Invalid Uom Id provided");
		} else if(!uomservice.isUomExist(part.getUom().getId())){
			errors.put("uom","Uom Id not exist");
		}
		
		return errors;
	}
}
