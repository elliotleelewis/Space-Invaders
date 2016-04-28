package spaceInvaders;
import processing.core.*;
public class Slider
{
	// TYPE:	UI Element
	// DESC:	A value slider that allows the user to control a value of a variable. For example, one of its uses is a volume slider.
	PApplet parent;
	Game game;
	int x, y, width, height;
	private float value;
	public Slider(PApplet parent, Game game, int x, int y, int width, int height, float value)
	{
		this.parent = parent;
		this.game = game;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.value = value;
	}
	public float getValue()
	{
		return value;
	}
	public void update()
	{
		render();
		int mX = parent.mouseX;
		if(clicked() && mX > x - width / 2 && mX < x + width / 2)
		{
			value = (float) (mX - (x - width / 2)) / (float) width;
		}
	}
	private void render()
	{
		parent.strokeWeight(2);
		parent.fill(155);
		parent.stroke(130);
		parent.rect(x, y, width, height);
		parent.fill(180);
		parent.stroke(155);
		parent.rect(x - (width / 2) + (width * value), y, height * 2, height * 2);
		parent.fill(255);
	}
	public boolean clicked()
	{
		return mouseOver() && parent.mousePressed;
	}
	private boolean mouseOver()
	{
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		if(mX < x - width / 2  || mX > x + width / 2 || mY < y - height || mY > y + height)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}