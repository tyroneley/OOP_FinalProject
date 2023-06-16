package entity;

import main.GamePanel;

public class Projectile extends Entity {
    Entity user;
    double xVelocity;
    double yVelocity;

    // instantiates a new projectile
    public Projectile(GamePanel gp) {
        super(gp);
        type = 3;
    }

    // sets the starting values of the projectile
    public void set(double x, double y, String direction, boolean alive, Entity user) {
        this.alive = true;
        this.x = x;
        this.y = y;
        this.user = user;
        this.life = maxLife;
        this.direction = direction;
        //float dx = mouseX - (float)x;
        //float dy = mouseY - (float)y;
        //float length = (float)Math.sqrt(dx*dx + dy*dy);
        //dx /= length;
        //dy /= length;
        //float length = (float) Math.sqrt((mouseX - x)*(mouseX - x) + (mouseY - y)*(mouseY - y));
        //this.xVelocity = (float) (mouseX - x) /length * (gp.player.speed);
        //this.yVelocity = (float) (mouseY - y) /length * (gp.player.speed);
    }
    
    public void update() {
        if (this.user == gp.player) {
            int monsterIndex = gp.collision.checkEntity(this, gp.monsters); // if collision with monster
            if (monsterIndex != 999) {
                gp.player.damageMonster(monsterIndex, attack); // damage monster
                alive = false;
            }
        }

        if (this.user != gp.player) {

        }

        switch(direction) {
            case "up":
            // shoots up if entity firing projectile is moving upward
                y -= speed;
                break;
            case "down":
            // shoots up if entity firing projectile is moving downward
                y += speed;
                break;
            case "left":
            // shoots up if entity firing projectile is moving left
                x -= speed;
                break;
            case "right":
            // shoots up if entity firing projectile is moving right
                x += speed;
                break;
        }
        //x += this.xVelocity * this.speed;
        //y += this.yVelocity * this.speed;

        // subtracts life duration of projectile per tick
        life--;
        if (life <= 0) {
            alive = false;
        }
    }
}
