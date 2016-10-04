/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.ui;

import java.util.concurrent.Semaphore;

/**
 * This class is used to synchronise the Parser and the MainWindow when stepping
 * through instructions
 * @author martinhart
 */
public class StepLock {
    
    private boolean released;
    private final Semaphore semaphore;
    
    public StepLock() {
        released = false;
        this.semaphore = new Semaphore(1, true);
    }
    
    /**
     * This function returns true if the parser is allowed to continue
     * @return 
     */
    public boolean canContinue() {
        boolean result = false;
        try {
            semaphore.acquire(1);
            result = released;
            released = false;
        }
        catch(Exception e) {
            e.printStackTrace();
            result = false;
        }
        finally {
            semaphore.release(1);
        }
        return result;
    }
    
    /**
     * This function causes the parser to be allowed to continue. 
     */
    public void allowContinue() {
        try {
            semaphore.acquire(1);
            released = true;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            semaphore.release(1);
        }
    }
}
