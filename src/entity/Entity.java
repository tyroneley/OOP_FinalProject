package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.Spritesheet;

public class Entity {
    public GamePanel gp;
    public double x, y;
    public double speed;
    
    public Spritesheet spritesheet;
    public Spritesheet meleeSpritesheet;
    public BufferedImage image;
    public Spritesheet spriteSheet1, spritesheet2;
    
    public String name;
    public String face = "right";
    public String direction = "down";
    
    public int currentFrame;
    public boolean collision = false;
    public boolean collisionOn = false;
    public boolean invincible = false;
    public boolean meleeAttacking = false;
    public boolean alive = true;
    public boolean dying = false;
    public boolean onPath = false;

    public int attack;
    public Projectile projectile;

    public int actionLockCounter = 0;
    public int dyingCounter;
    long lastHurtTime = System.currentTimeMillis();
    long lastProjectileFiredTime = System.currentTimeMillis();

    public int type;
    
    public Rectangle solidArea = new Rectangle(32, 32, 32, 32);
    
    public Rectangle attackArea = new Rectangle(8, 16, 32, 32);
    
    public double solidAreaDefaultX, solidAreaDefaultY;

    public int maxLife;
    public int life;
    
    public int counter = 0;
    public int attackCounter = 0;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }
    
    public void setAction() {}

    public void damageReaction() {}

    public void checkCollision() {
        collisionOn = false;
        gp.collision.checkTile(this);
        gp.collision.checkObject(this, false);
        gp.collision.checkEntity(this, gp.monsters);
        boolean contactPlayer = gp.collision.checkPlayer(this);

        if (this.type == 2 && contactPlayer) {
            if (!gp.player.invincible) {
                gp.player.life -= 1;
            }
        }
    }

    public void update() {
        setAction();
        checkCollision();

        if (direction == "up") {
            if (!collisionOn) {
                y -= speed;
            }
        } 
        
        if (direction == "down") {
            if (!collisionOn) {
                y += speed;
            }
        } 
        
        if (direction == "left") {
            if (!collisionOn) {
                x -= speed;
            }
        }
        
        if (direction == "right") {
            if (!collisionOn) {
                x += speed;
            }
        }

        counter++;
        if (counter > 12) {
            spriteSheet1.moveFrame();
            counter = 0;
        }

        if (invincible) {
            long time = System.currentTimeMillis();
            if ((time - lastHurtTime) > 2000) {
                lastHurtTime = time;
                invincible = false;
            }
        }
    }

    public Spritesheet setup(String imagePath, int width, int height, int row, int col) {
        Spritesheet tempSpritesheet = null;

        try {
            tempSpritesheet = new Spritesheet(imagePath, width, height, row, col);

        } catch (Exception e) {
            System.out.println("Setup Sprite Error");
            System.exit(0);
        }

        return tempSpritesheet;
    }

    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;

        if (dyingCounter <= 5) {
            changeAlpha(g2, 0f);
        }

        if (dyingCounter > 5 && dyingCounter <= 10) {
            changeAlpha(g2, 1f);
        }

        if (dyingCounter > 15 && dyingCounter <= 20) {
            changeAlpha(g2, 0f);
        }

        if (dyingCounter > 20 && dyingCounter <= 25) {
            changeAlpha(g2, 1f);
        }
        
        if (dyingCounter > 25 && dyingCounter <= 30) {
            changeAlpha(g2, 0f);
        }

        if (dyingCounter > 30) {
            dying = false;
            alive = false;
        }
    }

    public void checkDrop() {}

    public void searchPath(int goalCol, int goalRow) {
        int startCol = (int)(x + solidArea.x) / gp.tileSize;
        int startRow = (int)(y + solidArea.y) / gp.tileSize;

        gp.pathfinder.setNodes(startCol, startRow, goalCol, goalRow, this);
        if (gp.pathfinder.search()) {
            int nextX = gp.pathfinder.pathList.get(0).col * gp.tileSize;
            int nextY = gp.pathfinder.pathList.get(0).row * gp.tileSize;

            int entityLeftX = (int)x + solidArea.x;
            int entityRightX = (int)x + solidArea.x + solidArea.width;
            int entityTopY = (int)y + solidArea.y;
            int entityBottomY = (int)y + solidArea.y + solidArea.height;

            if (entityTopY > nextY && entityLeftX >= nextX && entityRightX < nextX + gp.tileSize) {
                direction = "up";
            } else if (entityTopY < nextY && entityLeftX >= nextX && entityRightX < nextX + gp.tileSize) {
                direction = "down";
            } else if (entityTopY >= nextY && entityBottomY < nextY + gp.tileSize) {
                if (entityLeftX > nextX) {
                    direction = "left";
                }
                if (entityLeftX < nextX) {
                    direction = "right";
                } 
            } else if (entityTopY > nextY && entityLeftX > nextX) {
                direction = "up";
                checkCollision();
                if (collisionOn) {
                    direction = "left";
                }
            } else if (entityTopY > nextY && entityLeftX < nextX) {
                direction = "up";
                checkCollision();
                if (collisionOn) {
                    direction = "right";
                }
            } else if (entityTopY < nextY && entityLeftX > nextX) {
                direction = "down";
                checkCollision();
                if (collisionOn) {
                    direction = "left";
                }
            } else if (entityTopY < nextY && entityLeftX < nextX) {
                direction = "down";
                checkCollision();
                if (collisionOn) {
                    direction = "right";
                }
            } else {
                direction = "down";
            }

            int nextCol = gp.pathfinder.pathList.get(0).col;
            int nextRow = gp.pathfinder.pathList.get(0).row;
            if (nextCol == goalCol && nextRow == goalRow) {
                onPath = false;
            }
        }
    }

    public void dropItem(Entity droppedItem) {
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == null) {
                gp.obj[i] = droppedItem;
                gp.obj[i].x = x;
                gp.obj[i].y = y;
                break;
            }
        }
    }

    public void changeAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch(face) {
            case "left":
                image = spriteSheet1.getSprite(1);
                break;
            case "right":
                image = spriteSheet1.getSprite(0);
                break;
        }

        if (type == 2) {
            double oneScale = (double)gp.tileSize / maxLife;
            double hpBarValue = oneScale * life;

            g2.setColor(new Color(0, 0, 0));
            g2.fillRect((int)x-1, (int)y, gp.tileSize + 2, 12);
            g2.setColor(new Color(255, 0, 0));
            g2.fillRect((int)x, (int)y, (int)hpBarValue, 10);
        }

        if (dying && type == 2) {
            dyingAnimation(g2);
        }
        
        g2.drawImage(image, (int)x, (int)y, gp.tileSize, gp.tileSize, null);
    }
}
