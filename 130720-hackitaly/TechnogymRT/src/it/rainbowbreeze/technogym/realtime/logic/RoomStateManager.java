/**
 * 
 */
package it.rainbowbreeze.technogym.realtime.logic;

import it.rainbowbreeze.libs.common.IRainbowLogFacility;
import it.rainbowbreeze.technogym.realtime.comm.RoomState;
import it.rainbowbreeze.technogym.realtime.comm.model.RoomStateData;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

/**
 * @author alfredomorresi
 *
 */
public class RoomStateManager {
    private static final String LOG_HASH = RoomStateManager.class.getSimpleName();

    private final IRainbowLogFacility mLogFacility;
    private final RoomState mRoomState;
    private final long mRoomId;
    
    public RoomStateManager(
            IRainbowLogFacility logFacility,
            HttpTransport transport,
            JsonFactory jsonFactory,
            long roomId) {
        mLogFacility = logFacility;
        mRoomState = new RoomState(transport, jsonFactory, null);
        mRoomId = roomId;
    }
    
    public void startFromApplication(Context context) {
        mLogFacility.v(LOG_HASH, "Starting scheduled service for uploading data from the application");
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(new Runnable() {
                    public void run() {
                        //TODO read data from the server
                        //getRoomState(mRoomId);
                    }
                }, 0, 5, TimeUnit.SECONDS);
    }

    public RoomStateData getRoomState (Context context) {
        try {
            return mRoomState.getRoomState(mRoomId).execute();
        } catch (Exception e) {
            mLogFacility.e(LOG_HASH, e);
        }
        return null;
    }
}
