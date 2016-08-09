package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;
import java.util.Map;

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.utils.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/7/22.
 */
public class DownloadCollectCountTask {
    String username;
    private static final String TAG = DownloadCollectCountTask.class.getSimpleName();
    Context mContext;

    public DownloadCollectCountTask(String username, Context context) {
        this.mContext = context;
        this.username = username;
    }

    public void execute() {
        final OkHttpUtils2<MessageBean> utils2 = new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_FIND_COLLECT_COUNT)
                .addParam("userName",username)
                .targetClass(MessageBean.class)
                .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean msg) {
                        Log.e(TAG, "result=" + msg);
                        if (msg != null) {
                            if (msg.isSuccess()) {
                                FuLiCenterApplication.getInstance().setCollectCount(Integer.parseInt(msg.getMsg()));
                            } else {
                                FuLiCenterApplication.getInstance().setCollectCount(0);
                            }
                            mContext.sendStickyBroadcast(new Intent("update_collect"));

                        }

                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "error="+error);

                    }
                });
    }
}
