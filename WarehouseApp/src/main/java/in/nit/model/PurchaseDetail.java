package in.nit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
@Table(name = "purchase_detail_tab")
public class PurchaseDetail {
	@Id
	@GeneratedValue
//	@GeneratedValue(generator = "purchase_detail_seq_name")
//	@GenericGenerator(name = "purchase_detail_seq_name",strategy = "in.nit.generator.PurchaseDetailGenerator")
	@Column(name = "purc_dtl_id_col")
	private Integer id;
	
	@Transient
	private Integer slno;
	
	@Column(name = "purc_dtl_qty_col")
	private Integer qty;
	
	@ManyToOne
	@JoinColumn(name = "part_id_fk")
	private Part part;
	
	@ManyToOne
	@JoinColumn(name = "po_id_fk")
	private PurchaseOrder po;
	
}
