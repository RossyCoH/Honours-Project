package userApplication;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;

public class BusinessApplication {

	UserApplicationShared app;
	
	int clientID;
	int hireID;
	int equipmentID;
	int opID;
	boolean windowDisplay;
	ArrayList<Integer> selectedEquipmentIDs;
	String uInfo; // String to display for current user
	
	/*
	 * UI Windows
	 */
	EmployeeWindow eW;
	HireDisplayWindow hDW;
	EquipmentUsageWindow eqUW;
	HireAddWindow hAW;
	ClientAddWindow cAW;
	
	/***
	 * 
	 * @param appHost
	 * @param info
	 */
	public BusinessApplication(UserApplicationShared appHost, String info) {
		this.app = appHost;
		windowDisplay = false;
		uInfo = info;
		selectedEquipmentIDs = new ArrayList<Integer>();
		eW = new EmployeeWindow(this, uInfo);
	}
	
	/***
	 * Function to reset variable to allow new window to be opened and update hire table
	 * @param window - number for window that user is coming from; 1- equipmentOutReturn, 2-EquipmentDisplay
	 */
	public void resetWindowDisplay(int window) {
		windowDisplay = false;
		eqUW = null;
		hDW.updateTable();
	}
	
	/***
	 * Function to clear user and return to login window
	 * @param view (boolean) - if client (false) or employee (true) view
	 */
	public void Logout() {
		app.Logout(true);
	}
	
	/***
	 * Function to launch hire display window and close
	 * @param view (boolean) - if client (false) or employee (true) view
	 */
	public void displayHireRecordWindow(boolean view) {
		hDW = new HireDisplayWindow(this, view);
		eW = null;
	}
	
	/***
	 * Function to get current hire ID
	 * @return hireID
	 */
	public int getHire() {
		return hireID;
	}
	
	/***
	 * Function is display equipment usage window
	 * @param equipID
	 */
	public void displayUsageWindow(int equipID) {
		if (!windowDisplay) {
			equipmentID = equipID;
			eqUW = new EquipmentUsageWindow(this, equipmentID);
			windowDisplay = true;
		}
		
	}
	
	/***
	 * Function to display hire add window
	 */
	public void displayAddHireWindow() {
		hAW = new HireAddWindow(this);
		
	}
	
	/***
	 * 
	 */
	public void displayAddClientWindow() {
		cAW = new ClientAddWindow(this);
	}
	
	public void updateEmpHires() {
		eW.updateTable();
	}
	
	/***
	 * Function to return to employee window and remove hire display window
	 */
	public void returnEmployeeWindow() {
		selectedEquipmentIDs.clear();
		windowDisplay = false; // Reset window to view
		eW = new EmployeeWindow(this, uInfo);
		hAW = null;
	}
	
	/***
	 * Function to get all hire records and split into individual data 
	 * @return sRecords - ArrayList of split strings containing a hire records data
	 */
	public ArrayList<String[]> hireRecords() {
		
		ArrayList<String[]> sRecords = new ArrayList<String[]>(); // Store all records
		String[] response = app.messageServer("allhires").split(">"); // Get hires as message and split into components

		// Loop through each record and split into fields (start at 1 for to miss idenfier)
		for (int i = 1; i < response.length; i++) {
			// add array of field to list
			 sRecords.add(response[i].split("#"));
		 }
		
		return sRecords;
	}
	
	/***
	 * Function to set current hireID 
	 * @param hID - inthireID
	 */
	public void setHire(int hID) {
		hireID = hID;
	}
	
	/***
	 * Function to set current equipment item
	 * @param eqID
	 */
	public void setCurrentEquipmentID(int eqID) {
		equipmentID = eqID;
	}
	
	/***
	 * Function to get equipment for a select hire and split into individual data 
	 * @return sRecords - ArrayList of split strings containing equipment data
	 */
	public ArrayList<String[]> hireEquipmentList() {
		ArrayList<String[]> sRecords = new ArrayList<String[]>();
		
		String[] records = app.messageServer("hireequip>" + hireID).split(">");
		for (int i = 1; i < records.length; i++) {
			sRecords.add(records[i].split("#"));
		}
		return sRecords;
	}
	
	/***
	 * Function to get equipment for a select search term and split into individual data
	 * @param searchTerm - (String) term to used to filter data
	 * @return sRecords - ArrayList of split strings containing equipment data
	 */
	public ArrayList<String[]> equipmentSearch(String searchTerm) {
		ArrayList<String[]> sRecords = new ArrayList<String[]>();
		
		if (selectedEquipmentIDs.size() == 0) {
			selectedEquipmentIDs.add(0);
		}
		
		String[] records = app.messageServer("equipsearch>" + searchTerm + ">").split(">");
		String[] currRecord;
		Integer currID;
		for (int j = 1; j < records.length; j++) {
			
			currRecord = records[j].split("#");
			currID = Integer.parseInt(currRecord[0]);
			if (!selectedEquipmentIDs.contains(currID)) {
				sRecords.add(records[j].split("#"));
			}
		}
		
		return sRecords;
	}
	
	public ArrayList<String[]> equipmentFilter(String type) {
		ArrayList<String[]> sRecords = new ArrayList<String[]>();
		
		if (selectedEquipmentIDs.size() == 0) {
			selectedEquipmentIDs.add(0);
		}
		
		String[] records = app.messageServer("equipfilter>" + type + ">").split(">");
		String[] currRecord;
		Integer currID;
		for (int j = 1; j < records.length; j++) {
			
			currRecord = records[j].split("#");
			currID = Integer.parseInt(currRecord[0]);
			if (!selectedEquipmentIDs.contains(currID)) {
				sRecords.add(records[j].split("#"));
			}
		}
		
		return sRecords;
	}
	
