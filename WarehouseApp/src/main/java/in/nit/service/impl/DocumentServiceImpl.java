package in.nit.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.nit.model.Document;
import in.nit.repo.DocumentRepository;
import in.nit.service.IDocumentService;

@Service
public class DocumentServiceImpl implements IDocumentService {
	@Autowired
	private DocumentRepository repo;
	
	@Override
	public void saveDocument(Document doc) {
		repo.save(doc);
	}

	@Override
	public Optional<Document> findDocument(Integer id) {
		
		return repo.findById(id);
	}

}
