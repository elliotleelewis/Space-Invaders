package spaceInvaders;
import processing.core.*;
public class Explosion
{
	// TYPE:	Game Element	
	// DESC:	Animation for when an alien is shot.
	PApplet parent;
	Game game;
	PImage frame1, frame2;
	int position, creationFrame;
	float x, y;
	public Explosion(PApplet parent, Game game, int position, float y)
	{
		this.parent = parent;
		this.game = game;
		this.position = position;
		x = game.alienX + (position * 30);
		this.y = y;
		frame1 = Game.explosion1I;
		frame2 = Game.explosion2I;
		creationFrame = Game.frameCount;
	}
	public void update()
	{
		move();
		render();
	}
	private void move()
	{
		x = game.alienX + (position * 30);
	}
	public void render()
	{
		if(Game.frameCount - creationFrame < Game.framesPerTextureUpdate)
		{
			parent.image(frame1, x, y);
		}
		else if(Game.frameCount - creationFrame < Game.framesPerTextureUpdate * 2)
		{
			parent.image(frame2, x, y);
		}
	}
	public void shiftDown()
	{
		y += 10;
	}
}