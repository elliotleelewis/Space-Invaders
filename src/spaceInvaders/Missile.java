package spaceInvaders;
import java.util.*;
import processing.core.*;
public class Missile extends Entity
{
	// TYPE:	Game Entity
	// DESC:	Can be fired by the user. If it hits a wall then the wall takes damage and the missile is destroyed. If it hits an alien then
	//			the alien is killed and the missile is destroyed.
	static float speed = 10f;
	public Missile(PApplet parent, Game game, float x, float y)
	{
		super(parent, game, x, y);
	}
	protected void move()
	{
		if(y - speed <= 0)
		{
			game.destroyMissile();
		}
		ArrayList<Alien> aliens = game.aliens;
		for(int i = 0; i < aliens.size(); i++)
		{
			Alien alien = aliens.get(i);
			if(alienCollision(alien, i))
			{
				switch(alien.type)
				{
					case 0:
						game.addScore(30);
						break;
					case 1:
						game.addScore(20);
						break;
					case 2:
						game.addScore(10);
						break;
				}
				game.destroyAlien(i);
				game.destroyMissile();
				return;
			}
		}
		ArrayList<Wall> walls = game.walls;
		for(int i = 0; i < walls.size(); i++)
		{
			Wall wall = walls.get(i);
			if(wallCollision(wall, i))
			{
				game.damageWall(i, Math.round(x));
				game.destroyMissile();
				return;
			}
		}
		if(superAlienCollision())
		{
			game.destroySuperAlien();
			game.destroyMissile();
			game.addScore(100);
		}
		y -= speed;
	}
	public void render()
	{
		parent.fill(255, 0, 0);
		parent.rect(x, y, 2, 8);
		parent.fill(255);
	}
	private boolean alienCollision(Alien alien, int i)
	{
		boolean out = true;
		if(alien.x + (alien.texture.width / 2) < x - 1)
		{
			out = false;
		}
		else if(alien.x - (alien.texture.width / 2) > x + 1)
		{
			out = false;
		}
		else if(alien.y + (alien.texture.height / 2) < y - 4)
		{
			out = false;
		}
		else if(alien.y - (alien.texture.height / 2) > y + 4)
		{
			out = false;
		}
		return out;
	}
	private boolean superAlienCollision()
	{
		if(game.superAlien == null)
		{
			return false;
		}
		SuperAlien superAlien = game.superAlien;
		boolean out = true;
		if(superAlien.x + (superAlien.texture.width / 2) < x - 1)
		{
			out = false;
		}
		else if(superAlien.x - (superAlien.texture.width / 2) > x + 1)
		{
			out = false;
		}
		else if(superAlien.y + (superAlien.texture.height / 2) < y - 4)
		{
			out = false;
		}
		else if(superAlien.y - (superAlien.texture.height / 2) > y + 4)
		{
			out = false;
		}
		return out;
	}
	private boolean wallCollision(Wall wall, int i)
	{
		if(wall.y + 30 > y + 4 && wall.y - 30 < y - 4)
		{
			if(wall.x - (Wall.textureWidth / 2) <= x - 1 && wall.x - (Wall.textureWidth / 4) >= x + 1 && !wall.destroyed.get(0))
			{
				return true;
			}
			if(wall.x - (Wall.textureWidth / 4) <= x - 1 && wall.x >= x + 1 && !wall.destroyed.get(1))
			{
				return true;
			}
			if(wall.x <= x - 1 && wall.x + (Wall.textureWidth / 4) >= x + 1 && !wall.destroyed.get(2))
			{
				return true;
			}
			if(wall.x + (Wall.textureWidth / 4) <= x - 1 && wall.x + (Wall.textureWidth / 2) >= x + 1 && !wall.destroyed.get(3))
			{
				return true;
			}
		}
		return false;
	}
}