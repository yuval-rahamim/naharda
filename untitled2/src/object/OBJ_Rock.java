package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

import java.awt.*;

public class OBJ_Rock extends Projectile {
    GamePanel gp;
    public OBJ_Rock(GamePanel gp) {
        super(gp);

        this.gp = gp;
        name = "Rock";
        speed = 5;
        maxLife = 80;
        life = maxLife;
        attack = 2;
        useCost = 1;
        alive = false;
        getImage();
    }
    private void getImage() {
        up1 = setup("projectile/rock_down_1",gp.tileSize,gp.tileSize);
        up2 = setup("projectile/rock_down_1",gp.tileSize,gp.tileSize);
        down1 = setup("projectile/rock_down_1",gp.tileSize,gp.tileSize);
        down2 = setup("projectile/rock_down_1",gp.tileSize,gp.tileSize);
        left1 = setup("projectile/rock_down_1",gp.tileSize,gp.tileSize);
        left2 = setup("projectile/rock_down_1",gp.tileSize,gp.tileSize);
        right1 = setup("projectile/rock_down_1",gp.tileSize,gp.tileSize);
        right2 = setup("projectile/rock_down_1",gp.tileSize,gp.tileSize);
    }
    public boolean haveResource(Entity user) {
        boolean haveResource = false;
        if (user.ammo >=useCost){
            haveResource = true;
        }
        return haveResource;
    }

    public Color getParticleColor(){
        Color color = new Color(40, 50, 0);
        return color;
    }
    public int getParticleSize(){
        return 5;
    }
    public int getParticleSpeed(){
        return 1;
    }
    public int getParticleMaxLife(){
        return 20;
    }

    public void subResource(Entity user){
        user.ammo -= useCost;
    }
}
