package lcms.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import lcms.base.C;
import lcms.base.Log;

public class DisabledAccountsP extends JPanel implements ActionListener{
	private JScrollPane scrollpane;
	private JTable accountTable;
	public DefaultTableModel model;
	private JButton enableB;
	private MenuP menu;
	public DisabledAccountsP(MenuP menu){
		this.menu = menu;
		setSize(menu.getWidth(), menu.getHeight());
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		scrollpane = new JScrollPane();
		scrollpane.setSize(getWidth(), getHeight());
		accountTable = new JTable();
		accountTable.setSize(scrollpane.getWidth(), scrollpane.getHeight());
		update();
		scrollpane.add(accountTable);
		scrollpane.setViewportView(accountTable);
		enableB = new JButton("Enable Selected");
		enableB.addActionListener(this);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(scrollpane);
		gbc.gridy++;
		add(enableB, gbc);
	}
	
	public void update(){
		model = new DefaultTableModel();
		model.addColumn("Type");
		model.addColumn("Username");
		model.addColumn("Password");
		model.addColumn("FirstName");
		model.addColumn("LastName");
		model.addColumn("Age");		
		accountTable.setModel(model);
		
		ResultSet userSet = C.select("disabled_users", "*");
		try {
			while(userSet.next()){
				model.addRow(new Object[]{userSet.getString("accounttype"), userSet.getString("username")
						, userSet.getString("password"), userSet.getString("firstname")
						, userSet.getString("lastname"), userSet.getString("age")});
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	@Override
	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		if(cmd.equals("Enable Selected")){
			int row = accountTable.getSelectedRow();
			if(row != -1){
				String type = (String)model.getValueAt(row, 0);
				String username = (String)model.getValueAt(row, 1);
				String password = (String)model.getValueAt(row, 2);
				String firstname = (String)model.getValueAt(row, 3);
				String lastname = (String)model.getValueAt(row, 4);
				String age = (String)model.getValueAt(row, 5);
				if(C.insert("users", new String[]{type, username, password, firstname, lastname, age, "0"})){
					if(C.delete("disabled_users", "username", username)){
						model.removeRow(row);
						menu.ap.model.addRow(new String[]{type, username, password, firstname, lastname, age, "0"});
						Log.log("[Account Enabled]", menu.acc.getUsername());
					}else{
						C.delete("users", "username", username);
						JOptionPane.showMessageDialog(this, "Error! Account was not Enabled.");						
					}
				}
			}			
		}
	}
}
