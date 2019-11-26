package com.vince.cd;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import static android.view.View.VISIBLE;

/**
 * Created by vince on 2017-2-28.
 */

public class ViewUtils {
    public static boolean isMiddleViewVisible(View view, View parent) {
        if (null == view || null == parent) {
            return false;
        }

        Object v = view;
        while (v instanceof View) {
            if (VISIBLE != ((View) v).getVisibility()) {
                return false;
            }

            if (v == parent) {
                return true;
            }

            v = ((View) v).getParent();
        }

        return false;
    }

    //activty是否ready, 根据android.R.id.content宽度和高度来判断
    public static boolean isContentViewOk(Activity activity) {
        ViewGroup viewGroup = (ViewGroup) activity.findViewById(android.R.id.content);
        return viewGroup.getWidth() != 0 || viewGroup.getHeight() != 0;
    }
}
