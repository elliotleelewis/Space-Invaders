package spaceInvaders;
import java.util.*;
import processing.core.*;
public class Wall extends Entity
{
	// TYPE:	Game Entity
	// DESC:	Walls are there to defend the player from bombs dropped by the aliens
	int maxHealth;
	static int textureWidth = 60;
	ArrayList<Boolean> destroyed;
	ArrayList<Integer> health;
	ArrayList<PImage> texture;
	public Wall(PApplet parent, Game game, int maxHealth, int x)
	{
		super(parent, game, x, parent.height - 125);
		this.maxHealth = maxHealth;
		destroyed = new ArrayList<Boolean>();
		destroyed.add(Boolean.FALSE);
		destroyed.add(Boolean.FALSE);
		destroyed.add(Boolean.FALSE);
		destroyed.add(Boolean.FALSE);
		health = new ArrayList<Integer>();
		health.add(maxHealth);
		health.add(maxHealth);
		health.add(maxHealth);
		health.add(maxHealth);
		texture = new ArrayList<PImage>();
		texture.add(Game.wallAI);
		texture.add(Game.wallBI);
		texture.add(Game.wallCI);
		texture.add(Game.wallDI);
		texture.add(Game.wallDamAI);
		texture.add(Game.wallDamBI);
		texture.add(Game.wallDamCI);
		texture.add(Game.wallDamDI);
	}
	@Override
	public void update()
	{
		for(int i = 0; i < health.size(); i++)
		{
			if(health.get(i) <= 0)
			{
				destroyed.set(i, Boolean.TRUE);
			}
			if(!destroyed.get(i))
			{
				render(i);
			}
		}
	}
	public void render(int i)
	{
		parent.imageMode(PApplet.CORNER);
		if(i == 0)
		{
			parent.image(health.get(i) > maxHealth / 2 ? texture.get(i) : texture.get(i + 4), x + ((i - 2) * 15), y - (texture.get(i).height / 2));
		}
		else
		{
			parent.image(health.get(i) > maxHealth / 2 ? texture.get(i) : texture.get(i + 4), x + ((i - 2) * 15) - 5, y - (texture.get(i).height / 2));
		}
		parent.imageMode(PApplet.CENTER);
	}
	public void attack(int x)
	{
		int dX = x - Math.round(this.x);
		if(dX < -16)
		{
			health.set(0, health.get(0) - 1);
		}
		else if(dX > 16)
		{
			health.set(3, health.get(3) - 1);
		}
		else if(dX < 0)
		{
			health.set(1, health.get(1) - 1);
		}
		else if(dX > 0)
		{
			health.set(2, health.get(2) - 1);
		}
	}
	protected void move() {}
	public void render() {}
}