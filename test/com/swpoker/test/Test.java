package com.swpoker.test;

import java.lang.reflect.Field;
import java.util.Arrays;

import com.swpoker.webb.TestCBean;

public class Test {
	
	
	static String buildMethodName(String pre,String  name){						
		return pre+name.substring(0,1).toUpperCase()+name.substring(1);
	}	
	
	public static void main(String [] args){
		
		//System.out.printf("%1$.1s".toUpperCase(), "abc");
		//System.out.println(buildMethodName("set","a"));
		//System.out.println(buildMethodName("set","abc"));
		
		//Class c=TestCBean.class;
		Object v = new TestCBean();
		for(Class c = v.getClass() ; c != null && c.getName().equals(Object.class.getName()) == false ;c=c.getSuperclass()){
			System.out.println(c.getName());
		}
		
		//System.out.println(Arrays.toString(TestCBean.class.getDeclaredFields()));
		//System.out.println(Arrays.toString(TestCBean.class.getSuperclass().getDeclaredFields()));
		
		
	}
}
