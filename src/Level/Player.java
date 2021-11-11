package Level;

import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Game.GameState;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Utils.AirGroundState;
import Utils.Direction;
import Utils.Point;
import Utils.Stopwatch;

import java.util.ArrayList;

import Enemies.Fireball;
import Enemies.DinosaurEnemy.DinosaurState;

public abstract class Player extends GameObject 
{
    // Values that affect player movement
    // these should be set in a subclass
    protected float walkSpeed = 0;
    protected float gravity = 0;
    protected float jumpHeight = 0;
    protected float jumpDegrade = 0;
    protected float terminalVelocityX = 0;

    // Values used to handle player movement
    protected float jumpForce = 0;
    protected float momentumX = 0;
    protected float terminalVelocityY = 0;
    protected float momentumXIncrease = 0;
    protected float momentumYIncrease = 0;

    // Values used to handle player movement
    protected float momentumY = 0;
    protected float moveAmountX, moveAmountY;

    // Values used to keep track of player's current state
    protected GameState levelTwo;
    protected PlayerState playerState;
    protected PlayerState previousPlayerState;
    protected Direction facingDirection;
    protected AirGroundState airGroundState;
    protected AirGroundState previousAirGroundState;
    protected LevelState levelState;

    // Classes that listen to player events can be added to this list
    protected ArrayList<PlayerListener> listeners = new ArrayList<>();

    // Define keys
    protected KeyLocker keyLocker = new KeyLocker();
    protected Key JUMP_KEY = Key.UP;
    protected Key MOVE_LEFT_KEY = Key.LEFT;
    protected Key MOVE_RIGHT_KEY = Key.RIGHT;
    protected Key CROUCH_KEY = Key.DOWN;
    protected Key rightKey = Key.D;
    protected Key leftKey = Key.A;
    protected Key upKey = Key.W;
    protected Key downKey = Key.S;
    protected Key spaceKey = Key.SPACE;
    protected Key attackKey = Key.E;
    
    // jump mechanics
    protected Stopwatch jumpTimer = new Stopwatch();
    protected boolean canJump = true;
    // attack mechanics
    protected boolean canShoot = true;

    // If true, player cannot be hurt by enemies (good for testing)
    protected boolean isInvincible = false;
    
	protected PlayerAttack currentProjectile;

    public Player(SpriteSheet spriteSheet, float x, float y, String startingAnimationName) 
    {
        super(spriteSheet, x, y, startingAnimationName);
        facingDirection = Direction.RIGHT;
        airGroundState = AirGroundState.AIR;
        previousAirGroundState = airGroundState;
        playerState = PlayerState.STANDING;
        previousPlayerState = playerState;
        levelState = LevelState.RUNNING;
        
        currentProjectile = null;
    }

    public void update() 
    {
        moveAmountX = 0;
        moveAmountY = 0;

        // If player is currently playing through level (has not won or lost)
        if (levelState == LevelState.RUNNING) 
        {
            applyGravity();

            // Update player's state and current actions, which includes things like determining how much it should move each frame and if its walking or jumping
            do 
            {
                previousPlayerState = playerState;
                handlePlayerState();
            } while (previousPlayerState != playerState);

            previousAirGroundState = airGroundState;

            // Update player's animation
            super.update();

            // Move player with respect to map collisions based on how much player needs to move this frame
            super.moveYHandleCollision(moveAmountY);
            super.moveXHandleCollision(moveAmountX);

            updateLockedKeys();
        }

        // If player has beaten level
        else if (levelState == LevelState.LEVEL_COMPLETED) 
        {
            updateLevelCompleted();
        }

        // If player has lost level
        else if (levelState == LevelState.PLAYER_DEAD) 
        {
            updatePlayerDead();
        }
    }

    // Add gravity to player, which is a downward force
    protected void applyGravity() 
    {
        moveAmountY += gravity + momentumY;
    }

    // Based on player's current state, call appropriate player state handling method
    protected void handlePlayerState() 
    {
        switch (playerState) 
        {
            case STANDING:
                playerStanding();
                break;
            case WALKING:
                playerWalking();
                break;
            case CROUCHING:
                playerCrouching();
                break;
            case JUMPING:
                playerJumping();
                break;
            case ATTACKING:
            	playerAttacking();
            	break;
        }
    }

