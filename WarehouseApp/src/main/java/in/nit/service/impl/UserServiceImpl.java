package in.nit.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.nit.model.User;
import in.nit.repo.UserRepository;
import in.nit.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private UserRepository repo;
	
	@Override
	@Transactional
	public Integer saveUser(User user) {
		
		return repo.save(user).getId();
	}

	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteUser(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Optional<User> getOneUser(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> getAllUsers() {
		
		return  repo.findAll();
	}

	@Override
	public boolean isUserExist(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<User> getAllUsers(Pageable pageable) {
		
		return repo.findAll(pageable);
	}

	@Override
	@Transactional
	public Integer updateUserStatus(boolean status, Integer id) {
		
		return repo.updateUserStatus(status, id);
	}

}
