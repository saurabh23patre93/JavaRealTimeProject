package in.nit.validate;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.nit.model.Uom;
import in.nit.service.IUomService;

@Component
public class UomValidator {
	
	@Autowired
	private IUomService service;
	
	public Map<String,String> validate(Uom uom){
		Map<String,String> errors=new HashMap<>();
		
		if (uom.getUomType()==null||uom.getUomType().isEmpty()) {
			errors.put("uomType","Empty Uom Type is entered");
		}
		if(uom.getUomModel()==null || uom.getUomModel().isEmpty()) {
			errors.put("uomModel", "Empty Uom Model is Entered");
		}else if(service.isUomModelExist(uom.getUomModel())) {
			errors.put("uomModel", "Uom Model '"+uom.getUomModel()+"' already Exist");
		}
		return errors;
	}
}
