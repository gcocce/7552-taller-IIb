package controllers;

import java.io.IOException;

import javax.swing.tree.TreeModel;

import javax.swing.tree.TreePath;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import views.IProjectView;

public interface IProjectController {
	void createProject(String string);
	boolean openProject(String string) throws Exception;
	IDiagramController getCurrentDiagramController();
	IProjectView getView();
	TreeModel getProjectTree();
	void changeElement(TreePath treePath);
	void deleteElement(TreePath path);
	void validateProject(int toleranceLevel);
	void transformToDomainDiagram() throws ParserConfigurationException, SAXException, IOException;
	void showDiagram() throws Exception;
}
