package lcms.base;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import lcms.atcs.base.Simulation;
import lcms.panels.MenuP;


public class Simulator extends JPanel implements Runnable{	
	private Simulation sim;
	private float lastTime, curTime;
	public double grade;
	private Polygon viewport, commandport, flightport, auxport;
	public MenuP menu;
	public boolean practice;
	public double time_left, time_passed;
	
	public Simulator(Launcher applet, MenuP menu, boolean practice){
		buildPorts();	
		this.practice = practice;
		this.menu = menu;
		//addKeyListener(new KB());
		this.setSize(applet.getWidth(), applet.getHeight());
		sim = new Simulation(this);
		lastTime = System.currentTimeMillis();
		grade = 100;
		time_left = 60*10;
		time_passed = 0;
		//this.setFocusable(true);
		new Thread(this).start();
	}
	
	
	@Override
	public void paintComponent(Graphics g){		
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON ); //Basic Anti-Aliasing
		render(g2d);
		update();
		repaint();
	}
	private void render(Graphics2D g){		
		renderBackground(g);
		sim.render(g);
	}
	private void update(){
		curTime = System.currentTimeMillis();
		sim.update((float)(curTime-lastTime)/1000);
		lastTime = curTime;
		if(grade < 70){					
			fail();								
		}
	}
	public void fail(){
		String time = Calendar.getInstance().getTime().toString();
		time = time.substring(0, time.lastIndexOf(" ")).substring(0, time.lastIndexOf(" "));
		if(practice){
			C.insert("scores", new String[]{""+grade, menu.acc.getUsername(), time,"" + 0, "Practice Simulation"});	
			Log.log("[Simulation Taken - " + grade + "%]", menu.acc.getUsername());
			JOptionPane.showMessageDialog(this, "You have failed the practice simulation!");
			menu.applet.enterPanel(menu);
			Launcher.panels.remove(this);
		}else{
			C.insert("scores", new String[]{""+grade, menu.acc.getUsername(), time,"" + 0, "Test Simulation"});	
			C.delete("users", "username", menu.acc.getUsername());
			C.insert("disabled_accounts", new String[]{"Student", menu.acc.getUsername(), menu.acc.getLastname(), menu.acc.getAge()});
			Log.log("[Simulation Taken - " + grade + "%]", menu.acc.getUsername());
			JOptionPane.showMessageDialog(this, "You have failed out the class!");
			menu.applet.enterPanel(0);
			Launcher.panels.remove(this);	
		}	
	}
	private void renderBackground(Graphics2D g){
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.white);
		g.draw(viewport);
		g.draw(commandport);
		g.drawString("Current Score: "+ grade + "%", (int)commandport.getBounds2D().getMinX() + 520, (int)commandport.getBounds2D().getMinY() + 20);
		g.drawString("Time left: " + Double.toString(time_left).substring(0, 4), (int)commandport.getBounds2D().getMinX() + 350, (int)commandport.getBounds2D().getMinY() + 20);
		g.draw(flightport);
		g.draw(auxport);	
		g.drawOval((int)auxport.getBounds2D().getMinX() + 40, (int)auxport.getBounds2D().getMinY() + 10, 80, 80);
		g.drawString("0", (int)auxport.getBounds2D().getMinX() + 77, (int)auxport.getBounds2D().getMinY() + 24);
		g.drawString("90", (int)auxport.getBounds2D().getMinX() + 102, (int)auxport.getBounds2D().getMinY() + 56);
		g.drawString("180", (int)auxport.getBounds2D().getMinX() + 70, (int)auxport.getBounds2D().getMinY() + 86);
		g.drawString("270", (int)auxport.getBounds2D().getMinX() + 42, (int)auxport.getBounds2D().getMinY() + 56);
	}
	private void buildPorts(){
		viewport = new Polygon();
		viewport.addPoint(0,0);
		viewport.addPoint(650,0);
		viewport.addPoint(650,500);
		viewport.addPoint(0,500);
		flightport = new Polygon();
		flightport.addPoint(650,0);
		flightport.addPoint(800,0);
		flightport.addPoint(800,600);
		flightport.addPoint(650,600);
		commandport = new Polygon();
		commandport.addPoint(0,500);
		commandport.addPoint(650,500);
		commandport.addPoint(650,600);
		commandport.addPoint(0,600);
		auxport = new Polygon();
		auxport = new Polygon();
		auxport.addPoint(650,500);
		auxport.addPoint(800,500);
		auxport.addPoint(800,600);
		auxport.addPoint(650,600);
	}
	public Image getBufferedImage(){
		return createImage(getWidth(), getHeight());
	}


	@Override
	public void run() {
		while(time_left > 0){
			time_left-=.01;
			try{
				Thread.sleep(10);
			}catch(Exception e){}
			time_passed = 60*10 - time_left;
		}
		String time = Calendar.getInstance().getTime().toString();
		time = time.substring(0, time.lastIndexOf(" ")).substring(0, time.lastIndexOf(" "));
		C.insert("scores", new String[]{""+grade, menu.acc.getUsername(), time,"" + 0, "Practice Simulation"});	
		Log.log("[Simulation Taken - " + grade + "%]", menu.acc.getUsername());
		JOptionPane.showMessageDialog(this, "You have survived the practice simulation!");
		menu.applet.enterPanel(menu);
		Launcher.panels.remove(this);
	}
}
