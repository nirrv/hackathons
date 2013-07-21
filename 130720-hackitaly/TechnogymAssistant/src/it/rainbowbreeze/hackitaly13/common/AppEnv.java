/**
 *
 */
package it.rainbowbreeze.hackitaly13.common;

import static it.rainbowbreeze.libs.common.RainbowContractHelper.checkNotNull;
import it.rainbowbreeze.hackitaly13.ui.ActivityHelper;
import it.rainbowbreeze.libs.common.RainbowServiceLocator;
import it.rainbowbreeze.libs.logic.RainbowCrashReporter;
import it.rainbowbreeze.technogym.realtime.logic.GymActivityManager;
import it.rainbowbreeze.technogym.realtime.logic.RoomStateManager;
import android.content.Context;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

/**
 * Main application classes lazy-loading singleton
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public class AppEnv {
    // ------------------------------------------ Private Fields
    private static final String LOG_HASH = "AppEnv";
    private static final Object mSyncObject = new Object();
    
    
    // -------------------------------------------- Constructors
    private AppEnv(Context context) {
        //use default objects factory
        this(context, getDefaultObjectsFactory());
    }
    
    private AppEnv(Context context, ObjectsFactory objectsFactory) {
        //use a custom object factory
        checkNotNull(objectsFactory, "ObjectsFactory");
        setupVolatileData(context, objectsFactory);
    }

    
    // --------------------------------------- Public Properties
    /** keys for application preferences */
    public final static String APP_PREFERENCES_KEY = "FaceDetector";

    /** lazy loading singleton */
    public static AppEnv i(Context context) {
        synchronized (mSyncObject) {
            if (null == mInstance)
                mInstance = new AppEnv(context);
        }
        return mInstance;
    }
    /** lazy loading singleton, reload the environment each time (for testing purposes) */
    public static AppEnv i(Context context, ObjectsFactory objectsFactory) {
        synchronized (mSyncObject) {
            if (null == mInstance) {
                mInstance = new AppEnv(context, objectsFactory);
            } else {
                //force the rebuild of all the environment
                mInstance.setupVolatileData(context, objectsFactory);
            }
        }
        return mInstance;
    }
    private static AppEnv mInstance;

    
    /** Application version displayed to the user (about activity etc) */
    public final static String APP_DISPLAY_VERSION = "1.0";
    
    /** Application name used during the ping of update site */
    public final static String APP_INTERNAL_NAME = "VoiceButler";

    /** Application version for internal use (update, crash report etc) */
    public final static String APP_INTERNAL_VERSION = "01.00";

    /** Application version for internal use (update, crash report etc) */
    public final static String EMAIL_FOR_ERROR_LOG = "alfredo.morresi@gmail.com";
    
    /** Tag to use in the log */
    public final static String LOG_TAG = "TechnogymAssistant";
    
    /** Not found item in content provider */
    public static final long NOT_FOUND = -1;

    /** Default objects factory, testing purposes */
    protected static final ObjectsFactory defaultObjectsFactory = new ObjectsFactory();
    public static final ObjectsFactory getDefaultObjectsFactory()
    { return defaultObjectsFactory; }

    
    // ------------------------------------------ Public Methods

    public LogFacility getLogFacility() {
        return checkNotNull(RainbowServiceLocator.get(
                LogFacility.class), LogFacility.class.getSimpleName());
    }
    
    public GymActivityManager getGymActivityManager() {
        return checkNotNull(RainbowServiceLocator.get(
                GymActivityManager.class), GymActivityManager.class.getSimpleName());
    }
    
    public RoomStateManager getRoomStateManager() {
        return checkNotNull(RainbowServiceLocator.get(
                RoomStateManager.class), RoomStateManager.class.getSimpleName());
    }
    
    public ActivityHelper getActivityHelper() {
        return checkNotNull(RainbowServiceLocator.get(
                ActivityHelper.class), ActivityHelper.class.getSimpleName());
	}
    
    // ----------------------------------------- Private Methods
    /**
     * Setup the volatile data of application.
     * This is needed because sometime the system release memory
     * without completely close the application, so all static fields
     * will become null :(
     * 
     */
    private void setupVolatileData(Context context, ObjectsFactory mObjectsFactory) {
        //set the log tag
        LogFacility logFacility = mObjectsFactory.createLogFacility(LOG_TAG);
        logFacility.i(LOG_HASH, "Initializing environment");

        //empty service locator
        RainbowServiceLocator.clear();
        //put log facility
        RainbowServiceLocator.put(logFacility);
        
        //initialize (and automatically register) crash reporter
        RainbowCrashReporter crashReport = mObjectsFactory.createCrashReporter(context);
        RainbowServiceLocator.put(crashReport);        
        //create services and helper respecting IoC dependencies
        ActivityHelper activityHelper = mObjectsFactory.createActivityHelper(context, logFacility);
        RainbowServiceLocator.put(activityHelper);
        
        final HttpTransport transport = AndroidHttp.newCompatibleTransport();
        final JsonFactory jsonFactory = new GsonFactory();
        GymActivityManager gymActivityManager = new GymActivityManager(logFacility, transport, jsonFactory);
        RainbowServiceLocator.put(gymActivityManager);
        RoomStateManager roomStateManager = new RoomStateManager(logFacility, transport, jsonFactory);
        RainbowServiceLocator.put(roomStateManager);
    }
    
    
    // ----------------------------------------- Private Classes
    public static class ObjectsFactory {
        public LogFacility createLogFacility(String logTag)
        { return new LogFacility(logTag); }
        
        public RainbowCrashReporter createCrashReporter(Context context)
        { return new RainbowCrashReporter(context); }
        
        public ActivityHelper createActivityHelper(Context context, LogFacility logFacility)
        { return new ActivityHelper(logFacility, context); }
    }

}
