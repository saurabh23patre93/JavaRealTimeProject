package in.nit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "grn_tab")
public class Grn {
	@GeneratedValue
	@Id
	@Column(name="grn_id_col")
	private Integer id;
	
	@Column(name="grn_code_col")
	private String grnCode;
	
	@Column(name="grn_type_col")
	private String grnType;
	
	//private PurchaseOrder po;
	
	//private List<GrnDetail> dtls;
	
	@Column(name="grn_description_col")
	private String description;
}
