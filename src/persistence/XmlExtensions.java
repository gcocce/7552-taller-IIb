package persistence;

import org.w3c.dom.Element;

public class XmlExtensions {

	public static String getStringOrNull(Element element, String attribute)
	{
		return element.hasAttribute(attribute) ? 
				element.getAttribute(attribute) : 
					null;
	}
	
	public static Boolean getBooleanOrDefault(Element element, String attribute, Boolean defaultValue)
	{
		return element.hasAttribute(attribute) ? 
				Boolean.parseBoolean(element.getAttribute(attribute)) : 
					defaultValue;
	}
}
