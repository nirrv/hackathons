package it.rainbowbreeze.hackitaly13.ui;

import it.rainbowbreeze.hackitaly13.R;
import it.rainbowbreeze.hackitaly13.common.AppEnv;
import it.rainbowbreeze.hackitaly13.common.LogFacility;
import it.rainbowbreeze.technogym.realtime.logic.GymActivityManager;
import it.rainbowbreeze.technogym.realtime.logic.RoomStateManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class RoomSetupActivity extends Activity {
    private static final String LOG_HASH = RoomSetupActivity.class.getSimpleName();
    
    private LogFacility mLogFacility;
    private GymActivityManager mGymActivityManager;
    private ActivityHelper mActivityHelper;
    private ImageView mBtnStart;
    private int phase = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AppEnv appEnv = AppEnv.i(getApplicationContext());
		mLogFacility = appEnv.getLogFacility();
		mGymActivityManager = appEnv.getGymActivityManager();
		mActivityHelper = appEnv.getActivityHelper();
		
		setContentView(R.layout.act_roomsetup);
		
		mBtnStart = (ImageView) findViewById(R.id.roomsetup_btnStart);
		mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (phase) {
                case 1:
                    mBtnStart.setImageResource(R.drawable.gioco_v4_c);
                    phase++;
                    break;

                case 2:
                    mActivityHelper.openMainGame(RoomSetupActivity.this);
                    break;
                default:
                    break;
                }
            }
        });
		
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

}
