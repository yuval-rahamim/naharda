package object;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class OBJ_Coin_Gold extends Entity {
    GamePanel gp;
    public OBJ_Coin_Gold(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = typeConsumable;
        name = "Gold Coin";

        value = new Random().nextInt(19,40)+1;;
        down1 = setup("objects/gold_coin",26,40);
    }
    public void use(Entity entity) {
        gp.ui.addMessage("coin "+value);
        gp.player.coin += value;
    }
}
