package com.technogym.android.myvisio.api;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * The Equipment content provider that allows you to get and set informations about the equipment the user is using.
 */
public class Equipment extends ContentProviderProxy
{
	public final static String	PATH_UPGRADE_LOWKIT_FROM_USB		= "/mnt/sdcard/technogym/tmp/lowkit.bin";
	public final static String	PATH_UPGRADE_BRAKETABLE_FROM_USB	= "/mnt/sdcard/technogym/tmp/braketable.bin";

	public final static String	VERSION_LOWKIT						= "VERSION_LOWKIT";
	public final static String	LOWKIT_DEVICE						= "LOWKIT_DEVICE";
	public final static String	LOWKIT_REGISTER_ADDRESS				= "LOWKIT_REGISTER_ADDRESS";
	public final static String	LOWKIT_REGISTER_VALUE				= "LOWKIT_REGISTER_VALUE";
	public final static String	MAIN_VOLTAGE						= "MAIN_VOLTAGE";
	public final static String	USER_DETECT							= "USER_DETECT";
	// public final static String USER_UNDETECTED = "USER_UNDETECTED";
	public final static String	STANDARD_SETTING_PROGRESS			= "STANDARD_SETTING_PROGRESS";
	public final static String	UPGRADE								= "UPGRADE";
	public final static String	MANUAL_TEST_DATA					= "MANUAL_TEST_DATA";

	/**
	 * Percentage of inclination of the treadmill. To get the value, use a syntax like {@code eq.getDouble(Equipment.INCLINE)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	public final static String	INCLINE								= "EQUIPMENT_INCLINE";

	/**
	 * Speed of the treadmill, in Km/h. To get the value, use a syntax like {@code eq.getDouble(Equipment.SPEED)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	public final static String	SPEED								= "EQUIPMENT_SPEED";

	/**
	 * Indicates the time it takes to travel a kilometer, in seconds. To get the value, use a syntax like {@code eq.getDouble(Equipment.PACE)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	public final static String	PACE								= "EQUIPMENT_PACE";

	/**
	 * The status of the current training session. To get the value, use a syntax like {@code eq.getLong(Equipment.STATUS)}
	 * where <b><i>ep</i></b> is an instance of Equipment class.
	 * <p>The allowed types are:
	 * <pre> {@code 
	 * Equipment.UNKNOWN
	 * Equipment.READY
	 * Equipment.RUNNING
	 * Equipment.PAUSE
	 * Equipment.STOPPED
	 * Equipment.EMERGENCY
	 * Equipment.FAULT
	 * }</pre>
	 * </p>
	 */
	public final static String	STATUS								= "EQUIPMENT_STATUS";

	public final static String	STATUS_NOTIFICATION					= "EQUIPMENT_STATUS_NOTIFICATION";

