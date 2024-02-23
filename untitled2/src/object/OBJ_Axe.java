package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Axe extends Entity {
    public OBJ_Axe(GamePanel gp) {
        super(gp);

        type = typeAxe;
        name = "Woodcutter's Axe";
        down1 = setup("objects/axe",gp.tileSize, gp.tileSize);
        attackArea.width = 30;
        attackArea.height = 30;
        description = "["+name+"]\n"+"can cut trees\n and much more";
        attackValue = 2;
    }
}
