package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Star extends Entity {
    public OBJ_Star(GamePanel gp) { 
        super(gp);

        name = "Star" ;
        type = 1;
        spriteSheet1 = setup("/res/objects/star.png", 8, 8, 1, 1);
    }
}
