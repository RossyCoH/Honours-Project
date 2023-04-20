package testing;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import hireService.DataAccess;
import hireService.EqUsage;
import hireService.Equipment;
import hireService.Hire;

/***
 * Class to test Data Access functionality
 * @author Ross Hunter
 * @since 15/03/2023
 *
 */
class DataAccessTest {

	DataAccess dA = new DataAccess();
	
	/***
	 * Test to ensure getHire returns correct hire record, by checking clientID
	 */
	@Test
	void testGetHire() {
		Hire hire = dA.getHire(132);
		assertEquals(1234, hire.getClientID());
	}
	
	/***
	 * Test to ensure getHire returns nothing, with of incorrect clientID
	 */
	@Test
	void testGetHireInvalid() {
		Hire hire = dA.getHire(0);
		assertEquals(null, hire);
	}
	
	/***
	 * Test that equipment search, with term used in item names, returns correct number of items
	 */
	@Test
	void testEquipmentSearch() {
		ArrayList<Equipment> eqList = dA.getEquipmentBySearch("Motorola");
		assertEquals(8, eqList.size());
	}
	
	/***
	 * Test that equipment search, with term NOT used in item names, returns 0 items
	 */
	@Test
	void testEquipmentSearchNotUsed() {
		ArrayList<Equipment> eqList = dA.getEquipmentBySearch("Good");
		assertEquals(0, eqList.size());
	}
	
	/***
	 * Test that function returns all available equipment items
	 */
	@Test
	void testEquipmentAvailable() {
		ArrayList<Equipment> eqList = dA.getEquipmentAvailable();
		assertEquals(13, eqList.size());
	}

	/***
	 * Test to check filtering the equipment by a type returns expected available items
	 */
	@Test
	void testFilterEquipmentValid() {
		ArrayList<Equipment> eqList = dA.filterEquipment("Headset");
		assertEquals(3, eqList.size());
	}
	
	/***
	 * Test to check filtering using invalid type return 0 results
	 */
	@Test
	void testFilterEquipmentInvalid() {
		ArrayList<Equipment> eqList = dA.filterEquipment("Something");
		assertEquals(0, eqList.size());
	}
	
	/***
	 * Test to check correct number of equipment items are returned for a valid hireID
	 */
	@Test
	void testEquipmentbyHire() {
		ArrayList<Equipment> eqList = dA.getEquipmentByHire(142);
		assertEquals(5, eqList.size());
	}
	
	/***
	 * Test to check 0 equipment items are returned for a invalid hireID
	 */
	@Test
	void testEquipmentbyHireInvalid() {
		ArrayList<Equipment> eqList = dA.getEquipmentByHire(745);
		assertEquals(0, eqList.size());
	}
	
	/**
	 * Test to check correct number of hires given for the client
	 */
	@Test
	void testClientHireRecords() {
		ArrayList<Hire> hireList = dA.clientHireRecords(1234);
		assertEquals(1, hireList.size());
	}
	
	/**
	 * Test to check 0 hire records are returned for a invalid clientID
	 */
	@Test
	void testClientHireRecordsInvalid() {
		ArrayList<Hire> hireList = dA.clientHireRecords(0353);
		assertEquals(0, hireList.size());
	}
	
	/**
	 * Test to check correct number of usage instances are returned for equipID
	 */
	@Test
	void testGetEquipmentUsageList() {
		ArrayList<EqUsage> list = dA.getEquipmentUsageList(132, 52627327);
		assertEquals(3, list.size());
	}
	
	/**
	 * Test to check 0 usage instances are returned for invalid equipID
	 */
	@Test
	void testGetEquipmentUsageListInvalidEquip() {
		ArrayList<EqUsage> list = dA.getEquipmentUsageList(146, 342);
		assertEquals(0, list.size());
	}
	
	/**
	 * Test to check 0 usage instances are returned for invalid hireID
	 */
	@Test
	void testGetEquipmentUsageListInvalidHire() {
		ArrayList<EqUsage> list = dA.getEquipmentUsageList(112, 52627327);
		assertEquals(0, list.size());
	}
	
	/**
	 * Test to check the availability of an equipment is correct
	 */
	@Test
	void testGetEquipmentStatus() {
		assertTrue(dA.getEquipmentStatus(23123456));
	}
	
	/**
	 * Test to check the availability dosn't show for a invalid equipmentID
	 */
	@Test
	void testGetEquipmentStatusInvalid() {
		assertFalse(dA.getEquipmentStatus(1342));
	}
	
