package in.nit.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.nit.exception.custom.DataNotFoundException;
import in.nit.model.OrderMethod;
import in.nit.repo.OrderMethodRepository;
import in.nit.service.IOrderMethodService;

@Service
public class OrderMethodServiceImpl implements IOrderMethodService {
	
	@Autowired
	private OrderMethodRepository repo;
	
	@Override
	@Transactional
	public String saveOrderMethod(OrderMethod orm) {
		String id=null;
		
		id=repo.save(orm).getId();
		return id;
	}

	@Override
	@Transactional
	public void updateOrderMethod(OrderMethod orm) {
		Optional<OrderMethod> opt=repo.findById(orm.getId());
		
		if (opt==null) {
			throw new DataNotFoundException("Order Method '"+orm.getId()+"' Not Found");
		}
		
		repo.save(orm);
	}

	@Override
	@Transactional
	public void deleteOrderMethod(String id) {
		
		repo.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<OrderMethod> getOneOrderMethod(String id) {
		Optional<OrderMethod> opt=null;
		
		opt=repo.findById(id);
		if (opt==null) {
			throw new DataNotFoundException("Order Method '"+id+"' Not Found");
		}
		return opt;
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderMethod> getAllOrderMethods() {
		List<OrderMethod> list=null;
		
		list=repo.findAll();
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isOrderMethodExist(String id) {
		boolean exist=false;
		
		exist=repo.existsById(id);
		return exist;
	}
	@Override
	public List<OrderMethod> getAllOrderMethods(Specification<OrderMethod> specification) {
		
		return repo.findAll(specification);
	}
}
