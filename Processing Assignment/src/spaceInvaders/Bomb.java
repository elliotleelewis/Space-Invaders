package spaceInvaders;
import java.util.*;
import processing.core.*;
public class Bomb
{
	// TYPE:	Game Entity
	// DESC:	These are dropped from the aliens. If they hit a wall, the wall is damaged and the bomb is destroyed. If they hit a player then
	//			the player loses a life and the game resumes.
	PApplet parent;
	Game game;
	float x, y;
	static float speed = 8f;
	public Bomb(PApplet parent, Game game, float x, float y)
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
	private void move()
	{
		if(y + speed > parent.height)
		{
			game.destroyBomb();
		}
		if(defenderCollision())
		{
			game.destroyDefender();
			game.destroyBomb();
		}
		ArrayList<Wall> walls = game.walls;
		for(int i = 0; i < walls.size(); i++)
		{
			if(wallCollision(walls.get(i), i))
			{
				game.damageWall(i, (int) x);
				game.destroyBomb();
			}
		}
		y += speed;
	}
	public void render()
	{
		parent.fill(255);
		parent.rect(x, y, 4, 4);
	}
	private boolean defenderCollision()
	{
		boolean out = true;
		int dX = game.defender.x;
		int dY = game.defender.y;
		int dWidth = Game.defenderI.width;
		int dHeight = Game.defenderI.height;
		if(x - 2 < dX - dWidth)
		{
			out = false;
		}
		else if(x + 2 > dX + dWidth)
		{
			out = false;
		}
		else if(y - 2 < dY - dHeight)
		{
			out = false;
		}
		else if(y + 2 > dY + dHeight)
		{
			out = false;
		}
		return out;
	}
	private boolean wallCollision(Wall wall, int i)
	{
		if(wall.y + 30 > y + 4 && wall.y - 30 < y - 4)
		{
			if(wall.x - (Wall.textureWidth / 2) <= x - 2 && wall.x - (Wall.textureWidth / 4) >= x + 2 && !wall.destroyed.get(0))
			{
				return true;
			}
			if(wall.x - (Wall.textureWidth / 4) <= x - 2 && wall.x >= x + 2 && !wall.destroyed.get(1))
			{
				return true;
			}
			if(wall.x <= x - 2 && wall.x + (Wall.textureWidth / 4) >= x + 2 && !wall.destroyed.get(2))
			{
				return true;
			}
			if(wall.x + (Wall.textureWidth / 4) <= x - 2 && wall.x + (Wall.textureWidth / 2) >= x + 2 && !wall.destroyed.get(3))
			{
				return true;
			}
		}
		return false;
	}
}