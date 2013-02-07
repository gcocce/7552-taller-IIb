package persistence;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import models.domain.DomainClass;

public class ClassesCollectionXmlManager implements IXmlManager<List<DomainClass>> {

	@Override
	public Element getElementFromItem(List<DomainClass> item, Document document) {
		// TODO: We don't need to generate the XML, so don't implement it
		return null;
	}

	@Override
	public List<DomainClass> getItemFromXmlElement(Element element)
			throws Exception {
		List<DomainClass> classCollection = new ArrayList<DomainClass>();
		NodeList nList = element.getElementsByTagName("class");
        
        for (int temp = 0; temp < nList.getLength(); temp++) {
 		   Node nNode = nList.item(temp);
 		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				  Element myClass= (Element) nNode;
				  
				  String sId=myClass.getAttribute("id");
				  String sName=myClass.getAttribute("name");
	      
				  //aca voy populando el modelo de classes.
				  classCollection.add(new DomainClass(sName, sId, null, null, null));

				  
				  // Procesar nodos hijos (agregar atributos)
				  /*Element eAtributos=(Element)myClass.getElementsByTagName("attributes").item(0);
				  this.procesarAtributos(clase, eAtributos);
*/
 		   }
 		}                
		return classCollection;
	}

}
