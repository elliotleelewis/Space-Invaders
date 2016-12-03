package spaceInvaders;
import processing.core.*;
public class Button
{
	// TYPE:	UI Element
	// DESC:	Simple clickable button class. Can be created at a position and with a certain size containing text.
	PApplet parent;
	Game game;
	String text;
	int x, y, width, height;
	private boolean disabled;
	public Button(PApplet parent, Game game, String text, int x, int y, int width, int height)
	{
		this.parent = parent;
		this.game = game;
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public void render()
	{
		parent.strokeWeight(2);
		parent.textAlign(PApplet.CENTER, PApplet.CENTER);
		parent.textFont(Game.titleFont);
		if(disabled)
		{
			parent.fill(100);
			parent.stroke(75);
			parent.textSize(18);
		}
		else if(mouseOver())
		{
			parent.fill(180);
			parent.stroke(155);
			parent.textSize(20);
		}
		else
		{
			parent.fill(160);
			parent.stroke(135);
			parent.textSize(18);
		}
		parent.rect(x, y, width, height, ((width + height) / 2) / 5);
		parent.noStroke();
		if(disabled)
		{
			parent.fill(155);
		}
		else
		{
			parent.fill(255);
		}
		parent.text(text, x, y);
		parent.textFont(Game.defaultFont);
		parent.fill(255);
	}
	public void setDisabled()
	{
		disabled = true;
	}
	public void setEnabled()
	{
		disabled = false;
	}
	public boolean getDisabled()
	{
		return disabled;
	}
	public boolean clicked()
	{
		return mouseOver() && parent.mousePressed && game.buttonReleased;
	}
	private boolean mouseOver()
	{
		if(disabled)
		{
			return false;
		}
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		if(mX > x + width / 2 || mX < x - width / 2 || mY > y + height / 2 || mY < y - height / 2)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}