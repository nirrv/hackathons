package it.rainbowbreeze.hackitaly13.ui;

import it.rainbowbreeze.hackitaly13.R;
import it.rainbowbreeze.hackitaly13.common.AppEnv;
import it.rainbowbreeze.hackitaly13.common.LogFacility;
import it.rainbowbreeze.technogym.realtime.logic.GymActivityManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainGameActivity extends Activity
{
    private LogFacility mLogFacility;
    private GymActivityManager mGymActivityManager;
    private TextView mLblCollisions;
    private ImageView mBtnSpeedUp;
    private ImageView mBtnSpeedDown;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AppEnv appEnv = AppEnv.i(getApplicationContext());
		mLogFacility = appEnv.getLogFacility();
		mGymActivityManager = appEnv.getGymActivityManager();
		
		setContentView(R.layout.act_maingame);
		mLblCollisions = (TextView) findViewById(R.id.maingame_lblCollisions);
		mBtnSpeedUp = (ImageView) findViewById(R.id.maingame_btnSpeedUp);
		mBtnSpeedDown = (ImageView) findViewById(R.id.maingame_btnSpeedDown);
		
		mBtnSpeedUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseSpeed();
            }
        });
		
		mBtnSpeedDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseSpeed();
            }
        });
	}


    protected void increaseSpeed() {
        new Thread(new Runnable() {            
            @Override
            public void run() {
                mGymActivityManager.increaseSpeed(getApplicationContext());
                checkSpeed();
            }
        }).start();
    }


    protected void decreaseSpeed() {
        new Thread(new Runnable() {            
            @Override
            public void run() {
                mGymActivityManager.decreaseSpeed(getApplicationContext());
                checkSpeed();
            }
        }).start();
    }
	
    protected void checkSpeed() {
        // TODO Auto-generated method stub
        
    }

}
