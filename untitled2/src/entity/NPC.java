package entity;

import main.GamePanel;

import java.util.Random;

public class NPC extends Entity{
    public NPC(GamePanel gp)
    {
        super(gp);

        direction = directions.down;
        speed = 1;
        imageChangeSpeed = 10;
        getNpcImage();
        setDialog();
    }

    public void getNpcImage()
    {
        up1 = setup("npc/oldman_up_1",gp.tileSize,gp.tileSize);
        up2 = setup("npc/oldman_up_2",gp.tileSize,gp.tileSize);

        down1 = setup("npc/oldman_down_1",gp.tileSize,gp.tileSize);
        down2 = setup("npc/oldman_down_2",gp.tileSize,gp.tileSize);

        left1 = setup("npc/oldman_left_1",gp.tileSize,gp.tileSize);
        left2 = setup("npc/oldman_left_2",gp.tileSize,gp.tileSize);

        right1 = setup("npc/oldman_right_1",gp.tileSize,gp.tileSize);
        right2 = setup("npc/oldman_right_2",gp.tileSize,gp.tileSize);
    }
    public void setDialog(){
        dialogues[0] = "KrisReaper \n sigma has lost his meaning :(";
        dialogues[1] = "you are my fire!";
        dialogues[2] = "the one desireeeeeeeee!";
        dialogues[3] = "believe, when i say(ohhhh)!";
        dialogues[4] = "i want it that wayyyyy!!!!!!!";
        dialogues[5] = "tell me whahy";
        dialogues[6] = " ";
    }
    public void setAction()
    {
        if (onPath){
            int goalCol = 36;
            int goalRow = 21;

            searchPath(goalCol,goalRow);
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
    public void speak() {

        super.speak();
        onPath = true;
    }

}
