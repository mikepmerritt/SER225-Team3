package Screens;

import java.awt.Color;

import Engine.Config;
import Engine.GamePanel;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Engine.Screen;
import Engine.ScreenManager;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.ImageEffect;
import GameObject.Sprite;
import Level.Map;
import Level.Player;
import Level.PlayerListener;
import Maps.TestMap;
import Maps.TestMap2;
import Maps.TestMap3;
import Maps.TestMap4;
import Maps.TestMap5;
import Maps.TestMap6;
import Maps.TestMap8;
import Maps.TestMap9;
import Players.Cat;
import SpriteFont.SpriteFont;
import Utils.Stopwatch;

// This class is for when the platformer game is actually being played
public class PlayLevelScreen extends Screen implements PlayerListener 
{
	protected ScreenCoordinator screenCoordinator;
	protected PlayLevelScreenState playLevelScreenState;
	
	protected Map map;
	
	protected LevelSelectScreen levelSelectScreen;
	protected OptionsScreen optionsScreen;
	
	private SpriteFont pauseLabel;
	private SpriteFont instructionLabel;
	private SpriteFont instruction2Label;
	private SpriteFont instruction3Label;
	private SpriteFont instruction4Label;
	private SpriteFont instruction5Label;
	private SpriteFont instruction6Label;
	private SpriteFont returnInstructionLabel;
	
	private Sprite soundSprite;
	
	protected Player player;
	
	private KeyLocker keyLocker = new KeyLocker();
	private Stopwatch keyTimer = new Stopwatch();
	protected Stopwatch screenTimer = new Stopwatch();
	
	protected LevelClearedScreen levelClearedScreen;
	protected LevelLoseScreen levelLoseScreen;
	protected FinalLevelClearedScreen finalClearScreen;
	
	protected int levelNum = 0;
	protected int catNum = 0;

	public PlayLevelScreen(ScreenCoordinator screenCoordinator) 
	{
		this.screenCoordinator = screenCoordinator;
		this.playLevelScreenState = PlayLevelScreenState.RUNNING;
	}

	public PlayLevelScreen(ScreenCoordinator screenCoordinator, PlayLevelScreenState state) 
	{
		this.screenCoordinator = screenCoordinator;
		this.playLevelScreenState = state;
	}

	public void initialize() 
	{
		this.map = getCurrentMap();
		map.reset();
		System.out.println(levelNum);

		levelSelectScreen = new LevelSelectScreen(this);
		levelSelectScreen.initialize();
		
		optionsScreen = new OptionsScreen(this);
		optionsScreen.initialize();
		
		pauseLabel = new SpriteFont("Pause", 350, 250, "Comic Sans", 30, Color.white);
		
		instructionLabel = new SpriteFont("To JUMP: UP arrow key, or 'W', or SPACEBAR", 130, 140, "Times New Roman", 20, Color.white);
		instructionLabel.setOutlineColor(Color.white);
		instructionLabel.setOutlineThickness(2.0f);
		
		instruction2Label = new SpriteFont("To MOVE LEFT: LEFT arrow key, or 'A'", 130, 180, "Times New Roman", 20, Color.white);
		instruction2Label.setOutlineColor(Color.white);
		instruction2Label.setOutlineThickness(2.0f);
		
		instruction3Label = new SpriteFont("To MOVE RIGHT: RIGHT arrow key, or 'D'", 130, 220, "Times New Roman", 20, Color.white);
		instruction3Label.setOutlineColor(Color.white);
		instruction3Label.setOutlineThickness(2.0f);
		
		instruction4Label = new SpriteFont("To CROUCH: DOWN arrow key, or 'S'", 130, 260, "Times New Roman", 20, Color.white);
		instruction4Label.setOutlineColor(Color.white);
		instruction4Label.setOutlineThickness(2.0f);
		
		instruction5Label = new SpriteFont("To ATTACK: E key", 130, 300, "Times New Roman", 20, Color.white);
		instruction5Label.setOutlineColor(Color.white);
		instruction5Label.setOutlineThickness(2.0f);
		
		instruction6Label = new SpriteFont("To INTERACT: F key", 130, 340, "Times New Roman", 20, Color.white);
		instruction6Label.setOutlineColor(Color.white);
		instruction6Label.setOutlineThickness(2.0f);

		returnInstructionLabel = new SpriteFont("Press X to return to the menu", 5, 560, "Times New Roman", 30, Color.white);
		returnInstructionLabel.setOutlineColor(Color.white);
		returnInstructionLabel.setOutlineThickness(2.0f);
		
		soundSprite = new Sprite(ImageLoader.load(Config.VOLUME_SPRITE), 5, 532, 0.25f, ImageEffect.NONE);
		
		this.player = getCat();
		//this.player = new Cat(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
		this.player.setMap(map);
		this.player.addListener(this);
		this.player.setLocation(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);

		keyTimer.setWaitTime(200);

		if(getPlayLevelScreenState() == PlayLevelScreenState.RUNNING){
			GamePanel.music("src/gaming.wav", Config.VOLUME);
		}
	}

