package com.wjt.model;

import com.wjt.common.Constants;
import com.wjt.common.Direction;
import com.wjt.common.PlayerType;
import lombok.extern.slf4j.Slf4j;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Time 2020/3/29/22:46
 * @Author jintao.wang
 * @Description 玩家的坦克由玩家控制;敌人的坦克由系统自动控制,总是向玩家的方向运动,向玩家开炮,并能避开障碍物;
 * 所有坦克的位置坐标的间距都是单步距离的整数倍;
 */
@Slf4j
public class Tank {
    public volatile int x, y;
    public volatile int xv, yv;
    /**
     * 坦克大小;
     */
    public volatile int length, width;

    public volatile Color color;
    public volatile Direction direction;

    /**
     * 边界;
     */
    public volatile Rectangle rect;


    public volatile GunBarrel gunBarrel;

    /**
     * 所属玩家;
     */
    public PlayerType playerType;
    /**
     * 引用所有的坦克容器;
     */
    public final TankContainer tankContainer;


    /**
     * 炮弹容器;
     */
    //public static final ConcurrentSkipListSet<Missile> MISSILES = new ConcurrentSkipListSet<>();
    //public final ConcurrentHashMap<Missile, Object> MISSILES = new ConcurrentHashMap<>(50);
    public final ConcurrentHashMap<Missile, Object> MISSILES;

    /**
     * 爆炸容器;
     */
    public final ConcurrentHashMap<Bomb, Object> BOMB_CONTAINER;

    public int score;

    public Tank(Rectangle rect, PlayerType playerType, TankContainer tankContainer, ConcurrentHashMap<Missile, Object> missiles, ConcurrentHashMap<Bomb, Object> bombContainer) {
        this(Constants.INIT_X, Constants.INIT_Y, rect, playerType, tankContainer, missiles, bombContainer);
    }

    public Tank(int x, int y, Rectangle rect, PlayerType playerType, TankContainer tankContainer, ConcurrentHashMap<Missile, Object> missiles, ConcurrentHashMap<Bomb, Object> bombContainer) {
        this.x = x;
        this.y = y;
        xv = 0;
        yv = 0;
        length = Constants.TANK_LENGTH;
        width = Constants.TANK_WIDTH;
        direction = Direction.BOTTOM;
        this.rect = rect;
        this.MISSILES = missiles;
        this.BOMB_CONTAINER = bombContainer;
        log.info("Before_GunBarrel;playerType={};", playerType);
        this.gunBarrel = new GunBarrel(x + (length >> 1), y + (width >> 1), this.direction, Constants.GUN_BARREL_LENGTH, Color.DARK_GRAY);

        this.playerType = playerType;

        this.tankContainer = tankContainer;
        if (this.playerType == PlayerType.PLAYER_D) {
            //敌人坦克;
            this.tankContainer.enemyTanks.put(this, Boolean.TRUE);
            this.color = Color.YELLOW;
            this.score = Constants.ENEMY_SCORE;
        } else {
            //玩家坦克;
            this.tankContainer.playerTanks.put(this, Boolean.TRUE);
            this.color = Color.RED;
            this.score = Constants.PLAYER_SCORE;
        }
        //log.info("this.playerType={};this.tankContainer={};", this.playerType, this.tankContainer);
        log.info("this.playerType={};this.color={};init_pos=({},{});", this.playerType, this.color, this.x, this.y);
    }


    public void draw(Graphics2D g2d) {
        //绘制坦克;
        Color oldColor = g2d.getColor();
        g2d.setColor(color);
        g2d.fillOval(x, y, length, width);
        g2d.setColor(oldColor);
        //绘制炮管;
        this.gunBarrel.draw(g2d);
        //绘制炮弹;
/*        for (Missile missile : MISSILES.keySet()) {
            missile.move();
            missile.draw(g2d);
        }*/
    }

