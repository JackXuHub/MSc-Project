package com.cn;
import java.sql.*;
public class Sql
{
	
	public ResultSet rsexecuteQuery(String Sql) throws ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary","root","317317");
		Statement stmt=con.createStatement();
		ResultSet rs=stmt.executeQuery(Sql);
		return rs;
	}

}