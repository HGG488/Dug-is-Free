package Object;

import entity.Player;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;

public abstract class CombatObject {
    public Type type;
    public double angle,angle1;
    public int curentTipX;
    public int curentTipY;
    public Point mouseCoord;
    public enum Type {GUN, SHIELD}
    public double a=0.1f;
    public BufferedImage image, image1, image2;
    public int worldX, worldY;
    public int width, height;
    int centerX,centerY;
    public Owner owner;
    public enum Owner {PLAYER, ENEMY}
    public int ownerIndex=-1;
    ;
    public boolean needsRotation = false;

    public CombatObject(GamePanel gp){
        this.mouseCoord=gp.getMousePosition();
    }

    public abstract void draw(Graphics2D g2, GamePanel gp) ;




    public abstract void update(GamePanel gp) ;
}
