package it.rainbowbreeze.hackitaly13.logic;

import it.rainbowbreeze.hackitaly13.common.LogFacility;
import it.rainbowbreeze.hackitaly13.common.PlayerDirections;
import android.graphics.Point;

public class GameManager {
    private static final String LOG_HASH = GameManager.class.getSimpleName();
    
    public final static int FIELD_WIDTH = 100;
    public final static int FIELD_HEIGHT = 80;
    public static final byte EMPTY_FIELD = -1;
    public static final int MAX_PLAYERS = 3;

    public static int turn;
    
    public byte[][] playground;
    public double playerSpeeds[];
    public Point playerLastPositions[];
    public static PlayerDirections playerDirections[];
    
    
    private final LogFacility mLogFacility;
    
    
    
    
    public GameManager(LogFacility logFacility) {
        mLogFacility = logFacility;
    }
    
    
    public void resetFields() {
        turn = 0;
        playground = new byte[FIELD_WIDTH][FIELD_HEIGHT];
        playerSpeeds = new double[MAX_PLAYERS];
        playerDirections = new PlayerDirections[MAX_PLAYERS];
        playerLastPositions = new Point[MAX_PLAYERS];
        
        
        for (int x=0; x<FIELD_WIDTH; x++) {
            for(int y=0; y<FIELD_HEIGHT; y++) {
                playground[x][y] = EMPTY_FIELD;
            }
        }
        
        for (int player=0; player<MAX_PLAYERS; player++) {
            playerDirections[player] = PlayerDirections.UNKNOWN;
            playerSpeeds[player] = 0;
            int x, y;
            PlayerDirections dir;
            switch (player) {
            case 0:
                x = 0; y = FIELD_HEIGHT / 2; dir = PlayerDirections.RIGHT;
                break;
            case 1:
                x = FIELD_WIDTH / 2; y = FIELD_HEIGHT - 1; dir = PlayerDirections.UP;
                break;
            case 2:
                x = FIELD_WIDTH - 1; y = FIELD_HEIGHT / 2; dir = PlayerDirections.LEFT;
                break;
            case 3:
                x = FIELD_WIDTH / 2; y = FIELD_HEIGHT / 2; dir = PlayerDirections.DOWN;
                break;
            default:
                x = 0; y = 0; dir = PlayerDirections.UNKNOWN;
            }
            playerLastPositions[player] = new Point(x, y);
            playerDirections[player] = dir;
        }
    }
    
    
    public boolean movePlayer(int player, double speed) {
        movePlayer(player, playerDirections[player], speed);
        return checkForCollisions();
    }
    
    public boolean movePlayer(int player, PlayerDirections direction) {
        movePlayer(player, direction, playerSpeeds[player]);
        return checkForCollisions();
    }
    
    public boolean movePlayer(int player, PlayerDirections direction, double speed) {
        //updates the playground
        playerDirections[player] = direction;
        playerSpeeds[player] = speed;
        
        return checkForCollisions();
    }

    public boolean moveOneTurn() {
        //generates moves from other players
        turn++;
        
        if (turn == 20) {
            movePlayer(1, PlayerDirections.RIGHT);
        }
        if (turn == 40) {
            movePlayer(1, PlayerDirections.DOWN);
        }
        for (int player=0; player<MAX_PLAYERS; player++) {
            if (playerSpeeds[player] <= 0)  {
//                mLogFacility.v(LOG_HASH, "Skipping player " + player);
                continue;
            }
            int steps = ((int) (playerSpeeds[player] / 5)) + 1;
            //add steps
            mLogFacility.v(LOG_HASH, "Player " + player + " is moving by " + steps + " steps with direction " + playerDirections[player]);
            
            Point lastPos = playerLastPositions[player];
            switch (playerDirections[player]) {
            case DOWN:
                for(int i=1; i<=steps; i++) {
                    playground[lastPos.x][lastPos.y + i] = (byte) player;
                }
                playerLastPositions[player].y += steps;
                break;

            case UP:
                for(int i=0; i<=steps; i++) {
                    playground[lastPos.x][lastPos.y - i] = (byte) player;
                }
                playerLastPositions[player].y -= steps;
                break;
                
            case LEFT:
                for(int i=0; i<=steps; i++) {
                    playground[lastPos.x - i][lastPos.y] = (byte) player;
                }
                playerLastPositions[player].x -= steps;
                break;
                
            case RIGHT:
                for(int i=0; i<=steps; i++) {
                    playground[lastPos.x + i][lastPos.y] = (byte) player;
                }
                playerLastPositions[player].x += steps;
                break;
                
            default:
                break;
            }
            
            if (playerLastPositions[player].x < 0) playerLastPositions[player].x = 0;
            if (playerLastPositions[player].y < 0) playerLastPositions[player].y = 0;
            if (playerLastPositions[player].x >= FIELD_WIDTH) playerLastPositions[player].x = FIELD_WIDTH - 1;
            if (playerLastPositions[player].y >= FIELD_HEIGHT) playerLastPositions[player].y = FIELD_HEIGHT - 1;
            
        }
        
        return checkForCollisions();
    }


    private boolean checkForCollisions() {
        return false;
    }
}
