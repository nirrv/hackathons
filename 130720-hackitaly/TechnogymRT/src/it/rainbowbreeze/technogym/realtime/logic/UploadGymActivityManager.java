/**
 * 
 */
package it.rainbowbreeze.technogym.realtime.logic;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.technogym.android.myvisio.api.Equipment;
import com.technogym.android.myvisio.api.Training;

import android.content.Context;

import it.rainbowbreeze.libs.common.IRainbowLogFacility;
import it.rainbowbreeze.technogym.realtime.comm.GymActivity;
import it.rainbowbreeze.technogym.realtime.comm.model.GymActivityData;

/**
 * @author alfredomorresi
 *
 */
public class UploadGymActivityManager {
    private static final String LOG_HASH = UploadGymActivityManager.class.getSimpleName();

    /** Minimum delta speed */
    private static final double SPEED_DELTA = 0.1;
    
    private final IRainbowLogFacility mLogFacility;
    private final GymActivity mGymActivity;
    
    public UploadGymActivityManager(IRainbowLogFacility logFacility, HttpTransport transport, JsonFactory jsonFactory) {
        mLogFacility = logFacility;
        mGymActivity = new GymActivity(transport, jsonFactory, null);
    }
    
    public void startFromApplication(Context context) {
        mLogFacility.v(LOG_HASH, "Starting scheduled service for uploading data from the application");
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(new Runnable() {
                    public void run() {
                        //TODO read data from the server
                        //mGymActivity.getRoomState(roomId);
                    }
                }, 0, 5, TimeUnit.SECONDS);
    }

    public void turnLeft(Context context) {
        //sends new user data
        GymActivityData data = gatherData(context);
        data.turnLeft();
        uploadDataToServer(data);
    }
    
    public void turnRight(Context context) {
        //sends new user data
        GymActivityData data = gatherData(context);
        data.turnRight();
        uploadDataToServer(data);
    }
    
    public boolean increaseSpeed(Context context) {
        return changeSpeed(context, SPEED_DELTA);
    }

    public boolean decreaseSpeed(Context context) {
        return changeSpeed(context, -SPEED_DELTA);
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
        GymActivityData data = gatherData(context);
        uploadDataToServer(data);
        return true;
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
        long sec = -1;
        try {
            sec = Long.getLong(timeSec);
        } catch (Exception e) {
            mLogFacility.e(e);
        }
        
        if (-1 == sec) {
            //TODO return
        }
        
        int distance = Equipment.getInstance(context).getInt(Training.DISTANCE); //meters?
        
        //prepared the package
        GymActivityData data = new GymActivityData()
                .setTimestamp(sec)
                .setDistance(distance);
        
        return data;
    }
}
