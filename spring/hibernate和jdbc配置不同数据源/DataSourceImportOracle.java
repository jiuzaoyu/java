package com.changyou.erp.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

import com.changyou.erp.common.PropertiesEncryptFactoryBean;

public class DataSourceImportOracle  extends BasicDataSource{
	public DataSourceImportOracle(){
		super();
		String url = "";
		InputStream is = null;
		Properties p = null;		
		 try {		      
			  is = this.getClass().getClassLoader().getResourceAsStream("conf/jdbc.properties");
			  PropertiesEncryptFactoryBean encryptor = new PropertiesEncryptFactoryBean();
			  p = new Properties();
		      p.load(is);
		      //String password = encryptor.deEncryptString(p.getProperty("oracle.jdbc.password"));
			  String password = p.getProperty("password_importOracle").trim();
			  this.setPassword(password);
		    } catch (Exception e) {
		      e.printStackTrace();
		    } finally {
		      try {
		        is.close();
		      } catch (IOException e) {
		        e.printStackTrace();
		      }
		    }
	}		
}
