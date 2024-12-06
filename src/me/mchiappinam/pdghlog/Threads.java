package me.mchiappinam.pdghlog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Threads extends Thread {
	private Main plugin;
	private String dateTimebase;
	
	private String nick;
	private String role;
	private String type;
	private String log;
	private String world;
	private String location;
	private String dateTime;
	public Threads(String db, String nk, String tp, String lg, String mo, String ll, String da) {
		dateTimebase=db;
		nick=nk;
		type=tp;
		log=lg;
		world=mo;
		location=ll;
		dateTime=da;
	}
	public Threads(String db, String nk, String cg, String tp, String lg, String mo, String ll, String da) {
		dateTimebase=db;
		nick=nk;
		role=cg;
		type=tp;
		log=lg;
		world=mo;
		location=ll;
		dateTime=da;
	}
	
	public void run() {
		switch(dateTimebase) {
			case "general": {
				try {
					Connection con = DriverManager.getConnection(Main.mysql_url,Main.mysql_user,Main.mysql_pass);
					//Prepared statement
					PreparedStatement pst = con.prepareStatement("INSERT INTO "+Main.tableGeneral+"(server, nick, type, log, world, location, dateTime) VALUES(?, ?, ?, ?, ?, ?, ?)");
						//Values
						pst.setString(1, Main.serverName);
						pst.setString(2, nick);
						pst.setString(3, type);
						pst.setString(4, log);
						pst.setString(5, world);
						pst.setString(6, location);
						pst.setString(7, dateTime);
						//Do the MySQL query
						pst.executeUpdate();
						pst.close();
						con.close();
						break;
				}catch (SQLException ex) {
					plugin.getServer().getLogger().severe("Erro!");
					System.out.print(ex);
					break;
				}
			}
			case "staff": {
				try {
					Connection con = DriverManager.getConnection(Main.mysql_url,Main.mysql_user,Main.mysql_pass);
					//Prepared statement
					PreparedStatement pst = con.prepareStatement("INSERT INTO "+Main.tableStaff+"(server, nick, role, type, log, world, location, dateTime) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
						//Values
						pst.setString(1, Main.serverName);
						pst.setString(2, nick);
						pst.setString(3, role);
						pst.setString(4, type);
						pst.setString(5, log);
						pst.setString(6, world);
						pst.setString(7, location);
						pst.setString(8, dateTime);
						//Do the MySQL query
						pst.executeUpdate();
						pst.close();
						con.close();
						break;
				}catch (SQLException ex) {
					plugin.getServer().getLogger().severe("Erro!");
					System.out.print(ex);
					break;
				}
			}
		}
	}
}
