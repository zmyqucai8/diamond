package com.zmy.diamond.utli.view;

import android.view.Gravity;

/**
 * Created by Sai on 15/8/9.
 */
public class AlertAnimateUtil {
    private static final int INVALID = -1;
    /**
     * Get default animation resource when not defined by the user
     *
     * @param gravity       the gravity of the dialog
     * @param isInAnimation determine if is in or out animation. true when is is
     * @return the id of the animation resource
     */
    static int getAnimationResource(int gravity, boolean isInAnimation) {
        switch (gravity) {
            case Gravity.BOTTOM:
                return isInAnimation ? com.bigkoo.alertview.R.anim.slide_in_bottom : com.bigkoo.alertview.R.anim.slide_out_bottom;
            case Gravity.CENTER:
                return isInAnimation ? com.bigkoo.alertview.R.anim.fade_in_center : com.bigkoo.alertview.R.anim.fade_out_center;
        }
        return INVALID;
    }
}
