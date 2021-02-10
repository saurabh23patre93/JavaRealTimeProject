package in.nit.edi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.nit.model.ShipmentType;
import in.nit.service.IShipmentTypeService;
import in.nit.util.EmailUtil;

@Component
public class ShipmentTypeEditMQService {
	private Logger log=LoggerFactory.getLogger(ShipmentTypeEditMQService.class);
	
	@Autowired
	private IShipmentTypeService service;
	@Autowired
	private EmailUtil util;
	@Value("${my.app.admin.email}")
	private String to;
	
	@JmsListener(destination = "shipmenttypecreate")
	public void createShipmentType(String shipmentTypeJson) {
		String message=null;
		ObjectMapper om=null;
		Integer id=null;
		ShipmentType ob=null;
		
		try {
			log.info("ShipmentType Edit Service-save Operation");
			om=new ObjectMapper();
			ob=om.readValue(shipmentTypeJson,ShipmentType.class);
			id=service.saveShipmentType(ob);
			message="ShipmentType Edit Save Success: "+id;
			log.info(message);
		} catch (Exception e) {
			message="ShipmentType SAVE FAIL:" + e.getMessage();
			log.error(message);
			e.printStackTrace();
		}
		
		//Send one ack email to admin@support.com
		util.send(to,"Crate-ShipmentType",message);
	}
}
