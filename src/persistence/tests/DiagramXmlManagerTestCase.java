package persistence.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import junit.framework.Assert;

import models.Diagram;
import models.EntityCollection;
import models.Hierarchy;
import models.HierarchyCollection;
import models.Relationship;
import models.RelationshipCollection;
import models.Diagram.DiagramState;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import persistence.DiagramXmlManager;


public class DiagramXmlManagerTestCase {

	@Test
	public void testShouldGenerateAXmlFromDiagram() throws Exception
	{
		EntityCollection entities = new EntityCollection();
		RelationshipCollection relationships = new RelationshipCollection();
		HierarchyCollection hierarchies = new HierarchyCollection();
		List<Diagram> subDiagrams = new ArrayList<Diagram>();
		Diagram subDiagram1 = new Diagram();
		subDiagram1.setName("Sub1");
		Diagram subDiagram2 = new Diagram();
		subDiagram2.setName("Sub2");
		
		entities.add("entity1");
		entities.add("entity2");
		relationships.add(new Relationship(UUID.randomUUID(), "relation1", true));
		relationships.add(new Relationship(UUID.randomUUID(), "relation2", false));
		Hierarchy hierarchy = hierarchies.createHierarchy(entities.get("entity1").getId(), true, true);
		hierarchies.addChild(hierarchy.getId(), entities.get("entity2").getId());
		
		subDiagram1.setEntities(entities);
		subDiagram1.addSubDiagram(subDiagram2);
		
		subDiagrams.add(subDiagram1);
		subDiagrams.add(subDiagram2);
		
		Diagram diagram = new Diagram(entities, relationships, hierarchies, subDiagrams);
		
		Document document = TestUtilities.createDocument();
		
		DiagramXmlManager xmlManager = new DiagramXmlManager();
		
		Element diagramElement = xmlManager.getElementFromItem(diagram, document);
		
		Assert.assertEquals("diagram", diagramElement.getTagName());
		Assert.assertEquals(diagram.getId().toString(), diagramElement.getAttribute("id"));
		
		Element entitiesElement = (Element) diagramElement.getElementsByTagName("entities").item(0);
		Element relationshipsElement = (Element) diagramElement.getElementsByTagName("relationships").item(0);
		Element hierarchiesElement = (Element) diagramElement.getElementsByTagName("hierarchies").item(0);
		Element subDiagramsElement = (Element) diagramElement.getElementsByTagName("subDiagrams").item(0);
		
		NodeList items = entitiesElement.getChildNodes();
		Assert.assertEquals(2, items.getLength());
		Assert.assertEquals("entity1", ((Element) items.item(0)).getAttribute("name"));
		Assert.assertEquals("entity2", ((Element) items.item(1)).getAttribute("name"));
		
		items = hierarchiesElement.getChildNodes();
		Assert.assertEquals(1, items.getLength());
		Assert.assertEquals(hierarchy.getId().toString(), ((Element) items.item(0)).getAttribute("id"));
		
		NodeList relationsList = relationshipsElement.getElementsByTagName("relationship");
		NodeList subDiagList = subDiagramsElement.getChildNodes();
		
		Assert.assertEquals(2, relationsList.getLength());					
		Assert.assertEquals("relation1", ((Element) relationsList.item(0)).getAttribute("name"));
		Assert.assertEquals("relation2", ((Element) relationsList.item(1)).getAttribute("name"));
		
		Assert.assertEquals(2, subDiagList.getLength());
		Assert.assertEquals(subDiagram1.getId().toString(), ((Element) subDiagList.item(0)).getAttribute("id"));
		Assert.assertEquals(subDiagram1.getName(), ((Element) subDiagList.item(0)).getAttribute("name"));
		Assert.assertEquals(subDiagram2.getId().toString(), ((Element) subDiagList.item(1)).getAttribute("id"));
		Assert.assertEquals(subDiagram2.getName(), ((Element) subDiagList.item(1)).getAttribute("name"));
	}
	
