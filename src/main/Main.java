package main;

import javax.swing.*;
import java.io.IOException;



public class Main {
    public static void main(String[] args)  {

            JFrame window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setTitle("Dug is Free");
            GamePanel gamePanel = GamePanel.getInstance();
            window.add(gamePanel);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);

            gamePanel.setupGame();
            gamePanel.startGameThread();
        }

    }



