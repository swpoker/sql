package com.swpoker.util.sql;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

 



public class SQLFactory {
	private static String ArgSign(int size){
		String[] rs = new String[size];
		Arrays.fill(rs, "?");		 
		return SQLUtil.join(rs, ",");		
	}	
	
	
	/**
	 * 
	 * MainSQLClause
	 * 
	 * @return
	 */
	public static MainSQLClause SQL(){
		return MainSQLClause.getInstance();
	}
	
	public static WhereClause WHERE(){
		return WhereClause.Where();
	}	
		
	
	 /**
	  * ("aa in (%?)", new Object[]{"a","b"})
	  * --> aa in (?,?) ,[a,b] <br>
	  * 
	  * ("aa in (%?) and bb in (%?)", new Object[]{"a","b"})
	  * --> aa in (?,?) and bb in (?,?) ,[a,b,a,b] <br>
	  * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public static SQLClause IN(String sql,Object ... args){
		 	MainSQLClause rs = SQL();
			if(sql == null){
				return rs;
			}			
			Object [] _args = SQLUtil.dealParameters(args);
			if(_args == null || _args.length == 0){
				return rs;
			}			
			String _s="%?";
			if(sql.contains(_s) == false){
				return rs;
			}											
			
			List _argrs = new ArrayList(); 
			int _sl = _s.length();
			for(int i=sql.indexOf(_s);i>-1;i=sql.indexOf(_s,i+_sl)){
				_argrs.addAll(Arrays.asList(_args));				
			}														
			rs.append(sql.replace(_s, String.format(" %s ", SQLFactory.ArgSign(_args.length))), _argrs.toArray());			
			return rs;
	 }
	
	
	public static SQLClause UNION(SQLClause ... sqllist){
		MainSQLClause rs = SQL();
		if(sqllist == null || sqllist.length == 0){
			return rs ;
		}
		
		List<String> sql=new ArrayList();
		List args=new ArrayList();
		for(SQLClause sc : sqllist){
			sql.add(sc.sql());
			args.addAll(Arrays.asList(sc.args()));
		}		
				
		return rs.append(SQLUtil.join(sql.toArray()," UNION " ), args.toArray());
	}
	
	public static SQLClause COUNT(SQLClause sqlclause){
		return  SQL().test(sqlclause!= null && sqlclause.isEmpty() == false)
		.append(
			 SQL().append("SELECT COUNT(1) FROM (")
			.append(sqlclause)
			.append(")")
		)
		;
	}
	
	
}
