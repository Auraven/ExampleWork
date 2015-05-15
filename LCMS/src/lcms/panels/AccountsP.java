package lcms.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import lcms.base.C;
import lcms.base.Log;

public class AccountsP extends JPanel implements ActionListener{
	private JScrollPane scrollpane;
	private JTable accountTable;
	public DefaultTableModel model;
	private JButton disableB;
	private MenuP menu;
	public AccountsP(MenuP menu){
		this.menu = menu;
		setSize(menu.getWidth(), menu.getHeight());
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		//ScrollPane/Table		
		scrollpane = new JScrollPane();
		scrollpane.setSize(getWidth(), getHeight());
		accountTable = new JTable();
		accountTable.setSize(scrollpane.getWidth(), scrollpane.getHeight());
		update();
		scrollpane.add(accountTable);
		scrollpane.setViewportView(accountTable);
		
		//Bottom
		disableB = new JButton("Disable Selected");
		disableB.addActionListener(this);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(scrollpane, gbc);
		gbc.gridy++;
		add(disableB, gbc);
	}
	
	public void update(){
		model = new DefaultTableModel();
		model.addColumn("Type");
		model.addColumn("Username");
		model.addColumn("Password");
		model.addColumn("FirstName");
		model.addColumn("LastName");
		model.addColumn("Age");
		model.addColumn("Active");		
		accountTable.setModel(model);

		ResultSet userSet = C.select("users", "*");
		try {
			while(userSet.next()){
				JCheckBox cb = new JCheckBox();
				cb.setName(userSet.getString("username"));
				
				model.addRow(new Object[]{userSet.getString("accounttype"), userSet.getString("username")
						, userSet.getString("password"), userSet.getString("firstname")
						, userSet.getString("lastname"), userSet.getString("age")
						, userSet.getString("isactive")});
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		if(cmd.equals("Disable Selected")){
			int row = accountTable.getSelectedRow();
			if(row != -1){
				String type = (String)model.getValueAt(row, 0);
				String username = (String)model.getValueAt(row, 1);
				String password = (String)model.getValueAt(row, 2);
				String firstname = (String)model.getValueAt(row, 3);
				String lastname = (String)model.getValueAt(row, 4);
				String age = (String)model.getValueAt(row, 5);
				if(C.insert("disabled_users", new String[]{type, username, password, firstname, lastname, age})){
					if(C.delete("users", "username", username)){
						model.removeRow(row);
						menu.dap.model.addRow(new String[]{type, username, password, firstname, lastname, age});
						Log.log("[Disabled Account]", menu.acc.getUsername());
					}else{
						C.delete("disabled_users", "username", username);
						JOptionPane.showMessageDialog(this, "Error! Account was not Disabled.");					
					}					
				}
			}			
		}
	}
}
