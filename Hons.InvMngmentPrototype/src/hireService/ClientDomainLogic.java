package hireService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/***
 * Class to handle client domain operatons on the server
 * @author Ross Hunter
 * @since 
 */
public class ClientDomainLogic {
	
	private DataInterface dataIntf;
	
	public ClientDomainLogic(DataInterface dataAccess) {
		dataIntf = dataAccess;
	}
	
	/***
	 * Method to recive all hire records for a client
	 * @param cID
	 * @return 
	 */
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
	
	/***
	 * Method to complete a equipment return and reset availability
	 * @param note - records provided by user
	 * @param eqID - equipment id num
	 * @param hID - hire id num
	 * @param opID - operator id num
	 * @return if successful or not
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
	 * Method to process the addition of a new operator
	 * @param name - supplied name of the operaor
	 * @param cID - id number of the client
	 * @return boolean as String for message passing
	 */
	public String addOperator(String name, String cID) {
		boolean result = dataIntf.addOperator(name, parseInt(cID));
		if (result) {
			return "true";
		}
		else {
			return "false";
		}
	}
	
	/***
	 * Method to recivde list of operators for a client and append to single string message passing
	 * @param clientID - id number of the client
	 * @return out - String with all operators 
	 */
	public String getOperators(int clientID) {
		String out = "";
		
		for (String s : dataIntf.getOperators(clientID)) {
			out += s;
		}
		
		return out;
	}
	
	/***
	 * Method to parse a string to int value for utilisation
	 * @param string - integer as string
	 * @return i - integer 
	 */
	private int parseInt(String string) {
		int i = Integer.parseInt(string);
		return i;
	}
}
