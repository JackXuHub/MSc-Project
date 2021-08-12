package com.cn;


import java.sql.*;

import com.cn.Sql;
public class Sql1
{
	
	
	/**
	 * @param args
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	
	
	
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Sql database_notepad=new Sql();
		String input="abandon";
		String sql11="select English from dic where English like'"+(input)+"'";

		ResultSet rs=database_notepad.rsexecuteQuery(sql11);

		while(rs.next()) {
    			String st=rs.getString("English");
				System.out.println(st.equals("abandon"));			
			}
		}
//			


//		
//	}
//	
//	public static void main(String[] args) {
//		String st;
//		try {
//			
//			Class.forName("com.mysql.jdbc.Driver");
//			System.out.println("drivers connect successfully");
//			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary","root","317317");
//			System.out.println("the database is successfully connected");
//			Statement stmt=con.createStatement();
//			String input="good";
//		    //String Sql="select English,Chinese from dic where English like'"+(input)+"'";
//			ResultSet result = stmt.executeQuery("select English from dic where English like'"+(input)+"'");
//			
//			while(result.next()) {
//				st=result.getString("English");
//				System.out.println(st.equals("good"));
//				
//			}
//			
//		//System.out.println(st.equals("good"));
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			System.out.println("the driver fails to launch");
//			System.out.println("the database fails to launch");
//			
//		}
//		
//	}
	
	
	
	
//	public ResultSet rsexecuteQuery(String Sql) throws ClassNotFoundException, SQLException
//	{
//		Class.forName("com.mysql.jdbc.Driver");
//		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary","root","root");
//		Statement stmt=con.createStatement();
//		ResultSet rs=stmt.executeQuery(Sql);
//		return rs;
//	}
//	public int rsexecuteUpdate(String Sql) throws ClassNotFoundException, SQLException
//	{
//		
//		Class.forName("com.mysql.jdbc.Driver");
//		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql","root","root");
//		Statement stmt=con.createStatement();
//		return stmt.executeUpdate(Sql);
//		
//	}
}