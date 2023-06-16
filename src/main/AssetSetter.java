package main;

import java.util.Random;

import monster.Zombie;
// import object.OBJ_Star;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        //gp.obj[0] = new OBJ_Star(gp);
        //gp.obj[0].x = 26 * gp.maxTileRow;
        //gp.obj[0].y = 4 * gp.maxTileCol;
    }

    public void setMonster() {
        //gp.monsters[0] = new Zombie(gp);
        //gp.monsters[0].x = 26 * gp.maxTileRow;
        //gp.monsters[0].y = 1 * gp.maxTileCol;
    }

    // spawns monster
    public void spawnMonster(int index) {
        // generates random spawn coordinates within the given bounds
        Random random = new Random();
        int x = random.nextInt(8, 42); 
        int y = random.nextInt(1, 14);

        // instantiates and spawns the monster
        gp.monsters[index] = new Zombie(gp);
        gp.monsters[index].x = x * gp.maxTileRow;
        gp.monsters[index].y = y * gp.maxTileCol;
    }
}
