/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrency;

/**
 *
 * @author ThinkPad
 */
public class FairRoad extends Road {
    //Same settings as in the LTS
    private int nrMartin = 0;
    private int nrEnemy = 0;
    private int waitMartin = 0;
    private int waitEnemy = 0;
    private boolean martinTurn = true;
    //martin may request entry. If the road has enemies or it is not his turn with enemies waiting then he can not enter.
    @Override
    synchronized void martinEnter() throws InterruptedException {
        ++waitMartin;
        while (nrEnemy >0 || (waitEnemy>0 && !martinTurn)) wait();
        --waitMartin;
        ++nrMartin;

    }
    //when martin leaves the road he notifies the enemies the road is available and changes the road priority
    @Override
    synchronized void martinExit(){
        --nrMartin;
        martinTurn = false;
        if (nrMartin==0)
            notifyAll();

    }
//Enemies may request entry. If martin is on the road or it is martin's turn and he is waiting then enemies can not enter.
    
    @Override
    synchronized void enemyEnter()  throws InterruptedException {
        ++waitEnemy;
        while (nrMartin>0 || (waitMartin>0 && martinTurn)) wait();
        --waitEnemy;
        ++nrEnemy;

    }
//enemies exit and change the priority of the road and notify martin that the road is available.
    @Override
    synchronized void enemyExit(){
        --nrEnemy;
        martinTurn = true;
        if (nrEnemy==0)
            notifyAll();

    }
    
    
}
