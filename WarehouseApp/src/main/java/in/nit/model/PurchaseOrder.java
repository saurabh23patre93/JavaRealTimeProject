package in.nit.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name = "purchase_order_tab")
public class PurchaseOrder {
	@Id
	@GeneratedValue
//	@GeneratedValue(generator = "purchase_order_seq_name")
//	@GenericGenerator(name = "purchase_order_seq_name",strategy = "in.nit.generator.PurchaseOrderGenerator")
	@Column(name = "purchase_order_id_col")
	private Integer id;
	
	@Column(name = "order_code_col",length = 18,nullable = false)
	private String orderCode;
	
	@Column(name = "reference_number_col",length = 18,nullable = false)
	private String referenceNumber;
	
	@Column(name = "quality_check_col",length = 18,nullable = false)
	private String qualityCheck;
	
	@Column(name = "status_col",length = 18,nullable = false)
	private String status;
	
	@Column(name = "description_col",length = 180,nullable = false)
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "shipment_type_id_col_fk")
	private ShipmentType shipmentType;
	
	@ManyToOne
	@JoinColumn(name = "whusertype_id_col_fk")
	private WhUserType whUserType;
	
	//Bidirectional mapping
	@OneToMany(mappedBy = "po",fetch = FetchType.EAGER)
	private List<PurchaseDetail> dtls;
}
