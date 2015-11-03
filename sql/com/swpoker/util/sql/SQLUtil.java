package com.swpoker.util.sql;

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

}
