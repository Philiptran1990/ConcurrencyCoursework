package concurrency;


class Martin implements Runnable {
    
    DisplayCanvas display;
    Road control;
    int id;
    
    Martin(Road b, DisplayCanvas c, int id)
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
            while(!display.moveMartin(id));
            control.martinEnter();
            while(display.moveMartin(id));
            control.martinExit();
        }
        } catch (InterruptedException e){}
    }
    
}
