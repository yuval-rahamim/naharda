package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        window.setTitle("The POEM of NAHARDA");

        GamePanel gamePanel = new GamePanel();
        window.setIconImage(gamePanel.player.down1);

        window.add(gamePanel);
        window.pack();

        window.setLocationRelativeTo(null);

        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}