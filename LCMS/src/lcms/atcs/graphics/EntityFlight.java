package lcms.atcs.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

public class EntityFlight extends Entity{
	private Point linePos;
	private String id, status;
	private int state, altitude, speed, degree, alert;
	private double absX, absY; //Absolute X and Y positions
	private EntityAirport ap;
	
	public EntityFlight(String id, Point pos, EntityAirport ap){
		super(pos);
		this.ap = ap;
		this.id = id;
		linePos = new Point(pos.x, pos.y);
		absX = pos.x;
		absY = pos.y;		
		speed = 4;
		int variation = (int)(Math.random()*30);
		if(Math.random()*1000 > 500){
			variation *= -1;
		}
		degree = (int)(Math.toDegrees(Math.atan2(ap.pos.getY() - pos.y, ap.pos.getX() - pos.x))) + 90 + variation;
		altitude = 0;
		do{
			altitude = (int)(Math.random()*3000) + 1500;
		}while(altitude < 1500);
		status = "Enroute";
		alert = 0;
	}
	
	@Override
	public void render(Graphics2D g){
		super.render(g);	
		g.setColor(Color.white);
		g.drawOval(pos.x-5, pos.y-5, 10, 10);
		g.drawLine(pos.x, pos.y, linePos.x, linePos.y);
		switch(alert){
			case 0:				
				break;
			case 1:
				g.setColor(Color.YELLOW);
				break;
			case 2:
				g.setColor(Color.ORANGE);
				break;
			case 3:
				g.setColor(Color.RED);
				break;
		}
		if(alert > 0){
			g.drawOval(pos.x-15, pos.y-15, 30, 30);
		}
	}
	@Override
	public void update(float time_s){
		super.update(time_s);
		buildShape();
		if(ap.shape.intersects(shape.getBounds2D())){
			kill();
		}
		//Calculate Velocity
		double dX = (double)(Math.cos(Math.toRadians(degree-90)));
		double dY = (double)(Math.sin(Math.toRadians(degree-90)));
		double vX = dX*(0.001*speed);
		double vY = dY*(0.001*speed);
		absX += vX;
		absY += vY;
		pos.x = (int)absX;
		pos.y = (int)absY;
		linePos.x = (int)(pos.x + dX*30);
		linePos.y = (int)(pos.y + dY*30);		
	}
	public double distanceTo(EntityFlight other){
		return (double)Math.sqrt((double)Math.pow((double)other.pos.x - (double)this.pos.x, 2) + (double)Math.pow((double)other.pos.y - (double)this.pos.y, 2));
	}
	public void setDegree(int degree){
		this.degree = degree;
	}
	public void setSpeed(int speed){
		this.speed = speed;
	}
	public void setAlt(int altitude){
		this.altitude = altitude;
	}
	public void setStatus(String status){
		this.status = status;
	}
	public void setAlert(int alert){
		this.alert = alert;
	}
	public String getName(){
		return id;
	}
	public String getStatus(){
		return status;
	}
	public int getAlt(){
		return altitude;
	}
	public double getAltDif(EntityFlight other){
		return Math.abs(other.getAlt() - altitude);
	}
	public int getDegree(){
		return degree;
	}
	public int getSpeed(){
		return speed;
	}
	@Override
	protected void buildShape() {
		shape = new Polygon();
		shape.addPoint(pos.x - 2, pos.y - 2);
		shape.addPoint(pos.x + 2, pos.y - 2);
		shape.addPoint(pos.x + 2, pos.y + 2);
		shape.addPoint(pos.x - 2, pos.y + 2);
	}	
}
