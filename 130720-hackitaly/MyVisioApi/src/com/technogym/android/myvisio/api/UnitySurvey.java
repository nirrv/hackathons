package com.technogym.android.myvisio.api;

import java.util.ArrayList;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class UnitySurvey implements Parcelable ,Notification
{
//	public static final int		NOT_YET_SHOWN				= 0;
//	public static final int		NOW_SHOWN					= 1;
//	public static final int		CLOSED						= 2;
//	public static final int		PULLED_DOWN					= 3;
//	public static final int		ENCLOSED					= 4;
//	public static final int		CLOSED_AND_COMPLETED		= 5;
	
	public String id;
	public String title;
	public boolean	envelope;
	public ArrayList<UnityQuestion> questions;
	public int	state;
	public int current_question;
	
	
	private String	layout						= NOTHING;
	private String	resType						= NOTHING;
	private String	resPath						= NOTHING;
	
	public UnitySurvey(String _id, String _title, boolean _envelope, ArrayList<UnityQuestion> _questions, String _layout,  String _resType, String _resPath)
	{
		questions = _questions;
		id = _id;
		title = _title;
		envelope = _envelope;
		state = NOT_YET_SHOWN;
		if(_layout.equals("OnlyText"))
			layout = Notification.SURVEY_TEXT;
		else
			layout = Notification.SURVEY_TEXT_AND_RESOURCE;
			
		resType = _resType;
		resPath = _resPath;		
		current_question = 0;	
	}
	
//	public void answer()
//	{
//		current_question++;
//		//SS memorizza nel DB
//	}
	
	public UnityQuestion getNextQuestion()
	{
		return questions.get(current_question);
	}
	
	public UnitySurvey(Parcel in)
	{
		questions = new ArrayList<UnityQuestion>();
		readFromParcel(in); 
	}
	
	private void readFromParcel(Parcel in)
	{
		title = in.readString();
		id = in.readString();
		envelope = in.readByte() == 1;
		state = in.readInt();		
		ArrayList<UnityQuestion> _questions = new ArrayList<UnityQuestion>();
		in.readTypedList(_questions, UnityQuestion.CREATOR);
		for(int i=0; i < _questions.size(); i++)
			questions.add(_questions.get(i));
		_questions.clear();
		current_question = in.readInt();
		
		layout = in.readString();
		resType = in.readString();
		resPath = in.readString();			
	}
		
	@Override
	public void setEvelope(boolean envState)
	{
		envelope = envState;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(title);
		dest.writeString(id);
		dest.writeByte((byte) (envelope ? 1 : 0));
		dest.writeInt(state);	
		dest.writeTypedList(questions);
		dest.writeInt(current_question);
		
		dest.writeString(layout);
		dest.writeString(resType);
		dest.writeString(resPath);
	}
	

	@Override
	public int describeContents()
	{
		return 0;
	}
	
	public static final Parcelable.Creator<UnitySurvey> CREATOR = new Parcelable.Creator<UnitySurvey>() 
	{
		public UnitySurvey createFromParcel(Parcel source) 
		{
			return new UnitySurvey(source);
		}
		
		public UnitySurvey[] newArray(int size) 
		{
			return new UnitySurvey[size];
		}
	};

	@Override
	public String getResType()
	{
		return resType;
	}

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

	@Override
	public String getResPath()
	{
		return resPath;
	}

	@Override
	public String getText()
	{
		return (invariant()) ? questions.get(current_question).getText(): null;
	}
	
	@Override
	public String getMsgUuid()
	{
		return id;
	}
	
	private boolean invariant()
	{
		return ((questions != null) && (current_question < questions.size())) ? true : false;
	}

	@Override
	public void show(Context ctx)
	{
		String[] projection = null;
		String selection = NotificationMetaData.UID + " = ?";
		String[] selectionArgs = { id };
		String sortOrder = null;

		Cursor c = ctx.getContentResolver().query(NotificationMetaData.SURVEY_TABLE_URI, projection, selection, selectionArgs, sortOrder);

		if (c != null)
		{
			c.moveToLast();
			state = NOW_SHOWN;
			c.moveToFirst();
			ContentValues cv = new ContentValues();
			
			//AA: safety ...
			long ts_start_showing = c.getLong(c.getColumnIndex(NotificationMetaData.TIMESTAMP_START_SHOWING));
			if(ts_start_showing == 0)//AA: per gestire i survey che hanno più pannelli e quindi eseguono + show e non close !!!!!!!
				cv.put(NotificationMetaData.TIMESTAMP_START_SHOWING, System.currentTimeMillis());
			cv.put(NotificationMetaData.STATE, state);
			c.close();
			ctx.getContentResolver().update(NotificationMetaData.SURVEY_TABLE_URI, cv, selection, selectionArgs);
		}		
	}
	@Override
	public void close(Context ctx)
	{
		String[] projection = null;
		String selection = NotificationMetaData.UID + " = ?";
		String[] selectionArgs = { id };
		String sortOrder = NotificationMetaData.ID_KEY + " ASC";

		Cursor c = ctx.getContentResolver().query(NotificationMetaData.SURVEY_TABLE_URI, projection, selection, selectionArgs, sortOrder);

		if (c != null)
		{
			c.moveToLast();
			if(current_question == questions.size())
				state = CLOSED_AND_COMPLETED;
			else
				state = CLOSED;
			c.moveToFirst();
			ContentValues cv = new ContentValues();
			cv.put(NotificationMetaData.TIMESTAMP_STOP_SHOWING, System.currentTimeMillis());
			cv.put(NotificationMetaData.STATE, state);

			c.close();
			ctx.getContentResolver().update(NotificationMetaData.SURVEY_TABLE_URI, cv, selection, selectionArgs);
			
			Intent intent = new Intent(NOTIFICATION_RESULT);
			intent.putExtra(RESULT_KEY, this);
			ctx.sendBroadcast(intent);
		}		
	}
	
	public static long getVisualizationTime(Context ctx,String id)
	{
		String[] projection = null;
		String selection = NotificationMetaData.UID + " = ?";
		String[] selectionArgs = { id };
		String sortOrder = null;

		Cursor c = ctx.getContentResolver().query(NotificationMetaData.SURVEY_TABLE_URI, projection, selection, selectionArgs, sortOrder);

		if ((c != null) && (c.moveToNext()))
		{
			long ts_start_showing = c.getLong(c.getColumnIndex(NotificationMetaData.TIMESTAMP_START_SHOWING));
			long ts_stop_showing = c.getLong(c.getColumnIndex(NotificationMetaData.TIMESTAMP_STOP_SHOWING));

			long showingTime = (ts_stop_showing == 0) ? 0 : ((ts_stop_showing > ts_start_showing) ? ts_stop_showing - ts_start_showing : 0);			
			showingTime += c.getLong(c.getColumnIndex(NotificationMetaData.TIMESTAMP_START_SHOWING));
			return showingTime/1000;
		}
		return 0;
	}

	@Override
	public void answer(Context ctx, String _answers)
	{
//		Log.e("SURVEY","" + _answers);
		current_question++;
		updateAnswer(ctx, _answers);
//		Log.e("SURVEY","current_question " + current_question);
//		Log.e("SURVEY","answers " + _answers);
		Intent intent = new Intent(WOW_SYSTEM_SERVICE_ACTION);
		intent.putExtra(REQUEST_KEY, HideNotification);
		intent.putExtra(REQUEST_REMOVE_PANEL_KEY, true);
		ctx.startService(intent);

		if(invariant())
		{
			intent = new Intent(WOW_SYSTEM_SERVICE_ACTION);
			intent.putExtra(REQUEST_KEY, ShowNotification);
			intent.putExtra(TYPE_NOTIFICATION_KEY, SURVEY);
			intent.putExtra(VALUE_KEY, this);
			ctx.startService(intent);
		}	
		else
		{
			if(questions != null && current_question == questions.size()) //AA: survey is over! Close it!
				close(ctx);		
		}
	}
	
	public String toShow(Context ctx)
	{		
		if (id == null)
			id = UUID.randomUUID().toString();

		ContentValues cv = new ContentValues();

		String[] projection = null;
		String selection = NotificationMetaData.UID + " = ?";
		String[] selectionArgs = { id };
		String sortOrder = null;

		Cursor c = ctx.getContentResolver().query(NotificationMetaData.SURVEY_TABLE_URI, projection, selection, selectionArgs, sortOrder);

		if ((c != null) && (c.moveToNext()))
		{
			long ts_start_showing = c.getLong(c.getColumnIndex(NotificationMetaData.TIMESTAMP_START_SHOWING));
			long ts_stop_showing = c.getLong(c.getColumnIndex(NotificationMetaData.TIMESTAMP_STOP_SHOWING));
			//AA: safety ...
			long showingTime = (ts_stop_showing == 0) ? 0 : ((ts_stop_showing > ts_start_showing) ? ts_stop_showing - ts_start_showing : 0);			
			showingTime += c.getLong(c.getColumnIndex(NotificationMetaData.TIME_ALREADY_SHOWN));
			cv.put(NotificationMetaData.TIME_ALREADY_SHOWN, showingTime);
			cv.put(NotificationMetaData.TIMESTAMP_START_SHOWING, 0 );
			cv.put(NotificationMetaData.TIMESTAMP_STOP_SHOWING, 0);
			
			ctx.getContentResolver().update(NotificationMetaData.SURVEY_TABLE_URI, cv, selection, selectionArgs);
			
			current_question =  (int) c.getLong(c.getColumnIndex(NotificationMetaData.CURRENT_QUESTION));
		}
		else
		{  //AA: quando si verifica questo caso????
			state = NOT_YET_SHOWN;
			cv.put(NotificationMetaData.UID, id.toString());
			cv.put(NotificationMetaData.TIMESTAMP_START_SHOWING, 0);
			cv.put(NotificationMetaData.TIMESTAMP_STOP_SHOWING, 0);
			cv.put(NotificationMetaData.STATE, state);
			cv.put(NotificationMetaData.CURRENT_QUESTION, current_question);
			cv.put(NotificationMetaData.TIME_ALREADY_SHOWN, 0);
			ctx.getContentResolver().insert(NotificationMetaData.SURVEY_TABLE_URI, cv);
		}
		
		if(c!=null)
			c.close();

		Intent intent = new Intent(WOW_SYSTEM_SERVICE_ACTION);
		intent.putExtra(REQUEST_KEY, ShowNotification);
		intent.putExtra(TYPE_NOTIFICATION_KEY, SURVEY);
		intent.putExtra(VALUE_KEY, this);
		ctx.startService(intent);

		return id;		
	}

	@Override
	public ArrayList<String> getAnswers()
	{
		return (invariant()) ? questions.get(current_question).getAllAnswers() : null;
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

		Cursor c = ctx.getContentResolver().query(NotificationMetaData.SURVEY_TABLE_URI, projection, selection, selectionArgs, sortOrder);

		long showingTime = 0;

		if (c != null)
		{
			// SS c.moveToFirst();
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
	
	public boolean updateAnswer(Context ctx, String _answers)
	{
		String[] projection = null;
		String selection = NotificationMetaData.UID + " = ?";
		String[] selectionArgs = { id };
		String sortOrder = null;
		
//		Log.e("SURVEY","" + _answers);

		try
		{
			Cursor c = ctx.getContentResolver().query(NotificationMetaData.SURVEY_TABLE_URI, projection, selection, selectionArgs, sortOrder);
	
			if (c != null)
			{
				if(c.moveToFirst())
				{
//					Log.e("SURVEY","D1");
					ContentValues cv = new ContentValues();
					String _answer =  c.getString(c.getColumnIndex(NotificationMetaData.ANSWERS));
					if(_answer == null) 
						_answer = "";
					cv.put(NotificationMetaData.ANSWERS, _answer + "!" + _answers);
					cv.put(NotificationMetaData.CURRENT_QUESTION,current_question);
					ctx.getContentResolver().update(NotificationMetaData.SURVEY_TABLE_URI, cv, selection, selectionArgs);
				}
				c.close();
			}
	
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
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

		Cursor c = ctx.getContentResolver().query(NotificationMetaData.SURVEY_TABLE_URI, projection, selection, selectionArgs, sortOrder);

		int _state = UNKNOWN;
		if (c != null)
		{
			if(c.moveToLast())			
				_state = c.getInt(c.getColumnIndex(NotificationMetaData.STATE));
			c.close();
		}
		
		return _state;
	}
//	//AA: mettere quaesta funzione fuori!!!!
//	static public ArrayList<SurveyStats> getAllSurveysStat(Context ctx, String id)
//	{
//		String[] projection = null;
//		String selection = NotificationMetaData.UID + " = ?";
//		String[] selectionArgs = { id };
//		String sortOrder = null;
//
//		Cursor c = ctx.getContentResolver().query(NotificationMetaData.SURVEY_TABLE_URI, projection, selection, selectionArgs, sortOrder);
//
//		ArrayList<SurveyStats> srvStats = new ArrayList<SurveyStats>(); 
//		
//		c.moveToFirst();
//		while (c != null)
//		{
//			String uuid = c.getString(c.getColumnIndex(NotificationMetaData.UID));
//			int state = c.getInt(c.getColumnIndex(NotificationMetaData.STATE));
//			int totDuration = c.getInt(c.getColumnIndex(NotificationMetaData.TIME_ALREADY_SHOWN));
//			String answers = c.getString(c.getColumnIndex(NotificationMetaData.ANSWERS));
//			ArrayList<ArrayList<String> > questions = null;
//			if(answers != null)
//			{
//				questions = new ArrayList<ArrayList<String> >();
//				String q[] = answers.split("!");
//				if(q.length>0)
//				{
//					
//					for(String s : q)
//					{
//						ArrayList<String> subAnswers = new ArrayList<String>();
//						String a[] = s.split("#");
//						for(String sb : a)
//						{
//							subAnswers.add(sb);
//						}
//						questions.add(subAnswers);
//					}
//				}
//			}
//			//SurveyStats(String _SurveyId,int _SurveyDuration,int _Completed,ArrayList<ArrayList<String>> _Questions)
//			SurveyStats oneSurveyStat = new SurveyStats(uuid,totDuration,(state == Notification.CLOSED_AND_COMPLETED) ? 1 : 0,questions);
//				
//			
//			if(c.isLast())
//			{
//				c.close();
//				c = null;
//			}
//			else
//				c.moveToNext();
//		}
//		
//		return null;
//	}
//	
	

	

	
	
	static public void reset(Context ctx)
	{
		ctx.getContentResolver().delete(NotificationMetaData.SURVEY_TABLE_URI, null, null);
	}

	@Override
	public void enclosed(Context ctx)
	{
		//AA: To fill!!!!!!!!!!!!
	   /*
		* 		String[] projection = null;
		* 		String selection = NotificationMetaData.UID + " = ?";
		* 		String[] selectionArgs = { id };
		* 		// SS String sortOrder = null;
		* 		String sortOrder = NotificationMetaData.ID_KEY + " ASC";
		* 		
		* 		Cursor c = ctx.getContentResolver().query(NotificationMetaData.NOTIFICATION_TABLE_URI, projection, selection, selectionArgs, sortOrder);
		* 		
		* 		if (c != null)
		* 		{
		* 		if(c.moveToNext())
		* 			{
		* 				state = ENCLOSED;
		* 				c.moveToFirst();
		* 				ContentValues cv = new ContentValues();
		* 				cv.put(NotificationMetaData.TIMESTAMP_STOP_SHOWING, System.currentTimeMillis());
		* 				cv.put(NotificationMetaData.STATE, state);	
		* 				ctx.getContentResolver().update(NotificationMetaData.NOTIFICATION_TABLE_URI, cv, selection, selectionArgs);
		* 		
		* 				Intent intent = new Intent(NOTIFICATION_RESULT);
		* 				intent.putExtra(RESULT_KEY, this);
		* 				ctx.sendBroadcast(intent);
		* 			}
		* 			c.close();
		* 		}
   	    */
		String[] projection = null;
		String selection = NotificationMetaData.UID + " = ?";
		String[] selectionArgs = { id };

		String sortOrder = null;

		Cursor c = ctx.getContentResolver().query(NotificationMetaData.SURVEY_TABLE_URI, projection, selection, selectionArgs, sortOrder);

		if (c != null && c.moveToNext())
		{		
			state = ENCLOSED;
			c.moveToFirst();
			ContentValues cv = new ContentValues();
			cv.put(NotificationMetaData.TIMESTAMP_STOP_SHOWING, System.currentTimeMillis());
			cv.put(NotificationMetaData.STATE, state);
			ctx.getContentResolver().update(NotificationMetaData.SURVEY_TABLE_URI, cv, selection, selectionArgs);

			Intent intent = new Intent(NOTIFICATION_RESULT);
			intent.putExtra(RESULT_KEY, this);
			ctx.sendBroadcast(intent);
		
			c.close();
		}
		
	}
}
