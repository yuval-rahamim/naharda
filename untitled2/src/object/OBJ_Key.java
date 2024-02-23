package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity
{
    public OBJ_Key(GamePanel gp)
    {
        super(gp);

        type = typeConsumable;
        name = "Key";
        down1= setup("objects/key1",gp.tileSize,gp.tileSize);
        description = "["+name+"]\n"+"it opens a door";
    }
}
