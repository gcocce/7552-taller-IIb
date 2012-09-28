package persistence;

import java.util.UUID;

import models.Diagram;
import models.Diagram.DiagramState;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DiagramXmlManager implements IXmlManager<Diagram> {

	public Element getElementFromItem(Diagram diagram, Document document) {
		Element diagramElement = document.createElement("diagram");
		
		diagramElement.setAttribute("id", diagram.getId().toString());
		diagramElement.setAttribute("state", diagram.getState().toString());
		diagramElement.appendChild(new EntityCollectionXmlManager().getElementFromItem(diagram.getEntities(), document));
		diagramElement.appendChild(new RelationshipCollectionXmlManager().getElementFromItem(diagram.getRelationships(), document));
		diagramElement.appendChild(new HierarchyCollectionXmlManager().getElementFromItem(diagram.getHierarchies(), document));
		
		Element element = document.createElement("subDiagrams");
		for (Diagram subDiagram : diagram) {
				element.appendChild(this.getSubDiagramElement(subDiagram, document));
		}
		diagramElement.appendChild(element);
	
		return diagramElement;
	}
	
	public Element getSubDiagramElement(Diagram diagram, Document document) {
		Element subDiagramElement = document.createElement("diagram");
		
		subDiagramElement.setAttribute("id", diagram.getId().toString());
		subDiagramElement.setAttribute("name", diagram.getName());
		
		return subDiagramElement;
	}
	
	public Diagram getItemFromXmlElement(Element diagramElement) throws Exception {
		Diagram diagram = new Diagram(UUID.fromString(diagramElement.getAttribute("id")), DiagramState.valueOf(diagramElement.getAttribute("state")));
				
		Element entitiesElement = (Element) diagramElement.getElementsByTagName("entities").item(0);
		Element relationshipsElement = (Element) diagramElement.getElementsByTagName("relationships").item(0);
		Element hierarchiesElement = (Element) diagramElement.getElementsByTagName("hierarchies").item(0);
		Element diagramsElement = (Element) diagramElement.getElementsByTagName("subDiagrams").item(0);
		
		diagram.setEntities(new EntityCollectionXmlManager().getItemFromXmlElement(entitiesElement));
		diagram.setRelationships(new RelationshipCollectionXmlManager().getItemFromXmlElement(relationshipsElement));
		diagram.setHierarchies(new HierarchyCollectionXmlManager().getItemFromXmlElement(hierarchiesElement));
		
		if (diagramsElement == null)
		{	
			return diagram;
		}
		
		for (int i = 0; i < diagramsElement.getChildNodes().getLength(); i++) {
			Node node = diagramsElement.getChildNodes().item(i);
        	if (node == null || !(node instanceof Element)){
        		continue;
        	}
			diagram.getSubDiagramNames().add(this.getSubDiagramFromXmlElement((Element)node));
		}
		return diagram;
	}
	
	public String getSubDiagramFromXmlElement(Element diagramElement){
		String diagramName = diagramElement.getAttribute("name");
		return diagramName;
	}
}
