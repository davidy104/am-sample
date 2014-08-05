package com.aucklandmuseum.image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class EmptyListTest {

	@Test
	public void test() {
		List<String> list = Collections.<String> emptyList();
		String[] array = new String[] { "a", "b" };

		System.out.println("list size:" + list.size());
		
		if (array.length > 0) {
			list = new ArrayList<String>(array.length);
			for (String s : array) {
				list.add(s);
			}

			System.out.println("list size:" + list.size());
		}
	}

}
