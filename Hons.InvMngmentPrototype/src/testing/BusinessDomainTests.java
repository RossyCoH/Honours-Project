package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;

import org.junit.jupiter.api.Test;

import hireService.DataInterface;
import hireService.HireApp;

class BusinessDomainTests {

	DataInterface dI = new DataAccess();
	HireApp hA = new HireApp(dI);
	
	@Test
	void testSearchEquipment() {
		String[] eqString = hA.searchEquipment("Motorola").split(">");
		assertEquals(9, eqString.length);
	}
	
	@Test
	void testSearchEquipmentNotUsed() {
		String[] eqString = hA.searchEquipment("Good").split(">");
		assertEquals(1, eqString.length);
	}
	
	@Test
	void testEquipmentFilter() {
		String[] eqString = hA.equipmentFilter("Headset").split(">");
		assertEquals(4, eqString.length);
	}
	
	/***
	 * Test to check filtering using invalid type return 0 results
	 */
	@Test
	void testFilterEquipmentInvalid() {
		String[] eqString = hA.equipmentFilter("Something").split(">");
		assertEquals(1, eqString.length);
	}
	
	@Test
	void testAddHire() {
		Date d1 = Date.valueOf("2023-04-04");
		Date d2 = Date.valueOf("2023-04-09");
		int[] eqIDs = {36372647, 23123466, 23123469};
		assertTrue(hA.addHire(d1, d2, 101011, eqIDs));
	}

	/**
	 * Test to check hire record with invalid clientID is not added
	 */
	@Test
	void testAddHireInvalid() {
		Date d1 = Date.valueOf("2023-04-03");
		Date d2 = Date.valueOf("2023-04-07");
		int[] eqIDs = {36372647, 23123466, 23123469};
		assertFalse(hA.addHire(d1, d2, 10101, eqIDs));
	}
	
	/***
	 * Test that query to get correct equipment items gets all equipment form IDs (exp. 3)
	 */
	@Test
	void testGetSelectedEquipment() {
		int[] eqIDs = {36372647, 23123465, 23123469};
		String[] eqString = hA.selectedEquipmentList(eqIDs).split(">");
		assertEquals(4, eqString.length);
	}
	
	/***
	 * Test that query to get correct equipment items gets all valid equipment from IDs (exp. 2)
	 */
	@Test
	void testGetSelectedEquipmentInvalid() {
		int[] eqIDs = {36372647, 23923465, 23123469};
		String[] eqString = hA.selectedEquipmentList(eqIDs).split(">");
		assertEquals(3, eqString.length);
	}
	
	@Test 
	void testCompleteHire() {
		assertTrue(hA.endHire(157));
	}
	
	@Test 
	void testCompleteHireInvalid() {
		assertFalse(hA.endHire(1111));
	}
	
	@Test
	void testHireCompStateTrue() {
		assertTrue(hA.hireCompState(149));
	}
	
	@Test
	void testHireCompStateFalse() {
		assertFalse(hA.hireCompState(132));
	}
	
	/**
	 * 
	 */
	@Test
	void testAddClientValid() {
		assertTrue(hA.addClient("Test Client 52", "Test Address", "Test Contact", "Test Contact Address", "0b3829e4eb45e5ce087918d3fe8b8a646ee160799beaa614c7e07e5d9e011d0763ebd4c6b7853fdec9813dc135935a079488274e2ae19d84530a3c6114cf04ba"));
	}
	
	@Test
	void testGetClients() {
		assertEquals(13, hA.getClients().split(">").length);
	}
	
	@Test
	void testGetEquipmentTypes() {
		assertEquals("enum('Two-Way Radio','Headset','Earpiece')", hA.getEquipmentTypes());
	}
	
	@Test
	void testGetHires() {
		assertEquals(26, hA.getHires().split(">").length);
	}
	
	/***
	 * Test to ensure getHire returns correct hire record, by checking clientID
	 */
	@Test
	void testGetHire() {
		String[] hire = hA.getHire(132).split("#");
		assertEquals("1234", hire[4]);
	}
	
	/***
	 * Test to ensure getHire returns nothing, with of incorrect clientID
	 */
	@Test
	void testGetHireInvalid() {
		String hire = hA.getHire(0);
		assertEquals(null, hire);
	}
	
	@Test
	void testGetEquipmentUsageList() {
		String[] list = hA.getEquipmentUsage(132, 52627327).split(">");
		assertEquals(4, list.length);
	} 
	
	/**
	 * Test to check 0 usage instances are returned for invalid equipID
	 */
	@Test
	void testGetEquipmentUsageListInvalidEquip() {
		String[] list = hA.getEquipmentUsage(146, 342).split(">");
		assertEquals(1, list.length);
	}
	
	/**
	 * Test to check 0 usage instances are returned for invalid hireID
	 */
	@Test
	void testGetEquipmentUsageListInvalidHire() {
		String[] list = hA.getEquipmentUsage(112, 52627327).split(">");
		assertEquals(1, list.length);
	}
	
	void testGetHireEquipmentList() {
		assertEquals(4, hA.getHireEquipmentList(132).split(">").length);
	}
	
	
	
}
