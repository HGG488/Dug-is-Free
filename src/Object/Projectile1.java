package Object;

import entity.Entity;
import main.CombinationChecker;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import Enemy.*;

public class Projectile1 {

    GamePanel gp;
    public Graphics2D g2;

    public enum Owner {PLAYER, ENEMY}


    public Owner owner;
    public int speed;
    public int maxLife;
    public int life;
    public int attack;
    public BufferedImage up1, up2;
    public int worldX, worldY;
    public Entity.Direction direction;
    public boolean alive = false;
    public int width, height;
    public double angle;
    int curentTipX = -10;
    int curentTipY = -10;
    int curentOngoingTipX;
    int curentOngoingTipY;
    int centerX;
    int centerY;

    Point mouseCoord;
    Entity shadow;

    double a;

    public Projectile1(GamePanel gp, Owner owner) {

        shadow = new Entity(gp);
        this.gp = gp;
        speed = 15;
        maxLife = 300;
        life = maxLife;
        attack = 2;
        try {
            if (owner == Owner.PLAYER) {
                up1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesMore/proiectil.png")));
                up2 = Gun.flipImage(up1, false, true);
            } else {
                up1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesMore/proiectil inamic.png")));
                up2 = Gun.flipImage(up1, false, true);
            }
        } catch (IOException ex) {
            System.out.println("eroare la citire imagine Projectile");
        }
        width = up1.getWidth();
        height = up1.getHeight();

    }

    Entity user;

    public void set(int worldX, int worldY, Entity.Direction direction, boolean alive, Entity user, int curentTipX, int curentTipY, Point mouseCoord) {
        alive = true;
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;

        this.curentTipX = curentTipX;
        this.curentTipY = curentTipY;
        this.curentOngoingTipX = curentTipX;
        this.curentOngoingTipY = curentTipY;
        this.speed = 15;
        if (mouseCoord != null)
            angle = Math.atan2(mouseCoord.y - centerY, mouseCoord.x - centerX);
        this.mouseCoord = mouseCoord;


    }

    public void update() {


        if (alive) {
            curentOngoingTipX += speed * Math.cos(angle);
            curentOngoingTipY += speed * Math.sin(angle);
            if (user == gp.player) {
                //Shadow este o entitate invizibila temporara folosita la verificarea coliziunilor
                centerX = gp.player.screenX + GamePanel.tileSize / 2 - 10;
                centerY = gp.player.screenY + GamePanel.tileSize / 2 - 10;
                shadow.worldX = curentOngoingTipX + gp.player.worldX - gp.player.screenX;
                shadow.worldY = curentOngoingTipY + gp.player.worldY - gp.player.screenY;
                shadow.moving = false;

                shadow.solidArea = new Rectangle(this.width / 8, this.height / 8, this.width / 4, this.height / 4);
                shadow.solidAreaDefaultX = shadow.solidArea.x;
                shadow.solidAreaDefaultY = shadow.solidArea.y;
                shadow.collisionOn = false;
                shadow.speed = this.speed;
                shadow.projectile = this;
                int monsterIndex = gp.cChecker.checkEntityProjectile(shadow, gp.monster);
                gp.cChecker.checkTileProjectile(shadow);
                gp.cChecker.checkObjectProjectile(shadow, false);
                if (shadow.collisionOn)
                    alive = false;
                if (monsterIndex != 999 && ((CombinationChecker.SHIELD_IS_NOTHING || CombinationChecker.ENEMY_HAS_NOTHING) || !CombinationChecker.ENEMY_HAS_SHIELD)) {
                    gp.player.damageMonster(monsterIndex);

                }

            } else {
                centerX = user.worldX - gp.player.worldX + gp.player.screenX + GamePanel.tileSize / 2 - 10;
                centerY = user.worldY - gp.player.worldY + gp.player.screenY + GamePanel.tileSize / 2 - 10;
                shadow.worldX = curentOngoingTipX + gp.player.worldX - gp.player.screenX;
                shadow.worldY = curentOngoingTipY + gp.player.worldY - gp.player.screenY;
                shadow.moving = false;

                shadow.solidArea = new Rectangle(this.width / 8, this.height / 8, this.width / 4, this.height / 4);
                shadow.solidAreaDefaultX = shadow.solidArea.x;
                shadow.solidAreaDefaultY = shadow.solidArea.y;
                shadow.collisionOn = false;
                shadow.speed = this.speed;
                shadow.projectile = this;
                int monsterIndex = gp.cChecker.checkEntityProjectile(shadow, gp.monster);
                gp.cChecker.checkTileProjectile(shadow);
                boolean taggedPlayer = gp.cChecker.checkPlayer(shadow);
                if (taggedPlayer && ((CombinationChecker.SHIELD_IS_NOTHING || CombinationChecker.DUG_HAS_NOTHING) || !CombinationChecker.DUG_HAS_SHIELD)) {
                    if (!gp.player.invincible) {
                        gp.player.life -= 1;
                        gp.player.invincible = true;
                    }
                }
                gp.cChecker.checkObjectProjectile(shadow, false);
                if (shadow.collisionOn)
                    alive = false;

                life -= speed;
                if (life <= 0) {
                    alive = false;

                    life = maxLife;
                }

            }

        }


    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        int screenX = curentOngoingTipX;
        int screenY = curentOngoingTipY;

        if (worldX + GamePanel.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - GamePanel.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + GamePanel.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - GamePanel.tileSize < gp.player.worldY + gp.player.screenY) {

            if (mouseCoord != null) {

                g2.drawImage(up1, screenX, screenY, this.width, this.height, null);


            } else
                g2.drawImage(up1, screenX, screenY, this.width, this.height, null);

        }


    }
}

