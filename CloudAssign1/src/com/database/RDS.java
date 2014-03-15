package com.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.*;

import org.aspectj.weaver.ast.Test;




public class RDS {
	public Connection connect = null;
	public Statement statement = null;
	public ResultSet resultSet = null;
	public String DB_END_POINT = "mydb1.ctbpooua5ltr.us-east-1.rds.amazonaws.com";
	public final String DB_USER_NAME = "rich";
	public final String DB_PWD = "11111111";
	public final String DB_NAME = "mydb1";
	public final int DB_PORT = 3306;
	
	
	public void createConnectionAndStatement()
	{
		try{
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://"+DB_END_POINT+":"+DB_PORT+"/"+DB_NAME,DB_USER_NAME,DB_PWD);
					//.getConnection("jdbc:mysql://liguifaninstance1.clionqr9dgrf.us-west-2.rds.amazonaws.com:3306/mysqldb?user=liguifan&password=Li932942");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
			close();
		}
	}

	public void insertTable(String Tablename,String name, String link) throws SQLException{
		String insert="insert into "+Tablename+" values(\""+name+"\",\""+link+"\")";
		System.out.println(insert);
		createConnectionAndStatement();
		
	statement.executeUpdate(insert);
	}
	
	public ArrayList<String> queryTable(String Tablename) throws SQLException{
		createConnectionAndStatement();
		String query="select V.s3link from "+Tablename+" V ";//where V.name= \""+name+"\"";
		System.out.println(query);
		ResultSet rs=statement.executeQuery(query);
		ArrayList<String> ArrL=new ArrayList<String>();
		boolean hasnext=rs.next();
		while (hasnext){		
		//System.out.println(rs.getString("S3link"));
		ArrL.add(rs.getString("S3link"));
		hasnext=rs.next();
		}
		return ArrL;
	}
	
	public void returnUrl(ResultSet rs) throws SQLException{
//		ArrayList<String> result=new ArrayList<String>();
		while ( rs.next() ) {
            int numColumns = rs.getMetaData().getColumnCount();
            for ( int i = 1 ; i <= numColumns ; i++ ) {
               // Column numbers start at 1.
               // Also there are many methods on the result set to return
               //  the column as a particular type. Refer to the Sun documentation
               //  for the list of valid conversions.
               System.out.println( "COLUMN " + i + " = " + rs.getObject(i));
            }
        }
		
	    
	}
	
	public void createTable(String Tablename)
	{
		try {
			createConnectionAndStatement();
			//String createTableSql = "CREATE TABLE VIDEO_INFO (name VARCHAR(255) not NULL, timestamp TIMESTAMP, " + 
				//	" s3link VARCHAR(255), cflink VARCHAR(255), rating INTEGER, totalvotes INTEGER)";
			//String createTableSql ="CREATE TABLE STUDENT ( name VARCHAR(255) )";
			String createTableSql = "CREATE TABLE "+Tablename+" (name VARCHAR(255) not NULL, s3link VARCHAR(255) ) ";
			statement.executeUpdate(createTableSql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//close();
		}

	}
	
	public void deleteTable(String tableName) throws SQLException{
		createConnectionAndStatement();
		String delete="drop table "+tableName;
		System.out.println(delete);
		statement.executeUpdate(delete);
	}
	
	// You need to close the resultSet
	public void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}
	
	
// The following is used to test RDS
	
//	public static void main(String args[]) throws SQLException{
//		Test t=new Test();
//		//String road="insert into VIDEO_INFO values(\"Music2\",\"dddd\")";
//		//String road1="insert into VIDEO_INFO values(\"Music3\",\"dddd1\")";
//				//t.create_bucket_upload("/Users/Rich/Desktop/Rich.jpg");
//		RDS2 r=new RDS2();
//		String name1="Music2";
//		String name2="Music3";
//		String link1="dddd";
//		String link2="dddd1";
//		//r.deleteTable("VIDEO_INFO");
//		String tablename="VEDIO";
//		//r.createTable(tablename);
//
//		r.insertTable(tablename,name1,link1);
//		r.insertTable(tablename,name2,link2);
//		ResultSet rs=r.queryTable(tablename);
//		
//		ArrayList<String> ArrL=new ArrayList<String>();
//		boolean hasnext=rs.next();
//		while (hasnext){		
//		//System.out.println(rs.getString("S3link"));
//		ArrL.add(rs.getString("S3link"));
//		hasnext=rs.next();
//		}
//		
//		
//		for(String x:ArrL){
//		System.out.println(x);
//		}
//		
//	}


} 


