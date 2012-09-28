package persistence;

import infrastructure.StringExtensions;

import java.util.UUID;

import models.AttributeCollection;
import models.Cardinality;
import models.Relationship;
import models.RelationshipEntity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class RelationshipXmlManager implements IXmlManager<Relationship> {

	private IXmlManager<AttributeCollection> attributeCollectionXmlManager;

	public RelationshipXmlManager(){
		this(new AttributeCollectionXmlManager());
	}
	
	public RelationshipXmlManager(
			IXmlManager<AttributeCollection> attributeCollectionXmlManager) {
		this.attributeCollectionXmlManager = attributeCollectionXmlManager;
	}

	public Element getElementFromItem(Relationship relationship,
			Document document) {
		Element relationshipElement = document.createElement("relationship");
		
		relationshipElement.setAttribute("id", relationship.getId().toString());
		relationshipElement.setAttribute("name", 
				StringExtensions.isNullOrEmpty(relationship.getName()) ? "" : relationship.getName());
		relationshipElement.setAttribute("composition", relationship.isComposition().toString());
		
		Element entitiesElement = document.createElement("entities");
		
		for (RelationshipEntity relationshipEntity : relationship.getRelationshipEntities()) 
		{
			entitiesElement.appendChild(
					new RelationshipEntityXmlManager()
						.getElementFromItem(relationshipEntity, document));
		}
		
		Element attributes = this.attributeCollectionXmlManager.getElementFromItem(relationship.getAttributes(), document);
		relationshipElement.appendChild(attributes);
		
		relationshipElement.appendChild(entitiesElement);
		
		return relationshipElement;
	}
	
	public Relationship getItemFromXmlElement(Element relationshipElement) throws Exception {
		UUID id = UUID.fromString(relationshipElement.getAttribute("id"));
		String name = XmlExtensions.getStringOrNull(relationshipElement, "name");
		Boolean composition = XmlExtensions.getBooleanOrDefault(relationshipElement, "composition", false);
		
		Relationship relationship = new Relationship(id, name, composition);
		
		NodeList entitiesNodeList = relationshipElement.getElementsByTagName("entities");
		
		Element entitiesElement = (Element) entitiesNodeList.item(0);
		
		NodeList entityNodeList = entitiesElement.getElementsByTagName("entity");
		
		for (int i = 0; i < entityNodeList.getLength(); i++) {
			Element entityElement = (Element) entityNodeList.item(i);
			relationship.addRelationshipEntity(
					new RelationshipEntityXmlManager().getItemFromXmlElement(entityElement));
		}
		
		Element attributesElement = (Element) relationshipElement.getElementsByTagName("attributes").item(0);
		
		AttributeCollection attributes = this.attributeCollectionXmlManager.getItemFromXmlElement(attributesElement);
		relationship.setAttributes(attributes);
		
		return relationship;
	}
	
	private class RelationshipEntityXmlManager
	{
		public Element getElementFromItem(RelationshipEntity relationshipEntity,
				Document document) {
			Element entityElement = document.createElement("entity");
			
			entityElement.setAttribute("entityId", relationshipEntity.getEntityId().toString());
			if (relationshipEntity.getCardinality() != null)
			{
				String minimum = Cardinality.getStringForCardinality(relationshipEntity.getCardinality().getMinimum());
				String maximum = Cardinality.getStringForCardinality(relationshipEntity.getCardinality().getMaximum());
				
				entityElement.setAttribute("minimumCardinality", minimum);
				entityElement.setAttribute("maximumCardinality", maximum);
			}
			
			if (relationshipEntity.getRole() != null && !relationshipEntity.getRole().isEmpty()){
				entityElement.setAttribute("role", relationshipEntity.getRole());
			}
			
			entityElement.setAttribute("isStrongEntity", Boolean.toString(relationshipEntity.isStrongEntity()));
			
			return entityElement;
		}

		public RelationshipEntity getItemFromXmlElement(Element entityElement) throws Exception {
			UUID id = UUID.fromString(entityElement.getAttribute("entityId"));
			String minimumCard = XmlExtensions.getStringOrNull(entityElement, "minimumCardinality");
			String maximumCard = XmlExtensions.getStringOrNull(entityElement, "maximumCardinality");
			String role = XmlExtensions.getStringOrNull(entityElement, "role");
			boolean isStrongEntity = Boolean.parseBoolean(
					XmlExtensions.getStringOrNull(entityElement,"isStrongEntity"));
			double minimum;
			double maximum;
			if (minimumCard == null && maximumCard == null)
				return new RelationshipEntity(id, null, role,isStrongEntity);
			if (minimumCard == null) {
				maximum = Cardinality.getCardinalityFromString(maximumCard);
				return new RelationshipEntity(id, new Cardinality(0, maximum), role,isStrongEntity);
			}
			if (maximumCard == null) {
				minimum = Cardinality.getCardinalityFromString(minimumCard);
				return new RelationshipEntity(id, new Cardinality(minimum, Double.POSITIVE_INFINITY), 
						role,isStrongEntity);
			}
			minimum = Cardinality.getCardinalityFromString(minimumCard);		
			maximum = Cardinality.getCardinalityFromString(maximumCard);
			return new RelationshipEntity(id, new Cardinality(minimum, maximum), 
					role,isStrongEntity);
		}
	}
}
