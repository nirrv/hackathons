package com.technogym.android.myvisio.api;

import java.util.ArrayList;

import android.content.Context;

public interface Notification
{
	public static final String	WOW_SYSTEM_SERVICE_ACTION	= "com.technogym.android.wowsystemservice.ACTION";
	public static final String	REQUEST_KEY					= "com.technogym.android.wowsystemservice.REQUEST_KEY";
	public static final String	TYPE_NOTIFICATION_KEY		= "com.technogym.android.wowsystemservice.TYPE_NOTIFICATION_KEY";
	public static final String	VALUE_KEY					= "com.technogym.android.wowsystemservice.VALUE_KEY";
	public static final String	REQUEST_REMOVE_PANEL_KEY	= "com.technogym.android.wowsystemservice.COOLDOWN_REQUEST_REMOVE_PANEL_KEY";

	public static final int		ShowNotification			= 80;
	public static final int		HideNotification			= 81;
	
	public static final String MESSAGE = "com.technogym.android.notification.MESSAGE";
	public static final String SURVEY = "com.technogym.android.notification.SURVEY";

	public static final String	NOTIFICATION_RESULT			= "com.technogym.android.notification.result";
	public static final String	RESULT_KEY					= "com.technogym.android.notification.result.key";

	static public final String	ONLY_TEXT					= "OnlyText";
	static public final String	ONLY_RESOURCE_43			= "OnlyResource43";
	static public final String	ONLY_RESOURCE_LANDSCAPE		= "OnlyResourceLandscape";
	static public final String	TEXT_AND_RESOURCE			= "TextAndResource";
	static public final String	SURVEY_TEXT					= "SurveyText";
	static public final String	SURVEY_TEXT_AND_RESOURCE	= "SurveyTextAndResource";
	
	static public final String	NOTHING						= "";
	static public final String	IMAGE						= "Image";
	static public final String	VIDEO						= "Video";
	
	static public final String	SINGLE_CHOICE					= "SingleChoice";
	static public final String	MULTIPLE_CHOICE					= "MultipleChoice";

	public static final int		UNKNOWN						= -1;
	public static final int		NOT_YET_SHOWN				= 0;
	public static final int		NOW_SHOWN					= 1;
	public static final int		CLOSED						= 2;
	public static final int		PULLED_DOWN					= 3;
	public static final int		ENCLOSED					= 4;
	public static final int		CLOSED_AND_COMPLETED		= 5;

	public void close(Context ctx);
	public void show(Context ctx);
	public void answer(Context ctx, String _answers);
	
	public ArrayList<String> getAnswers();
	
	public String getResType();
	public String getTitle();
	public String getLayout();
	public String getResPath();
	public String getText();
	public String toShow(Context ctx);
	public void setEvelope(boolean envState);
	public void enclosed(Context ctx);
	public int getState();
	public String getMsgUuid();
}