	public void update() 
	{
		// based on screen state, perform specific actions
		switch (playLevelScreenState) 
		{
		
		// if level is "running" update player and map to keep game logic for the
		// platformer level going
		case RUNNING:
			if (Keyboard.isKeyDown(Key.P) && !keyLocker.isKeyLocked(Key.P)) 
			{
				playLevelScreenState = PlayLevelScreenState.PAUSE;
				keyLocker.lockKey(Key.P);
			} 
			else if (Keyboard.isKeyDown(Key.X) && !keyLocker.isKeyLocked(Key.X)) 
			{
				playLevelScreenState = PlayLevelScreenState.INSTRUCTIONS;
				keyLocker.lockKey(Key.X);
			} 
			else 
			{
				player.update();
				map.update(player);
			}

			if (Keyboard.isKeyUp(Key.P)) 
			{
				keyLocker.unlockKey(Key.P);
			}
			if (Keyboard.isKeyUp(Key.X)) 
			{
				keyLocker.unlockKey(Key.X);
			}

			break;
		// if level has been completed, bring up level cleared screen
		case LEVEL_COMPLETED:
			// Check to see if the level number is less than the index of the last level
			// In this case, the last level is 5, which has an index of 4.
			if(levelNum < 4)
			{
				levelClearedScreen = new LevelClearedScreen();
				levelClearedScreen.initialize();
			}
			// If it is the final level, use the final clear screen instead.
			else
			{
				finalClearScreen = new FinalLevelClearedScreen();
				finalClearScreen.initialize();
			}
			screenTimer.setWaitTime(2500);
			playLevelScreenState = PlayLevelScreenState.LEVEL_WIN_MESSAGE;
			break;
			
		// if level cleared screen is up and the timer is up for how long it should stay
		// out, go back to main menu
		case LEVEL_WIN_MESSAGE:
			if (screenTimer.isTimeUp()) 
			{
				nextLevel();
				playLevelScreenState = PlayLevelScreenState.RUNNING;
			}
			break;
			
		// if player died in level, bring up level lost screen
		case PLAYER_DEAD:
			levelLoseScreen = new LevelLoseScreen(this, screenCoordinator);
			levelLoseScreen.initialize();
			playLevelScreenState = PlayLevelScreenState.LEVEL_LOSE_MESSAGE;
			break;
			
		// wait on level lose screen to make a decision (either resets level or sends
		// player back to main menu)
		case LEVEL_LOSE_MESSAGE:
			levelLoseScreen.update();
			break;
		case LEVEL_SELECT:
			levelSelectScreen.update();
			break;
		case OPTIONS:
			optionsScreen.update();
			break;
		case PAUSE:
			if (Keyboard.isKeyDown(Key.P) && !keyLocker.isKeyLocked(Key.P)) 
			{
				playLevelScreenState = PlayLevelScreenState.RUNNING;
				keyLocker.lockKey(Key.P);
			}
			if (Keyboard.isKeyUp(Key.P)) 
			{
				keyLocker.unlockKey(Key.P);
			}

			break;
		case INSTRUCTIONS:
			if (Keyboard.isKeyDown(Key.X) && !keyLocker.isKeyLocked(Key.X)) 
			{
				playLevelScreenState = PlayLevelScreenState.RUNNING;
				keyLocker.lockKey(Key.X);
			}
			if (Keyboard.isKeyUp(Key.X)) 
			{
				keyLocker.unlockKey(Key.X);
			}

			break;
		}
		
		/*
		 * As of 11/4/21, OptionsScreen.java is part of PlayLevelScreen.java. If user 
		 * is in the play level screen, the "M" key is used to mute. If user is in options 
		 * screen, the mute feature is done by selecting the mute option.  
		 */
		if (this.screenCoordinator.getGameState() == GameState.LEVEL) 
		{
			if ((Keyboard.isKeyDown(Key.M) && keyTimer.isTimeUp()))
	        {
	        	keyTimer.reset();
	        	muteVolume();
	        } 
		}
		
		/*
		 * As of 11/9/21, OptionsScreen.java and InstructionScreen.java are part of 
		 * PlayLevelScreen.java. If user is in the options screen, the sound icon appears 
		 * under the "Volume Control" label. If the user is in instructions screen during
		 * gameplay, the sound icon will appear in the specified location. Otherwise, it
		 * will appear in the left-bottom corner.
		 */
		if (this.screenCoordinator.getGameState() == GameState.OPTIONS)
		{
			soundSprite.setLocation(190, 170);
		}
		else if (this.getPlayLevelScreenState() == PlayLevelScreenState.INSTRUCTIONS)
		{
			soundSprite.setLocation(373, 532);
		}
		else
		{
			soundSprite.setLocation(5, 532);
		}
	}

