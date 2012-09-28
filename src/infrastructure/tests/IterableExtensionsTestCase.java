package infrastructure.tests;


import org.junit.Before;
import org.junit.Test;

import infrastructure.Func;
import infrastructure.IterableExtensions;

import java.util.ArrayList;
import java.util.Comparator;
import junit.framework.Assert;

public class IterableExtensionsTestCase {
    private ArrayList<Integer> iterable1;
    private ArrayList<Integer> iterable2;
    private ArrayList<Integer> iterable3;

    @Test
    public void testCountReturnsCorrectCount() {
        Assert.assertEquals(50, IterableExtensions.count(iterable1));
        Assert.assertEquals(100, IterableExtensions.count(iterable2));
    }

    @Test
    public void testFirstOrDefaultReturnsFirstValueThatMatches() {
        class CmpFunc extends Func<Integer, Integer, Boolean> {
            @Override
            public Boolean execute(Integer val, Integer param) {
                return val % param == 0;
            }
        }

        int value = IterableExtensions.firstOrDefault(this.iterable1, new CmpFunc(), new Integer(5));
        Assert.assertEquals(5, value);
    }

    @Test
    public void testFirstOrDefaultReturnsNullIfValueIsNotFound() {
        class CmpFunc extends Func<Integer, Integer, Boolean> {
            @Override
            public Boolean execute(Integer val, Integer param) {
                return val < param;
            }
        }

        Assert.assertNull(IterableExtensions.firstOrDefault(this.iterable1, new CmpFunc(), new Integer(0)));
    }

    @Test
    public void testWhereReturnsItemsThatMatchCondition() {
        class CmpFunc extends Func<Integer, Integer, Boolean> {
            @Override
            public Boolean execute(Integer val, Integer param) {
                return param == 5;
            }
        }

        Iterable<Integer> value = IterableExtensions.where(this.iterable3, new CmpFunc(), 5);
        int count = 0;
        for (Integer val : value) {
            count = (val == 5) ? count + 1 : count;
        }
        Assert.assertEquals(2, count);

        value = IterableExtensions.where(this.iterable1, new CmpFunc(), 5);
        count = 0;
        for (Integer val : value) {
            count = (val == 5) ? count + 1 : count;
        }
        Assert.assertEquals(1, count);

        value = IterableExtensions.where(this.iterable1, new CmpFunc(), 500);
        count = 0;
        for (Integer val : value) {
            count = (val == 5) ? count + 1 : count;
        }
        Assert.assertEquals(0, count);
    }
    
    @Test
    public void testShouldGetDistinctElements()
    {
    	Assert.assertEquals(100, this.iterable3.size());
    	
    	Iterable<Integer> distinct = IterableExtensions.distinct(this.iterable3, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				if (o1 < o2)
				{
					return -1;
				}
				else if (o1 > o2){
					return 1;
				}
				return 0;
			}
		});
    	
    	Assert.assertEquals(50, IterableExtensions.count(distinct));
    }

    @Before
    public void setUp() throws Exception {
        this.iterable1 = new ArrayList<Integer>();
        for (int i = 1; i < 51; i++) {
            this.iterable1.add(i);
        }

        this.iterable2 = new ArrayList<Integer>();
        for (int i = 1; i < 101; i++) {
            this.iterable2.add(i);
        }

        this.iterable3 = new ArrayList<Integer>();
        for (int i = 1; i < 51; i++) {
            this.iterable3.add(i);
        }

        for (int i = 1; i < 51; i++) {
            this.iterable3.add(i);
        }
    }
}
