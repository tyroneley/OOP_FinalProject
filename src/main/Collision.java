package main;

import entity.Entity;

public class Collision {
    GamePanel gp;

    public Collision(GamePanel gp) {
        this.gp = gp;
    }

    // checks tile collision
    public void checkTile(Entity entity) {
        // get coordinates of player and player's collision box
       double entityLeftWorldX = entity.x + entity.solidArea.x;
       double entityRightWorldX = entity.x + entity.solidArea.x + entity.solidArea.width;
       double entityTopWorldY = entity.y + entity.solidArea.y;
       double entityBottomWorldY = entity.y + entity.solidArea.y + entity.solidArea.height;

       // get column and row of coordinates
       double entityLeftCol = entityLeftWorldX / 32;
       double entityRightCol = entityRightWorldX / 32;
       double entityTopRow = entityTopWorldY / 32;
       double entityBottomRow = entityBottomWorldY / 32;

       int tileNum1; int tileNum2;

       switch(entity.direction) {
        case "up":
            // if collision happens during upward movement
            entityTopRow = (entityTopWorldY - entity.speed) / 32;
            tileNum1 = gp.tileM.mapTileNum[(int)entityLeftCol][(int)entityTopRow];
            tileNum2 = gp.tileM.mapTileNum[(int)entityRightCol][(int)entityTopRow];
            if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                entity.collisionOn = true; // enables collision
            }
            break;
            case "down":
            // if collision happens during downward movement
            entityBottomRow = (entityBottomWorldY + entity.speed) / 32;
            tileNum1 = gp.tileM.mapTileNum[(int)entityLeftCol][(int)entityBottomRow];
            tileNum2 = gp.tileM.mapTileNum[(int)entityRightCol][(int)entityBottomRow];
            if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                entity.collisionOn = true; // enables collision
            }
            break;
        case "left":
            // if collision happens during left movement
            entityLeftCol = (entityLeftWorldX - entity.speed) / 32;
            tileNum1 = gp.tileM.mapTileNum[(int)entityLeftCol][(int)entityTopRow];
            tileNum2 = gp.tileM.mapTileNum[(int)entityLeftCol][(int)entityBottomRow];
            if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                entity.collisionOn = true; // enables collision
            }
            break;
        case "right":
            // if collision happens during right movement
            entityRightCol = (entityRightWorldX + entity.speed) / 32;
            tileNum1 = gp.tileM.mapTileNum[(int)entityRightCol][(int)entityTopRow];
            tileNum2 = gp.tileM.mapTileNum[(int)entityRightCol][(int)entityBottomRow];
            if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                entity.collisionOn = true; // enables collision
            }
            break;
       }
    }

    // checks player collision with another entity
    public boolean checkPlayer(Entity entity) {
        boolean contactPlayer = false;
        
        // gets coordinates of entity
        entity.solidArea.x = (int)entity.x + (int)entity.solidArea.x;
        entity.solidArea.y = (int)entity.y + (int)entity.solidArea.y;

        // gets coordinates of player
        gp.player.solidArea.x = (int)gp.player.x + (int)gp.player.solidArea.x;
        gp.player.solidArea.y = (int)gp.player.y + (int)gp.player.solidArea.y;

        switch(entity.direction) {
            case "up":
                // stops player if upward collision
                entity.solidArea.y -= entity.speed;
                break;
            case "down":
                // stops player if downward collision
                entity.solidArea.y += entity.speed;
                break;
            case "left":
                // stops player if left collision
                entity.solidArea.x -= entity.speed;
                break;
            case "right":
                // stops player if right collision
                entity.solidArea.x += entity.speed;
                break;
        }

        // toggles collision
        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }

        // return coordinates of entity and player to original values
        entity.solidArea.x = (int)entity.solidAreaDefaultX;
        entity.solidArea.y = (int)entity.solidAreaDefaultY;
        gp.player.solidArea.x = (int)gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = (int)gp.player.solidAreaDefaultY;

        // returns whether contact is made or not
        return contactPlayer;
    }

    // checks entity (not player) collision
    public int checkEntity(Entity entity, Entity[] target) {
        int index = 999; // returns this number if no entity collision is found
        for (int i = 0; i < target.length; i++) {
            if (target[i] != null) {
                // get coordinates of entity
                entity.solidArea.x = (int)entity.x + entity.solidArea.x;
                entity.solidArea.y = (int)entity.y + entity.solidArea.y;
                
                // get coordinates of entity in question
                target[i].solidArea.x = (int)target[i].x + target[i].solidArea.x;
                target[i].solidArea.y = (int)target[i].y + target[i].solidArea.y;

                switch(entity.direction) {
                    case "up":
                        // stops entity if upward collision
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        // stops entity if downward collision
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        // stops entity if left collision
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        // stops entity if right collision
                        entity.solidArea.x += entity.speed;
                        break;
                }

                // toggles collision and sets index of entity from the given list
                if (entity.solidArea.intersects(target[i].solidArea)) {
                    if (target[i] != entity) {
                        entity.collisionOn = true;
                        index = i;
                    }
                }

                // return coordinates of entity and player to original values
                entity.solidArea.x = (int)entity.solidAreaDefaultX;
                entity.solidArea.y = (int)entity.solidAreaDefaultY;
                target[i].solidArea.x = (int)target[i].solidAreaDefaultX;
                target[i].solidArea.y = (int)target[i].solidAreaDefaultY;
            }
        }
        return index;
    }

    // checks object collision (stars)
    public int checkObject(Entity entity, boolean player) {
        int index = 999; // returns this number if no entity collision is found
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {

                // get coordinates of entity that touched the object
                entity.solidArea.x = (int)entity.x + entity.solidArea.x;
                entity.solidArea.y = (int)entity.y + entity.solidArea.y;
                
                // get coordinates of object in question
                gp.obj[i].solidArea.x = (int)gp.obj[i].x + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = (int)gp.obj[i].y + gp.obj[i].solidArea.y;

                switch(entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }

                // toggles collision and sets index of entity from the given list
                if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                    if (gp.obj[i].collision) {
                        entity.collisionOn = true;
                    }
                    if (player) {
                        index = i;
                    }
                }
                    
                // return coordinates of entity and player to original values
                entity.solidArea.x = (int)entity.solidAreaDefaultX;
                entity.solidArea.y = (int)entity.solidAreaDefaultY;
                gp.obj[i].solidArea.x = (int)gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = (int)gp.obj[i].solidAreaDefaultY;
            }
        }
        return index;
    }
}
