package controllers.tests.mocks;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import models.Diagram;
import persistence.IXmlManager;

public class MockDiagramXmlManager implements IXmlManager<Diagram> {

	private String rootName;
	private Diagram diagram;
	private List<Element> elements;
	private List<Diagram> diagrams;
	
	public MockDiagramXmlManager(){
		this.elements = new ArrayList<Element>();
	}

	@Override
	public Element getElementFromItem(Diagram item, Document document) {
		this.diagram = item;
		return document.createElement(this.rootName);
	}

	@Override
	public Diagram getItemFromXmlElement(Element element) throws Exception {
		Diagram diagram = diagrams != null && diagrams.size() != 0 ? diagrams.remove(0) : new Diagram();
		this.diagram = diagram;
		this.elements.add(element);
		return diagram;
	}

	public void setDiagramsToReturn(List<Diagram> diagrams){
		this.diagrams = diagrams;
	}
	
	public void setElementNameOfRoot(String value) {
		this.rootName = value;
	}

	public Diagram getDiagramRelatedToElement() {
		return this.diagram;
	}

	public List<Element> getElementsPassedAsParameter() {
		return this.elements;
	}
}
