package in.nit.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.nit.model.ShipmentType;

public interface ShipmentTypeRepository extends JpaRepository<ShipmentType,Integer> {
	//for Jquery shipment code validation purpose
	@Query("SELECT COUNT(ST.shipmentCode) FROM ShipmentType ST WHERE ST.shipmentCode=:code")
	public Integer getShipmentCodeCount(String code);
	
	//For pie chart and bar chart
	@Query("SELECT ST.shipmentMode,count(ST.shipmentMode) FROM ShipmentType ST GROUP BY ST.shipmentMode")
	public List<Object[]> getShipmentModeCount();
	
	//For Model Integration with PurchaseOrder Model
	@Query("SELECT id,shipmentMode FROM ShipmentType")
	List<Object[]> getShipmentTypeIdAndModel();

	//for pagination purpose
	Page<ShipmentType> findAll(Pageable pageable);
}
