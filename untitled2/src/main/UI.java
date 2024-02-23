package main;

import entity.Entity;
import object.OBJ_Heart;
import object.OBJ_Key;
import object.OBJ_ManaCrystal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80;
    public boolean messageOn = false;
    public String currentDialog = "";

    ArrayList<String> message = new ArrayList<String>();
    ArrayList<Integer> messageCounter = new ArrayList<Integer>();

    public boolean gameFinished = false;
    public int commandNum = 0;
    public int slotCol;
    public int slotRow;

    BufferedImage mansonImage,cecileImage,backgroundImage,keyImage,heart_full, heart_half,heart_blank, crystal_full, crystal_blank;

    public UI(GamePanel gp)
    {
        this.gp = gp;

        arial_40 = new Font("Arial",Font.PLAIN,40);
        arial_80 = new Font("Arial",Font.BOLD,80);

        OBJ_Key key= new OBJ_Key(gp);
        keyImage = key.down1;

        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;

        Entity crystal = new OBJ_ManaCrystal(gp);
        crystal_full = crystal.image;
        crystal_blank = crystal.image2;

        backgroundImage = gp.setupJpg("maps/b",gp.screenWidth,gp.screenHeight);
        cecileImage = gp.setupJpg("maps/cecile",gp.tileSize*5,gp.tileSize*5);
        mansonImage = gp.setupJpg("maps/shiny_manson",gp.tileSize*5,gp.tileSize*5);
        gp.pokemon = cecileImage;
    }
    public void addMessage(String text)
    {
        message.add(text);
        messageCounter.add(0);

    }
    public void draw(Graphics2D g2)
    {
        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(Color.white);

        if (gp.gameState==gp.titleState)
        {
            drawTitleScreen();
        }
        if (gp.gameState == gp.playState)
        {
            drawPlayerLife();
            drawMessage();
        }
        if (gp.gameState==gp.pauseState)
        {
            drawPauseScreen();
            drawPlayerLife();
        }
        if (gp.gameState==gp.dialogState)
        {
            drawDialogScreen();
            drawPlayerLife();
        }
        if (gp.gameState == gp.characterState)
        {
            drawCharcterScreen();
            drawInventory();
        }
        if (gp.gameState == gp.gameOverState)
        {
            drawGameOverScreen();
        }
        if (gp.gameState==gp.CecileState)
        {
            drawCecileScreen();
        }
        if (gp.gameState==gp.MansonState)
        {
            drawMansonScreen();
        }
    }

    private void drawGameOverScreen() {

        g2.setColor(new Color(0, 0, 0,150));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        g2.setColor(new Color(0, 0, 0, 199));
        g2.fillRect(0,gp.tileSize*4,gp.screenWidth,gp.tileSize*3);

        int x,y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,110f));

        text = "wasted";
        g2.setColor(new Color(0, 0, 0));
        x = getXCenter(text);
        y = gp.tileSize*6 - 10;
        g2.drawString(text,x,y);

        g2.setColor(new Color(225, 2, 2));
        g2.drawString(text,x-4,y-4);

        g2.setColor(new Color(241, 238, 238));
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,40f));
        text = "retry";
        x = getXCenter(text);
        y += gp.tileSize -10;
        g2.drawString(text,x,y);
    }
    public void drawCecileScreen() {

        g2.drawImage(cecileImage,0,0,gp.screenWidth,gp.screenHeight,null);

        int x,y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,50f));

        text = "you found shiny cecile!";
        g2.setColor(new Color(75, 40, 40, 150));
        x = getXCenter(text);
        y = gp.tileSize*2 - 10;
        g2.drawString(text,x,y);

        g2.setColor(new Color(0));
        g2.drawString(text,x-4,y-4);
    }
    public void drawMansonScreen() {

        g2.drawImage(mansonImage,0,0,gp.screenWidth,gp.screenHeight,null);

        int x,y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,50f));

        text = "you found shiny manson!";
        g2.setColor(new Color(75, 40, 40, 150));
        x = getXCenter(text);
        y = gp.tileSize*2 - 10;
        g2.drawString(text,x,y);

        g2.setColor(new Color(0));
        g2.drawString(text,x-4,y-4);
    }

    private void drawInventory() {
        int frameX = gp.tileSize*9,frameY = gp.tileSize;
        int frameWidth = gp.tileSize*6,frameHeight = gp.tileSize*5;
        drawSubWindow(frameX,frameY,frameWidth,frameHeight);

        final int slotXstart = frameX+20;
        final int slotYstart = frameY+20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.tileSize+3;

        for (int i = 0; i<gp.player.inventory.size();i++)
        {
            if (gp.player.inventory.get(i) ==  gp.player.currentWeapon || gp.player.inventory.get(i) ==  gp.player.currentShield )
            {
                g2.setColor(new Color(240,190,90));
                g2.fillRoundRect(slotX,slotY, gp.tileSize, gp.tileSize,10,10);
            }

            g2.drawImage(gp.player.inventory.get(i).down1,slotX,slotY,null);

            slotX+=slotSize;

            if (i==4 || i==9||i==14)
            {
                slotX = slotXstart;
                slotY +=slotSize;
            }
        }

        int cursorX = slotXstart + slotSize *slotCol,cursorY = slotYstart + slotSize *slotRow;
        int cursorWidth= gp.tileSize,cursorHeight = gp.tileSize;

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX,cursorY,cursorWidth,cursorHeight,10,10);

        int dframeX = frameX,dframeY = frameY + frameHeight;
        int dframeWidth = frameWidth,dframeHeight = gp.tileSize*3;


        int textX = dframeX+20,textY = dframeY+gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(25F));

        int itemIndex = getItemIndexOnSlot();
        if (itemIndex<gp.player.inventory.size())
        {
            drawSubWindow(dframeX,dframeY,dframeWidth,dframeHeight);
            drawLines(gp.player.inventory.get(itemIndex).description,textX,textY,32);
        }

    }
    public int getItemIndexOnSlot()
    {
        int itemIndex = slotCol+slotRow*5;
        return itemIndex;
    }

    private void drawMessage() {
        int messageX = gp.tileSize;
        int messageY = gp.tileSize*4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,28F));

        for (int i =0; i<message.size(); i++)
        {
            if (message.get(i)!=null){
                g2.setColor(Color.white);
                g2.drawString(message.get(i),messageX,messageY);

                int counter = messageCounter.get(i)+1;
                messageCounter.set(i,counter);
                messageY+=50;
                if (messageCounter.get(i) > 120)
                {
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }

    private void drawCharcterScreen() {
        final int frameX = gp.tileSize, frameY= gp.tileSize,frameWidth= gp.tileSize*5,frameHeight= gp.tileSize*10;
        drawSubWindow(frameX,frameY,frameWidth,frameHeight);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(25f));

        int textX = frameX+20;
        int textY = frameY+gp.tileSize;
        final int lineHeight = 35;

        g2.drawString("Level",textX,textY);
        textY += lineHeight;
        g2.drawString("Life",textX,textY);
        textY += lineHeight;
        g2.drawString("Mana",textX,textY);
        textY += lineHeight;
        g2.drawString("Strength",textX,textY);
        textY += lineHeight;
        g2.drawString("Dexterity",textX,textY);
        textY += lineHeight;
        g2.drawString("Attack",textX,textY);
        textY += lineHeight;
        g2.drawString("Defence",textX,textY);
        textY += lineHeight;
        g2.drawString("Exp",textX,textY);
        textY += lineHeight;
        g2.drawString("Next Level",textX,textY);
        textY += lineHeight;
        g2.drawString("Coin",textX,textY);
        textY += lineHeight + 10;
        g2.drawString("Weapon",textX,textY);
        textY += lineHeight + 15;
        g2.drawString("Shield",textX,textY);

        int tailX = (frameX+frameWidth)-30;
        textY = frameY + gp.tileSize;
        String value;

        value = String.valueOf(gp.player.level);
        textX = getXRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.life+"/" + gp.player.maxLife);
        textX = getXRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.mana+"/" + gp.player.maxMana);
        textX = getXRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.strength);
        textX = getXRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = getXRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = getXRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp);
        textX = getXRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coin);
        textX = getXRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        g2.drawImage(gp.player.currentWeapon.down1,tailX-gp.tileSize,textY-24,null);
        textY += gp.tileSize;
        g2.drawImage(gp.player.currentShield.down1,tailX-gp.tileSize,textY-24,null);
    }

    private void drawPlayerLife() {
        int x  = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;
        while(i<gp.player.maxLife/2)
        {
            g2.drawImage(heart_blank,x,y,null);
            i++;
            x+=gp.tileSize;
        }

        x  = gp.tileSize/2;
        y  = gp.tileSize/2;
        i = 0;

        while (i<gp.player.life)
        {
            g2.drawImage(heart_half,x,y,null);
            i++;
            if (i<gp.player.life)
            {
                g2.drawImage(heart_full,x,y,null);
            }
            i++;
            x+=gp.tileSize;
        }

        x = gp.tileSize/2;
        y = (int) (gp.tileSize *1.5);
        i = 0;
        while (i<gp.player.maxMana){
            g2.drawImage(crystal_blank,x,y,null);
            i++;
            x += 35;
        }

        x = gp.tileSize/2;
        y = (int) (gp.tileSize *1.5);
        i=0;
        while (i<gp.player.mana) {
            g2.drawImage(crystal_full,x,y,null);
            i++;
            x+=35;
        }

        x = gp.tileSize/2 +10;
        y = (int) (gp.tileSize *3);
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        g2.drawImage(keyImage, x, y-20, gp.tileSize, gp.tileSize, null);
        g2.drawString("x " + gp.player.numOfKeys, x+gp.tileSize+10, y+15);

        x = gp.tileSize*12;
        y = (int) (gp.tileSize*1.5);
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        g2.drawString("ammo: " + gp.player.ammo, x, y);

    }

    private void drawTitleScreen() {
        g2.setColor(new Color(175, 111, 111));
        g2.drawImage(backgroundImage,0,0,gp.screenWidth,gp.screenHeight,null);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD,70F));
        String text = "the poem of Naharda";
        int x = getXCenter(text);
        int y = gp.tileSize*4;

        g2.setColor(Color.DARK_GRAY);
        g2.drawString(text,x+5,y+5);

        g2.setColor(Color.white);
        g2.drawString(text,x,y);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD,40F));

        text = "NEW GAME";
        x = getXCenter(text);
        y+= gp.tileSize*4;

        g2.setColor(Color.DARK_GRAY);
        g2.drawString(text,x+5,y+5);

        g2.setColor(Color.white);
        g2.drawString(text,x,y);
        if (commandNum==0)
        {
            g2.drawString(">",x-gp.tileSize,y);
        }

        text = "LOAD GAME";
        x = getXCenter(text);
        y+= gp.tileSize;

        g2.setColor(Color.DARK_GRAY);
        g2.drawString(text,x+5,y+5);

        g2.setColor(Color.white);
        g2.drawString(text,x,y);
        if (commandNum==1)
        {
            g2.drawString(">",x-gp.tileSize,y);
        }

        text = "QUIT";
        x = getXCenter(text);
        y+= gp.tileSize;

        g2.setColor(Color.DARK_GRAY);
        g2.drawString(text,x+5,y+5);

        g2.setColor(Color.white);
        g2.drawString(text,x,y);
        if (commandNum==2)
        {
            g2.drawString(">",x-gp.tileSize,y);
        }
    }

    private void drawDialogScreen() {
        int x = gp.tileSize*2;
        int y = gp.tileSize*7;
        int width = gp.screenWidth-(gp.tileSize*4);
        int height = gp.tileSize*4;

        drawSubWindow(x,y,width,height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,20F));
        x+=gp.tileSize;
        y+=gp.tileSize;
        drawLines(currentDialog,x,y,40);
        g2.drawString("PRESS ENTER",width-gp.tileSize-15,y+ gp.tileSize*2+15);


    }
    public void drawLines(String text,int x, int y, int space)
    {
        for (String line : text.split("\n"))
        {
            g2.drawString(line,x,y);
            y+=space;
        }
    }
    public void drawSubWindow(int x,int y,int width,int height)
    {
        Color c = new Color(0,0,0,210);
        g2.setColor(c);
        g2.fillRoundRect(x,y,width,height,35,35);

        c= new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5,y+5,width-10,height-10,25,25);

    }

    public void drawPauseScreen()
    {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,80));
        String text = "PAUSED";
        int x = getXCenter(text);
        int y = gp.screenHeight/2 +10;

        g2.drawString(text,x,y);
    }

    public int getXCenter(String text)
    {
        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }

    public int getXRight(String text,int tailX)
    {
        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        int x= tailX-length;
        return x;
    }
}
