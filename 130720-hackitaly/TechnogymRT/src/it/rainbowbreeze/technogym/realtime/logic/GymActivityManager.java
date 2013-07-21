/**
 * 
 */
package it.rainbowbreeze.technogym.realtime.logic;

import it.rainbowbreeze.libs.common.IRainbowLogFacility;
import it.rainbowbreeze.technogym.realtime.comm.GymActivity;
import it.rainbowbreeze.technogym.realtime.comm.model.GymActivityData;
import android.content.Context;
import android.content.Intent;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.technogym.android.myvisio.api.Equipment;
import com.technogym.android.myvisio.api.Training;

/**
 * @author alfredomorresi
 *
 */
public class GymActivityManager {
    private static final String LOG_HASH = GymActivityManager.class.getSimpleName();

    /** Minimum delta speed */
    private static final double SPEED_DELTA = 0.1;
    
    private final IRainbowLogFacility mLogFacility;
    private final GymActivity mGymActivity;
    
    public GymActivityManager(IRainbowLogFacility logFacility, HttpTransport transport, JsonFactory jsonFactory) {
        mLogFacility = logFacility;
        mGymActivity = new GymActivity(transport, jsonFactory, null);
    }
    
    public void turnLeft(Context context) {
        mLogFacility.v(LOG_HASH, "Turning left");
//        GymActivityData data = gatherData(context);
//        data.turnLeft();
//        uploadDataToServer(data);
    }
    
    public void turnRight(Context context) {
        mLogFacility.v(LOG_HASH, "Turning right");
//        GymActivityData data = gatherData(context);
//        data.turnRight();
//        uploadDataToServer(data);
    }
    
    public boolean increaseSpeed(Context context) {
        return changeSpeed(context, SPEED_DELTA);
    }

    public boolean decreaseSpeed(Context context) {
        return changeSpeed(context, -SPEED_DELTA);
    }

    public void startWorkout(Context context) {
        mLogFacility.e(LOG_HASH, "Starting workout");
//        Intent intent = new Intent("com.technogym.android.visiowow.training.easystart.action.RUN_FROM_DASHBOARD");
        Intent intent = new Intent("com.technogym.android.visiowow.training.easystart.action.RUN_FROM_READY_TO_USE");
        context.startService(intent);
    }    
    
    private boolean changeSpeed(Context context, double deltaChange) {
        Equipment eq = Equipment.getInstance(context);
        //read current speed
        double currentSpeed = eq.getDouble(Equipment.SPEED);
        double minSpeed = eq.getDouble(Equipment.SPEED_MIN);
        double maxSpeed = eq.getDouble(Equipment.SPEED_MAX);
        
        //speed decrease
        if (deltaChange < 0) {
            if ((currentSpeed + deltaChange) < minSpeed) {
                return false;
            }
        }
        //speed increase
        if (deltaChange > 0) {
            if ((currentSpeed + deltaChange) > maxSpeed) {
                return false;
            }
        }
        
        //changes speed
        eq.set(Equipment.SPEED, (currentSpeed + deltaChange));
        
        //sends new user data
//        GymActivityData data = gatherData(context);
//        uploadDataToServer(data);
        return true;
    }
    
    public double getSpeed(Context context) {
        return Equipment.getInstance(context).getDouble(Equipment.SPEED);
    }
    
    /**
     * Gathers Technogym data and upload it to server
     * @param data data to send to server
     */
    private void uploadDataToServer(GymActivityData data) {
        try {
            mGymActivity.update(data).execute();
        } catch (Exception e) {
            mLogFacility.e(LOG_HASH, e);
        }
    }
    
    
    /**
     * Gathers user data
     * 
     * @return data gathers from the equipment
     */
    private GymActivityData gatherData(Context context) {
        //get current training seconds
        String timeSec = Training.getInstance(context).getString(Training.TIME);
        mLogFacility.v(LOG_HASH, "timesec: " + timeSec);
        long sec = -1;
        try {
            sec = Long.getLong(timeSec);
        } catch (Exception e) {
            mLogFacility.e(e);
        }
        
        int distance = Training.getInstance(context).getInt(Training.DISTANCE); //meters?
        mLogFacility.v(LOG_HASH, "distance: " + distance);
        
        //prepared the package
        GymActivityData data = new GymActivityData()
                .setTimestamp(sec)
                .setDistance(distance);
        
        return data;
    }
}
