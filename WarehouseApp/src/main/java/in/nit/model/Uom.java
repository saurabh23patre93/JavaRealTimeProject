package in.nit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name = "uom_tab")
public class Uom {
	@Id
	//@GeneratedValue
	@GeneratedValue(generator = "uom_seq_name")
	@GenericGenerator(name = "uom_seq_name",strategy = "in.nit.generator.UomGenerator")
	@Column(name="uom_id_col")
	private String id;
	
	@Column(name = "uom_type_col",length = 15,nullable = false)
	@NotNull(message = "Uom Type can not be null")
	private String uomType;
	
	@Column(name = "uom_model_col",length = 15,nullable = false)
	@NotNull(message = "Uom Model can not be null")
	@Pattern(regexp = "[A-Z]{3,6}",message = "Invalid Pattern Entered")
	private String uomModel;
	
	@Column(name = "description_col",length = 180,nullable = false)
	@Size(min = 2,max = 10,message = "Length must be 2-10 chars only")
	@NotNull(message = "Uom description can not be null")
	private String description;
	
}
