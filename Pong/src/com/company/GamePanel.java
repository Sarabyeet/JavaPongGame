package com.company;

import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

public class GamePanel extends JPanel implements Runnable{

    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.5555));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;

    Thread thread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;

    GamePanel(){
        newPaddles();
        newBall();
        score = new Score(GAME_WIDTH,GAME_HEIGHT);
        this.setPreferredSize(SCREEN_SIZE);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        thread = new Thread(this);
        thread.start();

    }

    private void newBall(){
        random = new Random();
        ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2), random.nextInt(GAME_HEIGHT-BALL_DIAMETER), BALL_DIAMETER, BALL_DIAMETER);
//        ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2), (GAME_HEIGHT/2)-(BALL_DIAMETER/2), BALL_DIAMETER, BALL_DIAMETER);
    }

    private void newPaddles(){
        paddle1 = new Paddle(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,1);
        paddle2 = new Paddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,2);
    }

    public void paint(Graphics g){
        image = createImage(getWidth(),getHeight());
        graphics = image.getGraphics();

        draw(graphics);
        g.drawImage(image, 0,0,this);

    }
    private void draw(Graphics g){
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);
    }

    private void move(){
        paddle1.move();
        paddle2.move();
        ball.move();
    }

    private void checkCollision(){
        // Bounces ball off the edges

        if (ball.y<=0){
            ball.setYDirection(-ball.yVelocity);
        }
        if (ball.y>=GAME_HEIGHT-BALL_DIAMETER){
            ball.setYDirection(-ball.yVelocity);
        }
        // Bounces ball off paddles
        if(ball.intersects(paddle1)){  // intersects method is available to rectangle class
            // ball.setXDirection(-ball.xVelocity);
            // or
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; // Optional for more difficulty
            if(ball.yVelocity>0){
                ball.yVelocity++; // Optional for more difficulty
            }
            else {
                ball.yVelocity--;
            }
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if(ball.intersects(paddle2)){
            // ball.setXDirection(-ball.xVelocity);
            // or
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; // Optional for more difficulty
            if(ball.yVelocity>0){
                ball.yVelocity++; // Optional for more difficulty
            }
            else {
                ball.yVelocity--;
            }
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        // Stops paddle at window edges

        if (paddle1.y<=0){
            paddle1.y = 0;
        }
        if (paddle1.y>=GAME_HEIGHT-PADDLE_HEIGHT){
            paddle1.y = GAME_HEIGHT-PADDLE_HEIGHT;
        }
        if (paddle2.y<=0){
            paddle2.y = 0;
        }
        if (paddle2.y>=GAME_HEIGHT-PADDLE_HEIGHT){
            paddle2.y = GAME_HEIGHT-PADDLE_HEIGHT;
        }

        // Gives 1 point and creates new point

        if(ball.x<=0){
            score.player2++;
            newPaddles();
            newBall();
            System.out.println("Player 3: "+score.player2);
        }
        if(ball.x>=GAME_WIDTH-BALL_DIAMETER){
            score.player1++;
            newPaddles();
            newBall();
            System.out.println("Player 1: "+score.player1);
        }
    }

    @Override
    public void run(){
        // ************* Game loop **************
        long lastTime = System.nanoTime();
        double amountOfTicks = 75.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while (true){
            long now = System.nanoTime();
            delta += (now - lastTime)/ns;
            lastTime = now;

            if (delta>=1){
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }

    class AL extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }
}
