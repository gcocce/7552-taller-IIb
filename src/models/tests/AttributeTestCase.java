package models.tests;

import static org.junit.Assert.*;
import models.Attribute;
import models.AttributeType;

import org.junit.Test;



public class AttributeTestCase {

	@Test(expected=IllegalArgumentException.class)
	public void testSetExpressionOnCharacterizationAttribute () throws Exception {
		Attribute att = new Attribute ("Attribute1");
		att.setType(AttributeType.characterization);
		att.setExpression("expresion");
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetExpressionOnCharacterizationAttribute () throws Exception {
		Attribute att = new Attribute ("Attribute1");
		att.setType(AttributeType.characterization);
		att.getExpression();
	}
	
		
	@Test
	public void testSetExpressionOnCopyAttribute() {
		Attribute att = new Attribute ("Attribute1");
		att.setType(AttributeType.copy);
		try {
			att.setExpression("expresion");
		}catch (Exception e) {
			fail();
		}
		assertTrue (att.getExpression().equals("expresion"));
	}
	
	@Test
	public void testGetExpressionOnCopyAttribute() {
		Attribute att = new Attribute ("Attribute1");
		String str1 = null;
		att.setType(AttributeType.copy);
		try {
			att.setExpression("expresion");
			str1 = att.getExpression();
		}catch (Exception e) {
			fail();
		}
		assertTrue (att.getExpression().equals(str1));
	}
	
	@Test
	public void testSetExpressionOnCalculated() {
		Attribute att = new Attribute ("Attribute1");
		att.setType(AttributeType.calculated);
		try {
			att.setExpression("expresion");
		}catch (Exception e) {
			fail();
		}
		assertTrue (att.getExpression().equals("expresion"));
	}
	
	
	@Test
	public void testGetExpressionOnCalculated() {
		Attribute att = new Attribute ("Attribute1");
		String str1 = null;
		att.setType(AttributeType.calculated);
		try {
			att.setExpression("expresion");
			str1 = att.getExpression();
		}catch (Exception e) {
			fail();
		}
		str1.toLowerCase();
		assertTrue (att.getExpression().equals(str1));
	}
	
}
