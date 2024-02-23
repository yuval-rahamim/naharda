package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity {
    GamePanel gp ;
    public OBJ_Potion_Red(GamePanel gp) {
        super(gp);

        this.gp =gp;
        type = typeConsumable;
        name = "Red Potion";
        value = 6;
        down1= setup("objects/potion_red",gp.tileSize,gp.tileSize);
        description = "["+name+"]\n"+"heals your life by " + value+"!";
    }
    public void use(Entity entity) {
        gp.gameState = gp.dialogState;
        gp.ui.currentDialog = "you drank the "+name+"!\n" + "your life has been recovered by "+value;
        entity.life+=value;
        if (gp.player.life>gp.player.maxLife)
        {
            gp.player.life = gp.player.maxLife;
        }
    }
}
