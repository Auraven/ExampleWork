package lcms.atcs.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

public abstract class Entity {
	protected Polygon shape;
	protected Point pos;
	protected Color color;
	protected boolean isAlive;
	public Entity(Point pos){
		this.pos = pos;
		isAlive = true;
		buildShape();
	}
	
	public void render(Graphics2D g){	
		if(shape != null){
			Color t = g.getColor();
			g.setColor(color);
			g.setColor(t);
			g.draw(shape);
		}
	}
	public void update(float time_s){
		if(shape != null && shape.xpoints[0] != pos.x && shape.ypoints[0] != pos.y)
		{
			buildShape();
		}
	}
	
	protected abstract void buildShape();
	public void kill(){
		isAlive = false;
	}
	public void setColor(Color color){
		this.color = color;
	}
	public Color getColor(){
		return color;
	}
	public Polygon getShape(){
		return shape;
	}
}
