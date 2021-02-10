package in.nit.service.impl;

import java.util.List;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.nit.model.SaleDetail;
import in.nit.model.SaleOrder;
import in.nit.repo.SaleDetailRepository;
import in.nit.repo.SaleOrderRepository;
import in.nit.service.ISaleOrderService;

@Service
public class SaleOrderServiceImpl implements ISaleOrderService {

	@Autowired
	private SaleOrderRepository repo;
	
	@Autowired
	private SaleDetailRepository dtlRepo;
	
	@Override
	@Transactional
	public Integer saveSaleOrder(SaleOrder saleOrder) {
		Integer id=null;
		
		id=repo.save(saleOrder).getId();
		return id;
	}

	
	@Override
	@Transactional
	public void updateSaleOrder(SaleOrder saleOrder) {
		
		repo.save(saleOrder);
	}

	@Override
	@Transactional
	public void deleteSaleOrder(Integer id) {
		
		repo.deleteById(id);
	}

	
	@Transactional(readOnly = true)
	public Optional<SaleOrder> getOneSaleOrder(Integer id) {
		Optional<SaleOrder> opt=null;
		
		opt=repo.findById(id);
		return opt;
	}

	@Override
	@Transactional(readOnly = true)
	public List<SaleOrder> getAllSaleOrders() {
		List<SaleOrder> list=null;
		
		list=repo.findAll();
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isSaleOrderExist(Integer id) {
		boolean exist=repo.existsById(id);
		return exist;
	}
	
	@Override
	@Transactional
	public Integer addItemToSo(SaleDetail dtl) {
		
		return dtlRepo.save(dtl).getId();
	}
	
	@Override
	@Transactional
	public List<SaleDetail> getSaleDtlWithSoId(Integer saleId) {
		
		return dtlRepo.getSaleDtlWithSoId(saleId);
	}


	@Override
	@Transactional
	public void deleteSaleDtl(Integer id) {
		
		dtlRepo.deleteById(id);
	}


	@Override
	@Transactional
	public void updateSaleOrderStatus(String status, Integer id) {
		
		repo.updateSaleOrderStatus(status, id);
	}


	@Override
	@Transactional
	public Integer getSaleDtlCountWithSoId(Integer saleId) {
		
		return dtlRepo.getSaleDetailCountWithSoId(saleId);
	}
}
