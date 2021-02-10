package in.nit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;

import in.nit.model.OrderMethod;


public interface IOrderMethodService {
	
	public String saveOrderMethod(OrderMethod orm);
	public void updateOrderMethod(OrderMethod orm);
	public void deleteOrderMethod(String id);
	public Optional<OrderMethod> getOneOrderMethod(String id);
	public List<OrderMethod> getAllOrderMethods();
	public boolean isOrderMethodExist(String id);
	
	public List<OrderMethod> getAllOrderMethods(Specification<OrderMethod> specification);
}