    // Player STANDING state logic
    protected void playerStanding() 
    {
        // Sets animation to a STAND animation based on which way player is facing
        currentAnimationName = facingDirection == Direction.RIGHT ? "STAND_RIGHT" : "STAND_LEFT";

        // If walk left or walk right key is pressed, player enters WALKING state
        if (Keyboard.isKeyDown(MOVE_LEFT_KEY) || Keyboard.isKeyDown(MOVE_RIGHT_KEY) || Keyboard.isKeyDown(rightKey) || Keyboard.isKeyDown(leftKey)) 
        {
            playerState = PlayerState.WALKING;
        }

        // If jump key is pressed, player enters JUMPING state
        else if ((Keyboard.isKeyDown(JUMP_KEY) && !keyLocker.isKeyLocked(JUMP_KEY)) 
        			|| (Keyboard.isKeyDown(upKey) && !keyLocker.isKeyLocked(upKey)) 
        			|| (Keyboard.isKeyDown(spaceKey) && !keyLocker.isKeyLocked(spaceKey))) 
        {
            keyLocker.lockKey(JUMP_KEY);
            keyLocker.lockKey(upKey);
            keyLocker.lockKey(spaceKey);
            playerState = PlayerState.JUMPING;
        }
        // If crouch key is pressed, player enters CROUCHING state
        else if (Keyboard.isKeyDown(CROUCH_KEY) || Keyboard.isKeyDown(downKey)) 
        {
            playerState = PlayerState.CROUCHING;
        }
        else if(Keyboard.isKeyDown(attackKey)) 
        {
        	playerState = PlayerState.ATTACKING;
        }
    }

    // Player WALKING state logic
    protected void playerWalking() 
    {
        // Sets animation to a WALK animation based on which way player is facing
        currentAnimationName = facingDirection == Direction.RIGHT ? "WALK_RIGHT" : "WALK_LEFT";

        // If walk left key is pressed, move player to the left
        if (Keyboard.isKeyDown(MOVE_LEFT_KEY) || Keyboard.isKeyDown(leftKey)) 
        {
            moveAmountX -= walkSpeed;
            facingDirection = Direction.LEFT;
        }

        // If walk right key is pressed, move player to the right
        else if (Keyboard.isKeyDown(MOVE_RIGHT_KEY) || Keyboard.isKeyDown(rightKey)) 
        {
        	moveAmountX += walkSpeed;
            facingDirection = Direction.RIGHT;
        } 
        else if (Keyboard.isKeyUp(MOVE_LEFT_KEY) && Keyboard.isKeyUp(MOVE_RIGHT_KEY) && Keyboard.isKeyUp(rightKey) && Keyboard.isKeyUp(leftKey)) 
        {
            playerState = PlayerState.STANDING;
        }

        // If jump key is pressed, player enters JUMPING state
        if ((Keyboard.isKeyDown(JUMP_KEY) && !keyLocker.isKeyLocked(JUMP_KEY)) 
        		|| (Keyboard.isKeyDown(upKey) && !keyLocker.isKeyLocked(upKey)) 
        		|| (Keyboard.isKeyDown(spaceKey) && !keyLocker.isKeyLocked(spaceKey))) 
        {
            keyLocker.lockKey(JUMP_KEY);
            keyLocker.lockKey(upKey);
            keyLocker.lockKey(spaceKey);
            playerState = PlayerState.JUMPING;
        }
        // If crouch key is pressed,
        else if (Keyboard.isKeyDown(CROUCH_KEY) || Keyboard.isKeyDown(downKey))
        {
            playerState = PlayerState.CROUCHING;
        }
        else if(Keyboard.isKeyDown(attackKey)) 
        {
        	keyLocker.lockKey(attackKey);
        	playerState = PlayerState.ATTACKING;
        	//System.out.println(previousPlayerState.toString());
        }
    }

