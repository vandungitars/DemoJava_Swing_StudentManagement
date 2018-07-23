package ConnectDatabaseStudent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MyConnect 
{
	private final String className = "com.mysql.jdbc.Driver"; 
	private final String url = "jdbc:mysql://localhost:3306/student";
	private final String user = "root";
	private final String password = "vandung";
	private String table = "student_info";
	private Connection connection;
	
	public void showData(ResultSet rs)
	{
		if(rs == null)
		{
			System.out.println("Not Data!");
		}
		else
		{
			System.out.println("ID\tName\t\t\tPoint");
			try {
				while(rs.next())
				{
					System.out.printf("%-7d %-23s %-5.2f \n",rs.getInt(1),rs.getString(2),rs.getDouble(3));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ResultSet getData()
	{
		ResultSet rs = null;
		String command = "select * from " + table;
		Statement st;
		try {
			st = connection.createStatement();
			rs = st.executeQuery(command);
		} catch (SQLException e) {
			System.out.println("Get Data Error!");
		}	
		return rs;
	}
	
	public ResultSet getDataID(int id)
	{
		String command = "select * from " + table + " where id = ?";
		ResultSet rs = null;
		PreparedStatement pst = null;
		try {
			pst = connection.prepareStatement(command);
			pst.setLong(1, id);
			rs = pst.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return rs;
	}

	public void deleteDataID(int id)
	{
		String command = "delete from " + table + " where id = ?";
		try {
			PreparedStatement pst =  connection.prepareStatement(command);
			pst.setLong(1, id);
			int x = pst.executeUpdate();
			if( x > 0 )
			{
				System.out.println("Delete success!");
			}
			else
			{
				System.out.println("Can not Delete!");
			}
		} catch (SQLException e) {
			System.out.println("Delete Error " + e.toString());
		}
	}
	
	public void updateData(int id, Student sv)
	{
		String command = "update " + table + " set name = ?, point = ? where id = ?";
		try {
			PreparedStatement pst =  connection.prepareStatement(command);
			pst.setString(1, sv.getName());
			pst.setDouble(2, sv.getPoint());
			pst.setLong(3, sv.getId());
			int x = pst.executeUpdate();
			if( x > 0 )
			{
				System.out.println("Update success!");
			}
			else
			{
				System.out.println("Can not Update!");
			}
		} catch (SQLException e) {
			System.out.println("Update Error " + e.toString());
		}
	}
	
	public void insertData(Student sv)
	{
		String command = "insert into " + table + " values(?, ?, ?)";
		try {
			PreparedStatement pst =  connection.prepareStatement(command);
			pst.setLong(1, sv.getId());
			pst.setString(2, sv.getName());
			pst.setDouble(3, sv.getPoint());
			int x = pst.executeUpdate();
			if( x > 0 )
			{
				System.out.println("Insert success!");
			}
			else
			{
				System.out.println("Can not Insert!");
			}
		} catch (SQLException e) {
			System.out.println("Insert Error " + e.toString());
		}
	}
	
	public void connect()
	{
		try {
			Class.forName(className);
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("Connection success!");
		} 
		catch (ClassNotFoundException e) {
			System.out.println("Class not found!");
		} catch (SQLException e) {
			System.out.println("Mysql can not connection!");
		}
	}
}