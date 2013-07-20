package com.technogym.android.myvisio.api;

import java.util.ArrayList;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class UnityMessage implements Parcelable ,Notification
{
	private String				title						= NOTHING;

	private String				text						= NOTHING;

	public String				id;
	private String				layout						= NOTHING;
	private boolean				envelope					= false;

	private String				resType						= NOTHING;
	private String				resPath						= NOTHING;

	public int					state;

	public UnityMessage(String _title, String _text, String _layout, boolean _envelope, String _resType, String _resPath)
	{
		title = _title;
		text = _text;
		// SS uuid = UUID.randomUUID();
		layout = _layout;
		envelope = _envelope;
		resType = _resType;
		resPath = _resPath;

		state = NOT_YET_SHOWN;
	}

	public UnityMessage(String _uid, String _title, String _text, String _layout, boolean _envelope, String _resType, String _resPath)
	{
		title = _title;
		text = _text;
		id = _uid;
		layout = _layout;
		envelope = _envelope;
		resType = _resType;
		resPath = _resPath;

		state = NOT_YET_SHOWN;
	}

	public UnityMessage(Parcel in)
	{
		readFromParcel(in);
	}

	//
	// public UUID getUUID()
	// {
	// return uuid;
	// }

	@Override
	public String getTitle()
	{
		return title;
	}

	@Override
	public String getLayout()
	{
		return layout;
	}

	public boolean isEnvelope()
	{
		return envelope;
	}

	@Override
	public String getResType()
	{
		return resType;
	}

	@Override
	public String getResPath()
	{
		return resPath;
	}

	@Override
	public String getText()
	{
		return text;
	}
	
	@Override
	public void setEvelope(boolean envState)
	{
		envelope = envState;
	}

	@Override
	public String getMsgUuid()
	{
		return id;
	}

	// public void setUUID(UUID _uuid)
	// {
	// uuid = _uuid;
	// }

	private void readFromParcel(Parcel in)
	{
		title = in.readString();
		text = in.readString();
		id = in.readString();
		layout = in.readString();
		envelope = in.readByte() == 1;
		resType = in.readString();
		resPath = in.readString();
		state = in.readInt();
	}

	@Override
	public int describeContents()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(title);
		dest.writeString(text);
		dest.writeString(id);
		dest.writeString(layout);
		dest.writeByte((byte) (envelope ? 1 : 0));
		dest.writeString(resType);
		dest.writeString(resPath);
		dest.writeInt(state);
	}

	public static final Creator<UnityMessage>	CREATOR	= new Creator<UnityMessage>()
														{
															public UnityMessage createFromParcel(Parcel source)
															{
																return new UnityMessage(source);
															}

															public UnityMessage[] newArray(int size)
															{
																return new UnityMessage[size];
															}
														};

	public String toShow(Context ctx)
	{
		if (id == null)
			id = UUID.randomUUID().toString();

		ContentValues cv = new ContentValues();
		
		String[] projection = null;
		String selection = NotificationMetaData.UID + " = ?";
		String[] selectionArgs = { id };
		String sortOrder = null;
		
		Cursor c = ctx.getContentResolver().query(NotificationMetaData.NOTIFICATION_TABLE_URI, projection, selection, selectionArgs, sortOrder);
		if ((c != null) && (c.moveToNext()))
		{
			long ts_start_showing = c.getLong(c.getColumnIndex(NotificationMetaData.TIMESTAMP_START_SHOWING));
			long ts_stop_showing = c.getLong(c.getColumnIndex(NotificationMetaData.TIMESTAMP_STOP_SHOWING));

			
			//AA: safety ...
			long showingTime = (ts_stop_showing == 0) ? 0 : ((ts_stop_showing > ts_start_showing) ? ts_stop_showing - ts_start_showing : 0);			
			showingTime += c.getLong(c.getColumnIndex(NotificationMetaData.TIME_ALREADY_SHOWN));
			cv.put(NotificationMetaData.TIME_ALREADY_SHOWN, showingTime);
			
			
			cv.put(NotificationMetaData.TIMESTAMP_START_SHOWING, 0);
			cv.put(NotificationMetaData.TIMESTAMP_STOP_SHOWING, 0);
			
			ctx.getContentResolver().update(NotificationMetaData.NOTIFICATION_TABLE_URI, cv, selection, selectionArgs);
		}
		else
		{
			state = NOT_YET_SHOWN;
			cv.put(NotificationMetaData.UID, id.toString());
			cv.put(NotificationMetaData.TIMESTAMP_START_SHOWING, 0);
			cv.put(NotificationMetaData.TIMESTAMP_STOP_SHOWING, 0);
			cv.put(NotificationMetaData.STATE, state);
			cv.put(NotificationMetaData.TIME_ALREADY_SHOWN, 0);
			ctx.getContentResolver().insert(NotificationMetaData.NOTIFICATION_TABLE_URI, cv);
		}
		
		if(c!=null)
			c.close();	
		
		if (!isEnvelope())
		{
//			Envelope.showOpen(ctx);
			Intent intent = new Intent(WOW_SYSTEM_SERVICE_ACTION);
			intent.putExtra(REQUEST_KEY, ShowNotification);
			intent.putExtra(TYPE_NOTIFICATION_KEY, MESSAGE);
			intent.putExtra(VALUE_KEY, this);
			ctx.startService(intent);
		}
//		else
//			Envelope.showClose(ctx);

		return id;
	}

	public void show(Context ctx)
	{
		String[] projection = null;
		String selection = NotificationMetaData.UID + " = ?";
		String[] selectionArgs = { id };
		String sortOrder = null;

		Cursor c = ctx.getContentResolver().query(NotificationMetaData.NOTIFICATION_TABLE_URI, projection, selection, selectionArgs, sortOrder);

		if (c != null)
		{
			c.moveToLast();
			state = NOW_SHOWN;
			c.moveToFirst();
			ContentValues cv = new ContentValues();
			cv.put(NotificationMetaData.TIMESTAMP_START_SHOWING, System.currentTimeMillis());
			cv.put(NotificationMetaData.STATE, state);
			// SS cv.put(TIMESTAMP_STOP_SHOWING, 0);

			c.close();
			ctx.getContentResolver().update(NotificationMetaData.NOTIFICATION_TABLE_URI, cv, selection, selectionArgs);
		}
	}

	@Override
	public void close(Context ctx)
	{
		String[] projection = null;
		String selection = NotificationMetaData.UID + " = ?";
		String[] selectionArgs = { id };
		// SS String sortOrder = null;
		String sortOrder = NotificationMetaData.ID_KEY + " ASC";

		Cursor c = ctx.getContentResolver().query(NotificationMetaData.NOTIFICATION_TABLE_URI, projection, selection, selectionArgs, sortOrder);

		if (c != null)
		{
			if(c.moveToNext())
			{
				state = CLOSED;
				ContentValues cv = new ContentValues();
				cv.put(NotificationMetaData.TIMESTAMP_STOP_SHOWING, System.currentTimeMillis());
				cv.put(NotificationMetaData.STATE, state);
				ctx.getContentResolver().update(NotificationMetaData.NOTIFICATION_TABLE_URI, cv, selection, selectionArgs);
				Intent intent = new Intent(NOTIFICATION_RESULT);
				intent.putExtra(RESULT_KEY, this);
				ctx.sendBroadcast(intent);
			}
			c.close();
		}
	}

	public void enclosed(Context ctx)
	{
		String[] projection = null;
		String selection = NotificationMetaData.UID + " = ?";
		String[] selectionArgs = { id };
		// SS String sortOrder = null;
		String sortOrder = NotificationMetaData.ID_KEY + " ASC";

		Cursor c = ctx.getContentResolver().query(NotificationMetaData.NOTIFICATION_TABLE_URI, projection, selection, selectionArgs, sortOrder);

		if (c != null)
		{
			if(c.moveToNext())
			{
				state = ENCLOSED;
				c.moveToFirst();
				ContentValues cv = new ContentValues();
				cv.put(NotificationMetaData.TIMESTAMP_STOP_SHOWING, System.currentTimeMillis());
				cv.put(NotificationMetaData.STATE, state);
	
				ctx.getContentResolver().update(NotificationMetaData.NOTIFICATION_TABLE_URI, cv, selection, selectionArgs);
	
				Intent intent = new Intent(NOTIFICATION_RESULT);
				intent.putExtra(RESULT_KEY, this);
				ctx.sendBroadcast(intent);
			}
			c.close();
		}
	}

	public void hide(Context ctx)
	{
		String[] projection = null;
		String selection = NotificationMetaData.UID + " = ?";
		String[] selectionArgs = { id };
		// SS String sortOrder = null;
		String sortOrder = NotificationMetaData.ID_KEY + " ASC";

		Cursor c = ctx.getContentResolver().query(NotificationMetaData.NOTIFICATION_TABLE_URI, projection, selection, selectionArgs, sortOrder);

		if (c != null)
		{
			if(c.moveToNext())
			{
				state = PULLED_DOWN;
				ContentValues cv = new ContentValues();
				cv.put(NotificationMetaData.TIMESTAMP_STOP_SHOWING, System.currentTimeMillis());
				cv.put(NotificationMetaData.STATE, state);
	
				ctx.getContentResolver().update(NotificationMetaData.NOTIFICATION_TABLE_URI, cv, selection, selectionArgs);
	
				Intent intent = new Intent(NOTIFICATION_RESULT);
				intent.putExtra(RESULT_KEY, this);
				ctx.sendBroadcast(intent);
			}
			c.close();
		}
	}

	/*
	 * return 0 if uuid message hasn't been shown yet
	 * otherwise if uuid message had already showed return msec of showing
	 */
	public long getShowingTime(Context ctx)
	{
		String[] projection = null;
		String selection = NotificationMetaData.UID + " = ?";
		String[] selectionArgs = { id };
		String sortOrder = null;

		Cursor c = ctx.getContentResolver().query(NotificationMetaData.NOTIFICATION_TABLE_URI, projection, selection, selectionArgs, sortOrder);

		long showingTime = 0;

		if (c != null)
		{
			if(c.moveToNext())
			{
				long time_already_show = c.getLong(c.getColumnIndex(NotificationMetaData.TIME_ALREADY_SHOWN));
				showingTime = time_already_show;
				
				long ts_start_showing = c.getLong(c.getColumnIndex(NotificationMetaData.TIMESTAMP_START_SHOWING));
				long ts_stop_showing = c.getLong(c.getColumnIndex(NotificationMetaData.TIMESTAMP_STOP_SHOWING));

				showingTime += (ts_stop_showing == 0) ? 0 : ((ts_stop_showing > ts_start_showing) ? ts_stop_showing - ts_start_showing : 0);
			}
			c.close();
		}

		return showingTime;
	}

	@Override
	public void answer(Context ctx, String _answers)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<String> getAnswers()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getState()
	{
		return state;
	}
	static public int getState(Context ctx, String id)
	{
		String[] projection = null;
		String selection = NotificationMetaData.UID + " = ?";
		String[] selectionArgs = { id };
		String sortOrder = null;

		Cursor c = ctx.getContentResolver().query(NotificationMetaData.NOTIFICATION_TABLE_URI, projection, selection, selectionArgs, sortOrder);

		int _state = UNKNOWN;
		if (c != null)
		{
			c.moveToLast();
			_state = c.getInt(c.getColumnIndex(NotificationMetaData.STATE));
			c.close();
		}
		
		return _state;
	}
	
	static public void reset(Context ctx)
	{
		ctx.getContentResolver().delete(NotificationMetaData.NOTIFICATION_TABLE_URI, null, null);
	}

}
