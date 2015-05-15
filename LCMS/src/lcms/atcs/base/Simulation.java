package lcms.atcs.base;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Calendar;

import javax.swing.JOptionPane;

import lcms.atcs.graphics.EntityAirport;
import lcms.atcs.graphics.EntityFlight;
import lcms.atcs.interact.TextField;
import lcms.base.C;
import lcms.base.Launcher;
import lcms.base.Log;
import lcms.base.Simulator;

public class Simulation {
	private Simulator simr;
	private TextField commandField;
	private EntityAirport airport;
	
	public Simulation(Simulator simr){
		this.simr = simr;
		airport = new EntityAirport(new Point(300,300), this);
		commandField = new TextField(new Point(40, 550), new Dimension(500, 20));
	}
	public void render(Graphics2D g){
		airport.render(g);
		g.drawString("ATC Command Line", 40, 540);
		commandField.render(g);
	}
	public void update(float time_s){
		airport.update(time_s);
		commandField.update();
		checkKeys();

	}
	
	public void checkKeys(){
		for(String key: KB.key.keySet())
		{
			boolean down = KB.key.get(key);
			if(down)
			{
				if(key.equalsIgnoreCase("enter"))
				{
					String[] args = commandField.getText().split(" ");
					if(args[0].equalsIgnoreCase("f")){
						EntityFlight flight = airport.getFlight(args[1].toUpperCase());
						if(flight != null){
							if(args[2].equalsIgnoreCase("d")){
								flight.setDegree(Integer.parseInt(args[3]));
							}
							else if(args[2].equalsIgnoreCase("s")){
								flight.setSpeed(Integer.parseInt(args[3]));
							}else if(args[2].equalsIgnoreCase("a")){
								flight.setAlt(Integer.parseInt(args[3]));
							}
						}						
					}else if (args[0].equalsIgnoreCase("exit") && simr.practice){						
						String time = Calendar.getInstance().getTime().toString();
						time = time.substring(0, time.lastIndexOf(" ")).substring(0, time.lastIndexOf(" "));
						C.insert("scores", new String[]{"Quit", simr.menu.acc.getUsername(), time,"" + 0, "Practice Simulation"});	
						Log.log("[Simulation Taken - Quit]", simr.menu.acc.getUsername());
						JOptionPane.showMessageDialog(simr, "You have quit the simulation.");
						simr.menu.applet.enterPanel(simr.menu);
						Launcher.panels.remove(this);
					}
					else if(args[0].equalsIgnoreCase("exit")){
						String time = Calendar.getInstance().getTime().toString();
						time = time.substring(0, time.lastIndexOf(" ")).substring(0, time.lastIndexOf(" "));
						C.insert("scores", new String[]{"Quit", simr.menu.acc.getUsername(), time,"" + 0, "Practice Simulation"});	
						Log.log("[Simulation Taken - Quit]", simr.menu.acc.getUsername());
						JOptionPane.showMessageDialog(simr, "You have quit the simulation.");
						simr.menu.applet.enterPanel(simr.menu);
						Launcher.panels.remove(this);
					}
					commandField.setText("");
				}
				KB.key.put(key, false);
			}
		}	
	}
	public Simulator getSim(){
		return simr;
	}
}
