package lcms.panels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;

import lcms.accounts.Account;
import lcms.accounts.Auditor;
import lcms.accounts.Manager;
import lcms.accounts.Student;
import lcms.accounts.Teacher;
import lcms.base.C;
import lcms.base.Launcher;
import lcms.base.Log;

@SuppressWarnings("serial")
public class MenuP extends JPanel implements ActionListener{
	public Launcher applet;
	public Account acc;
	public JTabbedPane tabpane;
	public AccountsP ap;
	public DisabledAccountsP dap;
	
	public MenuP(Launcher applet, Account acc){
		//Set Up
		this.applet = applet;
		this.acc = acc;
		setSize(applet.getWidth(), applet.getHeight());
		//Gen Panel
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		tabpane = new JTabbedPane();
		tabpane.setSize(applet.getWidth(), applet.getHeight());			
		
        GroupLayout glayout = new GroupLayout(this);
        setLayout(glayout);
        glayout.setHorizontalGroup(
        		glayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(tabpane)
        );
        glayout.setVerticalGroup(
        		glayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(tabpane)
        );

       GroupLayout glayout2 = new GroupLayout(applet.getContentPane());
        applet.getContentPane().setLayout(glayout2);
        glayout2.setHorizontalGroup(
        		glayout2.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(this, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        glayout2.setVerticalGroup(
        		glayout2.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(this, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );		
		
		if(acc instanceof Student){
			genStudentView(layout);
		}else if(acc instanceof Teacher){
			genTeacherView(layout);
		}else if(acc instanceof Manager){
			genManagerView(layout);
		}else if(acc instanceof Auditor){
			genAuditorView(layout);
		}		
		layout.putConstraint(SpringLayout.NORTH, this, 10, SpringLayout.NORTH, tabpane);
		add(tabpane, BorderLayout.CENTER);		
	}
	
	private void genStudentView(SpringLayout layout){
		//Panels
		JPanel home = new JPanel(layout);
		JPanel history = new JPanel(layout);
		JPanel practice = new JPanel(layout);
		JPanel sim = new JPanel(layout);		
		//Setup
		home.setSize(tabpane.getWidth(), tabpane.getHeight());
		
		
		
		JButton logoutB = new JButton("Log out");
		logoutB.addActionListener(this);
		logoutB.setLocation(home.getWidth() - logoutB.getWidth(), home.getHeight() - logoutB.getHeight());
		
		home.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		home.add(new JLabel("Welcome"), c);
		c.gridy++;
		home.add(new JLabel("First name:"), c);
		c.gridx++;
		home.add(new JLabel(acc.getFirstname()), c);
		c.gridx = 0;
		c.gridy++;
		home.add(new JLabel("Last name:"), c);
		c.gridx++;
		home.add(new JLabel(acc.getLastname()), c);
		c.gridx = 0;
		c.gridy++;
		home.add(logoutB, c);
		
		
		
		history.setSize(tabpane.getWidth(), tabpane.getHeight());
		history.add(new LogP(this));
		practice.setSize(tabpane.getWidth(), tabpane.getHeight());
		practice.add(new PracticeP(this, acc));
		sim.setSize(tabpane.getWidth(), tabpane.getHeight());
		sim.add(new ActualP(this));
		tabpane.addTab("Home", home);		
		tabpane.addTab("History", history);
		tabpane.addTab("Practice", practice);
		tabpane.addTab("Graded Simulator", sim);
	}
	private void genTeacherView(SpringLayout layout){
		JPanel home = new JPanel(layout);
		JPanel managetests = new JPanel(layout);
		//JPanel managecourses = new JPanel(layout);
		JPanel managestudents = new JPanel(layout);
		JButton logoutB = new JButton("Log out");
		logoutB.addActionListener(this);
		logoutB.setLocation(home.getWidth() - logoutB.getWidth(), home.getHeight() - logoutB.getHeight());
		
		home.setLayout(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = 0;
		c2.gridy = 0;
		home.add(new JLabel("Welcome"), c2);
		c2.gridy++;
		home.add(new JLabel("First name:"), c2);
		c2.gridx++;
		home.add(new JLabel(acc.getFirstname()), c2);
		c2.gridx = 0;
		c2.gridy++;
		home.add(new JLabel("Last name:"), c2);
		c2.gridx++;
		home.add(new JLabel(acc.getLastname()), c2);
		c2.gridx = 0;
		c2.gridy++;
		home.add(logoutB, c2);
		//ManageTests
		managetests.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JLabel d = new JLabel("Create practice tests for students");
		JButton createT = new JButton("Create Test");
		createT.addActionListener(this);
		c.gridx = 0;
		c.gridy = 0;
		managetests.add(d, c);
		c.gridy += 2;
		managetests.add(createT, c);
		
		//
		managestudents.add(new StudentsP(this));
		tabpane.addTab("Home", home);
		tabpane.addTab("Tests", managetests);
		//tabpane.addTab("Courses", managecourses);
		tabpane.addTab("Students", managestudents);
	}
	private void genManagerView(SpringLayout layout){
		JPanel home = new JPanel(layout);
		JPanel accounts = new JPanel(layout);
		JPanel dis_accounts = new JPanel(layout);
		JPanel create_account = new JPanel(layout);
		JPanel logs = new JPanel(layout);
		
		JButton logoutB = new JButton("Log out");
		logoutB.addActionListener(this);
		logoutB.setLocation(home.getWidth() - logoutB.getWidth(), home.getHeight() - logoutB.getHeight());
		
		home.setLayout(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = 0;
		c2.gridy = 0;
		home.add(new JLabel("Welcome"), c2);
		c2.gridy++;
		home.add(new JLabel("First name:"), c2);
		c2.gridx++;
		home.add(new JLabel(acc.getFirstname()), c2);
		c2.gridx = 0;
		c2.gridy++;
		home.add(new JLabel("Last name:"), c2);
		c2.gridx++;
		home.add(new JLabel(acc.getLastname()), c2);
		c2.gridx = 0;
		c2.gridy++;
		home.add(logoutB, c2);

		logs.add(new LogP(this));
		ap = new AccountsP(this);
		dap = new DisabledAccountsP(this);
		
		accounts.add(ap);
		dis_accounts.add(dap);
		create_account.add(new CreateAccountP(this));
		tabpane.addTab("Home", home);
		tabpane.addTab("Accounts", accounts);
		tabpane.addTab("Disabled Accounts", dis_accounts);
		tabpane.addTab("Create Account", create_account);
		tabpane.addTab("Log History", logs);
	}
	private void genAuditorView(SpringLayout layout){
		JPanel home = new JPanel(layout);
		JPanel logs = new JPanel(layout);
		JButton logoutB = new JButton("Log out");
		logoutB.addActionListener(this);
		logoutB.setLocation(home.getWidth() - logoutB.getWidth(), home.getHeight() - logoutB.getHeight());
		
		home.setLayout(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = 0;
		c2.gridy = 0;
		home.add(new JLabel("Welcome"), c2);
		c2.gridy++;
		home.add(new JLabel("First name:"), c2);
		c2.gridx++;
		home.add(new JLabel(acc.getFirstname()), c2);
		c2.gridx = 0;
		c2.gridy++;
		home.add(new JLabel("Last name:"), c2);
		c2.gridx++;
		home.add(new JLabel(acc.getLastname()), c2);
		c2.gridx = 0;
		c2.gridy++;
		home.add(logoutB, c2);
		logs.add(new LogP(this));
		tabpane.addTab("Home", home);
		tabpane.addTab("Log History", logs);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		if(cmd.equals("Log out")){
			C.update("users", "isactive", "0", "username", acc.getUsername());
			applet.enterPanel(0);
			Launcher.panels.remove(1);
			Log.log("[Logged Out]", acc.getUsername());
		}else if(cmd.equals("Create Test")){
			new TestMakerP(applet, this);
		}
	}
}
