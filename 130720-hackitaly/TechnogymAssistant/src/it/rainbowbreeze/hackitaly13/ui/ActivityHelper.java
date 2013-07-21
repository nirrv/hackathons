/**
 * 
 */
package it.rainbowbreeze.hackitaly13.ui;

import android.app.Activity;
import android.content.Context;
import it.rainbowbreeze.libs.common.IRainbowLogFacility;
import it.rainbowbreeze.libs.ui.RainbowActivityHelper;

/**
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class ActivityHelper extends RainbowActivityHelper {

    // ------------------------------------------ Private Fields

    // -------------------------------------------- Constructors
    public ActivityHelper(IRainbowLogFacility logFacility, Context context) {
        super(logFacility, context);
    }

    // --------------------------------------- Public Properties

    // ------------------------------------------ Public Methods
    public void openMainGame(Activity callerActivity) {
        openActivity(callerActivity, MainGameActivity.class, null, false, REQUESTCODE_NONE);
    }

    // ----------------------------------------- Private Methods

    // ----------------------------------------- Private Classes
}
