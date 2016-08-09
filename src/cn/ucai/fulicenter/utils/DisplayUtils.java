package cn.ucai.fulicenter.utils;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

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
    public static void initBackWithTitle(final Activity activity,String title) {

        ((TextView)activity.findViewById(R.id.tv_back)).setText(title);
        activity.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();

            }
        });


    }
}
