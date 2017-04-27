package com.datastructure.list;

import java.util.ArrayList;
import java.util.ListIterator;

public class ListIteratorExample {

	public static void main(String[] args) {
		System.out.println("hello");
		ArrayList al = new ArrayList();
		for(int i=1;i<=5;i++)al.add(i);
		ListIterator iter = al.listIterator();
		while(iter.hasNext()){
			System.out.println("Prev "+iter.previousIndex());
			int obj = (Integer)iter.next();
			System.out.println(obj);
			if(obj==3){
				System.out.println("In if");
				iter.set(110);
			}
			System.out.println("Next "+iter.nextIndex());
		}
		System.out.println(al);
	}

}
