package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Entity {
    protected boolean shining = false;
    int shineycounter = 0;
    public int WorldX,WorldY;
    public double speed = 1,imageChangeSpeed = 10;
    public  int actionLockCounter = 0;
    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    public BufferedImage attackUp1, attackUp2, attackUp3, attackDown1, attackDown2, attackDown3, attackLeft1, attackLeft2, attackLeft3, attackRight1, attackRight2, attackRight3;
    public int maxLife ;
    public int life;
    public int maxMana;
    public int mana;
    public int ammo;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Entity currentWeapon;
    public Entity currentShield;
    public Projectile projectile;
    public Projectile projectile2;

    public int value;
    public int attackValue;
    public int defenseValue;
    public int useCost;

    public boolean invincible = false;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    public boolean hpBarOn = false;
    public int invincibleCounter = 0;
    public int dyingCounter = 0;
    int hpBarCounter = 0;

    String dialogues[] = new String[20];
    int dialogueIndex = 0;

    protected void getdying() {}

    public enum directions{
        up,
        down,
        left,
        right
    }
    public directions direction = directions.down;

    public BufferedImage image,image2,image3;
    public String name;
    public boolean collision = false;
    public boolean onPath = false;
    public int spriteCounter = 0;
    public int spriteNum =1;

    public Rectangle solidArea = new Rectangle(0,0,40,40);
    public Rectangle attackArea = new Rectangle(0,0,0,0);
    public int solidAreaDefaultX,solidAreaDefaultY;
    public boolean collisionOn = false;
    public int type; //0 = player, 1 = npc, 2 = enemy
    public final int typePlayer = 0;
    public final int typeNpc = 1;
    public final int typeMonster = 2;
    public final int typeSword = 3;
    public final int typeAxe = 4;
    public final int typeShield = 5;
    public final int typeConsumable = 6;
    public String description = "";

    public int shotAvailableCounter = 0;
    GamePanel gp;


    public Entity(GamePanel gp)
    {
        this.gp = gp;
    }
    public void use(Entity entity) {}
    public void checkDrop(){}
    public void dropItem(Entity droppedItem){
        for (int i =0; i<gp.obj[1].length;i++){
            if (gp.obj[gp.currentMap][i] == null)
            {
                gp.obj[gp.currentMap][i] = droppedItem;
                gp.obj[gp.currentMap][i].WorldX = WorldX;
                gp.obj[gp.currentMap][i].WorldY = WorldY;
                break;
            }
        }
    }
    public Color getParticleColor(){
        Color color = null;
        return color;
    }
    public int getParticleSize(){
        return 0;
    }
    public int getParticleSpeed(){
        return 0;
    }
    public int getParticleMaxLife(){
        return 0;
    }
    public void generateParticle(Entity generator, Entity target){

        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxLife();

        Particle p1 = new Particle(gp,target,color,size,speed,maxLife,-2,-1);
        Particle p2 = new Particle(gp,target,color,size,speed,maxLife,2,-1);
        Particle p3 = new Particle(gp,target,color,size,speed,maxLife,-2,1);
        Particle p4 = new Particle(gp,target,color,size,speed,maxLife,2,1);
        gp.particleList.add(p1);
        gp.particleList.add(p2);
        gp.particleList.add(p3);
        gp.particleList.add(p4);
    }
    public void setAction() {}
    public void damageReaction(){}
    public void speak(){
        if (dialogues[dialogueIndex]==null)
        {
            dialogueIndex=0;
        }

        gp.ui.currentDialog = dialogues[dialogueIndex];
        dialogueIndex++;

        switch (gp.player.direction)
        {
            case up :
                direction = directions.down;
                break;
            case down:
                direction = directions.up;
                break;
            case left:
                direction = directions.right;
                break;
            case right:
                direction = directions.left;
                break;
        }
    }
    public void checkCollision(){
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this,false);
        gp.cChecker.checkEntity(this, gp.monster);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this,gp.iTile);

        boolean contactPlayer = gp.cChecker.checkPlayer(this);


        if (this.type==typeMonster && contactPlayer && !gp.player.invincible) {
            damagePlayer(attack);
        }
    }
    public void update()
    {
        setAction();
        checkCollision();
        if (!collisionOn)
        {
            switch (direction)
            {
                case up:
                    WorldY -= speed;
                    break;
                case down:
                    WorldY += speed;
                    break;
                case left:
                    WorldX -= speed;
                    break;
                case right:
                    WorldX += speed;
                    break;

            }
        }

        spriteCounter++;
        if (spriteCounter > imageChangeSpeed) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

        if (invincible)
        {
            invincibleCounter++;
            if (invincibleCounter>40)
            {
                invincible = false;
                invincibleCounter = 0;
            }
        }
        if (shotAvailableCounter < 45)
        {
            shotAvailableCounter++;
        }
    }

    public void damagePlayer(int attack)
    {
        int damage = attack - gp.player.defense;
        if (damage<0)
        {
            damage = 0;
        }
        gp.player.life-=damage;
        gp.player.invincible=true;

    }

    public BufferedImage setup(String imagePath,int width, int height)
    {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image,width,height);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return image;
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int screenX = WorldX-gp.player.WorldX + gp.player.screenX;
        int screenY = WorldY-gp.player.WorldY + gp.player.screenY;

        if (WorldX + gp.tileSize>gp.player.WorldX-gp.player.screenX && WorldX - gp.tileSize< gp.player.WorldX+gp.player.screenX && WorldY+ gp.tileSize >gp.player.WorldY-gp.player.screenY && WorldY - gp.tileSize< gp.player.WorldY+gp.player.screenY)
        {

            switch (direction){
                case up:
                    switch (spriteNum){
                        case 1:
                            image =up1;
                            break;
                        case 2:
                            image =up2;
                            break;
                        case 3:
                            image =up3;
                            break;
                    }
                    break;
                case down:
                    switch (spriteNum){
                        case 1:
                            image =down1;
                            break;
                        case 2:
                            image =down2;
                            break;
                        case 3:
                            image =down3;
                            break;
                    }
                    break;
                case left:
                    switch (spriteNum){
                        case 1:
                            image =left1;
                            break;
                        case 2:
                            image =left2;
                            break;
                        case 3:
                            image =left3;
                            break;
                    }
                    break;
                case right:
                    switch (spriteNum){
                        case 1:
                            image =right1;
                            break;
                        case 2:
                            image =right2;
                            break;
                        case 3:
                            image =right3;
                            break;
                    }
                    break;
            }

            if (type == 2 && hpBarOn) {
                double oneScale= (double)gp.tileSize/maxLife;
                double hpBarValue= oneScale*life;


                g2.setColor(new Color(35,35,35));
                g2.fillRect(screenX-1, screenY - 11, gp.tileSize+2, 12);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 10, (int)hpBarValue, 10);

                hpBarCounter++;

                if (hpBarCounter>200)
                {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if (invincible)
            {
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2,0.4f); //if player invincible his image opacity will be 70%
            }
            if (dying)
            {
                dyingAnimation(g2);
            }
            if (shining)
            {
                shiningAnimation();
            }

            g2.drawImage(image,screenX,screenY,null);

            changeAlpha(g2,1f);
        }
    }

    private void shiningAnimation() {
            shineycounter++;

            gp.gameState = gp.waitState;

            int i = 5;
            if (shineycounter <= i)
            {
                spriteNum= 2;
            }
            if (shineycounter > i && shineycounter<=i*2)
            {
                spriteNum= 1;
            }
            if (shineycounter > i*2 && shineycounter<=i*3)
            {
                spriteNum= 2;
            }
            if (shineycounter > i*3 && shineycounter<=i*4)
            {
                spriteNum= 1;
            }
            if (shineycounter > i*4 && shineycounter<=i*5)
            {
                spriteNum= 2;
            }
            if (shineycounter > i*5 && shineycounter<=i*6)
            {
                spriteNum= 1;
            }
            if (shineycounter > i*6)
            {
                shining = false;
                shineycounter = 0;
                int shin = gp.player.shin;
                if (shin<=99900) {
                    gp.gameState = gp.dialogState;
                    gp.ui.currentDialog = "what are you doing step bro?\nthis is not pokemon. \nyou will not find pikachu!!";
                } else if (shin<=99980) {
                    gp.gameState = gp.CecileState;
                } else if (shin<=99990) {
                    gp.gameState = gp.MansonState;
                }else {
                    gp.gameState = gp.CecileState;
                }
            }
    }

    private void dyingAnimation(Graphics2D g2) {

        dyingCounter++;
        int i = 5;
        if (dyingCounter <= i)
        {
            changeAlpha(g2,0f);
        }
        if (dyingCounter > i && dyingCounter<=i*2)
        {
            changeAlpha(g2,1f);
        }
        if (dyingCounter > i*2 && dyingCounter<=i*3)
        {
            changeAlpha(g2,0f);
        }
        if (dyingCounter > i*3 && dyingCounter<=i*4)
        {
            changeAlpha(g2,1f);
        }
        if (dyingCounter > i*4 && dyingCounter<=i*5)
        {
            changeAlpha(g2,0f);
        }
        if (dyingCounter > i*5 && dyingCounter<=i*6)
        {
            changeAlpha(g2,1f);
        }
        if (dyingCounter > i*6)
        {
            alive = false;
            dyingCounter = 0;
        }
    }

    public void changeAlpha(Graphics2D g2, float alphaValue)
    {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alphaValue));
    }

    public void searchPath(int goalCol, int goalRow){
        int startCol = (WorldX+ solidArea.x)/gp.tileSize;
        int startRow = (WorldY+ solidArea.y)/gp.tileSize;

        gp.pFinder.setNode(startCol,startRow,goalCol,goalRow);

        if (gp.pFinder.search())
        {
            int nextX = gp.pFinder.pathList.get(0).col*gp.tileSize;
            int nextY = gp.pFinder.pathList.get(0).row*gp.tileSize;

            int enLeftX = WorldX + solidArea.x;
            int enRightX = WorldX + solidArea.x+solidArea.width;
            int enTopY = WorldY + solidArea.y;
            int enBottomY = WorldY + solidArea.y+solidArea.height;

            if (enTopY > nextY && enLeftX >= nextX && enRightX<nextX + gp.tileSize){
                direction = directions.up;
            }else if (enTopY < nextY && enLeftX >= nextX && enRightX<nextX + gp.tileSize){
                direction = directions.down;
            } else if (enTopY >= nextY && enBottomY <= nextY+gp.tileSize){
                if (enLeftX > nextX ){
                    direction = directions.left;
                }
                if (enLeftX < nextX ){
                    direction = directions.right;
                }
            } else if (enTopY > nextY && enLeftX > nextX) {
                direction = directions.up;
                checkCollision();
                if (collisionOn){
                    direction = directions.left;
                }
            }else if (enTopY > nextY && enLeftX < nextX) {
                direction = directions.up;
                checkCollision();
                if (collisionOn){
                    direction = directions.right;
                }
            }else if (enTopY < nextY && enLeftX > nextX) {
                direction = directions.down;
                checkCollision();
                if (collisionOn){
                    direction = directions.left;
                }
            }else if (enTopY < nextY && enLeftX < nextX) {
                direction = directions.down;
                checkCollision();
                if (collisionOn){
                    direction = directions.right;
                }
            }
            int nextCol = gp.pFinder.pathList.get(0).col;
            int nextRow = gp.pFinder.pathList.get(0).row;
            if (nextCol == goalCol && nextRow == goalRow){
                onPath =false;
            }
        }else {
            System.out.printf("nai");
        }
    }
}
