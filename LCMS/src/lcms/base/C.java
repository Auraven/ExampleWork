package lcms.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class C {
	public static Connection myConn;
	public static Statement myStmt;
	public static boolean connected;

	public static boolean connect() {
		myConn = null;
		myStmt = null;
		try {
			// 1. Get a connection to database
			myConn = DriverManager.getConnection("jdbc:mysql://starcktech.com:3306/APP", "Group3", "passwordJava");
			myStmt = myConn.createStatement();
		} catch (Exception exc) {
			exc.printStackTrace();
			return false;
		}
		connected = true;
		return true;
	}
	public static boolean insert(String table, String[] args){
		connect();
		if(connected){
			String values = "";
			for(int i = 0; i < args.length; i++){
				if(i > 0){
					values += "," + "'" + args[i] + "'";
				}else{
					values += "'" + args[i] + "'";
				}
			}
			String query = "INSERT INTO " + table + " VALUES (" + values + ")";
			try {
				myStmt.executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
				return false;				
			}
			return true;
		}
		return false;
	}
	public static boolean update(String table, String target, String value, String selector, String select){
		connect();
		if(connected){			
			String query = "UPDATE " + table + " SET " + target + " = '" + value + "' WHERE " + selector + " = '" + select + "'";
			try {
				myStmt.executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	public static boolean delete(String table, String selector, String target){
		connect();
		if(connected){
			String query = "DELETE FROM " + table + " WHERE " + selector + "='" + target + "'";
			try {
				myStmt.executeUpdate(query);
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		return false;
	}
	public static ResultSet select(String table, String selector){
		connect();
		if(connected){			
			String query = "SELECT " + selector + " FROM " + table;
			try {
				return myStmt.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	public static ResultSet selectWhere(String table, String selector, String target){
		connect();
		if(connected){			
			String query = "SELECT " + selector + " FROM " + table + " WHERE " + selector + " = '" + target + "'";
			try {
				return myStmt.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	public static ResultSet selectWhere(String table, String selectorA, String selectorB, String target){
		connect();
		if(connected){			
			String query = "SELECT " + selectorA + " FROM " + table + " WHERE " + selectorB + " = '" + target + "'";
			try {
				return myStmt.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
}
