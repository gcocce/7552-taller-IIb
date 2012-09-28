package persistence;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class XmlFileManager implements IXmlFileManager{

    public void write(Document document, String path) {
    	// write the XML document to disk
        try {
        	// create DOMSource for source XML document
            Source xmlSource = new DOMSource(document);

            // create StreamResult for transformation result
            Result result = new StreamResult(new FileOutputStream(
                    path));

            // create TransformerFactory
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();

            // create Transformer for transformation
            Transformer transformer = transformerFactory.newTransformer();
            //transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            //transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //Java XML Indent

            // transform and deliver content to client
            transformer.transform(xmlSource, result);
        }

        // handle exception creating TransformerFactory
        catch (TransformerFactoryConfigurationError factoryError) {
            System.err.println("Error creating" + "TransformerFactory");
            factoryError.printStackTrace();
        } catch (TransformerException transformerError) {
            System.err.println("Error transforming document");
            transformerError.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

//    public Document read(String path) {
//        DocumentBuilderFactory dBF = DocumentBuilderFactory.newInstance();
//        dBF.setIgnoringComments(true); // Ignore the comments present in the
//        dBF.setIgnoringElementContentWhitespace(true);
//        // XML File when reading the xml
//        DocumentBuilder builder = null;
//        try {
//            builder = dBF.newDocumentBuilder();
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        }
//
//        InputSource input = new InputSource(path);
//        Document doc = null;
//        try {
//            assert builder != null;
//            doc = builder.parse(input);
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return doc;
//    }
    public Document read(String path)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        InputSource is;
        Document document = null;
		try 
		{
			builder = factory.newDocumentBuilder();
			is = new InputSource(new StringReader(readFile(path)));
			document = builder.parse(is);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		}
		catch (SAXException e) 
		{
			e.printStackTrace();
		}
        
		return document;
    }
    
    private static String readFile(String path) throws IOException {
    	File file = new File(path);
    	FileInputStream stream = new FileInputStream(file);
    	FileChannel fc = null;
    	try {
    		fc = stream.getChannel();
    		MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
    		/* Instead of using default, pass in a decoder. */
    		return Charset.defaultCharset().decode(bb).toString();
    	}
    	finally {
    		fc.close();
    		stream.close();
    	}
	}

	@Override
	public Document createDocument() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.newDocument();
        
        return document;
	}

}
