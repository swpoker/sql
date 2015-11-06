package com.swpoker.util.sql.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.swpoker.util.sql.SQLClause;
import com.swpoker.util.sql.SQLFactory;
import com.swpoker.util.sql.SQLUtil;
import com.swpoker.util.sql.WhereClause;

public class SQLTest {
	static Log log = LogFactory.getLog(SQLTest.class);
	
	static ApplicationContext context ;
	
	static JdbcTemplate jdbcTemplate;
	
	@BeforeClass 
	public static void prepare(){
		//load spring
		//file
		//context=new GenericXmlApplicationContext(new FileSystemResource("sqltest/spring-config.xml"));		
		//classpath
		context=new GenericXmlApplicationContext("spring-config.xml");		
		jdbcTemplate=new JdbcTemplate(context.getBean("dataSource",DataSource.class));
	}
	
	private List<Map<String, Object>> query(SQLClause sqlclause) {
		try {			
			if(sqlclause.isEmpty()){
				log.debug("sqlclause.isEmpty()");
				return new ArrayList();
			}
			sqlclause.valid();
			log.debug(sqlclause);			 
			List<Map<String, Object>> rs = this.jdbcTemplate.queryForList(sqlclause.sql(),sqlclause.args());
			log.debug(rs.size());		
			log.debug(rs);	
			return rs;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
		
	@Test
	public void testquery(){
		log.debug(query(
					SQLFactory.SQL().append("select sysdate from dual")
				));		
	}
	
	
	List<Map<String, Object>> querycondition(String [] proftype,int [] profyear,String [] profcmonth,String profstate){		 	
		return query(
				SQLFactory.SQL()
				.append("select * from pbcmf12")
				.append(SQLFactory.WHERE()
						.and(SQLFactory.IN("PROFTYPE in (%?)", proftype))
						.and(SQLFactory.IN("PROFYEAR in (%?)", profyear))
						.and(SQLFactory.IN("PROFCMONTH in (%?)",profcmonth))						
						.test(profstate!=null && !"".equals(profstate)).and("PROFSTATE=?",profstate)
						)
				.append("order by PROFYEAR,PROFTYPE,PROFCMONTH")
			);
	}
	
	@Test
	public void testquery_allcondition(){
		querycondition(
				 new String [] {"PR","PG"} //proftype
				,new int [] {2013,2014}//profyear
				,new String [] {"201306","201408"}//profcmonth
				,"2"//profstate
				);
					
	}
	
	@Test
	public void testquery_onecondition(){
		querycondition(
				 new String [] {"PR","PG"} //proftype
				,new int [] {}//profyear
				,null//profcmonth
				,""//profstate
				);
					
	}	
	@Test
	public void testquery_nocondition(){
		querycondition(
				 new String [] {} //proftype
				,new int [] {}//profyear
				,new String [] {}//profcmonth
				,""//profstate
				);
					
	}	
	
	void _testquery_null(String profcmonth){	
		 
		query(SQLFactory.SQL()
				.append("select * from pbcmf12")
				.append(
						SQLFactory.WHERE()
						//value
						.test(StringUtils.isNotEmpty(profcmonth)).and("profcmonth=?", profcmonth)
						
						//empty
						.test("".equals(profcmonth))
						
						//null
						.test(profcmonth==null).and("profcmonth is null and PROFDMONTH is null")
				)
				);		
	}
	
	@Test
	public void testquery_value_empty_null(){
		//value :profcmonth = "201308"
		_testquery_null("201308");
		
		//empty : no condition
		_testquery_null("");
		
		//null : profcmonth is null
		_testquery_null(null);				
	}
	
	void _testquery_condition(String type){
		log.debug(String.format("type=[%s]", type));
		query(
				//sql start
				SQLFactory.SQL()
				.append("select * from pbcmf12")
				.append(
						SQLFactory.WHERE()
						.test("1".equals(type)).and("proftype in ('R','G') and PROFSTATE='2'")
						
						.test("2".equals(type)).and("MODIFYDATE > sysdate -100")
						
						.test("3".equals(type)).and("PROFSTATE='1'")
				)
						
				//sql end
				);	
	}
	
	@Test 
	public void testquery_condition(){
		
		//type = 1 : proftype in ('R','G')
		_testquery_condition("1");
		
		//type = 2 : MODIFYDATE > sysdate -100
		_testquery_condition("2");		

		//type = 3 : PROFSTATE='1'
		_testquery_condition("3");		
		
		//no match
		_testquery_condition(null);
		_testquery_condition("-1");
		
	}
	
	@Test
	public void testquery_where(){
		log.debug(WhereClause.Where()
				.and("1=1").and("2=2")
				);
		log.debug(WhereClause.Empty()
				.and("1=1").and("2=2")
				);		
		log.debug(WhereClause.And()
				.and("1=1").and("2=2")
				);
		log.debug(WhereClause.Or()
				.and("1=1").and("2=2")
				);		
	} 
	
	@Test
	public void test_union(){
		query(
				SQLFactory.UNION(
						SQLFactory.SQL().append("select * from pbcmf12 where proftype = 'PG' and profcmonth = '201510' ")
						,SQLFactory.SQL().append("select * from pbcmf12 where proftype = 'PR' and profcmonth = '201510' ")
						)
		);
	} 
	@Test
	public void test_count(){

		query(
				SQLFactory.COUNT(
						SQLFactory.UNION(
								SQLFactory.SQL().append("select * from pbcmf12 where proftype = 'PG' and profcmonth = '201510' ")
								,SQLFactory.SQL().append("select * from pbcmf12 where proftype = 'PR' and profcmonth = '201510' ")
							)
						)
		);	
		query(
				SQLFactory.COUNT(
						SQLFactory.SQL()
						)
		);		
	}
	
	
}
