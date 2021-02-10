package in.nit.model;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "user_tab")
public class User {
	@Id
	@GeneratedValue
	@Column(name = "user_id_col")
	private Integer id;
	
	@Column(name = "user_name_col")
	private String name;
	
	@Column(name = "user_email_col")
	private String email;
	
	@Column(name = "user_pwd_col")
	private String password;
	
	@Column(name = "user_active_col")
	private boolean active;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "user_roles_tab",
					joinColumns = @JoinColumn(name = "id_join_id"))
	@Column(name = "user_role_col")
	private Set<String> roles;
}