	/***
	 * Function to get list of selected equipment to be added to a hire
	 * @return sRecords - ArrayList of split strings containing equipment data
	 */
	public ArrayList<String> equipmentSelectedList() {
		ArrayList<String> sRecords = new ArrayList<String>();
		String eqiupIDs = "";
		for (int s : selectedEquipmentIDs) {
			eqiupIDs += ">" + s; 
		}
		
		String[] records = app.messageServer("equipselected>" + eqiupIDs).split(">");
		for (int i = 0; i < records.length; i++) {
			sRecords.add(records[i]);
		}
		
		return sRecords;
	}
	
	/***
	 * Function to add equipment ID to list, after user selects from table
	 * @param equipID - Object from table of equipment id
	 * @return boolean - if successful (e.g. no error in parsing Id from object to int
	 */
	public boolean addEquipmentSelected(Object equipID) {
		if (selectedEquipmentIDs.size() == 1 && selectedEquipmentIDs.get(0) == 0) {
			selectedEquipmentIDs.clear();
		}
		
		try {
			int eqID = Integer.parseInt(equipID.toString());
			selectedEquipmentIDs.add(eqID);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/***
	 * Method to remove an item from the selected list
	 * @param eqID - Id num of equiment item selected
	 * @return if successfully removed (true) or not (false)
	 */
	public boolean removeEquipmentSelected(String eqID) {
		int eqInt = Integer.parseInt(eqID); // Convert to int
		
		// Check its in the list
		if (selectedEquipmentIDs.contains(eqInt)) {
			int index = selectedEquipmentIDs.indexOf(eqInt);
			selectedEquipmentIDs.remove(index);
			return true;
		}
		return false;
	}
	
	public String[] getClients() {
		String[] clients = null;
		
		
		clients = app.messageServer("getclients").split(">");
		String[] outCli = new String[clients.length - 1];
		
		for (int i = 0; i < clients.length - 1; i++) {
			outCli[i] =  clients[i + 1];
		}
		return outCli;
	}
	
	public boolean endHire() {
		String res = app.messageServer("endhire>" + hireID);
		if (res.equals("true")) {
			return true;
		}
		else {
			return false;
		}
		//return hireApp.endHire(hireID);
	}
	
	public boolean getHireCompleteState() {
		String res = app.messageServer("hireCompState>" + hireID);
		if (res.equals("true")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/***
	 * Function to add a new client 
	 * @param clientID
	 * @param clientName
	 * @param clientAddress
	 * @param contactName
	 * @param contactAddress
	 * @param password
	 * @return
	 */
	public boolean addClient(String clientName, String clientAddress, String contactName, String contactAddress, char[] password) {
		String passwordHash = app.hashPassword(password);
		String res = app.messageServer("addclient>" + clientName + ">" + clientAddress + ">" + contactName 
				+ ">" + contactAddress + ">" + passwordHash);
		if (res.equals("true")) {
			return true;
		}
		else {
			return false;
		}
		//return hireApp.addClient(Integer.parseInt(clientID), clientName, clientAddress, contactName, contactAddress, passwordHash);
	}
	
	/***
	 * Function to add a new hire (both to hire table) and all equipment to be used
	 * @param startDateS (String) - date as string for hire to start
	 * @param endDateS (String) - date as string for hire to end
	 * @param clientID (String) - ID number of client hire is assigned to
	 * @return (boolean) - if hire records are successfully add
	 * @throws ParseException
	 */
	public boolean addHire(String startDateS, String endDateS, String clientID) throws ParseException {
		
		Date startDate = Date.valueOf(startDateS);
		Date endDate = Date.valueOf(endDateS);
		
		String result = app.messageServer("addhire>" + startDate + ">" + endDate + ">" + clientID + ">" + IDsToString() );
		if (result.equals("true")) {
			selectedEquipmentIDs.clear();
			return true;
		}
		else {
			return false;
		}
		
	}
	
	/***
	 * Function to get all records of usage of a equipment item by a certain hire
	 * @return sRecords - ArrayList of split strings containing equipment usage data
	 */
	public ArrayList<String[]> equipmentUsage() {
		ArrayList<String[]> sRecords = new ArrayList<String[]>();
		
		//String[] records = hireApp.getEquipmentUsage(equipmentID);
		String[] records = app.messageServer("equipuseage>" + hireID + ">" + equipmentID).split(">");
		// Loop starting at 1 to account for first '>' before first entry,
		for (int i = 1; i < records.length; i++) {
			sRecords.add(records[i].split("#"));
			
			//String[] test = sRecords.get(i);
			//System.out.println(test[5]);
		}
		
		return sRecords;
	}
	
	public String[] getEquipmentTypes() {
		String[] records = app.messageServer("equiptypes>").split(",");
		
		int i = 0;
		for (String r : records) {

			r = r.replaceAll("enum", "");
			records[i] = r.replaceAll("['()]", "");
			
			i++;
		}
		return records;
	}
	
	/***
	 * Function to convert arrayList of equipment IDs to integer array
	 * @return (int[]) - array of equipment IDs as integers
	 */
	private String IDsToString() {
		String eqiupIDs = "";
		for (int s : selectedEquipmentIDs) {
			eqiupIDs += ">" + s; 
		}
		return eqiupIDs;
	}
}
