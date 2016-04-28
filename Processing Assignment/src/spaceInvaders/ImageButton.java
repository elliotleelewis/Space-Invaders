package spaceInvaders;
import processing.core.*;
public class ImageButton
{
	// TYPE:	UI Element
	// DESC:	Similar to a button but in this case, a clickable image is displayed instead.
	PApplet parent;
	Game game;
	PImage texture, hoverTexture;
	int x, y, width, height;
	boolean mouseOver;
	public ImageButton(PApplet parent, Game game, PImage texture, PImage hoverTexture, int x, int y)
	{
		this.parent = parent;
		this.game = game;
		this.texture = texture;
		this.hoverTexture = hoverTexture;
		this.x = x;
		this.y = y;
	}
	public void render()
	{
		if(mouseOver())
		{
			parent.image(hoverTexture, x, y);
		}
		else
		{
			parent.image(texture, x, y);
		}
	}
	public boolean clicked()
	{
		return mouseOver() && parent.mousePressed && game.buttonReleased;
	}
	private boolean mouseOver()
	{
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		if(mX > x + (texture.width / 2) || mX < x - (texture.width / 2) || mY > y + (texture.height / 2) || mY < y - (texture.height / 2))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}