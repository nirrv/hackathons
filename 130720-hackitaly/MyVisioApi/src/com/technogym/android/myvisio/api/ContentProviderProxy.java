package com.technogym.android.myvisio.api;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

/**
 * <h1>Overview</h1>
 * The {@code ContentProviderProxy} class exposes a proxy for particular content providers: it is a proxy
 *  that allows you to work directly on another class, the {@code InMemoryContentProvider}.
 * The {@code InMemoryContentProvider} class provides a series of important information for the {@code ContentProviderProxy} class, as:
 * <ul>
 * <li>{@code db}: a hash map corresponding to all available content providers (such as the User CP, Training CP and Equipment CP)</li>
 * <li>{@code types}: a hash map with the variable type of each field in a single ContentProvider (for example, for the field "NAME" of the User CP will be returned a String type). 
 * The type of a field of a content provider can then be obtained with the method {@code getType(Uri fieldUri)} where {@code fieldUri} is the name of the field for which you want to get the type (eg. {@code User.NAME});</li>
 * </ul>
 * <p><b>NOTE</b>: The information contained in {@code InMemoryContentProvider} and then processed by the {@code ContentProviderProxy} class are volatile data contained in RAM and not contained in the DB. 
 * This means that data are deleted at shutdown and reloaded the next time.</p>
 * The {@code ContentProviderProxy} is instantiated when you start the application, then this operation is transparent to the user. 
 * In particular, the constructor of this class receives as input the Android context and the URI of the {@code InMemoryContentProvider}.
 * In the {@code onCreate()} method of {@code ContentProviderProxy} class many fields are instantiated. Main fields are:
 * <ul>
 * <li>{@code cache}: a hash map that represents a complete image of the original {@code ContentProviderProxy}, the {@code InMemoryContentProvider} class. This hash map contains all the content providers that developers can use. 
 * Whenever a field in a content provider is updated, this map is also updated.</li>
 * <li>{@code observers}: a map of the observers on the fields of a content provider for which you want to perform actions when they are modified.</li>
 * </ul>
 * <h2>How this class works</h2>
 * When the device is started, an instance of the {@code ContentProviderProxy} class is created. Into the {@code onCreate()} method some objects are instantiated and the private method {@code refresh()} is called; 
 * this method is then invoked in {@code onChange()} method of {@code ContentProviderProxy}. 
 * This means that for each set operation is performed on a field of a content provider, the {@code refresh()} method is called.
 * The {@code refresh()} method performs a series of operations that allow you to always get the latest data of the various content providers:
 * <ol>
 * <li>Executes a query on the original content provider, the {@code InMemoryContentProvider} class.</li>
 * <li>The query returns an Android MatrixCursor: If a field of a content provider is not present in those referenced by the {@code ContentProviderProxy}, or if its value is different from the previous value, 
 * it is added or updated into the cache Hash map.</li>
 * <li>For each changed / added field, is also updated the corresponding observer, if it is present into the observers Has map.</li>
 * </ol>
 * Inside the {@code ContentProviderProxy} are present some important methods:
 * <ul>
 * <li>{@code notifyOnChange}: this method allow to add an observer to those present in observers hash map, to keep track of changes on a field of a content provider. An example of its use is:
 * <pre>{@code user.notifyOnChange(User.MWAPPS_USERID, mwObserver)}</pre>
 * In this case we used the method on the {@code User} content provider, in particular for the field {@code MWAPPS_USERID}. The second parameter is the observer object to use; 
 * you can also specify a third parameter, a boolean value that indicates whether the observer must be immediately active or if it becomes active from the second event. 
 * If the third parameter is not specified, its value is True.
 * Steps to be taken to any modification of the field are specified in the {@code update()} method of the observer object passed to the {@code notifyOnChange()} method as input parameter.</li>
 * <li>
 * {@code removeNotifyOnChange()}: this method allows you to remove an observer from those in the observers hash map, so that you never have to keep track of changes this field
 * <p><b>NOTE</b>: It is good practice to call this method inside the method {@code onDestroy()} of the Activity in which it was previously called the {@code notifyOnChange()}.</p>
 * </li>
 * </ul>
 * Other important methods are the various get and set to read / write the values of the fields of content providers. 
 * For each data type of a field in a content provider there is a method get / set.
 */
public class ContentProviderProxy extends ContentObserver
{
	private ContentProviderClient		cp;
	private HashMap<String, String>		cache;
	private MultiMap<String, Observer>	observers;
	private Uri							uri_cp;
	protected Context					ctx;

	public ContentProviderProxy(Context _ctx, Uri uri)
	{
		super(new Handler(Looper.getMainLooper()));

		ctx = _ctx;

		observers = new MultiMap<String, Observer>();
		// ### va poi rilasciata la risorsa
		uri_cp = uri;
		cp = ctx.getContentResolver().acquireContentProviderClient(uri_cp);

		cache = new HashMap<String, String>();
		ctx.getContentResolver().registerContentObserver(uri_cp, true, this);
		_refresh();
		//		Log.w("CPPROXY","create " + uri);
	}

