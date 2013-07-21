/**
 * 
 */
package it.rainbowbreeze.hackitaly13.ui;

import it.rainbowbreeze.hackitaly13.R;
import it.rainbowbreeze.hackitaly13.common.AppEnv;
import it.rainbowbreeze.hackitaly13.common.LogFacility;
import it.rainbowbreeze.hackitaly13.logic.GameManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author alfredomorresi
 *
 */
public class TronFieldView extends View {
    private final static String LOG_HASH = TronFieldView.class.getSimpleName();
    
    private LogFacility mLogFacility;
    
    private final static int FIELD_WIDTH = GameManager.FIELD_WIDTH;
    private final static int FIELD_HEIGHT = GameManager.FIELD_HEIGHT;
    private static final int MAX_PLAYERS = GameManager.MAX_PLAYERS;
    private static final byte EMPTY_FIELD = -1;

    private static final int TAIL_HORIZONTAL = 1;
    private static final int TAIL_VERTICAL = 2;
    private static final int TAIL_UPRIGHT = 3;
    private static final int TAIL_UPLEFT = 4;
    private static final int TAIL_DOWNRIGHT = 5;
    private static final int TAIL_DOWNLEFT = 6;
    private static final int TAIL_RIGHTUP = 7;
    private static final int TAIL_RIGHTDOWN = 8;
    private static final int TAIL_LEFTUP = 9;
    private static final int TAIL_LEFTDOWN = 10;
    
    private byte[][] mPlayfield = new byte[FIELD_WIDTH][FIELD_HEIGHT];
    private final Bitmap[][] mTails = new Bitmap[10][MAX_PLAYERS];
    private final Paint[] mPlayerPaints = new Paint[MAX_PLAYERS];
    private Paint mBitmapPaint;
    
    public TronFieldView(Context context) { 
        super(context);
        initVars(context);
    }

    public TronFieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVars(context);
    }

    public TronFieldView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initVars(context);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        mLogFacility.v(LOG_HASH, "Start onDraw");
        
        //maps playfield on image
        final int blockWidth = getWidth() / FIELD_WIDTH;
        final int blockHeight = getHeight() / FIELD_HEIGHT;
//        mLogFacility.v(LOG_HASH, String.format("width:%s, height:%s", blockWidth, blockHeight));
        
        //start scanning
        for (int player=0; player<MAX_PLAYERS; player++) {
            for (int x=0; x<FIELD_WIDTH; x++) {
                for(int y=0; y<FIELD_HEIGHT; y++) {
                    if (EMPTY_FIELD != mPlayfield[x][y]) {
                        canvas.drawRect(x * blockWidth, y * blockHeight, (x + 1) * blockWidth, (y + 1) * blockHeight,
                                mPlayerPaints[mPlayfield[x][y]]);
                    }
//                    Bitmap tail = findTailImage(x, y, player);
//                    if (null != tail) {
//                        mLogFacility.v(LOG_HASH, String.format("Draw at x:%s, y:%s", x, y));
//                        canvas.drawBitmap(tail, 16*x, 16*y, mBitmapPaint);
//                    }
                }
            }
        }
        
//        mLogFacility.v(LOG_HASH, "End onDraw");
    }

    private void initVars(Context context) {
        mLogFacility = AppEnv.i(context).getLogFacility();
        
        for (int x=0; x<FIELD_WIDTH; x++) {
            for(int y=0; y<FIELD_HEIGHT; y++) {
                mPlayfield[x][y] = EMPTY_FIELD;
            }
        }
//        mPlayfield[3][1] = 0;
//        mPlayfield[4][1] = 0;
//        mPlayfield[5][1] = 0;
//        mPlayfield[6][1] = 0;
//        mPlayfield[7][1] = 0;
//        mPlayfield[8][1] = 0;
//        
//        mPlayfield[6][1] = 1;
//        mPlayfield[6][2] = 1;
//        mPlayfield[6][3] = 1;
//        mPlayfield[6][4] = 1;
//        mPlayfield[6][5] = 1;
//        mPlayfield[6][6] = 1;

        Resources res = context.getResources();
        mBitmapPaint = new Paint();
        mBitmapPaint.setFilterBitmap(true);
        
        for (int player=0; player<MAX_PLAYERS; player++) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            switch (player) {
            case 0:
                paint.setColor(res.getColor(android.R.color.white));
                break;
            case 1:
                paint.setColor(res.getColor(android.R.color.holo_green_light));
                break;
            case 2:
                paint.setColor(res.getColor(android.R.color.holo_orange_light));
                break;
            case 3:
                paint.setColor(res.getColor(android.R.color.holo_purple));
                break;
            default:
                paint.setColor(res.getColor(android.R.color.black));
            }
            mPlayerPaints[player] = paint;
        }
        
//        int player = 0;
//        mTails[TAIL_HORIZONTAL][player] = BitmapFactory.decodeResource(res, R.drawable.tail_blue_horizonal);
//        mTails[TAIL_VERTICAL][player] = BitmapFactory.decodeResource(res, R.drawable.tail_blue_vertical);
//        mTails[TAIL_UPRIGHT][player] = BitmapFactory.decodeResource(res, R.drawable.tail_blue_upright);
//        mTails[TAIL_RIGHTDOWN][player] = BitmapFactory.decodeResource(res, R.drawable.tail_blue_rightdown);
//        mTails[TAIL_RIGHTUP][player] = BitmapFactory.decodeResource(res, R.drawable.tail_blue_rightup);
    }
    
    public void setPlayfield(byte[][] playfield) {
        mPlayfield = playfield;
    }
    
    /**
     * Scans work from left to right and from top to bottom
     *   0,0  ------  
     *    -  -------
     *    -  ------- MaxX, MaxY
     *    
     * @param x
     * @param y
     * @param player
     * @return
     */
    private Bitmap findTailImage(int x, int y, int player) {
        if (x > 0 && x < (FIELD_WIDTH - 1) && y > 0 && y < (FIELD_HEIGHT -1)) {
            if (checkTuple(mPlayfield[x-1][y], mPlayfield[x][y], mPlayfield[x+1][y], player))
                    return mTails[TAIL_HORIZONTAL][player];
            if (checkTuple(mPlayfield[x][y-1], mPlayfield[x][y], mPlayfield[x][y+1], player))
                return mTails[TAIL_VERTICAL][player];
            if (checkTuple(mPlayfield[x][y-1], mPlayfield[x][y], mPlayfield[x+1][y], player))
                return mTails[TAIL_DOWNRIGHT][player];
        } else if (x > 0)
        //searches for near moves of the same player
        if (0==x && 0==y) {
            
        }
        
        return null;
    }
    
    private boolean checkTuple(int int1, int int2, int int3, int player) {
        return int1 == int2 && int2 == int3 && int3 == player;
    }

}