    public void keyPressed(KeyEvent e) {
        final int oldXv = xv, oldYv = yv;
        if (this.playerType == PlayerType.PLAYER_A) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_W) {
                if (yv >= 0) {
                    yv -= Constants.TANK_YV;
                }
                this.direction = Direction.UPPER;
            } else if (keyCode == KeyEvent.VK_A) {
                if (xv >= 0) {
                    xv -= Constants.TANK_XV;
                }
                this.direction = Direction.LEFT;
            } else if (keyCode == KeyEvent.VK_S) {
                if (yv <= 0) {
                    yv += Constants.TANK_YV;
                }
                this.direction = Direction.BOTTOM;
            } else if (keyCode == KeyEvent.VK_D) {
                if (xv <= 0) {
                    xv += Constants.TANK_XV;
                }
                this.direction = Direction.RIGHT;
            } else if (keyCode == KeyEvent.VK_J) {
                fire();
            }
        }
        log.info("keyPressed;this.playerType={};this.color={};e={};oldV=({},{});newV=({},{});this.direction={};", this.playerType, this.color, e.getKeyChar(), oldXv, oldYv, xv, yv, this.direction);
    }

    /**
     * 发射炮弹;
     */
    public void fire() {
        int missileX = (x + (this.length >> 1)) - (Constants.MISSILE_LENGTH >> 1), missileY = (y + (this.width >> 1) - (Constants.MISSILE_WIDTH >> 1));
        Color missileColor = (this.playerType == PlayerType.PLAYER_D ? Color.MAGENTA : Color.BLACK);
        new Missile(missileX, missileY, this.direction, this, this.rect, missileColor);
    }

    public void keyReleased(KeyEvent e) {
        if (this.playerType == PlayerType.PLAYER_A) {
            //仅玩家坦克受控制;
            this.xv = this.yv = 0;
        }
    }


    public void move() {
        x += xv;
        y += yv;
        //防止越界;
        int left = rect.x, right = left + rect.width, upper = rect.y, bottom = upper + rect.height;
        x = (x < left ? left : x);
        x = (x > right ? right : x);
        y = (y < upper ? upper : y);
        y = (y > bottom ? bottom : y);
        //移动炮管;
        this.gunBarrel.move(x + (length >> 1), y + (width >> 1), this.direction);
        log.info("this.playerType={};this.color={};move_pos=({},{});", this.playerType, this.color, this.x, this.y);
    }

    /**
     * 销毁坦克;
     */
    public void destroy() {
        if (playerType == PlayerType.PLAYER_D) {
            this.tankContainer.enemyTanks.remove(this);
        } else {
            this.tankContainer.playerTanks.remove(this);
        }
    }

    public void setSpeed() {
        int size = this.tankContainer.playerTanks.size(), d = 40;
        //增加随机性;
        int t = Constants.RANDOM.nextInt(1501);
        if (this.playerType == PlayerType.PLAYER_D && size >= 1) {
            /**
             * 只能自动设定敌人坦克的速度;
             * 敌人的坦克由系统自动控制;
             * 主要有三种移动策略:
             * 某一玩家的方向运动,向玩家开炮,并能避开障碍物;
             * 随机运动;
             * 保持原样;
             */

            //敌人的坦克越少,进攻的几率越大;
            final int enemyCount = this.tankContainer.enemyTanks.size();
            int floor = 10, ceil = 100;
            if (enemyCount < 3) {
                ceil += 200;
            } else if (enemyCount < 6) {
                ceil += 100;
            }

            if (t > floor && t < ceil) {
                int i = Math.abs(Constants.RANDOM.nextInt());

                ArrayList<Tank> playerTanks = new ArrayList<>(this.tankContainer.playerTanks.keySet());
                size = playerTanks.size();
                log.info("playerTanks={};tank_pos=({},{});", playerTanks, this.x, this.y);
                if (size >= 1) {
                    //随机选择一个玩家坦克,并向它进攻;
                    Tank playerTank = playerTanks.get(i % size);

                    if (playerTank.x < this.x) {
                        this.xv = -Constants.TANK_XV;
                        if (playerTank.y < this.y) {
                            this.direction = Direction.LEFT_UPPER;
                            this.yv = -Constants.TANK_YV;
                        } else if (playerTank.y == this.y) {
                            this.direction = Direction.LEFT;
                            this.yv = 0;
                        } else {
                            this.direction = Direction.LEFT_BOTTOM;
                            this.yv = Constants.TANK_YV;
                        }
                    } else if (playerTank.x == this.x) {
                        this.xv = 0;

                        if (playerTank.y < this.y) {
                            this.direction = Direction.UPPER;
                            this.yv = -Constants.TANK_YV;
                        } else if (playerTank.y == this.y) {
/*                        this.direction = Direction.LEFT;
                        this.yv = 0;*/
                        } else {
                            this.direction = Direction.BOTTOM;
                            this.yv = Constants.TANK_YV;
                        }
                    } else {
                        this.xv = Constants.TANK_XV;

                        if (playerTank.y < this.y) {
                            this.direction = Direction.RIGHT_UPPER;
                            this.yv = -Constants.TANK_YV;
                        } else if (playerTank.y == this.y) {
                            this.direction = Direction.RIGHT;
                            this.yv = 0;
                        } else {
                            this.direction = Direction.RIGHT_BOTTOM;
                            this.yv = Constants.TANK_YV;
                        }
                    }
                }
            } else if (t >= 300 && t < (300 + d)) {
                this.direction = Direction.UPPER;
                this.xv = 0;
                this.yv = -Constants.TANK_YV;
            } else if (t >= 400 && t < (400 + d)) {
                this.direction = Direction.RIGHT_UPPER;
                this.xv = Constants.TANK_XV;
                this.yv = -Constants.TANK_YV;
            } else if (t >= 500 && t < (500 + d)) {
                this.direction = Direction.RIGHT;
                this.xv = Constants.TANK_XV;
                this.yv = 0;
            } else if (t >= 600 && t < (600 + d)) {
                this.direction = Direction.RIGHT_BOTTOM;
                this.xv = Constants.TANK_XV;
                this.yv = Constants.TANK_YV;
            } else if (t >= 700 && t < (700 + d)) {
                this.direction = Direction.BOTTOM;
                this.xv = 0;
                this.yv = Constants.TANK_YV;
            } else if (t >= 800 && t < (800 + d)) {
                this.direction = Direction.LEFT_BOTTOM;
                this.xv = -Constants.TANK_XV;
                this.yv = Constants.TANK_YV;
            } else if (t >= 900 && t < (900 + d)) {
                this.direction = Direction.LEFT;
                this.xv = -Constants.TANK_XV;
                this.yv = 0;
            } else if (t >= 1000 && t < (1000 + d)) {
                this.direction = Direction.LEFT_UPPER;
                this.xv = -Constants.TANK_XV;
                this.yv = -Constants.TANK_YV;
            } else if (t >= 1100 && t < (1000 + d)) {
                this.xv = -this.xv;
            } else if (t >= 1100 && t < (1100 + d)) {
                this.yv = -this.yv;
            } else {
                //保持原样;
            }
        }
    }

    public void addScore() {
        this.score += Constants.HIT_ADD_SCORE;
    }

    public void subScore() {
        this.score -= Constants.HIT_SUB_SCORE;
        if (this.score <= 0) {
            //死亡;
            destroy();
        }
    }


    /**
     * 撞墙检测;
     *
     * @param walls
     * @return 第一个会撞到的墙;
     */
    public Wall mayHitWalls(Collection<Wall> walls) {
        int newX = this.x + this.xv;
        int newY = this.y + this.yv;
        Rectangle tankRect = new Rectangle(newX, newY, this.length, this.width);
        for (Wall wall : walls) {
            if (wall.rect.intersects(tankRect)) {
                return wall;
            }
        }
        return null;
    }


    /**
     * 检测是否有坦克穿越;
     *
     * @param other
     * @return
     */
    public boolean willPassTank(Tank other) {
        return (this == other ? false : this.newTankRect().intersects(other.newTankRect()));
    }


    public Rectangle newTankRect() {
        int newX = this.x + this.xv;
        int newY = this.y + this.yv;
        return new Rectangle(newX, newY, this.length, this.width);
    }


    public void stop() {
        this.xv = this.yv = 0;
    }


    /**
     * 逆向运动;
     */
    public void reverseMove() {
        this.direction = this.direction.reverseDirection();
        this.xv = -this.xv;
        this.yv = -this.yv;
    }

    /**
     * 遇到障碍物随机改变动向;
     */
    public void randomMoveWhileBlocked() {
        int t = Constants.RANDOM.nextInt(300);
        if (t < 50) {
            this.xv = -Constants.TANK_XV;
            this.yv = 0;
            this.direction = Direction.LEFT;
        } else if (t < 100) {
            this.xv = 0;
            this.yv = -Constants.TANK_YV;
            this.direction = Direction.UPPER;
        } else if (t < 150) {
            this.xv = Constants.TANK_XV;
            this.yv = 0;
            this.direction = Direction.RIGHT;
        } else if (t < 200) {
            this.xv = 0;
            this.yv = Constants.TANK_YV;
            this.direction = Direction.BOTTOM;
        } else {
            this.xv = this.yv = 0;
        }
    }

}
