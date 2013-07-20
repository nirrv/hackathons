package com.technogym.android.myvisio.api;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * The Training content provider that allows you to get and set informations about the current training session.
 */
public class Training extends ContentProviderProxy
{
	public final static String	GREEN_SELECTED_TARGET			= "TRAINING_SELECTED_GREEN_TARGET";
	public final static String	GREEN_FACILITY_OFFSET			= "TRAINING_SELECTED_GREEN_FACILITY_OFFSET";
	public final static String	GREEN_FACILITY_INCREMENT		= "TRAINING_SELECTED_GREEN_FACILITY_INCREMENT";

	/**
	 * The number of calories burned in the current training session. To get the value, use a syntax like {@code tr.getInt(Training.CALORIES)}
	 * where <b><i>tr</i></b> is an instance of Training class;
	 */
	public final static String	CALORIES						= "TRAINING_CALORIES";

	public final static String	CHRONO							= "TRAINING_CHRONO";

	/**
	 * The distance traveled in the current training session.  To get the value, use a syntax like {@code tr.getInt(Training.DISTANCE)}
	 * where <b><i>tr</i></b> is an instance of Training class;
	 */
	public final static String	DISTANCE						= "TRAINING_DISTANCE";

	/**
	 * The actual heart rate of user in the current training session. To get the value, use a syntax like {@code tr.getLong(Training.HEARTRATE)}
	 * where <b><i>tr</i></b> is an instance of Training class.
	 * the value is -1 if the value is not available.
	 */
	public final static String	HEARTRATE						= "TRAINING_HEARTRATE";

	/**
	 * The heart rate channel in the current training session. To get the value, use a syntax like {@code tr.getLong(Training.HEARTRATE_CHANNEL)}
	 * where <b><i>tr</i></b> is an instance of Training class.
	 * <p>The allowed types are
	 * <pre> {@code 
	 * Training.HAND
	 * Training.BAND
	 * }</pre></p>
	 */
	public final static String	HEARTRATE_CHANNEL				= "TRAINING_HEARTRATE_CHANNEL";

	/**
	 * Measures the amount of energy a person uses per minute. To get the value, use a syntax like {@code tr.getInt(Training.METS)}
	 * where <b><i>tr</i></b> is an instance of Training class.
	 */
	public final static String	METS							= "TRAINING_METS";

	/**
	 * The maximum value that heart rate can assume. To get the value, use a syntax like {@code tr.getLong(Training.HEARTRATE_MAX)}
	 * where <b><i>tr</i></b> is an instance of Training class.
	 */
	public final static String	HEARTRATE_MAX					= "TRAINING_HEARTRATE_MAX";

	/**
	 * The heart rate average in the current training session. To get the value, use a syntax like {@code tr.getInt(Training.HEARTRATE_AVG)}
	 * where <b><i>tr</i></b> is an instance of Training class.
	 */
	public final static String	HEARTRATE_AVG					= "TRAINING_HEARTRATE_AVG";

	public static final int		HAND							= 2;

	/**
	 * Indicates you are using a chest band as heart rate channel to get the heart rate. 
	 */
	public static final int		BAND							= 1;

	// SS public final static String PAUSE_BEGIN_TIME = "PAUSE_BEGIN_TIME";

	/**
	 * The elapsed time of the current training session, in seconds. To get the value, use a syntax like {@code tr.getString(Training.TIME)}
	 * where <b><i>tr</i></b> is an instance of Training class.
	 */
	public final static String	TIME							= "TRAINING_TIME";

	/**
	 * The status of the current training session. To get the value, use a syntax like {@code tr.getLong(Training.STATUS)}
	 * where <b><i>tr</i></b> is an instance of Training class.
	 * <p>The allowed types are:
	 * <pre> {@code 
	 * Training.ST_UNKNOWN
	 * Training.ST_RUNNING
	 * Training.ST_PAUSE
	 * Training.ST_WARMUP
	 * Training.ST_COOLDOWN
	 * Training.ST_STOPPED
	 * Training.ST_WAIT_DATA
	 * Training.ST_TERMINATE
	 * Training.ST_INTERRUPTED
	 * Training.ST_WAITING_HR_DETECTED
	 * }</pre>
	 * </p>
	 */
	public final static String	STATUS							= "TRAINING_STATUS";

	public final static String	STATUS_NOTIFICATION				= "TRAINING_STATUS_NOTIFICATION";

	/**
	 * The target time for the current training session, in seconds. To get the value, use a syntax like {@code tr.getLong(Training.TARGET_TIME)}
	 * where <b><i>tr</i></b> is an instance of Training class.
	 * If the value is not available it returns -1.
	 */
	public final static String	TARGET_TIME						= "TRAINING_TARGET_TIME";

