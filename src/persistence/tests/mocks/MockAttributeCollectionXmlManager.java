package persistence.tests.mocks;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import models.AttributeCollection;
import persistence.IXmlManager;

public class MockAttributeCollectionXmlManager implements IXmlManager<AttributeCollection> {

	private Element createdElement;
	private AttributeCollection attributeCollection;
	
	public AttributeCollection getAttributeCollection() {
		return this.attributeCollection;
	}

	public void setAttributeCollection(AttributeCollection attributeCollection) {
		this.attributeCollection = attributeCollection;
	}

	@Override
	public Element getElementFromItem(AttributeCollection item,
			Document document) {
		Element element = document.createElement("attributes");
		this.setCreatedElement(element);
		return element;
	}

	@Override
	public AttributeCollection getItemFromXmlElement(Element element) throws Exception {
		this.attributeCollection = new AttributeCollection();
		return this.attributeCollection;
	}

	public void setCreatedElement(Element createdElement) {
		this.createdElement = createdElement;
	}

	public Element getCreatedElement() {
		return createdElement;
	}
}
