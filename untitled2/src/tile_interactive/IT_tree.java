package tile_interactive;

import entity.Entity;
import main.GamePanel;

import java.awt.*;

public class IT_tree extends InteractiveTile{
    GamePanel gp;
    public IT_tree(GamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;

        down1 = setup("tiles_interactive/drytree",gp.tileSize,gp.tileSize);
        destructible = true;
        life = 3;
    }

    public InteractiveTile getDestroyedForm(){
        InteractiveTile tile = new IT_Trunk(gp,WorldX/gp.tileSize,WorldY/gp.tileSize);
        return tile;
    }

    public boolean isCorrectItem(Entity entity){
        boolean isCorrectItem = false;
        if (entity.currentWeapon.type == typeAxe)
        {
            isCorrectItem = true;
        }
        return isCorrectItem;
    }

    public Color getParticleColor(){
        Color color = new Color(33, 20, 3);
        return color;
    }
    public int getParticleSize(){
        return 6;
    }
    public int getParticleSpeed(){
        return 1;
    }
    public int getParticleMaxLife(){
        return 20;
    }
}
