package org.generation.italy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;



public class Main {
	private final static String DB_URL = "jdbc:mysql://localhost:3306/nation";				
	private final static String DB_USER = "root";
	private final static String DB_PASSWORD = "rootpassword";
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		try(Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)){
			// Se metto qui lo scanner posso non chiuderlo?
			System.out.println("Search:");
			String search = "%" + scan.nextLine() + "%";
			
			String select = "select c.country_id ,c.name as name, r.name , c2.name \r\n"
					+ "from countries c \r\n"
					+ "join regions r on c.region_id = r.region_id \r\n"
					+ "join continents c2 on r.continent_id = c2.continent_id\r\n"
					+ "where c.name like  ?  \r\n"
					+ "order by c.name ;";
			try(PreparedStatement ps = con.prepareStatement(select)){
				ps.setString(1, search);
				
				try(ResultSet rs = ps.executeQuery()){
					
					while(rs.next()) {
						System.out.println(rs.getInt("c.country_id")+ "\t\t" + rs.getString("c.name")+ "\t\t"+ rs.getString("r.name")+ "\t\t" +rs.getString("c2.name"));
					}
				}
				}

		


			} catch (SQLException e) {
				System.out.println("OOOPS an error occurred");
				System.out.println(e.getMessage());

			}
			scan.close();
		}
	}
