package entity;

import main.GamePanel;

public class Projectile extends Entity{
    Entity user;
    public Projectile(GamePanel gp) {
        super(gp);


    }

    public void set(int WorldX,int WorldY,directions direction, boolean alive, Entity user) {
        this.WorldX = WorldX;
        this.WorldY = WorldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;

    }
    public void update() {

        if (user == gp.player) {
            int monsterIndex = gp.cChecker.checkEntity(this,gp.monster);
            if (monsterIndex!=-1) {
                gp.player.damageMonster(monsterIndex,attack, knockBackPower);
                generateParticle(user.projectile,gp.monster[gp.currentMap][monsterIndex]);
                alive = false;
            }
        }else{
            boolean contactPlayer = gp.cChecker.checkPlayer(this);
            if (!gp.player.invincible && contactPlayer) {
                damagePlayer(attack);
                generateParticle(user.projectile, gp.player);
                alive = false;
            }
        }

        switch (direction){
            case up :
                WorldY-=speed;
                break;
            case down:
                WorldY+=speed;
                break;
            case left:
                WorldX-=speed;
                break;
            case right:
                WorldX+=speed;
                break;
        }

        life--;
        if (life<=0)
        {
            alive = false;
        }
        spriteCounter++;
        if (spriteCounter>12)
        {
            if (spriteNum == 1)
            {
                spriteNum=2;
            } else if (spriteNum==2) {
                spriteNum=1;
            }
            spriteCounter = 0;
        }
    }
    public boolean haveResource(Entity user){
        return false;
    }
    public void subResource(Entity user){}
}
