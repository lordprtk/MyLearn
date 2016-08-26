package com.java.general;

class InnerClassIntialization{
    public static void main(String args[]){
    	InnerClassIntialization prc = new InnerClassIntialization();
    	Test test = prc.new Test();
    	test.testPrint();
    	prc.testPrint();
	}
    
    public void testPrint(){
    	System.out.println("Printing from main class");
    }
    
    class Test{
    	public Test(){
    		System.out.println("Constructor of Test");
    	}
    	
    	public void testPrint(){
    		System.out.println("Printing from test class");
    	}
    }
}
