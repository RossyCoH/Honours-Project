package hireService;

public class Equipment {
	
	private int eqID;
	private String equipmentModel;
	private String equipmentType;
	private boolean status;
	
	public Equipment(int eID, String model, String type, boolean status) {
		this.eqID = eID;
		this.equipmentModel = model;
		this.equipmentType = type;
		this.status = status;
	}
	
	public Equipment(int eID, String model, String type) {
		this.eqID = eID;
		this.equipmentModel = model;
		this.equipmentType = type;
	}
	
	public int getID() {
		return this.eqID;
	}
	public String getModel() {
		return this.equipmentModel;
	}
	public String getType() {
		return this.equipmentType;
	}
	
	public boolean getStatus() {
		return this.status;
	}
	
	public String toString() {
		return "ID: " + eqID + "Model: " + equipmentModel;
	}
	
}
