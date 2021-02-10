package in.nit.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.nit.model.User;

public interface UserRepository extends JpaRepository<User,Integer> {

	//For update User status
	@Modifying
	@Query("UPDATE User SET active=:status WHERE id=:id")
	public Integer updateUserStatus(boolean status,Integer id);
	
	//For pagination Purpose
	public Page<User> findAll(Pageable pageable);
}