    // Player CROUCHING state logic
    protected void playerCrouching() 
    {
        // Sets animation to a CROUCH animation based on which way player is facing
        currentAnimationName = facingDirection == Direction.RIGHT ? "CROUCH_RIGHT" : "CROUCH_LEFT";

        // If crouch key is released, player enters STANDING state
        if (Keyboard.isKeyUp(CROUCH_KEY) && Keyboard.isKeyUp(downKey)) 
        {
            playerState = PlayerState.STANDING;
        }

        // If jump key is pressed, player enters JUMPING state
        if ((Keyboard.isKeyDown(JUMP_KEY) && !keyLocker.isKeyLocked(JUMP_KEY)) 
        		|| (Keyboard.isKeyDown(upKey) && !keyLocker.isKeyLocked(upKey)) 
        		|| (Keyboard.isKeyDown(spaceKey) && !keyLocker.isKeyLocked(spaceKey))) 
        {
            keyLocker.lockKey(JUMP_KEY);
            keyLocker.lockKey(upKey);
            keyLocker.lockKey(spaceKey);
            playerState = PlayerState.JUMPING;
        }
    }

    // player JUMPING state logic
    protected void playerJumping() {
    	if(Keyboard.isKeyDown(attackKey)) {
    		playerAirAttacking();
    	}
    	else if (Keyboard.isKeyUp(attackKey)){
    		canShoot = true;
    	}
        // if last frame player was on ground and this frame player is still on ground, the jump needs to be setup
        if (previousAirGroundState == AirGroundState.GROUND && airGroundState == AirGroundState.GROUND) {

            // sets animation to a JUMP animation based on which way player is facing
            currentAnimationName = facingDirection == Direction.RIGHT ? "JUMP_RIGHT" : "JUMP_LEFT";

            // player is set to be in air and then player is sent into the air
            jumpTimer.setWaitTime(120);
            jumpTimer.reset();
            airGroundState = AirGroundState.AIR;
            jumpForce = jumpHeight/2 + 2;
            if (jumpForce > 0) {
                moveAmountY -= jumpForce;
                jumpForce -= jumpDegrade/2;
                if (jumpForce < 0) {
                    jumpForce = 0;
                }
            }
        }
        // if player is in air (currently in a jump) and has more jumpForce, continue sending player upwards
        else if (airGroundState == AirGroundState.AIR) {
        	if((Keyboard.isKeyDown(spaceKey) || Keyboard.isKeyDown(JUMP_KEY) || Keyboard.isKeyDown(upKey)) && jumpTimer.isTimeUp() && canJump) {
        		jumpForce = jumpHeight-1;
        		canJump = false;
        	}
            if (jumpForce > 0) {
                moveAmountY -= jumpForce;
                jumpForce -= jumpDegrade;
                
                if (jumpForce < 0) 
                {
                    jumpForce = 0;
                }
            }

            // If player is moving upwards, set player's animation to jump. if player moving downwards, set player's animation to fall
            if (previousY > Math.round(y)) 
            {
                currentAnimationName = facingDirection == Direction.RIGHT ? "JUMP_RIGHT" : "JUMP_LEFT";
            } 
            else 
            {
                currentAnimationName = facingDirection == Direction.RIGHT ? "FALL_RIGHT" : "FALL_LEFT";
            }

            // allows you to move left and right while in the air
            if (Keyboard.isKeyDown(MOVE_LEFT_KEY) || Keyboard.isKeyDown(leftKey)) {
                facingDirection = Direction.LEFT;
                moveAmountX -= walkSpeed;
            } else if (Keyboard.isKeyDown(MOVE_RIGHT_KEY) || Keyboard.isKeyDown(rightKey)) {
                facingDirection = Direction.RIGHT;
                moveAmountX += walkSpeed;
            }

            // If player is falling, increases momentum as player falls so it falls faster over time
            if (moveAmountY > 0) 
            {
                increaseMomentum();
            }
        }
        // if player last frame was in air and this frame is now on ground, player enters STANDING state
        else if (previousAirGroundState == AirGroundState.AIR && airGroundState == AirGroundState.GROUND) {
            playerState = PlayerState.STANDING;
            canShoot = true;
            canJump = true;
        }
    }
    
