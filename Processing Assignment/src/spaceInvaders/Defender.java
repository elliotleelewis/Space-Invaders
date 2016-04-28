package spaceInvaders;
import processing.core.*;
public class Defender
{
	// TYPE:	Game Entity
	// DESC:	This is what the player controls. It can move left and right and also shoot missiles.
	PApplet parent;
	int x, y;
	int speed = 8;
	public Defender(PApplet parent)
	{
		this.parent = parent;
		x = parent.width / 2;
		y = parent.height - 40;
	}
	public void update()
	{
		move();
		render();
	}
	private void move()
	{
		if(Game.keys[Game.leftKey] && x - speed >= 0)
		{
			x -= speed;
		}
		if(Game.keys[Game.rightKey] && x + speed <= parent.width)
		{
			x += speed;
		}
	}
	public void render()
	{
		parent.image(Game.defenderI, x, y);
	}
}