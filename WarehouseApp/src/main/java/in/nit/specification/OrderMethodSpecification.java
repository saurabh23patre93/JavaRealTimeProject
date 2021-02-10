package in.nit.specification;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import in.nit.model.OrderMethod;
//Generate dynamic where condition(predicate)
public class OrderMethodSpecification implements Specification<OrderMethod>{
	
	
	private OrderMethod filter;
	
	public OrderMethodSpecification(OrderMethod filter) {
		this.filter = filter;
	}

	@Override
	public Predicate toPredicate(Root<OrderMethod> root, 
							CriteriaQuery<?> query, 
							CriteriaBuilder criteriaBuilder) {
		Predicate predicate=null;
		List<Expression<Boolean>> expression=null;
		
		predicate=criteriaBuilder.conjunction();//criteriaBuilder.disjuction();
		expression=predicate.getExpressions();//empty expression
		if (filter.getOrderCode()!=null && !filter.getOrderCode().isEmpty()) {
			//where ordercode like '%_%'
			expression.add(criteriaBuilder.like(root.get("orderCode"),
												"%"+filter.getOrderCode()+"%"));
		}
		if (filter.getDescription()!=null && !filter.getDescription().isEmpty()) {
			expression.add(criteriaBuilder.like(root.get("description"),
												"%"+filter.getDescription()+"%"));
		}
		if (filter.getOrderType()!=null && !filter.getOrderType().isEmpty()) {
			expression.add(
					criteriaBuilder.like(
							root.get("orderType"), 
							"%"+filter.getOrderType()+"%"
							)
					);
		}
		return predicate;
	}

}
