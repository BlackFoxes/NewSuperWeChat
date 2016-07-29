package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;
import java.util.Map;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.SuperWeChatApplication;
import cn.ucai.fulicenter.bean.GroupAvatar;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.utils.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/7/26.
 */
public class DownloadGroupListTask {
    String username;
    private static final String TAG = DownloadContactListTask.class.getSimpleName();
    Context mContext;

    public DownloadGroupListTask(String username,Context context) {
        this.mContext = context;
        this.username = username;
    }

    public void execute() {
        Log.e(TAG, "execute");
        final OkHttpUtils2<String> utils2 = new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_FIND_GROUP_BY_USER_NAME)
                .addParam(I.User.USER_NAME,username)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Result fromJson = Utils.getListResultFromJson(result, GroupAvatar.class);
                        Log.e(TAG, "result=" + fromJson);
                        List<GroupAvatar>list  = (List<GroupAvatar>) fromJson.getRetData();
                        if (list != null && list.size() > 0) {
                            Log.e(TAG, "size=" + list.size());
                            SuperWeChatApplication.getInstance().setGrouplist (list);
                            Map<String, GroupAvatar> groupAvatarMap = SuperWeChatApplication.getInstance().getGroupAvatarMap();
                            for (GroupAvatar groupAvatar : list) {
                                groupAvatarMap.put(String.valueOf(groupAvatar.getMGroupHxid()), groupAvatar);
                            }
                            mContext.sendStickyBroadcast(new Intent("update_group_list"));
                        }

                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "error="+error);

                    }
                });
    }
}
