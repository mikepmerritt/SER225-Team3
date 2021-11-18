package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.ImageEffect;
import GameObject.Sprite;
import Level.Map;
import Maps.LevelSelectMap;
import Maps.TitleScreenMap;
import Screens.PlayLevelScreen.PlayLevelScreenState;
import SpriteFont.SpriteFont;
import Utils.Stopwatch;

import java.awt.*;

// This is the class for the main menu screen
public class LevelSelectScreen extends Screen 
{
	protected PlayLevelScreen playLevelScreen;
	
	protected SpriteFont levelOne;
	protected SpriteFont levelTwo;
	protected SpriteFont levelThree;
	protected SpriteFont levelFour;
	protected SpriteFont levelFive;
	protected SpriteFont levelSix;
	protected SpriteFont levelSeven;
	protected SpriteFont levelEight;
	protected SpriteFont levelNine;
	
	protected Map background;
	
	protected Stopwatch keyTimer = new Stopwatch();
	protected KeyLocker keyLocker = new KeyLocker();
	
	protected Color selected = new Color(255, 215, 0);
	protected Color notSelected = new Color(49, 207, 240);
	
	protected int currentLevelHovered = 0; // current menu item being "hovered" over
	protected int LevelSelected = -1;
	protected int pointerLocationX, pointerLocationY;

	public LevelSelectScreen(PlayLevelScreen screen) 
	{
		this.playLevelScreen = screen;
	}

	@Override
	public void initialize() 
	{
		levelOne = new SpriteFont("Level One", 100, 100, "Comic Sans", 30, new Color(49, 207, 240));
		levelOne.setOutlineColor(Color.black);
		levelOne.setOutlineThickness(3);
		
		levelTwo = new SpriteFont("Level Two", 100, 180, "Comic Sans", 30, new Color(49, 207, 240));
		levelTwo.setOutlineColor(Color.black);
		levelTwo.setOutlineThickness(3);
		
		levelThree = new SpriteFont("Level Three", 100, 260, "Comic Sans", 30, new Color(49, 207, 240));
		levelThree.setOutlineColor(Color.black);
		levelThree.setOutlineThickness(3);
		
		levelFour = new SpriteFont("Level Four", 100, 340, "Comic Sans", 30, new Color(49, 207, 240));
		levelFour.setOutlineColor(Color.black);
		levelFour.setOutlineThickness(3);
		
		levelFive = new SpriteFont("Level Five", 100, 420, "Comic Sans", 30, new Color(49, 207, 240));
		levelFive.setOutlineColor(Color.black);
		levelFive.setOutlineThickness(3);
		
		levelSix = new SpriteFont("Level Six", 360, 100, "Comic Sans", 30, new Color(49, 207, 240));
		levelSix.setOutlineColor(Color.black);
		levelSix.setOutlineThickness(3);
		
		levelSeven = new SpriteFont("Level Seven", 360, 180, "Comic Sans", 30, new Color(49, 207, 240));
		levelSeven.setOutlineColor(Color.black);
		levelSeven.setOutlineThickness(3);
		
		levelEight = new SpriteFont("Level Eight", 360, 260, "Comic Sans", 30, new Color(49, 207, 240));
		levelEight.setOutlineColor(Color.black);
		levelEight.setOutlineThickness(3);
		
		levelNine = new SpriteFont("Level Nine", 360, 340, "Comic Sans", 30, new Color(49, 207, 240));
		levelNine.setOutlineColor(Color.black);
		levelNine.setOutlineThickness(3);
		
		background = new LevelSelectMap();
		background.setAdjustCamera(false);
		
		keyTimer.setWaitTime(200);
		keyLocker.lockKey(Key.SPACE);
	}

