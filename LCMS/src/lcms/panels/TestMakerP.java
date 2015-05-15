package lcms.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import lcms.base.Launcher;
import lcms.base.Log;
import lcms.base.PracticeTest;
import lcms.base.Question;

public class TestMakerP extends JPanel implements ActionListener{
	private MenuP menu;
	private Launcher applet;
	private JRadioButton cab, cbb, ccb;
	private JTextField queF, anaF, anbF, ancF, tnF;
	private PracticeTest pTest;
	public TestMakerP(Launcher applet, MenuP menu){
		this.menu = menu; 
		this.applet = applet;
		Launcher.panels.add(this);
		applet.enterPanel(this);
		setSize(menu.getWidth(), menu.getHeight());
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		pTest = new PracticeTest();
		JLabel tl = new JLabel("Practice Test Creation");
		JLabel tnl = new JLabel("Test Name");
		JLabel anl = new JLabel("Add Question");
		JLabel aal = new JLabel("Answer A");
		JLabel abl = new JLabel("Answer B");
		JLabel acl = new JLabel("Answer C");
		queF = new JTextField(30);
		tnF = new JTextField(30);
		anaF = new JTextField(30);
		anbF = new JTextField(30);
		ancF = new JTextField(30);
		cab = new JRadioButton("A");
		cab.addActionListener(this);
		cbb = new JRadioButton("B");
		cbb.addActionListener(this);		
		ccb = new JRadioButton("C");
		ccb.addActionListener(this);
		JButton addB = new JButton("Add Question");
		addB.addActionListener(this);
		JButton finB = new JButton("Finish");
		finB.addActionListener(this);
		JButton cancelB = new JButton("Cancel");
		cancelB.addActionListener(this);
		
		c.gridx = 0;
		c.gridy = 0;
		add(tl, c);
		c.gridy++;
		add(tnl, c);
		c.gridx++;
		add(tnF, c);
		c.gridx = 0;
		c.gridy++;
		add(anl, c);
		c.gridx++;
		add(queF, c);
		c.gridx = 0;
		c.gridy++;
		add(aal, c);
		c.gridx++;
		add(anaF, c);
		c.gridx++;
		add(cab, c);
		c.gridx = 0;
		c.gridy++;
		add(abl, c);
		c.gridx++;
		add(anbF, c);
		c.gridx++;
		add(cbb, c);
		c.gridx = 0;
		c.gridy++;
		add(acl, c);
		c.gridx++;
		add(ancF, c);
		c.gridx++;
		add(ccb, c);
		c.gridx--;
		c.gridy++;
		add(addB, c);
		c.gridy++;
		add(finB, c);
		c.gridy++;
		add(cancelB, c);
	}
	public void clearText(){
		queF.setText("");
		anaF.setText("");
		anbF.setText("");
		ancF.setText("");
		if(cab.isSelected()){
			cab.setSelected(false);
		}
		if(cbb.isSelected()){
			cbb.setSelected(false);
		}
		if(ccb.isSelected()){
			ccb.setSelected(false);
		}
	}
	@Override
	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		
		if(cmd.equals("Add Question")){
			if(tnF.getText().length() > 3){	
				pTest.setName(tnF.getText());
				if(queF.getText().length() > 3){
					if(anaF.getText().length() > 0 && anbF.getText().length() > 0 && ancF.getText().length() > 0){
						if(cab.isSelected()){
							pTest.addQuestion(new Question(queF.getText(), anaF.getText(), anbF.getText(), ancF.getText(), anaF.getText()));
							JOptionPane.showMessageDialog(this, "Question Added! Add more or Create the Test");
							clearText();
						}
						else if(cbb.isSelected()){
							pTest.addQuestion(new Question(queF.getText(), anaF.getText(), anbF.getText(), ancF.getText(), anbF.getText()));
							JOptionPane.showMessageDialog(this, "Question Added! Add more or Create the Test");
							clearText();
						}
						else if(ccb.isSelected()){
							pTest.addQuestion(new Question(queF.getText(), anaF.getText(), anbF.getText(), ancF.getText(), ancF.getText()));
							JOptionPane.showMessageDialog(this, "Question Added! Add more or Create the Test");
							clearText();
						}
						else{
							JOptionPane.showMessageDialog(this, "Please Mark a correct answer with the radio buttons!");
						}	
					}else{
						JOptionPane.showMessageDialog(this, "Please enter longer answers!");
					}									
				}else{
					JOptionPane.showMessageDialog(this, "Please enter a Longer question name!");
				}				
			}else{
				JOptionPane.showMessageDialog(this, "Please enter a Longer test name!");
			}			
		}
		else if(cmd.equals("A")){
			if(cbb.isSelected()){
				cbb.setSelected(false);
			}
			if(ccb.isSelected()){
				ccb.setSelected(false);
			}
		}else if(cmd.equals("B")){
			if(cab.isSelected()){
				cab.setSelected(false);
			}
			if(ccb.isSelected()){
				ccb.setSelected(false);
			}
		}else if(cmd.equals("C")){
			if(cab.isSelected()){
				cab.setSelected(false);
			}
			if(cbb.isSelected()){
				cbb.setSelected(false);
			}
		}else if(cmd.equals("Finish")){			
			File testFolder = new File("test");
			if(!testFolder.exists()){
				testFolder.mkdir();
			}
			File test = new File("test/" + pTest.name + ".txt");
			if(!test.exists()){
				try {
					test.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				PrintWriter fileOut = new PrintWriter(test);
				fileOut.println(pTest.name);
				for(Question q: pTest.questions){
					fileOut.println(q.question + "," + q.answerA + "," + q.answerB + "," + q.answerC + "," + q.correctanswer);
				}
				fileOut.close();
				JOptionPane.showMessageDialog(this, "Test Created!");
				Log.log("[Test Created]", menu.acc.getUsername());
				applet.enterPanel(menu);
				Launcher.panels.remove(this);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Something went wrong! Contact an administrator. \nTest Creation Cancelled");
				applet.enterPanel(menu);
				Launcher.panels.remove(this);				
			}			
		}else if(cmd.equals("Cancel")){
			applet.enterPanel(menu);
			Launcher.panels.remove(this);			
			JOptionPane.showMessageDialog(this, "Test Creation Cancelled.");
		}
	}
}
