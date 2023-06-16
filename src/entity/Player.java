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

    // instantiates new player
    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.gp = gp;
        this.keyH = keyH;
        
        // hitbox of player
        solidArea = new Rectangle(0, 0, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // attack hitbox of player
        attackArea.width = 48;
        attackArea.height = 48;

        setDefaultValues();
        getPlayerImage();
        getPlayerMeleeImage();
    }

    public void setDefaultValues() {
        // starting position of player
        x = 26 * gp.maxTileRow;
        y = 8 * gp.maxTileCol;
       
        // sets the starting stats/values for player
        type = 0;
        maxLife = 3;
        life = maxLife;
        speed = 4.0;
        direction = "down";
        face = "left";
        projectile = new OBJ_IceShard(gp);
    }

    // get player spritesheet
    public void getPlayerImage() {
        try {
            spritesheet = setup("/res/player/human_walksheet.png", 32, 32, 4, 2);
        } catch (Exception e) {
            System.out.println("GET PLAYER IMAGE ERROR");
            System.exit(0);
        }
    }

    // get melee attack spritesheet
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
        
        // movement inputs
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

            // checks if collided with monster
            int monsterIndex = gp.collision.checkEntity(this, gp.monsters);
            contactMonster(monsterIndex);

            // calls for spritesheet to move frame every 12 ticks
            counter++;
            if (counter > 12) {
                spritesheet.moveFrame();
                counter = 0;
            }

            // resets temporary invincible state after player takes damage
            if (invincible) {
                long time = System.currentTimeMillis();
                if ((time - lastHurtTime) > 3000) {
                    invincible = false;
                    lastHurtTime = time;
                }
            }
        }

        // checks for any tile events
        gp.eventHandler.checkEvent();

        // handles projectile fire input
        if (keyH.projectilePressed) {
            long currentFiredTime = System.currentTimeMillis();
            // firing cooldown
            if ((currentFiredTime - lastProjectileFiredTime) > 1500) {
                lastProjectileFiredTime = currentFiredTime;
                //float mouseX = ((float)x + (keyH.mouseX - gp.screenWidth / 2));
                //float mouseY = ((float)y + (keyH.mouseY - gp.screenHeight / 2));
                projectile.set(x-32, y-32, direction, true, this); // coordinate-32 due to scaling issues
                gp.projectileList.add(projectile);
            }
        }

        // faces player image depending on mouse position
        if (keyH.mouseX > gp.screenWidth / 2) {
            face = "right";
        } else if (keyH.mouseX < gp.screenWidth / 2) {
            face = "left";
        }

        // player death
        if (life <= 0) {
            gp.gameState = gp.gameOverState;
        }
    }

    // melee attack function
    public void meleeAttack() {
        attackCounter++;

        if (attackCounter % 2 == 0) {
            // handles attack animation based on tick
            meleeSpritesheet.moveFrame();

            // on lunge animation
            if (attackCounter > 2 && attackCounter < 6) {
                // gets position of player and width/height of hitbox
                double currentWorldX = x;
                double currentWorldY = y;
                double solidAreaWidth = solidArea.width;
                double solidAreaHeight = solidArea.height;

                // attack knockback
                switch(direction) {
                    case "up": y -= attackArea.height; break;
                    case "down": y += attackArea.height; break;
                    case "left": x -= attackArea.width; break;
                    case "right": x += attackArea.width; break;
                }

                // change hitbox to attack hitbox
                solidArea.width = attackArea.width;
                solidArea.height = attackArea.height;
                
                // check if monster is hit
                int monsterIndex = gp.collision.checkEntity(this, gp.monsters);
                damageMonster(monsterIndex, attack);
                
                // return to original values
                x = currentWorldX;
                y = currentWorldY;
                solidArea.width = (int)solidAreaWidth;
                solidArea.height = (int)solidAreaHeight;
            }
        } 

        // resets attack counter and disables attack
        if (attackCounter > 6) {
            attackCounter = 0;
            keyH.mouseClicked = false;
            meleeAttacking = false;
        }
    }

    // damage monster function
    public void damageMonster(int index, int attack) {
        if (index != 999) {
            if (!gp.monsters[index].invincible) {
                gp.monsters[index].life -= 1;
                gp.monsters[index].invincible = true;
                gp.monsters[index].damageReaction(); // chases player on damage if not chasing already

                // toggles monster death when life runs out
                if (gp.monsters[index].life <= 0) {
                    gp.monsters[index].dying = true;
                } 
            }
        }
    }

    // on monster contact
    public void contactMonster(int index) {
        if (index != 999) {
            if (!invincible) {
                // temporary invincible state
                invincible = true;
                life -= 1;
            }
        }
    }

    // on object contact
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

        // gets image of player character
        BufferedImage image = spritesheet.getSprite(0);

        switch(face) {
            // if facing left direction
            case "left":
                // if attacking animation, else normal walking animation
                if (meleeAttacking) {
                    // row is 1 for left face
                    image = meleeSpritesheet.getSprite(1);
                } else {
                    // row is 1 for left face
                    image = spritesheet.getSprite(1);
                }
                break;
            case "right":
            // if attacking animation, else normal walking animation
                if (meleeAttacking) {
                    // row is 0 for left face
                    image = meleeSpritesheet.getSprite(0);
                } else {
                    // row is 0 for left face
                    image = spritesheet.getSprite(0);
                }
                break;
        }

        // draws player with the given image
        g2.drawImage(image, (int)x, (int)y, gp.tileSize, gp.tileSize, null);
    }
}
