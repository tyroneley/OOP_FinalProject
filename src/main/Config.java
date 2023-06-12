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

    public void saveConfig() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("config.txt"));
            //System.out.println(gp.player.hasStars + " " + highScore);
            if (gp.player.hasStars > highScore) {  
                writer.write(Integer.toString(gp.player.hasStars));
            } else {
                writer.write(Integer.toString(highScore));
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
