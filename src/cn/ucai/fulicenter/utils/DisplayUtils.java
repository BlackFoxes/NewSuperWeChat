package cn.ucai.fulicenter.utils;

import android.app.Activity;
import android.view.View;

import cn.ucai.fulicenter.R;

/**
 * Created by Administrator on 2016/8/3.
 */
public class DisplayUtils {
    public static void initBack(final Activity activity) {
        activity.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();

            }
        });

    }
}