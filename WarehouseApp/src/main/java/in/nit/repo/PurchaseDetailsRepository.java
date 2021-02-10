package in.nit.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.nit.model.PurchaseDetail;

public interface PurchaseDetailsRepository extends JpaRepository<PurchaseDetail,Integer> {
	
	/*
	 * Stage#4 Display all Parts which are added to PO 
	 * as a HTML table below to FORM 
	 * with dummy Remove button
	 * Display PurchaseDtls below
	 * SQL:
	 * select * from PurchaseDtls where po_id_fk=<PO_PK>
	 * 
	 * HQL:
	 * select  <a1>.<variable>, <a2>.<variable>
	 * from  <ParentClass>  <a1>
	 * [join-type]
	 * <a1>.<hasAVariableName> as <a2>
	 * where <condition>
	 * 
	 * Ex:
	 * SELECT  PDTL
	 * FROM    PurchaseDtl PDTL
	 * INNER JOIN
	 * pdtl.po  as  PO
	 * WHERE  PO.id=? //FK
	 * 
	 * 111
	 * hql-JAVA - * - MULTIPLY  / DEVIDE + ADD - SUB
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	@Query("SELECT PDTL FROM PurchaseDetail PDTL INNER JOIN PDTL.po AS PO WHERE PO.id=:purchaseId")
	public List<PurchaseDetail> getPurchaseDtlWithPoId(Integer purchaseId);

	//Stage#6 Confirm Button Operation with status conditions
	@Query("SELECT count(PDTL.id) FROM PurchaseDetail PDTL INNER JOIN PDTL.po AS PO WHERE PO.id=:purchaseId ")
	public Integer getPurchaseDetailCountWithPoID(Integer purchaseId);
	
}
