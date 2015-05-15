package lcms.atcs.graphics;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Hashtable;

import lcms.atcs.base.Simulation;

public class EntityAirport extends Entity{
	private Hashtable<String, EntityFlight> flights;
	private Simulation l;
	//private EntityFlight selectedFlight;
	
	public EntityAirport(Point pos, Simulation l){
		super(pos);
		this.l = l;
		flights = new Hashtable<String, EntityFlight>();
	}
	public void render(Graphics2D g){
		g.draw(shape);
		
		int i = 0;
		for(String key: flights.keySet()){
			EntityFlight flight = flights.get(key);
			flight.render(g);
			int offY = (i*50);
			g.drawRect(650, offY, 150, 50);
			g.drawString("F: " + flight.getName(), 654, offY + 12);
			g.drawString("Status: " + flight.getStatus(), 654, offY + 12*2);
			g.drawString("A: " + flight.getAlt(), 654, offY + 12*3);
			g.drawString("D: " + flight.getDegree() + "  S: " + flight.getSpeed(), 654, offY + 12*4);
			i++;
		}
	}
	public void update(float time_s){
		if(flights.size() < l.getSim().time_passed/20){
			spawnFlight();
		}else if(flights.size() == 0){
			spawnFlight();
		}
		
		for(int i = 0; i < flights.keySet().size(); i++){
			String key = (String)flights.keySet().toArray()[i];
			EntityFlight flight = flights.get(key);
			flight.update(time_s);
			if(flight.isAlive){
				for(int j = 0; j < flights.keySet().size(); j++){
					String key2 = (String)flights.keySet().toArray()[j];				
					if(!key.equals(key2)){
						EntityFlight flight2 = flights.get(key2);
						double d = flight.distanceTo(flight2);
						double a = flight.getAltDif(flight2);
						//System.out.println(d);
						if(d < 10 && a < 200){
							flight.kill();
							flight2.kill();
							flight.setAlert(3);
							l.getSim().grade -= 20;
							l.getSim().fail();
							break;
						}
						else if(d < 18 && a < 800){
							flight.setAlert(3);
							l.getSim().grade -= 0.01;
							break;
						}
						else if(d < 28 && a < 1200){
							flight.setAlert(2);
							l.getSim().grade -= 0.002;
							break;
						}
						else if(d < 38 && a < 1800){
							flight.setAlert(1);
							l.getSim().grade -= 0.0004;
							break;
						}
						else{
							flight.setAlert(0);
						}
					}	
				}
			}else{
				flights.remove(key);
				i--;
			}			
		}		
	}	
	public void spawnFlight(){
		int tag = 0;
		do{
			tag = (int)(Math.random()*999);
		}while(tag < 100);
		double side = Math.random()* 1000;
		Point pos = new Point();
		if(side < 250){//North
			pos.x = (int)(Math.random()*800);
			pos.y = -10;
			flights.put("EDI" + tag, new EntityFlight("EDI" + tag, pos, this));
		}else if(side < 500){//East
			pos.x = 810;
			pos.y = (int)(Math.random()*600);
			flights.put("GTY" + tag, new EntityFlight("GTY" + tag, pos, this));
		}else if(side < 750){//South
			pos.x = (int)(Math.random()*800);
			pos.y = 610;
			flights.put("KUR" + tag, new EntityFlight("KUR" + tag, pos, this));
		}else{//West
			pos.x = -10;
			pos.y = (int)(Math.random()*600);
			flights.put("TPG" + tag, new EntityFlight("TPG" + tag, pos, this));
		}			
	}
	@Override
	protected void buildShape() {
		shape = new Polygon();
		shape.addPoint(pos.x-3, pos.y-3);
		shape.addPoint(pos.x+3, pos.y-3);
		shape.addPoint(pos.x+3, pos.y+3);
		shape.addPoint(pos.x-3, pos.y+3);
	}
	
	public EntityFlight getFlight(String name){
		return flights.get(name);
	}
	public Hashtable<String, EntityFlight> getFlightTable(){
		return flights;
	}
}
