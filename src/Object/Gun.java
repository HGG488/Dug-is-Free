package Object;

import main.GamePanel;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Gun extends CombatObject {
    GamePanel gp;

    public Gun(GamePanel gp, Owner owner) {
        super(gp);
        mouseCoord=new Point();
        mouseCoord.x=0;
        mouseCoord.y=0;

        this.gp = gp;
        needsRotation = true;
        this.owner = owner;
        type = Type.GUN;
        if (this.owner == Owner.PLAYER)
            try {
                image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesMore/gun4.png")));
                image1 = flipImage(image, false, true);
            } catch (IOException e) {
                System.out.println("eroare la citire imagine Gun-player");
            }
        else {
            try {
                image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesMore/gunEnemy4.png")));
                image1 = flipImage(image, false, true);
            } catch (IOException e) {
                System.out.println("eroare la citire imagine Gun-enemy");
            }
        }
        this.height = image.getHeight();
        this.width = image.getWidth();
    }

    public void draw(Graphics2D g2, GamePanel gp) {

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        if (worldX + GamePanel.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - GamePanel.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + GamePanel.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - GamePanel.tileSize < gp.player.worldY + gp.player.screenY) {
            if (this.needsRotation && gp.gameState == gp.playState) {
                int mouseX, mouseY;
                if (mouseCoord != null) {
                    mouseX = mouseCoord.x;
                    mouseY = mouseCoord.y;
                    AffineTransform transform = g2.getTransform();


                    g2.rotate(angle, centerX + 10, centerY + 10);

                    if (mouseX < centerX)
                        g2.drawImage(image1, screenX, screenY, this.width, this.height, null);
                    else
                        g2.drawImage(image, screenX, screenY, this.width, this.height, null);

                    g2.setTransform(transform);

                } else
                    g2.drawImage(image, screenX, screenY, this.width, this.height, null);
            } else {
                g2.drawImage(image, screenX, screenY, this.width, this.height, null);
            }
        }
    }

    public void update(GamePanel gp) {
        if(mouseCoord!=null){
        if (owner == Owner.PLAYER) {
            worldX = gp.player.worldX + GamePanel.tileSize;
            worldY = gp.player.worldY;

            mouseCoord = gp.getMousePosition();
            centerX = gp.player.screenX+GamePanel.tileSize/2-10 ;
            centerY = gp.player.screenY+GamePanel.tileSize/2-10;
        } else if (owner == Owner.ENEMY && gp.monster[ownerIndex]!=null) {
            worldX = gp.monster[ownerIndex].worldX + GamePanel.tileSize;
            worldY = gp.monster[ownerIndex].worldY;

            centerX = gp.monster[ownerIndex].worldX - gp.player.worldX + gp.player.screenX + GamePanel.tileSize / 2 - 10;
            centerY = gp.monster[ownerIndex].worldY - gp.player.worldY + gp.player.screenY + GamePanel.tileSize / 2 - 10;
            mouseCoord.x = gp.player.screenX + GamePanel.tileSize/2-10;
            mouseCoord.y = gp.player.screenY + GamePanel.tileSize/2-10;
        }
            int mouseY,mouseX;
            if(mouseCoord==null)
            {   mouseCoord=new Point();
                mouseCoord.x=0;
                mouseCoord.y=0;
            }
            mouseY=mouseCoord.y;
            mouseX=mouseCoord.x;
            //calculeaza unghiul pentru a roti arma dupa pozitia mouse-ului
            angle = Math.atan2(centerY - mouseY, centerX - mouseX) - Math.PI;
            angle1 = Math.abs(angle) + Math.PI / 2;

            curentTipX = (int) (Math.sin(angle1) * (double) (33 + this.width)) + centerX;
            curentTipY = (int) (Math.cos(angle1) * (double) (33 + this.width)) + centerY;


        }
    }
    //functie pentru a intoarce o imagine
    public static BufferedImage flipImage(final BufferedImage image, final boolean horizontal,
                                          final boolean vertical) {
        int x = 0;
        int y = 0;
        int w = image.getWidth();
        int h = image.getHeight();

        final BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2d = out.createGraphics();

        if (horizontal) {
            x = w;
            w *= -1;
        }

        if (vertical) {
            y = h;
            h *= -1;
        }

        g2d.drawImage(image, x, y, w, h, null);
        g2d.dispose();

        return out;
    }
}

