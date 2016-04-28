package spaceInvaders;
import processing.core.*;
public class Sign
{
	// TYPE:	UI Element
	// DESC:	Popup box in game that notifies the player of the text assigned to it.
	PApplet parent;
	Game game;
	String text;
	int x, y, width, height;
	public Sign(PApplet parent, Game game, String text, int x, int y, int width, int height)
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
		parent.fill(160);
		parent.stroke(135);
		parent.textSize(18);
		parent.rect(x, y, width, height, ((width + height) / 2) / 5);
		parent.noStroke();
		parent.fill(255);
		parent.text(text, x, y);
		parent.textFont(Game.defaultFont);
		parent.fill(255);
	}
}