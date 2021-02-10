package in.nit.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.nit.model.Document;


public interface DocumentRepository extends JpaRepository<Document,Integer> {

	
}
