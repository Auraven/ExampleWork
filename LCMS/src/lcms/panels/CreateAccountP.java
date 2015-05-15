package lcms.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lcms.base.C;
import lcms.base.Log;

public class CreateAccountP extends JPanel implements ActionListener{
	public MenuP menu;
	private JComboBox<String> selType;
	private JTextField usernameF, passwordF, firstnameF, lastnameF, ageF;
	private JCheckBox enabledC;
	public CreateAccountP(MenuP menu){
		this.menu = menu;
		setSize(menu.getWidth(), menu.getHeight());
		setLayout(new GridBagLayout());
		selType = new JComboBox<String>();
		selType.addItem("Student");
		selType.addItem("Teacher");
		selType.addItem("Manager");
		selType.addItem("Auditor");
		
		JLabel tl = new JLabel("Account Type:");
		JLabel ul = new JLabel("Username:");
		JLabel pl = new JLabel("Password:");
		JLabel fnl = new JLabel("First Name:");
		JLabel lnl = new JLabel("LastName:");
		JLabel al = new JLabel("Age:");
		JLabel el = new JLabel("Enabled:");
		usernameF = new JTextField(20);
		passwordF = new JTextField(20);
		firstnameF = new JTextField(20);
		lastnameF = new JTextField(20);
		ageF = new JTextField(3);
		enabledC = new JCheckBox();
		JButton createB = new JButton("Create Account");
		createB.addActionListener(this);
		//Layout
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;	
		add(tl, gbc);
		gbc.gridx++;
		add(selType, gbc);
		gbc.gridy += 2;
		gbc.gridx = 0;
		add(ul, gbc);
		gbc.gridx++;
		add(usernameF, gbc);
		gbc.gridx = 0;
		gbc.gridy++;
		add(pl, gbc);
		gbc.gridx++;
		add(passwordF, gbc);
		gbc.gridx = 0;
		gbc.gridy += 2;
		add(fnl, gbc);
		gbc.gridx++;
		add(firstnameF, gbc);
		gbc.gridx = 0;
		gbc.gridy++;
		add(lnl, gbc);
		gbc.gridx++;
		add(lastnameF, gbc);
		gbc.gridx = 0;
		gbc.gridy++;
		add(al, gbc);
		gbc.gridx++;
		add(ageF, gbc);
		gbc.gridx = 0;
		gbc.gridy++;
		add(el, gbc);
		gbc.gridx++;
		add(enabledC, gbc);
		gbc.gridx = 1;
		gbc.gridy++;
		add(createB, gbc);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		if(cmd.equals("Create Account")){
			String type = (String)selType.getSelectedItem();
			String username = usernameF.getText();
			String password = passwordF.getText();
			String firstname = firstnameF.getText();
			String lastname = lastnameF.getText();
			String age = ageF.getText();
			boolean enabled = enabledC.isSelected();
			if(!type.isEmpty() && !username.isEmpty() && !password.isEmpty() && !lastname.isEmpty() && !age.isEmpty()){
				if(username.length() < 3){
					JOptionPane.showMessageDialog(this, "Username must be longer than 3 characters.");
					return;
				}
				if(password.length() < 6){
					JOptionPane.showMessageDialog(this, "Password must be longer than 6 characters.");
					return;
				}
				ResultSet userSet = C.selectWhere("users", "username", username);
				try {
					if(userSet != null && userSet.next()){
						JOptionPane.showMessageDialog(this, "User " + username + " already exists.");
						usernameF.setText("");
					}else{
						if(enabled){
							C.insert("users", new String[]{type, username, password, firstname, lastname, age, "0"});
						}
						else{
							C.insert("disabled_users", new String[]{type, username, password, firstname, lastname, age});
						}
						Log.log("[Account Created] ", username);
						JOptionPane.showMessageDialog(this, "User " + username + " successfully added!");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}		
			}else{
				JOptionPane.showMessageDialog(this, "Please fill out all fields");
			}
				
		}
	}
}
