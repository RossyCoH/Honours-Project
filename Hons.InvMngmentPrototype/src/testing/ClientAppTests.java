package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import userApplication.ClientApplication;
import userApplication.UserApplicationShared;

class ClientAppTests {

	UserApplicationShared uApp = new UserApplicationShared();
	//ClientApplication clientApp = new ClientApplication(uApp, "");
	
	@Test
	void testClientHireRecords() {
		uApp.loginProcess("Napier", "napierpass".toCharArray());
		//assertEquals(4, clientApp.clientHireRecords().size());
	}
	
	

}