	/**
	 * The target distance for the current training session, in meters. To get the value, use a syntax like {@code tr.getLong(Training.TARGET_DISTANCE)}
	 * where <b><i>tr</i></b> is an instance of Training class.
	 * If the value is not available it returns -1.
	 */
	public final static String	TARGET_DISTANCE					= "TRAINING_TARGET_DISTANCE";

	/**
	 * The target calories for the current training session, in kcal. To get the value, use a syntax like {@code tr.getLong(Training.TARGET_CALORIES)}
	 * where <b><i>tr</i></b> is an instance of Training class.
	 * If the value is not available it returns -1.
	 */
	public final static String	TARGET_CALORIES					= "TRAINING_TARGET_CALORIES";

	/**
	 * The target heart rate for the current training session. To get the value, use a syntax like {@code tr.getLong(Training.TARGET_HEARTRATE)}
	 * where <b><i>tr</i></b> is an instance of Training class.
	 * If the value is not available it returns -1.
	 */
	public final static String	TARGET_HEARTRATE				= "TRAINING_TARGET_HEARTRATE";

	/**
	 * A string representing the AppInfo object. To get the value, use a syntax like {@code tr.getLong(Training.INFO_VIEW_DESCRIPTOR)}
	 * where <b><i>tr</i></b> is an instance of Training class.
	 */
	public final static String	INFO_VIEW_DESCRIPTOR			= "INFO_VIEW_DESCRIPTOR";

	public final static String	PROPERTY_SET					= "PROPERTY_SET";

	/**
	 * Display mode of the training. To get the value, use a syntax like {@code tr.getString(Training.TRAINING_VIEW_MODE)}
	 * where <b><i>tr</i></b> is an instance of Training class.
	 * <p>
	 * The values that you can use are: 
	 * <pre> {@code 
	 * Training.VIEW_FULL
	 * Training.VIEW_MINIMAL
	 * }</pre>
	 * </p>
	 */
	public final static String	TRAINING_VIEW_MODE				= "TRAINING_VIEW_MODE";

	/**
	 * Indicates whether the current training session is modified or not (as stopped). To get the value, use a syntax like {@code tr.getBoolean(Training.EXERCISE_MODIFY)}
	 * where <b><i>tr</i></b> is an instance of Training class.
	 */
	public final static String	EXERCISE_MODIFY					= "EXERCISE_MODIFY";

	/**
	 * Indicates whether the current training session is recorded or is to record. To get the value, use a syntax like {@code tr.getBoolean(Training.RECORDING_MODE)}
	 * where <b><i>tr</i></b> is an instance of Training class.
	 */
	public final static String	RECORDING_MODE					= "RECORDING_MODE";

	public final static String	CONTROL_FROM_SCAFE				= "CONTROL_FROM_SCAFE";

	public final static String	PROFILE_SET						= "PROFILE_SET";

	/**
	 * Indicates the level of the current training. To get the value, use a syntax like {@code tr.getLong(Training.LEVEL)}
	 * where <b><i>tr</i></b> is an instance of Training class.
	 */
	public final static String	LEVEL							= "TRAINING_LEVEL";

	/**
	 * Some of the training data (e.g. gradient, speed) are visible in the status bar.
	 */
	public final static int		VIEW_MINIMAL					= 1;

	/**
	 * 
	 */
	public final static int		VIEW_FULL						= 2;

	// valori dello stato
	/**
	 * The training status is "unknown". It is one of the values you can get when using the {@code tr.getLong(Training.STATUS)} command, where <b><i>tr</i></b> is an instance of Training class.
	 */
	public final static int		ST_UNKNOWN						= 0;

	/**
	 * The training status is "running": in this state the training is in progress. It is one of the values you can get when using the {@code tr.getLong(Training.STATUS)} command, where <b><i>tr</i></b> is an instance of Training class.
	 */
	public final static int		ST_RUNNING						= 1;
	// SS public final static int STATUS_READY = 2;

	/**
	 * The training status is "pause": in this state the training is paused and can be resumed at any time. 
	 * . It is one of the values you can get when using the {@code tr.getLong(Training.STATUS)} command, where <b><i>tr</i></b> is an instance of Training class.
	 */
	public final static int		ST_PAUSE						= 3;

	/**
	 * The training status is "warm up". It is one of the values you can get when using the {@code tr.getLong(Training.STATUS)} command, where <b><i>tr</i></b> is an instance of Training class.
	 */
	public final static int		ST_WARMUP						= 4;

	/**
	 * The training status is "cool down". In this state speed and inclination are decreased gradually to terminate the training.
	 * It is one of the values you can get when using the {@code tr.getLong(Training.STATUS)} command, where <b><i>tr</i></b> is an instance of Training class.
	 */
	public final static int		ST_COOLDOWN						= 5;

