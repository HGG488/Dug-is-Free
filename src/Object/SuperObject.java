package Object;

import entity.Entity;
import main.AssetSetter;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
public class SuperObject {
    public BufferedImage image,image1,image2;
    public String name;
    public boolean collision=false;
    public int worldX,worldY;
    public  Rectangle solidArea=new Rectangle(1,1,64,64);
    public int solidAreaDefaultX=0;
    public int solidAreaDefaultY=0;
    boolean moving = false;
    int pixelCounter=0;
    int speed=9;
    Entity.Direction movingDirection;
    public void draw(Graphics2D g2, GamePanel gp){

        int screenX=worldX-gp.player.worldX+gp.player.screenX;
        int screenY=worldY-gp.player.worldY+gp.player.screenY;

        if(worldX+ GamePanel.tileSize >gp.player.worldX-gp.player.screenX &&
                worldX- GamePanel.tileSize <gp.player.worldX+gp.player.screenX &&
                worldY+ GamePanel.tileSize >gp.player.worldY-gp.player.screenY &&
                worldY- GamePanel.tileSize <gp.player.worldY+gp.player.screenY)
            if(gp.levelManager.curentLevel+1== AssetSetter.levelMatrix[worldY/GamePanel.tileSize][worldX/GamePanel.tileSize])
                g2.drawImage(image,screenX,screenY, GamePanel.tileSize, GamePanel.tileSize,null);
             else
                g2.drawImage(gp.tileM.tile[1].image,screenX,screenY, GamePanel.tileSize, GamePanel.tileSize,null);

        if(moving){
            this.move1(movingDirection);
        }
    }


    // ca si  la player se foloseste un algoritm pentru a muta blocul un Tile intreg ( Grid based movement)
    public void move1(Entity.Direction direction){
        this.movingDirection= direction;
        moving=true;
        int tempspeed = speed;
        if (pixelCounter + speed > GamePanel.tileSize) {
            speed = GamePanel.tileSize - pixelCounter;
            pixelCounter = GamePanel.tileSize;

        }
        else
            pixelCounter+=speed;
        if(pixelCounter==GamePanel.tileSize)
        {moving =false;
            pixelCounter=0;}

        switch (direction){
            case up:this.worldY-=speed;break;
            case down:this.worldY+=speed;break;
            case left:this.worldX-=speed;break;
            case right:this.worldX+=speed;break;
        }

        speed=tempspeed;

    }
}
