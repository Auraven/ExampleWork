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
import javax.swing.JPanel;

import lcms.accounts.Account;
import lcms.base.Launcher;
import lcms.base.PracticeTest;
import lcms.base.Question;
import lcms.base.Simulator;

public class PracticeP extends JPanel implements ActionListener{
	private Account acc;
	private MenuP menu;
	private JComboBox<String> testbox;
	private ArrayList<PracticeTest> practicetests;
	public PracticeP(MenuP menu, Account acc){
		this.acc = acc;
		this.menu = menu;
		loadTests();
		setSize(menu.getWidth(), menu.getHeight());
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JButton pSim = new JButton("Simulator Practice");
		pSim.addActionListener(this);
		JButton tTest = new JButton("Take Practice Test");
		tTest.addActionListener(this);
		c.gridx = 0;
		c.gridy = 0;
		add(testbox, c);
		c.gridx++;
		add(tTest, c);
		c.gridy++;
		add(pSim, c);
	}
	public void loadTests(){
		testbox = new JComboBox<String>();
		practicetests = new ArrayList<PracticeTest>();
		File testFolder = new File("test");
		if(!testFolder.exists()){
			testbox.addItem("No Tests Available");
			testbox.setSelectedItem("No Tests Available");
			testbox.setEnabled(false);
		}else{
			for(File testFile: testFolder.listFiles()){
				try {
					Scanner fileIn = new Scanner(testFile);
					PracticeTest pt = new PracticeTest();
					pt.setName(fileIn.nextLine());
					while(fileIn.hasNextLine()){
						String[] args = fileIn.nextLine().split(",");
						pt.addQuestion(new Question(args[0],args[1],args[2],args[3],args[4]));
					}
					practicetests.add(pt);
					testbox.addItem(pt.name);
					fileIn.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			if(practicetests.isEmpty()){
				testbox.addItem("No Tests Available");
				testbox.setSelectedItem("No Tests Available");
				testbox.setEnabled(false);
			}
		}
	}
	@Override
	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		if(cmd.equals("Simulator Practice")){
			Launcher.panels.add(new Simulator(menu.applet, menu, true));
			menu.applet.enterPanel(2);
		}
		else if(cmd.equals("Take Practice Test")){
			for(PracticeTest p: practicetests){
				if(p.name.equals((String)testbox.getSelectedItem())){
					new PracticeTestP(menu.applet, menu, p);
					break;
				}
			}		
		}
	}
}
