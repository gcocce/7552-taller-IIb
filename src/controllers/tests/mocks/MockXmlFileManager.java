package controllers.tests.mocks;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import persistence.IXmlFileManager;
import persistence.tests.TestUtilities;

public class MockXmlFileManager implements IXmlFileManager {

	private Document documentToSave;
	private String pathToSave;
	private List<Document> documents;
	private boolean createDocumentCalled;
	private List<String> pathsRead;
	
	public MockXmlFileManager(){
		this.pathsRead = new ArrayList<String>();
		this.documents = new ArrayList<Document>();
	}

	@Override
	public Document read(String filePath) throws Exception {
		if (filePath.equals("lectura")) {
			
		}
		Document document = TestUtilities.createDocument();
		document.appendChild(document.createElement("root"));
		this.documents.add(document);
		this.pathsRead.add(filePath);
		return document;
	}

	@Override
	public void write(Document document, String filePath) {
		this.setDocumentToSave(document);
		this.setPathToSave(filePath);
	}

	public void setDocumentToSave(Document documentToSave) {
		this.documentToSave = documentToSave;
	}

	public Document getDocumentToSave() {
		return documentToSave;
	}

	public void setPathToSave(String pathToSave) {
		this.pathToSave = pathToSave;
	}

	public String getPathToSave() {
		return pathToSave;
	}

	public void setDocumentToCreate(Document document) {
		this.documents.add(document);
	}

	@Override
	public Document createDocument() {
		this.createDocumentCalled = true;
		return this.documents.get(0);
	}

	public boolean wasCreateDocumentCalled() {
		return this.createDocumentCalled;
	}

	public List<String> getPathsRead() {
		return this.pathsRead;
	}

	public List<Document> getCreatedDocuments() {
		return this.documents;
	}

}
