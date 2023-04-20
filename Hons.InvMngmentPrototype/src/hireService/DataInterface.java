package hireService;

import java.sql.Date;
import java.util.ArrayList;

/***
 * Interface for hire service database access
 * @author Ross Hunter
 * @dateCreated 27/01/2023
 * @dateLastModified 02/02/2023
 *
 */
public interface DataInterface {

	public ArrayList<Hire> getHireRecords();
	
	public Hire getHire(int hID);
	
	public ArrayList<Equipment> getEquipmentBySearch(String searchTerm);
	
	public ArrayList<Equipment> getEquipmentAvailable();
	
	public ArrayList<Equipment> filterEquipment(String type);
	
	public ArrayList<Equipment> getEquipmentByHire(int hID);
	
	public ArrayList<Hire> clientHireRecords(int clientID);
	
	public ArrayList<EqUsage> getEquipmentUsageList(int hireID, int equipID);
	
	public boolean getEquipmentStatus(int equipID);
	
	public boolean updateEquipmentStatus(int equipID, boolean status);
	
	public boolean addUsageRecord(String outDT, int eqID, int hID, int idNo);
	
	public boolean addOperator(String name, int cID);
	
	public ArrayList<String> getOperators(int clientID);
	
	public boolean updateUsageRecord(String inDT, String note, int eqID, int hID);
	
	public ArrayList<Equipment> getSelectedEquipment(int[] selectEquipIDs);
	
	public boolean addHire(Date startDate, Date endDate, int clientID);
	
	public boolean setEquipmentHire(int[] equipIDs);
	
	public boolean completeHire(int hireID);
	
	public boolean hireCompleteState(int hireID);
	
	public boolean setEquipmentAvilable(int[] equipIDs);
	
	public boolean addClient(String clientName, String clientAddress, String contactName, String contactAddress, String password);
	
	public String loginCheck(String name, String password);
	
	public int getClientID(int accountID);
	
	public ArrayList<String> getClients();
	
	public String getEquipmentTypes();
		
}
