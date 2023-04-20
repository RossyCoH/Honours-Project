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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class EquipmentUsageWindow {
	
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
	private JLabel tblLbl = new JLabel("Usage Records:");
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
	    		Object selectedOID = recordTable.getModel().getValueAt(row, 0);
	    		//System.out.println(row);
	    		selectedUsage(selectedOID);
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
	{
		
		buttonPanel.add(backButton);
	}
	
	private JFrame window = new JFrame();
	{
		window.setLayout(new GridLayout(3, 1));
		window.setPreferredSize(new Dimension(600,400));
		window.add(topPanel);
		window.add(feedbackPanel);
		window.add(buttonPanel);
		window.pack();
		window.setTitle("Equipment Managment App - Equipment Usage");
		try {
			window.setIconImage(ImageIO.read(new File("src/imgs/radio_icon.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public EquipmentUsageWindow( ClientApplication app, int eqID) {
		clientLogic = app;
		
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setVisible(true);
		
		backButton.addActionListener(new BackButtonListener());
		
		updateTable();
		userLbl.setText("EquipmentID: " + eqID);
	}
	
	public EquipmentUsageWindow( BusinessApplication app, int eqID) {
		businessLogic = app;
		
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setVisible(true);
		
		backButton.addActionListener(new BackButtonListener());
		
		updateTable();
		userLbl.setText("EquipmentID: " + eqID);
	}
	
	private void updateTable() {
		
		recordTable.repaint();
		model.setRowCount(0);
		model.setColumnCount(0);
		
		String column[] = {"DATE/TIME OUT", "DATE/TIME RETURN", "NOTES", "OP ID", "OP NAME"};
		for (String c : column) {
			model.addColumn(c);
		}
		
		ArrayList<String[]> records = null;
		if (view) {
			records = businessLogic.equipmentUsage();
		} else {
			records = clientLogic.equipmentUsage();
		}
		for (String[] r : records) {
			
			model.addRow(r);
			//System.out.println(r[0]);
		}
		
		//feedbackPanel.add(new JScrollPane(jt));
		System.out.println();
		
		recordTable.setModel(model);
	}
	
	// The action listener on the check button
		private class BackButtonListener implements ActionListener
		{
			// Called when the check button is clicked
			public void actionPerformed(ActionEvent arg0)
			{
				if (view) {
					businessLogic.resetWindowDisplay(2);
				}
				else {
					clientLogic.resetWindowDisplay(2);
				}
				window.dispose();
				
			}
		}
	private void selectedUsage(Object opID) {
		//logicLayer.displayCheckoutWindow(0, "in");
	}
}
