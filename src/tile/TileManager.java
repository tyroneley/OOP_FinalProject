package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[25]; // max number of tile types
        mapTileNum = new int[gp.maxTileCol][gp.maxTileRow];

        getTileImage();
        loadMap("/res/maps/map01.txt");
    }

    // loads the tile images and also sets the collision
    public void getTileImage() {
        setup(0, "dungeon_floor1", false);
        setup(1, "dungeon_floor2", false);
        setup(2, "dungeon_floor3", false);
        setup(3, "dungeon_wall1_1", true);
        setup(4, "dungeon_wall1_2", true);
        setup(5, "dungeon_wall2_1", true);
        setup(6, "dungeon_wall2_2", true);
        setup(7, "dungeon_wall3_1", true);
        setup(8, "dungeon_wall3_2", true);
        setup(9, "spike", false);
        setup(10, "dungeon_wall4_1", true);
        setup(11, "dungeon_wall5_1", true);
    }

    public void setup(int index, String imageName, boolean collision) {
        // UtilityTool utilityTool = new UtilityTool();
        try {
            tile[index] = new Tile(); // initializes new tile object
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/" + imageName + ".png")); // gets the image from the source file
            tile[index].collision = collision; // sets the collision
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadMap(String mapPathName) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(mapPathName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            int col = 0;
            int row = 0;

            // loads the tiles according to the map txt file
            // gets the tile based on number values corresponding to the indexes given during the tile setup method
            // 0 0 0 0 0 would be 1 row of floor tiles of 5 columns
            while (col < gp.maxTileCol && row < gp.maxTileRow) {
                String lineReader = reader.readLine();
                
                while (col < gp.maxTileCol) {
                    String numbers[] = lineReader.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }

                if (col == gp.maxTileCol) {
                    col = 0;
                    row++;
                }
            }

            reader.close();
        } catch (Exception e) {

        }
    }

    public void draw(Graphics2D g2) {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        // draws the tiles after the map is loaded from the text file
        while (col < gp.maxTileCol && row < gp.maxTileRow) {
            int tileNum = mapTileNum[col][row];

            g2.drawImage(tile[tileNum].image, (int)x, (int)y, 32, 32, null);
            col++;
            x += 32;

            if (col == 45) {
                col = 0;
                x = 0;
                row++;
                y += 32;
            }
        }
    }
}
