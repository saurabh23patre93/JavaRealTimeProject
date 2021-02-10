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
@Table(name = "wh_user_type_tab")
public class WhUserType {
	@Id
	@GeneratedValue
//	@GeneratedValue(generator = "wh_user_type_seq_name")
//	@GenericGenerator(name = "wh_user_type_seq_name",strategy = "in.nit.generator.WhUserTypeGenerator")
	@Column(name = "wh_user_type_id_col")
	private Integer id;
	
	@Column(name = "user_type_col",length = 8,nullable = false)
	private String userType;
	
	@Column(name = "user_code_col",length = 15,nullable = false)
	private String userCode;
	
	@Column(name = "user_for_col",length = 15,nullable = false)
	private String userFor;//="Purchase/Sale";
	
	@Column(name = "user_email_col",length = 250,nullable = false)
	private String userEmail;
	
	@Column(name = "user_contact_col",length = 15,nullable = false)
	private String userContact;
	
	@Column(name = "user_id_type_col",length = 15,nullable = false)
	private String userIdType;
	
	@Column(name = "if_other_col",length = 15,nullable = false)
	private String ifOther;
	
	@Column(name = "id_number_col",length = 15,nullable = false)
	private String idNumber;
}
