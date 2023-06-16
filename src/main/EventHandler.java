package main;

import java.awt.Rectangle;

public class EventHandler {
    GamePanel gp;
    Rectangle eventRectangle;
    int eventRectangleDefaultX, eventRectangleDefaultY;
    long lastTime = System.currentTimeMillis();

    // constructor for an event and its hitbox
    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRectangle = new Rectangle();
        eventRectangle.x = 0;
        eventRectangle.y = 0;
        eventRectangle.width = 16;
        eventRectangle.height = 16;
        eventRectangleDefaultX = eventRectangle.x;
        eventRectangleDefaultY = eventRectangle.y;
    }

    // setup events with the designated column + row, along with the function
    public void checkEvent() {
        if (isHit(12, 5, "any")) { damageSpike(); }
        if (isHit(4, 10, "any")) { damageSpike(); }
    }

    // handles tile event collisions
    public Boolean isHit(int eventCol, int eventRow, String reqDirection) {
        boolean hit = false;
        long time = System.currentTimeMillis();

        // gets players current coordinates
        gp.player.solidArea.x = (int)gp.player.x + gp.player.solidArea.x;
        gp.player.solidArea.y = (int)gp.player.y + gp.player.solidArea.y;
        eventRectangle.x = ((eventCol-1) * 32) + eventRectangle.x;
        eventRectangle.y = ((eventRow-1) * 32) + eventRectangle.y;

        // on player touch
        if (gp.player.solidArea.intersects(eventRectangle)) {
            if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                if ((time - lastTime) > 1000) {
                    lastTime = time;
                    hit = true;
                }
            }
        }

        gp.player.solidArea.x = (int)gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = (int)gp.player.solidAreaDefaultY;
        eventRectangle.x = eventRectangleDefaultX;
        eventRectangle.y = eventRectangleDefaultY;

        return hit;
    }

    // handles spike tile damage
    public void damageSpike() {
        gp.player.life -= 1;
    }
}
