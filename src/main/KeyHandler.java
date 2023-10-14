package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class KeyHandler implements KeyListener {
    public boolean upPressed = false, downPressed = false, leftPressed = false, rightPressed = false;
    GamePanel gp;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (gp.gameState == gp.titleState) {
            TitleState(code);
        } else if (gp.gameState == gp.playState)

            PlayState(code);
        else if (gp.gameState == gp.pauseState) {
            try {
                PauseState(code);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else if (gp.gameState == gp.endGameState)
            EndGameState(code);
        else if (gp.gameState == gp.gameOverState) {
            try {
                GameOverState(code);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W)
            upPressed = false;
        if (code == KeyEvent.VK_S)
            downPressed = false;
        if (code == KeyEvent.VK_A)
            leftPressed = false;
        if (code == KeyEvent.VK_D)
            rightPressed = false;
    }

    public void PlayState(int code) {
        if (code == KeyEvent.VK_L)
            for (int i = 0; i < gp.monster.length; i++)
                if (gp.monster[i] != null) {
                    gp.combatObjects.remove(gp.monster[i].gun);
                    gp.combatObjects.remove(gp.monster[i].shield);
                    gp.monster[i].life = -1;
                    gp.monster[i].dying = true;
                    gp.levelManager.killEnemy();
                }
        if (code == KeyEvent.VK_F)
            gp.player.life = gp.player.maxLife;
        if (code == KeyEvent.VK_W)
            upPressed = true;
        if (code == KeyEvent.VK_S)
            downPressed = true;
        if (code == KeyEvent.VK_A)
            leftPressed = true;
        if (code == KeyEvent.VK_D)
            rightPressed = true;

        if (code == KeyEvent.VK_ESCAPE) {
            if (gp.gameState == gp.playState)
                gp.gameState = gp.pauseState;

        }
    }

    public void TitleState(int code) {
        if (code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            if (gp.ui.commandNum < 0)
                gp.ui.commandNum = gp.ui.maxCommandNum;
        }
        if (code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            if (gp.ui.commandNum > gp.ui.maxCommandNum)
                gp.ui.commandNum = 0;
        }
        if (code == KeyEvent.VK_SPACE) {
            if (gp.ui.commandNum == 0) {
                gp.gameState = gp.playState;

                gp.levelManager.curentLevel = 0;
            } else if (gp.ui.commandNum == 2)
                System.exit(0);
            else if (gp.ui.commandNum == 1) {
                gp.dbOperator.loadGame();
                gp.gameState = gp.playState;

            }

        }

    }

    public void GameOverState(int code) throws IOException {
        if (code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            if (gp.ui.commandNum < 0)
                gp.ui.commandNum = 1;
        }
        if (code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            if (gp.ui.commandNum > 1)
                gp.ui.commandNum = 0;
        }
        if (code == KeyEvent.VK_SPACE) {
            if (gp.ui.commandNum == 0) {
                gp.restart();
                gp.gameState = gp.playState;

            } else if (gp.ui.commandNum == 1) {
                gp.levelManager.curentLevel = 0;
                gp.restart();
                gp.gameState = gp.titleState;

            }
        }
    }

    public void PauseState(int code) throws IOException {
        if (code == KeyEvent.VK_ESCAPE)
            if (gp.gameState == gp.pauseState)
                gp.gameState = gp.playState;
        if (code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            if (gp.ui.commandNum < 0)
                gp.ui.commandNum = 1;
        }
        if (code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            if (gp.ui.commandNum > 1)
                gp.ui.commandNum = 0;
        }
        if (code == KeyEvent.VK_SPACE) {
            if (gp.ui.commandNum == 0) {
                gp.dbOperator.saveGame();

            } else if (gp.ui.commandNum == 1) {
                gp.levelManager.curentLevel = 0;
                gp.restart();
                gp.gameState = gp.titleState;

            }
        }
    }

    public void EndGameState(int code) {
        gp.ui.commandNum = 1;
        if (code == KeyEvent.VK_SPACE)
            System.exit(0);
    }
}
