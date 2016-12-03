package spaceInvaders;
import processing.core.*;
public abstract class Entity
{
	PApplet parent;
	Game game;
	float x, y;
	public Entity(PApplet parent, Game game, float x, float y)
	{
		this.parent = parent;
		this.game = game;
		this.x = x;
		this.y = y;
	}
	public void update()
	{
		move();
		render();
	}
	protected abstract void move();
	public abstract void render();
}