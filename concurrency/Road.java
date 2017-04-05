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
public class Road  {
    
    synchronized void martinEnter () throws InterruptedException {}
    synchronized void martinExit () throws InterruptedException {}
    synchronized void enemyEnter () throws InterruptedException {}
    synchronized void enemyExit () throws InterruptedException {}
    
}
