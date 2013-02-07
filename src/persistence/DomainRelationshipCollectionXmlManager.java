package persistence;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import models.domain.DomainRelationship;

public class DomainRelationshipCollectionXmlManager implements
		IXmlManager<List<DomainRelationship>> {

	@Override
	public Element getElementFromItem(List<DomainRelationship> item,
			Document document) {
		// TODO: We don't need to generate the XML, so don't implement it
		return null;
	}

	@Override
	public List<DomainRelationship> getItemFromXmlElement(Element element)
			throws Exception {
	      List<DomainRelationship> relationshipCollection = new ArrayList<DomainRelationship>();

	      NodeList nList = element.getElementsByTagName("relationship");
	      
	      for (int temp = 0; temp < nList.getLength(); temp++) {
			   Node nNode = nList.item(temp);
			   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					  Element myRelationship= (Element) nNode;
					  
					  String sId=myRelationship.getAttribute("id");
					  String sName=myRelationship.getAttribute("name");
					  String sDir = myRelationship.getAttribute("directionality");
					  String sComposition = myRelationship.getAttribute("composition");
					  boolean composition = false;
					  if(sComposition.equals("true"))
						  composition = true;
	/*				  
					  Element rClasses=(Element)myRelationship.getElementsByTagName("classes").item(0);
				      NodeList rClassesList = eClases.getElementsByTagName("class");
				      for (int temp2 = 0; temp2 < rClassesList.getLength(); temp2++) {
						   Node rClass = rClassesList.item(temp2);
						   if (rClass.getNodeType() == Node.ELEMENT_NODE) {
								  Element myrClass= (Element) rClass;
	*/							  
					  //aca voy populando el modelo de relaciones.
					  try {
						relationshipCollection.add(new DomainRelationship(null,sName,null,null));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

			   }
			}                
			return relationshipCollection;
	}

}
