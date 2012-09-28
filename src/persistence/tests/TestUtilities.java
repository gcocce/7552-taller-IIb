package persistence.tests;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class TestUtilities {
	 public static Document loadXMLFromString(String xml) throws Exception
	    {
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        InputSource is = new InputSource(new StringReader(xml));
	        return builder.parse(is);
	    }
	 
	 public static Document createDocument() throws ParserConfigurationException
	 {
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         DocumentBuilder builder = dbf.newDocumentBuilder();
         Document document = builder.newDocument();
         
         return document;
	 }
}
