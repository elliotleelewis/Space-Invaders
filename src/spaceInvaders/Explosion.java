package spaceInvaders;
import processing.core.*;
public class Explosion extends Entity
{
	// TYPE:	Game Element	
	// DESC:	Animation for when an alien is shot.
	PImage frame1, frame2;
	int position, creationFrame;
	public Explosion(PApplet parent, Game game, int position, float y)
	{
		super(parent, game, game.alienX + (position * 30), y);
		this.position = position;
		frame1 = Game.explosion1I;
		frame2 = Game.explosion2I;
		creationFrame = Game.frameCount;
	}
	protected void move()
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