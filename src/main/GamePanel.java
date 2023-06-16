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
    final int originalTileSize = 32; // the default tile/character size is 32x32
    final int scale = 3; // scaled up 3x for a larger display on screen
    
    public final int tileSize = originalTileSize * scale; // supposed to be the scaled tile size
    public final int maxScreenCol = 15; // columns on screen
    public final int maxScreenRow = 8; // rows on screen
    public final int maxTileCol = 45; // actual number of columns
    public final int maxTileRow = 24; // actual number of rows
    public final int screenWidth = tileSize * maxScreenCol; 
    public final int screenHeight = tileSize * maxScreenRow;

    int FPS = 60;

    Thread gameThread;
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public Pathfinder pathfinder = new Pathfinder(this);
    public Collision collision = new Collision(this);
    public AssetSetter assetSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eventHandler = new EventHandler(this);

    public Player player = new Player(this, keyH); // instantiates new player object with keyhandler to handle inputs
    public Entity obj[] = new Entity[100]; // list of entities/objects, max of 100 at one time
    public final int maxMonsters = 6; // only 6 monsters can exist/be spawned, no more and no less
    public Entity monsters[] = new Entity[maxMonsters]; // list to store monsters
    public ArrayList<Entity> projectileList = new ArrayList<>(); // list to store projectiles
    Config config = new Config(this);
    ArrayList<Entity> entityList = new ArrayList<>();
    // all enemies, objects and projectiles are entities
    // their respective classes sub classes of entities and inherit its attributes, although
    // not all of them are used

    public int gameState; // used to set the current game state and alternate between playing, pausing, and the game over screen
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
        config.loadConfig(); // loads saved star data
        assetSetter.setObject(); // if any objects are set to spawn on game launch, set them
        assetSetter.setMonster(); // if any monsters are set to spawn on game launch, set them
        gameState = playState; // set the game state to playing on game launch
    }

    // game loop
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

        // determines the speed of which the game is drawn
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
        if (gameState == playState) { // updates players, monsters, projectiles and objects during play state
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

        // one of the reasons why all players, enemies, projeciles and objects are entities is for performance
        // the order of which objects should render first is done through sorting by their position/coordinates

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
