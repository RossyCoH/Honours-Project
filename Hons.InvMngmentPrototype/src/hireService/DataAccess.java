package hireService;

import java.sql.*;
import java.util.ArrayList;

/***
 * Class to handle data input and output to database for hire service
 * @author Ross Hunter
 * @dateCreated 26/01/2023
 * @dateLastModified 12/03/2023
 *
 */
public class DataAccess implements DataInterface {

	Connection conn; // Class to hold
	
	public DataAccess() {
		
	}
	
	/**
	 * Method to open DB connection to hire DB
	 * @return if connection is opened successful 
	 */
	private boolean openConnection() {
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/hons_hire?user=Java&password=Java&useSSL=false");
			conn.setAutoCommit(false);
			return true;
		}
		catch (ClassNotFoundException cnf)
		{
			System.err.println("Could not load driver");
			System.err.println(cnf.getMessage());
			System.exit(-1);
			return false;
		}
		catch (SQLException sqe)
		{
			System.err.println("Error connecting to Database");
			System.err.println(sqe.getMessage());
			System.exit(-1);
			return false;
		}
		
		
	}
	
	/**
	 *  Method to close DB connection
	 * @return if connection is closed successful 
	 */
	private boolean closeConnection() {
		try {
			conn.close();
			conn = null;
			return true;
		} catch (SQLException sqe) {
			System.err.println("Error in closing connection");
			System.err.println(sqe.getMessage());
			System.exit(-1);
			return false;
		}
		
		
	}
	
	public ArrayList<Hire> getHireRecords() {
		
		Statement statement = null;
		ArrayList<Hire> hireList = new ArrayList<Hire>();
		
		if (openConnection())
		{
			try {
				statement = conn.createStatement();
				String query = "SELECT * FROM hire INNER JOIN client on hire.clientID = client.clientID ORDER BY hireID";
				ResultSet results = statement.executeQuery(query);
				while (results.next())
				{
					Integer hireID = results.getInt("hireID");
					Date startDate = results.getDate("startDate");
					Date endDate = results.getDate("endDate");
					boolean complete = results.getBoolean("complete");
					Integer clientID = results.getInt("clientID");
					String clientName = results.getString("clientName");
					Hire hire = new Hire(hireID, startDate, endDate, complete, clientID, clientName);
					hireList.add(hire);
				}
				
				
			} catch(SQLException sqe) {
				
			}
			
		}
		
		closeConnection();
		
		return hireList;
	}
	
	/*
	public ArrayList<Hire> getHireRecord(int clientID) {
		
		Statement statement = null;
		ArrayList<Hire> hireList = new ArrayList<Hire>();
		
			try {
				statement = conn.createStatement();
				String query = "SELECT * FROM hire WHERE clientID = " + clientID + "";
				ResultSet results = statement.executeQuery(query);
				while (results.next())
				{
					Integer hireID = results.getInt("hireID");
					Date startDate = results.getDate("startDate");
					Date endDate = results.getDate("endDate");
					Hire hire = new Hire(hireID, startDate, endDate, clientID);
					hireList.add(hire);
				}
				
				
			} catch(SQLException sqe) {
				
			}
		
		return hireList;
	}
	*/
	
	/***
	 * Function to get Hire from ID
	 * @param hID - id number of hire
	 * @return hire - hire class with data
	 */
	public Hire getHire(int hID) {
		
		Hire hire = null;
		Statement statement = null;
		
		if (openConnection())
		{
			try {
				statement = conn.createStatement();
				String query = "SELECT * FROM hire WHERE HireID ="+ hID + ";";
				ResultSet results = statement.executeQuery(query);
				while (results.next())
				{
					Integer hireID = results.getInt("hireID");
					Date startDate = results.getDate("startDate");
					Date endDate = results.getDate("endDate");
					boolean complete = results.getBoolean("complete");
					Integer clientID = results.getInt("clientID");
					hire = new Hire(hireID, startDate, endDate, complete, clientID);
				}
				
				
			} catch(SQLException sqle) {
				sqle.printStackTrace();
			}
			
		}
		
		closeConnection();
		
		return hire;
	}
	
	/***
	 * Function to get equipment based on search term
	 * @param searchTerm - inputed string to search with
	 * @return equipList - list of equipment items
	 */
	public ArrayList<Equipment> getEquipmentBySearch(String searchTerm) {
		
		PreparedStatement statement = null;
		ArrayList<Equipment> equipList = new ArrayList<Equipment>();
		
		if (openConnection()) {
			
			// Test:
			// SELECT * FROM equipment WHERE equipmentModel LIKE '%Radio%' OR equipmentType LIKE '%Radio%' AND OnHire IS TRUE AND EquipmentID NOT IN (23123458,23123459,23123461) 
			
			try {
				statement = conn.prepareStatement("SELECT * FROM equipment WHERE equipmentModel LIKE ? AND OnHire IS TRUE");
				//String query = "SELECT * FROM equipment WHERE equipmentModel LIKE '%" + searchTerm +"%' OR equipmentType LIKE '%" + searchTerm + "%' AND OnHire IS TRUE AND EquipmentID NOT IN ?;";
				statement.setString(1, "%" + searchTerm + "%");
				ResultSet results = statement.executeQuery();
				while (results.next())
				{
					Integer equipID = results.getInt("EquipmentID");
					String equipModel = results.getString("EquipmentModel");
					String equipType = results.getString("EquipmentType");
					Equipment newEQ = new Equipment(equipID, equipModel, equipType);
					equipList.add(newEQ);
				}
				
				
			} catch(SQLException sqle) {
				sqle.printStackTrace();
			}
			
		}
		
		closeConnection();
		
		return equipList;
		
	}
	
	/***
	 * Method to get all equipment items available
	 * @return equipList - ArrayList of all equipment items
	 */
	public ArrayList<Equipment> getEquipmentAvailable() {
		
		PreparedStatement statement = null;
		ArrayList<Equipment> equipList = new ArrayList<Equipment>();
		
		if (openConnection()) {
			
			try {

				statement = conn.prepareStatement("SELECT * FROM equipment WHERE OnHire IS TRUE");
				//String query = "SELECT * FROM equipment WHERE equipmentModel LIKE '%" + searchTerm +"%' OR equipmentType LIKE '%" + searchTerm + "%' AND OnHire IS TRUE AND EquipmentID NOT IN ?;";
				ResultSet results = statement.executeQuery();
				while (results.next())
				{
					Integer equipID = results.getInt("EquipmentID");
					String equipModel = results.getString("EquipmentModel");
					String equipType = results.getString("EquipmentType");
					Equipment newEQ = new Equipment(equipID, equipModel, equipType);
					equipList.add(newEQ);
				}
				
				
			} catch(SQLException sqle) {
				sqle.printStackTrace();
			}
			
		}
		
		closeConnection();
		
		return equipList;
		
	}
	
	/***
	 * Method to return available equipment items of a particular type
	 * @param type - Equipment type selected by the user
	 * @return equipList - ArrayList of all equipment items
	 */
	public ArrayList<Equipment> filterEquipment(String type) {
		
		PreparedStatement statement = null;
		ArrayList<Equipment> equipList = new ArrayList<Equipment>();
		
		if (openConnection()) {
			
			try {

				statement = conn.prepareStatement("SELECT * FROM equipment WHERE OnHire IS TRUE AND EquipmentType LIKE ?");
				//String query = "SELECT * FROM equipment WHERE equipmentModel LIKE '%" + searchTerm +"%' OR equipmentType LIKE '%" + searchTerm + "%' AND OnHire IS TRUE AND EquipmentID NOT IN ?;";
				statement.setString(1, "%" + type + "%");
				ResultSet results = statement.executeQuery();
				while (results.next())
				{
					Integer equipID = results.getInt("EquipmentID");
					String equipModel = results.getString("EquipmentModel");
					String equipType = results.getString("EquipmentType");
					Equipment newEQ = new Equipment(equipID, equipModel, equipType);
					equipList.add(newEQ);
				}
				
				
			} catch(SQLException sqle) {
				sqle.printStackTrace();
			}
			
		}
		
		closeConnection();
		
		return equipList;
	}
	
	/**
	 * Method  to get all equipment items with a hire
	 * @param hireID - int of hireID
	 * @return equipList - ArrayList of all equipment items
	 */
	public ArrayList<Equipment> getEquipmentByHire(int hireID) {
		
		Statement statement = null;
		ArrayList<Equipment> equipList = new ArrayList<Equipment>();
		
		if (openConnection())
		{
			try {
				statement = conn.createStatement();
				String query = "SELECT * FROM equipmenthire AS eh INNER JOIN equipment AS eq ON eh.EquipmentID = eq.EquipmentID "
						+ "WHERE eh.hireID=" + hireID + "";
				ResultSet results = statement.executeQuery(query);
				while (results.next())
				{
					Integer equipID = results.getInt("EquipmentID");
					String equipModel = results.getString("EquipmentModel");
					String equipType = results.getString("EquipmentType");
					boolean Available = results.getBoolean("Available");
					Equipment newEQ = new Equipment(equipID, equipModel, equipType, Available);
					equipList.add(newEQ);
				}
				
				
			} catch(SQLException sqle) {
				sqle.printStackTrace();
			}
			
		}
		
		closeConnection();
		
		return equipList;
	}
	
	//
	public ArrayList<Hire> clientHireRecords(int clientID) {
		
		Statement statement = null;
		ArrayList<Hire> hireList = new ArrayList<Hire>();		
		
		if(openConnection()) {
			
			try {
				statement = conn.createStatement();
				String query = "SELECT * FROM hire WHERE clientID = "+ clientID +"";
				ResultSet results = statement.executeQuery(query);
				while (results.next()) {
					Integer hireID = results.getInt("hireID");
					Date startDate = results.getDate("startDate");
					Date endDate = results.getDate("endDate");
					boolean complete = results.getBoolean("complete");
					Hire hire = new Hire(hireID, startDate, endDate, complete);
					hireList.add(hire);
				}
			} catch (SQLException sqle) {
				// TODO Auto-generated catch block
				sqle.printStackTrace();
			}
			
		}
		
		closeConnection();
		
		return hireList;
	}
	
	/**
	 * Method to get list of usage records for a equipment item on a hire
	 * @param hireID
	 * @param equipID
	 * @return usageList
	 */
	public ArrayList<EqUsage> getEquipmentUsageList(int hireID, int equipID) {
		
		ArrayList<EqUsage> usageList = new ArrayList<EqUsage>();
		PreparedStatement statement = null;
		
		if(openConnection()) {
			
			try {
				statement = conn.prepareStatement("SELECT * FROM equipmentusage eu INNER JOIN operator o ON eu.opID = o.opID WHERE eu.hireID = ? AND eu.equipmentID = ?");
				//String query = "SELECT * FROM equipmenthire AS eh INNER JOIN equipmentusage AS eu ON eh.equipmentID = eu.equipmentID INNER JOIN operator AS o ON eu.opID = o.opID "+ "WHERE eh.hireID = ? AND eh.equipmentID = ?";
				//SELECT ID FROM equipmenthire WHERE hireID = 150 AND equipmentID = 232624328 LIMIT 1;
				statement.setInt(1, hireID);
				statement.setInt(2, equipID);
				ResultSet results = statement.executeQuery();
				while (results.next()) {
					Integer usageID = results.getInt("usageID");
					Timestamp outDate = results.getTimestamp("outDateTime");
					Timestamp returnDate = results.getTimestamp("returnDateTime");
					String notes = results.getString("notes");
					int opID = results.getInt("OpID");
					String opName = results.getString("OpName");
					
					EqUsage usage;
					if (returnDate == null) {
						usage = new EqUsage(usageID, outDate.toLocalDateTime(), null, notes, opID, opName);
					}
					else {
						usage = new EqUsage(usageID, outDate.toLocalDateTime(), returnDate.toLocalDateTime(), notes, opID, opName);
					}
					
					usageList.add(usage);
				}
			} catch (SQLException sqle) {
				// TODO Auto-generated catch block
				sqle.printStackTrace();
			}
			
		}
		
		closeConnection();
		
		return usageList;
	}
	
	/***
	 * Method to get the availably status of the equipment within a hire (e.g. can it be check-out)
	 * @param equipID
	 * @return res
	 */
	public boolean getEquipmentStatus(int equipID) {
		Statement statement = null;
		boolean res = false;
		
		if(openConnection()) {
			try {
				statement = conn.createStatement();
				String query = "SELECT Available FROM equipment WHERE EquipmentID = "+ equipID +" LIMIT 1";
				ResultSet results = statement.executeQuery(query);
				if (results.next()) {
					res = results.getBoolean("Available");
				}
				statement.close();
			}
			catch (SQLException sqle) {
				sqle.printStackTrace();
				
				closeConnection();
				return res;
			}
		}
		
		closeConnection();
		
		return res;
	}
	
	// 
	/***
	 * Function to change equipment status (if available)
	 */
	public boolean updateEquipmentStatus(int equipID, boolean status) {
		
		PreparedStatement statement = null;
		
		if(openConnection()) {
			
			try {
				statement = conn.prepareStatement("UPDATE equipment SET Available = ? WHERE EquipmentID = ?");
				statement.setBoolean(1, status);
				statement.setInt(2, equipID);
				int result = statement.executeUpdate();
				
				if (result == 1)
				{
					conn.commit();
					statement.close();
				}
				else {
					
					conn.rollback();
					statement.close();
					closeConnection();
					return false;
				}
				
			}
			catch (SQLException sqle) {
				sqle.printStackTrace();
				return false;
			}
		}
		
		closeConnection();
		return true;
	}
	
	/***
	 *  Method to insert new record of usage into equipmentUsage Table
	 *  @param eqID - equipment ID of item being checked out
	 *  @param hID - hire ID of hire equipment is assigned to
	 *  @param opID - ID number of operator to use equipment
	 *  @return if query is successful or not
	 */
	public boolean addUsageRecord(String outDT, int eqID, int hID, int opID) {
		PreparedStatement statement = null;
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		//String outDT = LocalDateTime.now().format(formatter);
		//System.out.println(outDT);
		
		if(openConnection()) {
			try {
				statement = conn.prepareStatement("INSERT INTO equipmentusage (OutDateTime, OpID, EquipmentID, HireID)"+
						"VALUES (?,?,?,?)");
				statement.setString(1, outDT);
				statement.setInt(2, opID);
				statement.setInt(3, eqID);
				statement.setInt(4, hID);
				
				int res = statement.executeUpdate();
				if (res == 1) {
					conn.commit();
					statement.close();
				}
				else {
					conn.rollback();
					statement.close();
					
					closeConnection();
					return false;
				}
				
			} catch (SQLException sqle) {
				sqle.printStackTrace();
				return false;
			}
		}
		else {
			return false;
		}
		
		closeConnection();
		
		return true;
	}
	
	/***
	 * Function to add operator to record for client
	 * @param name - Name of operator
	 * @param cID - ID of client operator is with
	 * @return result - if query is successful
	 */
	public boolean addOperator(String name, int cID) {
		
		boolean result = false;
		PreparedStatement statement = null;
		
		if(openConnection()) {
			try {
				statement = conn.prepareStatement("INSERT INTO operator (OpName, ClientID) VALUES(?,?)");
				statement.setString(1, name);
				statement.setInt(2, cID);
				
				int res = statement.executeUpdate();
				if (res == 1) {
					conn.commit();
					result = true;
					statement.close();
				}
				else {
					conn.rollback();
					statement.close();
				}
				
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		
		closeConnection();
		return result;
	}
	
	/***
	 * Method to get all operators for set client
	 * @param clientID - id number of current client
	 * @return ops - arrayList of operator ids and names 
	 */
	public ArrayList<String> getOperators(int clientID) {
		ArrayList<String> ops = new ArrayList<String>();
		PreparedStatement statement = null;
		
		if(openConnection()) {
			try {
				statement = conn.prepareStatement("SELECT OpID, OpName FROM operator WHERE clientID = ?");
				statement.setInt(1, clientID);
				
				ResultSet results = statement.executeQuery();
				while (results.next()) {
					String value =  results.getString("opID") + ", " + results.getString("opName");
					ops.add(">" + value);
				}
			} catch (SQLException sqle) {
				// TODO Auto-generated catch block
				sqle.printStackTrace();
			}
		}
		closeConnection();
		
		return ops;
	}
	
	/***
	 * Method to update equipment usage record with date/Time and note
	 * @param inDT - date/Time of completion
	 * @param note - string note of condition provided by user
	 * @param eqID - ID number of equipment item
	 * @param hID - ID number of hire equipment is assigned to 
	 * @return if update statement is executed successfully
	 */
	public boolean updateUsageRecord(String inDT, String note, int eqID, int hID) {
		
		PreparedStatement statement = null;
		
		int usageID = getUsageID(eqID, hID);
		
		if (usageID == -1) {
			return false;
		}
		
		if(openConnection()) {
			try {
				statement = conn.prepareStatement("UPDATE equipmentusage SET ReturnDateTime = ?, Notes = ? WHERE UsageID = ?");
				statement.setString(1, inDT);
				statement.setString(2, note);
				statement.setInt(3, usageID);
				int res = statement.executeUpdate();
				if (res == 1) {
					conn.commit();
				}
				else {
					conn.rollback();
					statement.close();
					closeConnection();
					return false;
				}
				
				statement.close();
			} catch (SQLException sqle) {
				// TODO Auto-generated catch block
				sqle.printStackTrace();
				
				closeConnection();
				return false;
			}
		}
		else {
			return false;
		}
		
		closeConnection();
		
		return true;
	}
	
	/***
	 * Function to get equipment items from selected ID values
	 * @param selectEquipIDs (int[]) - array of ID numbers as ints
	 * @return equipList (ArrayList<Equipment>) - List of equipment
	 */
	public ArrayList<Equipment> getSelectedEquipment(int[] selectEquipIDs) {
		ArrayList<Equipment> equipList = new ArrayList<Equipment>();
		PreparedStatement statement = null;
		
		if (openConnection()) {
			try {
				for (int id : selectEquipIDs) {
					statement = conn.prepareStatement("SELECT equipmentID, EquipmentModel, EquipmentType FROM equipment WHERE equipmentID= ? LIMIT 1");;
					statement.setInt(1, id);
					ResultSet results = statement.executeQuery();
					if (results.next()) {
						int eID = results.getInt("equipmentID");
						String eModel = results.getString("EquipmentModel");
						String eType = results.getString("EquipmentType");
						Equipment equip = new Equipment(eID, eModel, eType);
						equipList.add(equip);
					}
				}
				
			} catch(SQLException sqle) {
				closeConnection();
			}
			
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		closeConnection();
		
		return equipList;
	}
	
	/***
	 * Function to add hire
	 * @param startDate - supplied date of hire start
	 * @param endDate - supplied date of hire end
	 * @param clientID - id of client hire is associated with
	 * @return boolean - if insert/update queries have been successful
	 */
	public boolean addHire(Date startDate, Date endDate, int clientID) {
		//Statement statement = null;
		
		if (openConnection()) {
			try {
				PreparedStatement statement = conn.prepareStatement("INSERT INTO hire (StartDate, EndDate, Complete, ClientID) " +
						"VALUES (?,?,?,?)");
				statement.setDate(1, startDate);
				statement.setDate(2, endDate);
				statement.setBoolean(3, false);
				statement.setInt(4, clientID);
				int res = statement.executeUpdate();
				if (res == 1) {
					conn.commit();
				}
				else {
					conn.rollback();
					statement.close();
					return false;
				}
				
				statement.close();
				
			} catch (SQLException sqle) {
				// TODO Auto-generated catch block
				sqle.printStackTrace();
				closeConnection();
				return false;
			}
		}
		else {
			closeConnection();
			return false;
		}
		
		closeConnection();
		return true;
	}
	
	/***
	 * Function to add record of equipment items on specific hire and update equipment availability
	 * @param equipIDs - array of ID numbers of equipment items
	 * @return boolean - if all insert and update queries have been successful
	 */
	public boolean setEquipmentHire(int[] equipIDs) {
		
		int hireID;
		
		if (openConnection()) {
			
			hireID = getLastID(); //Get last ID used
			if (hireID == -1) {
				closeConnection();
				return false;
			} else {

				PreparedStatement ieStatement;
				//PreparedStatement uStatement;
				try {

					// For each equipment id
					for (int i : equipIDs) {

						ieStatement = null;
						//uStatement = null;

						//Insert into table 
						ieStatement = conn
								.prepareStatement("INSERT INTO equipmenthire (EquipmentID, HireID) VALUES (?,?)");
						ieStatement.setInt(1, i);
						ieStatement.setInt(2, hireID);
						int result = ieStatement.executeUpdate();
						// If successful, change equipment item status
						if (result == 1) {
							//conn.commit(); // Commit changes
							//ieStatement.close();

							ieStatement = conn
									.prepareStatement("UPDATE equipment SET OnHire = ? WHERE EquipmentID = ?");
							ieStatement.setBoolean(1, false);
							ieStatement.setInt(2, i);
							int result2 = ieStatement.executeUpdate();
							if (result2 != 1) {

								conn.rollback();
								ieStatement.close();
								closeConnection();
								return false;
							}

						} else {

							conn.rollback();
							ieStatement.close();
							closeConnection();
							return false;
						}

						conn.commit();
						ieStatement.close();
					}

				} catch (SQLException sqle) {
					// TODO Auto-generated catch block
					sqle.printStackTrace();
					closeConnection();
					return false;
				}
			} 
		}
		
		closeConnection();
		return true;
	}
	
	/***
	 * Function to set complete hire field to true for selected hire
	 * @param hireID - Hire to be set to complete
	 * @return boolean - If update query operated correctly
	 */
	public boolean completeHire(int hireID) {
		
		if (openConnection()) {
			
			try {
				PreparedStatement statement = conn.prepareStatement("UPDATE Hire SET Complete = ? WHERE HireID=?");
				statement.setBoolean(1, true);
				statement.setInt(2, hireID);
				int result = statement.executeUpdate();
				
				if (result == 1)
				{
					conn.commit();
					statement.close();
				}
				else {
					conn.rollback();
					statement.close();
					closeConnection();
					return false;
				}
				
			} catch (SQLException sqle) {
				// TODO Auto-generated catch block
				sqle.printStackTrace();
			}
			
		}
		closeConnection();
		return true;
	}
	
	public boolean hireCompleteState(int hireID) {
		
		if (openConnection()) {
			
			try {
				PreparedStatement statement = conn.prepareStatement("SELECT * FROM hire WHERE Complete = ? and HireID= ?");
				statement.setBoolean(1, true);
				statement.setInt(2, hireID);
				ResultSet result = statement.executeQuery();
				if (result.next())
				{
					statement.close();
				}
				else {
					statement.close();
					closeConnection();
					return false;
				}
				
			} catch (SQLException sqle) {
				// TODO Auto-generated catch block
				sqle.printStackTrace();
			}
			
		}
		closeConnection();
		return true;
	}
	
	/***
	 * Function to set any equipment from a set hire to available again
	 * @param equipIDs - List of equipment IDs to be updates
	 * @return boolean - If update query operated correctly
	 */
	public boolean setEquipmentAvilable(int[] equipIDs) {
		
		if (openConnection()) {

			try {
				// For each equipment id
				for (int i : equipIDs) {
					//Insert into table 
					PreparedStatement statement = conn.prepareStatement("UPDATE equipment SET OnHire = ?, Available = ? WHERE EquipmentID = ?");
					statement.setBoolean(1, true);
					statement.setBoolean(2, true);
					statement.setInt(3, i);
					int result = statement.executeUpdate();
					
					if (result == 1)
					{
						conn.commit();
						statement.close();
						
					}
					else {
						conn.rollback();
						statement.close();
						closeConnection();
						return false;
					}
					
					
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		closeConnection();
		return true;
	}
	
	/***
	 * Method to get the last ID in the hire table
	 * @return int - hireID 
	 */
	private int getLastID() {
		int hireID = -1;
		
		try {
			PreparedStatement statement = conn.prepareStatement("SELECT HireID FROM hire ORDER BY HireID DESC LIMIT 1");
			ResultSet results = statement.executeQuery();
			while(results.next()) {
				hireID = results.getInt("HireID");
			}
			statement.close();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		return hireID;
	}
	
	/***
	 * Function to add client record to client table in DB
	 * @param clientID
	 * @param clientName
	 * @param clientAddress
	 * @param contactName
	 * @param contactAddress
	 * @return Whether true or not
	 */
	public boolean addClient(String clientName, String clientAddress, String contactName, String contactAddress, String password) {
		PreparedStatement statement = null;
		
		if (addAccount(clientName, password, "Client")) {
			
			int accountID = getAccountID(clientName);
			
			if (accountID == -1) {
				return false;
			}
			else {
				
				if (openConnection()) {
					try {
						
						statement = conn.prepareStatement("INSERT INTO client (ClientName, ClientAddress, ContactName, ContactAddress, AccountID)"
								+ "VALUES (?,?,?,?,?)");
						statement.setString(1, clientName);
						statement.setString(2, clientAddress);
						statement.setString(3, contactName);
						statement.setString(4, contactAddress);
						statement.setInt(5, accountID);
						int i = statement.executeUpdate();
						if (i == 1) {
							conn.commit();
						}
						statement.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						closeConnection();
						return false;
					}
					
				}
			}
			
			closeConnection();
		}
		
		return true;
	}
	
	/***
	 * Method to check user supplied login details match one in system
	 * @param name
	 * @param password
	 * @return result
	 */
	public String loginCheck(String name, String password) {
		
		String result = null;
		
		if(openConnection()) {
			try {
				PreparedStatement statement = conn.prepareStatement("SELECT * from Account WHERE AccountName = ? AND passwordHash = ?");
				statement.setString(1, name);
				statement.setString(2, password);
				ResultSet results = statement.executeQuery();
				if (results.next()) {
					int accountID = results.getInt("AccountID");
					String uType = results.getString("AccountType");
					String aName = results.getString("AccountName");
					result = accountID + ">" + uType + ">" + aName;
					
					statement.close();
				}

			} catch (SQLException sqle) {
				sqle.printStackTrace();
				closeConnection();
				return result;
			}
		}
		
		closeConnection();
		return result;
		
	}
	
	/***
	 * Function to get clientID from the accountID, to set client
	 * @param accountID - verified accountID
	 * @return clientID - id of associated client (or -1 if error)
	 */
	public int getClientID(int accountID) {
		int clientID = -1;
		if (openConnection()) {
			try {
				PreparedStatement statement = conn.prepareStatement("SELECT AccountID, ClientID from Client WHERE AccountID = ?");
				statement.setInt(1, accountID);
				ResultSet results = statement.executeQuery();
				if (results.next()) {
					clientID = results.getInt("ClientID");
				}
			} catch (SQLException sqle) {
				// TODO Auto-generated catch block
				sqle.printStackTrace();
				closeConnection();
			}
		}
		closeConnection();
		return clientID;
	}
	
	/***
	 * Method to get list of all clients
	 * @return clients
	 */
	public ArrayList<String> getClients() {
		
		ArrayList<String> clients = new ArrayList<String>();
		PreparedStatement statement = null;
		
		if(openConnection()) {
			try {
				statement = conn.prepareStatement("SELECT ClientID, ClientName FROM Client");
				ResultSet results = statement.executeQuery();
				while (results.next()) {
					String value =  results.getInt("ClientID") + ", " + results.getString("ClientName");
					clients.add(">" + value);
				}
			} catch (SQLException sqle) {
				// TODO Auto-generated catch block
				sqle.printStackTrace();
			}
		}
		closeConnection();
		
		return clients;
	}
	
	/***
	 * Function to add account for accessing system
	 * @param name
	 * @param password
	 * @param type
	 * @return
	 */
	public boolean addAccount(String name, String password, String type) {
		PreparedStatement statement = null;
		
		if (openConnection()) {
			try {
				statement = conn.prepareStatement("INSERT INTO Account (AccountName, passwordHash, AccountType) VALUES (?,?,?)");
				statement.setString(1, name);
				statement.setString(2, password);
				statement.setString(3, type);
				int i = statement.executeUpdate();
				if (i == 1) {
					conn.commit();
				}
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				closeConnection();
				return false;
			}
		}
		closeConnection();
		return true;
	}
	
	/***
	 * Function to get accountID to add client, based on name
	 * @param name - client name supplied
	 * @return accountID 
	 */
	private int getAccountID(String name) {
		PreparedStatement statement = null;
		int accountID = -1;
		
		if (openConnection()) {
			try {
				statement = conn.prepareStatement("SELECT AccountID FROM Account WHERE AccountName = ?");
				statement.setString(1, name);
				ResultSet resSet = statement.executeQuery();
				if (resSet.next()) {
					accountID = resSet.getInt("AccountID");
				}
				
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		closeConnection();
		return accountID;
	}
	
	/***
	 * Function to get the usageID of a usage record
	 * @param eqID
	 * @param hID
	 * @return
	 */
	private int getUsageID(int eqID, int hID) {
		PreparedStatement statement = null;
		int ID = -1;
		
		if (openConnection()) {
			try {
				statement = conn.prepareStatement("SELECT UsageID, OutDateTime FROM equipmentusage AS eu "
						+ "INNER JOIN equipmenthire AS eh ON eu.equipmentID = eh.equipmentID "
						+ "WHERE eu.ReturnDateTime IS NULL AND eu.equipmentID = ? AND eh.hireID = ?");
				statement.setInt(1, eqID);
				statement.setInt(2, hID);
				ResultSet resSet = statement.executeQuery();
				if (resSet.next()) {
					ID = resSet.getInt("UsageID");
				}
				
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		closeConnection();
		return ID;
	}
	
	/***
	 * Method to get types of equipment
	 * @return types
	 */
	public String getEquipmentTypes() {
		PreparedStatement statement = null;
		String types = "";
		
		if (openConnection()) {
			
			try {

				statement = conn.prepareStatement("SELECT COLUMN_TYPE AS type FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'hons_hire' AND TABLE_NAME = 'equipment' AND COLUMN_NAME = 'EquipmentType'");
				//String query = "SELECT * FROM equipment WHERE equipmentModel LIKE '%" + searchTerm +"%' OR equipmentType LIKE '%" + searchTerm + "%' AND OnHire IS TRUE AND EquipmentID NOT IN ?;";
				ResultSet results = statement.executeQuery();
				while (results.next())
				{
					types = results.getString("type");
				}

			} catch(SQLException sqle) {
				sqle.printStackTrace();
			}
			
		}
		
		closeConnection();
		
		return types;
	}
	
}
