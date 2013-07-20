package com.technogym.android.myvisio.api.systembar;

import android.content.Context;
import android.content.Intent;

public class Envelope
{
	private static final String	SYSTEM_BAR_SERVICE_ACTION		= "com.technogym.android.system.systembar.ACTION";
	private static final String	REQUEST_KEY						= "com.technogym.android.system.systembar.REQUEST_KEY";
	
	private static final int SHOW_ENVELOPE_OPEN 	= 10;
	private static final int SHOW_ENVELOPE_CLOSED 	= 11;
	private static final int HIDE_ENVELOPE 			= 12;
	
	public static final String ENVELOPE_OPENED_NOTIFICATION = "com.technogym.android.systemui.envelope.opened";
	public static final String ENVELOPE_CLOSED_NOTIFICATION = "com.technogym.android.systemui.envelope.closed";
	
	
	private Envelope()
	{}
	
	public static void showOpen(Context ctx)
	{
		Intent intent = new Intent(SYSTEM_BAR_SERVICE_ACTION);
		intent.putExtra(REQUEST_KEY, SHOW_ENVELOPE_OPEN);
		ctx.startService(intent);		
	}
	
	public static void showClose(Context ctx)
	{
		Intent intent = new Intent(SYSTEM_BAR_SERVICE_ACTION);
		intent.putExtra(REQUEST_KEY, SHOW_ENVELOPE_CLOSED);
		ctx.startService(intent);		
	}
	
	public static void hide(Context ctx)
	{
		Intent intent = new Intent(SYSTEM_BAR_SERVICE_ACTION);
		intent.putExtra(REQUEST_KEY, HIDE_ENVELOPE);
		ctx.startService(intent);		
	}
}
