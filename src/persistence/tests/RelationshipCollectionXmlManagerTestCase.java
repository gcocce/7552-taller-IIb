package persistence.tests;

import infrastructure.IterableExtensions;

import java.util.UUID;

import models.Cardinality;
import models.Entity;
import models.Relationship;
import models.RelationshipCollection;
import models.RelationshipEntity;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import persistence.RelationshipCollectionXmlManager;


public class RelationshipCollectionXmlManagerTestCase {

	@Test
	public void testShouldCreateRelationshipElementFromRelationshipCollection() throws Exception{
		RelationshipCollectionXmlManager xmlManager = new RelationshipCollectionXmlManager();
		Document document = TestUtilities.createDocument();
		
		RelationshipCollection collection = new RelationshipCollection();
		
		Entity entity1 = new Entity("Entity1");
		Entity entity2 = new Entity("Entity2");
		Entity entity3 = new Entity("Entity3");
		Entity entity4 = new Entity("Entity4");
		
		Relationship rel1 = new Relationship(
				new RelationshipEntity(entity1, new Cardinality(0, Double.POSITIVE_INFINITY), ""), 
				new RelationshipEntity(entity2, new Cardinality(1, 3), ""));
		
		Relationship rel2 = new Relationship(
				new RelationshipEntity(entity3, new Cardinality(5, Double.POSITIVE_INFINITY), ""), 
				new RelationshipEntity(entity4, new Cardinality(2, 3), ""));
		
		rel1.setName("relationship1");
		rel2.setName("relationship2");
		collection.add(rel1);
		
		collection.add(rel2);
		
		Element element = xmlManager.getElementFromItem(collection, document);
		
		Assert.assertEquals("relationships", element.getTagName());
		
		NodeList relationshipList = element.getElementsByTagName("relationship");
		
		Assert.assertEquals(2, relationshipList.getLength());
		
		Element relationship1 = (Element) relationshipList.item(0);
		Element relationship2 = (Element) relationshipList.item(1);
		
		NodeList entitiesList1 = relationship1.getElementsByTagName("entities");
		
		Assert.assertEquals(1, entitiesList1.getLength());
		
		Node entitiesNode1 = entitiesList1.item(0);
		
		NodeList entities1 = entitiesNode1.getChildNodes();
		
		Assert.assertEquals(2, entities1.getLength());
		
		Element entityElement1 = (Element)entities1.item(0);
		Element entityElement2 = (Element)entities1.item(1);
		
		Assert.assertEquals(entity1.getId().toString(), entityElement1.getAttribute("entityId"));
		Assert.assertEquals(entity2.getId().toString(), entityElement2.getAttribute("entityId"));
		
		NodeList entitiesList2 = relationship2.getElementsByTagName("entities");
		
		Assert.assertEquals(1, entitiesList2.getLength());
		
		Node entitiesNode2 = entitiesList2.item(0);
		
		NodeList entities2 = entitiesNode2.getChildNodes();
		
		Assert.assertEquals(2, entities1.getLength());
		
		Element entityElement3 = (Element)entities2.item(0);
		Element entityElement4 = (Element)entities2.item(1);
		
		Assert.assertEquals(entity3.getId().toString(), entityElement3.getAttribute("entityId"));
		Assert.assertEquals(entityElement3.getAttribute("isStrongEntity"),"false");
		Assert.assertEquals(entity4.getId().toString(), entityElement4.getAttribute("entityId"));
	}
	
	@Test
	public void testShouldCreateRelationshipCollectionFromXml() throws Exception{
		String xml = "<diagram>" +
		"<relationships>" +
		"<relationship id='01854049-A762-4392-9357-A213C4110220' " +
		"name='Relationship1' composition='true'>" +
		"<attributes>" +
		"<attribute name='nombre' id='0E688A75-A645-4665-85C8-21179BF362B8'/>" +
		"</attributes>" +
		"<entities>" +
		"<entity entityId='0E6A2A75-A645-4665-85C8-21179BF362B8' minimumCardinality='0'" +
		" maximumCardinality='1' role='Role1' isStrongEntity='false'/>" +
		"<entity entityId='0E6A2A75-A645-4665-85C8-21179BF362B7' minimumCardinality='0'" +
		" maximumCardinality='*' role='Role2' isStrongEntity='false'/>" +
		"<entity entityId='0E6A2A75-A645-4665-85C8-21179BF362B6' minimumCardinality='*'" +
		" maximumCardinality='*' role='Role3' isStrongEntity='false'/>" +
		"<entity entityId='0E6A2A75-A645-4665-85C8-21179BF362B5' minimumCardinality='2'" +
		" maximumCardinality='7' role='Role4' isStrongEntity='true'/>" +
		"</entities>" +
		"</relationship>" +
		"<relationship id='01854049-A762-4392-9357-A213C4110221' " +
		"name='Relationship2' composition='true'>" +
		"<attributes>" +
		"<attribute name='nombre' id='0E688A75-A645-4665-85C8-21179BF362B8'/>" +
		"</attributes>" +
		"<entities>" +
		"<entity entityId='0E6A2A75-A645-4665-85C8-21179BF362B4' minimumCardinality='0'" +
		" maximumCardinality='1' role='Role1' isStrongEntity='false'/>" +
		"<entity entityId='0E6A2A75-A645-4665-85C8-21179BF362B3' minimumCardinality='0'" +
		" maximumCardinality='*' role='Role2' isStrongEntity='false'/>" +
		"<entity entityId='0E6A2A75-A645-4665-85C8-21179BF362B2' minimumCardinality='*'" +
		" maximumCardinality='*' role='Role3' isStrongEntity='true'/>" +
		"<entity entityId='0E6A2A75-A645-4665-85C8-21179BF362B1' minimumCardinality='2'" +
		" maximumCardinality='7' role='Role4' isStrongEntity='false'/>" +
		"</entities>" +
		"</relationship>" +
		"</relationships>" +
		"</diagram>";
		
		Document document = TestUtilities.loadXMLFromString(xml);
		
		Element relationshipsElement = (Element) document.getElementsByTagName("relationships").item(0);
		
		RelationshipCollectionXmlManager xmlManager = new RelationshipCollectionXmlManager();
		
		RelationshipCollection relationships = xmlManager.getItemFromXmlElement(relationshipsElement);
		
		Assert.assertEquals(2, relationships.count());
		
		Relationship relationship1 = relationships.get(UUID.fromString("01854049-A762-4392-9357-A213C4110220"));
		Relationship relationship2 = relationships.get(UUID.fromString("01854049-A762-4392-9357-A213C4110221"));
		
		Assert.assertEquals(4, IterableExtensions.count(relationship1.getRelationshipEntities()));
		Assert.assertEquals(4, IterableExtensions.count(relationship2.getRelationshipEntities()));

		Integer i = 8;
		
		for (RelationshipEntity relationshipEntity : relationship1.getRelationshipEntities()) {
			Assert.assertEquals("0e6a2a75-a645-4665-85c8-21179bf362b" + i.toString(),
					relationshipEntity.getEntityId().toString());
			i--;
		}
		
		for (RelationshipEntity relationshipEntity : relationship2.getRelationshipEntities()) {
			Assert.assertEquals("0e6a2a75-a645-4665-85c8-21179bf362b" + i.toString(),
					relationshipEntity.getEntityId().toString());
			i--;
		}
	}
	
}