	@Test
	public void testShouldGenerateADiagramFromXml() throws Exception
	{
		String xml = "<diagram id='3F2504E0-4F89-11D3-9A0C-0300FFFFFFFF' state='Invalid'><entities>" +
				"<entity name='entity1' id='3F2504E0-4F89-11D3-9A0C-030CFFFFFFFF'/>" +
				"<entity name='entity2' id='3F2504E0-4F89-11D3-9A0C-030DFFFFFFFF'/></entities>" +
				"<relationships><relationship id='3F2504E0-4F89-11D3-9A0C-030CFFFFFFFA' " +
				"composition='true' name='relation1'><entities>" +
				"<entity entityId='3F2504E0-4F89-11D3-9A0C-030CFFFFFFFF'/></entities>" +
				"</relationship></relationships><hierarchies><hierarchy " +
				"total='true' exclusive='true' " +
				"id='3F2504E0-4F89-11D3-9A0C-030CFFFFFFFC' " +
				"generalEntityId='3F2504E0-4F89-11D3-9A0C-030CFFFFFFFF'>" +
				"<specificEntities><entityId>" +
				"3F2504E0-4F89-11D3-9A0C-030DFFFFFFFF</entityId>" +
				"</specificEntities></hierarchy></hierarchies><subDiagrams>" +
				"<diagram id='3F2504E0-4F89-11D3-9A0C-0301FFFFFFFF' name='sub1'/>" +
				"<diagram id='3F2504E0-4F89-11D3-9A0C-0302FFFFFFFF' name='sub2'/>" +
				"</subDiagrams></diagram>";
		
		Document document = TestUtilities.loadXMLFromString(xml);
		
		DiagramXmlManager xmlManager = new DiagramXmlManager();
		
		Element diagramElement = document.getDocumentElement();
		
		Diagram diagram = xmlManager.getItemFromXmlElement(diagramElement);
		
		Assert.assertEquals("3f2504e0-4f89-11d3-9a0c-0300ffffffff", diagram.getId().toString());
		Assert.assertEquals(DiagramState.Invalid, diagram.getState());
		
		Assert.assertEquals(2, diagram.getEntities().count());
		Assert.assertNotNull(diagram.getEntities().get("entity1"));
		Assert.assertNotNull(diagram.getEntities().get("entity2"));
		Assert.assertEquals("3f2504e0-4f89-11d3-9a0c-030cffffffff", diagram.getEntities().get("entity1").getId().toString());
		Assert.assertEquals("3f2504e0-4f89-11d3-9a0c-030dffffffff", diagram.getEntities().get("entity2").getId().toString());
		
		Assert.assertEquals(1, diagram.getRelationships().count());
		Assert.assertNotNull(diagram.getRelationship(UUID.fromString("3F2504E0-4F89-11D3-9A0C-030CFFFFFFFA")));
		Assert.assertEquals("relation1",diagram.getRelationship(UUID.fromString("3F2504E0-4F89-11D3-9A0C-030CFFFFFFFA")).getName());
		Assert.assertTrue(diagram.getRelationship(UUID.fromString("3F2504E0-4F89-11D3-9A0C-030CFFFFFFFA")).isComposition());
		
		Assert.assertEquals(1, diagram.getHierarchies().count());
		Assert.assertNotNull(diagram.getHierarchies().getHierarchy(UUID.fromString("3F2504E0-4F89-11D3-9A0C-030CFFFFFFFC")));
		Assert.assertTrue(diagram.getHierarchies().getHierarchy(UUID.fromString("3F2504E0-4F89-11D3-9A0C-030CFFFFFFFC")).isExclusive());
		Assert.assertTrue(diagram.getHierarchies().getHierarchy(UUID.fromString("3F2504E0-4F89-11D3-9A0C-030CFFFFFFFC")).isTotal());
		Assert.assertEquals("3f2504e0-4f89-11d3-9a0c-030cffffffff", diagram.getHierarchies().getHierarchy(UUID.fromString("3F2504E0-4F89-11D3-9A0C-030CFFFFFFFC")).getGeneralEntityId().toString());
		
		Assert.assertEquals(2, diagram.getSubDiagramNames().size());
		Assert.assertNotNull("Sub1", diagram.getSubDiagramNames().get(0));
		Assert.assertNotNull("Sub2", diagram.getSubDiagramNames().get(1));
	}
}
