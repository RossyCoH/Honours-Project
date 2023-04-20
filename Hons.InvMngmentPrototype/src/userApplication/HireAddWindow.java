package userApplication;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import com.github.lgooddatepicker.components.DatePicker;

/***
 * Class for hire addition window
 * @author Ross Hunter
 * @since 17/02/2023
 * 
 */
public class HireAddWindow {

	private BusinessApplication logicLayer;
	
	
	private JPanel inputPanel = new JPanel();
	private JLabel clientLbl = new JLabel("Client ID:");
	//private JTextField clientTxt = new JTextField(10);
	private JComboBox<String> clientCmb = new JComboBox<String>();
	
	private JLabel startDateLbl = new JLabel("Start Date: *");
	private DatePicker startDatePicker = new DatePicker();
	
	private JLabel endDataLbl = new JLabel("End Date: *");
	private DatePicker endDatePicker = new DatePicker();
	{
		
		inputPanel.add(clientLbl);
		//inputPanel.add(clientTxt);
		inputPanel.add(clientCmb);
		inputPanel.add(startDateLbl);

		inputPanel.add(startDatePicker);
		inputPanel.add(endDataLbl);
		
		inputPanel.add(endDatePicker);

		
	}
	
	private JPanel searchFilterPanel = new JPanel();
	private JLabel searchLbl = new JLabel("Search Equipment:");
	private JTextField searchTxt = new JTextField(15);
	private JButton searchButton = new JButton("Search");
	private JLabel filterLbl = new JLabel("Filter by Type:");
	private JComboBox<String> filterTypeCmb = new JComboBox<String>();
	{

		searchFilterPanel.add(searchLbl);
		searchFilterPanel.add(searchTxt);
		searchFilterPanel.add(searchButton);
		searchFilterPanel.add(filterLbl);
		searchFilterPanel.add(filterTypeCmb);
		
		searchFilterPanel.setVisible(true);
		
	}
	
	private JLabel searchTableLbl = new JLabel("Equipment search:");
	private JLabel addedListLbl = new JLabel("Equipment to be hired (double click to remove):");
	JPanel lblp = new JPanel();
	{
		lblp.add(searchTableLbl);
		
		lblp.setLayout(new GridLayout(1,2));
	}
	
	
	private JPanel resultsPanel = new JPanel();
	private JPanel searchPanel = new JPanel();
	private JPanel addedPanel = new JPanel();
	private JTable equipSearchTable = new JTable();
	private JList<String> equipAddedLst = new JList<String>();
	private DefaultTableModel searchTableModel = new DefaultTableModel();
	private DefaultListModel<String> addedListModel = new DefaultListModel<String>();
	private JScrollPane scrollST = new JScrollPane(equipSearchTable);
	private JScrollPane scrollAT = new JScrollPane(equipAddedLst);
	{
		// Making equipment search table read only
		equipSearchTable.setDefaultEditor(Object.class, null);
		equipSearchTable.setRowSelectionAllowed(true);
		equipSearchTable.setCellSelectionEnabled(true);
	    ListSelectionModel cellSelectionModel = equipSearchTable.getSelectionModel();
	    cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			    
	    equipSearchTable.addMouseListener(new MouseAdapter() {
	    	public void mouseClicked(MouseEvent e) {
			    int row = equipSearchTable.getSelectedRow();
			   	int col = equipSearchTable.getSelectedColumn();
			   	if (col == 3) {
			    	Object selectedHID;
					selectedHID = equipSearchTable.getModel().getValueAt(row, 0);
							
					if (logicLayer.addEquipmentSelected(selectedHID)) {
						updateAddedList();
						updateSearchTable(searchTxt.getText(),  "search");
					}
			    }
			    		
	    	}
	    });
	    
	    MouseListener mouseListener = new MouseAdapter() {
	    	public void mouseClicked(MouseEvent mouseEvent) {
			    if (mouseEvent.getClickCount() == 2) {
			    	int index = equipAddedLst.locationToIndex(mouseEvent.getPoint());
			    	if (index >= 0) {
			    		//set the text of the label to the selected value of lists
			    		String eqID = equipAddedLst.getSelectedValue().split(",")[0];
			    		if (logicLayer.removeEquipmentSelected(eqID)) {
			    			updateSearchTable("", "search");
			    			updateAddedList();
			    		}
			    		else {
			    			JOptionPane.showMessageDialog(window, "Error! Try Again!");
			    		}
			    	}
			    }
	    	}
	    };
	    equipAddedLst.addMouseListener(mouseListener);
		
		scrollST.setPreferredSize(new Dimension(380,280));
		scrollAT.setPreferredSize(new Dimension(380,280));
		
		
		searchPanel.setLayout(new GridLayout(1, 1));
		addedPanel.setLayout(new GridLayout(1, 1));
		

		searchPanel.add(scrollST);
		
		lblp.add(addedListLbl);
		addedPanel.add(scrollAT);
		
		resultsPanel.add(searchPanel);
		resultsPanel.add(addedPanel);
		
		resultsPanel.setLayout(new GridLayout(1,2));
		resultsPanel.setVisible(true);
	}
	
	private JPanel buttonPanel = new JPanel();
	private JButton backButton = new JButton("< Back");
	private JButton submitButton = new JButton("Submit");
	{
		buttonPanel.add(backButton);
		buttonPanel.add(submitButton);
		buttonPanel.setVisible(true);
	}
	
