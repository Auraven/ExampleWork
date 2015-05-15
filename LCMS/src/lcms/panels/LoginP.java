package lcms.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import lcms.accounts.Auditor;
import lcms.accounts.Manager;
import lcms.accounts.Student;
import lcms.accounts.Teacher;
import lcms.base.C;
import lcms.base.Launcher;
import lcms.base.Log;

@SuppressWarnings("serial")
public class LoginP extends JPanel implements ActionListener{
	private Launcher applet;
	
	private JTextField username;
	private JPasswordField password;
	
	public LoginP(Launcher applet){
		super();
		this.applet = applet;
		setSize(applet.getWidth(), applet.getHeight());
		/*Authentification Page Stuff*/
		
		 /*-Have the password box generate asterisks for every letter the person
		 *types
		 * -Show image for top of screen
		 */
		SpringLayout layout= new SpringLayout();
		username = new JTextField(30);
		password = new JPasswordField(30);
		password.setEchoChar('*');
		JLabel login = new JLabel("Username");
		JLabel loginPass = new JLabel("Password");
		
		/*
		 * Later these two will display an image to be shown at the top of the
		 * authentification page.
		 */
		ImageIcon img; 
		JLabel airplaneImage = new JLabel("");
		
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		
		setLayout(layout);
		add(username);//username box creation
		add(password);//password box creation
		add(login); 
		add(loginPass); 
		add(loginButton);
		
		layout.putConstraint(SpringLayout.WEST, login, 200, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, login, 150, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, username, 280, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, username, 150, SpringLayout.NORTH, this);				
		
		layout.putConstraint(SpringLayout.WEST, loginPass, 200, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, loginPass, 170, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, password, 280, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, password, 170, SpringLayout.NORTH, this);
		
		
		layout.putConstraint(SpringLayout.WEST, loginButton, 300, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, loginButton, 200, SpringLayout.NORTH, this);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		if(cmd.equalsIgnoreCase("login")){
			String user_name = username.getText();
			String pass_word = password.getText();
			if(user_name.equals("") && pass_word.equals("")){
				JOptionPane.showMessageDialog(this, "Please Enter a Username and Password");
				username.setBackground(Color.red);
				password.setBackground(Color.red);
			}
			else if(user_name.equals("")){
				JOptionPane.showMessageDialog(this, "Please Enter a Username");
				username.setBackground(Color.red);
				password.setBackground(Color.white);
			}
			else if(pass_word.equals("")){
				JOptionPane.showMessageDialog(this, "Please Enter a Password");
				password.setBackground(Color.red);
				username.setBackground(Color.white);
			}
			//Query Users
			
			ResultSet userSet = C.selectWhere("users", "*", "username", username.getText());
			try {
				boolean hit = false;
				while(userSet.next()){
					hit = true;
					String s_password = userSet.getString("password");
					if(!userSet.wasNull() && s_password.equals(password.getText())){
						int s_act = userSet.getInt("isactive");
						if(s_act == 0){
							String s_acc = userSet.getString("accounttype");
							
							if(s_acc.equalsIgnoreCase("Student")){
								Launcher.panels.add(1, new MenuP(applet, new Student(userSet)));
							}else if(s_acc.equalsIgnoreCase("Teacher")){
								Launcher.panels.add(1, new MenuP(applet, new Teacher(userSet)));
							}else if(s_acc.equalsIgnoreCase("Manager")){
								Launcher.panels.add(1, new MenuP(applet, new Manager(userSet)));
							}else if(s_acc.equalsIgnoreCase("Auditor")){
								Launcher.panels.add(1, new MenuP(applet, new Auditor(userSet)));
							}
							else{
								System.out.println(s_acc);
							}
							Log.log("[Logged In]", username.getText());
							C.update("users", "isactive", "1", "username", user_name);
							applet.enterPanel(1);
							break;
						}else{
							JOptionPane.showMessageDialog(this, "User " + user_name + " is already logged in! Please contact a manager for support.");
							C.update("users", "isactive", "0", "username", user_name);
							break;
						}
						
					}else if(!userSet.wasNull() && !s_password.equals(password.getText())){
						JOptionPane.showMessageDialog(this, "Invalid Username or Password!");
					}else if(userSet.wasNull() && username.getText().equalsIgnoreCase("admin")){
						//First Time admin Log in
						JOptionPane.showMessageDialog(this, "Welcome Admin!");
						if(C.insert("users", new String[]{"","Manager","Admin","password","TempFirstName","TempLastName", "TempAge"})){
							Launcher.panels.add(new MenuP(applet, new Manager(userSet)));
							applet.enterPanel(1);
							username.setText("");
							password.setText("");
							Log.log("[Logged In]", username.getText());
						}
					}
				}
				if(!hit){
					JOptionPane.showMessageDialog(this, "Invalid Username or Password!");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}		
			try {
				userSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}	
}