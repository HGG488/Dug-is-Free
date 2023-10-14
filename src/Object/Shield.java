package Object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Shield extends CombatObject {
    GamePanel gp;

    public Shield(GamePanel gp, Owner owner)  {
        super(gp);
        this.gp = gp;
        this.owner=owner;
        this.needsRotation = false;
        this.type = Type.SHIELD;

       try{ image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesMore/shield.png")));}
       catch (IOException e){
           System.out.println("eroare la citire imagine Shield");
       }

        this.height = image.getHeight();
        this.width = image.getWidth();
    }
    public void draw( Graphics2D g2,GamePanel gp){

        int screenX=worldX-gp.player.worldX+gp.player.screenX;
        int screenY=worldY-gp.player.worldY+gp.player.screenY;

        if(worldX+ GamePanel.tileSize >gp.player.worldX-gp.player.screenX &&
                worldX- GamePanel.tileSize <gp.player.worldX+gp.player.screenX &&
                worldY+ GamePanel.tileSize >gp.player.worldY-gp.player.screenY &&
                worldY- GamePanel.tileSize <gp.player.worldY+gp.player.screenY)
            g2.drawImage(image,screenX,screenY, this.width,this.height,null);
    }
    public void update(GamePanel gp){

        if(owner==Owner.PLAYER){
            worldX=gp.player.worldX-15;
            worldY=gp.player.worldY-15;
        }
        else if(owner== Owner.ENEMY &&gp.monster[ownerIndex]!=null) {
            worldX = gp.monster[ownerIndex].worldX-15 ;
            worldY = gp.monster[ownerIndex].worldY-15;
        }

    }


}