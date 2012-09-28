package persistence;

import infrastructure.StringExtensions;

import java.util.UUID;

import models.AttributeCollection;
import models.Entity;
import models.EntityType;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EntityXmlManager implements IXmlManager<Entity> {

	public Element getElementFromItem(Entity entity, Document document) {
		Element entityElement = document.createElement("entity");
		
		entityElement.setAttribute("name", entity.getName().toString());
		entityElement.setAttribute("id", entity.getId().toString());
		entityElement.setAttribute("type", entity.getType().toString());
		entityElement.appendChild(new AttributeCollectionXmlManager().
				getElementFromItem(entity.getAttributes(), document));
		return entityElement;
	}

	public Entity getItemFromXmlElement(Element entityElement) throws Exception
	{
		String name = entityElement.getAttribute("name");
		UUID id = UUID.fromString(entityElement.getAttribute("id"));
		EntityType type = EntityType.valueOf(StringExtensions.isNullOrEmpty
				(entityElement.getAttribute("type"))? 
						"None" : entityElement.getAttribute("type"));
		Element attributesElem = (Element) entityElement.getElementsByTagName(
				"attributes").item(0);
		AttributeCollection attributes = new AttributeCollectionXmlManager(
				).getItemFromXmlElement(attributesElem);
		return new Entity(name, id, type, attributes);
	}
}
