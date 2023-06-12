package object;

import entity.Projectile;
import main.GamePanel;

public class OBJ_IceShard extends Projectile {
    GamePanel gp;

    public OBJ_IceShard(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Ice Shard";
        speed = 6.0;
        maxLife = 100;
        life = maxLife;
        attack = 1;
        alive = false;
        getImage();
    }

    public void getImage() {
        spriteSheet1 = setup("/res/objects/ice_projectile.png", 32, 32, 1, 1);
    }
}