	@Override
	public void update() 
	{
		// update background map (to play tile animations)
		background.update(null);

		// if down or up is pressed, change menu item "hovered" over (blue square in
		// front of text will move along with currentMenuItemHovered changing)
		if ((Keyboard.isKeyDown(Key.DOWN) || Keyboard.isKeyDown(Key.S)) && keyTimer.isTimeUp()) 
		{
			keyTimer.reset();
			if(currentLevelHovered != 8)
				currentLevelHovered++;
		} 
		else if ((Keyboard.isKeyDown(Key.UP) || Keyboard.isKeyDown(Key.W)) && keyTimer.isTimeUp()) 
		{
			keyTimer.reset();
			if(currentLevelHovered != 0)
				currentLevelHovered--;
		}
		else if ((Keyboard.isKeyDown(Key.LEFT) || Keyboard.isKeyDown(Key.A)) && keyTimer.isTimeUp()) 
		{
			keyTimer.reset();
			if(currentLevelHovered > 4 && currentLevelHovered-5 >= 0) {
				currentLevelHovered -= 5;
			}
			
		}
		else if ((Keyboard.isKeyDown(Key.RIGHT) || Keyboard.isKeyDown(Key.D)) && keyTimer.isTimeUp()) 
		{
			keyTimer.reset();
			if(currentLevelHovered != 4 && currentLevelHovered+5 <= 8)
				currentLevelHovered += 5;
		}

		// sets location for blue square in front of text (pointerLocation) and also
		// sets color of spritefont text based on which menu item is being hovered
		switch(currentLevelHovered) {
			case 0: levelNine.setColor(notSelected);
					levelOne.setColor(selected);
					levelTwo.setColor(notSelected);
					levelSix.setColor(notSelected);
					pointerLocationX = 70;
					pointerLocationY = 80;
					break;
			case 1: levelOne.setColor(notSelected);
					levelTwo.setColor(selected);
					levelThree.setColor(notSelected);
					levelSeven.setColor(notSelected);
					pointerLocationX = 70;
					pointerLocationY = 160;
					break;
			case 2: levelTwo.setColor(notSelected);
					levelThree.setColor(selected);
					levelFour.setColor(notSelected);
					levelEight.setColor(notSelected);
					pointerLocationX = 70;
					pointerLocationY = 240;
					break;
			case 3: levelThree.setColor(notSelected);
					levelFour.setColor(selected);
					levelFive.setColor(notSelected);
					levelNine.setColor(notSelected);
					pointerLocationX = 70;
					pointerLocationY = 320;
					break;
			case 4: levelFour.setColor(notSelected);
					levelFive.setColor(selected);
					levelSix.setColor(notSelected);
					pointerLocationX = 70;
					pointerLocationY = 400;
					break;
			case 5: levelFive.setColor(notSelected);
					levelSix.setColor(selected);
					levelSeven.setColor(notSelected);
					levelOne.setColor(notSelected);
					pointerLocationX = 330;
					pointerLocationY = 80;
					break;
			case 6: levelSix.setColor(notSelected);
					levelSeven.setColor(selected);
					levelEight.setColor(notSelected);
					levelTwo.setColor(notSelected);
					pointerLocationX = 330;
					pointerLocationY = 160;
					break;
			case 7: levelSeven.setColor(notSelected);
					levelEight.setColor(selected);
					levelNine.setColor(notSelected);
					levelThree.setColor(notSelected);
					pointerLocationX = 330;
					pointerLocationY = 240;
					break;
			case 8: levelEight.setColor(notSelected);
					levelNine.setColor(selected);
					levelOne.setColor(notSelected);
					levelFour.setColor(notSelected);
					pointerLocationX = 330;
					pointerLocationY = 320;
					break;
		}
		
		
		LevelSelected = currentLevelHovered;
		
		// if space is pressed on menu item, change to appropriate screen based on which
		// menu item was chosen
		if (Keyboard.isKeyUp(Key.SPACE)) 
		{
			keyLocker.unlockKey(Key.SPACE);
		}
		
		if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) 
		{
			switch(LevelSelected) {
				case 0: playLevelScreen.screenCoordinator.setGameState(GameState.OPENING);
						break;
				case 1: playLevelScreen.setLevelNum(1);
						playLevelScreen.setPlayLevelScreenState(PlayLevelScreenState.RUNNING);
						playLevelScreen.initialize();
						break;
				case 2: playLevelScreen.setLevelNum(2);
						playLevelScreen.setPlayLevelScreenState(PlayLevelScreenState.RUNNING);
						playLevelScreen.initialize();
						break;
				case 3: playLevelScreen.setLevelNum(3);
						playLevelScreen.setPlayLevelScreenState(PlayLevelScreenState.RUNNING);
						playLevelScreen.initialize();
						break;
				case 4: playLevelScreen.setLevelNum(4);
						playLevelScreen.setPlayLevelScreenState(PlayLevelScreenState.RUNNING);
						playLevelScreen.initialize();
						break;
				case 5: playLevelScreen.setLevelNum(5);
						playLevelScreen.setPlayLevelScreenState(PlayLevelScreenState.RUNNING);
						playLevelScreen.initialize();
						break;
				case 6: playLevelScreen.setLevelNum(6);
						playLevelScreen.setPlayLevelScreenState(PlayLevelScreenState.RUNNING);
						playLevelScreen.initialize();
						break;
				case 7: playLevelScreen.setLevelNum(7);
						playLevelScreen.setPlayLevelScreenState(PlayLevelScreenState.RUNNING);
						playLevelScreen.initialize();
						break;
				case 8: playLevelScreen.setLevelNum(8);
						playLevelScreen.setPlayLevelScreenState(PlayLevelScreenState.RUNNING);
						playLevelScreen.initialize();
						break;
				case 9: playLevelScreen.setLevelNum(9);
						playLevelScreen.setPlayLevelScreenState(PlayLevelScreenState.RUNNING);
						playLevelScreen.initialize();
						break;
						
			}
		}
	}

	@Override
	public void draw(GraphicsHandler graphicsHandler) 
	{
		background.draw(graphicsHandler);
		levelOne.draw(graphicsHandler);
		levelTwo.draw(graphicsHandler);
		levelThree.draw(graphicsHandler);
		levelFour.draw(graphicsHandler);
		levelFive.draw(graphicsHandler);
		levelSix.draw(graphicsHandler);
		levelSeven.draw(graphicsHandler);
		levelEight.draw(graphicsHandler);
		levelNine.draw(graphicsHandler);

		graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20,
				new Color(49, 207, 240), Color.black, 2);
	}
	
	@Override
	public void muteVolume() { }

	public int getLevelSelected() 
	{
		return LevelSelected;
	}
}
