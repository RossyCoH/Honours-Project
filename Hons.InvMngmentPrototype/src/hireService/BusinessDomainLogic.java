package hireService;

import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BusinessDomainLogic {

	private DataInterface dataIntf;
	
	public BusinessDomainLogic(DataInterface dataAccess) {
		dataIntf = dataAccess;
	}
	
	/***
	 * Method to receive and transform list of all hire records for sending
	 * @return recordsString- string of equipment records, concatentated by '>' between records and '#' between fields
	 */
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
	
	/***
	 * Method to handle search term input and transform equipment records for output
	 * @param searchTerm - search term provided by user
	 * @return recordsString- string of equipment records, concatentated by '>' between records and '#' between fields
	 */
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
	
	/***
	 * Method to handle filter term input and transform equipment records for output
	 * @param type - equipment type selected
	 * @return recordsString- string of equipment records, concatentated by '>' between records and '#' between fields
	 */
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
	
	/***
	 * Method to recive if hire is complete
	 * @param hireID - hire id num
	 * @return boolean 
	 */
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
	 * Method to recive and tranform list of all clients for sending as message
	 * @return out - string containing all clients
	 */
	public String getClients() {
		String out = "";
		
		for (String s : dataIntf.getClients()) {
			out += s;
		}
		
		return out;
	}
	
	/***
	 * Method to recive the equipment types
	 * @return
	 */
	public String getEquipmentTypes() {
		return dataIntf.getEquipmentTypes();
	}
}
