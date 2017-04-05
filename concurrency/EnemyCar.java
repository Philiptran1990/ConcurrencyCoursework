/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrency;


class EnemyCar implements Runnable {
    
    DisplayCanvas display;
    Road control;
    int id;
    
    EnemyCar(Road b, DisplayCanvas c, int id)
    {
        display = c;
        this.id = id;
        control = b;
    }
    
    @SuppressWarnings("empty-statement")
    public void run()
    {
        try{
        while(true)
        {
            while(!display.moveEnemy(id));
            control.enemyEnter();
            while(display.moveEnemy(id));
            control.enemyExit();
        }
        } catch (InterruptedException e){}
    }
    
}
