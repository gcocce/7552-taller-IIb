package persistence.tests;


import junit.framework.Assert;
import models.AttributeCollection;
import models.Entity;
import models.EntityType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import persistence.EntityXmlManager;

public class EntityXmlManagerTestCase {

	@Test
	public void testShouldGenerateAnXmlElementFromEntity() throws Exception
	{
		AttributeCollection attributes = new AttributeCollection();
		attributes.addAttribute("att1");
		attributes.addAttribute("att2");
		attributes.addAttribute("att3");
		
		Entity entity = new Entity("TestName1", EntityType.Historic, attributes);
		
		Document document = TestUtilities.createDocument();
		
		EntityXmlManager xmlManager = new EntityXmlManager();
		
		Element element = xmlManager.getElementFromItem(entity, document);
		
		Assert.assertEquals("entity", element.getTagName());
		Assert.assertEquals(entity.getName(), element.getAttribute("name"));
		Assert.assertEquals(entity.getId().toString(), element.getAttribute("id"));
		Assert.assertEquals(entity.getType().toString(), element.getAttribute("type"));
		
		NodeList attributesElement = element.getChildNodes().item(0).getChildNodes();
		Assert.assertEquals(3, attributesElement.getLength());
		Assert.assertEquals("att1", ((Element) attributesElement.item(0)).getAttribute("name"));
		Assert.assertEquals("att2", ((Element) attributesElement.item(1)).getAttribute("name"));
		Assert.assertEquals("att3", ((Element) attributesElement.item(2)).getAttribute("name"));
	}
	
	@Test
	public void testShouldGenerateAnEntityFromXml() throws Exception
	{
		String xml = "<entities><entity name='TestName' type='Historic' " +
				"id='3F2504E0-4F89-11D3-9A0C-030CFFFFFFFF'><attributes>" +
				"<attribute name='attr1' id='3F2504E0-4F89-11D3-9A0C-130CFFFFFFFF'" +
				" isKeyField='true'/><attribute name='attr2' " +
				"id='3F2504E0-4F89-11D3-9A0C-230CFFFFFFFF' isKeyField='false'/>" +
				"<attribute name='attr3' id='3F2504E0-4F89-11D3-9A0C-330CFFFFFFFF'" +
				" isKeyField='true'/></attributes></entity></entities>";
		
		Document document = TestUtilities.loadXMLFromString(xml);
		
		EntityXmlManager xmlManager = new EntityXmlManager();
		
		Element entityElement = (Element) document.getElementsByTagName("entity").item(0);
		
		Entity entity = xmlManager.getItemFromXmlElement(entityElement);
		Assert.assertEquals("TestName", entity.getName());
		Assert.assertEquals("Historic", entity.getType().toString());
		Assert.assertEquals("3f2504e0-4f89-11d3-9a0c-030cffffffff", entity.getId().toString());
		
		Assert.assertEquals(3, entity.getAttributes().count());
		Assert.assertNotNull(entity.getAttributes().getAttribute("attr1"));
		Assert.assertNotNull(entity.getAttributes().getAttribute("attr2"));
		Assert.assertNotNull(entity.getAttributes().getAttribute("attr3"));
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

}
