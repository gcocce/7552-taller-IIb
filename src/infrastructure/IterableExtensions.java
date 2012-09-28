package infrastructure;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class IterableExtensions {
    public static <T> int count(Iterable<T> iterable) {
        int count = 0;
        for (@SuppressWarnings("unused") T item : iterable) {
            count++;
        }
        return count;
    }

    public static <T> List<T> getListOf(Iterable<T> iterable){
        List<T> list = new ArrayList<T>();
        for(T item : iterable){
            list.add(item);
        }
        return list;
    }
    
    public static <T> List<T> getListOf(T[] array){
        List<T> list = new ArrayList<T>();
        for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
        return list;
    }

    public static <T> T firstOrDefault(Iterable<T> iterable) {
        return IterableExtensions.firstOrDefault(iterable, new Func<T, Boolean, Boolean>(){

			@Override
			public Boolean execute(T p1, Boolean p2) {
				return p2;
			}}, true);
    }
    
    public static <T, U> T firstOrDefault(Iterable<T> iterable, Func<T, U, Boolean> func, U param) {
        for (T item : iterable) {
            if (func.execute(item, param)) {
                return item;
            }
        }
        return null;
    }
    
    public static <T, Param, U> Iterable<U> select(Iterable<T> iterable, Func<T, Param, U> func, Param param) {
        ArrayList<U> allItems = new ArrayList<U>();
        for (T item : iterable) {
            allItems.add(func.execute(item, param));
        }
        return allItems;
    }

    public static <T, U> Iterable<T> where(Iterable<T> iterable, Func<T, U, Boolean> func, U param) {

        ArrayList<T> allItems = new ArrayList<T>();
        for (T item : iterable) {
            if (func.execute(item, param)) {
                allItems.add(item);
            }
        }
        return allItems;
    }

	public static <T> Iterable<T> distinct(Iterable<T> iterable, Comparator<T> comparator) {
		Set<T> set = new TreeSet<T>(comparator);
		
		for (T item : iterable) {
			set.add(item);
		}
		
		return set;
	}

	public static <T> Iterable<T> getIterableOf(T[] array) {
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		
		return list;
	}
}
