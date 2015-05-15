package lcms.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lcms.accounts.Account;
import lcms.base.Launcher;
import lcms.base.PracticeTest;
import lcms.base.Question;
import lcms.base.Simulator;

public class ActualP extends JPanel implements ActionListener{
	private Account acc;
	private MenuP menu;
	public ActualP(MenuP menu){
		this.acc = menu.acc;
		this.menu = menu;
		setSize(menu.getWidth(), menu.getHeight());
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JButton pSim = new JButton("Actual Simulator Test");
		pSim.addActionListener(this);		
		c.gridx = 0;
		c.gridy = 0;
		add(new JLabel("If you fail then good bye"), c);
		c.gridy++;
		add(pSim, c);		
	}	
	@Override
	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		if(cmd.equals("Actual Simulator Test")){
			Launcher.panels.add(new Simulator(menu.applet, menu, false));
			menu.applet.enterPanel(2);
		}		
	}
}
