/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrency;

import java.awt.*;
import java.applet.*;

/**
 *
 * @author ThinkPad
 */
public class DisplayCanvas extends Canvas {
    
    WhackAMole controller;
    
    Image martin;
    Image enemyCar;
    Image road;
    int martinX, martinY;
    int[] enemyX, enemyY;
    
    int maxCar = 2;
    int maxMartin = 2;
    
    boolean frozen = false;
    final static int initMartinX = 220;
    final static int initMartinY = 5;
    final static int initEnemyX = 5;
    final static int initEnemyY = 90;
    final static int roadY = 90;
    int cycleTime = 20;
    
    DisplayCanvas(WhackAMole controller)
    {
        super();
        this.controller = controller;
        
        MediaTracker mt;
        mt = new MediaTracker(this);
        
        martin = controller.getImage(controller.getDocumentBase(), "image/martin.gif");
        enemyCar = controller.getImage(controller.getDocumentBase(), "image/redcar.gif");
        road = controller.getImage(controller.getDocumentBase(), "image/road.gif");
       
        mt.addImage(martin, 0);
        mt.addImage(enemyCar, 1);
        mt.addImage(road, 2);
        
        try {
            mt.waitForID(0);
            mt.waitForID(1);
            mt.waitForID(2);
           
        } catch (java.lang.InterruptedException e)
        {
            System.out.println("Cant load img");
        }
        
        setSize(road.getWidth(null), road.getHeight(null));
        
        init(1);
    }
    public void init(int cars) { //set number of cars
        maxCar = cars;
        frozen = false;
		//Each instance of new enemy car will have its own x and y coords whereas martin a single thread will have his own
        enemyX = new int[maxCar];
        enemyY = new int[maxCar];
		   martinX = initMartinX;
           martinY = initMartinY;
		   //For each new enemy car their initial X position is further behind the enemy car thread before them.
        for (int i = 0; i<maxCar ; i++) {
            enemyX[i] =initEnemyX - i*85;
            enemyY[i] =initEnemyY;
        }
        repaint();
    }

    Image offscreen;
    Dimension offscreensize;
    Graphics offgraphics;

    public void backdrop() {
        Dimension d = size();
	    if ((offscreen == null) || (d.width != offscreensize.width)
	                            || (d.height != offscreensize.height)) {
	        offscreen = createImage(d.width, d.height);
	        offscreensize = d;
	        offgraphics = offscreen.getGraphics();
	        offgraphics.setFont(new Font("Helvetica",Font.BOLD,36));
	    }
        offgraphics.setColor(Color.lightGray);
        offgraphics.drawImage(road,0,0,this);
    }

    @Override
    public void paint(Graphics g) {
         update(g);
    }

    @Override
    public void update(Graphics g) {
        backdrop();
        for (int i=0; i<maxCar; i++) {
            offgraphics.drawImage(martin,martinX,martinY,this);
            offgraphics.drawImage(enemyCar,enemyX[i],enemyY[i],this);
        
		//Checks for collisions between martin the enemy cars. This is not theortically possible however due to the constraints of our FairRoad class. If martin is on the road(when his Y is the same as the road he has begun moving on the road.) and the enemies are also beyond his house then they crash.
        if (martinY ==roadY && (enemyX[i]>220 && enemyX[i]<390)&&(martinX > 220 && martinX < 390) ){
            offgraphics.setColor(Color.red);
            offgraphics.drawString("Crunch!",200,100);
            frozen=true;
        }}
        g.drawImage(offscreen, 0, 0, null);
    }

    //returns true for the period from just before until just after car on bridge
    public  boolean moveMartin(int i) throws InterruptedException{
        int X = martinX;
        int Y = martinY;
        synchronized (this) {
            while (frozen ) wait();
            if (i==0) {
                if (X >=500) { X = initMartinX; Y = -10;}
                if (X <=initMartinX && X  > 100 && Y<roadY) ++Y;
                if (X >=initMartinX && Y==roadY) X+=2;
            }
            martinX=X;
            martinY=Y;
            
            repaint();
        }
        try{Thread.sleep(cycleTime);} 
        catch(InterruptedException e) {}  
          return (Y>40 && X<400);
     }

    //returns true for the period from just before until just after car on bridge
    public  boolean moveEnemy(int i) throws InterruptedException{
        int X = enemyX[i];
        int Y = enemyY[i];
        synchronized (this) {
            while (frozen ) wait();
            if (i==0 || Math.abs(enemyX[i-1] - X) > 120) {
                X += 2;
                if (X >=500) { X = -80; Y = initEnemyY;}
                }
			enemyX[i]=X;
            enemyY[i]=Y;
            repaint();
        }
        try{Thread.sleep(cycleTime);} catch(InterruptedException e) {}
        repaint();
        return (X>25 && X<400);
    }


    public synchronized void freeze(){
        frozen = true;
    }

    public synchronized void thaw() {
        frozen = false;
        notifyAll();
    }

}

