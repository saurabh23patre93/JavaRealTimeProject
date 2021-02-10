package in.nit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import in.nit.model.Uom;
import in.nit.model.User;

public interface IUserService {
	
	public Integer saveUser(User user);
	public void updateUser(User user);
	
	public void deleteUser(Integer id);
	public Optional<User> getOneUser(Integer id);
	
	//For Pagination Purpose
	public List<User> getAllUsers();
	public boolean isUserExist(Integer id);
	
	//for Pagination purpose
	public Page<User> getAllUsers(Pageable pageable);
	
	public Integer updateUserStatus(boolean status,Integer id);
}
