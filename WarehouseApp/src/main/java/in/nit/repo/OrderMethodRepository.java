package in.nit.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import in.nit.model.OrderMethod;


public interface OrderMethodRepository extends JpaRepository<OrderMethod, String>,JpaSpecificationExecutor<OrderMethod> {

	
}
