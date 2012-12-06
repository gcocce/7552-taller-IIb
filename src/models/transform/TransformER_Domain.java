package models.transform;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import models.domain.DomainClass;
import models.domain.DomainRelationship;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TransformER_Domain {
	DocumentBuilderFactory factoryBuilder;
	DocumentBuilder documentBuilder;
	
	private Document diagramDoc; // Antiguo modelo Entidad Relacion
	private Document graphDoc;	// Antiguo xml de la representacion gr�fica 
	private Document dominioDoc; // Modelo de dominio generado
	private Document newGraphDoc;// xml de la nueva representacion gr�fica
	
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
		this.dominioDoc = null;
		this.newGraphDoc = null;
	}
	
	
	public Document GetDomainModel(){
	    // Creamos el Document para el modelo de dominio
		dominioDoc = this.documentBuilder.newDocument();
        Element eDominio = dominioDoc .createElement("dominio");
        dominioDoc.appendChild(eDominio);
        
        Element eClases = dominioDoc.createElement("classes");
        eDominio.appendChild(eClases);
        
        Element eRelations = dominioDoc.createElement("relationships");
        eDominio.appendChild(eRelations);
	
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
        Element eClases= (Element)dominioDoc.getElementsByTagName("classes").item(0);
        Element eRelationsDomain= (Element)dominioDoc.getElementsByTagName("relationships").item(0);
        
        NodeList DERRelationshipList = relationshipsElement.getElementsByTagName("relationship");
        
        for (int temp = 0; temp < DERRelationshipList.getLength(); temp++) {
 		   Node nNode = DERRelationshipList.item(temp);
 		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				  Element relacionDER= (Element) nNode;
				  
				  String sId=relacionDER.getAttribute("id");
				  String sName=relacionDER.getAttribute("name");
				 			
				  Element eAtributos=(Element)relacionDER.getElementsByTagName("attributes").item(0);
				  NodeList attrList=eAtributos.getChildNodes();
				  
				  //si tiene atributos la relacion mapea a clase
				  if(attrList.getLength()!=0){
					  System.out.println("Relationship name: " + sName);

					  Element relationshipClass = dominioDoc.createElement("class");
					  relationshipClass.setAttribute("name", sName);
					  relationshipClass.setAttribute("id", sId);
					  eClases.appendChild(relationshipClass);

					  // Procesar nodos hijos

					  this.procesarAtributos(relationshipClass, eAtributos);
					  procesarRelacionConAtributos(relacionDER,eRelationsDomain,relationshipClass);
					  
				  }
				  else
					  procesarRelacionSinAtributos(relacionDER,eRelationsDomain);
					   
				  System.out.println("Fin Relationship: " + sName);
 		   }
 		}        
	}
	
	private void procesarRelacionConAtributos(Element relacionDER, Element eRelationsDomain,
			Element relationshipClass) {

		String sComposition = relacionDER.getAttribute("composition");
		  
		  if(sComposition == "true"){
			//creo una relacion normal con 2 clases
			  procesarRelacionSinAtributos(relacionDER,eRelationsDomain); 
			  
		  }
		  else{
			  //creo 2 relaciones contra la relacion que se hizo clase
			  crearRelacionDoble( relacionDER, eRelationsDomain, relationshipClass);
		  }

	}


	private void crearRelacionDoble(Element relacionDER, Element eRelationsDomain,
			Element relacionHechaClase) {
		
		Element eEntities=(Element)relacionDER.getElementsByTagName("entities").item(0);
		Element nuevaRelacionHechaClaseIzq = dominioDoc.createElement("class");
		Element nuevaRelacionHechaClaseDer = dominioDoc.createElement("class");
		
		nuevaRelacionHechaClaseIzq.setAttribute("id", relacionHechaClase.getAttribute("id"));
		nuevaRelacionHechaClaseDer.setAttribute("id", relacionHechaClase.getAttribute("id"));
				

		Element eRelationIzq = dominioDoc.createElement("relationship");
		String sId1=relacionDER.getAttribute("id"); //TODO: NO SE PUEDE REPETIR LE ID DE LA RELACION DEL DER EN LAS 2 RELACIONES DEL MODELO DE DOMINIO
		String sName1=relacionDER.getAttribute("name");
		String sComposition = relacionDER.getAttribute("composition");
		
		eRelationIzq.setAttribute("id", sId1);
		eRelationIzq.setAttribute("name", sName1 + "Izq");
		eRelationIzq.setAttribute("composition", sComposition);
		eRelationIzq.setAttribute("directionality", "bidirectional");
		eRelationsDomain.appendChild(eRelationIzq);

		Element eRelationDer = dominioDoc.createElement("relationship");
		eRelationDer.setAttribute("id", sId1);
		eRelationDer.setAttribute("name", sName1 + "Der");
		eRelationDer.setAttribute("composition", sComposition);
		eRelationDer.setAttribute("directionality", "bidirectional");
		eRelationsDomain.appendChild(eRelationDer);
		
		Element eClasesIzq = dominioDoc.createElement("classes");
		Element eClasesDer = dominioDoc.createElement("classes");
		
		NodeList entitiyList=eEntities.getChildNodes(); 
		String sCardinality[] = new String[entitiyList.getLength()];
        System.out.println("Numero de clases: " + entitiyList.getLength());
        List<Element> elementsList = new ArrayList<Element>() ;
        
        for (int temp = 0; temp < entitiyList.getLength(); temp++) {
 		   Node nNode = entitiyList.item(temp);
 		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				  Element eEntity= (Element) nNode;
				  
				  String sId=eEntity.getAttribute("entityId");
				  sCardinality[temp] = eEntity.getAttribute("maximumCardinality");
				  
				  System.out.println("class ID: " + sId);
	      
				  Element eClase = dominioDoc.createElement("class");
				  eClase.setAttribute("id", sId);

				  elementsList.add(eClase);
				  
 		   }
 		}     
        		
        elementsList.get(0).setAttribute("cardinality", "1");
		eClasesIzq.appendChild(elementsList.get(0));
		nuevaRelacionHechaClaseIzq.setAttribute("cardinality", sCardinality[1]);
		eClasesIzq.appendChild(nuevaRelacionHechaClaseIzq);
		eRelationIzq.appendChild(eClasesIzq);
		
		elementsList.get(1).setAttribute("cardinality", "1");
		eClasesDer.appendChild(elementsList.get(1));
		nuevaRelacionHechaClaseDer.setAttribute("cardinality", sCardinality[0]);
		eClasesDer.appendChild(nuevaRelacionHechaClaseDer);
		eRelationDer.appendChild(eClasesDer);
	}


	private void procesarRelacionSinAtributos(Element relacionDER, Element eRelations) {

		Element eEntities=(Element)relacionDER.getElementsByTagName("entities").item(0);
		

		Element eRelation = dominioDoc.createElement("relationship");
		String sId1 = relacionDER.getAttribute("id");
		String sName1 = relacionDER.getAttribute("name");
		String sComposition = relacionDER.getAttribute("composition");
		eRelation.setAttribute("id", sId1);
		eRelation.setAttribute("name", sName1);
		eRelation.setAttribute("composition", sComposition);
		eRelation.setAttribute("directionality", "bidirectional");
		eRelations.appendChild(eRelation);  
		
		Element eClases = dominioDoc.createElement("classes");
		NodeList entityList=eEntities.getChildNodes(); 
		String sCardinality[] = new String[entityList.getLength()];
        System.out.println("Numero de clases: " + entityList.getLength());
        List<Element> elementsList = new ArrayList<Element>() ;
        
        for (int temp = 0; temp < entityList.getLength(); temp++) {
 		   Node nNode = entityList.item(temp);
 		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				  Element eEntity= (Element) nNode;
				  
				  String sId=eEntity.getAttribute("entityId");
				  sCardinality[temp] = eEntity.getAttribute("maximumCardinality");
				  
				  System.out.println("class ID: " + sId);
	      
				  Element eClase = dominioDoc.createElement("class");
				  eClase.setAttribute("id", sId);

				  elementsList.add(eClase);
				  
 		   }
 		}
        
        int i=1;
		for(Element eClase:elementsList){
			eClase.setAttribute("cardinality", sCardinality[i]);
			i--;
			eClases.appendChild(eClase);
		}
        
        eRelation.appendChild(eClases);
		
		
	}


	private void procesarEntidades(Element entitiesElement){
        Element eClases= (Element)dominioDoc.getElementsByTagName("classes").item(0);
        
        NodeList nList = entitiesElement.getElementsByTagName("entity");
        
        for (int temp = 0; temp < nList.getLength(); temp++) {
 		   Node nNode = nList.item(temp);
 		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				  Element eEntity= (Element) nNode;
				  
				  String sId=eEntity.getAttribute("id");
				  String sName=eEntity.getAttribute("name");
				  
				  System.out.println("Entity name: " + sName);
	      
				  Element clase = dominioDoc.createElement("class");
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
				  String sCardinality = eAtribute.getAttribute("maximumCardinality");
				  
				  System.out.println("Attribute name: " + sName);
	      
				  Element eAtributo = dominioDoc.createElement("attribute");
				  eAtributo.setAttribute("name", sName);
				  eAtributo.setAttribute("id", sId);
				  eAtributo.setAttribute("cardinality", sCardinality);
				  
				  
				  // Procesar nodos hijos
				  Element eAtributosHijos=(Element) eAtribute.getElementsByTagName("attributes").item(0);
				  NodeList nListaHijos = eAtributosHijos.getElementsByTagName("attribute");				  
				  if (nListaHijos.getLength()>0){
					  this.procesarAtributosHijos(sName, sId, eAtributosHijos, clase);  
				  }else
					  eAtributos.appendChild(eAtributo);
 		   }
 		}        
 	}
	
	private void procesarAtributosHijos(String nombre, String id, Element atributes, Element claseContenedora){
		System.out.println("Se crea una nueva clase: " + nombre);
		
		Element clases = (Element)dominioDoc.getElementsByTagName("classes").item(0);
        Element clase= dominioDoc.createElement("class");
        clase.setAttribute("name", nombre);
        clase.setAttribute("id", id);
        clases.appendChild(clase);
        
        Element eAtributos = dominioDoc.createElement("attributes");
		clase.appendChild(eAtributos);

		//NodeList nListaHijos = atributes.getElementsByTagName("attribute");
		NodeList nListaHijos =atributes.getChildNodes();
		
		for (int temp = 0; temp < nListaHijos.getLength(); temp++) {
			Node nNode = nListaHijos.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eAtribute= (Element) nNode;
	 				  
				  String sId=eAtribute.getAttribute("id");
				  String sName=eAtribute.getAttribute("name");
				  String sCardinality = eAtribute.getAttribute("maximumCardinality");
				  
				  System.out.println("Attribute name: " + sName);
				  
				  Element eAtributo = dominioDoc.createElement("attribute");
				  eAtributo.setAttribute("name", sName);
				  eAtributo.setAttribute("id", sId);
				  eAtributo.setAttribute("cardinality", sCardinality);
				  
				  
				  // Procesar nodos hijos
				  Element eAtributosHijos=(Element) eAtribute.getElementsByTagName("attributes").item(0);
				  NodeList nLista = eAtributosHijos.getElementsByTagName("attribute");				  
				  if (nLista.getLength()>0){
					  crearRelacion(crearClaseDeRelacion(claseContenedora,"1.0"),crearClaseDeRelacion(clase,sCardinality));
					  this.procesarAtributosHijos(sName, sId, eAtributosHijos,clase);  
				  }else{
					  eAtributos.appendChild(eAtributo);
				  }
			}
				  
	  	  }
		
		System.out.println("Fin clase: " + nombre);
	}
	
	private Element crearClaseDeRelacion(Element clase, String cardinality) {
		
		  Element eClase = dominioDoc.createElement("class");
		  eClase.setAttribute("id", clase.getAttribute("id"));
		  eClase.setAttribute("cardinality",cardinality );

		return eClase;
	}


	private void crearRelacion(Element claseContenedora, Element clase) {

		Element eRelationsDomain= (Element)dominioDoc.getElementsByTagName("relationships").item(0);
		
		
		Element eRelation = dominioDoc.createElement("relationship");
		String sId1 = claseContenedora.getAttribute("id");
		String sName1 = claseContenedora.getAttribute("name");
		String sComposition1 = claseContenedora.getAttribute("composition");
		//nueva relacion, generar nuevo id
		eRelation.setAttribute("id", null);
		eRelation.setAttribute("name", "nuevaRelacion");
		eRelation.setAttribute("composition", "false");
		eRelation.setAttribute("directionality", "bidirectional");
		eRelationsDomain.appendChild(eRelation);  
		
		Element eClases = dominioDoc.createElement("classes");
		eClases.appendChild(claseContenedora);
		eClases.appendChild(clase);
        
        eRelation.appendChild(eClases);
		
		
		
	}


	public Document getGraphDomain(){
		Document newgraphDoc = this.documentBuilder.newDocument();
		
		
		return newgraphDoc ;
	}
	
	public List<DomainRelationship> populateDomainRelationships(Document dominioDoc) {

		List<DomainRelationship> relationshipCollection = new ArrayList<DomainRelationship>();
		  // Diagram element
      Element diagram=dominioDoc.getDocumentElement();
      
      // Lista de clases

      Element eClases= (Element)dominioDoc.getElementsByTagName("relationships").item(0);
      
      NodeList nList = eClases.getElementsByTagName("relationship");
      
      for (int temp = 0; temp < nList.getLength(); temp++) {
		   Node nNode = nList.item(temp);
		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				  Element myClass= (Element) nNode;
				  
				  String sId=myClass.getAttribute("id");
				  String sName=myClass.getAttribute("name");
				  String sDir = myClass.getAttribute("directionality");
				  String sComposition = myClass.getAttribute("composition");
				  boolean composition = false;
				  if(sComposition.equals("true"))
					  composition = true;

				  //aca voy populando el modelo de relaciones.
				  try {
					relationshipCollection.add(new DomainRelationship(null, sComposition, null, null));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		   }
		}                
		return relationshipCollection;
	}

	public List<DomainClass> populateDomainClasses(Document dominioDoc) {

		List<DomainClass> classCollection = new ArrayList<DomainClass>();
		  // Diagram element
        Element diagram=dominioDoc.getDocumentElement();
        
        // Lista de clases

        Element eClases= (Element)dominioDoc.getElementsByTagName("classes").item(0);
        
        NodeList nList = eClases.getElementsByTagName("class");
        
        for (int temp = 0; temp < nList.getLength(); temp++) {
 		   Node nNode = nList.item(temp);
 		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				  Element myClass= (Element) nNode;
				  
				  String sId=myClass.getAttribute("id");
				  String sName=myClass.getAttribute("name");
	      
				  //aca voy populando el modelo de classes.
				  classCollection.add(new DomainClass(sName, null, null, null));

				  
				  // Procesar nodos hijos (agregar atributos)
				  /*Element eAtributos=(Element)myClass.getElementsByTagName("attributes").item(0);
				  this.procesarAtributos(clase, eAtributos);
*/
 		   }
 		}                
		return classCollection;
	}
	
}
