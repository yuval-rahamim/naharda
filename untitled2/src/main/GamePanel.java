package main;

import ai.pathFinder;
import entity.Entity;
import entity.Player;
import tile.TileManeger;
import tile_interactive.InteractiveTile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable
{
    final int originalTileSize = 16;//16X16 TILE
    final int scale = 3;

    public final int tileSize = originalTileSize*scale;//48X48
    public final  int maxScreenCol = 16;
    public final  int maxScreenRow = 12;
    public final int screenWidth = tileSize*maxScreenCol;//768 PIXELS
    public final int screenHeight = tileSize*maxScreenRow;//576 PIXELS

    public final int maxWorldcol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize*maxWorldcol;
    public final int worldHeight = tileSize*maxWorldRow;

    public final  int fps = 60;
    public final int maxMap= 5;

    public TileManeger tileManeger = new TileManeger(this);

    public EventHandler eventHandler = new EventHandler(this);
    public KeyHandler keyHandler = new KeyHandler(this);
    public pathFinder pFinder = new pathFinder(this);
    Thread gameThread;
    public CollidionChecker cChecker = new CollidionChecker(this);
    public AssetSetter assetSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public Player player = new Player(this,keyHandler);
    public Entity obj[][] = new Entity[maxMap][100];
    public Entity npc[][] = new Entity[maxMap][10];
    public Entity monster[][] = new Entity[maxMap][50];
    public InteractiveTile iTile[][] = new InteractiveTile[maxMap][100];
    public int currentMap =0;

    public ArrayList<Entity> particleList = new ArrayList<Entity>();
    ArrayList<Entity> entityList = new ArrayList<Entity>();
    public Entity projectile [][] = new Entity[maxMap][20];
    public BufferedImage pokemon;
    double drawInterval;
    double nextDrawTime;
    double remainTime;

    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogState = 3;
    public final int characterState = 4;
    public final int gameOverState = 5;
    public final int CecileState = 6;
    public final int waitState = 7;
    public final int MansonState = 8;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.DARK_GRAY);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public  void setupGame()
    {
        gameState = titleState;

        assetSetter.setNPC();
        assetSetter.setObject();
        assetSetter.setMonster();
        assetSetter.setInteractiveTiles();
    }
    public void startGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {

        drawInterval = 1000000000/fps;
        nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread!=null)
        {

            //update information
            update();

            //draw the screen
            repaint();


            try {
                remainTime = nextDrawTime-System.nanoTime();
                remainTime = remainTime/1000000;
                if(remainTime<0)
                {
                    remainTime = 0;
                }
                Thread.sleep((long) remainTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void retry(){
        player.setDefaultPosition();
        player.restoreLifeAndMana();
        assetSetter.setMonster();
        assetSetter.setNPC();
    }
    public void restart(){
        retry();
        player.setDefaultValues();
        player.setItems();
        assetSetter.setObject();
        assetSetter.setInteractiveTiles();
    }
    public void update()
    {
        if (gameState == playState)
        {
            player.update();
            for (int i =0; i<npc[1].length;i++)
            {
                if (npc[currentMap][i]!=null)
                {
                    npc[currentMap][i].update();
                }
            }
            for (int i =0; i<monster[1].length;i++)
            {
                if (monster[currentMap][i]!=null)
                {
                    if (monster[currentMap][i].alive && !monster[currentMap][i].dying) {
                        monster[currentMap][i].update();
                    }else if (!monster[currentMap][i].alive){
                        monster[currentMap][i].checkDrop();
                        monster[currentMap][i] = null;
                    }
                }
            }
            for (int i =0; i<projectile[1].length;i++)
            {
                if (projectile[currentMap][i]!=null)
                {
                    if (projectile[currentMap][i].alive) {
                        projectile[currentMap][i].update();
                    }else{
                        projectile[currentMap][i] = null;
                    }
                }
            }
            for (int i =0; i<particleList.size();i++)
            {
                if (particleList.get(i)!=null)
                {
                    if (particleList.get(i).alive) {
                        particleList.get(i).update();
                    }else {
                        particleList.remove(i);
                    }
                }
            }
            for (int i =0; i<iTile[1].length;i++)
            {
                if (iTile[currentMap][i]!=null)
                {
                    iTile[currentMap][i].update();
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // graphics 2d have more functions than graphics

        if (gameState == titleState)
        {
            ui.draw(g2);
        }
        else {
            tileManeger.draw(g2);

            for (int i = 0; i<iTile[1].length; i++)
            {
                if (iTile[currentMap][i]!=null){
                    iTile[currentMap][i].draw(g2);
                }
            }

            entityList.add(player);
            for (int i=0; i<npc[1].length;i++)
            {
                if (npc[currentMap][i]!=null)
                {
                    entityList.add(npc[currentMap][i]);
                }
            }
            for (int i=0; i<obj[1].length;i++)
            {
                if (obj[currentMap][i]!=null)
                {
                    entityList.add(obj[currentMap][i]);
                }
            }
            for (int i=0; i<monster[1].length;i++)
            {
                if (monster[currentMap][i]!=null)
                {
                    entityList.add(monster[currentMap][i]);
                }
            }
            for (int i=0; i<projectile[1].length;i++)
            {
                if (projectile[currentMap][i]!=null)
                {
                    entityList.add(projectile[currentMap][i]);
                }
            }
            for (int i=0; i<particleList.size();i++)
            {
                if (particleList.get(i)!=null)
                {
                    entityList.add(particleList.get(i));
                }
            }

            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity o1, Entity o2) {
                    int result = Integer.compare(o1.WorldY,o2.WorldY);
                    return result;
                }
            });

            for (int i=0; i<entityList.size();i++)
            {
                entityList.get(i).draw(g2);
            }
            entityList.clear();

            ui.draw(g2);
        }
        g2.dispose();
    }

    public BufferedImage setupJpg(String imagePath, int width, int height)
    {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imagePath + ".jpg"));
            image = uTool.scaleImage(image,width,height);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return image;
    }
}
