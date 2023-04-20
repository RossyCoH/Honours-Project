package hireService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;

/***
 * Class to run server-side of application
 * @author Ross Hunter
 * @since 27/02/2023
 */
public class HireServer {

	static HireApp hireApp;
	static DataInterface dataIntf;
	static ClientDomainLogic clientDom;
	static BusinessDomainLogic businessDom;

    private static ServerSocket serverSocket;

    public static void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            while (true)
                new EchoClientHandler(serverSocket.accept()).start();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }

    }

    public static void stop() {
        try {

            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class EchoClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String msgOut;
        
        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (".".equals(inputLine)) {
                        out.println("bye");
                        break;
                    }
                    System.out.println(inputLine);
                    msgOut = interpretMessage(inputLine);
                    System.out.println(msgOut);
                    out.println(msgOut);
                }

                in.close();
                out.close();
                clientSocket.close();

            } catch (IOException e) {
                //LOG.debug(e.getMessage());
            }
        }
    }
	
	public static void main(String[] args) {
		
		dataIntf = new DataAccess();
		
		hireApp = new HireApp(dataIntf);
		clientDom = new ClientDomainLogic(dataIntf);
		businessDom = new BusinessDomainLogic(dataIntf);
		
		start(5555);
		/*
		 * try { ServerSocket serverSock =new ServerSocket(6666); Socket socket =
		 * serverSock.accept(); DataInputStream dataInS =new
		 * DataInputStream(socket.getInputStream()); DataOutputStream dataOutS =new
		 * DataOutputStream(socket.getOutputStream()); //BufferedReader buffReader =new
		 * BufferedReader(new InputStreamReader(System.in));
		 * 
		 * String stringIn="",stringOut=""; while(stringIn != null){
		 * stringIn=dataInS.readUTF(); System.out.println("client says: "+stringIn);
		 * stringOut = interpretMessage(stringIn); dataOutS.writeUTF(stringOut);
		 * dataOutS.flush(); }
		 * 
		 * dataInS.close(); socket.close(); serverSock.close(); } catch (IOException e)
		 * { // TODO Auto-generated catch block e.printStackTrace(); }
		 */

	}
	
	/***
	 * Method to set where message is to be processed
	 * @param inputString - string recived from client app
	 * @return returnS - string to be sent back to client app
	 */
	private static String interpretMessage(String inputString) {
		
		String[] splitS = inputString.split(">");
		String returnS = null;
		int len;
		int[] equipIDs;
		int ID;
		
		System.out.println(splitS[0]);
		//System.out.println(splitS[1]);
		switch (splitS[0]) {
		case "log":
			returnS = "log>" + hireApp.loginProcess(splitS[1], splitS[2]);
			break;
			
		case "allhires":
			returnS = hireApp.getHires().toString();
			break;
			
		case "clienthires":
			int cID = Integer.parseInt(splitS[1]);
			returnS = clientDom.getClientHireRecords(cID);
			break;
			
		case "hireequip":
			int hID = Integer.parseInt(splitS[1]);
			returnS = hireApp.getHireEquipmentList(hID);
			break;
			
		case "equipsearch":
			String search;
			if (splitS.length == 1) {
				search = "";
			}
			else {
				search = splitS[1];
			}
			returnS = businessDom.searchEquipment(search);
			break;
			
		case "equipfilter":
			returnS = businessDom.equipmentFilter(splitS[1]);
			break;
			
		case "equipuseage":
			int eID = Integer.parseInt(splitS[2]);
			ID = Integer.parseInt(splitS[1]);
			returnS = hireApp.getEquipmentUsage(ID, eID);
			break;
			
		case "equipselected":
			len = splitS.length;
			equipIDs = new int[len - 2];
			for (int i = 1; i < len - 1; i++) {
				equipIDs[i - 1] = Integer.parseInt(splitS[i + 1]);
			}
			returnS = businessDom.selectedEquipmentList(equipIDs);
			break;
			
		case "addhire":
			
			Date startDate = Date.valueOf(splitS[1]);
			Date endDate = Date.valueOf(splitS[2]);
			ID = parseInt(splitS[3]);
			
			len = splitS.length;
			equipIDs = new int[len - 5];
			for (int i = 0; i < len - 5; i++) {
				equipIDs[i] = Integer.parseInt(splitS[i + 5]);
			}
			
			if(businessDom.addHire(startDate, endDate, ID, equipIDs)) {
				returnS = "true";
			}
			else {
				returnS = "false";
			}
			
			break;
			
		case "endhire":
			ID = parseInt(splitS[1]);
			
			if (businessDom.endHire(ID)) {
				returnS = "true";
			}
			else {
				returnS = "false";
			}
			
			break;
		
		case "hireCompState":
			ID = parseInt(splitS[1]);
			
			if (hireApp.hireCompState(ID)) {
				returnS = "true";
			}
			else {
				returnS = "false";
			}
			
			break;
			
		case "addclient":
			
			if (businessDom.addClient(splitS[1], splitS[2], splitS[3], splitS[4], splitS[5])) {
				returnS = "true";
			}
			else {
				returnS = "false";
			}
			
			break;
			
		case "equipavail":
			ID = parseInt(splitS[1]);
			if (hireApp.equipmentAvailable(ID)) {
				returnS = "true";
			}
			else {
				returnS = "false";
			}
			
			break;
			
		case "checkoutequip":
			int opID = parseInt(splitS[1]);
			int eqID = parseInt(splitS[2]);
			ID = parseInt(splitS[3]);
			
			if(clientDom.checkoutEquipment(opID, eqID, ID) ) {
				returnS = "true";
			}
			else {
				returnS = "false";
			}
			break;
			
		case "returnequip":
			String note = splitS[1];
			int equID = parseInt(splitS[2]);
			ID = parseInt(splitS[3]);
			
			if(clientDom.returnEquipment(note, equID, ID) ) {
				returnS = "true";
			}
			else {
				returnS = "false";
			}
			break;
			
		case "addOp":
			String name = splitS[1];
			String cIDS = splitS[2];
			
			returnS = clientDom.addOperator(name, cIDS);
			break;
			
		case "getops":
			ID = parseInt(splitS[1]); //parse clientID from string to int
			
			returnS = clientDom.getOperators(ID);
			break;
			
		case "clientid":
			int aID = parseInt(splitS[1]);
			int cliID = hireApp.getClientID(aID);
			returnS = String.valueOf(cliID);
			break;
			
		case "getclients":
			returnS = businessDom.getClients();
			break;
			
		case "equiptypes":
			returnS = businessDom.getEquipmentTypes();
			break;
		}
		
		return returnS;
	}
	
	private static int parseInt(String string) {
		return Integer.parseInt(string);
	}

}
