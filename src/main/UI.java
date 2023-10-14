package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import Object.*;

public class UI {
    GamePanel gp;
    Font arial_40;
    Graphics2D g2;
    BufferedImage heart_full,heart_blank;
    public int commandNum=0;
    public int maxCommandNum=2;
    public UI(GamePanel gp){
        this.gp=gp;
        arial_40=new Font("Arial",Font.PLAIN,40);
        SuperObject heart=new Heart(gp);
        heart_full=heart.image;
        heart_blank=heart.image1;
    }
    public void draw(Graphics2D g2){
        this.g2=g2;

        g2.setColor(Color.white);
        if(gp.gameState==gp.titleState)
        {
            drawTitleScreen();
        }
        if(gp.gameState==gp.playState){
            drawPlayScreen();
            drawPlayerLife();
        }
        if(gp.gameState==gp.endGameState)
            drawEndGameState();
        if(gp.gameState==gp.pauseState){
            drawPauseScreen();

        }
        if(gp.gameState==gp.gameOverState)
        {
            drawGameOverScreen();
        }
    }
    public void drawPauseScreen(){

        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,80F));
        g2.setColor(Color.white);
        String text= "PAUSED";
        int x ;
        x=getXforCenteredText(text);
        int y =gp.screenHeight/2;
        g2.drawString(text,x,y);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD,50f));
        text="Save";

        x=getXforCenteredText(text);
        y+=GamePanel.tileSize;
        g2.drawString(text,x,y);
        if(commandNum == 0){
            g2.drawString(">",x-40,y);
        }
        g2.setFont(g2.getFont().deriveFont(50f));
        text="Quit";
        x=getXforCenteredText(text);
        y+=GamePanel.tileSize;
        g2.drawString(text,x,y);
        if(commandNum==1){
            g2.drawString(">",x-40,y);
        }

    }
    public void drawPlayScreen(){
        g2.setFont(arial_40);
        String text="Score : "+gp.score;
        int x=GamePanel.tileSize/2;
        int y=GamePanel.tileSize*2;
        g2.setColor(Color.white);
        g2.drawString(text,x+5,y+5);


    }
    public int getXforCenteredText(String text){
        int x ;
        int length=(int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        x=gp.screenWidth/2-length/2;
        return x;
    }
    public void drawPlayerLife(){
      int x=gp.tileSize/2;
      int y=gp.tileSize/2;
      int i=0;
      while(i<gp.player.life){
          g2.drawImage(heart_full,x,y,null);
          i++;
          x+=gp.tileSize;

      }
        while(i<gp.player.maxLife){
            g2.drawImage(heart_blank,x,y,null);
            i++;
            x+=gp.tileSize;

        }


    }
    public void drawGameOverScreen(){
        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,110f));
        text="Game Over";
        g2.setColor(Color.black);
        x=getXforCenteredText(text);
        y=GamePanel.tileSize*4;
        g2.drawString(text,x,y);
        g2.setColor(Color.white);
        g2.drawString(text,x-4,y-4);
        g2.setFont(g2.getFont().deriveFont(50f));
        text="Retry";
        x=getXforCenteredText(text);
        y+=GamePanel.tileSize*4;
        g2.drawString(text,x,y);
         if(commandNum == 0){
             g2.drawString(">",x-40,y);
         }
        text="Quit";
        x=getXforCenteredText(text);
        y+=55;
        g2.drawString(text,x,y);
      if(commandNum==1){
              g2.drawString(">",x-40,y);
     }



    }
    public void drawTitleScreen(){
        g2.setColor(Color.BLACK);
        g2.fillRect(0,0,gp.screenWidth,gp.screenWidth);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,96f));
        String text="Dug is Free";
        int x=getXforCenteredText(text);
        int y=GamePanel.tileSize*3;
        g2.setColor(Color.white);
        g2.drawString(text,x+5,y+5);
        g2.setColor(Color.BLUE);
        g2.drawString(text,x,y);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,48f));
        text="New Game";
        x=getXforCenteredText(text);
        y+=GamePanel.tileSize*4;
        g2.drawString(text,x,y);
        if(commandNum==0){
            g2.drawString(">",x-GamePanel.tileSize,y);
        }
        text="Load Game";
        x=getXforCenteredText(text);
        y+=GamePanel.tileSize*1;
        g2.drawString(text,x,y);
        if(commandNum==1){
            g2.drawString(">",x-GamePanel.tileSize,y);
        }
        text="Quit";
        x=getXforCenteredText(text);
        y+=GamePanel.tileSize*1;
        g2.drawString(text,x,y);
        if(commandNum==2){
            g2.drawString(">",x-GamePanel.tileSize,y);
        }

    }
    public void drawEndGameState(){
        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,40f));
        text="Congratulations! You finished the game!";
        g2.setColor(Color.white);
        x=getXforCenteredText(text);
        y=GamePanel.tileSize*3;
        g2.drawString(text,x,y);
        g2.setFont(g2.getFont().deriveFont(50f));
        text="Quit";
        x=getXforCenteredText(text);
        y+=GamePanel.tileSize*2;
        g2.drawString(text,x,y);
        if(commandNum==1){
            g2.drawString(">",x-40,y);
        }
    }
}
