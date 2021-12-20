package org.generation.italy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	private final static String URL = "jdbc:mysql://localhost:3306/nation";
	private final static String USER = "root";
	private final static String PASSWORD = "rootpassword";

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);

		try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

			System.out.println("Search:");
			String search = "%" + scan.nextLine() + "%";

			String select = "select c.country_id ,c.name as name, r.name , c2.name \r\n" + "from countries c \r\n"
					+ "join regions r on c.region_id = r.region_id \r\n"
					+ "join continents c2 on r.continent_id = c2.continent_id\r\n" + "where c.name like  ?  \r\n"
					+ "order by c.name ;";

			// risultati della ricerca per caratteri
			try (PreparedStatement ps = con.prepareStatement(select)) {
				ps.setString(1, search);

				try (ResultSet rs = ps.executeQuery()) {
					String id = "id";
					String cName = "country";
					String rName = "region";
					String conName = "continent";
					System.out.format("%3s%15s%25s%15s%n", id, cName, rName, conName);

					while (rs.next()) {
						System.out.format("%3s%15s%25s%15s%n", rs.getInt("c.country_id"), rs.getString("c.name"),
								rs.getString("r.name"), rs.getString("c2.name"));
					}
				}
			}
			System.out.println("Choose a country id: ");
			int countryId = scan.nextInt();

//			int numMaxLang = 0;

//			String maxLanguages = "select count(cl.language_id) as countLanguage, cl.country_id \r\n"
//					+ "from country_languages cl \r\n" + "group by cl.country_id \r\n"
//					+ "order by countLanguage desc \r\n" + "limit 1;\r\n";
//			// ricerca del numero massimo di lingue presenti in uno stato
//			try (PreparedStatement ps = con.prepareStatement(maxLanguages)) {
//				try (ResultSet rsMaxLang = ps.executeQuery()) {
//					while (rsMaxLang.next()) {
//						numMaxLang = rsMaxLang.getInt("countLanguage");
//					}
//
//				}
//
//			}

			// elenco lingue parlate
			String searchId = "select *\r\n" + "from countries c \r\n"
					+ "join country_languages cl ON c.country_id = cl.country_id \r\n"
					+ "join languages l on cl.language_id = l.language_id \r\n" + "where c.country_id = ?;";

			try (PreparedStatement ps = con.prepareStatement(searchId)) {
				ps.setInt(1, countryId);
				try (ResultSet rsLang = ps.executeQuery()) {

					System.out.print("Languages: ");
					while (rsLang.next()) {
						if (rsLang.isLast()) {
							System.out.println(rsLang.getString("l.language") + ".");
						}else {
							System.out.print(rsLang.getString("l.language") + ", ");
						}
					

					}
//					String languagesFormatted = languages.substring(0, languages.length() - 2) + ".";
//					System.out.println(languagesFormatted);
//					
//					for (int j = 0; j < languagesCount; j++) {
//						if (j < languagesCount - 1) {
//							System.out.print(languages[j] + ", ");
//						} else {
//							System.out.print(languages[j] + ".\n");
//						}
//
//					}
					
				}
               
			}
			// elenco statistiche
			String sqlStats = "select *\r\n" + "from countries c \r\n"
					+ "join country_stats cs on c.country_id = cs.country_id \r\n" + "where c.country_id = ?\r\n"
					+ "order by cs.`year` desc\r\n" + "limit 1 ;";
			try (PreparedStatement statsPs = con.prepareStatement(sqlStats)) {
				statsPs.setInt(1, countryId);
				try (ResultSet statsRs = statsPs.executeQuery()) {
					while (statsRs.next()) {
						System.out.println("Year: " + statsRs.getInt("cs.year"));
						System.out.println("Population: " + statsRs.getString("cs.population"));
						System.out.println("GDP: " + statsRs.getString("cs.gdp"));
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
