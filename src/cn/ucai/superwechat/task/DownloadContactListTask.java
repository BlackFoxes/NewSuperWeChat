package cn.ucai.superwechat.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import cn.ucai.superwechat.I;
import cn.ucai.superwechat.SuperWeChatApplication;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.bean.UserAvatar;
import cn.ucai.superwechat.utils.OkHttpUtils2;
import cn.ucai.superwechat.utils.Utils;

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
                        Result fromJson = Utils.getListResultFromJson(result, UserAvatar.class);
                        Log.e(TAG, "result=" + fromJson);
                        List<UserAvatar> list = (List<UserAvatar>) fromJson.getRetData();
                        if (list != null && list.size() > 0) {
                            Log.e(TAG, "size=" + list.size());
                            SuperWeChatApplication.getInstance().setUserList(list);
                            mContext.sendStickyBroadcast(new Intent("update_contact_list"));


                        }

                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "error="+error);

                    }
                });
    }
}
