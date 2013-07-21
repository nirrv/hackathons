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
import it.rainbowbreeze.technogym.realtime.comm.RoomState;
import it.rainbowbreeze.technogym.realtime.comm.model.GymActivityData;
import it.rainbowbreeze.technogym.realtime.comm.model.RoomStateData;

/**
 * @author alfredomorresi
 *
 */
public class RoomStateManager {
    private static final String LOG_HASH = RoomStateManager.class.getSimpleName();

    private final IRainbowLogFacility mLogFacility;
    private final RoomState mRoomState;
    
    public RoomStateManager(IRainbowLogFacility logFacility, HttpTransport transport, JsonFactory jsonFactory) {
        mLogFacility = logFacility;
        mRoomState = new RoomState(transport, jsonFactory, null);
    }
    
    public RoomStateData getRoomState (Context context, long roomId) {
        try {
            return mRoomState.getRoomState(roomId).execute();
        } catch (Exception e) {
            mLogFacility.e(LOG_HASH, e);
        }
        return null;
    }
}
