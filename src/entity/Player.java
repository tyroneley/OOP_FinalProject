package entity;

import main.KeyHandler;
import object.OBJ_IceShard;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;
    public int hasStars = 0;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.gp = gp;
        this.keyH = keyH;
        
        solidArea = new Rectangle(0, 0, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        attackArea.width = 48;
        attackArea.height = 48;

        setDefaultValues();
        getPlayerImage();
        getPlayerMeleeImage();
    }

    public void setDefaultValues() {
        x = 26 * gp.maxTileRow;
        y = 8 * gp.maxTileCol;
       
        type = 0;
        maxLife = 3;
        life = maxLife;
        speed = 4.0;
        direction = "down";
        face = "left";
        projectile = new OBJ_IceShard(gp);
    }

    public void getPlayerImage() {
        try {
            spritesheet = setup("/res/player/human_walksheet.png", 32, 32, 4, 2);
        } catch (Exception e) {
            System.out.println("GET PLAYER IMAGE ERROR");
            System.exit(0);
        }
    }

    public void getPlayerMeleeImage() {
        try {
            meleeSpritesheet = setup("/res/player/human_attacksheet.png", 32, 32, 4, 2);
        } catch (Exception e) {
            System.out.println("GET PLAYER MELEE IMAGE ERROR");
            System.exit(0);
        }
    }

    public void update() {
        collisionOn = false;
        gp.collision.checkTile(this);
        int objIndex = gp.collision.checkObject(this, true);
        pickupObject(objIndex);

        if (keyH.mouseClicked) {
            meleeAttacking = true;
            meleeAttack();
        }
        
        if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true) {
            if (keyH.upPressed == true) {
                direction = "up";
                if (!collisionOn) {
                    y -= speed;
                }
            } 
            
            if (keyH.downPressed == true) {
                direction = "down";
                if (!collisionOn) {
                   y += speed;
                }
            } 
            
            if (keyH.leftPressed == true) {
                direction = "left";
                if (!collisionOn) {
                   x -= speed;
                }
            }
            
            if (keyH.rightPressed == true) {
                direction = "right";
                if (!collisionOn) {
                   x += speed;
                }
            }

            int monsterIndex = gp.collision.checkEntity(this, gp.monsters);
            contactMonster(monsterIndex);

            counter++;
            if (counter > 12) {
                spritesheet.moveFrame();
                counter = 0;
            }

            if (invincible) {
                long time = System.currentTimeMillis();
                if ((time - lastHurtTime) > 3000) {
                    invincible = false;
                    lastHurtTime = time;
                }
            }
        }

        gp.eventHandler.checkEvent();

        if (keyH.projectilePressed) {
            long currentFiredTime = System.currentTimeMillis();
            if ((currentFiredTime - lastProjectileFiredTime) > 1500) {
                lastProjectileFiredTime = currentFiredTime;
                //float mouseX = ((float)x + (keyH.mouseX - gp.screenWidth / 2));
                //float mouseY = ((float)y + (keyH.mouseY - gp.screenHeight / 2));
                projectile.set(x-32, y-32, direction, true, this);
                gp.projectileList.add(projectile);
            }
        }

        if (keyH.mouseX > gp.screenWidth / 2) {
            face = "right";
        } else if (keyH.mouseX < gp.screenWidth / 2) {
            face = "left";
        }

        if (life <= 0) {
            gp.gameState = gp.gameOverState;
        }
    }

    public void meleeAttack() {
        attackCounter++;

        if (attackCounter % 2 == 0) {
            meleeSpritesheet.moveFrame();

            if (attackCounter > 2 && attackCounter < 6) {
                double currentWorldX = x;
                double currentWorldY = y;
                double solidAreaWidth = solidArea.width;
                double solidAreaHeight = solidArea.height;

                switch(direction) {
                    case "up": y -= attackArea.height; break;
                    case "down": y += attackArea.height; break;
                    case "left": x -= attackArea.width; break;
                    case "right": x += attackArea.width; break;
                }

                solidArea.width = attackArea.width;
                solidArea.height = attackArea.height;

                int monsterIndex = gp.collision.checkEntity(this, gp.monsters);
                damageMonster(monsterIndex, attack);

                x = currentWorldX;
                y = currentWorldY;
                solidArea.width = (int)solidAreaWidth;
                solidArea.height = (int)solidAreaHeight;
            }
        } 

        if (attackCounter > 6) {
            attackCounter = 0;
            keyH.mouseClicked = false;
            meleeAttacking = false;
        }
    }

    public void damageMonster(int index, int attack) {
        if (index != 999) {
            if (!gp.monsters[index].invincible) {
                gp.monsters[index].life -= 1;
                gp.monsters[index].invincible = true;
                gp.monsters[index].damageReaction();

                if (gp.monsters[index].life <= 0) {
                    gp.monsters[index].dying = true;
                } 
            }
        }
    }

    public void contactMonster(int index) {
        if (index != 999) {
            if (!invincible) {
                invincible = true;
                life -= 1;
            }
        }
    }

    public void pickupObject(int index) {
        if (index != 999) {
            String objectName = gp.obj[index].name;
            
            switch(objectName) {
                case "Star":
                    hasStars++;
                    gp.obj[index] = null;
                    break;
            }
        }
    }

    public void draw(Graphics2D g2) {
        // g2.setColor(Color.white);
        // g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        BufferedImage image = spritesheet.getSprite(0);

        switch(face) {
            case "left":
                if (meleeAttacking) {
                    image = meleeSpritesheet.getSprite(1);
                } else {
                    image = spritesheet.getSprite(1);
                }
                break;
            case "right":
                if (meleeAttacking) {
                    image = meleeSpritesheet.getSprite(0);
                } else {
                    image = spritesheet.getSprite(0);
                }
                break;
        }

        g2.drawImage(image, (int)x, (int)y, gp.tileSize, gp.tileSize, null);
    }
}
