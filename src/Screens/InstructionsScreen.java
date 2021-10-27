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

// This class is for the instructions screen
public class InstructionsScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected Map background;
    protected KeyLocker keyLocker = new KeyLocker();
    protected Stopwatch keyTimer = new Stopwatch();
    protected SpriteFont instructionsJump;
    protected SpriteFont instructions2Label;
    protected SpriteFont instructions3Label;
    protected SpriteFont instructions4Label;
    protected SpriteFont instructions5Label;
    protected SpriteFont instructions6Label;
    protected SpriteFont instructions7Label;
    protected SpriteFont returnInstructionsLabel;
    protected SpriteFont extraInfo;
    protected SpriteFont extraInfo2;
    protected Sprite soundSprite;
    
    public InstructionsScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // setup graphics on screen (background map, spritefont text)
        background = new TitleScreenMap();
        background.setAdjustCamera(false);
        instructionsJump = new SpriteFont("To JUMP: UP arrow key, or 'W', or SPACEBAR", 130, 100, "Times New Roman", 20, Color.darkGray);
        instructions2Label = new SpriteFont("To MOVE LEFT: LEFT arrow key, or 'A'", 130, 140, "Times New Roman", 20, Color.darkGray);
        instructions3Label = new SpriteFont("To MOVE RIGHT: RIGHT arrow key, or 'D'", 130, 180, "Times New Roman",20, Color.darkGray);
        instructions4Label = new SpriteFont("To CROUCH: DOWN arrow key, or 'S'", 130,220, "Times New Roman", 20, Color.darkGray);
        instructions5Label = new SpriteFont("To ATTACK: E key", 130, 260, "Times New Roman", 20, Color.darkGray);
        instructions6Label = new SpriteFont("To INTERACT: F key", 130, 300, "Times New Roman", 20, Color.darkGray);
        instructions7Label = new SpriteFont("To MUTE VOLUME: M key", 130, 340, "Times New Roman", 20, Color.darkGray);
        
        soundSprite = new Sprite(ImageLoader.load(Config.VOLUME_SPRITE), 370, 318, 0.25f, ImageEffect.NONE);
      
        returnInstructionsLabel = new SpriteFont("Press SPACE to return to the menu", 20, 560, "Times New Roman", 30, Color.white);
        
        extraInfo = new SpriteFont("PRESS P for PAUSE", 90, 400, "Times New Roman", 20,Color.darkGray);
        extraInfo2 = new SpriteFont("PRESS X for INSTRUCTIONS", 90, 422, "Times New Roman", 20,Color.darkGray);
        keyLocker.lockKey(Key.SPACE);
        keyTimer.setWaitTime(200);
    }

    @Override
    public void update() {
        background.update(null);

        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }

        // if space is pressed, go back to main menu
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            screenCoordinator.setGameState(GameState.MENU);
        }
        
        if ((Keyboard.isKeyDown(Key.M) && keyTimer.isTimeUp()))
        {
        	keyTimer.reset();
        	muteVolume();
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        instructions2Label.draw(graphicsHandler);
        instructions3Label.draw(graphicsHandler);
        instructions4Label.draw(graphicsHandler);
        instructions5Label.draw(graphicsHandler);
        instructions6Label.draw(graphicsHandler);
        instructions7Label.draw(graphicsHandler);
        instructionsJump.draw(graphicsHandler);
        
        extraInfo.draw(graphicsHandler);
        extraInfo2.draw(graphicsHandler);
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
