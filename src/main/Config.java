package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class Config {
    GamePanel gp;
    public int highScore = 0;

    public Config(GamePanel gp) {
        this.gp = gp;
    }

    // saves high score
    public void saveConfig() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("config.txt"));
            // compares current score and high score
            if (gp.player.hasStars > highScore) {  
                writer.write(Integer.toString(gp.player.hasStars));
            } else {
                // saves high score in case current high score is not 0
                writer.write(Integer.toString(highScore));
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // loads high score data
    public void loadConfig() {
        try {
        BufferedReader reader = new BufferedReader(new FileReader("config.txt"));
        String stars = reader.readLine();
        if (stars != null) {
            highScore = Integer.parseInt(stars);
            System.out.println(highScore);
        }
        reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
