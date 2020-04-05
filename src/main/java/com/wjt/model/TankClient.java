package com.wjt.model;

import com.wjt.common.Constants;
import com.wjt.common.PlayerType;
import lombok.extern.slf4j.Slf4j;

import javax.swing.JFrame;
import java.awt.BasicStroke;
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
    public final Rectangle GAME_RECT = new Rectangle(0 + d, 0 + d, width - 2 * d, height - 2 * d);

    public final int X1 = GAME_RECT.x, Y1 = GAME_RECT.y, X2 = GAME_RECT.x + GAME_RECT.width, Y2 = GAME_RECT.y + GAME_RECT.height;
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

    /**
     * 爆炸容器;
     */
    public final ConcurrentHashMap<Bomb, Object> BOMB_CONTAINER = new ConcurrentHashMap<>(50);

    /**
     * 墙的容器;
     */
    public final ConcurrentHashMap<Wall, Object> WALL_CONTAINER = new ConcurrentHashMap<>(10);

    /**
     * 总的敌人数量;
     */
    public volatile int enemyCount = 50;

    /**
     * 当前可用的血块容器;
     */
    public final ConcurrentHashMap<Blood, Object> BLOOD_CONTAINER = new ConcurrentHashMap<>();

    private void paintOffScreen() {

/*        if (checkGameEnd()) {
            return;
        }*/
        Graphics2D g2d = (Graphics2D) (OFF_SCREEN.getGraphics());
        g2d.setBackground(Color.GREEN);
        g2d.clearRect(0, 0, width, height);

        checkGameWin(g2d);
/*        if (checkGameWin(g2d)) {
            this.TANK_CONTAINER.clear();
            return;
        }*/

        checkGameOver(g2d);
/*        if (checkGameOver(g2d)) {
            this.TANK_CONTAINER.clear();
            return;
        }*/

        //绘制血块;
        BLOOD_CONTAINER.keySet().forEach(blood -> {
            blood.draw(g2d);
        });

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

        //绘制爆炸;
        this.BOMB_CONTAINER.keySet().forEach(bomb -> {
            bomb.draw(g2d);
        });

        //绘制墙;
        this.WALL_CONTAINER.keySet().forEach(wall -> {
            wall.draw(g2d);
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

                if (TANK_CONTAINER.playerTanks.size() == 0 && e.getKeyCode() == KeyEvent.VK_1) {
                    //新增一个玩家坦克;
                    new Tank(GAME_RECT, PlayerType.PLAYER_A, TANK_CONTAINER, MISSILES, BOMB_CONTAINER);
                }
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
        new Tank(GAME_RECT, PlayerType.PLAYER_A, TANK_CONTAINER, MISSILES, this.BOMB_CONTAINER);

        //墙;
        new Wall(200, 200, 200, 10, this.WALL_CONTAINER);
        new Wall(400, 400, 300, 10, this.WALL_CONTAINER);
        new Wall(200, 400, 10, 200, this.WALL_CONTAINER);
        new Wall(300, 600, 10, 200, this.WALL_CONTAINER);
        new Wall(300, 750, 300, 10, this.WALL_CONTAINER);
        new Wall(300, 820, 300, 10, this.WALL_CONTAINER);

        setVisible(true);
    }

    @Override
    public void run() {
        final long start = System.currentTimeMillis();
        //血块出现;
        bloodAppear();

        //坦克吃掉血块;
        TANK_CONTAINER.playerTanks.keySet().forEach(playerTank -> {
            BLOOD_CONTAINER.keySet().forEach(blood -> {
                playerTank.hitBlood(blood);
            });
        });

        //血块移动;
        BLOOD_CONTAINER.keySet().forEach(blood -> {
            blood.proceed();
            blood.move();
        });


        //生产敌人坦克;
        buildEnemyTank();
        //敌人坦克发射炮弹;
        enemyFire();
        //炮击坦克检测;
        hitTanks();
        //炮击墙检测;
        hitWalls();

        //敌人坦克改变速度;
        this.TANK_CONTAINER.enemyTanks.keySet().forEach(enemyTank -> {
            enemyTank.setSpeed();
        });

        //坦克穿越检测;
        //stopTankPassThrough();

        //移动玩家的坦克;
        TANK_CONTAINER.playerTanks.keySet().forEach(playerTank -> {
            //先执行撞墙检测;
            if (playerTank.mayHitWalls(this.WALL_CONTAINER.keySet()) == null) {
                playerTank.move();
            }
        });

        //移动敌人的坦克;
        this.TANK_CONTAINER.enemyTanks.keySet().forEach(enemyTank -> {
            //先执行撞墙检测;
            if (enemyTank.mayHitWalls(this.WALL_CONTAINER.keySet()) == null) {
                enemyTank.move();
            }
        });

        //移动炮弹;
        this.MISSILES.keySet().forEach(missile -> {
            missile.move();
        });
        //爆炸;
        this.BOMB_CONTAINER.keySet().forEach(bomb -> {
            bomb.proceed();
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

        if (TANK_CONTAINER.enemyTanks.keySet().size() >= 8) {
            return;
        }
        if (enemyCount <= 0) {
            return;
        }
        final long start = System.currentTimeMillis();
        int n = 0, x = Constants.INIT_X, y = Constants.INIT_Y + 200;
        if (TANK_CONTAINER.enemyTanks.keySet().size() > 5) {
            n = Math.abs(Constants.RANDOM.nextInt()) % 3 + 1;
        } else if (TANK_CONTAINER.enemyTanks.keySet().size() > 3) {
            n = Math.abs(Constants.RANDOM.nextInt()) % 5 + 1;
        } else if (TANK_CONTAINER.enemyTanks.keySet().size() > 1) {
            n = Math.abs(Constants.RANDOM.nextInt()) % 7 + 1;
        } else {
            n = Math.abs(Constants.RANDOM.nextInt()) % 7 + 1;
        }

        //最大的坦克产量限制;
        n = (n <= enemyCount ? n : enemyCount);

        for (int i = 0; i < n; i++) {
            if (x >= X2) {
                x = Constants.INIT_X;
                y += (Constants.TANK_LENGTH << 1);
            } else {
                x += Constants.TANK_LENGTH << 1;
            }
            new Tank(x, y, this.GAME_RECT, PlayerType.PLAYER_D, TANK_CONTAINER, MISSILES, this.BOMB_CONTAINER);
        }
        this.enemyCount -= n;
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
            final int n = Constants.RANDOM.nextInt(sum + 1) + 1;
            double threshold = 100.0 * n / (sum + 1);
            //敌人的坦克越少,开炮的几率越大;
            final int enemyCount = this.TANK_CONTAINER.enemyTanks.size();
            if (enemyCount < 3) {
                threshold += 60;
            } else if (enemyCount < 6) {
                threshold += 30;
            }
            final int floor = 71, ceil = floor + (int) (threshold);
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
                .append("fighting_enemy_count:")
                .append(TANK_CONTAINER.enemyTanks.size())
                .append("  ")
                .append("left_enemy_count:")
                .append(enemyCount)
                .append("  ")
                .append("missile_count:")
                .append(MISSILES.size())
                .substring(0);
        g2d.drawString(str, 150, 50);
        g2d.drawString(getPlayerScore(), 150, 70);
        g2d.setColor(oldColor);
    }

    public String getPlayerScore() {
        StringBuilder stringBuilder = new StringBuilder();
        TANK_CONTAINER.playerTanks.keySet().forEach(playerYank -> {
            stringBuilder.append(playerYank.playerType)
                    .append(":")
                    .append(playerYank.score);
        });
        return stringBuilder.substring(0);
    }

    /**
     * 炮弹击中坦克的检测;
     */
    public void hitTanks() {
        for (Missile missile : MISSILES.keySet()) {
            //敌人的炮弹只能打击玩家;
            if (missile.TANK.playerType == PlayerType.PLAYER_D) {
                for (Tank playerTank : TANK_CONTAINER.playerTanks.keySet()) {
                    if (missile.hitTank(playerTank)) {
                        break;
                    }
                }
            } else {
                //玩家的炮弹只能打击敌人;
                for (Tank enemyTank : TANK_CONTAINER.enemyTanks.keySet()) {
                    if (missile.hitTank(enemyTank)) {
                        break;
                    }
                }
            }
        }
    }

    public void hitWalls() {
        this.MISSILES.keySet().forEach(missile -> {
            this.WALL_CONTAINER.keySet().forEach(wall -> {
                missile.hitWall(wall);
            });
        });
    }

    public boolean checkGameOver(Graphics2D g2d) {
        if (TANK_CONTAINER.playerTanks.isEmpty()) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(50));
            g2d.drawString("Game Over", 500, 500);
            return true;
        } else {
            return false;
        }
    }

    public boolean checkGameWin(Graphics2D g2d) {
        if (enemyCount <= 0 && this.TANK_CONTAINER.enemyTanks.isEmpty() && !this.TANK_CONTAINER.playerTanks.isEmpty()) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(50));

            StringBuilder summaryBuilder = new StringBuilder()
                    .append("Game Win!!");
            this.TANK_CONTAINER.playerTanks.keySet().forEach(playerTank -> {
                summaryBuilder.append(playerTank.playerType.desc).append("_score:")
                        .append(playerTank.score);
            });
            g2d.drawString(summaryBuilder.substring(0), 500, 500);
            return true;
        } else {
            return false;
        }
    }

    public boolean checkGameEnd() {
        return this.TANK_CONTAINER.isEmpty() && enemyCount <= 0;
    }

    /**
     * 阻止坦克穿越;
     */
    public void stopTankPassThrough() {
        this.TANK_CONTAINER.playerTanks.keySet().forEach(playerTank1 -> {
            this.TANK_CONTAINER.playerTanks.keySet().forEach(playerTank2 -> {
                if (playerTank1.willPassTank(playerTank2)) {
                    playerTank1.stop();
                    playerTank2.stop();
                }
            });
            this.TANK_CONTAINER.enemyTanks.keySet().forEach(enemyTank -> {
                if (playerTank1.willPassTank(enemyTank)) {
                    playerTank1.stop();
                    enemyTank.stop();
                }
            });
        });

        this.TANK_CONTAINER.enemyTanks.keySet().forEach(enemyTank1 -> {
            this.TANK_CONTAINER.enemyTanks.keySet().forEach(enemyTank2 -> {
                if (enemyTank1.willPassTank(enemyTank2)) {
                    enemyTank1.stop();
                    enemyTank2.stop();
                }
            });
        });
    }

    /**
     * 剩余的血块数量;
     */
    public volatile int leftBloodCount = 10;

    /**
     * 血块随机出现;
     */
    public void bloodAppear() {
        final int t = Constants.RANDOM.nextInt(600);
        if (this.leftBloodCount >= 1 && t > 100 && t < 230 && BLOOD_CONTAINER.size() < 3) {
            this.leftBloodCount--;
            final int idx = t % Constants.BLOOD_INIT_POS.length;
            new Blood(Constants.BLOOD_INIT_POS[idx][0], Constants.BLOOD_INIT_POS[idx][1], this.BLOOD_CONTAINER, GAME_RECT);
            log.info("test;new blood;this.BLOOD_CONTAINER.size={};", this.BLOOD_CONTAINER.size());
        }
    }


}