	/**
	 * Indicates the code of the equipment. The code represents the singular equipment in its family and type. To get the value, use a syntax like {@code eq.getInt(Equipment.CODE)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	public final static String	CODE								= "EQUIPMENT_CODE";

	/**
	 * Indicates the generic code of the equipment. The generic code represents an equipment type into is family. To get the value, use a syntax like {@code eq.getInt(Equipment.GENERIC_CODE)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	public final static String	GENERIC_CODE						= "EQUIPMENT_GENERIC_CODE";

	/**
	 * Indicates the family code of the equipment. The family code represents a group of equipment types. To get the value, use a syntax like {@code eq.getInt(Equipment.FAMILY_CODE)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	public final static String	FAMILY_CODE							= "EQUIPMENT_FAMILY_CODE";
	// SS public final static String SPEED_BEFORE_PAUSE =
	// "EQUIPMENT_SPEED_BEFORE_PAUSE";

	/**
	 * Indicates minimum speed that the treadmill can reach, in Km/h. To get the value, use a syntax like {@code eq.getDouble(Equipment.SPEED_MIN)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	public final static String	SPEED_MIN							= "EQUIPMENT_SPEED_MIN";

	/**
	 * Indicates maximum speed that the treadmill can reach, in Km/h. To get the value, use a syntax like {@code eq.getDouble(Equipment.SPEED_MAX)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	public final static String	SPEED_MAX							= "EQUIPMENT_SPEED_MAX";

	/**
	 * Indicates minimum gradient percentage that the treadmill can reach. To get the value, use a syntax like {@code eq.getDouble(Equipment.INCLINE_MIN)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	public final static String	INCLINE_MIN							= "EQUIPMENT_INCLINE_MIN";

	/**
	 * Indicates maximum gradient percentage that the treadmill can reach. To get the value, use a syntax like {@code eq.getDouble(Equipment.INCLINE_MAX)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	public final static String	INCLINE_MAX							= "EQUIPMENT_INCLINE_MAX";

	/**
	 * Indicates the percentage of increase of the gradient. To get the value, use a syntax like {@code eq.getDouble(Equipment.INCLINE_INCREMENT)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	public final static String	INCLINE_INCREMENT					= "EQUIPMENT_INCLINE_INCREMENT";

	/**
	 * Indicates the watt value for the equipment. To get the value, use a syntax like {@code eq.getInt(Equipment.WATT)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	public final static String	WATT								= "EQUIPMENT_WATT";

	/**
	 * Indicates maximum watt value that the equipment can reach. To get the value, use a syntax like {@code eq.getInt(Equipment.WATT_MAX)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	public final static String	WATT_MAX							= "EQUIPMENT_WATT_MAX";

	/**
	 * Indicates minimum watt value that the equipment can reach. To get the value, use a syntax like {@code eq.getInt(Equipment.WATT_MIN)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	public final static String	WATT_MIN							= "EQUIPMENT_WATT_MIN";

	/**
	 * Indicates target watt value for the equipment. To get the value, use a syntax like {@code eq.getInt(Equipment.TARGET_WATT)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	public final static String	TARGET_WATT							= "EQUIPMENT_TARGET_WATT";

	/**
	 * Indicates the current effort level. To get the value, use a syntax like {@code eq.getInt(Equipment.EFFORT_LEVEL)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	public final static String	EFFORT_LEVEL						= "EQUIPMENT_EFFORT_LEVEL";

	/**
	 * Indicates minimum effort level. To get the value, use a syntax like {@code eq.getInt(Equipment.EFFORT_LEVEL_MIN)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	public final static String	EFFORT_LEVEL_MIN					= "EQUIPMENT_EFFORT_LEVEL_MIN";

	/**
	 * Indicates maximum effort level. To get the value, use a syntax like {@code eq.getInt(Equipment.EFFORT_LEVEL_MAX)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	public final static String	EFFORT_LEVEL_MAX					= "EQUIPMENT_EFFORT_LEVEL_MAX";

	/**
	 * Step per minutes. . To get the value, use a syntax like {@code eq.getInt(Equipment.SPM)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	public final static String	SPM									= "EQUIPMENT_SPM";

	/**
	 * Round per minutes. To get the value, use a syntax like {@code eq.getInt(Equipment.SPM)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	public final static String	RPM									= "EQUIPMENT_RPM";

	public final static String	RPM_MIN								= "EQUIPMENT_RPM_MIN";
	public final static String	RPM_MAX								= "EQUIPMENT_RPM_MAX";

	public final static String	TARGET_RPM							= "TARGET_RPM";

	/**
	 * Indicates if the dashboard is enabled or not. To get the value, use a syntax like {@code eq.getInt(Equipment.SPM)}
	 * where <b><i>eq</i></b> is an instance of Equipment class;
	 */
	//SS public final static String	DASHBOARD_ENABLED					= "DASHBOARD_ENABLED";

	// broadcast events
	/**
	 * Constant that indicates the action for the broadcast error log intent.
	 */
	public static final String	BROADCAST_ERROR_LOG_INTENT			= "com.technogym.android.equipment.action.ERROR_LOG";

	/**
	 * Constant that indicates the action for the broadcast operating data intent.
	 */
	public static final String	BROADCAST_OPERATING_DATA_INTENT		= "com.technogym.android.equipment.action.OPERATING_DATA";

	/**
	 * Constant that indicates the action for the broadcast parameter intent.
	 */
	public static final String	BROADCAST_PARAMETER_INTENT			= "com.technogym.android.equipment.action.PARAMETER";

	/**
	 * Constant that indicates the action for the broadcast up-down table point intent.
	 */
	public static final String	BROADCAST_UPDOWN_TABLE_POINT_INTENT	= "com.technogym.android.equipment.action.UPDOWN_TABLE_POINT";

	// valori dello stato
	/**
	 * The equipment status is "unknown". It is one of the values you can get when using the {@code eq.getLong(Equipment.STATUS)} command, where <b><i>eq</i></b> is an instance of Equipment class.
	 */
	public final static int		UNKNOWN								= 0;

	/**
	 * The equipment status is "ready". It is one of the values you can get when using the {@code eq.getLong(Equipment.STATUS)} command, where <b><i>eq</i></b> is an instance of Equipment class.
	 */
	public final static int		READY								= 1;

	/**
	 * The equipment status is "running". It is one of the values you can get when using the {@code eq.getLong(Equipment.STATUS)} command, where <b><i>eq</i></b> is an instance of Equipment class.
	 */
	public final static int		RUNNING								= 2;

	/**
	 * The equipment status is "pause". It is one of the values you can get when using the {@code eq.getLong(Equipment.STATUS)} command, where <b><i>eq</i></b> is an instance of Equipment class.
	 */
	public final static int		PAUSE								= 3;

