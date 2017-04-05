
package concurrency;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.*;
import static java.time.Clock.system;

/**
 *
 * @author ThinkPad
 */
public class WhackAMole extends Applet{
    
	DisplayCanvas display;
    Button start;
    Button stop;
    Button moreCars;
    Button lessCars;
    Thread martin;
    Thread enemyCar[];
    int maxCar = 2;
    int maxMartin = 1;
    //Initial settings
    public void init()
    {
        setLayout(new BorderLayout());
        display = new DisplayCanvas(this);
        add("Center", display);
        start = new Button("Start");
        stop = new Button("Stop");
        moreCars = new Button("More Cars");
        lessCars = new Button("Less Cars");
        Panel p = new Panel();
        p.setLayout(new FlowLayout());
        p.add(stop);
        p.add(start);
        p.add(moreCars);
        p.add(lessCars);
        add(p, BorderLayout.SOUTH);
        
    }
    //start method which initialises the threads depending on how many enemy cars the user wants
    @Override
    public void start(){
        Road b;
        
        b = new FairRoad();
         
        martin = new Thread();
        enemyCar = new Thread[maxCar];
        display.init(maxCar);
        
        //increases the amount of enemy car threads depending on the number the user selects. There is only one martin thread.
        for (int i = 0; i < maxCar; i++)
        {
            enemyCar[i] = new Thread(new EnemyCar(b, display, i));
        }
        martin = new Thread(new Martin(b, display, 0));
        for(int i = 0; i<maxCar; i++) {
            
            enemyCar[i].start();
        }
        
            martin.start();
    }
    
    @Override
    public void stop()
    {
        for(int i = 0; i<maxCar; i++)
        {
            enemyCar[i].stop();
            martin.stop();
        }    
    }
//controls the buttons
    @Override
   public boolean handleEvent(Event event) {
        if (event.id != event.ACTION_EVENT) {
            return super.handleEvent(event);
        } else if(event.target==start) {
            display.thaw();
            return true;
        } else if (event.target == stop) {
            display.freeze();
            return true;
        } else if (event.target == moreCars) {
            stop();
            maxCar++;
            start();
            return true;
         } else if (event.target == lessCars) {
            stop();
            if(maxCar > 1) {
                maxCar--;
            }
            start();
            return true;
         } else
            return super.handleEvent(event);
    }
}
