package edu.dufe.oes.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MySetUtil {

	public static List<Object> toList(Set<Object> objects){
		List<Object> list=new ArrayList<Object>();
		Iterator<Object> iterator = objects.iterator();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}
}