	/**
	 * The equipment status is "stopped". It is one of the values you can get when using the {@code eq.getLong(Equipment.STATUS)} command, where <b><i>eq</i></b> is an instance of Equipment class.
	 */
	public final static int		STOPPED								= 4;

	/**
	 * The equipment status is "emergency". It is one of the values you can get when using the {@code eq.getLong(Equipment.STATUS)} command, where <b><i>eq</i></b> is an instance of Equipment class.
	 */
	public final static int		EMERGENCY							= 5;

	/**
	 * The equipment status is "fault". It is one of the values you can get when using the {@code eq.getLong(Equipment.STATUS)} command, where <b><i>eq</i></b> is an instance of Equipment class.
	 */
	public final static int		FAULT								= 6;

	// tipi di errore
	/**
	 * Indicates that no errors have occurred. Default value is 0.
	 */
	public final static int		FaultNone							= 0;

	/**
	 * Indicates that a generic error have occurred. Default value is 1.
	 */
	public final static int		FaultGeneric						= 1;

	/**
	 * Indicates that an error in incline system have occurred. Default value is 2.
	 */
	public final static int		FaultIncline						= 2;

	/**
	 * Indicates an error occurred due to an emergency. Default value is 3.
	 */
	public final static int		FaultEmergency						= 3;

	/**
	 * Indicates an error occurred in the common low kit. Default value is 4.
	 */
	public final static int		FaultCommLowKit						= 4;

	public final static String	READY_TO_USE_ENABLED				= "READY_TO_USE_ENABLED";
	
	public final static String	UPLOADING_IN_PROGRESS				= "UPLOADING_IN_PROGRESS";

	/**
	 * The Content Provider URI.
	 */
	public static final Uri		CONTENT_URI							= Uri.parse("content://com.technogym.android.wow.equipment.AUTHORITY/item");

	Context						context;

	private static Equipment	instance							= null;

	private Equipment(Context ctx)
	{
		super(ctx, CONTENT_URI);
		context = ctx;
	}

	/**
	 * Obtain reference of singleton.
	 */

	public static synchronized Equipment getInstance(Context ctx)
	{
		if (instance == null)
		{
			instance = new Equipment(ctx);
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
	 * Constant that indicates the action is for equipment.
	 */
	public final static String	INTENT_KEY		= "com.technogym.android.visiowow.action.EQUIPMENT_ACTION";

	/**
	 * Constant that indicates the key for events to use in intent.putExtra.
	 */
	private static final String	INTENT_TYPE_KEY	= "com.technogym.android.visiowow.INTENT_TYPE_KEY";

	/**
	 * Constant for the value of the INTENT_TYPE_KEY. Default value is -1.
	 */
	private static final int	NO_INTENT_TYPE	= -1;

	/**
	 * Return the intent type
	 * @param intent The intent from which to obtain the type.
	 * @return The Intent type
	 */
	public static int getItentType(Intent intent)
	{
		int intentType = intent.getIntExtra(INTENT_TYPE_KEY, NO_INTENT_TYPE);
		return intentType;
	}

	/**
	 * Constant that indicates the key for notify action.
	 */
	public final static String	NOTIFY_KEY				= "com.technogym.android.visiowow.equipment.NOTIFY_KEY";

	/**
	 * Constant that indicates the key for events to use in intent.putExtra.
	 */
	public final static String	EVENT_KEY				= "com.technogym.android.visiowow.equipment.EVENT_KEY";

	/**
	 * Constant to indicate an unknow event. The default value is -1.
	 */
	public final static int		EVENT_UNKNOWN			= -1;

	/**
	 * Constant to indicate the event is started. The default value is 1.
	 */
	public final static int		EVENT_STARTED			= 1;

	/**
	 * Constant to indicate the event is stopped. The default value is 2.
	 */
	public final static int		EVENT_STOPPED			= 2;

	/**
	 * Constant to indicate the event is restarted. The default value is 3.
	 */
	public final static int		EVENT_RESTARTED			= 3;

	/**
	 * Start a broadcast to notify an event.
	 * @param ctx The Android context
	 * @param event The event index to notify. <p>The allowed types are:
	 * <pre> {@code 
	 * Equipment.EVENT_UNKNOWN
	 * Equipment.EVENT_STARTED
	 * Equipment.EVENT_STOPPED
	 * Equipment.EVENT_RESTARTED
	 * }</pre>
	 * </p> 
	 */

	public final static String	GREEN_TRAINING_ENERGY	= "GREEN_TRAINING_ENERGY";
	public final static String	GREEN_EQUIPMENT_ENERGY	= "GREEN_EQUIPMENT_ENERGY";

	public static void notifyEvent(Context ctx, int event)
	{
		Intent intent = new Intent(NOTIFY_KEY);
		intent.putExtra(EVENT_KEY, event);
		ctx.sendBroadcast(intent);
	}
}
