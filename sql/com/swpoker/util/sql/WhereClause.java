package com.swpoker.util.sql;


public class WhereClause extends SQLClause{
	//static 
	private static enum WhereClauseType{EMPTY,WHERE,AND,OR}	;
	
	private MainSQLClause msql =  MainSQLClause.getInstance();
	
	//object
	private WhereClauseType wct;


	
	WhereClause(WhereClauseType wct){
		this.wct=wct;
	}
	
	
	
	public static WhereClause Where(){
		return new WhereClause(WhereClauseType.WHERE);
	}	
	
	public static WhereClause Empty(){
		return new WhereClause(WhereClauseType.EMPTY);
	}	
	
	public static WhereClause And(){
		return new WhereClause(WhereClauseType.AND);
	}
	
	public static WhereClause Or(){
		return new WhereClause(WhereClauseType.OR);
	}	
	
	private String  _getWhereClauseType(WhereClauseType _wct){
		switch (_wct){
			case  EMPTY :return "";
			case  WHERE :return "WHERE";
			case  AND :return "AND";
			case  OR :return "OR";
			default :return "";
		}
	}
	
	private boolean isParenthesis(){
		switch (this.wct){
		case  EMPTY :return true;
		case  WHERE :return false;
		case  AND :return  true;
		case  OR :return  true;
		default :return  true;
		}		
	} 
	
	private String preParenthesis(){
		if(isParenthesis()){
			return " ( ";
		}
		return "";
	}
	
	private String subParenthesis(){
		if(isParenthesis()){
			return " ) ";
		}
		return "";		
	}
	
	
	String fisrtAppend(){
		return (" "+_getWhereClauseType(this.wct))+preParenthesis();
	}
	
	

	
	private WhereClause _append(String type,String clause,Object ... args){		
		//test
		if(checkTestNextStop()){			
			return this;
		}
		
		this.msql.append((this.msql.isEmpty()?fisrtAppend():type))
		.append(" (")
		.append(clause,args)
		.append(") ");
		return this;				
	}		
		
	private WhereClause _append(String type,WhereClause whereClause){	
		if(whereClause.isEmpty()){
			return this;
		}
		return _append(type,whereClause.sql(),whereClause.args());
	}
	

	
	
	public WhereClause and(WhereClause whereClause){			
		return _append(_getWhereClauseType(WhereClauseType.AND),whereClause);					
	}
	
	public WhereClause and(String clause,Object ... args){					
		return _append(_getWhereClauseType(WhereClauseType.AND),clause,args);		 
	}	
	public WhereClause and(SQLClause sql) {	
		if(sql.isEmpty()){
			return this;
		}
		return  and(sql.sql(),sql.args());
	}	
	
	
	public WhereClause or(WhereClause whereClause){			
		return _append(_getWhereClauseType(WhereClauseType.OR),whereClause);					
	}	

	public WhereClause or(String clause,Object ... args){			
		return _append(_getWhereClauseType(WhereClauseType.OR),clause,args);											
	}
	
	public WhereClause or(SQLClause sql) {
		if(sql.isEmpty()){
			return this;
		}		
		return  or(sql.sql(),sql.args());
	}		
	
	public WhereClause test(boolean test){
		dotest(test);
		return this;
	}
	
	
	@Override
	public boolean isEmpty() {
		
		return msql.isEmpty();
	}

	@Override
	public Object[] args() {		
		return msql.args();
	}	
	
	@Override
	public String sql(){
		if(msql.isEmpty()){
			return msql.sql();
		}
		msql.append(subParenthesis());
		return msql.sql();
	}
	
}
