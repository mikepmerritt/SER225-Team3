package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.ImageEffect;
import GameObject.Sprite;
import Level.Map;
import Maps.TitleScreenMap;
import SpriteFont.SpriteFont;
import Utils.Stopwatch;

import java.awt.*;

// This is the class for the main menu screen
public class MenuScreen extends Screen 
{
    protected ScreenCoordinator screenCoordinator;
    
    protected SpriteFont playGame;
    protected SpriteFont levelSelect;
    protected SpriteFont instructions;
    protected SpriteFont options;
    protected SpriteFont credits;
    protected Sprite soundSprite;
    
    protected Map background;
    
    protected Stopwatch keyTimer = new Stopwatch();
    protected KeyLocker keyLocker = new KeyLocker();
    
    protected int currentMenuItemHovered = 0; // current menu item being "hovered" over
    protected int menuItemSelected = -1;
    
    protected int pointerLocationX, pointerLocationY;

    public MenuScreen(ScreenCoordinator screenCoordinator) 
    {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() 
    {
        playGame = new SpriteFont("PLAY GAME", 80, 100, "Comic Sans", 30, new Color(49, 207, 240));
        playGame.setOutlineColor(Color.black);
        playGame.setOutlineThickness(3);
        
        levelSelect = new SpriteFont("LEVEL SELECT", 80, 200, "Comic Sans", 30, new Color(49, 207, 240));
        levelSelect.setOutlineColor(Color.black);
        levelSelect.setOutlineThickness(3);
        
        instructions = new SpriteFont("INSTRUCTIONS", 80, 300, "Comic Sans", 30, new Color(49,207,240));
        instructions.setOutlineColor(Color.black);
        instructions.setOutlineThickness(3);
        
        options = new SpriteFont("OPTIONS", 350, 100, "Comic Sans", 30, new Color(49,207,240));
        options.setOutlineColor(Color.black);
        options.setOutlineThickness(3);
        
        credits = new SpriteFont("CREDITS", 350, 200, "Comic Sans", 30, new Color(49,207,240));
        credits.setOutlineColor(Color.black);
        credits.setOutlineThickness(3);
        
        soundSprite = new Sprite(ImageLoader.load(Config.VOLUME_SPRITE), 5, 532, 0.25f, ImageEffect.NONE);
        
        background = new TitleScreenMap();
        background.setAdjustCamera(false);
        
        keyTimer.setWaitTime(200);
        keyLocker.lockKey(Key.SPACE);
        
        menuItemSelected = -1;
    }

    @Override
    public void update() 
    {
        // update background map (to play tile animations)
    	background.update(null);

        // if down or up is pressed, change menu item "hovered" over (blue square in front of text will move along with currentMenuItemHovered changing)
        if ((Keyboard.isKeyDown(Key.DOWN) || Keyboard.isKeyDown(Key.S)) && keyTimer.isTimeUp()) 
        {
            keyTimer.reset();
            if(currentMenuItemHovered != 2 && currentMenuItemHovered != 5) { currentMenuItemHovered++; }
        } 
        else if ((Keyboard.isKeyDown(Key.UP) || Keyboard.isKeyDown(Key.W)) && keyTimer.isTimeUp()) 
        {
            keyTimer.reset();
            if(currentMenuItemHovered != 3 && currentMenuItemHovered != 0) { currentMenuItemHovered--; }
        } 
        else if ((Keyboard.isKeyDown(Key.LEFT) || Keyboard.isKeyDown(Key.A)) && keyTimer.isTimeUp()) 
        {
            keyTimer.reset();
            if(currentMenuItemHovered >= 3) { currentMenuItemHovered -= 3; }
        } 
        else if ((Keyboard.isKeyDown(Key.RIGHT) || Keyboard.isKeyDown(Key.D)) && keyTimer.isTimeUp()) 
        {
            keyTimer.reset();
            if(currentMenuItemHovered < 3) { currentMenuItemHovered += 3; }
        }
        
        if ((Keyboard.isKeyDown(Key.M) && keyTimer.isTimeUp()))
        {
        	keyTimer.reset();
        	muteVolume();
        }
        
        // if down is pressed on last menu item or up is pressed on first menu item, "loop" the selection back around to the beginning/end
        if (currentMenuItemHovered > 4) 
        {
            currentMenuItemHovered = 0;
        } 
        else if (currentMenuItemHovered < 0) 
        {
            currentMenuItemHovered = 4;
        }

        // sets location for blue square in front of text (pointerLocation) and also sets color of spritefont text based on which menu item is being hovered
        if (currentMenuItemHovered == 0) 
        {
            playGame.setColor(new Color(255, 215, 0));
            levelSelect.setColor(new Color(49, 207, 240));
            instructions.setColor(new Color(49, 207, 240));
            options.setColor(new Color(49, 207, 240));
            credits.setColor(new Color(49, 207, 240));
            pointerLocationX = 50;
            pointerLocationY = 80;
        } 
        else if (currentMenuItemHovered == 1) 
        {
        	playGame.setColor(new Color(49, 207, 240));
            levelSelect.setColor(new Color(255, 215, 0));
            instructions.setColor(new Color(49, 207, 240));
            options.setColor(new Color(49, 207, 240));
            credits.setColor(new Color(49, 207, 240));
            pointerLocationX = 50;
            pointerLocationY = 180;
        } 
        else if (currentMenuItemHovered == 2) 
        {
        	playGame.setColor(new Color(49, 207, 240));
            levelSelect.setColor(new Color(49, 207, 240));
            instructions.setColor(new Color(255, 215, 0));
            options.setColor(new Color(49, 207, 240));
            credits.setColor(new Color(49, 207, 240));
            pointerLocationX = 50;
            pointerLocationY = 280;
            
        }
        else if (currentMenuItemHovered == 3) 
        {
        	playGame.setColor(new Color(49, 207, 240));
            levelSelect.setColor(new Color(49, 207, 240));
            instructions.setColor(new Color(49, 207, 240));
            options.setColor(new Color(255, 215, 0));
            credits.setColor(new Color(49, 207, 240));
            pointerLocationX = 320;
            pointerLocationY = 80;
        }
        else if (currentMenuItemHovered == 4) 
        {
        	playGame.setColor(new Color(49, 207, 240));
            levelSelect.setColor(new Color(49, 207, 240));
            instructions.setColor(new Color(49, 207, 240));
            options.setColor(new Color(49, 207, 240));
            credits.setColor(new Color(255, 215, 0));
            pointerLocationX = 320;
            pointerLocationY = 180;
        }
        
        
        // if space is pressed on menu item, change to appropriate screen based on which menu item was chosen
        if (Keyboard.isKeyUp(Key.SPACE)) 
        {
            keyLocker.unlockKey(Key.SPACE);
        }
        
        /*
         * Main Menu Options. When the "space" key is pressed, the selected option
         * will change the active screen to the specified game state below.
         */
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) 
        {
            menuItemSelected = currentMenuItemHovered;
            
            if (menuItemSelected == 0) 
            {
                screenCoordinator.setGameState(GameState.OPENING);
            } 
            else if (menuItemSelected == 1) 
            {
                screenCoordinator.setGameState(GameState.LEVELSELECT);
            }
            else if (menuItemSelected == 2)
            {
            	screenCoordinator.setGameState(GameState.INSTRUCTIONS);
            }
            else if (menuItemSelected == 3) 
            {
            	screenCoordinator.setGameState(GameState.OPTIONS);
            }
            else if (menuItemSelected == 4) 
            {
            	screenCoordinator.setGameState(GameState.CREDITS);
            }
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) 
    {
    	background.draw(graphicsHandler);
        playGame.draw(graphicsHandler);
        levelSelect.draw(graphicsHandler);
        instructions.draw(graphicsHandler);
        options.draw(graphicsHandler);
        credits.draw(graphicsHandler);
        soundSprite.draw(graphicsHandler);
        
        graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20, new Color(49, 207, 240), Color.black, 2);
    }

    public int getMenuItemSelected() 
    {
        return menuItemSelected;
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
}
