package persistence.tests;

import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;
import models.Hierarchy;
import models.HierarchyCollection;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import persistence.HierarchyCollectionXmlManager;
import persistence.XmlFileManager;

public class HierarchyCollectionXmlManagerTest {

    private static String HIERARCHIES = "hierarchies";
    private static String HIERARCHY = "hierarchy";
    private static String SPECIFICENTITIES = "specificEntities";
    private static String ENTITYID = "entityId";
    private static String GENERALENTITYATTRIBUTE = "generalEntityId";
    private static String TOTALATTRIBUTE = "total";
    private static String EXCLUSIVEATTRIBUTE = "exclusive";
    private static String IDATTRIBUTE = "id";

    private static String PATH = "HierarchyCollectionXmlManagerTest.xml";

    @Test
    public void testCreateElementOfHierarchyCollection() {
        HierarchyCollection hierarchyCollection = new HierarchyCollection();
        UUID generalEntityUUID = UUID.randomUUID();
        Hierarchy hierarchy;
        hierarchy = hierarchyCollection.createHierarchy(generalEntityUUID, false, true);

        UUID uuid = hierarchy.getId();
        UUID child1 = UUID.randomUUID();
        UUID child2 = UUID.randomUUID();
        UUID child3 = UUID.randomUUID();
        UUID child4 = UUID.randomUUID();
        try {
            hierarchyCollection.addChild(uuid, child1);
            hierarchyCollection.addChild(uuid, child2);
            hierarchyCollection.addChild(uuid, child3);
            hierarchyCollection.addChild(uuid, child4);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DocumentBuilderFactory dBF = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            DocumentBuilder builder = dBF.newDocumentBuilder(); // java xml documentbuilder
            document = builder.newDocument();
        } catch (ParserConfigurationException parserException) {
            parserException.printStackTrace();
        }

        assert document != null;
        Element diagram = document.createElement("diagram");
        document.appendChild(diagram);

        Element hierarchies = new HierarchyCollectionXmlManager().getElementFromItem(hierarchyCollection, document);
        diagram.appendChild(hierarchies);

        new XmlFileManager().write(document, PATH);

        NodeList nodeList = diagram.getElementsByTagName(HIERARCHIES);
        Element hierarchiesElement = (Element) nodeList.item(0);
        NodeList hierarchyElements = hierarchiesElement.getElementsByTagName(HIERARCHY);
        Element hierarchyElement = (Element) hierarchyElements.item(0);

        Assert.assertFalse(Boolean.valueOf(hierarchyElement.getAttribute(EXCLUSIVEATTRIBUTE)));
        Assert.assertTrue(Boolean.valueOf(hierarchyElement.getAttribute(TOTALATTRIBUTE)));
        Assert.assertEquals(generalEntityUUID.toString(), hierarchyElement.getAttribute(GENERALENTITYATTRIBUTE));
        Assert.assertEquals(uuid, UUID.fromString(hierarchyElement.getAttribute(IDATTRIBUTE)));
        NodeList specificsHierarchies = hierarchyElement.getElementsByTagName(SPECIFICENTITIES);
        NodeList entityIdsElement = ((Element) specificsHierarchies.item(0)).getElementsByTagName(ENTITYID);
        Text id = (Text) (entityIdsElement.item(0)).getFirstChild();
        Assert.assertEquals(child1, UUID.fromString(id.getNodeValue()));


    }

    @Test
    public void testCreateHierarchyCollectionFromXml() {

        String xml = "<diagram>\n" +
                "  <hierarchies>\n" +
                "    <hierarchy exclusive=\"false\" generalEntityId=\"3552f6a7-b89f-49da-8b40-faa8311f44a5\" id=\"6999e406-b57b-4c4e-a681-073ab2b6cfa7\" total=\"true\">\n" +
                "      <specificEntities>\n" +
                "        <entityId>831d2bc8-983b-4321-ab90-d12fd942a1c1</entityId>\n" +
                "        <entityId>d2ace698-3fdd-4309-8c5b-9e380b0da8cb</entityId>\n" +
                "      </specificEntities>\n" +
                "    </hierarchy>\n" +
                "    <hierarchy exclusive=\"true\" generalEntityId=\"9c2213a8-5d05-44ec-ad58-d1d59018ff90\" id=\"16731eba-d505-4101-99a3-4fd7dbc05296\" total=\"true\">\n" +
                "      <specificEntities/>\n" +
                "    </hierarchy>\n" +
                "  </hierarchies>\n" +
                "</diagram>";

        Document document = null;
        try {
            document = TestUtilities.loadXMLFromString(xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert document != null;
        HierarchyCollection hierarchyCollectionFromXml = new HierarchyCollectionXmlManager().getItemFromXmlElement(document.getDocumentElement());

        Assert.assertEquals("3552f6a7-b89f-49da-8b40-faa8311f44a5", hierarchyCollectionFromXml.getHierarchiesWithGeneralEntityUUID(UUID.fromString("3552f6a7-b89f-49da-8b40-faa8311f44a5")).iterator().next().getGeneralEntityId().toString());
        Assert.assertEquals(false, hierarchyCollectionFromXml.getHierarchiesWithGeneralEntityUUID(UUID.fromString("3552f6a7-b89f-49da-8b40-faa8311f44a5")).iterator().next().isExclusive());
        Assert.assertEquals(true, hierarchyCollectionFromXml.getHierarchiesWithGeneralEntityUUID(UUID.fromString("3552f6a7-b89f-49da-8b40-faa8311f44a5")).iterator().next().isTotal());

        Assert.assertEquals("9c2213a8-5d05-44ec-ad58-d1d59018ff90", hierarchyCollectionFromXml.getHierarchiesWithGeneralEntityUUID(UUID.fromString("9c2213a8-5d05-44ec-ad58-d1d59018ff90")).iterator().next().getGeneralEntityId().toString());
        Assert.assertEquals(true, hierarchyCollectionFromXml.getHierarchiesWithGeneralEntityUUID(UUID.fromString("9c2213a8-5d05-44ec-ad58-d1d59018ff90")).iterator().next().isExclusive());
        Assert.assertEquals(true, hierarchyCollectionFromXml.getHierarchiesWithGeneralEntityUUID(UUID.fromString("9c2213a8-5d05-44ec-ad58-d1d59018ff90")).iterator().next().isTotal());

        Hierarchy hierarchy = hierarchyCollectionFromXml.getHierarchiesWithGeneralEntityUUID(UUID.fromString("3552f6a7-b89f-49da-8b40-faa8311f44a5")).iterator().next();
        Assert.assertEquals(true, hierarchy.hasChild(UUID.fromString("d2ace698-3fdd-4309-8c5b-9e380b0da8cb")));
        Assert.assertEquals(true, hierarchy.hasChild(UUID.fromString("831d2bc8-983b-4321-ab90-d12fd942a1c1")));
    }

}
