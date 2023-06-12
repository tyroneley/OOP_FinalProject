package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Star;

public class Zombie extends Entity {
    public Zombie(GamePanel gp) {
        super(gp);

        name = "Zombie";
        type = 2;
        speed = 2.0;
        maxLife = 4;
        life = maxLife;
        solidArea.x = 0;
        solidArea.y = 0;

        getImage();
    }

    public void getImage() {
        spriteSheet1 = setup("/res/monsters/zombie_sheet.png", 32, 32, 4, 2);
    }

    public void setAction() {

        if (onPath) {
            int goalCol = (int)(gp.player.x + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (int)(gp.player.y + gp.player.solidArea.y) / gp.tileSize;

            searchPath(goalCol, goalRow);
        } else {
        actionLockCounter++;

        if (actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100)+1;

            if (i <= 25) {
                direction = "up";
            }
            if (i > 25 && i <= 50) {
                direction = "down";
            }
            if (i > 50 && i <= 75) {
                direction = "left";
            }
            if (i > 75 && i <= 100) {
                direction = "right";
            }

            actionLockCounter = 0;
        }
        }
    }

    public void update() {
        super.update();

        int xDistance = (int)Math.abs(x - gp.player.x);
        int yDistance = (int)Math.abs(y - gp.player.y);
        int tileDistance = (xDistance + yDistance) / gp.tileSize;

        if (!onPath && tileDistance < 32) {
            onPath = true;
        }
    }

    public void damageReaction() {
        actionLockCounter = 0;
        onPath = true;
    }
    
    public void checkDrop() {
        dropItem(new OBJ_Star(gp));
    }
}
