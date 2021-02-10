package in.nit.edi;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.nit.model.Uom;
import in.nit.service.IUomService;
import in.nit.util.EmailUtil;
import in.nit.validate.UomValidator;

@Component
public class UomEditMQService {
	private Logger log=LoggerFactory.getLogger(UomEditMQService.class);
	
	@Autowired
	private IUomService service;
	@Autowired
	private EmailUtil util;
	@Value("${my.app.admin.email}")
	private String to;
	@Autowired
	private UomValidator validator;
	
	@JmsListener(destination = "uomcreate")
	public void createUom(String uomJson) {
		String message=null;
		ObjectMapper om=null;
		String id=null;
		Uom ob=null;
		Map<String,String> errors=null;
		
		try {
			log.info("UOM Edit Service-save Operation");
			om=new ObjectMapper();
			ob=om.readValue(uomJson,Uom.class);
			errors=validator.validate(ob);
			if (errors.isEmpty()) {
				//Perform save opertion
				id=service.saveUom(ob);
				message="UOM Edit Save Success: "+id;
				log.info(message);
			} else {
				message = "UOM CREATION FAILED WITH ERRORS => " + errors.values().toString();
				log.error(message);
			}
			
		} catch (Exception e) {
			message="UOM SAVE FAIL:" + e.getMessage();
			log.error(message);
			e.printStackTrace();
		}
		
		//Send one ack email to admin@support.com
		util.send(to,"Create-UOM",message);
	}
}
