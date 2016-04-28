package spaceInvaders;
import java.util.*;
import processing.core.*;
public class SuperAlien
{
	// TYPE:	Game Entity
	// DESC:	Red alien that randomly spawns at the top of the game. When shot by a missile it gains bonus points for the player.
	PApplet parent;
	Game game;
	PImage texture = Game.superAlienI;
	Random random = new Random();
	float x;
	int y = 75;
	final int LEFT = -1;
	final int RIGHT = 1;
	int direction = LEFT;
	static float speed = 2f;
	public SuperAlien(PApplet parent, Game game)
	{
		this.parent = parent;
		this.game = game;
		if(random.nextInt(2) == 1)
		{
			direction = RIGHT;
		}
		if(direction == RIGHT)
		{
			x = parent.width + (texture.width / 2);
		}
		else
		{
			x = -(texture.width / 2);
		}
	}
	public void update()
	{
		move();
		render();
	}
	private void move()
	{
		x += (direction * -1) * speed;
	}
	public void render()
	{
		parent.image(texture, x, y);
	}
}