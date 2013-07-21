package it.rainbowbreeze.hackitaly13.ui;

import it.rainbowbreeze.hackitaly13.R;
import it.rainbowbreeze.hackitaly13.common.AppEnv;
import it.rainbowbreeze.technogym.realtime.logic.RoomStateManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class IntroActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_intro);
		
		Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
//              GymActivityManager gymActiviyManager = AppEnv.i(getApplicationContext()).getGymActivityManager();
                RoomStateManager roomStateManager = AppEnv.i(getApplicationContext()).createRoomStateManager(1000);
                roomStateManager.getRoomState(getApplicationContext());
            }
        });
		t.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.intro, menu);
		return true;
	}

}
