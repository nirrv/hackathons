package com.technogym.android.myvisio.api;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class UnityQuestion implements Parcelable
{
	static public final String	NOTHING			= "";
	static public final String	IMAGE			= "Image";
	static public final String	VIDEO			= "Video";
	static public final String	MULTIPLE_CHOICE	= "MultipleChoice";
	static public final String	SINGLE_CHOICE	= "SingleChoice";

	private String				question;

	private ArrayList<String>	answers;
	private int					answer;

	private String				type;

	public UnityQuestion(String _question, ArrayList<String> _answers, String _type)
	{
		question = _question;
		answers = _answers;
		type = _type;
	}

	public ArrayList<String> getAllAnswers()
	{
		return answers;
	}

	public void onAnswer(int _answer)
	{
		answer = _answer;
	}

	public int getAnswer()
	{
		return answer;
	}

	public boolean isSingleChoice()
	{
		return type.contentEquals(SINGLE_CHOICE);
	}
	public boolean isMultipleChoice()
	{
		return type.contentEquals(MULTIPLE_CHOICE);
	}

	public UnityQuestion(Parcel in)
	{
		question = "";
		type = "";
		answers = new ArrayList<String>();
		readFromParcel(in);
	}

	private void readFromParcel(Parcel in)
	{
		question = in.readString();
		in.readStringList(answers);
		answer = in.readInt();
		type = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		// TODO Auto-generated method stub
		dest.writeString(question);
		dest.writeStringList(answers);
		dest.writeInt(answer);
		dest.writeString(type);
	}

	@Override
	public int describeContents()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public static final Parcelable.Creator<UnityQuestion>	CREATOR	= new Parcelable.Creator<UnityQuestion>()
																	{
																		public UnityQuestion createFromParcel(Parcel source)
																		{
																			return new UnityQuestion(source);
																		}

																		public UnityQuestion[] newArray(int size)
																		{
																			return new UnityQuestion[size];
																		}
																	};

	public String getText()
	{
		return question;
	}
}
