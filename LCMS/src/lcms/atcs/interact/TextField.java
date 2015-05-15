package lcms.atcs.interact;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import lcms.atcs.base.KB;

public class TextField {
	private Point pos;
	private Dimension size;
	private Color b, f;
	private String text;
	private boolean d;
	
	public TextField(Point pos, Dimension size){
		this.pos = pos;
		this.size = size;
		this.b = Color.white;
		text = "";
	}
	
	public void render(Graphics2D g){
		Color t = g.getColor();
		g.setColor(b);
		g.drawRect(pos.x, pos.y, size.width, size.height);
		g.drawString(text, pos.x + 4, pos.y + 14);
		g.setColor(t);
	}
	public void update(){
		for(String key: KB.key.keySet())
		{
			boolean down = KB.key.get(key);
			String k = key.toLowerCase();
			if(down)
			{
				if(key.equalsIgnoreCase("backspace") && !text.isEmpty())
				{
					text = text.substring(0, text.length() - 1);
					KB.key.put(key, false);
				}
				else if(key.equalsIgnoreCase("space"))
				{
					text += " ";
					KB.key.put(key, false);
				}
				else if(k.length() == 1)
				{
					text += k;
					KB.key.put(key, false);
				}
			}
		}				
	}
	public String getText(){
		return text;
	}
	public void setText(String text){
		this.text = text;
	}
}
