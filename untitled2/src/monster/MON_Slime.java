package monster;

import entity.Entity;
import main.GamePanel;
import object.*;

import java.util.Random;

public class MON_Slime extends Entity {

    GamePanel gp;

    public MON_Slime(GamePanel gp) {
        super(gp);

        this.gp = gp;
        type = typeMonster;
        name = "Slime";
        speed = 1;
        imageChangeSpeed = 14;
        maxLife = 6;
        life = maxLife;
        attack = 3;
        defense = 0;
        exp = 1;
        projectile = new OBJ_Rock(gp);

        solidArea.x = 3;
        solidArea.y = 20;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }
    public void getImage(){
        up1 = setup("enemy/greenslime_down_1",gp.tileSize,gp.tileSize);
        up2 = setup("enemy/greenslime_down_2",gp.tileSize,gp.tileSize);
        down1 = setup("enemy/greenslime_down_1",gp.tileSize,gp.tileSize);
        down2 = setup("enemy/greenslime_down_2",gp.tileSize,gp.tileSize);
        left1 = setup("enemy/greenslime_down_1",gp.tileSize,gp.tileSize);
        left2 = setup("enemy/greenslime_down_2",gp.tileSize,gp.tileSize);
        right1 = setup("enemy/greenslime_down_1",gp.tileSize,gp.tileSize);
        right2 = setup("enemy/greenslime_down_2",gp.tileSize,gp.tileSize);
    }
    public void update(){
        super.update();
        int xDistance = Math.abs(WorldX-gp.player.WorldX);
        int yDistance = Math.abs(WorldY-gp.player.WorldY);
        int tileDistance = (xDistance + yDistance)/gp.tileSize;

        if (!onPath && tileDistance<5){
            int i = new Random().nextInt(100)+1;
            if (i>50){
                onPath = true;
            }
        }
        if (onPath && tileDistance >10){
            onPath = false;
        }
    }
    @Override
    public void setAction() {
        if (onPath){
            int goalCol = (gp.player.WorldX + gp.player.solidArea.x)/gp.tileSize;
            int goalRow = (gp.player.WorldY + gp.player.solidArea.y)/gp.tileSize;

            searchPath(goalCol,goalRow);

            int i = new Random().nextInt(200)+1;
            if (i>190 && !projectile.alive && shotAvailableCounter == 45)
            {
                projectile.set(WorldX,WorldY,direction,true,this);
                gp.projectileList.add(projectile);
                shotAvailableCounter = 0;
            }
        }else {
            actionLockCounter++;

            if (actionLockCounter == 120) {
                Random random = new Random();
                int i = random.nextInt(100) + 1; //pick a num from 1 to 100

                if (i <= 25) {
                    direction = directions.up;
                }
                if (i > 25 && i <= 50) {
                    direction = directions.down;
                }
                if (i > 50 && i <= 75) {
                    direction = directions.left;
                }
                if (i > 75) {
                    direction = directions.right;
                }
                actionLockCounter = 0;
            }
        }

    }
    public void damageReaction(){

        actionLockCounter = 0;
//        direction = gp.player.direction;
        onPath = true;
    }

    public void checkDrop(){
        int i = new Random().nextInt(100)+1;
        if (i<50){
            dropItem(new OBJ_Coin_Bronze(gp));
        } else if (i<60) {
            dropItem(new OBJ_Heart(gp));
        }else if (i<80) {
            dropItem(new OBJ_ManaCrystal(gp));
        } else if (i<90) {
            dropItem(new OBJ_Coin_Gold(gp));
        }
    }
}