	/**
	 * Allows to add an observer to those present in observers hash map.
	 * @param columnName The field to observe
	 * @param o The observer object to add
	 */
	public synchronized void notifyOnChange(String columnName, Observer o)
	{
		observers.add(columnName, o);
		o.update();
	}

	/**
	 * Allows to add an observer to those present in observers hash map.
	 * @param columnName The field to observe
	 * @param o The observer object to add
	 * @param update Indicates whether the observer must be immediately active or if it becomes active from the second event
	 */
	public synchronized void notifyOnChange(String columnName, Observer o, boolean update)
	{
		observers.add(columnName, o);
		if (update)
			o.update();
	}

	/**
	 * Removes the observer for the observers has map
	 * @param columnName The field that the observer refer
	 * @param o The observer object to remove
	 */
	public synchronized void removeNotifyOnChange(String columnName, Observer o)
	{
		observers.remove(columnName, o);
	}

	/**
	 * Returns the type of the field of the content provider
	 * @param fieldUri The field of the content provider you want to know the type
	 * @return The type of the field
	 */
	public synchronized String getType(Uri fieldUri)
	{
		String type = "unknown";
		try
		{
			type = cp.getType(fieldUri);
			/*
			 * if(type.contentEquals("unknown")) { // get(fieldUri) // String[]
			 * data = res.path.split("#"); }
			 */
		} catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return type;
	}

