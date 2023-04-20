package userApplication;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/***
 * Class to handle shared logic for user applcation
 * @author Ross Hunter
 * @since 28/01/2023
 * 
 */
public class UserApplicationShared {

	int userID;
	
	BusinessApplication businessApp;
	ClientApplication clientApp;
	
	static Socket socket;
	//static DataInputStream dataInS;
	static BufferedReader dataInS;
	//static DataOutputStream dataOutS;
	static PrintWriter dataOutS;
	static BufferedReader buffRead;
	
	/*
	 * UI Windows
	 */
	LoginWindow lW;
	
	public UserApplicationShared() {
		
		userID = -1;
		lW = new LoginWindow(this);


		 
	}
	
	/***
	 * Function to pass message to the server
	 * @param msgOut - String to be sent to the server
	 * @return response - returned string from the server
	 */
	public String messageServer(String msgOut) {
		
		String response = null;
		
		// Open Connection
		try {
			socket = new Socket("localhost",5555);
			dataOutS = new PrintWriter(socket.getOutputStream());
			dataInS = new BufferedReader(new InputStreamReader(socket.getInputStream()));    
			buffRead = new BufferedReader(new InputStreamReader(System.in));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Connection made: " + socket.isConnected());
		
		// Send 
		try {
			//str=buffRead.readLine();  
			dataOutS.println(msgOut); //Send message
			System.out.println("Message Sent");
			dataOutS.flush(); // Clear buffer  
			response = dataInS.readLine(); //Read response 
			System.out.println("Server says: "+ response);  

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Close connections
		try {
			dataOutS.close();  
			dataInS.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	/***
	 * Function to end the client-application
	 */
	public void endApp() {
		lW = null;
		return;
	}

	
	/***
	 * Launch client applcation and close login window
	 */
	public void ClientApp(int clientID, String info) {
		clientApp = new ClientApplication(this, clientID, info);
		lW = null;
	}
	
	/***
	 * Launch business applcation and close login window
	 */
	public void EmployeeApp(String info) {
		businessApp = new BusinessApplication(this, info);
		lW = null;
	}
	
	/***
	 * Function to clear user and return to login window
	 * @param view (boolean) - if client (false) or employee (true) view
	 */
	public void Logout(boolean view) {
		userID = -1;
		lW = new LoginWindow(this);
		if (view) {
			businessApp = null;
		}
		else {
			clientApp = null;
		}
	}
	
	/***
	 * Function to handle login process and verify details are valid user
	 * @param name (String) - username entered 
	 * @param password (String) - password entered 
	 * @return (boolean) - if login is successful
	 */
	public boolean loginProcess(String name, char[] password) {
		String[] resSplit;
		String uInfo;
		
		String hash = hashPassword(password);
		//String result = hireApp.loginProcess(name, hash);
		String result = messageServer("log>" + name + ">" + hash);
		
		//System.out.println(hashPassword("napierpass".toCharArray()));
		
		if (result != null) {
			resSplit = result.split(">");
			
			//System.out.println(resSplit[1]);
			
			if (resSplit.length == 4) {
				int accountID = Integer.parseInt(resSplit[1]);
				if (resSplit[2].equals("Client")) {
					int clientID = Integer.parseInt(messageServer("clientid>" + accountID));
					if (clientID == -1) {
						return false;
					} else {
						this.userID = clientID;
						uInfo = clientID + ", " + resSplit[3];
						ClientApp(clientID, uInfo);
						return true;
					}
				} else if (resSplit[2].equals("Employee")) {
					uInfo = accountID + ", " + resSplit[3];
					EmployeeApp(uInfo);
					return true;
				} else {
					return false;
				} 
			}
			else {
				return false;
			}
		}
		return false;
		
	}
	
	/***
	 * Function to hash and salt the password provided before being sent to the server
	 * @param password (char[]) - password provided converted into char array
	 * @return (String) - hash generated from password
	 */
	public String hashPassword(char[] password) {
		
		String salt = "1234";
        int iterations = 10000;
        int keyLength = 512;
        byte[] saltBytes = salt.getBytes();
		
		try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec( password, saltBytes, iterations, keyLength );
            SecretKey key = skf.generateSecret( spec );
            byte[] res = key.getEncoded( );
            return toHex(res);
        } catch ( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
	}
	
	/***
	 * Function to convert byte array to a hexadecimal-based string
	 * @param array (btye[]) - 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private String toHex(byte[] array) throws NoSuchAlgorithmException
	{
	    BigInteger bi = new BigInteger(1, array);
	    String hex = bi.toString(16);
	    
	    int paddingLength = (array.length * 2) - hex.length();
	    if(paddingLength > 0)
	    {
	        return String.format("%0"  + paddingLength + "d", 0) + hex;
	    }else{
	        return hex;
	    }
	}
}
