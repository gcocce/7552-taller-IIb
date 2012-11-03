package models;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TransformER_Domain {
	DocumentBuilderFactory factoryBuilder;
	DocumentBuilder documentBuilder;
	
	private Document diagramDoc; // Antiguo modelo Entidad Relacion
	private Document graphDoc;	// Antiguo xml de la representacion gráfica
	private Document dominioDoc; // Modelo de dominio generado
	private Document newGraphDoc;// xml de la nueva representacion gráfica
	
	public TransformER_Domain(Document diagram, Document graph){
		this.factoryBuilder = DocumentBuilderFactory.newInstance();
		this.factoryBuilder.setNamespaceAware(true);
		try {
			this.documentBuilder = this.factoryBuilder.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.diagramDoc=diagram;
		this.graphDoc=graph;
	}
	
	
	public Document GetDomainModel(){
	    // Creamos el Document para el modelo de dominio
		dominioDoc = this.documentBuilder.newDocument();
        Element eDominio = dominioDoc .createElement("dominio");
        dominioDoc.appendChild(eDominio);
        
        Element eClases = dominioDoc.createElement("clases");
        eDominio.appendChild(eClases);
	
        // Diagram element
        Element diagram=this.diagramDoc.getDocumentElement();
        
        // Lista de entities
        Element entitiesElement = (Element) diagram.getElementsByTagName("entities").item(0);
        this.procesarEntidades(entitiesElement);
        
        // Lista de relaciones
        Element relationshipElement = (Element) diagram.getElementsByTagName("relationships").item(0);
        this.procesarRelaciones(relationshipElement );
        
        
        return dominioDoc;
	}
	
	private void procesarRelaciones(Element relationshipsElement){
        Element eClases= (Element)dominioDoc.getElementsByTagName("clases").item(0);
        
        NodeList nList = relationshipsElement.getElementsByTagName("relationship");
        
        for (int temp = 0; temp < nList.getLength(); temp++) {
 		   Node nNode = nList.item(temp);
 		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				  Element eEntity= (Element) nNode;
				  
				  String sId=eEntity.getAttribute("id");
				  String sName=eEntity.getAttribute("name");
				  
				  System.out.println("Relationship name: " + sName);
	      
				  Element clase = dominioDoc.createElement("clase");
				  clase.setAttribute("name", sName);
				  clase.setAttribute("id", sId);
				  eClases.appendChild(clase);
				  
				  // Procesar nodos hijos
				  Element eAtributos=(Element)eEntity.getElementsByTagName("attributes").item(0);
				  this.procesarAtributos(clase, eAtributos);
				  
				  System.out.println("Fin Relationship: " + sName);
 		   }
 		}        
	}
	
	private void procesarEntidades(Element entitiesElement){
        Element eClases= (Element)dominioDoc.getElementsByTagName("clases").item(0);
        
        NodeList nList = entitiesElement.getElementsByTagName("entity");
        
        for (int temp = 0; temp < nList.getLength(); temp++) {
 		   Node nNode = nList.item(temp);
 		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				  Element eEntity= (Element) nNode;
				  
				  String sId=eEntity.getAttribute("id");
				  String sName=eEntity.getAttribute("name");
				  
				  System.out.println("Entity name: " + sName);
	      
				  Element clase = dominioDoc.createElement("clase");
				  clase.setAttribute("name", sName);
				  clase.setAttribute("id", sId);
				  eClases.appendChild(clase);
				  
				  // Procesar nodos hijos
				  Element eAtributos=(Element)eEntity.getElementsByTagName("attributes").item(0);
				  this.procesarAtributos(clase, eAtributos);
				  
				  System.out.println("Fin Entity: " + sName);
 		   }
 		}        
	}
	
	private void procesarAtributos(Element clase,Element eAtributes){
		Element eAtributos = dominioDoc.createElement("attributes");
		clase.appendChild(eAtributos);
		
        //NodeList nList = eAtributes.getElementsByTagName("attribute");
		NodeList nList=eAtributes.getChildNodes(); 
        
        System.out.println("Numero de atributos: " + nList.getLength());
        
        for (int temp = 0; temp < nList.getLength(); temp++) {
 		   Node nNode = nList.item(temp);
 		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				  Element eAtribute= (Element) nNode;
				  
				  String sId=eAtribute.getAttribute("id");
				  String sName=eAtribute.getAttribute("name");
				  
				  System.out.println("Attribute name: " + sName);
	      
				  Element eAtributo = dominioDoc.createElement("attribute");
				  eAtributo.setAttribute("name", sName);
				  eAtributo.setAttribute("id", sId);
				  eAtributos.appendChild(eAtributo);
				  
				  // Procesar nodos hijos
				  Element eAtributosHijos=(Element) eAtribute.getElementsByTagName("attributes").item(0);
				  NodeList nListaHijos = eAtributosHijos.getElementsByTagName("attribute");				  
				  if (nListaHijos.getLength()>0){
					  this.procesarAtributosHijos(sName, sId, eAtributosHijos);  
				  }
 		   }
 		}        
 	}
	
	private void procesarAtributosHijos(String nombre, String id, Element atributes){
		System.out.println("Se crea una nueva clase: " + nombre);
		
		Element clases = (Element)dominioDoc.getElementsByTagName("clases").item(0);
        Element clase= dominioDoc.createElement("clase");
        clase.setAttribute("name", nombre);
        clase.setAttribute("id", id);
        clases.appendChild(clase);

		//NodeList nListaHijos = atributes.getElementsByTagName("attribute");
		NodeList nListaHijos =atributes.getChildNodes();
		
		for (int temp = 0; temp < nListaHijos.getLength(); temp++) {
			Node nNode = nListaHijos.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eAtribute= (Element) nNode;
	 				  
				  String sId=eAtribute.getAttribute("id");
				  String sName=eAtribute.getAttribute("name");
				  
				  System.out.println("Attribute name: " + sName);
				  
				  Element eAtributo = dominioDoc.createElement("attribute");
				  eAtributo.setAttribute("name", sName);
				  eAtributo.setAttribute("id", sId);
				  clase.appendChild(eAtributo);
				  
				  // Procesar nodos hijos
				  Element eAtributosHijos=(Element) eAtribute.getElementsByTagName("attributes").item(0);
				  NodeList nLista = eAtributosHijos.getElementsByTagName("attribute");				  
				  if (nLista.getLength()>0){
					  this.procesarAtributosHijos(sName, sId, eAtributosHijos);  
				  }
	  		   }
	  	  }
		
		System.out.println("Fin clase: " + nombre);
	}
	
	public Document getGraphDomain(){
		Document newgraphDoc = this.documentBuilder.newDocument();
		
		
		return newgraphDoc ;
	}
	
}
