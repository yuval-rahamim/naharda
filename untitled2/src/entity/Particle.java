package entity;

import main.GamePanel;

import java.awt.*;

public class Particle extends Entity{
    Entity generator;
    Color color;
    int size;
    int xd,yd;
    public Particle(GamePanel gp,Entity generator,Color color, int size, int speed, int maxLife, int xd, int yd) {
        super(gp);

        this.color = color;
        this.generator = generator;
        this.xd = xd;
        this.yd = yd;
        this.size =size;
        this.speed =speed;
        this.maxLife = maxLife;
        life = maxLife;

        int offset = gp.tileSize/2 - size/2;
        WorldX = generator.WorldX + offset;
        WorldY = generator.WorldY + offset;
    }
    public void update(){
        life--;
        if (life<maxLife/3){
            yd++;
        }

        WorldX += xd*speed;
        WorldY += yd*speed;

        if (life == 0){
            alive = false;
        }
    }
    public void draw(Graphics2D g2){
        int screenX = WorldX - gp.player.WorldX + gp.player.screenX;
        int screenY = WorldY - gp.player.WorldY + gp.player.screenY;

        g2.setColor(color);
        g2.fillRect(screenX,screenY,size,size);
    }
}
