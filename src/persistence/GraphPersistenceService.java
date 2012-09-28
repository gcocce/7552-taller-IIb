package persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.w3c.dom.Document;

import com.mxgraph.io.mxCodec;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;

public class GraphPersistenceService implements IGraphPersistenceService{

	@Override
	public void load(String name, mxGraph graph) {	
		byte[] buffer = new byte[(int) new File(name).length()];
		FileInputStream f;
		try {
			f = new FileInputStream(name);
			f.read(buffer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String xml = new String(buffer);
		Document document = mxXmlUtils.parseXml(xml);
		mxCodec codec = new mxCodec(document);
		codec.decode(document.getDocumentElement(), graph.getModel());		
	}

	@Override
	public void save(String name, mxGraph graph) {
		mxCodec codec = new mxCodec();
		String xml = mxXmlUtils.getXml(codec.encode(graph.getModel()));
		FileWriter writer;
		try {
			writer = new FileWriter(new File(name));
			writer.write(xml);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
