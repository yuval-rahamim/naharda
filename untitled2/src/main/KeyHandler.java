package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    GamePanel gp;
    public boolean upPressed, downPressed, rightPressed, leftPressed, enterPressed,shotKeyPressed;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void tileState(int code)
    {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP)// w or up arrow
        {
            gp.ui.commandNum--;
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = 2;
            }
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.ui.commandNum++;
            if (gp.ui.commandNum>2)
            {
                gp.ui.commandNum = 0;
            }
        }
        if (code == KeyEvent.VK_ENTER) {
            switch (gp.ui.commandNum)
            {
                case 0:
                    gp.gameState=gp.playState;
                    break;
                case 1:
                    break;
                case 2:
                    System.exit(0);
                    break;
            }
        }
    }

    public void playState(int code){
        if (code == KeyEvent.VK_C)
        {
            gp.gameState = gp.characterState;
        }
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP)// w or up arrow
        {
            upPressed = true;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_P) {
            gp.gameState = gp.pauseState;
            System.out.printf("x: "+ (gp.player.WorldX/gp.tileSize+1)+" Y: "+(gp.player.WorldY/gp.tileSize+1) +"\n");
        }
        if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_X) {
            enterPressed = true;
        }
        if (code == KeyEvent.VK_F) {
            shotKeyPressed = true;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (gp.gameState== gp.titleState) {
            tileState(code);
        }
        else if(gp.gameState==gp.playState) {
           playState(code);
        } else if (gp.gameState==gp.pauseState) {
            if (code == KeyEvent.VK_P) {
                gp.gameState = gp.playState;
            }
        } else if(gp.gameState==gp.dialogState)
        {
           if (code == KeyEvent.VK_ENTER)
           {
               gp.gameState = gp.playState;
           }
        } else if (gp.gameState==gp.characterState)
        {
            if (code == KeyEvent.VK_C)
            {
                gp.gameState = gp.playState;
            }
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP)// w or up arrow
            {
                if (gp.ui.slotRow != 0) {
                    gp.ui.slotRow--;
                }
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                if (gp.ui.slotRow != 3) {
                    gp.ui.slotRow++;
                }
            }
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
                if (gp.ui.slotCol != 0) {
                    gp.ui.slotCol--;
                }
            }
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT)// w or up arrow
            {
                if (gp.ui.slotCol != 4) {
                    gp.ui.slotCol++;
                }
            }

            if (code == KeyEvent.VK_ENTER || code==KeyEvent.VK_X) {
                gp.player.selectItem();
            }
        } else if (gp.gameState==gp.CecileState) {
            if (code == KeyEvent.VK_ENTER) {
                gp.gameState = gp.playState;
                gp.player.exp+=100;
                gp.player.checkLevelUp();
            }
        }else if (gp.gameState==gp.MansonState) {
            if (code == KeyEvent.VK_ENTER) {
                gp.gameState = gp.playState;
                gp.player.exp+=100;
                gp.player.checkLevelUp();
            }
        } else if (gp.gameState==gp.gameOverState)
        {
            if (code == KeyEvent.VK_ENTER){
                gp.gameState = gp.playState;
                gp.retry();
            }else if (code == KeyEvent.VK_R){
                gp.gameState = gp.playState;
                gp.restart();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP)// w or up arrow
        {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S|| code == KeyEvent.VK_DOWN)
        {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A|| code == KeyEvent.VK_LEFT)
        {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D|| code == KeyEvent.VK_RIGHT)
        {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_F) {
            shotKeyPressed = false;
        }
    }
}
