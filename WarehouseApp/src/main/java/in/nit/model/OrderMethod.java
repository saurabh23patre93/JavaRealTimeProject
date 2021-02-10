package in.nit.model;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name = "order_method_tab")
public class OrderMethod {
	@Id
	@GeneratedValue(generator = "order_method_seq_name")
	@GenericGenerator(name = "order_method_seq_name",strategy = "in.nit.generator.OrderMethodGenerator")
	@Column(name = "order_method_id_col")
	private String id;
	
	@Column(name = "order_method_col",length = 15,nullable = false)
	private String orderMode;
	
	@Column(name = "order_code_col",length = 15,nullable = false)
	private String orderCode;
	
	@Column(name = "order_type_col",length = 7,nullable = false)
	private String orderType;
	
	@ElementCollection
	@CollectionTable(name = "order_accept_tab",
					joinColumns = @JoinColumn(referencedColumnName = "order_method_id_col"))
	@Column(name = "order_accept_col",length = 15,nullable = false)
	private List<String> orderAccept;
	
	@Column(name = "description_col",length = 180,nullable = false)
	private String description;
}