    public void playerAirAttacking() {
    	int attackX;
        float movementSpeed;
        if (facingDirection == Direction.RIGHT) {
        	attackX = Math.round(getX()) + getScaledWidth() - 20;
            movementSpeed = 1.5f;
        } else {
        	attackX = Math.round(getX());
            movementSpeed = -1.5f;
        }

        // define where projectile will spawn on the map (y location) relative to dinosaur enemy's location
        int attackY = Math.round(getY() + 25);

        // create projectile
        PlayerAttack projectile = new PlayerAttack(new Point(attackX, attackY), movementSpeed, 1000);
        currentProjectile = projectile;

        // add projectile enemy to the map for it to offically spawn in the level
        if(canShoot) {
        	map.addEnemy(projectile);
        	canShoot = false;
        }
    }
    
    // 11/19
    public void playerAttacking() {
    	if (playerState == PlayerState.ATTACKING) {
      
                // define where projectile will spawn on map (x location) relative to cat's location
                // and define its movement speed
                
                //Checks if you're walking before attacking and fixes the cat sprite animation to go back to standing.
                if(currentAnimationName == "WALK_RIGHT"){
                    currentAnimationName = "STAND_RIGHT";
                }

                if(currentAnimationName == "WALK_LEFT"){
                    currentAnimationName = "STAND_LEFT";
                }
                
                int attackX;
                float movementSpeed;
                if (facingDirection == Direction.RIGHT) {
                	attackX = Math.round(getX()) + getScaledWidth() - 20;
                    movementSpeed = 1.5f;
                } else {
                	attackX = Math.round(getX());
                    movementSpeed = -1.5f;
                }

            // define where projectile will spawn on the map (y location) relative to cat's location
            int attackY = Math.round(getY() + 25);

            // Create projectile
            PlayerAttack projectile = new PlayerAttack(new Point(attackX, attackY), movementSpeed, 1000);
            currentProjectile = projectile;

                // add projectile enemy to the map for it to offically spawn in the level
                if(canShoot) {
                	map.addEnemy(projectile);
                	canShoot = false;
                }
                
                //is key up
                if (Keyboard.isKeyUp(attackKey)) {
                	playerState = PlayerState.STANDING;
                	canShoot = true;
                }
            }
         }

    // While player is in air, this is called, and will increase momentumY by a set amount until player reaches terminal velocity
    protected void increaseMomentum() 
    {
        momentumY += momentumYIncrease;
        
        if (momentumY > terminalVelocityY) 
        {
            momentumY = terminalVelocityY;
        }
    }

    protected void increaseMomentumX() 
    {
        momentumX += momentumXIncrease;
        
        if (momentumX > terminalVelocityX) 
        {
            momentumX = terminalVelocityX;
        }
    }

    protected void updateLockedKeys() 
    {
        if (Keyboard.isKeyUp(JUMP_KEY) && Keyboard.isKeyUp(upKey) && Keyboard.isKeyUp(spaceKey)) 
        {
            keyLocker.unlockKey(JUMP_KEY);
            keyLocker.unlockKey(upKey);
            keyLocker.unlockKey(spaceKey);
        }
        else if (Keyboard.isKeyUp(attackKey)) 
        {
        	keyLocker.unlockKey(attackKey);
        }
    }
    