	private synchronized void _refresh()
	{
		try
		{
			//BB		Log.w("CPPRX", "refresh " + uri_cp) ; 

			Cursor c = cp.query(uri_cp, null, null, null, null);

			if (c != null)
			{
				if (c.moveToFirst())
				{
					ArrayList<String> changed = new ArrayList<String>();
					int cn = c.getColumnCount();
					for (int i = 0; i < cn; ++i)
					{
						String s = c.getColumnName(i);
						String o = c.getString(i);

						if (!(cache.containsKey(s) && cache.get(s).equals(o)))
						{
							cache.put(s, o);
							//TODO viene aggiunto anche se c'e' un valore cambiato, quindi chiamato piu' volte
							changed.add(s);
						}
					}
					c.close();

					for (String s : changed)
					{
						// Collection<Observer> foo = observers.getValues(s);
						Object[] foo = observers.getValues(s).toArray();
						for (Object o : foo)
						{
							Observer fo = (Observer) o;
							if (fo != null)
							{
								//SS Log.w("CPPRX", "update " + uri_cp + " " + s) ; 
								fo.update();
							}
						}
					}
				} else
				{
					//SS Log.e("CPPRX", "can't move first") ;
					c.close();
				}

			} else
				Log.e("CPPRX", "cursor is null");
		} 
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Executes the refresh() method to update content providers and observers
	 */
	@Override
	public synchronized void onChange(boolean selfChange)
	{
		_refresh();
	}

	/**
	 * Get the int value for the field passed as Input. If the value of the field is not available or it is not an int type, the method returns -1. 
	 * @param fieldCode The field of the content provider you want to know the value
	 * @return The int value of the field. Returns -1 if the value is not available or the field is not an int type.
	 */
	public synchronized int getInt(String fieldCode)
	{
		try
		{
			String v = cache.get(fieldCode);
			if (v != null)
			{
				Double d = Double.parseDouble(v);
				return d.intValue();
			} 
			else
					return -1;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Get the long value for the field passed as Input. If the value of the field is not available or it is not a long type, the method returns -1. 
	 * @param fieldCode The field of the content provider you want to know the value
	 * @return The long value of the field. Returns -1 if the value is not available or the field is not an long type.
	 */
	public synchronized long getLong(String fieldCode)
	{
		try
		{
			String v = cache.get(fieldCode);
			if (v != null)
			{
				Double d = Double.parseDouble(v);
				return d.longValue();
			} 
			else
				return -1;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Get the float value for the field passed as Input. If the value of the field is not available or it is not an float type, the method returns -1. 
	 * @param fieldCode The field of the content provider you want to know the value
	 * @return The float value of the field. Returns -1 if the value is not available or the field is not an float type.
	 */
	public synchronized float getFloat(String fieldCode)
	{
		try
		{
			String v = cache.get(fieldCode);
			if (v != null)
				return Float.parseFloat(v);
			else
				return -1;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Get the double value for the field passed as Input. If the value of the field is not available or it is not an double type, the method returns -1. 
	 * @param fieldCode The field of the content provider you want to know the value
	 * @return The double value of the field. Returns -1 if the value is not available or the field is not an double type.
	 */
	public synchronized double getDouble(String fieldCode)
	{
		try
		{
			String v = cache.get(fieldCode);
			if (v != null)
				return Double.parseDouble(v);
			else
				return -1;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Get the String value for the field passed as Input. If the value of the field is not available or it is not a String type, the method returns an empty string. 
	 * @param fieldCode The field of the content provider you want to know the value
	 * @return The String value of the field. Returns an empty string if the value is not available or the field is not a String type.
	 */
	public synchronized String getString(String fieldCode)
	{
		try
		{
			String v = cache.get(fieldCode);
			if (v != null)
			{
				return v;
			} 
			else
				return "";
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Get the boolean value for the field passed as Input. If the value of the field is not available or it is not an boolean type, the method returns false. 
	 * @param fieldCode The field of the content provider you want to know the value
	 * @return The boolean value of the field. Returns false if the value is not available or the field is not an boolean type.
	 */
	public synchronized boolean getBoolean(String fieldCode)
	{
		try
		{
			String v = cache.get(fieldCode);
			if (v != null)
			{
				return Boolean.valueOf(v);
			} 
			else
				return false;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Get the byte[] value for the field passed as Input. If the value of the field is not available or it is not an byte[], the method returns null. 
	 * @param fieldCode The field of the content provider you want to know the value
	 * @return The byte[] array of the field. Returns null if the value is not available or the field is not a byte[] array.
	 */
	public synchronized byte[] getByteArray(String fieldCode)
	{
		try
		{
			String v = cache.get(fieldCode);
	
			if (v != null && !v.isEmpty())
			{
				// v ha formato "aa;23;f0"...
				String[] chunks = v.split(";");
				byte[] data = new byte[chunks.length];
				for (int i = 0; i < chunks.length; ++i)
				{
					byte b = (byte) Integer.parseInt(chunks[i], 16);
					data[i] = b;
				}
				return data;
			} 
			else
				return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Set the int value for the field passed as Input. This method causes an invocation on the onChange() method of the ContentProviderProxy. 
	 * @param fieldCode The field of the content provider you want to set the value
	 * @param value The int value for the field
	 */
	public synchronized void set(String fieldCode, int value)
	{
		ContentValues values = new ContentValues();
		values.put(fieldCode, value);

		try
		{
			cp.update(uri_cp, values, null, null);
			String type = cp.getType(Uri.parse(fieldCode));
			if (type == null)
				setType(fieldCode, Integer.class.getSimpleName());
		} 
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Set the long value for the field passed as Input. This method causes an invocation on the onChange() method of the ContentProviderProxy. 
	 * @param fieldCode The field of the content provider you want to set the value
	 * @param value The long value for the field
	 */
	public synchronized void set(String fieldCode, long value)
	{
		ContentValues values = new ContentValues();
		values.put(fieldCode, value);

		try
		{
			cp.update(uri_cp, values, null, null);
			String type = cp.getType(Uri.parse(fieldCode));
			if (type == null)
				setType(fieldCode, Long.class.getSimpleName());
		} 
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Set the float value for the field passed as Input. This method causes an invocation on the onChange() method of the ContentProviderProxy. 
	 * @param fieldCode The field of the content provider you want to set the value
	 * @param value The float value for the field
	 */
	public synchronized void set(String fieldCode, float value)
	{
		ContentValues values = new ContentValues();
		values.put(fieldCode, value);

		try
		{
			cp.update(uri_cp, values, null, null);
			String type = cp.getType(Uri.parse(fieldCode));
			if (type == null)
				setType(fieldCode, Float.class.getSimpleName());
		} 
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// SS LogUtil.d("TEST CP", "Data for tag " + fieldCode +
		// " successfully updated");
	}

	/**
	 * Set the double value for the field passed as Input. This method causes an invocation on the onChange() method of the ContentProviderProxy. 
	 * @param fieldCode The field of the content provider you want to set the value
	 * @param value The double value for the field
	 */
	public synchronized void set(String fieldCode, double value)
	{
		ContentValues values = new ContentValues();
		values.put(fieldCode, value);

		try
		{
			cp.update(uri_cp, values, null, null);
			String type = cp.getType(Uri.parse(fieldCode));
			if (type == null)
				setType(fieldCode, Double.class.getSimpleName());
		} 
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Set the String value for the field passed as Input. This method causes an invocation on the onChange() method of the ContentProviderProxy. 
	 * @param fieldCode The field of the content provider you want to set the value
	 * @param value The String value for the field
	 */
	public synchronized void set(String fieldCode, String value)
	{
		ContentValues values = new ContentValues();
		values.put(fieldCode, value);

		try
		{
			cp.update(uri_cp, values, null, null);
			String type = cp.getType(Uri.parse(fieldCode));
			if (type == null)
				setType(fieldCode, value.getClass().getSimpleName());
		} 
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Set the boolean value for the field passed as Input. This method causes an invocation on the onChange() method of the ContentProviderProxy. 
	 * @param fieldCode The field of the content provider you want to set the value
	 * @param value The boolean value for the field
	 */
	public synchronized void set(String fieldCode, boolean value)
	{
		ContentValues values = new ContentValues();
		values.put(fieldCode, value);

		try
		{
			cp.update(uri_cp, values, null, null);
			String type = cp.getType(Uri.parse(fieldCode));
			if (type == null)
				setType(fieldCode, Boolean.class.getSimpleName());
		} 
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized Sample getSimple(String fieldCode)
	{
		Sample sample = new Sample();
		sample.value = getDouble(fieldCode);
		sample.timestamp = getLong(fieldCode + "_TS");

		return sample;
	}

	public void setType(String fieldCode, String typeName)
	{
		ContentValues values = new ContentValues();
		values.put(fieldCode, typeName);
		try
		{
			Uri metaUri = Uri.parse(uri_cp.toString() + "#meta");
			cp.update(metaUri, values, null, null);
		} 
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public synchronized void set(String fieldCode, Sample sample)
	{
		ContentValues values = new ContentValues();
		// SS values.put(fieldCode, sample.toString());
		values.put(fieldCode, sample.value);
		values.put(fieldCode + "_TS", sample.timestamp);

		try
		{
			cp.update(uri_cp, values, null, null);
			String type = cp.getType(Uri.parse(fieldCode));
			if (type != null)
			{
				// SS LogUtil.w("CP_PROXY","TYPE " + type);
			} 
			else
			{
				// SS LogUtil.w("CP_PROXY","TYPE is null");
				setType(fieldCode, sample.getClass().getSimpleName());
			}
		} 
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Set the byte[] array for the field passed as Input. This method causes an invocation on the onChange() method of the ContentProviderProxy. 
	 * @param fieldCode The field of the content provider you want to set the value
	 * @param value The byte[] array for the field
	 */
	public synchronized void set(String fieldCode, byte[] value)
	{
		// DC: codifico il bytearray in una stringa in modo che venga gestito
		// correttamente dal CP
		// ..poi il getByteArray effetua la decodifica prima di restituire il
		// byte array
		
		try
		{
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < value.length; ++i)
			{
				sb.append(String.format("%02x", value[i]));
				sb.append(";");
			}
			String encoded = sb.toString();
	
			ContentValues values = new ContentValues();
			values.put(fieldCode, encoded);

			String type = cp.getType(Uri.parse(fieldCode));
			if (type == null)
				setType(fieldCode, "byte_array");

			cp.update(uri_cp, values, null, null);
		} 
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// SimpleDate --------------------------------------------------------------
	/**
	 * Get the SimpleDate object for the field passed as Input.
	 * @param fieldCode The field of the content provider you want to know the value
	 * @return The SimpleDate object for the field.
	 */
	public synchronized SimpleDate getDate(String fieldCode)
	{
		int year = getInt(fieldCode + "_YEAR");
		int month = getInt(fieldCode + "_MONTH");
		int day = getInt(fieldCode + "_DAY");

		return new SimpleDate(year, month, day);
	}

	/**
	 * Set the SimpleDate object for the field passed as Input. SimpleDate is a class for date representation.
	 * This method causes an invocation on the onChange() method of the ContentProviderProxy. 
	 * @param fieldCode The field of the content provider you want to set the value
	 * @param value The SimpleDate object for the field
	 */
	public synchronized void set(String fieldCode, SimpleDate d)
	{
		ContentValues values = new ContentValues();
		values.put(fieldCode + "_YEAR", d.getYear());
		values.put(fieldCode + "_MONTH", d.getMonth());
		values.put(fieldCode + "_DAY", d.getDay());

		try
		{
			cp.update(uri_cp, values, null, null);
			String type = cp.getType(Uri.parse(fieldCode));
			if (type == null)
				setType(fieldCode, d.getClass().getSimpleName());
		} 
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Get the value for a field without specify value type.
	 * @param fieldCode The field of the content provider you want to set the value
	 * @return A String corresponding the type of the field passed as input
	 */
	public synchronized String get(String fieldCode)
	{
		String v = cache.get(fieldCode);
		if (v != null)
		{
			// SS LogUtil.w("TEST CP", "Data for code " + fieldCode + " is " + v);
			return v;
		} else
			return (new String(""));
	}

	/**
	 * Call this method causes the release of the ContentProviderProxy, and cache and observers has maps are cleared.
	 */
	public void tearDown()
	{
		cp.release();
		ctx.getContentResolver().unregisterContentObserver(this);
		cache.clear();
		observers.clear();
		//		Log.i("CPPROXY","teardown " + uri_cp);
	}
}
