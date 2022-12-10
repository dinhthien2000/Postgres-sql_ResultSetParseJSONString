package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class InterVal {

	public static final String jdbcURL = "jdbc:postgresql://103.146.21.197:5432/dbtemplate_billing";
	public static final String jdbcUsername = "admin";
	public static final String jdbcPassword = "cHZNV7w9h4NcKhsm93dbslDHlj3";

	// phương thức kết nối với CSDL
	public static Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("org.postgresql.Driver");
			connection = (Connection) DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
			System.out.println("sucsses connect");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;

	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Connection connection = getConnection();
		String sql = "select *\r\n"
				+ "		        from thongkecost_vs3(\r\n"
				+ "		            (to_timestamp( '2021-10-02' , 'yyyy-mm-dd hh24:mi:ss'))::timestamp without time zone,\r\n"
				+ "					(to_timestamp( '2022-10-02' , 'yyyy-mm-dd hh24:mi:ss'))::timestamp without time zone\r\n"
				+ "					, ''\r\n"
				+ "					, ''\r\n"
				+ "					, ''\r\n"
				+ "					, ''\r\n"
				+ "					, ''\r\n"
				+ "					, 'adminvanhanh'\r\n"
				+ "					, ''\r\n"
				+ "					, '' );";
		// Creating a JSONObject object
		JSONObject jsonObject = new JSONObject();
		// Creating a json array
		JSONArray array = new JSONArray();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int numCols = md.getColumnCount();
			String col = "tongthoigiandamthoai_";
			while(rs.next()) {
				JSONObject record = new JSONObject();
				for(int i = 1 ; i < numCols; i++) {
					if (md.getColumnName(i).equals(col)) {						
						org.postgresql.util.PGInterval interval =
							    (org.postgresql.util.PGInterval) rs.getObject(col);
						String time = interval.getHours()+" : "+interval.getMinutes()+" : "+(int)interval.getSeconds();
						record.put( md.getColumnName(i) , time);
					}else {
						
						record.put(md.getColumnName(i), rs.getObject(i));
					}		
				}
				array.add(record);
			}
			
			 jsonObject.put("Players_data", array.toString());
			 System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("fail");
		}

	}
}