    /*
     * Establishes player level limits; pseudo level boundaries. While the level is not
     * completed, the player cannot move under or over the x-values stated below. If the
     * player completes the level, the player is allowed to pass through right level 
     * limit. The player is scripted to walk off the right boundary of the level to 
     * progress to the next level.
     */
    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction) 
    {
    	if (direction == Direction.LEFT || direction == Direction.RIGHT) 
    	{
    		if (x < 0) 
    		{
    			hasCollided = true;
    			momentumX = 0;
    			setX(0);
    		}
    		
    		if (levelState == LevelState.RUNNING)
    		{
    			if (x > 1560) 
    			{
        			hasCollided = true;
        			momentumX = 0;
        			setX(1560);
    			}
    		}
    	}
    	
    	if (hasCollided && MapTileCollisionHandler.lastCollidedTileX != null) 
    	{
    		if (MapTileCollisionHandler.lastCollidedTileX.getTileType() == TileType.LETHAL) 
    		{
    			levelState = LevelState.PLAYER_DEAD;
    		}
    	}
    }

    @Override
    public void onEndCollisionCheckY(boolean hasCollided, Direction direction) 
    {
        // If player collides with a map tile below it, it is now on the ground.
        // if player does not collide with a map tile below, it is in air
        if (direction == Direction.DOWN) 
        {
            if (hasCollided) 
            {
                momentumY = 0;
                airGroundState = AirGroundState.GROUND;
            } 
            else 
            {
                playerState = PlayerState.JUMPING;
                airGroundState = AirGroundState.AIR;
            }
        }

        // If player collides with map tile upwards, it means it was jumping and then hit into a ceiling -- immediately stop upwards jump velocity
        else if (direction == Direction.UP) 
        {
            if (hasCollided) 
            {
                jumpForce = 0;
            }
        }
        
        if (hasCollided && MapTileCollisionHandler.lastCollidedTileY != null) 
        {
    		if (MapTileCollisionHandler.lastCollidedTileY.getTileType() == TileType.LETHAL) 
    		{
    			levelState = LevelState.PLAYER_DEAD;
    		}
    	}
    }

    // Other entities can call this method to hurt the player
    public void hurtPlayer(MapEntity mapEntity) 
    {
        if (!isInvincible) 
        {
            // If map entity is an enemy, kill player on touch
            if (mapEntity instanceof Enemy) 
            {
                levelState = LevelState.PLAYER_DEAD;
            }
        }
    }

    // Other entities can call this to tell the player they beat a level
    public void completeLevel() 
    {
        levelState = LevelState.LEVEL_COMPLETED;
    }

    // If player has beaten level, this will be the update cycle
    public void updateLevelCompleted() 
    {
        // If player is not on ground, player should fall until it touches the ground
        if (airGroundState != AirGroundState.GROUND && map.getCamera().containsDraw(this)) 
        {
            currentAnimationName = "FALL_RIGHT";
            applyGravity();
            increaseMomentum();
            super.update();
            moveYHandleCollision(moveAmountY);
        }
        // Move player to the right until it walks off screen
        else if (map.getCamera().containsDraw(this)) 
        {
            currentAnimationName = "WALK_RIGHT";
            super.update();
            moveXHandleCollision(walkSpeed);
        } 
        else 
        {
            // Tell all player listeners that the player has finished the level
            for (PlayerListener listener : listeners) 
            {
                listener.onLevelCompleted();
            }
        }
    }

    // If player has died, this will be the update cycle
    public void updatePlayerDead() 
    {
        // Change player animation to DEATH
        if (!currentAnimationName.startsWith("DEATH")) 
        {
            if (facingDirection == Direction.RIGHT) 
            {
                currentAnimationName = "DEATH_RIGHT";
            } 
            else 
            {
                currentAnimationName = "DEATH_LEFT";
            }
            
            super.update();
        }
        // If death animation not on last frame yet, continue to play out death animation
        else if (currentFrameIndex != getCurrentAnimation().length - 1) 
        {
          super.update();
        }
        // If death animation on last frame (it is set up not to loop back to start), player should continually fall until it goes off screen
        else if (currentFrameIndex == getCurrentAnimation().length - 1) 
        {
            if (map.getCamera().containsDraw(this)) 
            {
                moveY(3);
            } 
            else 
            {
                // Tell all player listeners that the player has died in the level
                for (PlayerListener listener : listeners) 
                {
                    listener.onDeath();
                }
            }
        }
    }
    

    public PlayerState getPlayerState() 
    {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) 
    {
        this.playerState = playerState;
    }

    public AirGroundState getAirGroundState() 
    {
        return airGroundState;
    }

    public Direction getFacingDirection() 
    {
        return facingDirection;
    }

    public void setFacingDirection(Direction facingDirection) 
    {
        this.facingDirection = facingDirection;
    }

    public void setLevelState(LevelState levelState) 
    {
        this.levelState = levelState;
    }

    public void addListener(PlayerListener listener) 
    {
        listeners.add(listener);
    } 
}
