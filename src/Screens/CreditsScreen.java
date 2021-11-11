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

// This class is for the credits screen
public class CreditsScreen extends Screen 
{
    protected ScreenCoordinator screenCoordinator;
    protected Map background;
    
    protected SpriteFont creditsLabel;
    protected SpriteFont createdByLabel;
    protected SpriteFont contributorsLabel;
    protected SpriteFont returnInstructionsLabel;
    protected Sprite soundSprite;
    
    protected Stopwatch keyTimer = new Stopwatch();
    protected KeyLocker keyLocker = new KeyLocker();

    public CreditsScreen(ScreenCoordinator screenCoordinator) 
    {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() 
    {
        // setup graphics on screen (background map, spritefont text)
        background = new TitleScreenMap();
        background.setAdjustCamera(false);
        creditsLabel = new SpriteFont("Credits", 15, 35, "Times New Roman", 30, Color.white);
        createdByLabel = new SpriteFont("Created by Alex Thimineur for Quinnipiac's SER225 Course.", 130, 140, "Times New Roman", 20, Color.white);
        contributorsLabel = new SpriteFont("Thank you to QU Alumni Brian Carducci, Joseph White,\nand Alex Hutman for their contributions.", 60, 220, "Times New Roman",20, Color.white);
        returnInstructionsLabel = new SpriteFont("Press X to return to the menu", 5, 560, "Times New Roman", 30, Color.white);
        
        soundSprite = new Sprite(ImageLoader.load(Config.VOLUME_SPRITE), 373, 532, 0.25f, ImageEffect.NONE);
        
        keyTimer.setWaitTime(200);
        keyLocker.lockKey(Key.SPACE);
    }

    @Override
    public void update() 
    {
        background.update(null);

        if (Keyboard.isKeyUp(Key.SPACE)) 
        {
            keyLocker.unlockKey(Key.SPACE);
        }

        // if space is pressed, go back to main menu
        if (!keyLocker.isKeyLocked(Key.X) && Keyboard.isKeyDown(Key.X)) 
        {
            screenCoordinator.setGameState(GameState.MENU);
        }
        
        if ((Keyboard.isKeyDown(Key.M) && keyTimer.isTimeUp()))
        {
        	keyTimer.reset();
        	muteVolume();
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) 
    {
        background.draw(graphicsHandler);
        creditsLabel.draw(graphicsHandler);
        createdByLabel.draw(graphicsHandler);
        contributorsLabel.drawWithParsedNewLines(graphicsHandler);
        returnInstructionsLabel.draw(graphicsHandler);
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
}
