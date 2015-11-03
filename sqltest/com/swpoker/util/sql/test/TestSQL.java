package com.swpoker.util.sql.test;



import org.junit.Test;


import com.swpoker.util.sql.MainSQLClause;
import com.swpoker.util.sql.SQLClause;
import com.swpoker.util.sql.SQLFactory;
import com.swpoker.util.sql.WhereClause;

public class TestSQL {
	
	void execute(SQLClause ms){
		
	//	jdbctemplage.exceteue(ms.sql(),ms.args());
	}
	
	void print(String m,SQLClause ms){
		System.out.println("*************");
		System.out.println(m);
		System.out.println(ms.isEmpty());
		System.out.println(ms.isValid());
		System.out.println(ms);
		//System.out.println("sql="+ms.sql());
		//System.out.println("args="+Arrays.asList(ms.args()));
		 
	}
	@Test
	public void testany2(){
		print("testany2",
			SQLFactory.SQL()
			.test(true).append("select ? ? from dual","a")
			.valid()
			);
	}
	
	@Test
	public void testany1(){
		String programid ="pb001,pb002,pb003";
		String userid="0000";
		String starttime="20150101";
		String endtime="";
		print("testany1",
				SQLFactory.SQL()
				.append("select * from pbjobprocess")
				.append(
						SQLFactory.WHERE()
						.test(false).and(SQLFactory.IN(" programid in (%?) ", programid.split(",")))
						.test(false).and(SQLFactory.IN(" programid2 in (%?) ", programid.split(",")))
						/*
						.and(SQLFactory.IN(" programid in (%?) ", programid.split(",")))
						.test(StrUtil.isEmpty(userid)==false).and(" userid = ?",userid)
						.test(StrUtil.isEmpty(starttime)==false).and(" starttime >= to_date(?,'yyyymmdd')?",starttime)
						.test(StrUtil.isEmpty(endtime)==false).and(" endtime <= to_date(?,'yyyymmdd')?",endtime)
						*/
						)						
		);
	}
	
	
	SQLClause testselect(String type,String apno,String idn,String col){
		return this.SQL("select * from table where type=? and apno=? and col=?", type,apno,col);
	}
	
	
	
	
	@Test
	public void testunion(){
		print("testunion",SQLFactory.UNION(this.testselect("1","10404101","A123456789","1040202")
				,this.testselect("2","10404101","A123456789","1040202")
				,this.testselect("3","10404101","A123456789","1040202")		
		));
	}
	
	@Test
	public void testinsert2(){
		print("testinsert2",
					SQLFactory.IN("SELECT * from tabela where apno in (%?) union SELECT * from tabelb where apno in (%?) ", "1", "2", "3", "4", "5", "6")
				);
	}
	
	/**
	 * 
sql=  insert into table values ( ?,?,?,?,?,? ) 
args=[1, 2, 3, 4, 5, 6]
	 */
	@Test
	public void testinsert(){
		print("testinsert",
				SQLFactory.SQL()
				// invalid
				//.append("insert into table values(?,?,?,?,?)",new Object[]{"1","2","3","4","5","6"})
				.append(SQLFactory.IN("insert into table values (%?) ",new Object[]{"1","2","3","4","5","6"}))
		);
	}
	
	@Test
	public void testsq14(){
		SQLClause sql=this.SQL()
		//select 1		
		.append(this.testselect("1","10404101","A123456789","1040202"))
		//select 2
		.test(true).append(this.SQL().append("union").append(this.testselect("2","10404101","A123456789","1040202")))		
		//select 3
		.test(false).append(this.SQL().append("union").append(this.testselect("3","10404101","A123456789","1040202")))
		//select 4
		.test(true).append(this.SQL().append("union").append(this.testselect("4","10404101","A123456789","1040202")))
		;
		print("testsq14",sql);
		
	}
	
	
	
	@Test
	public void testsq13(){
		
		
		print("testsq13",
				this.SQL()						
		);
		print("testsq13",
				this.SQL("select sysdate from dual")						
		);		
		print("testsq13",
				this.SQL("select * from tabel where cl=?","a1")						
		);	
		
		print("testsq13",
				this.SQL()
				.append(this.SQL("select * from tabelA"))
		);			
		print("testsq13",
				this.SQL()
				.append(this.SQL("select * from tabelA"))
				.append(this.where().and("cal='1'").and("ca2 is not null"))
		);	
		print("testsq13",
				this.SQL()
				.append(this.SQL("select * from tabelA"))
				.test(false).append(this.where().and("cal='1'").and("ca2 is not null"))
				
		);	
		
		print("testsq13",
				this.SQL()
				.append(this.SQL("select * from tabelA"))
				.append("order by cola desc")
				.test(false).append(",colb")
				.test(true).append(",colc")
				.test(false).append(",cold")
		);			
	}	
	
	
	
	
	
