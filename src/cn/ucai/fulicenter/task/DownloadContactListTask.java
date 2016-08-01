package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;
import java.util.Map;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.utils.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/7/22.
 */
public class DownloadContactListTask {
    String username;
    private static final String TAG = DownloadContactListTask.class.getSimpleName();
    Context mContext;

    public DownloadContactListTask(String username,Context context) {
        this.mContext = context;
        this.username = username;
    }

    public void execute() {
        final OkHttpUtils2<String> utils2 = new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_DOWNLOAD_CONTACT_ALL_LIST)
                .addParam(I.Contact.USER_NAME,username)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG, "result=" + result);
                        if (result != null) {

                            Result fromJson = Utils.getListResultFromJson(result, UserAvatar.class);
                            Log.e(TAG, "fromJson=" + fromJson);
                            if (fromJson != null) {

                                List<UserAvatar> list = (List<UserAvatar>) fromJson.getRetData();
                                if (list != null && list.size() > 0) {
                                    Log.e(TAG, "size=" + list.size());
                                    FuLiCenterApplication.getInstance().setUserList (list);
                                    Map<String, UserAvatar> userAvatarMap = FuLiCenterApplication.getInstance().getUserAvatarMap();
                                    for (UserAvatar u:list) {
                                        userAvatarMap.put(u.getMUserName(), u);

                                    }
                                    mContext.sendStickyBroadcast(new Intent("update_contact_list"));


                                }
                            }
                        }

                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "error="+error);

                    }
                });
    }
}
