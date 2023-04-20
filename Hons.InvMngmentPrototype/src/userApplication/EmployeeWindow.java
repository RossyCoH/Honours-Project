package userApplication;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;

/***
 * Class for the Hire Window
 * @author Ross Hunter
 * @dateCreated 29/01/2023
 * @dateLastModified 03/02/2023
 */
public class EmployeeWindow {
	
	private BusinessApplication logicLayer;
	
	private JPanel topPanel = new JPanel();
	private JLabel userLbl = new JLabel();
	{
		topPanel.add(userLbl);
	}
	
	private JPanel feedbackPanel = new JPanel();
	private JLabel tblLbl = new JLabel("All Hires: ");
	private JTable recordTable = new JTable();
	
	DefaultTableModel model = new DefaultTableModel();
	JScrollPane scroll = new JScrollPane(recordTable);
	{
		recordTable.setDefaultEditor(Object.class, null);
		recordTable.setRowSelectionAllowed(true);
		recordTable.setCellSelectionEnabled(true);
	    ListSelectionModel cellSelectionModel = recordTable.getSelectionModel();
	    cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
	    recordTable.addMouseListener(new MouseAdapter() {
	    	public void mouseClicked(MouseEvent e) {
	    		int row = recordTable.getSelectedRow();
	    		Object selectedHID = recordTable.getModel().getValueAt(row, 0);
	    		//System.out.println(row);
	    		hireSelection(selectedHID);
	    	}
	    });
	    
		feedbackPanel.setLayout(new GridLayout(2,1));
		feedbackPanel.setPreferredSize(new Dimension(380, 200));
		scroll.setPreferredSize(new Dimension(380,280));
		feedbackPanel.add(tblLbl);
		feedbackPanel.add(scroll);
		feedbackPanel.setVisible(true);
		
	}
	
	private JPanel buttonPanel = new JPanel();
	private JButton addClientButton = new JButton("Add Client");
	private JButton addHireButton = new JButton("Add Hire");
	private JButton logoutButton = new JButton("Logout");
	{
		
		buttonPanel.add(logoutButton);
		buttonPanel.add(addClientButton);
		buttonPanel.add(addHireButton);
	}
	
	private JFrame window = new JFrame();
	{
		window.setLayout(new GridLayout(3, 3));
		window.setPreferredSize(new Dimension(800,600));
		window.add(topPanel);
		window.add(feedbackPanel);
		window.add(buttonPanel);
		window.pack();
		window.setTitle("Equipment Managment App - Employee App");
		try {
			window.setIconImage(ImageIO.read(new File("src/imgs/radio_icon.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/***
	 * Construtor to create 
	 * @param app - connection to business logic
	 * @param uInfo - String with user info from display
	 */
	public EmployeeWindow( BusinessApplication app, String uInfo) {
		logicLayer = app;
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		logoutButton.addActionListener(new LogoutButtonListener());
		addHireButton.addActionListener(new AddHireButtonListener());
		addClientButton.addActionListener(new AddClientButtonListener());
		
		userLbl.setText("Employee: " + uInfo);
		
		updateTable();
	}
	
	private class AddHireButtonListener implements ActionListener
	{
		// Called when the check button is clicked
		public void actionPerformed(ActionEvent arg0)
		{
			logicLayer.displayAddHireWindow();
			window.dispose();
		}
	}
	
	private class AddClientButtonListener implements ActionListener
	{
		// Called when the check button is clicked
		public void actionPerformed(ActionEvent arg0)
		{
			logicLayer.displayAddClientWindow();
			//window.dispose();
		}
	}
	
	// The action listener on the check button
	private class LogoutButtonListener implements ActionListener
	{
		// Called when the check button is clicked
		public void actionPerformed(ActionEvent arg0)
		{
			logicLayer.Logout();
			window.dispose();
		}
	}
	
	public void updateTable() {
		
		scroll.repaint();
		recordTable.repaint();
		model.setRowCount(0);
		model.setColumnCount(0);
		
		String column[] = {"HIRE ID", "START DATE", "END DATE", "COMPLETE", "CLIENT ID", " CLICK TO VIEW"};
		for (String c : column) {
			model.addColumn(c);
		}
		
		ArrayList<String[]> records = logicLayer.hireRecords();
		for (String[] r : records) {
			
			model.addRow(r);
			//System.out.println(r[0]);
		}
		
		//feedbackPanel.add(new JScrollPane(jt));
		System.out.println();
		
		recordTable.setModel(model);
		recordTable.revalidate();
	}

	private void hireSelection(Object hID) {
		//System.out.println(hID);
		String hireS = (String) hID;
		logicLayer.setHire(Integer.parseInt(hireS));
		logicLayer.displayHireRecordWindow(true);
		window.setVisible(false); // Change to Close and clear resources!!
	}
	
}
