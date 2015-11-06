package com.swpoker.util.sql;

import java.util.ArrayList;
import java.util.List;

public class SQLUtil {
	   
	public static String join(Object[] array, String separator)
	   {
	     if (array == null) {
	       return null;
	     }
	     if (separator == null) {
	       separator = "";
	     }
	     int arraySize = array.length;	     	 	 		     	     	 	 	 		
	     StringBuffer buf = new StringBuffer(array.length);	     
	     for (int i = 0; i < arraySize; i++) {
	       if (i > 0) {
	         buf.append(separator);
	       }
	       if (array[i] != null) {
	         buf.append(array[i]);
	       }
	     }
	     return buf.toString();
	   }
	 
	public static Object[] dealParameters(Object ... args){		
		if(args == null){
			return null;
		}
		List rs = new ArrayList();
		for(Object a : args){
			if(a.getClass().isArray()){
				if(a instanceof byte[]){for(byte ao : (byte[])a){rs.add(ao);};}
				if(a instanceof short[]){for(short ao : (short[])a){rs.add(ao);};}
				if(a instanceof int[]){for(int ao : (int[])a){rs.add(ao);};}
				if(a instanceof long[]){for(long ao : (long[])a){rs.add(ao);};}
				if(a instanceof float[]){for(float ao : (float[])a){rs.add(ao);};}
				if(a instanceof double[]){for(double ao : (double[])a){rs.add(ao);};}
				if(a instanceof char[]){for(char ao : (char[])a){rs.add(ao);};}				 
			}else{
				rs.add(a);
			}
		}		
		return rs.toArray();		
	}
	
	
}
