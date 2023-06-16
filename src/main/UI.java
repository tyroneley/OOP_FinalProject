package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import entity.Entity;
import object.OBJ_Heart;
import object.OBJ_Star;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Spritesheet heart_full;
    Spritesheet heart_blank;
    Spritesheet starImage;
    
    public UI(GamePanel gp) {
        this.gp = gp;

        Entity star = new OBJ_Star(gp);
        starImage = star.spriteSheet1;

        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.spriteSheet1;
        heart_blank = heart.spritesheet2;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        String text = " " + gp.player.hasStars;
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        g2.setColor(Color.WHITE);
        g2.drawImage(starImage.getSprite(0), (int)16, (int)16, 32, 32, null);
        g2.drawString(text, 50, 49);;

        text = " " + gp.config.highScore;
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        g2.setColor(Color.YELLOW);
        g2.drawImage(starImage.getSprite(0), (int)16, (int)16, 32, 32, null);
        g2.drawString(text, 50, 92);;


        drawPlayerLife();

        if (gp.gameState == gp.playState) {

        }

        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }

        if (gp.gameState == gp.gameOverState) {
            gp.config.saveConfig();
            drawGameOverScreen();
        }
    }

    // gets the center X coordinate for the text based on the width of the screen
    // uses length of string to get the bounds
    public int getXForCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = (gp.screenWidth / 2) - (length / 2);
        return x;
    }

    // draws game over screen
    public void drawGameOverScreen() {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(new Font("Arial", Font.BOLD, 40));
       
        text = "Your run is over";
        
        g2.setColor(Color.white);
        x = getXForCenteredText(text);
        y = gp.tileSize * 2;
        g2.drawString(text, x, y);

        int highScore = gp.config.highScore;
        if (gp.player.hasStars > highScore) {
            text = "New high score!";
            g2.setColor(Color.white);
            x = getXForCenteredText(text);
            y = gp.tileSize * 4;
            g2.drawString(text, x, y);

            text = Integer.toString(gp.player.hasStars) + " stars";
            g2.setColor(Color.white);
            x = getXForCenteredText(text);
            y = gp.tileSize * 5;
            g2.drawString(text, x, y);
        }
    }

    // draws pause screen
    public void drawPauseScreen() {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        String text = "Need a break?";
        int x = getXForCenteredText(text);
        int y = gp.screenHeight / 2;

        g2.setColor(Color.white);
        g2.drawString(text, x, y);
    }

    public void drawPlayerLife() {
        double x = 32;
        double y = 675;
        int i = 0;

        while (i < gp.player.maxLife) {
            g2.drawImage(heart_blank.getSprite(0), (int)x, (int)y, 64, 64, null);
            i++;
            x += 64;
        }

        x = 32;
        y = 675;
        i = 0;

        while (i < gp.player.life) {
            g2.drawImage(heart_full.getSprite(0), (int)x, (int)y, 64, 64, null);
            i++;
            x += 64;
        }
    }
}
