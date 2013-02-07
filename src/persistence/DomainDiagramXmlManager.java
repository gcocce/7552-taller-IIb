package persistence;

import java.util.UUID;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import models.der.Diagram;
import models.der.Diagram.DiagramState;
import models.domain.DomainDiagram;

public class DomainDiagramXmlManager implements IXmlManager<DomainDiagram> {

	@Override
	public Element getElementFromItem(DomainDiagram item, Document document) {
		// TODO: We don't need to generate the XML, so don't implement it
//		Element diagramElement = document.createElement("diagram");
//		
//		diagramElement.setAttribute("id", diagram.getId().toString());
//		diagramElement.setAttribute("state", diagram.getState().toString());
//		diagramElement.appendChild(new EntityCollectionXmlManager().getElementFromItem(diagram.getEntities(), document));
//		diagramElement.appendChild(new RelationshipCollectionXmlManager().getElementFromItem(diagram.getRelationships(), document));
//		diagramElement.appendChild(new HierarchyCollectionXmlManager().getElementFromItem(diagram.getHierarchies(), document));
//		
//		Element element = document.createElement("subDiagrams");
//		for (Diagram subDiagram : diagram) {
//				element.appendChild(this.getSubDiagramElement(subDiagram, document));
//		}
//		diagramElement.appendChild(element);
//	
//		return diagramElement;
		return null;
	}

	@Override
	public DomainDiagram getItemFromXmlElement(Element diagramElement)
			throws Exception {
		DomainDiagram diagram = new DomainDiagram(UUID.fromString(diagramElement.getAttribute("id")));
		
		Element classesElement = (Element) diagramElement.getElementsByTagName("classes").item(0);
		Element relationshipsElement = (Element) diagramElement.getElementsByTagName("relationships").item(0);
//		Element subDiagramsElement = (Element) diagramElement.getElementsByTagName("subDiagrams").item(0);
		
		diagram.setClasses(new ClassesCollectionXmlManager().getItemFromXmlElement(classesElement));
		diagram.setRelationships(new DomainRelationshipCollectionXmlManager().getItemFromXmlElement(relationshipsElement));

		// Don't handle yet subdiagrams
//		if (subDiagramsElement != null)
//		{	
//			for (int i = 0; i < diagramsElement.getChildNodes().getLength(); i++) {
//				Node node = diagramsElement.getChildNodes().item(i);
//				if (node == null || !(node instanceof Element)){
//					continue;
//				}
//				diagram.getSubDiagramNames().add(this.getSubDiagramFromXmlElement((Element)node));
//			}
//		}
		
		return diagram;
	}

}
