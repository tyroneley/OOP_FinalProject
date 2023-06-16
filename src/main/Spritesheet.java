package main;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;


public class Spritesheet {
    private BufferedImage image;
    private int spriteWidth;
    private int spriteHeight;
    private int currentFrame = 0;
    private int maxFrames = 0;

    // creates an object of spritesheet with the given image
    // maxFrames == row since the max number of frames per column is the number of rows
    public Spritesheet(String imagePath, int spriteWidth, int spriteHeight, int row, int col) {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (IOException e) {
            System.out.println("Spritesheet Init Error");
            System.exit(0);
        }
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.maxFrames = row;
    }
    
    public BufferedImage getSprite(int row) {
        // resets column
        if (this.currentFrame == this.maxFrames) {
            this.currentFrame = 0;
        }

        int x = this.currentFrame * this.spriteWidth;
        int y = row * this.spriteHeight;

        // for spritesheets that are one tile, or 1 row and 1 column
        if (x == 8 && y == 0 && this.maxFrames == 1) {
            return image;
        }

        return image.getSubimage(x, y, this.spriteWidth, this.spriteHeight);
    }

    // increases current frame to move spritesheet subimage to the right by one
    public void moveFrame() {
        if (this.currentFrame == this.maxFrames) {
            this.currentFrame = 0;
        } else {
            this.currentFrame++;
        }
    }
    
    public int getSpriteWidth() {
        return this.spriteWidth;
    }

    public int getSpriteHeight() {
        return this.spriteHeight;
    }
}
