package com.technogym.android.myvisio.api;

import android.content.Context;
import android.net.Uri;


/**
 * The User content provider that allows you to get and set informations about the user.
 */
public class User extends ContentProviderProxy
{
	//Field CP
	/**
	 * The name of the logged user. To get the value, use a syntax like {@code userCp.getString(User.NAME)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	NAME					= "USER_NAME";
	
	/**
	 * The surname of the logged user. To get the value, use a syntax like {@code userCp.getString(User.SURNAME)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	SURNAME					= "USER_SURNAME";
	
	/**
	 * The nickname of the logged user. To get the value, use a syntax like {@code userCp.getString(User.NICKNAME)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	NICKNAME				= "USER_NICKNAME";
	
	/**
	 * The age of the logged user. To get the value, use a syntax like {@code userCp.getInt(User.AGE)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	AGE						= "USER_AGE";
	
	/**
	 * The body weight of the logged user, in kilograms. To get the value, use a syntax like {@code userCp.getString(User.BODYWEIGHT)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	BODYWEIGHT				= "USER_BODYWEIGHT";
	
	/**
	 * The height of the logged user, in centimeters. To get the value, use a syntax like {@code userCp.getInt(User.HEIGHT)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	HEIGHT					= "USER_HEIGHT";
	
	/**
	 * The gender of the logged user (M = male, F = female). To get the value, use a syntax like {@code userCp.getString(User.GENDER)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	GENDER					= "USER_GENDER";
	
	/**
	 * The birthday of the logged user. To get the value, use a syntax like {@code userCp.getString(User.BIRTHDAY)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	BIRTHDAY				= "USER_BIRTHDAY";
	
	/**
	 * The last date on which the user is weighted. To get the value, use a syntax like {@code userCp.getString(User.BODYWEIGHT_LASTUPDATE)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	BODYWEIGHT_LASTUPDATE	= "USER_BODYWEIGHT_LASTUPDATE";
	
	/**
	 * The language id of the logged user. To get the value, use a syntax like {@code userCp.getiNT(User.LANGUAGE)}
	 * where <b><i>userCp</i></b> is an instance of User class. 
	 * <p>
	 * To get the instance of Language enum, use syntax like {@code Languages l = Languages.fromTgsCode(id)}, where <b><i>id</i></b> is the id obtained with {@code userCp.getInt(User.LANGUAGE)}.
	 * </p>
	 */
	public final static String	LANGUAGE				= "USER_LANGUAGE";
	
	/**
	 * The maximum heart rate for the logged user. To get the value, use a syntax like {@code userCp.getInt(User.MAX_HEART_RATE)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	MAX_HEART_RATE			= "USER_MAX_HEART_RATE";
	
	/**
	 * The favorite entertainment for the logged user, read from the MyWellnessKey. To get the value, use a syntax like {@code userCp.getInt(User.FAV_ENTERTAINMENT)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	FAV_ENTERTAINMENT		= "FAV_ENTERTAINMENT";
	
	/**
	 * The favorite tv channel for the logged user, read from the MyWellnessKey. To get the value, use a syntax like {@code userCp.getInt(User.FAV_CHANNEL)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	FAV_CHANNEL				= "USER_FAV_CHANNEL";
	
	/**
	 * The favorite volume for the logged user, read from the MyWellnessKey. To get the value, use a syntax like {@code userCp.getInt(User.FAV_VOLUME)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	FAV_VOLUME				= "USER_FAV_VOLUME";

	public final static String	USER_ID					= "USER_ID";
	
	/**
	 * The club id where the logged user is registered. To get the value, use a syntax like {@code userCp.getInt(User.CLUB_ID)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	CLUB_ID					= "CLUB_ID";
	
	/**
	 * The chain id where the logged user is registered. To get the value, use a syntax like {@code userCp.getInt(User.CHAIN_ID)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	CHAIN_ID				= "CHAIN_ID";
	
	/**
	 * The session id for the logged user. To get the value, use a syntax like {@code userCp.getInt(User.SESSION_ID)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	SESSION_ID				= "SESSION_ID";

	
	/**
	 * The picture type for the logged user. To get the value, use a syntax like {@code userCp.getString(User.USER_PICTURE)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 * <p>
	 * The allowed types are:
	 * <ul><li>1 = User image</li><li>M = Generic male image</li><li>F = Generic female image</li></ul>
	 * </p>
	 */
	public final static String	USER_PICTURE			= "USER_PICTURE";
	
	/**
	 * The picture url for the logged user. To get the value, use a syntax like {@code userCp.getString(User.USER_PICTURE_URL)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	USER_PICTURE_URL			= "USER_PICTURE_URL";
	
	/**
	 * The mail for the logged user. To get the value, use a syntax like {@code userCp.getString(User.USER_MAIL)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	USER_EMAIL				= "USER_EMAIL";
	
	/**
	 * The user id on MyWellness server for the logged user. To get the value, use a syntax like {@code userCp.getString(User.MWAPPS_USERID)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	MWAPPS_USERID			= "MWAPPS_USERID";
	
	/**
	 * The user culture for the logged user. To get the value, use a syntax like {@code userCp.getString(User.USER_CULTURE)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	USER_CULTURE			= "USER_CULTURE";
	
	/**
	 * The unit measure system for the logged user. To get the value, use a syntax like {@code userCp.getiNT(User.UNIT_MEASURE_SYSTEM)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 * <p>
	 * This will return 0 for metric system, 1 for imperial. Use this info in according with the enum {@code UnitMeasureSystem}:
	 * <pre>
	 * {@code
	 * UnitMeasureSystem.Metric
	 * UnitMeasureSystem.Imperial}
	 * </pre>
	 * </p>
	 */
	public final static String	UNIT_MEASURE_SYSTEM		= "UNIT_MEASURE_SYSTEM";
	
