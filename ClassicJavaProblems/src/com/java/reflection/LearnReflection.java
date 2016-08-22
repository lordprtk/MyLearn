package com.java.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

class Test{
	private String str;
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public Test(){
		this.str = "Constructor initialization";
	}
	public Test(String s){
		this.str = "String passed to constructor -"+s;
	}
	public void publicMethod(String str){
		System.out.println("In public method, string passed-"+str);
	}
	private void privateMethod_1(String str){
		System.out.println("In private method 1, string passed-"+str);
	}
	private void privateMethod_2(){
		System.out.println("In private method 2");
	}
}
public class LearnReflection {
	public static void main(String[] args){
		Test test = new Test("s");
		Class cls = test.getClass();
		Constructor[] cstrs = cls.getConstructors();
		for(Constructor cstr:cstrs){
			System.out.println(cstr.getName());
			for(Class c:cstr.getParameterTypes()){
				System.out.println(c.getName());
			}
		}
		Method[] methods = cls.getMethods();
		for(Method meth: methods){
			System.out.println(meth.getName());
		}
		try{
			Method meth = cls.getDeclaredMethod("privateMethod_1", java.lang.String.class);
			meth.setAccessible(true);
			meth.invoke(test, "passed param");
			meth = cls.getDeclaredMethod("publicMethod", java.lang.String.class);
			meth.invoke(test, "public string Param passed");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
