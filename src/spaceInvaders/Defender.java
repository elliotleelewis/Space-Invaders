package spaceInvaders;
import processing.core.*;
public class Defender extends Entity
{
	// TYPE:	Game Entity
	// DESC:	This is what the player controls. It can move left and right and also shoot missiles.
	int speed = 8;
	public Defender(PApplet parent, Game game)
	{
		super(parent, game, parent.width / 2, parent.height - 40);
	}
	protected void move()
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