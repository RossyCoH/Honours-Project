package userApplication;

import java.util.ArrayList;

/***
 * Class for client domain within user application
 * @author Ross Hunter
 * @since 
 */
public class ClientApplication {

	UserApplicationShared app;
	
	int clientID;
	int hireID;
	int equipmentID;
	int opID;
	boolean windowDisplay;
	String uInfo; // String to display for current user
	
	/*
	 * UI Windows
	 */
	
	ClientWindow cW;
	HireDisplayWindow hDW;
	EquipmentOutReturnWindow eqORW;
	EquipmentUsageWindow eqUW;
	OperatorWindow oW;
	
	
	public ClientApplication(UserApplicationShared appHost, int clientID, String info) {
		this.app = appHost;
		windowDisplay = false;
		uInfo = info;
		this.clientID = clientID;
		cW = new ClientWindow(this, uInfo);
	}
	
	/***
	 * Function to reset variable to allow new window to be opened and update hire table
	 * @param window - number for window that user is coming from; 1- equipmentOutReturn, 2-EquipmentDisplay
	 */
	public void resetWindowDisplay(int window) {
		windowDisplay = false;
		switch(window) {
		case 1:
			eqORW = null;
		case 2:
			eqUW = null;
		}
		hDW.updateTable();
	}
	
	/***
	 * Function to clear user and return to login window
	 * @param view (boolean) - if client (false) or employee (true) view
	 */
	public void Logout() {
		app.Logout(false);
	}
	
	/***
	 * Function to launch hire display window and close
	 * @param view (boolean) - if client (false) or employee (true) view
	 */
	public void displayHireRecordWindow(boolean view) {
		hDW = new HireDisplayWindow(this, view);
		cW = null;
	}
	
	/***
	 * Function to get current hire ID
	 * @return hireID
	 */
	public int getHire() {
		return hireID;
	}
	
	/***
	 * Function to display checkout window 
	 * @param equipID
	 * @param state
	 */
	public void displayCheckoutWindow(int equipID, String state) {
		if (!windowDisplay) {
			equipmentID = equipID;
			eqORW = new EquipmentOutReturnWindow(this, state);
			windowDisplay = true;
		}
		
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
	
	public void diplayOperatorWindow() {
		oW = new OperatorWindow(this);
	}
	
	public void closeOperatorWindow() {
		oW = null;
	}
	
	/***
	 * Function to return to client window and remove hire display window
	 */
	public void returnClientWindow() {
		windowDisplay = false; // Reset window to view
		cW = new ClientWindow(this, uInfo);
		hDW = null;
	}
	
	/***
	 * Function to get hire records for the current client using the system and split into individual data 
	 * @return sRecords - ArrayList of split strings containing hire records data
	 */
	public ArrayList<String[]> clientHireRecords() {
		ArrayList<String[]> sRecords = new ArrayList<String[]>();
		
		String[] response = app.messageServer("clienthires>" + clientID).split(">");
		
		for (int i = 1; i < response.length; i++) {
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
	 * Function to set current operator
	 * @param opID
	 */
	public void setOpID(int opID) {
		this.opID = opID;
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
		
		//String[] records = hireApp.getHireEquipmentList(hireID);
		String[] records = app.messageServer("hireequip>" + hireID).split(">");
		for (int i = 1; i < records.length; i++) {
			sRecords.add(records[i].split("#"));
			
			//String[] test = sRecords.get(i);
			//System.out.println(test[5]);
		}
		return sRecords;
	}
	
	/***
	 * Method to get if a equipment item is available 
	 * @param equipID - Id num of equipmnet item
	 * @return if available (true) or not (false)
	 */
	public boolean equipmentAvailable(int equipID) {
		String res = app.messageServer("equipavail>" + equipID);
		if (res.equals("true")) {
			return true;
		}
		else {
			return false;
		}
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
	
	public boolean checkoutEquipment(String opID) {
		//boolean res = hireApp.checkoutEquipment(Integer.parseInt(opID), name, equipmentID, hireID);
		String res = app.messageServer("checkoutequip>" + opID + ">" + equipmentID + ">"+ hireID);
		if (res.equals("true")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean addOperator(String name) {
		String resultS = app.messageServer("addOp>" + name + ">" + clientID);
		if (resultS.equals("true")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String[] getOperators() {
		String[] ops = null;
		
		ops = app.messageServer("getops>" + clientID).split(">");
		String[] outOps = new String[ops.length - 1];
		
		for (int i = 0; i < ops.length - 1; i++) {
			outOps[i] =  ops[i + 1];
		}
		return outOps;
	}
	
	public boolean returnEquipment(String note) {
		//boolean res = hireApp.returnEquipment(note, equipmentID, hireID);
		String res = app.messageServer("returnequip>" + note + ">" + equipmentID + ">" + hireID);
		if (res.equals("true")) {
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
}
