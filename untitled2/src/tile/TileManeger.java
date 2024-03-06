package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManeger {
    GamePanel gp;
    int col,row;

    public Tile[] tiles; //tile types(water tile, wall tile, ground tile, and more)
    public int mapTileNum[][][];
    boolean drawPath = true;
    int tileNum;
    String mapFilePath;

    public TileManeger(GamePanel gp)
    {
        this.gp = gp;

        tiles = new Tile[10];
        mapTileNum = new int[gp.maxMap][gp.maxWorldcol][gp.maxWorldRow];

        getTileImage();

        loadMap("maps/world02.txt",0);
        loadMap("maps/interior01.txt",1);

    }

    public void getTileImage(){
            setup(1,"grass",false);

            setup(5,"wall",true);

            setup(6,"water",true);

            setup(4,"tree",true);

            setup(3,"sand",false);

            setup(0,"earth",false);

            setup(2,"pavement",false);
    }
    public void setup(int index,String imagePath,boolean collision)
    {
        UtilityTool uTool = new UtilityTool();
        try {
            tiles[index] = new Tile();
            tiles[index].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/"+ imagePath+".png"));
            tiles[index].image = uTool.scaleImage(tiles[index].image,gp.tileSize, gp.tileSize);
            tiles[index].collision = collision;
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public void  loadMap(String string,int map){
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(string);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col =0;
            int row = 0;
            String line;
            int num;
            String numbers [];
            while (col<gp.maxWorldcol&&row<gp.maxWorldRow)
            {
                line = br.readLine();
                while (col<gp.maxWorldcol)
                {
                    numbers = line.split(" ");
                    num = Integer.parseInt(numbers[col]);
                    mapTileNum[map][col][row] = num;
                    col++;
                }
                if (col == gp.maxWorldcol)
                {
                    col = 0;
                    row++;
                }
            }
            br.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2){
//        g2.drawImage(tiles[0].image,0,0,gp.tileSize,gp.tileSize,null);
        col = 0;
        row =0;

        while (col<gp.maxWorldcol&&row<gp.maxWorldRow)
        {
            tileNum = mapTileNum[gp.currentMap][col][row];

            int worldX = col * gp.tileSize;
            int worldY = row * gp.tileSize;
            int screenX = worldX-gp.player.WorldX + gp.player.screenX;
            int screenY = worldY-gp.player.WorldY + gp.player.screenY;

            if (worldX + gp.tileSize>gp.player.WorldX-gp.player.screenX && worldX - gp.tileSize< gp.player.WorldX+gp.player.screenX && worldY+ gp.tileSize >gp.player.WorldY-gp.player.screenY && worldY - gp.tileSize< gp.player.WorldY+gp.player.screenY)
            {
                g2.drawImage(tiles[tileNum].image,screenX,screenY,gp.tileSize,gp.tileSize,null);
            }

            col++;

            if (col == gp.maxWorldcol)
            {
                col = 0;
                row++;
            }
        }

        if (drawPath){
            g2.setColor(new Color(64, 255,0, 40));

            for (int i = 0; i<gp.pFinder.pathList.size(); i++){
                int worldX = gp.pFinder.pathList.get(i).col* gp.tileSize;
                int worldY = gp.pFinder.pathList.get(i).row* gp.tileSize;
                int screenX = worldX-gp.player.WorldX + gp.player.screenX;
                int screenY = worldY-gp.player.WorldY + gp.player.screenY;

                g2.fillRect(screenX,screenY,gp.tileSize,gp.tileSize);
            }
        }
    }
}
