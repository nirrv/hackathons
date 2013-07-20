package com.technogym.android.myvisio.api;

import android.content.Context;
import android.content.Intent;

public class Tracking 
{
	public static final String ACTION_SUBSCRIBE_PERFORMED_EXERCISE_XML = "com.technogym.android.mywellness.ACTION_SUBSCRIBE_PERFORMED_EXERCISE_XML" ;
	
	public static void subscribePerformedExerciseXml( Context ctx, String url)
	{
		Intent in = new Intent(ACTION_SUBSCRIBE_PERFORMED_EXERCISE_XML) ;
		in.putExtra("URL", url) ;
		ctx.sendBroadcast(in) ;
	}
}
