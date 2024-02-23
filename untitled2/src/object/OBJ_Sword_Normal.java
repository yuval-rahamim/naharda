package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Normal extends Entity {
    public OBJ_Sword_Normal(GamePanel gp) {

        super(gp);

        type = typeSword;
        name = "Normal Sword";
        down1 = setup("objects/sword_normal",gp.tileSize, gp.tileSize);
        attackArea.width = 36;
        attackArea.height = 36;
        description = "["+name+"]\n"+"An old sword";
        attackValue = 1;
    }
}
