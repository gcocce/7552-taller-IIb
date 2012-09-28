package models.tests;

import static org.junit.Assert.*;
import models.Attribute;
import models.AttributeCollection;

import org.junit.Before;
import org.junit.Test;

public class AttributeCollectionTestCase{

	private Attribute att1;
	private Attribute att2;
	private Attribute att3;
		
	@Before
	public void setUp () {
		att1 = new Attribute ("Attribute1");
		att2 = new Attribute ("Attribute2");
		att3 = new Attribute ("Attribute2");
	}
	
	@Test 
	public void testAddAttribute() {
		AttributeCollection attCollec = new AttributeCollection();
		try {
			attCollec.addAttribute(att1);
		} catch (Exception e) {
			fail();
		}
		
		assertTrue(attCollec.existsAttribute(att1.getName()));
	}
	
	
	@Test
	public void testCountTotalAttributes() {
		AttributeCollection attCollec = new AttributeCollection();
		try {
			attCollec.addAttribute(att1);
			attCollec.addAttribute(att2);
		} catch (Exception e) {
			fail();
		}
		
		assertTrue(attCollec.count() == 2);
	}
	
	@Test(expected=Exception.class)
	public void testAddTwoElementsWithSameName() throws Exception {
		AttributeCollection attCollec = new AttributeCollection();
		attCollec.addAttribute(att2);
		attCollec.addAttribute(att3);
	}
	
	@Test
	public void testRemoveAttributeFromCollection() {
		AttributeCollection attCollec = new AttributeCollection();
		try {
			attCollec.addAttribute(att1);
			attCollec.addAttribute(att2);
			attCollec.removeAttribute(att1.getName());
		} catch (Exception e) {
			fail();
		}
		assertFalse(attCollec.existsAttribute(att1.getName()));
		assertTrue(attCollec.count()==1);
	}

	
	
	
}
