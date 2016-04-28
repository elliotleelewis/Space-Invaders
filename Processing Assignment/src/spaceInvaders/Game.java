//	Created by Elliot Lewis
package spaceInvaders;
import java.io.*;
import java.util.*;
import kuusisto.tinysound.*;
import processing.core.*;
import processing.data.*;
enum GameMode
{
	// Enumerated type to store different game modes.
	START, GAME, END, LEADERBOARD, SETTINGS;
}
public class Game extends PApplet
{
	// TYPE:	Main Class
	// DESC:	This is the main class for the Space Invaders game. Most game logic is controlled from here.
	// Input
	public static boolean[] keys;
	// Class Objects
	public static Random random = new Random();
	// Resources
	// Visual
	public static PFont defaultFont, titleFont;
	public static PImage defenderI, alienA1I, alienA2I, alienB1I, alienB2I, alienC1I, alienC2I, explosion1I, explosion2I, superAlienI;
	public static PImage wallAI, wallBI, wallCI, wallDI, wallDamAI, wallDamBI, wallDamCI, wallDamDI;
	public static PImage settingsI, hoverSettingsI;
	// Audio
	public static Music gameThemeS, menuThemeS, superAlienS;
	public static Sound deathS, shootS, killS;
	// Text
	public static Table leaderboard;
	public static XML settings;
	// Game Entities
	ArrayList<Alien> aliens;
	ArrayList<Explosion> explosions;
	ArrayList<Star> stars;
	ArrayList<Wall> walls;
	Bomb bomb;
	Defender defender;
	Missile missile;
	SuperAlien superAlien;
	// Sliders
	Slider musicSlider, soundsSlider;
	// Settings
	// Keys
	public static int leftKey;
	public static int rightKey;
	public static int shootKey;
	// Volume
	public static float musicVolume;
	public static float soundsVolume;
	// Main Game Variables
	private GameMode gameState;
	public static boolean paused = false;
	private char[] name;
	private String fullName;
	private int score = 0;
	private int level = 1;
	private int maxLevel = 3;
	private int spareLives = 3;
	public static int frameCount;
	public static int framesPerTextureUpdate = 10;
	int alienDirection;
	final int LEFT = 0;
	final int RIGHT = 1;
	boolean aliensSpawned = false;
	float alienX = 25;
	int superAlienCount = 0;
	int maxSuperAliens;
	int startGameFrameCount = -1;
	int endGameFrameCount = -1;
	int swapDirectionFrameCount = -1;
	boolean leaderboardReloaded = true;
	boolean keyReleased = true;
	boolean buttonReleased = true;
	boolean listeningForLeft = false;
	boolean listeningForRight = false;
	boolean listeningForShoot = false;
	boolean highScoreSubmitted = false;
	boolean musicPlaying = false;
	static boolean destroyedDefenderThisUpdate = false;
	// Default Methods
	public void settings()
	{
		// Initialise Window
		size(500, 500);
	}
	public void setup()
	{
		// Initialising Resources
		TinySound.init();
		// Loading fonts
		defaultFont = loadFont("data/font/defaultFont.vlw");
		titleFont = loadFont("data/font/font.vlw");
		// Loading and resizing images
		defenderI = loadImage("data/image/defender.png");
		defenderI.resize(50, 25);
		alienA1I = loadImage("data/image/alienA1.png");
		alienB1I = loadImage("data/image/alienB1.png");
		alienC1I = loadImage("data/image/alienC1.png");
		alienA2I = loadImage("data/image/alienA2.png");
		alienB2I = loadImage("data/image/alienB2.png");
		alienC2I = loadImage("data/image/alienC2.png");
		alienA1I.resize(16, 16);
		alienB1I.resize(22, 16);
		alienC1I.resize(24, 16);
		alienA2I.resize(16, 16);
		alienB2I.resize(22, 16);
		alienC2I.resize(24, 16);
		explosion1I = loadImage("data/image/explosion1.png");
		explosion2I = loadImage("data/image/explosion2.png");
		explosion1I.resize(16, 16);
		explosion2I.resize(16, 16);
		superAlienI = loadImage("data/image/superAlien.png");
		superAlienI.resize(50, 22);
		wallAI = loadImage("data/image/wallA.png");
		wallBI = loadImage("data/image/wallB.png");
		wallCI = loadImage("data/image/wallC.png");
		wallDI = loadImage("data/image/wallD.png");
		wallAI.resize(Wall.textureWidth / 3, Wall.textureWidth * 3 / 4);
		wallBI.resize(Wall.textureWidth * 5 / 12, Wall.textureWidth * 3 / 4);
		wallCI.resize(Wall.textureWidth * 5 / 12, Wall.textureWidth * 3 / 4);
		wallDI.resize(Wall.textureWidth / 3, Wall.textureWidth * 3 / 4);
		wallDamAI = loadImage("data/image/wallDamA.png");
		wallDamBI = loadImage("data/image/wallDamB.png");
		wallDamCI = loadImage("data/image/wallDamC.png");
		wallDamDI = loadImage("data/image/wallDamD.png");
		wallDamAI.resize(Wall.textureWidth / 3, Wall.textureWidth * 3 / 4);
		wallDamBI.resize(Wall.textureWidth * 5 / 12, Wall.textureWidth * 3 / 4);
		wallDamCI.resize(Wall.textureWidth * 5 / 12, Wall.textureWidth * 3 / 4);
		wallDamDI.resize(Wall.textureWidth / 3, Wall.textureWidth * 3 / 4);
		settingsI = loadImage("data/image/settings.png");
		settingsI.resize(50, 50);
		hoverSettingsI = loadImage("data/image/hoverSettings.png");
		hoverSettingsI.resize(50, 50);
		// Loading sound files
		gameThemeS = TinySound.loadMusic(new File("./data/sound/gameTheme.wav"));
		menuThemeS = TinySound.loadMusic(new File("./data/sound/menuTheme.wav"));
		superAlienS = TinySound.loadMusic(new File("./data/sound/superAlien.wav"));
		deathS = TinySound.loadSound(new File("./data/sound/death.wav"));
		killS = TinySound.loadSound(new File("./data/sound/kill.wav"));
		shootS = TinySound.loadSound(new File("./data/sound/shoot.wav"));
		// Loading the leaderboard and settings files
		leaderboard = loadTable("data/leaderboard.csv", "header");
		settings = loadXML("data/settings.xml");
		// Initialising basic game settings and variables
		loadSettings();
		fill(255, 255, 255);
		textFont(defaultFont);
		textAlign(CENTER, CENTER);
		imageMode(CENTER);
		rectMode(CENTER);
		setGameState(GameMode.START);
		keys = new boolean[33];
		initialiseBackground();
		System.out.println("Created by Elliot Lewis ©");
	}
	public void draw()
	{
		// Run Game
		// Reload Resources
		if(!leaderboardReloaded)
		{
			updateLeaderboard();
		}
		// Background
		renderBackground();
		// Renders aspects of game dependent on the current game state
		switch(gameState)
		{
			case START:
				renderStartScreen();
				break;
			case GAME:
				renderGame();
				break;
			case END:
				renderEndScreen();
				break;
			case LEADERBOARD:
				renderLeaderboard();
				break;
			case SETTINGS:
				renderSettings();
				break;
		}
		frameCount++;
		// Update Input
		if(mousePressed)
		{
			buttonReleased = false;
		}
		else
		{
			buttonReleased = true;
		}
		destroyedDefenderThisUpdate = false;
	}
	public void keyPressed()
	{
		// Updates the keys boolean array when a key is pressed. Needed so that multiple keys can be pressed at once.
		keys[0] = (key == ' ') ? true : keys[0];
		keys[1] = (key == 'a' || key == 'A') ? true : keys[1];
		keys[2] = (key == 'b' || key == 'B') ? true : keys[2];
		keys[3] = (key == 'c' || key == 'C') ? true : keys[3];
		keys[4] = (key == 'd' || key == 'D') ? true : keys[4];
		keys[5] = (key == 'e' || key == 'E') ? true : keys[5];
		keys[6] = (key == 'f' || key == 'F') ? true : keys[6];
		keys[7] = (key == 'g' || key == 'G') ? true : keys[7];
		keys[8] = (key == 'h' || key == 'H') ? true : keys[8];
		keys[9] = (key == 'i' || key == 'I') ? true : keys[9];
		keys[10] = (key == 'j' || key == 'J') ? true : keys[10];
		keys[11] = (key == 'k' || key == 'K') ? true : keys[11];
		keys[12] = (key == 'l' || key == 'L') ? true : keys[12];
		keys[13] = (key == 'm' || key == 'M') ? true : keys[13];
		keys[14] = (key == 'n' || key == 'N') ? true : keys[14];
		keys[15] = (key == 'o' || key == 'O') ? true : keys[15];
		keys[16] = (key == 'p' || key == 'P') ? true : keys[16];
		keys[17] = (key == 'q' || key == 'Q') ? true : keys[17];
		keys[18] = (key == 'r' || key == 'R') ? true : keys[18];
		keys[19] = (key == 's' || key == 'S') ? true : keys[19];
		keys[20] = (key == 't' || key == 'T') ? true : keys[20];
		keys[21] = (key == 'u' || key == 'U') ? true : keys[21];
		keys[22] = (key == 'v' || key == 'V') ? true : keys[22];
		keys[23] = (key == 'w' || key == 'W') ? true : keys[23];
		keys[24] = (key == 'x' || key == 'X') ? true : keys[24];
		keys[25] = (key == 'y' || key == 'Y') ? true : keys[25];
		keys[26] = (key == 'z' || key == 'Z') ? true : keys[26];
		keys[27] = (key == PConstants.BACKSPACE) ? true : keys[27];
		keys[28] = (key == PConstants.ENTER || key == PConstants.RETURN) ? true : keys[28];
		keys[29] = (keyCode == PConstants.UP) ? true : keys[29];
		keys[30] = (keyCode == PConstants.DOWN) ? true : keys[30];
		keys[31] = (keyCode == PConstants.LEFT) ? true : keys[31];
		keys[32] = (keyCode == PConstants.RIGHT) ? true : keys[32];
	}
	public void keyReleased()
	{
		// Updates the keys boolean array when a key is released. Needed so that multiple keys can be pressed at once.
		keyReleased = true;
		keys[0] = (key == ' ') ? false : keys[0];
		keys[1] = (key == 'a' || key == 'A') ? false : keys[1];
		keys[2] = (key == 'b' || key == 'B') ? false : keys[2];
		keys[3] = (key == 'c' || key == 'C') ? false : keys[3];
		keys[4] = (key == 'd' || key == 'D') ? false : keys[4];
		keys[5] = (key == 'e' || key == 'E') ? false : keys[5];
		keys[6] = (key == 'f' || key == 'F') ? false : keys[6];
		keys[7] = (key == 'g' || key == 'G') ? false : keys[7];
		keys[8] = (key == 'h' || key == 'H') ? false : keys[8];
		keys[9] = (key == 'i' || key == 'I') ? false : keys[9];
		keys[10] = (key == 'j' || key == 'J') ? false : keys[10];
		keys[11] = (key == 'k' || key == 'K') ? false : keys[11];
		keys[12] = (key == 'l' || key == 'L') ? false : keys[12];
		keys[13] = (key == 'm' || key == 'M') ? false : keys[13];
		keys[14] = (key == 'n' || key == 'N') ? false : keys[14];
		keys[15] = (key == 'o' || key == 'O') ? false : keys[15];
		keys[16] = (key == 'p' || key == 'P') ? false : keys[16];
		keys[17] = (key == 'q' || key == 'Q') ? false : keys[17];
		keys[18] = (key == 'r' || key == 'R') ? false : keys[18];
		keys[19] = (key == 's' || key == 'S') ? false : keys[19];
		keys[20] = (key == 't' || key == 'T') ? false : keys[20];
		keys[21] = (key == 'u' || key == 'U') ? false : keys[21];
		keys[22] = (key == 'v' || key == 'V') ? false : keys[22];
		keys[23] = (key == 'w' || key == 'W') ? false : keys[23];
		keys[24] = (key == 'x' || key == 'X') ? false : keys[24];
		keys[25] = (key == 'y' || key == 'Y') ? false : keys[25];
		keys[26] = (key == 'z' || key == 'Z') ? false : keys[26];
		keys[27] = (key == BACKSPACE) ? false : keys[27];
		keys[28] = (key == ENTER || key == RETURN) ? false : keys[28];
		keys[29] = (keyCode == PConstants.UP) ? false : keys[29];
		keys[30] = (keyCode == PConstants.DOWN) ? false : keys[30];
		keys[31] = (keyCode == PConstants.LEFT) ? false : keys[31];
		keys[32] = (keyCode == PConstants.RIGHT) ? false : keys[32];
	}
	// Settings
	private void loadSettings()
	{
		// This function loads the settings in from the settings XML object.
		// First the keys settings are extracted from the settings object into an XML array.
		XML[] keys = settings.getChildren("key");
		for(int i = 0; i < keys.length; i++)
		{
			// Then, based off of the setting's ID, different variables are set to the loaded values.
			String id = keys[i].getString("id").toLowerCase().trim();
			switch(id)
			{
				case "left":
					leftKey = keys[i].getIntContent();
					break;
				case "right":
					rightKey = keys[i].getIntContent();
					break;
				case "shoot":
					shootKey = keys[i].getIntContent();
					break;
			}
		}
		// The volume settings are then extracted from the settings object into an XML array.
		XML[] volume = settings.getChildren("volume");
		for(int i = 0; i < volume.length; i++)
		{
			// Then, based off of the setting's ID, different variables are set to the loaded values.
			String id = volume[i].getString("id").toLowerCase().trim();
			switch(id)
			{
				case "music":
					musicVolume = volume[i].getFloatContent();
					break;
				case "sounds":
					soundsVolume = volume[i].getFloatContent();
					break;
			}
		}
		// The loaded settings are then printed out to the console.
		System.out.println("LOADED SETTINGS:");
		System.out.println("KEY - Move Left: " + getKeyName(leftKey));
		System.out.println("KEY - Move Right: " + getKeyName(rightKey));
		System.out.println("KEY - Shoot: " + getKeyName(shootKey));
		System.out.println("VOLUME - Music: " + musicVolume);
		System.out.println("VOLUME - Sounds: " + soundsVolume);
		applySettings();
	}
	// Getters and Setters
	public GameMode getGameState()
	{
		// Returns the current game state.
		return gameState;
	}
	public void setGameState(GameMode gameState)
	{
		// Function that updates the current game state to the one passed in through the single parameter. Based off of that setting, some
		// functions that relate to sounds and music are called.
		loadSettings();
		if(gameState == GameMode.GAME)
		{
			gameThemeS.play(true);
		}
		else
		{
			gameThemeS.stop();
			superAlienS.stop();
		}
		if(gameState == GameMode.START || gameState == GameMode.END || gameState == GameMode.LEADERBOARD || gameState == GameMode.SETTINGS)
		{
			if(!musicPlaying)
			{
				menuThemeS.play(true);
				musicPlaying = true;
			}
			leaderboardReloaded = false;
		}
		else
		{
			menuThemeS.stop();
			musicPlaying = false;
		}
		this.gameState = gameState;
	}
	public int getFrameCount()
	{
		// Returns the current frame count.
		return frameCount;
	}
	public String getKeyName(int keyCode)
	{
		// Returns a legible name for each keycode.
		switch(keyCode)
		{
			case -1:
				return "-";
			case 0:
				return "Space";
			case 1:
				return "A";
			case 2:
				return "B";
			case 3:
				return "C";
			case 4:
				return "D";
			case 5:
				return "E";
			case 6:
				return "F";
			case 7:
				return "G";
			case 8:
				return "H";
			case 9:
				return "I";
			case 10:
				return "J";
			case 11:
				return "K";
			case 12:
				return "L";
			case 13:
				return "M";
			case 14:
				return "N";
			case 15:
				return "O";
			case 16:
				return "P";
			case 17:
				return "Q";
			case 18:
				return "R";
			case 19:
				return "S";
			case 20:
				return "T";
			case 21:
				return "U";
			case 22:
				return "V";
			case 23:
				return "W";
			case 24:
				return "X";
			case 25:
				return "Y";
			case 26:
				return "Z";
			case 27:
				return "Backspace";
			case 28:
				return "Enter";
			case 29:
				return "Up";
			case 30:
				return "Down";
			case 31:
				return "Left";
			case 32:
				return "Right";
			default:
				return "";
		}
	}
	// Background
	private void initialiseBackground()
	{
		// This initialises the stars array so that all of the stars have random positions on the screen when the game first starts.
		int starCount = 50;
		float minDX = 0.25f;
		float maxDX = 0.75f;
		stars = new ArrayList<Star>();
		for(int i = 0; i < starCount; i++)
		{
			stars.add(new Star(this, this, minDX, maxDX));
		}
	}
	private void renderBackground()
	{
		// This method renders the background every single frame of the application.
		fill(10, 0, 10);
		rect(width / 2, height / 2, width, height);
		fill(255, 255, 255);
		for(int i = 0; i < stars.size(); i++)
		{
			stars.get(i).update();
		}
	}
	private void renderStartScreen()
	{
		// When the game state is on the start screen, this method is called. It renders the start screen of the game. This includes a title,
		// play, leaderboards and settings buttons.
		// Title
		textFont(titleFont);
		fill(100, 255, 100);
		// This controls the pulsating text animation for the title.
		float textSize = Math.abs(sin((float) frameCount / 20f) * 5) + 40;
		textSize(textSize);
		text("Space\nInvaders", width / 2, height / 4);
		fill(255, 255, 255);
		textFont(defaultFont);
		// Play Button
		Button play = new Button(this, this, "Play", width / 3, height / 2, 150, 50);
		play.render();
		if(play.clicked())
		{
			setGameState(GameMode.GAME);
		}
		// Leaderboards Button
		Button leaderboards = new Button(this, this, "Leaderboards", width * 2 / 3, height / 2, 150, 50);
		leaderboards.render();
		if(leaderboards.clicked())
		{
			setGameState(GameMode.LEADERBOARD);
		}
		// Credit
		textSize(24);
		text("by Elliot Lewis", width / 2, height * 3 / 4);
		// Settings Button
		ImageButton settings = new ImageButton(this, this, settingsI, hoverSettingsI, width - (settingsI.width * 2 / 3), height - (settingsI.height * 2 / 3));
		settings.render();
		if(settings.clicked())
		{
			setGameState(GameMode.SETTINGS);
		}
	}
	private void initialiseGame()
	{
		// This method initialises the variables needed for a new game. It's called when the game state is changed to 'GAME'.
		startGameFrameCount = frameCount;
		endGameFrameCount = -1;
		defender = new Defender(this, this);
		aliens = new ArrayList<Alien>();
		explosions = new ArrayList<Explosion>();
		walls = new ArrayList<Wall>();
		superAlien = null;
		bomb = null;
		missile = null;
		paused = false;
		alienDirection = RIGHT;
		highScoreSubmitted = false;
	}
	private void endGame()
	{
		// This method is called when the aliens reach the bottom of the screen. It causes the game to end no matter how many lives the user
		// has left.
		spareLives = 0;
	}
	private void restartGame()
	{
		// This method is called when the defender gets shot by an alien. It just destroys the game entities that would not be needed when the
		// player looses a life and the current game needs to resume.
		startGameFrameCount = frameCount;
		defender = new Defender(this, this);
		bomb = null;
		missile = null;
		superAlien = null;
		superAlienS.stop();
	}
	private void resetGame()
	{
		// This method is called when the game needs to start afresh, this may be because the game is over and the player clicks the replay
		// button. It resets the necessary variables.
		initialiseGame();
		score = 0;
		spareLives = 3;
		level = 1;
		alienX = 25;
		aliensSpawned = false;
	}
	private void renderGame()
	{
		// This method runs every frame update called whilst the user is actually playing the game. It draws the main entities, the GUI and
		// other components needed to render the game.
		Alien.speed = 1f + ((level - 1f) / 4); // The alien speed is dependent on the current level.
		// GUI Elements.
		textAlign(LEFT, CENTER);
		textSize(24);
		text("Score: " + score, 10, 20);
		text("Level: " + level, width - 200, 20);
		text("Lives: " + spareLives, width - 100, 20);
		textAlign(CENTER, CENTER);
		// Initialises the game if it hasn't been done so already.
		if(startGameFrameCount < 0)
		{
			initialiseGame();
		}
		// Creates the level if it hasn't already.
		if(!aliensSpawned)
		{
			createLevel();
		}
		// Determines what to do if all of the aliens have been killed. This either advances to the next level if there is one, or ends the
		// game if they've beaten the final level.
		if(aliens.size() == 0 && level + 1 <= maxLevel)
		{
			restartGame();
			aliensSpawned = false;
			level++;
		}
		else if(aliens.size() == 0 && level + 1 > maxLevel)
		{
			setGameState(GameMode.END);
		}
		// This runs the counter for that counts down from 3 when the game is starting, it allows the player to prepare for the upcoming level.
		int countdownTimer = 3 - (int) Math.floor(((double) (frameCount - startGameFrameCount) / 60));
		if(countdownTimer >= 0)
		{
			defender.render();
			for(int i = 0; i < aliens.size(); i++)
			{
				aliens.get(i).render();
			}
			for(int i = 0; i < walls.size(); i++)
			{
				walls.get(i).update();
			}
			fill(150);
			stroke(155);
			strokeWeight(2);
			rect(width / 2, height / 2, width / 5, height / 10);
			strokeWeight(0);
			fill(255);
			textFont(titleFont);
			textSize(((60 - ((frameCount - startGameFrameCount) % 60)) / 3) + 15);
			float opacity = 255 - (float) Math.floor(((double) ((frameCount - startGameFrameCount) % 60) / 60) * 255);
			fill(255, opacity);
			rect(width / 2, height / 2, width, height);
			fill(255);
			if(countdownTimer == 0)
			{
				text("GO!", width / 2, height / 2);
			}
			else
			{
				text(countdownTimer + "", width / 2, height / 2);
			}
			textFont(defaultFont);
			// To avoid the scenario in which the game is played in the background while the counter is running, the method is returned.
			return;
		}
		if(listenForKeyInput() == 16)
		{
			// This checks to see if the pause button is pressed and if it is then toggle the pause state and update sound effects accordingly.
			paused = !paused;
			if(paused)
			{
				superAlienS.setVolume(0d);
			}
			else
			{
				superAlienS.setVolume((double) soundsVolume);
			}
		}
		if(!paused)
		{
			// If the game isn't paused then this section of code is run. This controls the game logic, it includes code that allows the user
			// to control the defender, AI for the aliens, random generation of super aliens and the majority of the main game logic.
			// The following if statement controls the aliens movement. It's controlled by an alienDirection variable which has 2 states, left
			// and right. The alien's global speed is added or subtracted accordingly.
			if(alienDirection == LEFT)
			{
				alienX -= Alien.speed;
			}
			else
			{
				alienX += Alien.speed;
			}
			if(random.nextInt(1000) == 1 && superAlien == null && superAlienCount < maxSuperAliens && aliens.get(0).y >= 100)
			{
				// Controls super alien spawning, based off of a random number generator and the test to see if it will fit in the screen above
				// the other aliens.
				superAlien = new SuperAlien(this, this);
				superAlienS.play(true);
				superAlienCount++;
			}
			// This updates the defender and all the aliens.
			defender.update();
			for(int i = 0; i < aliens.size(); i++)
			{
				aliens.get(i).update();
			}
			// This checks to see if the explosion entities have expired and if they haven't, update them.
			for(int i = 0; i < explosions.size(); i++)
			{
				if(frameCount - explosions.get(i).creationFrame > framesPerTextureUpdate * 2)
				{
					explosions.remove(i);
				}
				else
				{
					explosions.get(i).update();
				}
			}
			// This updates the walls.
			for(int i = 0; i < walls.size(); i++)
			{
				walls.get(i).update();
			}
			// This checks to see if a missile already exists, if not then it plays the shooting audio clip and creates a new missile.
			if(keys[shootKey] && missile == null)
			{
				shootS.play(soundsVolume);
				missile = new Missile(this, this, defender.x, defender.y);
			}
			// Updates the bomb and missile entities if they exist.
			if(bomb != null)
			{
				bomb.update();
			}
			if(missile != null)
			{
				missile.update();
			}
			// Updates the super alien entity if it exists and then checks to see if its off the screen, if it is, then destroy it.
			if(superAlien != null)
			{
				superAlien.update();
				if(superAlien.x < -(superAlien.texture.width / 2) || superAlien.x > width + (superAlien.texture.width / 2))
				{
					destroySuperAlien();
				}
			}
			// Renders the pause on-screen text.
			text("P: Pause", width - (textWidth("P: Pause") / 2) - 10, height - 22);
		}
		else
		{
			// If the game is paused then the game entities are rendered instead of updated.
			// Renders the defender, alien, explosion, wall, bomb, missile and super alien entities.
			defender.render();
			for(int i = 0; i < aliens.size(); i++)
			{
				aliens.get(i).render();
			}
			for(int i = 0; i < explosions.size(); i++)
			{
				explosions.get(i).render();
			}
			for(int i = 0; i < walls.size(); i++)
			{
				walls.get(i).update();
			}
			if(bomb != null)
			{
				bomb.render();
			}
			if(missile != null)
			{
				missile.render();
			}
			if(superAlien != null)
			{
				superAlien.render();
			}
			// Renders the paused popup screen.
			renderPausedPopup();
			// Resets the game and loads the main menu if the 'M' key is pressed on the keyboard.
			if(keys[13])
			{
				resetGame();
				setGameState(GameMode.START);
			}
		}
	}
	private void renderPausedPopup()
	{
		// This method is used to render a simple overlay when the game is paused.
		fill(color(100), 100);
		rect(width / 2, height / 2, width, height);
		fill(255);
		Sign paused = new Sign(this, this, "", width / 2, height / 2, width * 2 / 3, height / 3);
		paused.render();
		textFont(titleFont);
		textSize(32);
		text("Paused", width / 2, height * 2 / 5);
		textSize(24);
		text("Press 'P' to Continue", width / 2, height / 2);
		text("Press 'M' for Main Menu", width / 2, height * 3 / 5);
		textFont(defaultFont);
	}
	private void initialiseEndScreen()
	{
		// This method initialises the end screen which includes making sure the name variable is created as a new 3 character char array
		// containing only hyphens as well as settings other variables to specific values.
		endGameFrameCount = frameCount;
		name = new char[3];
		for(int i = 0; i < name.length; i++)
		{
			name[i] = '-';
		}
		highScoreSubmitted = false;
	}
	private void renderEndScreen()
	{
		// This function renders the end screen of the game. It displays basic overlays telling the player that they've lost and if they have
		// a score thats worthy of being on the leaderboards then it asks them to input their name in the form of a 3 character string.
		if(endGameFrameCount < 0)
		{
			// Looks out for the flag value for the endGameFrameCount variable, the flag is used to show that the end screen is not initialised
			// and the program will then therefore initialise it.
			initialiseEndScreen();
		}
		if(highScore())
		{
			// If the score is worthy of being on the leaderboards, then this if statement is triggered.
			// The program checks to see then if a high score has already been submitted.
			if(!highScoreSubmitted)
			{
				// This listens for the user to type in their name in the high score name field.
				for(int i = 0; i < name.length; i++)
				{
					if(keys[27] && name[name.length - 1] != '-')
					{
						name[name.length - 1] = '-';
						keyReleased = false;
					}
					if(name[i] != '-')
					{
						continue;
					}
					else
					{
						char input = listenForInput();
						if(input == '<' && i > 0)
						{
							name[i - 1] = '-';
							break;
						}
						else if(input != '<')
						{
							name[i] = input;
							break;
						}
					}
				}
				if(name[name.length - 1] != '-')
				{
					fullName = String.valueOf(name).replace('-', '\0');
				}
				// Submit score button. Checks to see if 3 characters have been entered and if they have, enable the user to submit their score
				// to the leaderboards.
				Button submitScore = new Button(this, this, "Submit Score", width / 2, height * 2 / 3, 150, 50);
				if(name[name.length - 1] == '-')
				{
					submitScore.setDisabled();
				}
				submitScore.render();
				if(submitScore.clicked())
				{
					submitScore(fullName, score);
				}
			}
			else
			{
				// If the high score has been submitted already then the submit score button is replaced with a "Score Submitted!" message.
				textSize(24);
				text("Score Submitted!", width / 2, height * 2 / 3);
			}
			// The currently entered name is displayed along with a header indicating what it is.
			textSize(32);
			textAlign(LEFT, CENTER);
			text("Name:", 159, height / 3);
			textAlign(CENTER, CENTER);
			text(name[0], 281, height / 3);
			text(name[1], 311, height / 3);
			text(name[2], 341, height / 3);
			textSize(24);
			text("Score: " + score, width / 2, height / 2);
			// Replay button. Allows the user to play again.
			Button replay = new Button(this, this, "Replay", width / 3, height * 2 / 3 + 75, 150, 50);
			replay.render();
			if(replay.clicked())
			{
				resetGame();
				setGameState(GameMode.GAME);
			}
			// Main Menu Button. Allows the user to navigate to the main menu.
			Button mainMenu = new Button(this, this, "Main Menu", width * 2 / 3, height * 2 / 3 + 75, 150, 50);
			mainMenu.render();
			if(mainMenu.clicked())
			{
				resetGame();
				setGameState(GameMode.START);
			}
		}
		else
		{
			// If the score isn't worthy of being on the leaderboards, then this section of code is run.
			// Pulsing "You Lose!" text.
			textSize(Math.abs(sin((float) frameCount / 60f) * 8) + 40);
			text("You Lose!", width / 2, height / 3);
			textSize(24);
			text("Score: " + score, width / 2, height / 2);
			// Replay button. Allows the user to play again.
			Button replay = new Button(this, this, "Replay", width / 3, height * 2 / 3, 150, 50);
			replay.render();
			if(replay.clicked())
			{
				resetGame();
				setGameState(GameMode.GAME);
			}
			// Main Menu Button. Allows the user to navigate to the main menu.
			Button mainMenu = new Button(this, this, "Main Menu", width * 2 / 3, height * 2 / 3, 150, 50);
			mainMenu.render();
			if(mainMenu.clicked())
			{
				resetGame();
				setGameState(GameMode.START);
			}
		}
	}
	private void renderLeaderboard()
	{
		// This function renders the leaderboard showing the current local top scores.
		// Draws the lines and background for the table.
		fill(10, 0, 10);
		rect(width / 2, (height / 2) - 50, 350, 350);
		fill(255);
		rect(width / 2, (height / 2) - 50, 4, 350);
		rect(width / 2, 75, 350, 4);
		// Draws the title text for the table.
		text("Name", ((width - 150) / 4) + 75, 50);
		text("Score", ((width - 150) * 3 / 4) + 75, 50);
		textSize(32);
		// Draws the scores and names to be put in the table.
		for(int i = 0; i < leaderboard.getRowCount(); i++)
		{
			TableRow row = leaderboard.getRow(i);
			String name = row.getString("Name");
			int score = row.getInt("Score");
			if(score <= 0)
			{
				continue;
			}
			text(name, ((width - 150) / 4) + 75, (50 * (i + 2)) + 25);
			text(score, ((width - 150) * 3 / 4) + 75, (50 * (i + 2)) + 25);
		}
		// Back Button. Allows the user to return to the main menu.
		Button back = new Button(this, this, "Main Menu", width / 2, height - 75, 150, 50);
		back.render();
		if(back.clicked())
		{
			setGameState(GameMode.START);
		}
	}
	private void renderSettings()
	{
		// This function renders the settings screen.
		textSize(24);
		text("Music Volume", width / 2, 50);
		// Initialises and draws both the volume sliders.
		if(musicSlider == null)
		{
			musicSlider = new Slider(this, this, width / 2, 85, width * 2 / 3, 10, musicVolume);
		}
		musicSlider.update();
		musicVolume = musicSlider.getValue();
		text("Sound Volume", width / 2, 125);
		if(soundsSlider == null)
		{
			soundsSlider = new Slider(this, this, width / 2, 160, width * 2 / 3, 10, soundsVolume);
		}
		soundsSlider.update();
		soundsVolume = soundsSlider.getValue();
		// Draws the set keys buttons and gives them functionality.
		textSize(30);
		text("Keys", width / 2, 200);
		int key = listenForKeyInput();
		textSize(24);
		text("Move Left: " + getKeyName(leftKey), width / 3, 240);
		Button editLeft = new Button(this, this, "Edit", width * 2 / 3, 240, 100, 24);
		if(listeningForLeft || listeningForRight || listeningForShoot)
		{
			editLeft.setDisabled();
		}
		editLeft.render();
		if(editLeft.clicked())
		{
			listeningForLeft = true;
		}
		if(listeningForLeft && key != -1 && key != rightKey && key != shootKey && key != 16)
		{
			leftKey = key;
			listeningForLeft = false;
		}
		textSize(24);
		text("Move Right: " + getKeyName(rightKey), width / 3, 280);
		Button editRight = new Button(this, this, "Edit", width * 2 / 3, 280, 100, 24);
		if(listeningForLeft || listeningForRight || listeningForShoot)
		{
			editRight.setDisabled();
		}
		editRight.render();
		if(editRight.clicked())
		{
			listeningForRight = true;
		}
		if(listeningForRight && key != -1 && key != leftKey && key != shootKey && key != 16)
		{
			rightKey = key;
			listeningForRight = false;
		}
		textSize(24);
		text("Shoot: " + getKeyName(shootKey), width / 3, 320);
		Button editShoot = new Button(this, this, "Edit", width * 2 / 3, 320, 100, 24);
		if(listeningForLeft || listeningForRight || listeningForShoot)
		{
			editShoot.setDisabled();
		}
		editShoot.render();
		if(editShoot.clicked())
		{
			listeningForShoot = true;
		}
		if(listeningForShoot && key != -1 && key != leftKey && key != rightKey && key != 16)
		{
			shootKey = key;
			listeningForShoot = false;
		}
		// Apply and cancel buttons.
		Button apply = new Button(this, this, "Apply", width / 3, height * 4 / 5, 150, 50);
		if(listeningForLeft || listeningForRight || listeningForShoot)
		{
			apply.setDisabled();
		}
		apply.render();
		if(apply.clicked())
		{
			saveSettings();
			musicSlider = null;
			soundsSlider = null;
			setGameState(GameMode.START);
		}
		Button cancel = new Button(this, this, "Cancel", width * 2 / 3, height * 4 / 5, 150, 50);
		if(listeningForLeft || listeningForRight || listeningForShoot)
		{
			cancel.setDisabled();
		}
		cancel.render();
		if(cancel.clicked())
		{
			loadSettings();
			musicSlider = null;
			soundsSlider = null;
			setGameState(GameMode.START);
		}
		// Renders popup when editing a key.
		if(listeningForLeft || listeningForRight || listeningForShoot)
		{
			Sign keyNotification = new Sign(this, this, "Please press key...", width / 2, height / 2, 250, 100);
			keyNotification.render();
		}
		applySettings();
	}
	private void applySettings()
	{
		// Function that applies the settings that are loaded currently.
		menuThemeS.setVolume((double) musicVolume);
		gameThemeS.setVolume((double) musicVolume);
		superAlienS.setVolume((double) soundsVolume);
	}
	private void saveSettings()
	{
		// This function saves the settings to file.
		// This section handles the keys settings.
		XML[] keys = settings.getChildren("key");
		for(int i = 0; i < keys.length; i++)
		{
			String id = keys[i].getString("id").toLowerCase().trim();
			switch(id)
			{
				case "left":
					settings.removeChild(keys[i]);
					keys[i].setContent(String.valueOf(leftKey));
					settings.addChild(keys[i]);
					break;
				case "right":
					settings.removeChild(keys[i]);
					keys[i].setContent(String.valueOf(rightKey));
					settings.addChild(keys[i]);
					break;
				case "shoot":
					settings.removeChild(keys[i]);
					keys[i].setContent(String.valueOf(shootKey));
					settings.addChild(keys[i]);
					break;
			}
		}
		// This handles the volume settings.
		XML[] volume = settings.getChildren("volume");
		for(int i = 0; i < volume.length; i++)
		{
			String id = volume[i].getString("id").toLowerCase().trim();
			switch(id)
			{
				case "music":
					settings.removeChild(volume[i]);
					volume[i].setContent(String.valueOf(musicVolume));
					settings.addChild(volume[i]);
					break;
				case "sounds":
					settings.removeChild(volume[i]);
					volume[i].setContent(String.valueOf(soundsVolume));
					settings.addChild(volume[i]);
					break;
			}
		}
		saveXML(settings, "data/settings.xml");
	}
	private void createLevel()
	{
		// This function creates each level.
		alienDirection = RIGHT;
		alienX = 25;
		aliens.clear();
		explosions.clear();
		walls.clear();
		superAlienCount = 0;
		int yCreate = 50;
		// Dependent on the level, different things are spawned in.
		switch(level)
		{
			case 1:
				maxSuperAliens = 6;
				for(int i = 0; i < 3; i++)
				{
					if(i == 0)
					{
						spawnAlienRow(0, yCreate);
					}
					else if(i == 1)
					{
						spawnAlienRow(1, yCreate);
					}
					else
					{
						spawnAlienRow(2, yCreate);
					}
					yCreate += 20;
				}
				spawnWall(2, width / 5);
				spawnWall(2, width * 2 / 5);
				spawnWall(2, width * 3 / 5);
				spawnWall(2, width * 4 / 5);
				break;
			case 2:
				maxSuperAliens = 4;
				for(int i = 0; i < 4; i++)
				{
					if(i == 0)
					{
						spawnAlienRow(0, yCreate);
					}
					else if(i <= 2)
					{
						spawnAlienRow(1, yCreate);
					}
					else
					{
						spawnAlienRow(2, yCreate);
					}
					yCreate += 20;
				}
				spawnWall(2, width / 4);
				spawnWall(2, width / 2);
				spawnWall(2, width * 3 / 4);
				break;
			case 3:
				maxSuperAliens = 3;
				for(int i = 0; i < 6; i++)
				{
					if(i <= 1)
					{
						spawnAlienRow(0, yCreate);
					}
					else if(i <= 3)
					{
						spawnAlienRow(1, yCreate);
					}
					else
					{
						spawnAlienRow(2, yCreate);
					}
					yCreate += 20;
				}
				spawnWall(2, width / 3);
				spawnWall(2, width * 2 / 3);
				break;
		}
		aliensSpawned = true;
	}
	private void spawnAlienRow(int type, int y)
	{
		// This spawns a single row of aliens.
		for(int i = 0; i < 11; i++)
		{
			aliens.add(new Alien(this, this, type, i, y));
		}
	}
	private void spawnWall(int maxHealth, int x)
	{
		// This spawns a single wall
		walls.add(new Wall(this, this, maxHealth, x));
	}
	public void swapAlienDirection()
	{
		// This function is called when one of the aliens hits one of the sides of the game, it then swaps its direction.
		if(frameCount - swapDirectionFrameCount > 30)
		{
			swapDirectionFrameCount = frameCount;
			if(alienDirection == RIGHT)
			{
				alienDirection = LEFT;
			}
			else
			{
				alienDirection = RIGHT;
			}
			for(int i = 0; i < aliens.size(); i++)
			{
				aliens.get(i).shiftDown();
			}
			for(int i = 0; i < explosions.size(); i++)
			{
				explosions.get(i).shiftDown();
			}
		}
	}
	public void addScore(int score)
	{
		// This adds a score passed into it onto the game's current score.
		this.score += score;
	}
	public void destroyAlien(int i)
	{
		// This destroys a specific alien entity.
		explosions.add(new Explosion(this, this, aliens.get(i).position, aliens.get(i).y));
		killS.play(soundsVolume);
		aliens.remove(i);
	}
	public void destroyBomb()
	{
		// This destroys the bomb entity.
		bomb = null;
	}
	public void destroyDefender()
	{
		// This destroys the player.
		if(!destroyedDefenderThisUpdate)
		{
			deathS.play(soundsVolume);
			for(int i = 0; i < aliens.size(); i++)
			{
				if(aliens.get(i).y > height - 125)
				{
					endGame();
					return;
				}
			}
			if(spareLives > 0)
			{
				spareLives--;
				restartGame();
			}
			else
			{
				setGameState(GameMode.END);
			}
			destroyedDefenderThisUpdate = true;
		}
	}
	public void destroyMissile()
	{
		// This destroys the missile entity.
		missile = null;
	}
	public void destroySuperAlien()
	{
		// This destroys the super alien entity.
		superAlien = null;
		superAlienS.stop();
	}
	public void damageWall(int i, int x)
	{
		// This damages a specific section of a specific wall.
		walls.get(i).attack(x);
		if(walls.get(i).destroyed.get(0) && walls.get(i).destroyed.get(1) && walls.get(i).destroyed.get(2) && walls.get(i).destroyed.get(3))
		{
			walls.remove(i);
		}
	}
	private void updateLeaderboard()
	{
		// This updates the loaded leaderboard.
		leaderboard = loadTable("data/leaderboard.csv", "header");
	}
	private void saveLeaderboard()
	{
		// This saves the current leaderboard to file.
		saveTable(leaderboard, "data/leaderboard.csv");
	}
	private boolean highScore()
	{
		// This checks to see if the current game score is a new high score or not.
		for(int i = 0; i < leaderboard.getRowCount(); i++)
		{
			TableRow row = leaderboard.getRow(leaderboard.getRowCount() - i - 1);
			int retrievedScore = row.getInt("Score");
			if(score > retrievedScore)
			{
				return true;
			}
		}
		return false;
	}
	private char listenForInput()
	{
		// This listens for a letter on the keyboard to be input and then returns that key as a char.
		if(keyReleased)
		{
			String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			for(int i = 1; i < (keys.length - 5); i++)
			{
				if(keys[27])
				{
					keyReleased = false;
					return '<';
				}
				else if(keys[i])
				{
					keyReleased = false;
					return alphabet.charAt(i - 1);
				}
				else
				{
					continue;
				}
			}
		}
		return '-';
	}
	private int listenForKeyInput()
	{
		// This listens for a key on the keyboard to be released and then returns that key as an integer keycode.
		if(keyReleased)
		{
			for(int i = 0; i < keys.length; i++)
			{
				if(keys[27])
				{
					keyReleased = false;
					return -1;
				}
				else if(keys[i])
				{
					keyReleased = false;
					return i;
				}
				else
				{
					continue;
				}
			}
		}
		return -1;
	}
	private void submitScore(String name, int score)
	{
		// This adds a score to the leaderboard in the correct position and then saves the leaderboard.
		int targetRow = 0;
		for(int i = 0; i < leaderboard.getRowCount(); i++)
		{
			int rowNum = leaderboard.getRowCount() - 1 - i;
			TableRow row = leaderboard.getRow(rowNum);
			int retrievedScore = row.getInt("Score");
			if(score > retrievedScore)
			{
				continue;
			}
			else if(score < retrievedScore)
			{
				targetRow = rowNum + 1;
				break;
			}
		}
		if(targetRow == leaderboard.getRowCount() - 1)
		{
			TableRow row = leaderboard.getRow(leaderboard.getRowCount() - 1);
			row.setString("Name", name);
			row.setInt("Score", score);
		}
		else
		{
			for(int i = 0; i < leaderboard.getRowCount() - targetRow - 1; i++)
			{
				TableRow currentRow = leaderboard.getRow(leaderboard.getRowCount() - 2 - i);
				TableRow previousRow = leaderboard.getRow(leaderboard.getRowCount() - 1 - i);
				previousRow.setString("Name", currentRow.getString("Name"));
				previousRow.setInt("Score", currentRow.getInt("Score"));
			}
			TableRow row = leaderboard.getRow(targetRow);
			row.setString("Name", name);
			row.setInt("Score", score);
		}
		saveLeaderboard();
		highScoreSubmitted = true;
	}
	// Main Method
	public static void main(String args[])
	{
		PApplet.main(new String[]{"spaceInvaders.Game"});
	}
}