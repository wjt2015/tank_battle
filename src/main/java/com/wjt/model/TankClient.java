package com.wjt.model;

import com.wjt.common.Constants;
import com.wjt.common.PlayerType;
import com.wjt.common.Utils;
import lombok.extern.slf4j.Slf4j;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @Time 2020/3/29/16:47
 * @Author jintao.wang
 * @Description
 */
@Slf4j
public class TankClient extends JFrame implements Runnable {

    public int width = 1200;
    public int height = 1000;
    int d = 5;

    /**
     * 游戏区域边界;
     */
    public final Rectangle RECT = new Rectangle(0 + d, 0 + d, width - 2 * d, height - 2 * d);

    public final int X1 = RECT.x, Y1 = RECT.y, X2 = RECT.x + RECT.width, Y2 = RECT.y + RECT.height;
    /**
     * 双缓冲用的虚拟图片;
     */
    public final BufferedImage OFF_SCREEN = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

    /**
     * 所有的坦克;
     */
    public final TankContainer TANK_CONTAINER = new TankContainer();

    /**
     * 炮弹容器;
     */
    public final ConcurrentHashMap<Missile, Object> MISSILES = new ConcurrentHashMap<>(50);

    private void paintOffScreen() {
        Graphics2D g2d = (Graphics2D) (OFF_SCREEN.getGraphics());
        g2d.setBackground(Color.GREEN);
        g2d.clearRect(0, 0, width, height);

        //绘制玩家的坦克;
        TANK_CONTAINER.playerTanks.keySet().forEach(playerTank -> {
            playerTank.draw(g2d);
        });
        //绘制敌人的坦克;
        TANK_CONTAINER.enemyTanks.keySet().forEach(enemyTank -> {
            enemyTank.draw(g2d);
        });
        //绘制炮弹;
        MISSILES.keySet().forEach(missile -> {
            missile.draw(g2d);
        });

        showCount(g2d);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        //在内存绘制;
        paintOffScreen();
        //复制;
        g2d.drawImage(OFF_SCREEN, 0, 0, null);
    }

    public TankClient() {
        setLocation(0, 0);
        setSize(width, height);
        setTitle("tankWar");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //setResizable(false);
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                log.info("window has been opened!e={};", e);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                log.info("window has been closing!e={};", e);
                System.exit(0);

            }

            @Override
            public void windowClosed(WindowEvent e) {
                log.info("window has been closed!e={};", e);
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                //仅控制玩家坦克;
                TANK_CONTAINER.playerTanks.keySet().forEach(playerTank -> {
                    playerTank.keyPressed(e);
                });
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //仅控制玩家坦克;
                TANK_CONTAINER.playerTanks.keySet().forEach(playerTank -> {
                    playerTank.keyReleased(e);
                });
            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                log.info("mouseXY=({},{});mouseXYOnScreen=({},{});clickCount={};",
                        e.getX(), e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount());
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        //玩家坦克;
        new Tank(RECT, PlayerType.PLAYER_A, TANK_CONTAINER, MISSILES);

        setVisible(true);
    }

    @Override
    public void run() {
        final long start = System.currentTimeMillis();
        //生产敌人坦克;
        buildEnemyTank();
        //敌人坦克发射炮弹;
        enemyFire();

        //移动玩家的坦克;
        TANK_CONTAINER.playerTanks.keySet().forEach(playerTank -> {
            playerTank.move();
        });

        //移动敌人的坦克;
        this.TANK_CONTAINER.enemyTanks.keySet().forEach(enemyTank -> {
            enemyTank.setSpeed();
            enemyTank.move();
        });

        //移动炮弹;
        MISSILES.keySet().forEach(missile -> {
            missile.move();
        });
        repaint();
        final long elapsed = System.currentTimeMillis() - start;
        //log.info("TANK_CONTAINER={};elapsed={}ms;", TANK_CONTAINER, elapsed);
        log.info("run_finish;elapsed={}ms;", elapsed);
    }

    /**
     * 生成敌人坦克;
     */
    public void buildEnemyTank() {

        if (TANK_CONTAINER.enemyTanks.keySet().size() >= 20) {
            return;
        }
        final long start = System.currentTimeMillis();
        int n = 0, x = Constants.INIT_X, y = Constants.INIT_Y + 200;
        if (TANK_CONTAINER.enemyTanks.keySet().size() > 30) {
            n = Math.abs(Constants.RANDOM.nextInt()) % 5;
        } else if (TANK_CONTAINER.enemyTanks.keySet().size() > 20) {
            n = Math.abs(Constants.RANDOM.nextInt()) % 11;
        } else if (TANK_CONTAINER.enemyTanks.keySet().size() > 10) {
            n = Math.abs(Constants.RANDOM.nextInt()) % 17;
        } else {
            n = Math.abs(Constants.RANDOM.nextInt()) % 17 + 1;
        }

        for (int i = 0; i < n; i++) {
            if (x >= X2) {
                x = Constants.INIT_X;
                y += (Constants.TANK_LENGTH << 1);
            } else {
                x += Constants.TANK_LENGTH;
            }
            new Tank(x, y, this.RECT, PlayerType.PLAYER_D, TANK_CONTAINER, MISSILES);
        }
        final long elapsed = System.currentTimeMillis() - start;
        //log.info("n={};TANK_CONTAINER={};elapsed={}ms;", n, TANK_CONTAINER, elapsed);
        log.info("n={};elapsed={}ms;", n, elapsed);
    }

    /**
     * 敌人坦克发射炮弹;
     */
    public void enemyFire() {
        final int sum = TANK_CONTAINER.enemyTanks.keySet().size();
        if (sum >= 1) {
            //n个发射机会;
            final int n = Constants.RANDOM.nextInt(sum + 1);
            final double threhold = 100.0 * n / sum;
            final int floor = 131, ceil = floor + (int) (threhold);
            TANK_CONTAINER.enemyTanks.keySet().forEach(enemyTank -> {
                int t = Constants.RANDOM.nextInt(1501);
                if (t >= floor && t < ceil) {
                    //概率发射;
                    enemyTank.fire();
                }
            });
        }
    }

    public void showCount(Graphics2D g2d) {
        Color oldColor = g2d.getColor();
        g2d.setColor(Color.MAGENTA);
        String str = new StringBuilder().append("player_count:")
                .append(TANK_CONTAINER.playerTanks.size())
                .append("  ")
                .append("enemy_count:")
                .append(TANK_CONTAINER.enemyTanks.size())
                .append("  ")
                .append("missile_count:")
                .append(MISSILES.size())
                .substring(0);
        g2d.drawString(str, 150, 50);
        g2d.setColor(oldColor);
    }

}
