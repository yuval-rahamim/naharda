package object;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class OBJ_Coin_Bronze extends Entity {
    GamePanel gp;
    public OBJ_Coin_Bronze(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = typeConsumable;
        name = "Bronze Coin";

        value = new Random().nextInt(3)+1;;
        down1 = setup("objects/coin_bronze",50,40);
    }
    public void use(Entity entity) {
        gp.ui.addMessage("coin "+value);
        gp.player.coin += value;
    }
}
