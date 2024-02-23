package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_green_gem extends Entity {
    GamePanel gp;
    public OBJ_green_gem(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = typeConsumable;
        name = "gem";
        down1 = setup("objects/gem",gp.tileSize,gp.tileSize);
    }
    public void use(Entity entity) {
        gp.ui.addMessage("one gem");
        gp.player.gem++;
    }
}
