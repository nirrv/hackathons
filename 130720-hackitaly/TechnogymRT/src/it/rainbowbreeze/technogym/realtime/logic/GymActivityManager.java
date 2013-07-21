/**
 * 
 */
package it.rainbowbreeze.technogym.realtime.logic;

import it.rainbowbreeze.libs.common.IRainbowLogFacility;

/**
 * Manager for managing gym activity tasks (i.e starting a run, increase speed etc)
 * 
 * @author alfredomorresi
 */
public class GymActivityManager {
    private final static String LOG_HASH = GymActivityManager.class.getSimpleName();
    
    private final IRainbowLogFacility mLogFacility;

    public GymActivityManager(IRainbowLogFacility logFacility) {
        mLogFacility = logFacility;
    }
    

    public void startWorkout() {
        mLogFacility.e(LOG_HASH, "Starting workout");
        //TODO
    }
}
