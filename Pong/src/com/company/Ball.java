package com.company;

import java.awt.*;
import java.util.Random;

public class Ball extends Rectangle{
    Random random;
    int xVelocity;
    int yVelocity;
    int initialSpeed = 2;

    Ball(int x, int y, int width, int height){
        super(x,y,width,height);

        random = new Random();
        int xDirection = random.nextInt(2);
        if(xDirection == 0) // Will make ball go left if it's 0
            xDirection--;
        setXDirection(xDirection * initialSpeed);

        int yDirection = random.nextInt(2);
        if(yDirection == 0)
            yDirection--;
        setYDirection(yDirection * initialSpeed);

    }

    void setXDirection(int randomXDirection){
        xVelocity = randomXDirection;
    }

    void setYDirection(int randomYDirection){
        yVelocity = randomYDirection;
    }

    public void move(){
        x+= xVelocity;
        y+= yVelocity;
    }

    public void draw(Graphics g){
        g.setColor(Color.white);
        g.fillOval(x,y,width,height);
    }

}
