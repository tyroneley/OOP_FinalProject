package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Heart extends Entity {
    public OBJ_Heart(GamePanel gp) {
        super(gp);

        name = "Heart" ;
        type = 1;
        spriteSheet1 = setup("/res/objects/heart.png", 8, 8, 1, 1);
        spritesheet2 = setup("/res/objects/heart_blank.png", 8, 8, 1, 1);
    }
}
