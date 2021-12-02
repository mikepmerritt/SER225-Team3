package Level;


import Builders.FrameBuilder;
import Engine.Config;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.Enemy;
import Level.MapEntityStatus;
import Level.Player;
import Utils.Direction;
import Utils.Point;
import Utils.Stopwatch;
import Level.Map;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

// This class is for the fireball enemy that the DinosaurEnemy class shoots out
// it will travel in a straight line (x axis) for a set time before disappearing
// it will disappear early if it collides with a solid map tile
public class PlayerAttack extends Enemy {
    private float movementSpeed;
    private Stopwatch existenceTimer = new Stopwatch();
    
    public static Clip clip;

    public PlayerAttack(Point location, float movementSpeed, int existenceTime) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("Fireball.png"), 7, 7), "DEFAULT");
        this.movementSpeed = movementSpeed;

        // how long the fireball will exist for before disappearing
        existenceTimer.setWaitTime(existenceTime);

        // this enemy will not respawn after it has been removed
        isRespawnable = false;

        initialize();
    }

    @Override
    public void update(Player player) {
        // if timer is up, set map entity status to REMOVED
        // the camera class will see this next frame and remove it permanently from the map
        if (existenceTimer.isTimeUp()) {
            this.mapEntityStatus = MapEntityStatus.REMOVED;
        } else {
            // move attack forward
            moveXHandleCollision(movementSpeed);
            super.update(player);
            ArrayList<Enemy> enemies = map.getActiveEnemies();
            for (int i = 0; i < enemies.size(); i++) {
            	if (intersects(enemies.get(i)) && !(enemies.get(i) instanceof PlayerAttack)) {
            		playSound("src/defeat.wav", Config.VOLUME); // play enemy defeated sound effect
            		enemies.get(i).mapEntityStatus = MapEntityStatus.REMOVED;
            	}
            }
        }
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction) {
        // if fireball collides with anything solid on the x axis, it is removed
        if (hasCollided) {
            this.mapEntityStatus = MapEntityStatus.REMOVED;
        }
    }

    @Override
    public void touchedPlayer(Player player) {
        // if fireball touches player, it disappears
        //super.touchedPlayer(player);
        //this.mapEntityStatus = MapEntityStatus.REMOVED;
    	
    	
    	
    }
    

    @Override
    public HashMap<String, Frame[]> getAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 0)
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build()
            });
        }};
    }
    
    // find and play audio clip of a given file name
    // implementation is the same as the music but without the looping
    public static void playSound(String filepath, double gain) {
    	
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
    
    // same as method for the music, needed for playSound to function
    public static void setVolume(double gain) {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
		gainControl.setValue(dB);
		
	}
}