	@Override
	public void draw(GraphicsHandler graphicsHandler) 
	{
		// based on screen state, draw appropriate graphics
		switch (playLevelScreenState) 
		{
		case RUNNING:
		case LEVEL_COMPLETED:

		case PLAYER_DEAD:
			map.draw(graphicsHandler);
			player.draw(graphicsHandler);
			break;
		case LEVEL_WIN_MESSAGE:
			// Check to see if the level number is less than the index of the last level
			// In this case, the last level is 5, which has an index of 4.
			if(levelNum < 4)
			{
				levelClearedScreen.draw(graphicsHandler);
			}
			// If it is the final level, draw the final clear screen instead.
			else
			{
				finalClearScreen.draw(graphicsHandler);
			}
			break;
		case LEVEL_LOSE_MESSAGE:
			levelLoseScreen.draw(graphicsHandler);
			break;
		case LEVEL_SELECT:
			levelSelectScreen.draw(graphicsHandler);
			break;
		case OPTIONS:
			optionsScreen.draw(graphicsHandler);
			break;
		case PAUSE:
					map.draw(graphicsHandler);
			player.draw(graphicsHandler);
			pauseLabel.draw(graphicsHandler);
			graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(),
					new Color(0, 0, 0, 100));
			break;
		case INSTRUCTIONS:
			map.draw(graphicsHandler);
			player.draw(graphicsHandler);
			instructionLabel.draw(graphicsHandler);
			instruction2Label.draw(graphicsHandler);
			instruction3Label.draw(graphicsHandler);
			instruction4Label.draw(graphicsHandler);
			instruction5Label.draw(graphicsHandler);
			instruction6Label.draw(graphicsHandler);
			returnInstructionLabel.draw(graphicsHandler);

			graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(),
					new Color(0, 0, 0, 100));
			break;
		}
		
		soundSprite.draw(graphicsHandler);
	}
	
	/*
     * Is called from the update function. Mutes the sound if the volume gain
     * is more than one and unmutes if it equals 0 (muted). Changes the icon
     * depending if sound is muted or not. Persistent across screens.
     */
	@Override
	public void muteVolume()
    {
		if (Config.VOLUME > 0)
        {
        	GamePanel.setVolumeMute();
        	Config.VOLUME_SPRITE = "Mute.png";
        	soundSprite.setImage(Config.VOLUME_SPRITE);
        }
        else if (Config.VOLUME == 0)
        {
        	GamePanel.setVolumeMed();
        	Config.VOLUME_SPRITE = "Unmute.png";
        	soundSprite.setImage(Config.VOLUME_SPRITE);
        }
    }


	public PlayLevelScreenState getPlayLevelScreenState() 
	{
		return playLevelScreenState;
	}

	@Override
	public void onLevelCompleted() 
	{
		playLevelScreenState = PlayLevelScreenState.LEVEL_COMPLETED;
	}

	@Override
	public void onDeath() 
	{
		playLevelScreenState = PlayLevelScreenState.PLAYER_DEAD;
	}

	public Map getCurrentMap() 
	{
		if (levelNum == 0) {
			return new TestMap();
		} else if (levelNum == 1) {
			return new TestMap2();
		} else if (levelNum == 2) {
			return new TestMap3();
		} else if (levelNum == 3) {
			return new TestMap4();
		} else if (levelNum == 4) {
			return new TestMap5();
		} else if (levelNum == 5) {
			return new TestMap6();
		} else if (levelNum == 7){
			return new TestMap8();
		} else {
			return new TestMap9();
		}
	}

	public void resetLevel() 
	{
		playLevelScreenState = PlayLevelScreenState.RUNNING;
		initialize();
	}

	public void goBackToMenu() 
	{
		screenCoordinator.setGameState(GameState.MENU);
		GamePanel.music("src/title screen.wav", Config.VOLUME);
	}

	public void nextLevel() 
	{
		levelNum++;
		// Since there are only 9 levels, if the level number is 9 or higher, 
		// all levels have been completed, so the main menu is loaded instead of the next level
		if (levelNum < 9) {
			initialize();
		}
		else {
			goBackToMenu();
		}
	}

	public int getLevelNum() 
	{
		return levelNum;
	}

	public void setLevelNum(int num) 
	{
		levelNum = num;
	}

	public void setPlayLevelScreenState(PlayLevelScreenState state) 
	{
		playLevelScreenState = state;
	}
	
	public Cat getCat() 
	{
		if (catNum == 0) 
		{
			return new Cat("CatGreen.png",map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
		} 
		else if (catNum == 1) 
		{
			return new Cat("CatBlue.png", map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
		}  
		else 
		{
			return new Cat("Cat.png",map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
		}
	}
	
	// This enum represents the different states this screen can be in
	public enum PlayLevelScreenState 
	{
		RUNNING, LEVEL_COMPLETED, PLAYER_DEAD, LEVEL_WIN_MESSAGE, LEVEL_LOSE_MESSAGE, LEVEL_SELECT, PAUSE, INSTRUCTIONS, OPTIONS
	}

	public void setCatNum(int i) 
	{
		catNum = i;
	}
}
