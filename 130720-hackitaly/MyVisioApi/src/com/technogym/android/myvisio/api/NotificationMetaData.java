package com.technogym.android.myvisio.api;

import android.net.Uri;

public class NotificationMetaData
{
	private NotificationMetaData()
	{}

	public static final String	AUTHORITY					= "com.technogym.android.wow.notificator.AUTHORITY";
	public static final String	NOTIFICATION_TABLE_NAME		= "NotificationTable";
	public static final Uri		NOTIFICATION_TABLE_URI		= Uri.parse("content://" + AUTHORITY + "/" + NOTIFICATION_TABLE_NAME);

	public static final String	ID_KEY						= "_id";
	public static final String	TIMESTAMP_START_SHOWING		= "ts_show";
	public static final String	TIMESTAMP_STOP_SHOWING		= "ts_hide";
	public static final String	UID							= "UID";
	public static final String	STATE						= "state";
	public static final String	TIME_ALREADY_SHOWN			= "ts";
	
	public static final String	OFFLINE_JASON			= "LAST_JASON";
	
	public static final String	SURVEY_TABLE_NAME		= "SurveyTable";
	public static final Uri		SURVEY_TABLE_URI		= Uri.parse("content://" + AUTHORITY + "/" + SURVEY_TABLE_NAME);

	public static final String	COMMUNICATOR_OFFLINE_TABLE_NAME		= "OfflineCommTable";
	public static final Uri		COMMUNICATOR_OFFLINE_TABLE_URI		= Uri.parse("content://" + AUTHORITY + "/" + COMMUNICATOR_OFFLINE_TABLE_NAME);

	
	public static final String	CURRENT_QUESTION		= "current_question";
	public static final String	ANSWERS					= "answers";
}
