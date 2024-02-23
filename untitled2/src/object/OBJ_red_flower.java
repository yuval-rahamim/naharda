package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_red_flower extends Entity {
    public OBJ_red_flower(GamePanel gp) {
        super(gp);
        name = "flower";
        down1 = setup("objects/red_flower",gp.tileSize,gp.tileSize);
    }
}
