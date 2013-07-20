package com.technogym.android.myvisio.api;


import android.content.ContentValues;

public class Sample
{
	public static final String VALUE = "VALUE";
	public static final String TIMESTAMP = "TIMESTAMP";
	
	public double value;
	public long timestamp;
	
	public Sample(double d, long t)
	{
		value = d;
		timestamp = t;
	}
	
	public Sample(long d )
	{
		value = d;
		timestamp = System.currentTimeMillis();
	}

	public Sample()
	{
		value = -1L;
		timestamp = 0L;
	}
	
	public ContentValues toContentValues()
	{
		ContentValues v = new ContentValues();
		v.put(VALUE, value);
		v.put(TIMESTAMP, timestamp);
		
		return v;
	}
	
	public String toString()
	{
		return String.format("value %d timestamp %d", value, timestamp);
	}
}
