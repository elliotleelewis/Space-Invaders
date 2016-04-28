package spaceInvaders;
import processing.core.*;
public class Star extends Entity
{
	// TYPE:	Game Element
	// DESC:	Floating randomly moving stars that are shown in the background of the game.
	float minDY, maxDY, dY;
	boolean big;
	public Star(PApplet parent, Game game, float minDY, float maxDY)
	{
		super(parent, game, Game.random.nextInt(parent.width), Game.random.nextInt(parent.height));
		this.minDY = minDY;
		this.maxDY = maxDY;
		generate(true);
	}
	protected void move()
	{
		if(y + dY > parent.height)
		{
			generate(false);
		}
		y += dY;
	}
	public void render()
	{
		if(big)
		{
			parent.rect(x, y, 6, 2);
			parent.rect(x, y, 2, 6);
		}
		else
		{
			parent.rect(x, y, 2, 2);
		}
	}
	private void generate(boolean firstTime)
	{
		float deltaDX = maxDY - minDY;
		dY = ((float) Game.random.nextInt((int) (deltaDX * 100)) / 100f) + minDY;
		x = Game.random.nextInt(parent.width);
		if(firstTime)
		{
			y = Game.random.nextInt(parent.height);
		}
		else
		{
			y = 0;
		}
		if(Game.random.nextInt(10) == 1)
		{
			big = true;
		}
		else
		{
			big = false;
		}
	}
}