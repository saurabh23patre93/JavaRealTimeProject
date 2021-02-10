package in.nit.model;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Data;

@Data
@Entity
@Table(name = "sale_order_tab")
public class SaleOrder {
	@Id
	@GeneratedValue
//	@GeneratedValue(generator = "sale_order_seq_name")
//	@GenericGenerator(name = "sale_order_seq_name",strategy = "in.nit.generator.SaleOrderGenerator")
	@Column(name = "sale_order_id_col")
	private Integer id;
	
	@Column(name = "order_code_col",length = 18,nullable = false)
	private String orderCode;
	
	@Column(name = "reference_number_col",length = 18,nullable = false)
	private String referenceNumber;
	
	@Column(name = "stock_mode_col",length = 15,nullable = false)
	private String stockMode;
	
	@Column(name = "stock_source_col",length = 7,nullable = false)
	private String stockSource;
	
	@Column(name = "default_status_col",length = 18,nullable = false)
	private String defaultStatus;
	
	@Column(name = "description_col",length = 180,nullable = false)
	private String description;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "shipment_type_id_col_fk")
	private ShipmentType shipmentType;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "whusertype_id_col_fk")
	private WhUserType whUserType;
	
	//Bidirectional mapping
	@OnDelete(action = OnDeleteAction.CASCADE)
	@OneToMany(mappedBy = "so",fetch = FetchType.EAGER)
	private List<SaleDetail> dtls;
}

