package in.nit.service;

import java.util.Optional;

import in.nit.model.Document;

public interface IDocumentService {
	
	public void saveDocument(Document doc);
	public Optional<Document> findDocument(Integer id);
}