	private WhereClause where() {		 
		return WhereClause.WhereWhereClause();
	}
	
	private MainSQLClause SQL(String sql, Object ... args) {		
		return SQL().append(sql,args);
	}
	/*
	protected MainSQLClause SQL(String sql) {		 
		return SQL().append(sql);
	}
	*/
	protected MainSQLClause SQL() {		 
		return MainSQLClause.getInstance();
	}
	
	
	
	@Test
	public void testsql2(){
		
		print("testsql2",
				
				MainSQLClause.getInstance()
				//.append(SQLUtil.BuildSQLInSQLClause(" and dfasf( select from aa where bb in %? )    ", new Object[]{"a","b","b","b"}))
				//.appendIn(String.format("aa in (%?)", SQLUtil.ArgSign(6) ), new Object[]{"a","b"})
				.append(SQLFactory.IN(" and dfasf( select from aa where bb in1 (%?)  in2 (%?) )    ", new Object[]{"a","b","b","b"}))
				.append(SQLFactory.IN(" and dfasf( select from aa where bb in1 (%?)  in2 (%?) in3 (%?) )    ", new Object[]{"a","b","b","b"}))
				.append(SQLFactory.IN(" and dfasf( select from aa where bb in (%?)   )    ", new Object[]{"a","b","b","b"}))
				
				//invalid
				.append(SQLFactory.IN(null, null))
				.append(SQLFactory.IN("and no ", new Object[]{"a","b","b","b"}))
				//.appendIn("",)				 
				 
		);
		
		
		
	}	
	
	@Test
	public void testsqlWhereClause(){
		String[] args = new String[]{"a","b","b","b"};
		print("",				
				WhereClause.WhereWhereClause().and(SQLFactory.IN("bb in %?", new Object[]{"a","b","b","b"}))			 
				 
		);
		
		
		
	}		
	
	@Test
	public void testsql(){
		
		
		
		
		
		SQLClause sql1=MainSQLClause.getInstance().append("(select * from table1 where a=? )","a");
		SQLClause sql2=MainSQLClause.getInstance()
		.append("( select * from table2  ")
		.test(true).append(WhereClause.WhereWhereClause().test(true).and(" t21=?","t21"))
		.append(" )")
		;
		
		
		
		SQLClause sql3=MainSQLClause.getInstance().append("(select * from table3   where c=? )","c");
		
		MainSQLClause ms=MainSQLClause.getInstance().append(sql1);
		if(true){
			ms.append(sql2);
		}
		if(false){
			ms.append(sql3);
		}

		print("testsql",ms);
		
		/*
		print("testsql",MainSQLClause.getInstance()
				.append("select * from table"));
		*/
	}
	
	@Test
	public void testsqlarg(){
		String attr3chek="rattr3";
		print("testsqlarg",MainSQLClause.getInstance()
				.append("select * from table")
				.append("where attr1 in ( ? ,?) ","p11","p22",3)
				.append("and attr2 < 2")
				.test(attr3chek.equals( "rattr3")).append("and attr3 = ?", "rattr3")
				.append("and attr4 betwwen 6 and 999")
				.test(false).append("and attr5 is null")
				.append(MainSQLClause.getInstance().append("(select * from table)"))
				);
	}
	
	@Test
	public void testwhere(){
		/*
		SQLClause sql1=MainSQLClause.getInstance().append("(select * from table1 where a=??? )","a");
		SQLClause sql2=MainSQLClause.getInstance().append("(select * from table2  where b=? )","b");
		SQLClause sql3=MainSQLClause.getInstance().append("(select * from table3   where c=? )","c");
		
		
		MainSQLClause sql=MainSQLClause.getInstance();
		if(true){
			sql.append(sql1);
		}
		
		if(true){
			sql.append(sql2);
		}
		
		if(true){
			sql.append(sql3);
		}
		print("testwhere",sql);
		*/
		 
		print("testwhere1",WhereClause.WhereWhereClause()
				.test(true).and(" attr1 = ?", "rattr1")
				.test(false).and("attr2 is not null")				
		);
		print("testwhere2",WhereClause.WhereWhereClause()
				.test(false).and(" attr1 = ?", "rattr1")
				.test(false).and("attr2 is not null")				
		);
		 
	}
	
	
	
}
