package hireService;

import java.sql.Date;

/***
 * Class for hire record details
 * @author Ross Hunter
 * @dateCreated 26/01/2023
 * @dateLastModifed 02/02/2023
 */
public class Hire {

	Integer hireID;
	Date startDate;
	Date endDate;
	boolean complete;
	Integer clientID;
	String clientName;
	
	public Hire(Integer hID, Date sD, Date eD, boolean comp, Integer cID, String clientName) {
		this.hireID = hID;
		this.startDate = sD;
		this.endDate = eD;
		this.complete = comp;
		this.clientID = cID;
		this.clientName = clientName;
	}
	
	public Hire(Integer hID, Date sD, Date eD, boolean comp, Integer cID) {
		this.hireID = hID;
		this.startDate = sD;
		this.endDate = eD;
		this.complete = comp;
		this.clientID = cID;
	}
	
	/***
	 * Constructor, for displaying to clients (where client id is not necessary)
	 * @param hID
	 * @param sD
	 * @param eD
	 * @param comp
	 */
	public Hire(Integer hID, Date sD, Date eD, boolean comp) {
		this.hireID = hID;
		this.startDate = sD;
		this.endDate = eD;
		this.complete = comp;
	}
	
	public Integer getHireID() {
		return hireID;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public boolean getComplete() {
		return complete;
	}
	
	public Integer getClientID() {
		return clientID;
	}
	
	public String getClientName() {
		return clientName;
	}
	
	public String toString() {
		return hireID + " , " + startDate + " , " + endDate + " , " + clientID;
	}
	
}
