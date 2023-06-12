package entity;

import main.GamePanel;

public class Projectile extends Entity {
    Entity user;
    double xVelocity;
    double yVelocity;

    public Projectile(GamePanel gp) {
        super(gp);
        type = 3;
    }

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
            int monsterIndex = gp.collision.checkEntity(this, gp.monsters);
            if (monsterIndex != 999) {
                gp.player.damageMonster(monsterIndex, attack);
                alive = false;
            }
        }

        if (this.user != gp.player) {

        }

        switch(direction) {
            case "up":
                y -= speed;
                break;
            case "down":
                y += speed;
                break;
            case "left":
                x -= speed;
                break;
            case "right":
                x += speed;
                break;
        }
        //x += this.xVelocity * this.speed;
        //y += this.yVelocity * this.speed;

        life--;
        if (life <= 0) {
            alive = false;
        }
    }
}
