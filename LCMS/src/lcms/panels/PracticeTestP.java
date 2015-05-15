package lcms.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import lcms.base.C;
import lcms.base.Launcher;
import lcms.base.Log;
import lcms.base.PracticeTest;

public class PracticeTestP extends JPanel implements ActionListener{
	private Launcher applet;
	private MenuP menu;
	private PracticeTest pt;
	private int index, correct;
	private JLabel quel, anal, anbl, ancl;
	private JRadioButton cab, cbb, ccb;
	private JButton nextB;
	
	public PracticeTestP(Launcher applet, MenuP menu, PracticeTest pt){
		Launcher.panels.add(this);
		applet.enterPanel(this);		
		setSize(menu.getWidth(), menu.getHeight());
		this.menu = menu;
		this.applet = applet;
		this.pt = pt;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		index = 1;
		correct = 0;
		JLabel tnl = new JLabel("Test: " + pt.name);
		nextB = new JButton("Next");
		nextB.addActionListener(this);
		quel = new JLabel("Question " + index);
		anal = new JLabel("Answer A:");
		anbl = new JLabel("Answer B:");
		ancl = new JLabel("Answer C:");
		cab = new JRadioButton("A");
		cab.addActionListener(this);
		cbb = new JRadioButton("B");
		cbb.addActionListener(this);
		ccb = new JRadioButton("C");
		ccb.addActionListener(this);
		
		c.gridx = 0;
		c.gridy = 0;
		
		add(tnl, c);
		c.gridy++;
		add(quel, c);
		c.gridy++;
		add(anal, c);
		c.gridx++;
		add(cab, c);
		c.gridx = 0;
		c.gridy++;
		add(anbl, c);
		c.gridx++;
		add(cbb, c);
		c.gridx = 0;
		c.gridy++;
		add(ancl, c);
		c.gridx++;
		add(ccb, c);
		c.gridx = 0;
		c.gridy++;
		add(nextB, c);
		loadQuestion(index);
	}
	public void loadQuestion(int index){
		this.index = index;
		quel.setText("Question " + index);
		anal.setText(pt.questions.get(index - 1).answerA);
		anbl.setText(pt.questions.get(index - 1).answerB);
		ancl.setText(pt.questions.get(index - 1).answerC);
		cab.setSelected(false);
		cbb.setSelected(false);
		ccb.setSelected(false);
		if(index == pt.questions.size()){
			nextB.setText("Finish");
		}
	}
	@Override
	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		if(cmd.equals("Next")){
			if(cab.isSelected()){
				if(anal.getText().contains(pt.questions.get(index - 1).correctanswer)){
					correct++;
				}
				if(index < pt.questions.size()){
					index++;
					loadQuestion(index);
				}
			}else if(cbb.isSelected()){
				if(anbl.getText().contains(pt.questions.get(index - 1).correctanswer)){
					correct++;
				}
				if(index < pt.questions.size()){
					index++;
					loadQuestion(index);
				}
			}else if(ccb.isSelected()){
				if(ancl.getText().contains(pt.questions.get(index - 1).correctanswer)){
					correct++;
				}
				if(index < pt.questions.size()){
					index++;
					loadQuestion(index);
				}
			}
			else{
				JOptionPane.showMessageDialog(this, "Please select an answer!");
			}
		}else if(cmd.equals("Finish")){
			if(cab.isSelected()){
				if(anal.getText().contains(pt.questions.get(index - 1).correctanswer)){
					correct++;
				}
				JOptionPane.showMessageDialog(this, "You Finished the test with " + correct + "/" + pt.questions.size() + " questions corretly answered.");
				int prc = (correct/pt.questions.size())*100;
				int passed = 0;
				if(prc >= 70){
					passed = 1;
				}
				applet.enterPanel(menu);
				Launcher.panels.remove(this);
				String time = Calendar.getInstance().getTime().toString();
				time = time.substring(0, time.lastIndexOf(" ")).substring(0, time.lastIndexOf(" "));
				C.insert("scores", new String[]{""+prc, menu.acc.getUsername(), time,"" + passed, pt.name});
				Log.log("[Tast Taken - " + correct + "/" + pt.questions.size() + "]", menu.acc.getUsername());				
			}else if(cbb.isSelected()){
				if(anbl.getText().contains(pt.questions.get(index - 1).correctanswer)){
					correct++;
				}
				JOptionPane.showMessageDialog(this, "You Finished the test with " + correct + "/" + pt.questions.size() + " questions corretly answered.");
				int prc = (correct/pt.questions.size())*100;
				int passed = 0;
				if(prc >= 70){
					passed = 1;
				}
				applet.enterPanel(menu);
				Launcher.panels.remove(this);
				String time = Calendar.getInstance().getTime().toString();
				time = time.substring(0, time.lastIndexOf(" ")).substring(0, time.lastIndexOf(" "));
				C.insert("scores", new String[]{""+prc, menu.acc.getUsername(), time,"" + passed, pt.name});				
				Log.log("[Tast Taken - " + correct + "/" + pt.questions.size() + "]", menu.acc.getUsername());
				
			}else if(ccb.isSelected()){
				if(ancl.getText().contains(pt.questions.get(index - 1).correctanswer)){
					correct++;
				}
				JOptionPane.showMessageDialog(this, "You Finished the test with " + correct + "/" + pt.questions.size() + " questions corretly answered.");
				int prc = (correct/pt.questions.size())*100;
				int passed = 0;
				if(prc >= 70){
					passed = 1;
				}
				String time = Calendar.getInstance().getTime().toString();
				time = time.substring(0, time.lastIndexOf(" ")).substring(0, time.lastIndexOf(" "));
				applet.enterPanel(menu);
				Launcher.panels.remove(this);
				C.insert("scores", new String[]{""+prc, menu.acc.getUsername(), time ,"" + passed, pt.name});
				Log.log("[Tast Taken - " + correct + "/" + pt.questions.size() + "]", menu.acc.getUsername());				
			}
			else{
				JOptionPane.showMessageDialog(this, "Please select an answer!");
			}			
		}else if(cmd.equals("A")){
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
		}
	}

}
