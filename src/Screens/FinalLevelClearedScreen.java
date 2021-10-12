package Screens;

import Engine.Config;
import Engine.GraphicsHandler;
import Engine.Screen;
import Engine.ScreenManager;
import SpriteFont.SpriteFont;

import java.awt.*;

// This class is for the final level cleared screen. After this screen, the program
// goes back to the main menu instead of starting another level.
public class FinalLevelClearedScreen extends Screen 
{
    protected SpriteFont winMessage;
    protected Color backgroundColor;

    public FinalLevelClearedScreen() 
    {
    	backgroundColor = new Color(21, 149, 62);
    }

    @Override
    public void initialize() 
    {
        winMessage = new SpriteFont("You completed all of the levels!", 180, 270, "Comic Sans", 30, Color.white);
    }

    @Override
    public void update() 
    {

    }

    // Draws the green background and white message to the screen.
    public void draw(GraphicsHandler graphicsHandler) 
    {
        graphicsHandler.drawFilledRectangle(0, 0, Config.WIDTH, Config.HEIGHT, backgroundColor);
        winMessage.draw(graphicsHandler);
    }
}