	/**
	 * Testing to check updating availability of equipment item is successful
	 */
	@Test
	void testupdateEquipmentStatus() {
		assertTrue(dA.updateEquipmentStatus(23123457, false));
	}
	
	/**
	 * Testing to check updating availability of equipment using unused equipID is unsuccessful
	 */
	@Test
	void testupdateEquipmentStatusInvalid() {
		assertFalse(dA.updateEquipmentStatus(2312345, false));
	}
	
	/***
	 * Test to ensure usage record is successfully added (return true) with valid data
	 */
	@Test
	void testAddCheckoutRecordValid() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String dateTime = LocalDateTime.now().format(formatter);
		assertTrue(dA.addUsageRecord(dateTime, 36372647, 132, 11113));
	}
	
	/***
	 * Test to ensure usage record is not added (return false) with invalid opertorID
	 */
	/*
	 * @Test void testAddCheckoutRecordInvalid() { DateTimeFormatter formatter =
	 * DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); String dateTime =
	 * LocalDateTime.now().format(formatter);
	 * assertFalse(dA.addUsageRecord(dateTime, 536372647, 132, 12113)); }
	 */
	
	/***
	 * Test to ensure usage record is not added (return false) with invalid opertorID
	 */
	@Test
	void testAddOperatorValid() {
		assertTrue(dA.addOperator("Gregor Townsend", 101015));
	}
	
	/***
	 * Test to ensure add operator returns false when name is not supplied (null) 
	 */
	@Test
	void testAddOperatorInvalidName() {
		assertFalse(dA.addOperator(null, 101015));
	}
	
	/***
	 * Test to ensure add operator returns false when invalid client id is supplied
	 */
	@Test
	void testAddOperatorInvalidClient() {
		assertFalse(dA.addOperator("Gregor Townsend", 1212));
	}
	
	/**
	 * Test to verify using valid client ID ('1112') returns correct number of operators (2)
	 */
	@Test
	void testGetOperatorValid() {
		ArrayList<String> sL = dA.getOperators(101012);
		assertEquals(2, sL.size());
	}
	
	/**
	 * Test to verify using invalid client ID returns 0
	 */
	@Test
	void testGetOperatorInvalid() {
		ArrayList<String> sL = dA.getOperators(1);
		assertEquals(0, sL.size());
	}
	
	/***
	 * Test to ensure update usage record with valid data is successfull
	 */
	@Test
	void testUpdateUsageRecord() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String dateTime = LocalDateTime.now().format(formatter);
		assertTrue(dA.updateUsageRecord(dateTime, "none", 36372647, 132));
	}
	
	/***
	 * Test to ensure usage record is not updated (return false) with invalid 
	 */
	@Test
	void testUpdateUsageRecordInvalid() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String dateTime = LocalDateTime.now().format(formatter);
		assertFalse(dA.updateUsageRecord(dateTime, "none", 536372647, 132));
	}
	
	/***
	 * Test that query to get correct equipment items gets all equipment form IDs (exp. 3)
	 */
	@Test
	void testGetSelectedEquipment() {
		int[] eqIDs = {36372647, 23123465, 23123469};
		ArrayList<Equipment> eq = dA.getSelectedEquipment(eqIDs);
		assertEquals(3, eq.size());
	}
	
	/***
	 * Test that query to get correct equipment items only get valid (exp. 2)
	 */
	@Test
	void testGetSelectedEquipmentInvalidID() {
		int[] eqIDs = {36372647, 1111111, 23123469};
		ArrayList<Equipment> eq = dA.getSelectedEquipment(eqIDs);
		assertEquals(2, eq.size());
	}
	
	/**
	 * Test to check hire record is successfully added
	 */
	@Test
	void testAddHire() {
		Date d1 = Date.valueOf("2023-04-03");
		Date d2 = Date.valueOf("2023-04-07");
		assertTrue(dA.addHire(d1, d2, 101011));
	}
	
	/**
	 * Test to check hire record with invalid clientID is not added
	 */
	@Test
	void testAddHireInvalid() {
		Date d1 = Date.valueOf("2023-04-03");
		Date d2 = Date.valueOf("2023-04-07");
		assertFalse(dA.addHire(d1, d2, 10101));
	}
	
	@Test
	void testSetEquipmentHire() {
		int[] eqIDs = {36372647, 23123465, 23123469};
		assertTrue(dA.setEquipmentHire(eqIDs));
	}
	
	@Test
	void testSetEquipmentInvalidID() {
		int[] eqIDs = {536372647, 1111111, 23123469};
		assertFalse(dA.setEquipmentHire(eqIDs));
	}
	
	@Test
	void testSetEquipmentInvalidIDOnly() {
		int[] eqIDs = {1111111};
		assertFalse(dA.setEquipmentHire(eqIDs));
	}

	@Test 
	void testCompleteHire() {
		assertTrue(dA.completeHire(152));
	}
	
	@Test 
	void testCompleteHireInvalid() {
		assertFalse(dA.completeHire(111));
	}
	
	@Test
	void setEquipmentAvilable() {
		int[] eqIDs = {36372647, 23123465, 23123469};
		assertTrue(dA.setEquipmentAvilable(eqIDs));
	}
	
	@Test
	void setEquipmentInvalid() {
		int[] eqIDs = {536372647, 1111111, 23123489};
		assertFalse(dA.setEquipmentAvilable(eqIDs));
	}
	
	/**
	 * 
	 */
	@Test
	void testAddClientValid() {
		assertTrue(dA.addClient("Test Client 1", "Test Address", "Test Contact", "Test Contact Address", "0b3829e4eb45e5ce087918d3fe8b8a646ee160799beaa614c7e07e5d9e011d0763ebd4c6b7853fdec9813dc135935a079488274e2ae19d84530a3c6114cf04ba"));
	}
	
	/**
	 * 
	 */
	/*
	 * @Test void testAddClientInValid() { assertFalse(dA.addClient("", "", "", "",
	 * "")); }
	 */
	
	/**
	 * Test to check valid employee login returns correct id (1)
	 */
	@Test
	void testLoginCheckValidClient() {
		String result = dA.loginCheck("Example", 
				"0b3829e4eb45e5ce087918d3fe8b8a646ee160799beaa614c7e07e5d9e011d0763ebd4c6b7853fdec9813dc135935a079488274e2ae19d84530a3c6114cf04ba").split(">")[0];
		assertEquals("1", result);
	}
	
	/**
	 * Test to check valid employee login returns correct id (2)
	 */
	@Test
	void testLoginCheckValidEmployee() {
		String result = dA.loginCheck("John Doe", 
				"4d92b815fbdb6f78fcb62c3ce05204db27845e59c2a2e62ee2477db8719036a39ead1eface7a6b4e0c0fce17ef120c8b92fe84c9a4d0096a47199a99ae78414b").split(">")[0];
		assertEquals("3", result);
	}
	
	/**
	 * Test to check login returns null with invlaid username
	 */
	@Test
	void testLoginCheckInvalidUserName() {
		String result = dA.loginCheck("SomethingWrong", 
				"0b3829e4eb45e5ce087918d3fe8b8a646ee160799beaa614c7e07e5d9e011d0763ebd4c6b7853fdec9813dc135935a079488274e2ae19d84530a3c6114cf04ba");
		assertEquals(null, result);
	}
	
	/**
	 * Test to check login returns null with invlaid password (hash)
	 */
	@Test
	void testLoginCheckInvalidPassword() {
		String result = dA.loginCheck("Example", 
				"4d92b815fbdb6f78fcb62c3ce05204db27845e59c2a2e62ea2477db8719036a39ead1eface7a6b4e0c0fce17ef127c8b92fe84c9a4d0096a47199a99ae78414f");
		assertEquals(null, result);
	}
	
	/***
	 * Test to verify that correct client id (1234) is returned from client account id (1)
	 */
	@Test
	void testGetClientIDValid() {
		int cID = dA.getClientID(1);
		assertEquals(1234, cID);
	}
	
	/***
	 * Test to verify that -1 is returned from invalid client account id (1111)
	 */
	@Test
	void testGetClientIDInvalid() {
		int cID = dA.getClientID(1111);
		assertEquals(-1, cID);
	}
	
	/**
	 * Test to verify the correct number of clients as are int the system is returned (8)
	 */
	@Test
	void testGetClients() {
		assertEquals(10, dA.getClients().size());
	}
	
	/***
	 * Test to ensure the list of all enums of equipmnet type are returned
	 */
	@Test
	void testGetEquipmentTypes() {
		assertEquals("enum('Two-Way Radio','Headset','Earpiece')", dA.getEquipmentTypes());
	}
}
