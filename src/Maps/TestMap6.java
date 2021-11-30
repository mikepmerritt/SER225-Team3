package Maps;

import java.util.ArrayList;

import Engine.ImageLoader;
import EnhancedMapTiles.EndLevelBox;
import EnhancedMapTiles.HorizontalMovingPlatform;
import GameObject.Rectangle;
import Level.EnhancedMapTile;
import Level.Map;
import Level.TileType;
import Tilesets.CommonTileset;
import Utils.Direction;
import Utils.Point;

public class TestMap6 extends Map 
{
	public TestMap6()
	{
		super("test_map6.txt", new CommonTileset(), new Point(2, 15));
	}
	
	//Add dynamic tiles to level
	public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() 
	{
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        enhancedMapTiles.add(new HorizontalMovingPlatform(
        		ImageLoader.load("GreenPlatform.png"), //Platform image
                getPositionByTileIndex(13, 9), 		   //Start location
                getPositionByTileIndex(17, 6), 		   //End location
                TileType.JUMP_THROUGH_PLATFORM, 	   //Tile behavior
                3, 									   //Size
                new Rectangle(0, 6, 16, 4), 		   //Bounds
                Direction.LEFT 						   //Start direction
        ));
        
        enhancedMapTiles.add(new HorizontalMovingPlatform(
        		ImageLoader.load("GreenPlatform.png"),
                getPositionByTileIndex(19, 9),
                getPositionByTileIndex(24, 9),
                TileType.JUMP_THROUGH_PLATFORM, 
                3, 
                new Rectangle(0, 6, 16, 4), 
                Direction.LEFT
        ));
        
        enhancedMapTiles.add(new HorizontalMovingPlatform(
        		ImageLoader.load("GreenPlatform.png"),
                getPositionByTileIndex(25, 9),
                getPositionByTileIndex(30, 9),
                TileType.JUMP_THROUGH_PLATFORM, 
                3, 
                new Rectangle(0, 6, 16, 4), 
                Direction.LEFT
        ));

        enhancedMapTiles.add(new EndLevelBox(getPositionByTileIndex(32, 12))); //Level Completion Box

        return enhancedMapTiles;
    }
}
