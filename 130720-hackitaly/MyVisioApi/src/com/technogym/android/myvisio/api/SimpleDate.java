package com.technogym.android.myvisio.api;


import java.util.Calendar;

public class SimpleDate
{
	private int year;
	private int month;
	private int day;
	
	public SimpleDate(int year, int month, int day)
	{
//SS		if(isCorrect(year,month,day))
		//SS		{
			this.year = year;
			this.month = month;
			this.day = day;
			//SS		}
			//SS		else
			//SS			now();
	}
	
	public SimpleDate()
	{
		now();
	}
	
	public void setYear(int year)
	{
		this.year = year;
	}

	public int getYear()
	{
		return this.year;
	}
	
	public void setMonth(int month)
	{
		this.month = month;
	}

	public int getMonth()
	{
		return this.month;
	}

	public void setDay(int day)
	{
		this.day = day;
	}

	public int getDay()
	{
		return this.day;
	}
	
	public String toString()
	{
		return String.format("year %d month %d day %d", this.year, this.month, this.day);
	}
	
/*
	public boolean isCorrect(int year, int month, int day)
	{
		if((year < 0) || (month < 0) || (day < 0))
			return false;

		if(month >= 12 )
			return false;
		
		GregorianCalendar cc =new GregorianCalendar(year,month,1);
		
		if(day > 31)
			return false;
		
		return true;
	}
*/
	
	private void now()
	{
		Calendar c = Calendar.getInstance();
		c = Calendar.getInstance();
		this.year = c.get(Calendar.YEAR);
		this.month = c.get(Calendar.MONTH);
		this.day = c.get(Calendar.DAY_OF_MONTH);
		//SS long msec = System.currentTimeMillis();
	}
}
