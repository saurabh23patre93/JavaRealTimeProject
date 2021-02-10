package in.nit.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import in.nit.model.ShipmentType;

public interface IShipmentTypeService {
	
	public Integer saveShipmentType(ShipmentType st);
	public void updateShipmentType(ShipmentType st);
	
	public void deleteShipmentType(Integer id);
	public Optional<ShipmentType> getOneShipmentType(Integer id);
	
	public List<ShipmentType> getAllShipmentTypes();
	public boolean isShipmentTypeExist(Integer id);
	
	boolean isShipmentTypeCodeExist(String shipmentCode);
	public List<Object[]> getShipmentModeCount();
	
	//For Model Integration with PurchaseOrder Model
	Map<Integer,String> getShipmentTypeIdAndModel();
	//For pagination Purpose
	Page<ShipmentType> getAllShipmentTypes(Pageable pageable);
}