	/**
	 * The offline user id. To get the value, use a syntax like {@code userCp.getString(User.OFFLINE_USERID)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String	OFFLINE_USERID			= "OFFLINE_USERID";
	
	/**
	 * The account type for the logged user. To get the value, use a syntax like {@code userCp.getString(User.ACCOUNT_TYPE)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 * <p>
	 * The allowed types are:
	 * <pre>
	 * {@code
	 * User.AccountType_none
	 * User.AccountType_withAccount
	 * User.AccountType_facilityUser}
	 * </pre>
	 * </p>
	 */
	public final static String	ACCOUNT_TYPE			= "ACCOUNT_TYPE";
	
	/**
	 * Flag indicating if the user has the my wellness key. To get the value, use a syntax like {@code userCp.getBoolean(User.ACCOUNT_TYPE)}
	 * where <b><i>userCp</i></b> is an instance of User class.
	 */
	public final static String HAS_MYWELLNESSKEY = "HAS_MYWELLNESSKEY" ;
	
	
	/**
	 * The default value is 0. 
	 */
	public final static int	AccountType_none = 0;
	
	/**
	 * The default value is 1.
	 */
	public final static int	AccountType_withAccount = 1;
	
	/**
	 * The default value is 2.
	 */
	public final static int	AccountType_facilityUser = 2;
	
	
	
	/**
	 * Default value for the user's body weight. The default value is 70 kg.
	 */
	public final static int		defaultBodyWeight		= 70;
	
	/**
	 * Default value for the user's age. The default value is 30 years old.
	 */
	public final static int		defaultAge				= 30;
	
	/**
	 * Default value for the user's body height. The default value is 170 cm.
	 */
	public final static int		defaultBodyHeight		= 170;
	
	/**
	 * The default value is 1
	 */
	public final static int		Male					= 1;
	
	/**
	 * The default value is 2
	 */
	public final static int		Female					= 2;

	
	/**
	 * Indicates that the logged user has no favorite entertainment. The default value is 0.
	 */
	public static final int		NoEntertainment			= 0;
	
	/**
	 * Indicates that the favorite entertainment of the logged user is radio. The default value is 1.
	 */
	public static final int		Radio					= 1;
	
	/**
	 * Indicates that the favorite entertainment of the logged user is TV. The default value is 2.
	 */
	public static final int		TV_Partial				= 2;
	
	/**
	 * Indicates that the favorite entertainment of the logged user is TV in full screen mode. The default value is 3.
	 */
	public static final int		TV_Full					= 3;

	
	/**
	 * The Content Provider URI.
	 */
	public static final Uri		CONTENT_URI				= Uri.parse("content://com.technogym.android.wow.user.AUTHORITY/item");

	
	private static User instance = null;
	
	/**
	 * Obtain reference of singleton.
	 */
	
	
	private User(Context ctx)
	{
		super(ctx, CONTENT_URI);
	}
	
	public static synchronized User getInstance(Context ctx)
	{
		if(instance == null)
		{
			instance = new User(ctx);
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
	 * Reset every properties of the User content provider. 
	 */
	public void reset()
	{
		this.set(User.NAME, "");
		this.set(User.SURNAME, "");
		this.set(User.NICKNAME, "");
		this.set(User.AGE, "");
		this.set(User.BODYWEIGHT, "");
		this.set(User.HEIGHT, "");
		this.set(User.GENDER, "");
		this.set(User.BIRTHDAY, "");
		this.set(User.BODYWEIGHT_LASTUPDATE, "");
		this.set(User.LANGUAGE, -1);
		this.set(User.MAX_HEART_RATE, "");
		this.set(User.FAV_ENTERTAINMENT, "");
		this.set(User.FAV_CHANNEL, "");
		this.set(User.FAV_VOLUME, "");
		this.set(User.USER_ID, "");
		this.set(User.CLUB_ID, "");
		this.set(User.CHAIN_ID, "");
		this.set(User.SESSION_ID, "");
		this.set(User.USER_PICTURE, "");
		this.set(User.USER_PICTURE_URL, "");
		this.set(User.USER_EMAIL, "");
		this.set(User.MWAPPS_USERID, "");
		this.set(User.USER_CULTURE, "");
		this.set(User.OFFLINE_USERID, "") ;
		this.set(User.ACCOUNT_TYPE, AccountType_none) ;
		this.set(User.HAS_MYWELLNESSKEY, false) ;
	}


	/**
	 * Indicates whether the user is logged in or not.
	 * @return True if the user is logged in, false otherwise.
	 */
	public boolean IsUserLogged()
	{
		String uid = getString(User.MWAPPS_USERID);
		return (uid != null && !uid.equals(""));
	}
}
