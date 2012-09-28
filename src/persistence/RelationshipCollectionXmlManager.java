package persistence;

import models.Relationship;
import models.RelationshipCollection;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class RelationshipCollectionXmlManager implements IXmlManager<RelationshipCollection> {

	public Element getElementFromItem(RelationshipCollection collection,
			Document document) {
		Element element = document.createElement("relationships");
		
		for (Relationship relationship : collection) {
			element.appendChild(new RelationshipXmlManager().getElementFromItem(relationship, document));
		}
		
		return element;
	}

	public RelationshipCollection getItemFromXmlElement(Element relationshipsElement) throws Exception {
		RelationshipCollection relationships = new RelationshipCollection();
		
		if (relationshipsElement == null)
			return relationships;
		NodeList relationshipsList = relationshipsElement.getElementsByTagName("relationship");
		
		for (int i = 0; i < relationshipsList.getLength(); i++) {
			Element relationshipElement = (Element) relationshipsList.item(i);
			relationships.add(new RelationshipXmlManager().getItemFromXmlElement(relationshipElement));
		}
		
		return relationships;
	}
}
