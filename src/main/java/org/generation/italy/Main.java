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
					String id = "id";
					String cName = "country";
					String rName = "region";
					String conName = "continent";
					System.out.format("%3s%15s%25s%15s%n",id , cName , rName, conName);
					
					while(rs.next()) {
						System.out.format("%3s%15s%25s%15s%n",rs.getInt("c.country_id"), rs.getString("c.name"), rs.getString("r.name"),rs.getString("c2.name"));
					}
				}
				}
			System.out.println("Choose a country id: ");
			int countryId = scan.nextInt();
			System.out.print("Languages:");
			
			String searchId = "select *\r\n"
					+ "from countries c \r\n"
					+ "join country_languages cl ON c.country_id = cl.country_id \r\n"
					+ "join languages l on cl.language_id = l.language_id \r\n"
					+ "where c.country_id = ?;";
			try(PreparedStatement ps = con.prepareStatement(searchId)){
				ps.setInt(1, countryId);
				try(ResultSet rsLang = ps.executeQuery()){
					int i = 0;
					String[] languages = new String[12];
					int languagesCount = 0;
					while(rsLang.next()) {
						languages[i] = rsLang.getString("l.language");
						i++;
						languagesCount ++;
//						System.out.print(rsLang.getString("l.language")+ ", ");
						
					}
					for(int j= 0 ; j < languagesCount ;j++) {
						if (j<languagesCount - 1) {
							System.out.print(languages[j] + ", ");
						}else {
							System.out.print(languages[j] + ".");
						}
						
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