	/**
	 * The training status is "stopped". The user stopped the training before the specified period.
	 * It is one of the values you can get when using the {@code tr.getLong(Training.STATUS)} command, where <b><i>tr</i></b> is an instance of Training class.
	 */
	public final static int		ST_STOPPED						= 6;

	/**
	 * The training status is "wait data". The system is waiting for data.
	 * It is one of the values you can get when using the {@code tr.getLong(Training.STATUS)} command, where <b><i>tr</i></b> is an instance of Training class.
	 */
	public final static int		ST_WAIT_DATA					= 7;

	/**
	 * The training status is "terminate". The user has completed the training.
	 * It is one of the values you can get when using the {@code tr.getLong(Training.STATUS)} command, where <b><i>tr</i></b> is an instance of Training class.
	 */
	public final static int		ST_TERMINATE					= 8;

	/**
	 * The training status is "interrupted". This status is caused by emergency or fault.
	 * It is one of the values you can get when using the {@code tr.getLong(Training.STATUS)} command, where <b><i>tr</i></b> is an instance of Training class.
	 */
	public final static int		ST_INTERRUPTED					= 9;																			// caused by emergency or fault

	/**
	 * The training status is "waiting for heart rate detection".
	 * It is one of the values you can get when using the {@code tr.getLong(Training.STATUS)} command, where <b><i>tr</i></b> is an instance of Training class.
	 */
	public final static int		ST_WAITING_HR_DETECTED			= 10;


	//SS public final static int		ST_READY						= 11;

	
	/**
	 * In this status, the heart rate is not detected.
	 * It is one of the values you can get when using the {@code tr.getLong(Training.STATUS)} command, where <b><i>tr</i></b> is an instance of Training class.
	 */
	public final static int		HR_NOT_DETECTED					= -1;

	/**
	 * The Content Provider URI.
	 */
	public static final Uri		CONTENT_URI						= Uri.parse("content://com.technogym.android.wow.training.AUTHORITY/item");

	/**
	 * String command to execute when pause() method is called.
	 */
	public final static String	PauseAction						= "com.technogym.android.visiowow.training.action.pause";

	/**
	 * String command to execute when restart() method is called.
	 */
	public final static String	RestartAction					= "com.technogym.android.visiowow.training.action.restart";

	/**
	 * String command to execute when cooldown() method is called.
	 */
	public final static String	CooldownAction					= "com.technogym.android.visiowow.training.action.cooldown";

	/**
	 * String command to execute when stop() method is called.
	 */
	public final static String	StopAction						= "com.technogym.android.visiowow.training.action.stop";

	/**
	 * String command to execute when abort() method is called.
	 */
	public final static String	AbortAction						= "com.technogym.android.visiowow.training.action.abort";

	/**
	 * String command to execute when cooldown() method is called.
	 */
	public final static String	TerminateWithoutCooldownAction	= "com.technogym.android.visiowow.training.action.terminatewithoutcooldown";

	private Context				context;

	private static Training		instance						= null;

	private Training(Context ctx)
	{
		super(ctx, CONTENT_URI);
		context = ctx;
	}

	/**
	 * Obtain reference of singleton.
	 */

	public static synchronized Training getInstance(Context ctx)
	{
		if (instance == null)
		{
			instance = new Training(ctx);
		}
		return instance;
	}

	/**
	 * Fake tear down does nothing because this Content Provider Proxy is a singleton.
	 */
	@Override
	public void tearDown()
	{

	}

	/**
	 * Start an intent to execute the pause action.
	 */
	public void pause()
	{
		Intent intent = new Intent(PauseAction);
		context.sendBroadcast(intent);
	}

	/**
	 * Start an intent to execute the restart action.
	 */
	public void restart()
	{
		Intent intent = new Intent(RestartAction);
		context.sendBroadcast(intent);
	}

	/**
	 * Start an intent to execute the cooldown action.
	 */
	public void cooldown()
	{
		// SS set(Training.EXERCISE_MODIFY,true);
		Intent intent = new Intent(CooldownAction);
		context.sendBroadcast(intent);
	}

	public void terminateWithoutCooldown()
	{
		// SS set(Training.EXERCISE_MODIFY,true);
		Intent intent = new Intent(TerminateWithoutCooldownAction);
		context.sendBroadcast(intent);
	}

	/**
	 * Start an intent to execute the stop action.
	 */
	public void stop()
	{
		// SS set(Training.EXERCISE_MODIFY,true);
		Intent intent = new Intent(StopAction);
		context.sendBroadcast(intent);
	}

	/**
	 * Start an intent to execute the abort action.
	 */
	public void abort()
	{
		Intent intent = new Intent(AbortAction);
		context.sendBroadcast(intent);
	}
}
