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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class HireDisplayWindow {
	
	private ClientApplication clientLogic;
	private BusinessApplication businessLogic;
	private boolean view;
	
	private JPanel topPanel = new JPanel();
	private JLabel userLbl = new JLabel();
	{
		topPanel.add(userLbl);
	}
	
	private JPanel feedbackPanel = new JPanel();
	private JTable recordTable = new JTable();
	private JLabel tblLbl = new JLabel("Equipment Hired:");
	
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
	    		
				// Get column and row values selected 
				int row = recordTable.getSelectedRow();
				int col = recordTable.getSelectedColumn();
				Object selectedEID = recordTable.getModel().getValueAt(row, 0);
				
				// When col is 4 for checking out, or any other for viewing usage
				if (col == 4 && !view) {
					// Checking equipment item is available
					if (equipmentAvailableCheck(selectedEID)) {
						equipmentSelection(selectedEID, 0); //If available, set view for checkout
					} else {
						equipmentSelection(selectedEID, 1); //If in use, set for return
					}

				} else if (col == 5) {
					// View usage history
					equipmentSelection(selectedEID, 2);
				}
				else if (view) {
					JOptionPane.showMessageDialog(window, "Not able to perform this action!");
				}
				
	    		//System.out.println(row);
	    	}
	    });
	    
		feedbackPanel.setLayout(new GridLayout(2,1));
		scroll.setPreferredSize(new Dimension(380,280));
		feedbackPanel.add(tblLbl);
		feedbackPanel.add(scroll);
		feedbackPanel.setVisible(true);
		
	}
	
	private JPanel buttonPanel = new JPanel();
	private JButton backButton = new JButton("< Back");
	private JButton endHireButton = new JButton("EndHire");
	{
		//buttonPanel.setLayout(new GridLayout(1,1));
		buttonPanel.add(backButton);
		buttonPanel.add(endHireButton);
	}
	
	private JFrame window = new JFrame();
	{
		window.setLayout(new GridLayout(3, 1));
		window.setPreferredSize(new Dimension(800,600));
		window.add(topPanel);
		window.add(feedbackPanel);
		window.add(buttonPanel);
		window.pack();
		window.setTitle("Equipment Managment App - Hire List");
		try {
			window.setIconImage(ImageIO.read(new File("src/imgs/radio_icon.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HireDisplayWindow( ClientApplication app, boolean view) {
		clientLogic = app;
		this.view = view;
		
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setVisible(true);
		
		backButton.addActionListener(new BackButtonListener());
		endHireButton.addActionListener(new EndHireButtonListener());
		
		// Hide endHire button when Client is viewing or hire is complete
		boolean complete = clientLogic.getHireCompleteState();
		if (!view || complete) {
			endHireButton.setVisible(false);
		}
		
		updateTable();
		userLbl.setText("HireID: " + clientLogic.getHire());
	}
	
	public HireDisplayWindow( BusinessApplication app, boolean view) {
		businessLogic = app;
		this.view = view;
		
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setVisible(true);
		
		backButton.addActionListener(new BackButtonListener());
		endHireButton.addActionListener(new EndHireButtonListener());
		
		// Hide endHire button when Client is viewing or hire is complete
		boolean complete = businessLogic.getHireCompleteState();
		if (!view || complete) {
			endHireButton.setVisible(false);
		}
		
		updateTable();
		userLbl.setText("HireID: " + businessLogic.getHire());
	}
	
	/***
	 * Function to update table data
	 */
	public void updateTable() {
		
		recordTable.repaint(); // Reset table
		model.setRowCount(0); // Start at row 0
		model.setColumnCount(0); // Start at row 0
		
		// Add columns with headers
		String column[] = {"EQIUP ID", "EQUIPMENT MAKE/MODEL", "TYPE", "AVAILABILTY", "CHECKOUT / RETURN", "USAGE"}; // Headers for table
		for (String c : column) {
			model.addColumn(c); // Add each column by header
		}
		
		ArrayList<String[]> records = null;
		if (view) {
			records = businessLogic.hireEquipmentList(); // Get all equipment items for the hire
		}
		else {
			records = clientLogic.hireEquipmentList(); // Get all equipment items for the hire
		}
		
		
		for (String[] r : records) {
			
			model.addRow(r); // Add each equipment item as a row
		}
		
		recordTable.setModel(model); // Set new model of new data to table
	}
	
	/***
	 * Function to handle what action to do based on which equipment item selected
	 * @param selectedEq
	 * @param actionState
	 */
	public void equipmentSelection(Object selectedEq, int actionState) {
		
		switch (actionState) {
			case 0:
				if (!view) { clientLogic.displayCheckoutWindow(Integer.parseInt(selectedEq.toString()), "out"); }
			case 1:
				if (!view) { clientLogic.displayCheckoutWindow(Integer.parseInt(selectedEq.toString()), "in"); }
			default:
				if (view) {
					businessLogic.displayUsageWindow(Integer.parseInt(selectedEq.toString()));
				}
				else {
					clientLogic.displayUsageWindow(Integer.parseInt(selectedEq.toString()));
				}
		}
		
	}
	
	private boolean equipmentAvailableCheck(Object eqID) {
		if (!view) {
			return clientLogic.equipmentAvailable(Integer.parseInt(eqID.toString()));
		}
		return false;
	}
	
	// The action listener on the check button
	private class BackButtonListener implements ActionListener
	{
		// Called when the check button is clicked
		public void actionPerformed(ActionEvent arg0)
		{
			if(!view) {
				clientLogic.returnClientWindow();
			}
			else {
				businessLogic.returnEmployeeWindow();
			}
			window.dispose();
		}
	}
	
	// The action listener on the check button
	private class EndHireButtonListener implements ActionListener
	{
		// Called when the check button is clicked
		public void actionPerformed(ActionEvent arg0)
		{
				
				if (businessLogic.endHire()) {
					JOptionPane.showMessageDialog(window, "Hire Completed Successfully!");
					businessLogic.returnEmployeeWindow();
					window.dispose();
				} else {
					JOptionPane.showMessageDialog(window, "Error! Cannot perform this operation");
				} 
		}
	}
}
