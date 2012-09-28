package persistence.tests;

import junit.framework.Assert;

import models.Entity;
import models.EntityCollection;
import models.EntityType;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import persistence.EntityCollectionXmlManager;


public class EntityCollectionXmlManagerTestCase {

	@Test
	public void testShouldGenerateAnXmlFromEntityCollection() throws Exception
	{
		EntityCollection collection = new EntityCollection();
		
		Entity entity1 = new Entity("Entity1", EntityType.Domain);
		Entity entity2 = new Entity("Entity2", EntityType.Thing);
		
		collection.add(entity1);
		collection.add(entity2);
		
		Document document = TestUtilities.createDocument();
		EntityCollectionXmlManager xmlManager = new EntityCollectionXmlManager();
		
		Element element = xmlManager.getElementFromItem(collection, document);
		
		Assert.assertEquals("entities", element.getTagName());
		
		NodeList items = element.getChildNodes();
		Assert.assertEquals(2, items.getLength()); 
		
		Assert.assertEquals("entity", ((Element)items.item(0)).getTagName());
		Assert.assertEquals(entity1.getName(), ((Element)items.item(0)).getAttribute("name"));
		Assert.assertEquals(entity2.getName(), ((Element)items.item(1)).getAttribute("name"));
		Assert.assertEquals(entity1.getType().toString(), ((Element)items.item(0)).getAttribute("type"));
		Assert.assertEquals(entity2.getType().toString(), ((Element)items.item(1)).getAttribute("type"));
		Assert.assertEquals(entity1.getId().toString(), ((Element)items.item(0)).getAttribute("id"));
		Assert.assertEquals(entity2.getId().toString(), ((Element)items.item(1)).getAttribute("id"));
	}
	
	@Test
	public void testShouldGenetarteAnEntityCollectionFromXml() throws Exception
	{
		String xml = "<entities><entity name='entity1' " +
				"id='3F2504E0-4F89-11D3-9A0C-030CFFFFFFFF' type='Domain'/>" +
				"<entity name='entity2' " +
				"id='3F2504E0-4F89-11D3-9A0C-030DFFFFFFFF' type='Thing'/></entities>";
		
		Document document = TestUtilities.loadXMLFromString(xml);
		
		EntityCollectionXmlManager xmlManager = new EntityCollectionXmlManager();
		Element entityCollectionElement = document.getDocumentElement();
		
		EntityCollection entityCollection = xmlManager.getItemFromXmlElement(entityCollectionElement);
		
		Assert.assertEquals(2, entityCollection.count());
		Assert.assertNotNull(entityCollection.get("entity1"));
		Assert.assertNotNull(entityCollection.get("entity2"));
		Assert.assertEquals("Domain", entityCollection.get("entity1").getType().toString());
		Assert.assertEquals("Thing", entityCollection.get("entity2").getType().toString());
	}
}
