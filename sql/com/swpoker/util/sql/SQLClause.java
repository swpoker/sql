package com.swpoker.util.sql;


public abstract class SQLClause {	

	
	//testnext
	private boolean _testnext_ = true;
	
	protected boolean checkTestNextStop(){
		if(this._testnext_ == true){			
			return false;
		}		
		//false
		this._testnext_=true;
		return true;
	}	
	
	protected void dotest(boolean test){
		this._testnext_=test;		
	}	
	
	private int findParms(String sql){
		int counts=0;
		for(char c : sql.toCharArray()){
			if(c=='?'){
				counts++;
			}
		}
		return counts;
	}
	
	 
	
	public SQLClause valid()  {
		if(this.isEmpty()){
			throw  new RuntimeException("sqlclause is empty");
		}
		String sql = this.sql();
		if(sql == null ){
			throw  new RuntimeException("sql is null");
		}		
		Object [] args=this.args();
		int argslength = args==null ? 0 : args.length ;
		//check ? and args
		if(findParms(sql) != argslength){
			throw  new RuntimeException("? in sql and args not match");
		}
		return this;
	}
	
	public boolean isValid(){
		try{
			this.valid();
			return true;
		}catch(RuntimeException e){
			return false;
		}
	}
	
	
	//abstract	
	public abstract boolean isEmpty();
	public abstract String sql();
	public abstract Object[] args();	
	
	@Override
	public String toString() {		
		return String.format("sql:[%s],args:[%s]", sql(),SQLUtil.join(args(), ","));
	}	
}
