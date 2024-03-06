package entity;

import main.GamePanel;
import main.KeyHandler;
import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyHandler;
    public int gem;
    public int shin;
    public  final int screenX;
    public  final int screenY;
    public int numOfKeys = 0;
    public boolean attackCanceled = false;
    public ArrayList<Entity> inventory = new ArrayList<Entity>();
    public final int maxInventorySize = 20;

    public Player(GamePanel gp, KeyHandler keyHandler) {
        super(gp);

        this.gp = gp;
        this.keyHandler = keyHandler;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2- (gp.tileSize/2);

        solidArea = new Rectangle(18,15,14,30);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();

    }
    public  void setDefaultValues()
    {
//        WorldX = gp.tileSize*23;
//        WorldY = gp.tileSize*21;
        WorldX = gp.tileSize*26;
        WorldY = gp.tileSize*21;
        speed = 3;
        imageChangeSpeed = 8;
        direction = directions.down;

        level = 1;
        strength =1;
        dexterity =1;
        exp = 0;
        nextLevelExp = 4;
        coin = 0;
        gem = 0;
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        maxLife = 6; // 3 full hearts
        life = maxLife;
        maxMana = 4;
        mana = maxMana;

        ammo = 20;
        projectile = new OBJ_Fireball(gp);

        projectile2 = new OBJ_Rock(gp);

        attack = getAttack();
        defense = getDefense();
    }

    public void setItems() {
        inventory.clear();
        numOfKeys = 0;
        inventory.add(currentWeapon);
        inventory.add(currentShield);
    }
    private int getAttack() {
        attackArea = currentWeapon.attackArea;
        return attack = strength * currentWeapon.attackValue;
    }
    public int getDefense() {
            return defense = dexterity * currentShield.defenseValue;
    }


    public void getPlayerImage()
    {
        up1 = setup("player/up-0",gp.tileSize,gp.tileSize);
        up2 = setup("player/up-1",gp.tileSize,gp.tileSize);
        up3 = setup("player/up-2",gp.tileSize,gp.tileSize);

        down1 = setup("player/pixil-layer-0",gp.tileSize,gp.tileSize);
        down2 = setup("player/pixil-layer-1",gp.tileSize,gp.tileSize);
        down3 = setup("player/pixil-layer-2",gp.tileSize,gp.tileSize);

        left1 = setup("player/left0",gp.tileSize,gp.tileSize);
        left2 = setup("player/left1",gp.tileSize,gp.tileSize);
        left3 = setup("player/left2",gp.tileSize,gp.tileSize);

        right1 = setup("player/right0",gp.tileSize,gp.tileSize);
        right2 = setup("player/right1",gp.tileSize,gp.tileSize);
        right3 = setup("player/right2",gp.tileSize,gp.tileSize);
    }
    public void getPlayerAttackImage()
    {
        if (currentWeapon.type == typeSword) {
            attackUp1 = setup("player/up_fight1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("player/up_fight2", gp.tileSize, gp.tileSize * 2);

            attackDown1 = setup("player/boy_attack_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("player/boy_attack_down_2", gp.tileSize, gp.tileSize * 2);

            attackLeft1 = setup("player/boy_attack_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("player/boy_attack_left_2", gp.tileSize * 2, gp.tileSize);

            attackRight1 = setup("player/boy_attack_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("player/boy_attack_right_2", gp.tileSize * 2, gp.tileSize);
        }if (currentWeapon.type == typeAxe) {
        attackUp1 = setup("player/boy_axe_up_1", gp.tileSize, gp.tileSize * 2);
        attackUp2 = setup("player/boy_axe_up_2", gp.tileSize, gp.tileSize * 2);

        attackDown1 = setup("player/boy_axe_down_1", gp.tileSize, gp.tileSize * 2);
        attackDown2 = setup("player/boy_axe_down_2", gp.tileSize, gp.tileSize * 2);

        attackLeft1 = setup("player/boy_axe_left_1", gp.tileSize * 2, gp.tileSize);
        attackLeft2 = setup("player/boy_axe_left_2", gp.tileSize * 2, gp.tileSize);

        attackRight1 = setup("player/boy_axe_right_1", gp.tileSize * 2, gp.tileSize);
        attackRight2 = setup("player/boy_axe_right_2", gp.tileSize * 2, gp.tileSize);
        }
    }
    public void update() {

        if (attacking) {
            attacking();
        }else if (keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed || keyHandler.enterPressed ) {
            if (keyHandler.upPressed) {
                direction = directions.up;

            } else if (keyHandler.downPressed) {
                direction = directions.down;

            }
            if (keyHandler.leftPressed) {
                direction = directions.left;

            } else if (keyHandler.rightPressed) {
                direction = directions.right;
            }

            collisionOn = false;
            gp.cChecker.checkTile(this);

            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            gp.cChecker.checkEntity(this,gp.iTile);

            gp.eventHandler.checkEvent();


            if (!collisionOn && !keyHandler.enterPressed) {
                switch (direction) {
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

            if (keyHandler.enterPressed && !attackCanceled)
            {
                attacking = true;
                spriteCounter = 0;
            }
            attackCanceled = false;

            gp.keyHandler.enterPressed = false;

            spriteCounter++;
            if (spriteCounter > imageChangeSpeed) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 3;
                } else if (spriteNum == 3) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }

        if (gp.keyHandler.shotKeyPressed && !projectile.alive && shotAvailableCounter == 45 && projectile.haveResource(this))
        {
            projectile.set(WorldX,WorldY,direction,true,this);
            projectile.subResource(this);

            gp.projectileList.add(projectile);
            shotAvailableCounter =0;

        } else if (gp.keyHandler.shotKeyPressed && !projectile2.alive && shotAvailableCounter == 45 && projectile2.haveResource(this))
        {
            projectile2.set(WorldX,WorldY,direction,true,this);
            projectile2.subResource(this);

            gp.projectileList.add(projectile2);
            shotAvailableCounter =0;

        }

        if (invincible)
        {
            invincibleCounter++;
            if (invincibleCounter>60)
            {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (shotAvailableCounter < 45)
        {
            shotAvailableCounter++;
        }

        if (life <=0){
         gp.gameState = gp.gameOverState;
        }
    }

    public void setDefaultPosition(){
        WorldX = gp.tileSize*23;
        WorldY = gp.tileSize*21;
        direction = directions.down;
    } public void restoreLifeAndMana(){
        life = maxLife;
        mana = maxMana;
        invincible = false;
    }
    public void attacking() {
        spriteCounter++;

        if (spriteCounter<=5)
        {
            spriteNum = 1;
        }if (spriteCounter>5&&spriteCounter<=25)
        {
            spriteNum = 2;

            int currentWorldX = WorldX;
            int currentWorldY = WorldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight= solidArea.height;

            switch (direction) {
                case up:
                    WorldY -= attackArea.height;
                    break;
                case down:
                    WorldY += attackArea.height;
                    break;
                case left:
                    WorldX -= attackArea.width;
                    break;
                case right:
                    WorldX += attackArea.width;
                    break;
            }

            int monsterIndex = gp.cChecker.checkEntity(this,gp.monster);
            damageMonster(monsterIndex,attack);

            int iTileIndex = gp.cChecker.checkEntity(this,gp.iTile);
            damageInteractiveTile(iTileIndex);

            WorldX = currentWorldX;
            WorldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }if (spriteCounter>25)
        {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void damageInteractiveTile(int i) {
        if (i!=-1 && gp.iTile[gp.currentMap][i].destructible && gp.iTile[gp.currentMap][i].isCorrectItem(this) && !gp.iTile[gp.currentMap][i].invincible)
        {
            gp.iTile[gp.currentMap][i].life--;
            gp.iTile[gp.currentMap][i].invincible = true;

            generateParticle(gp.iTile[gp.currentMap][i],gp.iTile[gp.currentMap][i]);

            if (gp.iTile[gp.currentMap][i].life == 0) {
                gp.iTile[gp.currentMap][i] = gp.iTile[gp.currentMap][i].getDestroyedForm();
            }
        }
    }

    public void damageMonster(int i, int attack) {
        if (i!=-1)
        {
            if (!gp.monster[gp.currentMap][i].invincible)
            {
                int damage = attack - gp.monster[gp.currentMap][i].defense;
                if (damage<0)
                {
                    damage = 0;
                }
                gp.monster[gp.currentMap][i].life -= damage;
                gp.ui.addMessage(damage + " damage!");
                gp.monster[gp.currentMap][i].invincible= true;
                gp.monster[gp.currentMap][i].damageReaction();

                if (gp.monster[gp.currentMap][i].life<=0)
                {
                    gp.monster[gp.currentMap][i].dying=true;
                    gp.ui.addMessage( "killed the "+ gp.monster[gp.currentMap][i].name+"!");
                    gp.ui.addMessage( "EXP + "+ gp.monster[gp.currentMap][i].exp+"!");
                    exp += gp.monster[gp.currentMap][i].exp;
                    checkLevelUp();
                }
            }
        }
    }

    public void checkLevelUp() {
        if (exp >= nextLevelExp)
        {
            while (exp >= nextLevelExp) {
                level++;
                nextLevelExp = nextLevelExp * 2;
                maxLife += 2;
                strength++;
                dexterity++;
                attack = getAttack();
                defense = getDefense();
            }
            gp.gameState = gp.dialogState;
            gp.ui.currentDialog = "you are level "+ level + " now"+"\nyour max life is "+maxLife/2+"\nyour strength is "+strength+"\nyour dexterity is "+dexterity;

        }
    }

    public void selectItem(){
        int itemIndex = gp.ui.getItemIndexOnSlot();
        if (itemIndex <inventory.size()){
            Entity selectedItem = inventory.get(itemIndex);
            if (selectedItem.type == typeSword || selectedItem.type == typeAxe)
            {
                currentWeapon =selectedItem;
                attack = getAttack();
                getPlayerAttackImage();
            }
            if (selectedItem.type == typeShield)
            {
                currentShield =selectedItem;
                defense = getDefense();
            }
            if (selectedItem.type == typeConsumable)
            {
                selectedItem.use(this);
                inventory.remove(itemIndex);
            }
        }
    }


    private void contactMonster(int i) {
        if (i!=-1 && !invincible && !gp.monster[gp.currentMap][i].dying && !gp.monster[gp.currentMap][i].invincible)
        {
            int damage = gp.monster[gp.currentMap][i].attack-defense;
            if (damage<0)
            {
                damage = 0;
            }
            life-=damage;
            invincible=true;
        }
    }

    private void interactNPC(int i) {
        if (gp.keyHandler.enterPressed ) {
            if (i != -1) {
                attackCanceled = true;
                gp.gameState = gp.dialogState;
                gp.npc[gp.currentMap][i].speak();
            }

        }
    }


    public void pickUpObject(int i){


        if (i!=-1)
        {
            String objectName = gp.obj[gp.currentMap][i].name;
            if (inventory.size() != maxInventorySize)
            {
                switch (gp.obj[gp.currentMap][i].type)
                {
                    case 6: //Type consumable
                        switch(objectName) {
                            case "Key":
                                numOfKeys++;
                                gp.ui.addMessage("you got a "+gp.obj[gp.currentMap][i].name+"!");
                                gp.obj[gp.currentMap][i] = null;
                                break;

                            case "boots":
                                gp.ui.addMessage("you got "+gp.obj[gp.currentMap][i].name+"!");
                                speed =4;
                                imageChangeSpeed -= 2;
                                gp.obj[gp.currentMap][i] = null;
                                break;
                            case "Red Potion":
                                inventory.add(gp.obj[gp.currentMap][i]);
                                gp.ui.addMessage("you got "+gp.obj[gp.currentMap][i].name+"!");
                                gp.obj[gp.currentMap][i] = null;
                                break;
                            default:
                                gp.obj[gp.currentMap][i].use(this);
                                gp.obj[gp.currentMap][i] = null;
                                break;
                        }
                        break;
                    case 5: //type Shield
                        inventory.add(gp.obj[gp.currentMap][i]);
                        gp.ui.addMessage("you got a "+gp.obj[gp.currentMap][i].name+"!");
                        gp.obj[gp.currentMap][i] = null;
                        break;
                    case 4: //type Axe
                        inventory.add(gp.obj[gp.currentMap][i]);
                        gp.ui.addMessage("you got an "+gp.obj[gp.currentMap][i].name+"!");
                        gp.obj[gp.currentMap][i] = null;
                        break;
                    case 3: //type sword
                        inventory.add(gp.obj[gp.currentMap][i]);
                        gp.ui.addMessage("you got an "+gp.obj[gp.currentMap][i].name+"!");
                        gp.obj[gp.currentMap][i] = null;
                        break;
                }

            }else{
                gp.ui.addMessage("your inventory is full");
            }

            switch(objectName){

                case "Door":
                    if (numOfKeys>0)
                    {
                        numOfKeys--;
                        gp.obj[gp.currentMap][i] = null;
                        gp.ui.addMessage("you opened the door!");
                    }else{
                        gp.gameState = gp.dialogState;
                        gp.ui.currentDialog = "you need a key to open this door!";
                    }
                    break;
                case "Chest":
                    gp.ui.gameFinished = true;
                    break;
                case "grass":
                    if (!gp.obj[gp.currentMap][i].shining && (keyHandler.rightPressed||keyHandler.leftPressed|| keyHandler.downPressed|| keyHandler.upPressed))
                    {
                        shin = new Random().nextInt(100000)+1;
                        if (shin >= 99000)
                            gp.obj[gp.currentMap][i].shining = true;
                    }
            }
        }

    }
    public void draw(Graphics2D g2)
    {
        BufferedImage image = null;
        int temScreenX = screenX;
        int temScreenY = screenY;

        switch (direction){
            case up:
                if (attacking)
                {temScreenY = screenY-gp.tileSize;
                    switch (spriteNum) {
                    case 1:
                        image = attackUp1;
                        break;
                    case 2:
                        image = attackUp2;
                        break;}}else {
                    switch (spriteNum) {
                        case 1:
                            image = up1;
                            break;
                        case 2:
                            image = up2;
                            break;
                        case 3:
                            image = up3;
                            break;
                    }
                }
                break;
            case down:
                if (attacking)
                {switch (spriteNum) {
                    case 1:
                        image = attackDown1;
                        break;
                    case 2:
                        image = attackDown2;
                        break;}}else {
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
                }}
                break;
            case left:
                if (attacking)
                {temScreenX = screenX-gp.tileSize;
                    switch (spriteNum) {
                    case 1:
                        image = attackLeft1;
                        break;
                    case 2:
                        image = attackLeft2;
                        break;}}else {
                    switch (spriteNum) {
                        case 1:
                            image = left1;
                            break;
                        case 2:
                            image = left2;
                            break;
                        case 3:
                            image = left3;
                            break;
                    }
                }
                break;
            case right:
                if (attacking)
                {switch (spriteNum) {
                    case 1:
                        image = attackRight1;
                        break;
                    case 2:
                        image = attackRight2;
                        break;}}else {
                    switch (spriteNum) {
                        case 1:
                            image = right1;
                            break;
                        case 2:
                            image = right2;
                            break;
                        case 3:
                            image = right3;
                            break;
                    }
                }
                break;
        }

        if (invincible)
        {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.4f)); //if player invincible his image opacity will be 70%
        }

        g2.drawImage(image, temScreenX, temScreenY, null);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
    }
}