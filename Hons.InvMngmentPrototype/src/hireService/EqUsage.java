package hireService;

import java.time.LocalDateTime;

/***
 * Class for records of equipment usage
 * @author Ross Hunter
 *
 */
public class EqUsage {

	int usageID;
	LocalDateTime outDateTime;
	LocalDateTime returnDateTime;
	String notes;
	int opID;
	String opName;
	
	public EqUsage(int usageID, LocalDateTime outDate, LocalDateTime returnDate, String notes, int opID, String opName) {
		this.usageID = usageID;
		this.outDateTime = outDate;
		this.returnDateTime = returnDate;
		this.notes = notes;
		this.opID = opID;
		this.opName = opName;
	}
	
	public int getUsageID() {
		return usageID;
	}
	
	public LocalDateTime getOutDate() {
		return outDateTime;
	}
	
	public LocalDateTime getReturnDate() {
		return returnDateTime;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public int getOpID() {
		return opID;
	}
	
	public String getOpName() {
		return opName;
	}
}
