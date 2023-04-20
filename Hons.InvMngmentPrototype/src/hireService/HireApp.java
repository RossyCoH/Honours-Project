package hireService;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/***
 * Class to manage the hire service
 * @author Ross Hunter
 * @dateCreated 25/01/2023
 * @dateLastModified 20/02/2023
 *
 */
public class HireApp {
	
	private DataInterface dataIntf;
	
	public HireApp(DataInterface dataAccess) {
		
		dataIntf = dataAccess;
	}
	
	public String getHires() {
		ArrayList<Hire> hireList = new ArrayList<Hire>();
		hireList = dataIntf.getHireRecords();
		//String[] recordsList = new String[hireList.size()];
		String recordsString = "";
		
		//int i = 0;
		for (Hire r : hireList) {
			//recordsList[i] = r.getHireID() + "#" + r.getStartDate() + "#" + r.getEndDate() + "#" + r.getComplete() + "#" + r.getClientID() + ">";
			recordsString += ">" + r.getHireID() + "#" + r.getStartDate() + "#" + r.getEndDate() + "#" + r.getComplete() + "#" + r.getClientID() + ", " + r.getClientName();
			//i++;
		}
		return recordsString;
	}
	
	public String searchEquipment(String searchTerm) {
		ArrayList<Equipment> eqList = new ArrayList<Equipment>();
		
		if (searchTerm.equals(" ") || searchTerm.equals("") ) {
			eqList = dataIntf.getEquipmentAvailable();
		}
		else {
			eqList = dataIntf.getEquipmentBySearch(searchTerm);
		}

		//String[] recordsList = new String[eqList.size()];
		String recordsString = "";
		
		//int i = 0;
		for (Equipment e : eqList) {
			recordsString += ">" + e.getID() + "#" + e.getModel() + "#" + e.getType();
			//recordsList[i] = e.getID() + "#" + e.getModel() + "#" + e.getType() + "#" + e.getStatus();
			//i++;
		}
		
		//System.out.println(eqList);
		
		//return recordsList;
		return recordsString;
	}
	
	public String equipmentFilter(String type) {
		ArrayList<Equipment> eqList = new ArrayList<Equipment>();
		
		eqList = dataIntf.filterEquipment(type);
		String recordsString = "";
		
		//int i = 0;
		for (Equipment e : eqList) {
			recordsString += ">" + e.getID() + "#" + e.getModel() + "#" + e.getType();
			//recordsList[i] = e.getID() + "#" + e.getModel() + "#" + e.getType() + "#" + e.getStatus();
			//i++;
		}
		
		//System.out.println(eqList);
		
		//return recordsList;
		return recordsString;
	}
	
	public String getClientHireRecords(int cID) {
		ArrayList<Hire> hireList = new ArrayList<Hire>();
		hireList = dataIntf.clientHireRecords(cID);
		
		//String[] recordsList = new String[hireList.size()];
		String recordsString = "";
		
		//int i = 0;
		for (Hire r : hireList) {
			//recordsList[i] = r.getHireID() + "#" + r.getStartDate() + "#" + r.getEndDate() + "#" + r.getComplete();
			recordsString += ">" + r.getHireID() + "#" + r.getStartDate() + "#" + r.getEndDate() + "#" + r.getComplete();
			//i++;
			//System.out.println(recordsList[i]);
		}
		
		//return recordsList;
		return recordsString;
	}
	
	/***
	 * Method to get the details of a hire
	 * @param hID - hire ID num
	 * @return hireString - containing hire details
	 */
	public String getHire(int hID) {
		Hire hire = dataIntf.getHire(hID);
		if (hire == null) {
			return null;
		}
		else {
			String hireString = hire.getHireID() + "#" + hire.getStartDate() + "#" + hire.getEndDate() + "#" + hire.getComplete() + "#" + hire.getClientID();
			return hireString;
		}
	}
	
	/**
	 * Function to get list of equipment items for a hire and create string for sending to client
	 * @param hID (int) - ID num of hire
	 * @return String with records divided by '>' and data fields within usage records divided by '#'
	 */
	public String getHireEquipmentList(int hID) {
		ArrayList<Equipment> equipmentList = new ArrayList<Equipment>();
		equipmentList = dataIntf.getEquipmentByHire(hID);
		
		//String[] recordsList = new String[equipmentList.size()];
		String recordsString = "";
		
		//int i = 0;
		for (Equipment e : equipmentList) {
			recordsString += ">" + e.getID() + "#" + e.getModel() + "#" +e.getType() + "#" + e.getStatus();
			//i++;
		}
		//return recordsList;
		return recordsString;
		
	}
	
	/***
	 * Function to get usage record equipment item and create string for sending to client
	 * @param equipID (int) - ID num of equipment item
	 * @return String with records divided by '>' and data fields within usage records divided by '#'
	 */
	public String getEquipmentUsage(int hireID, int equipID) {
		ArrayList<EqUsage> usageList = new ArrayList<EqUsage>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		usageList = dataIntf.getEquipmentUsageList(hireID, equipID);
		
		//String[] recordsList = new String[usageList.size()];
		String recordString = "";
		//System.out.println("here!");

		
		//int i = 0;
		for (EqUsage u : usageList) {
			// Check for if returnDate is not set, therefore to not be formatted
			if (u.getReturnDate() == null) {
				recordString += ">" + u.getOutDate().format(formatter) + "#" + null + "#" + u.getNotes() + "#" + u.getOpID() + "#" + u.getOpName();
			} 
			else {
				recordString += ">" + u.getOutDate().format(formatter) + "#" + u.getReturnDate().format(formatter) + "#" + u.getNotes() + "#" + u.getOpID() + "#" + u.getOpName();
			}
			
			//i++;
			//System.out.println(recordsList[i]);
		}
		
		return recordString;
	}
	
