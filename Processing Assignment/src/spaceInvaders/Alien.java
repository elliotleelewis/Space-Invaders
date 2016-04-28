package spaceInvaders;
import processing.core.*;
import java.util.*;
public class Alien
{
	// TYPE:	Game Entity
	// DESC:	Can be shot by missiles. If they have no aliens directly below them, they will drop bombs at the player.
	Random random = new Random();
	PApplet parent;
	Game game;
	PImage texture;
	float x, y;
	int type, position;
	static float speed = 1f;
	public Alien(PApplet parent, Game game, int type, int position, float y)
	{
		this.parent = parent;
		this.game = game;
		this.type = type;
		this.position = position;
		x = game.alienX + (position * 30);
		this.y = y;
		switch(type)
		{
			case 0:
				texture = Game.alienA1I;
				break;
			case 1:
				texture = Game.alienB1I;
				break;
			case 2:
				texture = Game.alienC1I;
				break;
		}
	}
	public void update()
	{
		move();
		render();
		if(isBottom() && random.nextInt(1000) == 1 && game.bomb == null)
		{
			dropBomb();
		}
	}
	private void move()
	{
		if(x + 12 >= parent.width || x - 12 <= 0 && isBottom())
		{
			game.swapAlienDirection();
		}
		if(y + texture.height >= parent.height - 125)
		{
			game.destroyDefender();
		}
		x = game.alienX + (position * 30);
	}
	public void render()
	{
		if(!Game.paused)
		{
			switch(type)
			{
				case 0:
					if(Game.frameCount % 50 <= 25)
					{
						texture = Game.alienA1I;
					}
					else
					{
						texture = Game.alienA2I;
					}
					break;
				case 1:
					if(Game.frameCount % 50 <= 25)
					{
						texture = Game.alienB1I;
					}
					else
					{
						texture = Game.alienB2I;
					}
					break;
				case 2:
					if(Game.frameCount % 50 <= 25)
					{
						texture = Game.alienC1I;
					}
					else
					{
						texture = Game.alienC2I;
					}
					break;
			}
		}
		parent.image(texture, x, y);
	}
	public void shiftDown()
	{
		y += 10;
	}
	public boolean isBottom()
	{
		boolean out = true;
		ArrayList<Alien> aliens = game.aliens;
		for(int i = 0; i < aliens.size(); i++)
		{
			float aX = aliens.get(i).x;
			float aY = aliens.get(i).y;
			if(aY >= y && aX < x && aX + aliens.get(i).texture.width > x)
			{
				return false;
			}
		}
		return out;
	}
	private void dropBomb()
	{
		game.bomb = new Bomb(parent, game, x, y);
	}
}