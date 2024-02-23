package main;

import entity.Entity;

public class CollidionChecker {
    GamePanel gp;

    public CollidionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        int EntityLeftX = entity.WorldX + entity.solidArea.x;
        int EntityRightX = entity.WorldX + entity.solidArea.x + entity.solidArea.width;
        int EntityTopY = entity.WorldY + entity.solidArea.y;
        int EntityBottomY = entity.WorldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = EntityLeftX / gp.tileSize;
        int entityRightCol = EntityRightX / gp.tileSize;
        int entityTopRow = EntityTopY / gp.tileSize;
        int entityBottomRow = EntityBottomY / gp.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case up:
                entityTopRow = (int) (EntityTopY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManeger.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileManeger.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                if (gp.tileManeger.tiles[tileNum1].collision == true || gp.tileManeger.tiles[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case down:
                entityBottomRow = (int) (EntityBottomY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManeger.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileManeger.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                if (gp.tileManeger.tiles[tileNum1].collision == true || gp.tileManeger.tiles[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case left:
                entityLeftCol = (int) (EntityLeftX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManeger.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileManeger.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                if (gp.tileManeger.tiles[tileNum1].collision == true || gp.tileManeger.tiles[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case right:
                entityRightCol = (int) (EntityRightX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManeger.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                tileNum2 = gp.tileManeger.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                if (gp.tileManeger.tiles[tileNum1].collision == true || gp.tileManeger.tiles[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;

        }

    }

    public int checkObject(Entity entity, boolean player) {
        int index = -1;
        for (int i = 0; i < gp.obj[1].length; i++) {
            if (gp.obj[gp.currentMap][i] != null) {
                entity.solidArea.x += entity.WorldX;
                entity.solidArea.y += entity.WorldY;

                gp.obj[gp.currentMap][i].solidArea.x += gp.obj[gp.currentMap][i].WorldX;
                gp.obj[gp.currentMap][i].solidArea.y += gp.obj[gp.currentMap][i].WorldY;

                switch (entity.direction) {
                    case up:
                        entity.solidArea.y -= entity.speed;
                        break;
                    case down:
                        entity.solidArea.y += entity.speed;
                        break;
                    case left:
                        entity.solidArea.x -= entity.speed;
                        break;
                    case right:
                        entity.solidArea.x += entity.speed;
                        break;
                }
                if (entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {
                    if (gp.obj[gp.currentMap][i].collision == true) {
                        entity.collisionOn = true;
                    }
                    if (player == true) {
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].solidAreaDefaultX;
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].solidAreaDefaultY;
            }
        }
        return index;
    }

    public int checkEntity(Entity entity, Entity[][] target)//npc or monster
    {
        int index = -1;
        for (int i = 0; i < target[1].length; i++) {
            if (target[gp.currentMap][i] != null) {
                entity.solidArea.x += entity.WorldX;
                entity.solidArea.y += entity.WorldY;

                target[gp.currentMap][i].solidArea.x += target[gp.currentMap][i].WorldX;
                target[gp.currentMap][i].solidArea.y += target[gp.currentMap][i].WorldY;

                switch (entity.direction) {
                    case up:
                        entity.solidArea.y -= entity.speed;
                        break;
                    case down:
                        entity.solidArea.y += entity.speed;
                        break;
                    case left:
                        entity.solidArea.x -= entity.speed;
                        break;
                    case right:
                        entity.solidArea.x += entity.speed;
                        break;
                }
                if (entity.solidArea.intersects(target[gp.currentMap][i].solidArea) && target[gp.currentMap][i] != entity) {
                    entity.collisionOn = true;
                    index = i;
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidAreaDefaultX;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidAreaDefaultY;
            }
        }
        return index;
    }

    public boolean checkPlayer(Entity entity) {
        boolean contactPlayer = false;

        entity.solidArea.x += entity.WorldX;
        entity.solidArea.y += entity.WorldY;

        gp.player.solidArea.x += gp.player.WorldX;
        gp.player.solidArea.y += gp.player.WorldY;

        switch (entity.direction) {
            case up:
                entity.solidArea.y -= entity.speed;
                break;
            case down:
                entity.solidArea.y += entity.speed;
                break;
            case left:
                entity.solidArea.x -= entity.speed;
                break;
            case right:
                entity.solidArea.x += entity.speed;
                break;
        }
        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer=true;
        }

        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        return contactPlayer;
    }

}
