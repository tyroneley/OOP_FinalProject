package main;

import entity.Entity;

public class Collision {
    GamePanel gp;

    public Collision(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
       double entityLeftWorldX = entity.x + entity.solidArea.x;
       double entityRightWorldX = entity.x + entity.solidArea.x + entity.solidArea.width;
       double entityTopWorldY = entity.y + entity.solidArea.y;
       double entityBottomWorldY = entity.y + entity.solidArea.y + entity.solidArea.height;

       double entityLeftCol = entityLeftWorldX / 32;
       double entityRightCol = entityRightWorldX / 32;
       double entityTopRow = entityTopWorldY / 32;
       double entityBottomRow = entityBottomWorldY / 32;

       int tileNum1; int tileNum2;

       switch(entity.direction) {
        case "up":
            entityTopRow = (entityTopWorldY - entity.speed) / 32;
            tileNum1 = gp.tileM.mapTileNum[(int)entityLeftCol][(int)entityTopRow];
            tileNum2 = gp.tileM.mapTileNum[(int)entityRightCol][(int)entityTopRow];
            if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                entity.collisionOn = true;
            }
            break;
            case "down":
            entityBottomRow = (entityBottomWorldY + entity.speed) / 32;
            tileNum1 = gp.tileM.mapTileNum[(int)entityLeftCol][(int)entityBottomRow];
            tileNum2 = gp.tileM.mapTileNum[(int)entityRightCol][(int)entityBottomRow];
            if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                entity.collisionOn = true;
            }
            break;
        case "left":
            entityLeftCol = (entityLeftWorldX - entity.speed) / 32;
            tileNum1 = gp.tileM.mapTileNum[(int)entityLeftCol][(int)entityTopRow];
            tileNum2 = gp.tileM.mapTileNum[(int)entityLeftCol][(int)entityBottomRow];
            if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                entity.collisionOn = true;
            }
            break;
        case "right":
            entityRightCol = (entityRightWorldX + entity.speed) / 32;
            tileNum1 = gp.tileM.mapTileNum[(int)entityRightCol][(int)entityTopRow];
            tileNum2 = gp.tileM.mapTileNum[(int)entityRightCol][(int)entityBottomRow];
            if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                entity.collisionOn = true;
            }
            break;
       }
    }

    public boolean checkPlayer(Entity entity) {
        boolean contactPlayer = false;
        entity.solidArea.x = (int)entity.x + (int)entity.solidArea.x;
        entity.solidArea.y = (int)entity.y + (int)entity.solidArea.y;

        gp.player.solidArea.x = (int)gp.player.x + (int)gp.player.solidArea.x;
        gp.player.solidArea.y = (int)gp.player.y + (int)gp.player.solidArea.y;

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

        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }

        entity.solidArea.x = (int)entity.solidAreaDefaultX;
        entity.solidArea.y = (int)entity.solidAreaDefaultY;
        gp.player.solidArea.x = (int)gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = (int)gp.player.solidAreaDefaultY;

        return contactPlayer;
    }

    public int checkEntity(Entity entity, Entity[] target) {
        int index = 999;
        for (int i = 0; i < target.length; i++) {
            if (target[i] != null) {
                entity.solidArea.x = (int)entity.x + entity.solidArea.x;
                entity.solidArea.y = (int)entity.y + entity.solidArea.y;
                
                target[i].solidArea.x = (int)target[i].x + target[i].solidArea.x;
                target[i].solidArea.y = (int)target[i].y + target[i].solidArea.y;

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

                if (entity.solidArea.intersects(target[i].solidArea)) {
                    if (target[i] != entity) {
                        entity.collisionOn = true;
                        index = i;
                    }
                }

                entity.solidArea.x = (int)entity.solidAreaDefaultX;
                entity.solidArea.y = (int)entity.solidAreaDefaultY;
                target[i].solidArea.x = (int)target[i].solidAreaDefaultX;
                target[i].solidArea.y = (int)target[i].solidAreaDefaultY;
            }
        }
        return index;
    }

    public int checkObject(Entity entity, boolean player) {
        int index = 999;
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {
                entity.solidArea.x = (int)entity.x + entity.solidArea.x;
                entity.solidArea.y = (int)entity.y + entity.solidArea.y;
                
                gp.obj[i].solidArea.x = (int)gp.obj[i].x + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = (int)gp.obj[i].y + gp.obj[i].solidArea.y;

                switch(entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                }
                entity.solidArea.x = (int)entity.solidAreaDefaultX;
                entity.solidArea.y = (int)entity.solidAreaDefaultY;
                gp.obj[i].solidArea.x = (int)gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = (int)gp.obj[i].solidAreaDefaultY;
            }
        }
        return index;
    }
}
