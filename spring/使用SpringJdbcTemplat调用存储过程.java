package com.changyou.erp.bx.service.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import oracle.jdbc.OracleTypes;

import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;

public class 使用SpringJdbcTemplat调用存储过程 {
	@Resource
	protected JdbcTemplate jdbcTemplate;
	@Resource
	private JdbcTemplate importOracleJdbcTemplate;
	
	/*
	 情况一：无返回值的存储过程调用
	存储过程如下：
	CREATE OR REPLACE PROCEDURE TESTPRO(PARAM1 IN VARCHAR2,PARAM2 IN VARCHAR2) AS   
	BEGIN  
		INSERT INTO TESTTABLE (ID,NAME) VALUES (PARAM1, PARAM2);  
	END TESTPRO;
	java代码如下：
	*/
	@Test
	public void test1(){   
		jdbcTemplate.execute("call testpro('p1','p2')");   
	} 
	
	/*
	 情况二：有返回值的存储过程（非结果集）
	存储过程如下：
	CREATE OR REPLACE PROCEDURE TESTPRO(PARAM1 IN VARCHAR2,PARAM2 OUT VARCHAR2) AS  
	BEGIN   
	    SELECT INTO PARAM2 FROM TESTTABLE WHERE ID= PARAM1;   
	END TESTPRO;
	java代码如下：
	*/
	@Test
	public void test2() {   
		String param2Value = (String) jdbcTemplate.execute(   
				new CallableStatementCreator() {   
					public CallableStatement createCallableStatement(Connection con) throws SQLException {   
						String storedProc = "{call testpro(?,?)}";// 调用的sql   
						CallableStatement cs = con.prepareCall(storedProc);   
						cs.setString(1, "p1");// 设置输入参数的值   
						cs.registerOutParameter(2, OracleTypes.VARCHAR);// 注册输出参数的类型   
						return cs;   
					}   
				}, new CallableStatementCallback() {   
					public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
						cs.execute();   
						return cs.getString(2);// 获取输出参数的值   
					}   
				});   
	} 
	/*
	下面是在项目中的例子：
	存储过程为：
	apps.cux_erp_apinv_pkg.apinv_input(p_batch_id IN NUMBER, p_return_status OUT VARCHAR2)
    p_batch_id => 批ID
    p_return_status => 返回状态，S：成功  E：失败
	调用方法：
    */
	public String getOracleFeedback(final Integer batchId){
		try {
			String param2Value = (String) importOracleJdbcTemplate.execute(   
				     new CallableStatementCreator() {   
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {   
				           String storedProc = "{call apps.cux_erp_apinv_pkg.apinv_input (?,?) }";// 调用的sql   
				           CallableStatement cs = con.prepareCall(storedProc);   
				           cs.setInt(1, batchId);// 设置输入参数的值   
				           cs.registerOutParameter(2, OracleTypes.VARCHAR);// 注册输出参数的类型   
				           return cs;   
				        }   
				     }, new CallableStatementCallback() {   
				         public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
				           cs.execute();   
				           return cs.getString(2);// 获取输出参数的值   
				     }   
				  }); 
			return param2Value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 情况三：有返回值的存储过程（结果集）
	 因oracle存储过程所有返回值都是通过out参数返回的,列表同样也不例外,但由于是集合,所以不能用一般的参数,必须要用pagkage,分两部分:  
	1.建一个程序包,如下：
	CREATE OR REPLACE PACKAGE TESTPACKAGE AS
		TYPE TEST_CURSOR IS REF CURSOR;
	END TESTPACKAGE; 
	2.建立存储过程,如下：
	CREATE OR REPLACE PROCEDURE TESTPRO(PARAM1 IN VARCHAR2,test_cursor out TESTPACKAGE.TEST_CURSOR) IS   
	BEGIN   
    	OPEN test_cursor FOR SELECT * FROM TESTTABLE;   
	END TESTPRO; 
	可以看到，列表是通过把游标作为一个out参数来返回的。
	java代码如下：
	*/
	@Test
    public void test() {   
        List resultList = (List) jdbcTemplate.execute(   
           new CallableStatementCreator() {   
              public CallableStatement createCallableStatement(Connection con) throws SQLException {   
                 String storedProc = "{call testpro(?,?)}";// 调用的sql   
                 CallableStatement cs = con.prepareCall(storedProc);   
                 cs.setString(1, "p1");// 设置输入参数的值   
                 cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型   
                 return cs;   
              }   
           }, new CallableStatementCallback() {   
              public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {   
                 List resultsMap = new ArrayList();   
                 cs.execute();   
                 ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值   
                 while (rs.next()) {// 转换每行的返回值到Map中   
                    Map rowMap = new HashMap();   
                    rowMap.put("id", rs.getString("id"));   
                    rowMap.put("name", rs.getString("name"));   
                    resultsMap.add(rowMap);   
                 }   
                 rs.close();   
                 return resultsMap;   
              }   
        });   
        for (int i = 0; i < resultList.size(); i++) {   
           Map rowMap = (Map) resultList.get(i);   
           String id = rowMap.get("id").toString();   
           String name = rowMap.get("name").toString();   
           System.out.println("id=" + id + ";name=" + name);   
        }   
      }   
	
	

}
