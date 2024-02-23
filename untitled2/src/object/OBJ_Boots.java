package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Boots extends Entity
{
    public OBJ_Boots(GamePanel gp)
    {
        super(gp);

        type = typeConsumable;
        name = "boots";
        description = "["+name+"]\n"+"make you faster!!";
        down1= setup("objects/shoe1",gp.tileSize,gp.tileSize);
    }
}
