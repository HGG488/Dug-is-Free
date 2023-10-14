package Enemy;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import main.*;

public class EnemyEnemy extends Entity {
    boolean active = false;
    private int animationCounter = 0;


    //INAMICUL INAMICILOR

    public EnemyEnemy(GamePanel gp) {
        super(gp);

        try {
            up1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesEnemy/entity.png")));
        } catch (IOException e) {
            System.out.println("eroare la citire imagine pentru EnemyEnemy");
        }
    }

    public void draw(Graphics2D g2, GamePanel gp) {
        if (active) {
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            if (gp.levelManager.curentLevel == 2)
                g2.drawImage(up1, screenX, screenY, GamePanel.tileSize * 2, GamePanel.tileSize * 2, null);
            if (animationCounter == 70) {
                animationCounter = 0;
            } else {
                if (animationCounter % 3 == 0) {
                    if (gp.levelManager.curentLevel == 2)
                        g2.fillOval(screenX - 30 * animationCounter, screenY - 30 * animationCounter, animationCounter * GamePanel.tileSize, animationCounter * GamePanel.tileSize);
                }
                animationCounter += 1;
            }
        }

    }

    public void update() {
        if (CombinationChecker.ENEMY_HAS_ENEMY && !gp.levelManager.levels.get(gp.levelManager.curentLevel).completed)
            active = true;
        else if (!CombinationChecker.ENEMY_HAS_ENEMY || gp.levelManager.levels.get(gp.levelManager.curentLevel).completed) {
            active = false;
        }
        if (CombinationChecker.ENEMY_HAS_ENEMY && !CombinationChecker.DUG_HAS_ENEMY) {
            for (int i = 0; i < gp.monster.length; i++)
                if (gp.monster[i] != null) {
                    gp.monster[i].life = -1;
                    gp.monster[i].dying = true;
                }
        }


    }

    @Override

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + GamePanel.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - GamePanel.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + GamePanel.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - GamePanel.tileSize < gp.player.worldY + gp.player.screenY) {
            switch (direction) {
                case up:
                    if (spriteNum == 1)
                        if (up1 != null) image = up1;
                    if (spriteNum == 2)
                        if (up2 != null) image = up2;
                        else image = up1;
                    if (spriteNum == 3)
                        if (up3 != null) image = up3;
                        else image = up1;
                    if (spriteNum == 4)
                        if (up4 != null) image = up4;
                        else image = up1;
                    break;
                case down:
                    if (spriteNum == 1)
                        if (down1 != null) image = down1;
                    if (spriteNum == 2)
                        if (down2 != null) image = down2;
                        else image = down1;
                    if (spriteNum == 3)
                        if (down3 != null) image = down3;
                        else image = down1;
                    if (spriteNum == 4)
                        if (down4 != null) image = down4;
                        else image = down1;
                    break;
                case left:
                    if (spriteNum == 1)
                        if (left1 != null) image = left1;
                    if (spriteNum == 2)
                        if (left2 != null) image = left2;
                    if (spriteNum == 3)
                        if (left3 != null) image = left3;
                    if (spriteNum == 4)
                        if (left4 != null) image = left4;
                    break;
                case right:
                    if (spriteNum == 1)
                        if (right1 != null) image = right1;
                    if (spriteNum == 2)
                        if (right2 != null) image = right2;
                    if (spriteNum == 3)
                        if (right3 != null) image = right3;
                    if (spriteNum == 4)
                        if (right4 != null) image = right4;
                    break;
            }
            if (type == ENEMY && hpBarOn) {
                double oneScale = (double) GamePanel.tileSize / maxLife;
                double hpBarValue = oneScale * life;
                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX - 1, screenY - 16, GamePanel.tileSize, 12);
                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);
            }
            hpBarcounter++;
            if (hpBarcounter > 200) {
                hpBarcounter = 0;
                hpBarOn = false;
            }
            if (invincible) {
                hpBarOn = true;
                hpBarcounter = 0;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

            }

            if (gp.levelManager.curentLevel + 1 == AssetSetter.levelMatrix[worldY / GamePanel.tileSize][worldX / GamePanel.tileSize])
                g2.drawImage(image, screenX, screenY, GamePanel.tileSize, GamePanel.tileSize, null);
            else
                g2.drawImage(gp.tileM.tile[1].image, screenX, screenY, GamePanel.tileSize, GamePanel.tileSize, null);


            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        }


    }
}


