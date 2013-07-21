package it.rainbowbreeze.hackitaly13.ui;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import it.rainbowbreeze.hackitaly13.R;
import it.rainbowbreeze.hackitaly13.common.AppEnv;
import it.rainbowbreeze.hackitaly13.common.LogFacility;
import it.rainbowbreeze.hackitaly13.common.PlayerDirections;
import it.rainbowbreeze.hackitaly13.logic.GameManager;
import it.rainbowbreeze.technogym.realtime.logic.GymActivityManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainGameActivity extends Activity {
    private static final String LOG_HASH = MainGameActivity.class.getSimpleName();
    
    
    private LogFacility mLogFacility;
    private GymActivityManager mGymActivityManager;
    private GestureDetector mGestureDetector;
    private MyGestureListener mOnGestureListener;
    private GameManager mGameManager;
    
    private TextView mLblCollisions;
    private ImageView mBtnSpeedUp;
    private ImageView mBtnSpeedDown;
    private TronFieldView mTronFieldView;
    
    private PlayerDirections mLastDirection;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AppEnv appEnv = AppEnv.i(getApplicationContext());
		mLogFacility = appEnv.getLogFacility();
		mGymActivityManager = appEnv.getGymActivityManager();
		mOnGestureListener = new MyGestureListener();
		mGestureDetector = new GestureDetector(mOnGestureListener);
		mGameManager = new GameManager(mLogFacility);
		
		setContentView(R.layout.act_maingame);
		mLblCollisions = (TextView) findViewById(R.id.maingame_lblCollisions);
		mBtnSpeedUp = (ImageView) findViewById(R.id.maingame_btnSpeedUp);
		mBtnSpeedDown = (ImageView) findViewById(R.id.maingame_btnSpeedDown);
		mTronFieldView = (TronFieldView) findViewById(R.id.maingame_vwMap);
		
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
		
		startWorkout();
		startGame();
	}
	
	private void startGame() {
	    mGameManager.resetFields();
	    mGameManager.movePlayer(0, 5);
        mGameManager.movePlayer(1, 10);
        mLastDirection =  PlayerDirections.RIGHT;
	    
        mLogFacility.v(LOG_HASH, "Starting scheduled service for uploading data from the application");
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(new Runnable() {
                    public void run() {
                        updateGameData();
                    }
                }, 0, 1, TimeUnit.SECONDS);
    }

    protected void updateGameData() {
        mLogFacility.v(LOG_HASH, "Updating data for turn " + mGameManager.turn);
        mGameManager.moveOneTurn();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mLblCollisions.setText("" + mGameManager.turn);
                mTronFieldView.setPlayfield(mGameManager.playground);
                mTronFieldView.invalidate();
            }
        });
    }

    private void startWorkout() {
        mLogFacility.v(LOG_HASH, "Starting workout");
        mGymActivityManager.startWorkout(getApplicationContext());
    }

    @Override
	public boolean onTouchEvent(MotionEvent event) {
	    return mGestureDetector.onTouchEvent(event);
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
        double speed = mGymActivityManager.getSpeed(this);
        mGameManager.movePlayer(0, speed);
        //TODO write speed
    }
    
    protected void changeDirection(final PlayerDirections newDirection) {
        //TODO direction unknown management
        mLogFacility.v(LOG_HASH, "Last: " + mLastDirection + ", new: " + newDirection);
        
        if ((mLastDirection == PlayerDirections.DOWN || mLastDirection == PlayerDirections.UP) &&
                (newDirection == PlayerDirections.DOWN || newDirection == PlayerDirections.UP)) {
            mLogFacility.v(LOG_HASH, "Cannot move on the same direction up/down");
        }
        else if ((mLastDirection == PlayerDirections.LEFT || mLastDirection == PlayerDirections.RIGHT) &&
                (newDirection == PlayerDirections.LEFT || newDirection == PlayerDirections.RIGHT)) {
            mLogFacility.v(LOG_HASH, "Cannot move on the same direction left/right");
        }
        
        (new Thread(new Runnable() {
            @Override
            public void run() {
                //change move
                if (mLastDirection == PlayerDirections.DOWN || mLastDirection == PlayerDirections.UP) {
                    if (newDirection == PlayerDirections.LEFT) {
                        mGymActivityManager.turnLeft(getApplicationContext());
                        mGameManager.movePlayer(0, PlayerDirections.LEFT);
                    } else {
                        mGymActivityManager.turnRight(getApplicationContext());
                        mGameManager.movePlayer(0, PlayerDirections.RIGHT);
                    }
                }
                if (mLastDirection == PlayerDirections.LEFT || mLastDirection == PlayerDirections.RIGHT) {
                    if (newDirection == PlayerDirections.DOWN) {
                        mGymActivityManager.turnLeft(getApplicationContext());
                        mGameManager.movePlayer(0, PlayerDirections.DOWN);
                    } else {
                        mGymActivityManager.turnRight(getApplicationContext());
                        mGameManager.movePlayer(0, PlayerDirections.UP);
                    }
                }
                mLastDirection = newDirection;
            }
        })).start();
    }
    
    
    private class MyGestureListener implements OnGestureListener {
        private static final float TOLERANCEX = 100;
        private static final float TOLERANCEY = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {
            boolean processed = false;
            mLogFacility.v(LOG_HASH, "OnFling");
            if (Math.abs(e1.getRawY() - e2.getRawY()) > TOLERANCEY) {
                if (e1.getRawY() < e2.getRawY()) {
                    mLogFacility.v(LOG_HASH, "Downscroll");
                    changeDirection(PlayerDirections.DOWN);
                } else {
                    mLogFacility.v(LOG_HASH, "Upscroll");
                    changeDirection(PlayerDirections.UP);
                }
                processed = true;
            }
            
            else if (Math.abs(e1.getRawX() - e2.getRawX()) > TOLERANCEX) {
                if (e1.getRawX() < e2.getRawX()) {
                    mLogFacility.v(LOG_HASH, "Rightscroll");
                    changeDirection(PlayerDirections.RIGHT);
                } else {
                    mLogFacility.v(LOG_HASH, "Leftscroll");
                    changeDirection(PlayerDirections.LEFT);
                }
                processed = true;
            }

            return processed;
        }

        @Override
        public void onLongPress(MotionEvent e) {}

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {}

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
        
        protected void updateGameData() {
            //updates data on training
            
            //updates game data
        }


    }

}
