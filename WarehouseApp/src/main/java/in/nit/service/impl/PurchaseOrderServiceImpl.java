package in.nit.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.nit.model.PurchaseDetail;
import in.nit.model.PurchaseOrder;
import in.nit.repo.PurchaseDetailsRepository;
import in.nit.repo.PurchaseOrderRepository;
import in.nit.service.IPurchaseOrderService;
@Service
public class PurchaseOrderServiceImpl  implements IPurchaseOrderService{
	@Autowired
	private PurchaseOrderRepository repo;
	
	@Autowired
	private PurchaseDetailsRepository dtlRepo;
	
	@Transactional
	public Integer savePurchaseOrder(PurchaseOrder purchaseOrder) {
		Integer id=null;
		
		id=repo.save(purchaseOrder).getId();
		return id;
	}
	
	@Transactional
	public void updatePurchaseOrder(PurchaseOrder purchaseOrder) {
		
		repo.save(purchaseOrder);
	}
	
	@Transactional
	public void deletePurchaseOrder(Integer id) {
		
		repo.deleteById(id);
	}
	
	@Transactional(readOnly = true)
	public Optional<PurchaseOrder> getOnePurchaseOrder(Integer id) {
		Optional<PurchaseOrder> opt=null;
		
		opt=repo.findById(id);
//		if (opt.isPresent()) {
//			return opt.get();
//		}
		return opt;
	}
	
	@Transactional(readOnly = true)
	public List<PurchaseOrder> getAllPurchaseOrders() {
		List<PurchaseOrder> list=null;
		
		list=repo.findAll();
		return list;
	}
	
	@Transactional(readOnly = true)
	public boolean isPurchaseOrderExist(Integer id) {
		boolean exist=repo.existsById(id);
		return exist;
	}

	@Transactional(readOnly = true)
	public boolean isPurchaseOrderCodeExist(String orderCode) {
		int count=repo.getOrderCodeCount(orderCode);
		boolean flag = (count>0 ? true: false);  
		return flag;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Object[]> getOrderCodeCount() {
		
		return repo.getOrderCodeCount();
	}

	@Override
	@Transactional
	public Integer addPartToPo(PurchaseDetail dtl) {
		
		return dtlRepo.save(dtl).getId();
	}

	
	@Transactional
	public List<PurchaseDetail> getPurchaseDetailWithPoId(Integer purchaseId) {
		
		return dtlRepo.getPurchaseDtlWithPoId(purchaseId);
	}

	@Override
	@Transactional
	public void deletePurchaseDetail(Integer id) {
		dtlRepo.deleteById(id);
	}
	
	@Override
	@Transactional
	public void updatePurchaseOrderStatus(String status, Integer id) {
		repo.updatePurchaseOrderStatus(status, id);
	}
	
	@Override
	@Transactional
	public Integer getPurchaseDetailCountWithPoId(Integer purchaseId) {
		
		return dtlRepo.getPurchaseDetailCountWithPoID(purchaseId);
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, String> getPoIdAndCodeByStatus(String status) {
		
		Map<String,String> map=new LinkedHashMap<>();
		List<Object[]> list=repo.getPoIdAndCodeByStatus(status);
		for(Object[] ob:list) {
			map.put(ob[0].toString(),ob[1].toString());
		}
		return map;
	}
}
