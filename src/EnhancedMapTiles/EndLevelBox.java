package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.Config;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.Player;
import Level.TileType;
import Utils.Point;

import java.io.File;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

// This class is for the end level gold box tile
// when the player touches it, it will tell the player that the level has been completed
public class EndLevelBox extends EnhancedMapTile {
	public static Clip clip;
	public boolean playedSoundEffect;
	
    public EndLevelBox(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("GoldBox.png"), 16, 16), "DEFAULT", TileType.PASSABLE);
        playedSoundEffect = false;
    }

    @Override
    public void update(Player player) {
        super.update(player);
        if (intersects(player)) {
        	// use boolean flag to play sound effect only once
        	if(!playedSoundEffect)
        	{
        		playWinSound("src/WinSound.wav", Config.VOLUME);
        		playedSoundEffect = true;
        	}
        	player.completeLevel();
            
        }
    }
    
    // find and play audio clip of a given file name
    // implementation is the same as the music but without the looping
    public static void playWinSound(String filepath, double gain) {
    	
		try {
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(filepath));
			clip = AudioSystem.getClip();
			clip.open(audioInput);
			setVolume(gain);
			clip.start();
		} catch (Exception ex) {
			System.out.println("No audio found!");
			ex.printStackTrace();

		}
		
	}
    
    // same as method for the music, needed for playWinSound to function
    public static void setVolume(double gain) {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
		gainControl.setValue(dB);
		
	}

    @Override
    public HashMap<String, Frame[]> getAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0), 500)
                        .withScale(3)
                        .withBounds(1, 1, 14, 14)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 1), 500)
                        .withScale(3)
                        .withBounds(1, 1, 14, 14)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 2), 500)
                        .withScale(3)
                        .withBounds(1, 1, 14, 14)
                        .build()
            });
        }};
    }
}
