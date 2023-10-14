package Tiles;

import main.AssetSetter;
import main.GamePanel;
import javax.imageio.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.io.*;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp)  {
        this.gp = gp;
        tile = new Tile[10];
        this.getTileImage();
        mapTileNum = new int[gp.maxWorldRow][gp.maxWorldCol];
        this.loadMap();
    }

    public void getTileImage()  {
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesMap/simple.png")));
            tile[0].collision = false;
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesMap/perete.png")));
            tile[2].collision = true;
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesMap/peretesimplu.png")));
            tile[1].collision = true;
            tile[3] = new Tile();
            tile[3].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesMap/poartarelief.png")));
            tile[3].collision = true;
            tile[4] = new Tile();
            tile[4].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesMap/poarta.png")));
            tile[4].collision = true;
        }
        catch (IOException e){
            System.out.println("get tile image "+e);
        }
    }
    public void update(){

        if(gp.levelManager.curentLevel==0 && gp.levelManager.levels.get(0).completed)
        {
            mapTileNum[10][8]=0;
            mapTileNum[10][9]=0;
            mapTileNum[10][10]=0;

            mapTileNum[9][22]=0;
            mapTileNum[9][23]=0;
            mapTileNum[9][24]=0;
            mapTileNum[9][25]=0;


        }
        if(gp.levelManager.curentLevel==1)
        {
            mapTileNum[10][8]=3;
            mapTileNum[10][9]=3;
            mapTileNum[10][10]=3;

        }

        if(gp.levelManager.curentLevel==1 && gp.levelManager.levels.get(1).completed)
        {
            mapTileNum[18][39]=0;
            mapTileNum[19][39]=0;
            mapTileNum[20][39]=0;




        }
        if(gp.levelManager.curentLevel== 2 )
        {
            mapTileNum[18][39]=4;
            mapTileNum[19][39]=4;
            mapTileNum[20][39]=4;

        }
        if(gp.levelManager.curentLevel==2 && gp.levelManager.levels.get(2).completed)
        {
            mapTileNum[12][45]=0;
            mapTileNum[13][45]=0;
            mapTileNum[14][45]=0;




        }
        if(gp.levelManager.curentLevel== 3 )
        {
           gp.gameState= gp.endGameState;

        }


    }
    public void draw(Graphics2D g2) {
        int worldCol=0;
        int worldRow=0;
        while(worldCol<gp.maxWorldCol&&worldRow<gp.maxWorldRow)
        {
            int tileNum=mapTileNum[worldRow][worldCol];
            int worldX=worldCol* GamePanel.tileSize;
            int worldY=worldRow* GamePanel.tileSize;
            int screenX=worldX-gp.player.worldX+gp.player.screenX;
            int screenY=worldY-gp.player.worldY+gp.player.screenY;




            if(worldX+ GamePanel.tileSize >gp.player.worldX-gp.player.screenX &&
               worldX- GamePanel.tileSize <gp.player.worldX+gp.player.screenX &&
                worldY+ GamePanel.tileSize >gp.player.worldY-gp.player.screenY &&
               worldY- GamePanel.tileSize <gp.player.worldY+gp.player.screenY)
                if(gp.levelManager.curentLevel+1== AssetSetter.levelMatrix[worldRow][worldCol])
            g2.drawImage(tile[tileNum].image,screenX,screenY, GamePanel.tileSize, GamePanel.tileSize,null);
                else
                    g2.drawImage(tile[1].image,screenX,screenY, GamePanel.tileSize, GamePanel.tileSize,null);
            worldCol++;
            if(worldCol==gp.maxWorldCol){
               worldCol=0;
               worldRow++;
            }

        }

    }

    public void loadMap()  {

        try {
            InputStream is = getClass().getResourceAsStream("/mapaTEXT/mapa.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0;
            int row = 0;
            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();
                while (col < gp.maxWorldCol) {
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[row][col] = num;
                    col++;
                }
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        }
        catch (IOException e) {
            System.out.println("tile manager " + e.getMessage());
        }
    }
}