	/***
	 * Method to return the response to if equipmnet is availbe for use (e.g to be checked out)
	 * @param equipID
	 * @return boolean
	 */
	public boolean equipmentAvailable(int equipID) {
		return dataIntf.getEquipmentStatus(equipID);
	}
	
	/***
	 * Function to call to add hire records
	 * @param startDate
	 * @param endDate
	 * @param clientID
	 * @return if datalayer function is true or not
	 */
	public boolean addHire(Date startDate, Date endDate, int clientID, int[] equipIDs) {
		if (dataIntf.addHire(startDate, endDate, clientID)) {
			return dataIntf.setEquipmentHire(equipIDs);
		}
		else {
			return false;
		}
	}
	
	/***
	 * Function to get selected equipment 
	 * @param selectedEquipIDs - list of equipment IDs selected by user
	 * @return String[] - list of equipment
	 */
	public String selectedEquipmentList(int[] selectedEquipIDs) {
		ArrayList<Equipment> equipList = new ArrayList<Equipment>();
		equipList = dataIntf.getSelectedEquipment(selectedEquipIDs);
		
		//String[] recordsList = new String[equipList.size()];
		String recordString = "";
		//System.out.println("here!");
		
		//int i = 0;
		for (Equipment e : equipList) {
			//recordsList[i] = e.getID() + ", " + e.getModel() + ", " + e.getType();
			recordString += ">" + e.getID() + ", " + e.getModel() + ", " + e.getType();
			//i++;
		}
		
		return recordString;
	}
	
	/***
	 * Function to end hire, setting all equipment back to available and complete field to true
	 * @param hireID
	 * @return
	 */
	public boolean endHire(int hireID) {
		
		ArrayList<Equipment> eqList = dataIntf.getEquipmentByHire(hireID);
		int[] eqArray = new int[eqList.size()];
		
		int i = 0;
		for (Equipment e : eqList) {
			eqArray[i] = e.getID();
			i++;
		}
		
		if (dataIntf.setEquipmentAvilable(eqArray)) {
			return dataIntf.completeHire(hireID);
		}
		else {
			return false;
		}

	}
	
	public boolean hireCompState(int hireID) {
		return dataIntf.hireCompleteState(hireID);
	}
	
	/***
	 * Function to add a new Client
	 * @param clientID
	 * @param clientName
	 * @param clientAddress
	 * @param contactName
	 * @param contactAddress
	 * @param password
	 * @return boolean - if query was successful
	 */
	public boolean addClient(String clientName, String clientAddress, String contactName, String contactAddress, String password) {
		return dataIntf.addClient(clientName, clientAddress, contactName, contactAddress, password);
	}
	
	/***
	 * Function to call to add checkout record, then call to update equipment available status
	 * @param opID - ID num of operator checking out device
	 * @param name - name of operator checking out device
	 * @param eqID - id num of equipment item
	 * @param hID - id num of hire equipment is assigned to
	 * @return - if function is completed correctly or not
	 */
	public boolean checkoutEquipment(int opID, int eqID, int hID) {
		//Set date format and get current data/time in such format as a String
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String outDT = LocalDateTime.now().format(formatter);
		
		if (dataIntf.addUsageRecord(outDT, eqID, hID, opID)) {
			dataIntf.updateEquipmentStatus(eqID, false);
			return true;
		}
		return false;
	}
	
	public String addOperator(String name, String cID) {
		boolean result = dataIntf.addOperator(name, parseInt(cID));
		if (result) {
			return "true";
		}
		else {
			return "false";
		}
	}
	
	public String getOperators(int clientID) {
		String out = "";
		
		for (String s : dataIntf.getOperators(clientID)) {
			out += s;
		}
		
		return out;
	}
	
	public String getClients() {
		String out = "";
		
		for (String s : dataIntf.getClients()) {
			out += s;
		}
		
		return out;
	}
	
	/***
	 * Function to complete a equipment return and reset availability
	 * @param note
	 * @param eqID
	 * @param hID
	 * @param opID
	 * @return
	 */
	public boolean returnEquipment(String note, int eqID, int hID) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String inDT = LocalDateTime.now().format(formatter);
		
		if (dataIntf.updateUsageRecord(inDT, note, eqID, hID)) { 
			
			if(dataIntf.updateEquipmentStatus(eqID, true)) {
				return true;
			}
			
			return false;
		}
		
	 	return false; 
	 }
	
	/***
	 * Function to start login process
	 * @param name
	 * @param passwordH
	 * @return String 
	 */
	public String loginProcess(String name, String passwordH) {
		return dataIntf.loginCheck(name, passwordH);
	}
	
	/***
	 * Method to recive the equipment types
	 * @return
	 */
	public String getEquipmentTypes() {
		return dataIntf.getEquipmentTypes();
	}
	
	/***
	 * Method to call data interface to get client id associted with account ID
	 * @param accountID
	 * @return int - client ID
	 */
	public int getClientID(int accountID) {
		return dataIntf.getClientID(accountID);
	}
	
	private static int parseInt(String string) {
		int i = Integer.parseInt(string);
		return i;
	}
}
