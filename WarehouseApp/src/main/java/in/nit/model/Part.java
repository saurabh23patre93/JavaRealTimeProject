package in.nit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name = "part_tab")
public class Part {
	@Id
	@GeneratedValue(generator = "part_seq_name")
	@GenericGenerator(name = "part_seq_name",strategy = "in.nit.generator.PartGenerator")
	@Column(name = "part_id_col")
	private String id;
	
	@Column(name = "part_code_col",length = 10,nullable = false)
	private String partCode;
	
	@Column(name = "part_width_col",length = 10,nullable = false)
	private Double partWidth;
	
	@Column(name = "part_length_col",length = 10,nullable = false)
	private Double partLength;
	
	@Column(name = "part_hight_col",length = 10,nullable = false)
	private Double partHight;
	
	@Column(name = "base_cost_col",length = 10,nullable = false)
	private Double baseCost;
	
	@Column(name = "base_currency_col",length = 10,nullable = false)
	private String baseCurrency;
	
	@Column(name = "description_col",length = 35,nullable = false)
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "uom_id_col_fk")
	private Uom uom;
}
