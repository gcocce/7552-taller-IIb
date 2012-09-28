package persistence;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface IXmlManager<T> {
	Element getElementFromItem(T item, Document document);
	T getItemFromXmlElement(Element element) throws Exception;
}
