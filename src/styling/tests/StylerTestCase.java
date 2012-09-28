package styling.tests;

import org.junit.Assert;
import org.junit.Test;

import styling.StyleConstants;
import styling.Styler;
import models.EntityType;


public class StylerTestCase {
	
	@Test
	public void testShouldReturnCorrectColorBasedOnEntityType() throws Exception
	{
		Assert.assertEquals(StyleConstants.DOMAIN_COLOR, Styler.getFillColor(EntityType.Domain));
		Assert.assertEquals(StyleConstants.THING_COLOR, Styler.getFillColor(EntityType.Thing));
		Assert.assertEquals(StyleConstants.HISTORIC_COLOR, Styler.getFillColor(EntityType.Historic));
		Assert.assertEquals(StyleConstants.PROGRAMMED_COLOR, Styler.getFillColor(EntityType.Programmed));
		Assert.assertEquals(StyleConstants.NONE_COLOR, Styler.getFillColor(EntityType.None));
	}
}
