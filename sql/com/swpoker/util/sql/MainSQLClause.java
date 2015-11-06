package com.swpoker.util.sql;


import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;



public class MainSQLClause extends SQLClause{
			
	protected int flag = 0 ; // 0:init ,1:start
	protected StringBuilder sb = new StringBuilder();
	protected List _args=new ArrayList();	
	
	private  MainSQLClause(){
		
	}
	
	public static MainSQLClause getInstance(){
		return new MainSQLClause();
	}
		
	
	public MainSQLClause append(String sql,Object ... args){	
		
		if(this.checkTestNextStop()){
			return this;
		}
		
		this.flag=1;
		this.sb.append(" "+sql);
		//for(Object arg : args ){this._args.add(arg);}
		this._args.addAll(Arrays.asList(SQLUtil.dealParameters(args)));
		return this;
	}	
	
	/**
	 * .appendIn("aa in %?", new Object[]{"a","b"})
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public MainSQLClause append(SQLClause sqlclause){		
		if(sqlclause.isEmpty()){
			return this;
		}
		return append(sqlclause.sql(),sqlclause.args());
	}	
	
	public MainSQLClause test(boolean test){
		this.dotest(test);
		return this;
	}
	
	public boolean isEmpty(){
		return this.flag == 0 ;
	}
	
	public String sql(){
		return  sb.toString();
	}
	
	public Object[] args(){
		return this._args.toArray();
	}

	

	
	
}
