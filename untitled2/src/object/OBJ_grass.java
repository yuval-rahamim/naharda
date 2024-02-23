package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_grass extends Entity {

    GamePanel gp;
    public OBJ_grass(GamePanel gp)
    {
        super(gp);
        this.gp = gp;

        name = "grass";
        down1 = setup("objects/grass",gp.tileSize,gp.tileSize);
        down2 = setup("objects/Grass_Shiny",gp.tileSize,gp.tileSize);
    }


}
