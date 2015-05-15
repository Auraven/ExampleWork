package lcms.base;

import java.util.ArrayList;

import javax.swing.JApplet;
import javax.swing.JPanel;

import lcms.atcs.base.KB;
import lcms.panels.LoginP;

@SuppressWarnings("serial")
public class Launcher extends JApplet{
	public static ArrayList<JPanel> panels;
	private static int currentPanel;
	
	public static void main(String[] args)
	{
	
	}
	
	@Override
	public void init(){
		super.init();
		//Setup the Applet Window
		setSize(800,600);
		addKeyListener(new KB());
		this.setFocusable(true);
		
		if(C.connect()){			
			//Create
			panels = new ArrayList<JPanel>();
			panels.add(new LoginP(this));
			enterPanel(0);
		}else{
			panels = new ArrayList<JPanel>();
			panels.add(new LoginP(this));
			enterPanel(0);
		}
		
	}	
	
	/**
	 * Enter a panel to interact with
	 * @param id The panel to switch to in the panels ArrayList
	 */
	public void enterPanel(int id){
		panels.get(currentPanel).setVisible(false);
		this.remove(panels.get(currentPanel));
		currentPanel = id;
		this.add(panels.get(currentPanel));
		panels.get(currentPanel).setVisible(true);
	}
	public void enterPanel(JPanel p){
		panels.get(currentPanel).setVisible(false);
		this.remove(panels.get(currentPanel));
		for(int i = 0; i < panels.size(); i++){
			if(panels.get(i).equals(p)){
				currentPanel = i;
				break;
			}
		}
		this.add(panels.get(currentPanel));
		panels.get(currentPanel).setVisible(true);
	}
}
