package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;

import AI.Pathfinder;
import entity.Player;
import entity.Entity;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 32;
    final int scale = 3;
    
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 15;
    public final int maxScreenRow = 8;
    public final int maxTileCol = 45;
    public final int maxTileRow = 24;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    int FPS = 60;

    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public Pathfinder pathfinder = new Pathfinder(this);
    Thread gameThread;
    public Collision collision = new Collision(this);
    public AssetSetter assetSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eventHandler = new EventHandler(this);

    public Player player = new Player(this, keyH);
    public Entity obj[] = new Entity[100];
    public final int maxMonsters = 6;
    public Entity monsters[] = new Entity[maxMonsters];
    public ArrayList<Entity> projectileList = new ArrayList<>();
    Config config = new Config(this);
    ArrayList<Entity> entityList = new ArrayList<>();

    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int gameOverState = 3;

    public GamePanel() {
        this.setPreferredSize(new DimensionUIResource(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseMotionListener(keyH);
        this.addMouseListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() {
        config.loadConfig();
        assetSetter.setObject();
        assetSetter.setMonster();
        gameState = playState;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }

            if (timer >= 1000000000) {
                timer = 0;
            }
        }
    }

    public void update() {
        if (gameState == playState) {
            player.update();

            for (int i = 0; i < monsters.length; i++) {
                if (monsters[i] == null) {
                    assetSetter.spawnMonster(i);
                }
            }
            
            for (int i = 0; i < monsters.length; i++) {
                if (monsters[i] != null) {
                    if (monsters[i].alive && !monsters[i].dying) {
                        monsters[i].update();
                    } 
                    
                    if (!monsters[i].alive) {
                        monsters[i].checkDrop();
                        monsters[i] = null;
                    }
                }
            }

            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    if (projectileList.get(i).alive) {
                       projectileList.get(i).update();
                    } 
                    
                    if (!projectileList.get(i).alive) {
                        projectileList.remove(i);
                    }
                }
            }
        }

        if (gameState == pauseState) {

        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D)g;
        tileM.draw(g2);

        entityList.add(player);

        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                entityList.add(obj[i]);
            }
        }

        for (int i = 0; i < monsters.length; i++) {
            if (monsters[i] != null) {
                entityList.add(monsters[i]);
            }
        }

        for (int i = 0; i < projectileList.size(); i++) {
            if (projectileList.get(i) != null) {
                entityList.add(projectileList.get(i));
            }
        }

        Collections.sort(entityList, new Comparator<Entity>() {
            @Override
            public int compare(Entity e1, Entity e2) {
                int result = Double.compare(e1.y, e2.y);
                return result;
            }
        });

        for (int i = 0; i < entityList.size(); i++) {
            entityList.get(i).draw(g2);
        }

        entityList.clear();

        ui.draw(g2);
    }
}