	private JFrame window = new JFrame();
	{
		window.setLayout(new GridLayout(5, 1));
		window.setPreferredSize(new Dimension(800,600));
		window.add(inputPanel);
		window.add(searchFilterPanel);
		window.add(lblp);
		window.add(resultsPanel);
		window.add(buttonPanel);
		window.pack();
		window.setTitle("Equipment Managment App - Add Hire");
		try {
			window.setIconImage(ImageIO.read(new File("src/imgs/radio_icon.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HireAddWindow( BusinessApplication app) {
		logicLayer = app;
		
		DefaultComboBoxModel<String> cmbModel = new DefaultComboBoxModel<String>();
		ArrayList<String> clientList = new ArrayList<String>();
		
		for (String r : logicLayer.getClients()) {
			clientList.add(r);
		}
		
		cmbModel.addAll(clientList);
		clientCmb.setModel(cmbModel);
		
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setVisible(true);
		
		filterTypeCmb.setModel(new DefaultComboBoxModel<String>(logicLayer.getEquipmentTypes()));
		
		searchButton.addActionListener(new SearchButtonListener());
		submitButton.addActionListener(new SubmitButtonListener());
		backButton.addActionListener(new BackButtonListener());
		filterTypeCmb.addActionListener(new CmbFListener());
		
		updateSearchTable("",  "search");
	}
	
	private void updateSearchTable(String searchTerm, String method) {
		
		scrollST.repaint();
		equipSearchTable.repaint();
		searchTableModel.setRowCount(0);
		searchTableModel.setColumnCount(0);
		
		String column[] = {"EQIUP ID", "EQUIPMENT MAKE/MODEL", "TYPE", "CLICK TO ADD"};
		for (String c : column) {
			searchTableModel.addColumn(c);
		}
		
		ArrayList<String[]> records;
		if (method.equals("search")) {
			records = logicLayer.equipmentSearch(searchTerm);
		}
		else if (method.equals("filter"))   {
			records = logicLayer.equipmentFilter(searchTerm);
		}
		else {
			records = null;
		}
		
		for (String[] r : records) {
			
			searchTableModel.addRow(r);
			//System.out.println(r[0]);
		}
		
		//feedbackPanel.add(new JScrollPane(jt));
		
		equipSearchTable.setModel(searchTableModel);
		equipSearchTable.revalidate();
	}
	
	
	private void updateAddedList() {
		
		equipAddedLst.repaint();
		addedListModel.setSize(0);
		
		ArrayList<String> records = logicLayer.equipmentSelectedList();
		
		int i = 0;
		for (String r : records) {
			addedListModel.add(i, r);
			//System.out.println(r[0]);
			i++;
		}
		
		equipAddedLst.setModel(addedListModel);
		equipAddedLst.revalidate();
		scrollAT.repaint();
	}
	
	/***
	 * Function to reset all fields after hire added successfully
	 */
	private void resetAfterComplete() {
		//clientTxt.setText("");
		clientCmb.setSelectedIndex(0);
		startDatePicker.clear();
		endDatePicker.clear();
		updateSearchTable("", "search");
		updateAddedList();
	}
	
	
	
	// The action listener on the submit button
	private class SubmitButtonListener implements ActionListener
	{
		// Called when the submit button is clicked
		public void actionPerformed(ActionEvent arg0)
		{
			String sDate = startDatePicker.getDateStringOrEmptyString();
			String eDate = endDatePicker.getDateStringOrEmptyString();
			
			String selectedCID = String.valueOf(clientCmb.getSelectedItem()).split(",")[0];
			try {
				if ( logicLayer.addHire(sDate, eDate, selectedCID) ) {
					JOptionPane.showMessageDialog(window, "Hire Added Successfully");
					resetAfterComplete();
				}
				else {
					JOptionPane.showMessageDialog(window, "Error! Try Again!");
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(window, "Error! Try Again!");
			}
		}
	}

	// The action listener on the submit button
	private class SearchButtonListener implements ActionListener
	{
		// Called when the submit button is clicked
		public void actionPerformed(ActionEvent arg0)
		{
			updateSearchTable(searchTxt.getText(),  "search");
		}
	}
	
	// The action listener on the check button
	private class BackButtonListener implements ActionListener
	{
		// Called when the check button is clicked
		public void actionPerformed(ActionEvent arg0)
		{
			logicLayer.returnEmployeeWindow();
			window.dispose();
				
		}
	}
	
	private class CmbFListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			String s = (String) filterTypeCmb.getSelectedItem();
			updateSearchTable(s, "filter");
			searchTxt.setText("");
		}
	}
	
	public void valueChanged(ListSelectionEvent e)
    {
        //set the text of the label to the selected value of lists
		String eqID = equipAddedLst.getSelectedValue().split(",")[0];
		if (logicLayer.removeEquipmentSelected(eqID)) {
			updateSearchTable("", "search");
			updateAddedList();
		}
		else {
			JOptionPane.showMessageDialog(window, "Error! Try Again!");
		}
         
    }
	
	/*
	 * private class CmbCListener implements ActionListener { public void
	 * actionPerformed(ActionEvent arg0) { String s = (String)
	 * filterTypeCmb.getSelectedItem(); updateSearchTable(s, "filter"); } }
	 */
	
}
