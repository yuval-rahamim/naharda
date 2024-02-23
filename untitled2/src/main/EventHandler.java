package main;

import entity.Entity;

import java.awt.*;

public class EventHandler
{
    GamePanel gp;
    Rectangle eventRectangle [] = new Rectangle[10];
    int defualtX,defaultY;
    public EventHandler(GamePanel gp)
    {
        this.gp = gp;
        for (int i =0; i<gp.maxMap; i++){
            eventRectangle[i] = new Rectangle();
            eventRectangle[i].x = 23;
            eventRectangle[i].y = 23;
            eventRectangle[i].width = gp.tileSize;
            eventRectangle[i].height = gp.tileSize;
        }

    }
    public void checkEvent(){

        if (hit(0,26,15,Entity.directions.right))
        {
            damagePit(gp.dialogState);
        }
        if (hit(0,23,7,Entity.directions.up))
        {
            healingPool(gp.dialogState);
        }
        if (hit(0,20,15,Entity.directions.left))
        {
            teleport(0,38,10,gp.dialogState);
        }
        if (hit(0,13,41,Entity.directions.down)){
            teleport(1,26,21,gp.dialogState);
        }
    }

    public boolean hit(int map,int eventCol, int eventRow, Entity.directions reqDirection)
    {
        boolean hit = false;

        if (map == gp.currentMap) {
            gp.player.solidArea.x += gp.player.WorldX;
            gp.player.solidArea.y += gp.player.WorldY;
            eventRectangle[map].x += eventCol * gp.tileSize;
            eventRectangle[map].y += eventRow * gp.tileSize;

            if (gp.player.solidArea.intersects(eventRectangle[map])) {
                if (gp.player.direction == reqDirection) {
                    hit = true;
                }
            }

            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
            eventRectangle[map].x = defualtX;
            eventRectangle[map].y = defaultY;
        }
        return  hit;

    }

    private void teleport(int map, int col, int row,int gameState)
    {
        gp.gameState = gameState;
        gp.currentMap = map;
        gp.player.WorldX = gp.tileSize*col;
        gp.player.WorldY = gp.tileSize*row;
        gp.player.direction = Entity.directions.down;
        gp.ui.currentDialog = "omg! where am I ?!?";
    }
    public void damagePit(int gameState)
    {
        gp.gameState = gameState;
        gp.ui.currentDialog = "you fall into a pit!";
        gp.player.life-=1;
    }
    public void healingPool(int gameState)
    {
        if (gp.keyHandler.enterPressed )
        {
            gp.gameState = gameState;
            gp.player.attackCanceled=true;
            gp.ui.currentDialog = " your life has been recovered\n in life it is all about balance\n all the enemys have been recovered";
            gp.player.life = gp.player.maxLife;
            gp.player.mana = gp.player.maxMana;
            gp.assetSetter.setMonster();
        }
    }
}
