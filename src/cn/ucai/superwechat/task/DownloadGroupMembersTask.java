package cn.ucai.superwechat.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ucai.superwechat.I;
import cn.ucai.superwechat.SuperWeChatApplication;
import cn.ucai.superwechat.bean.MemberUserAvatar;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.bean.UserAvatar;
import cn.ucai.superwechat.utils.OkHttpUtils2;
import cn.ucai.superwechat.utils.Utils;

/**
 * Created by Administrator on 2016/7/22.
 */
public class DownloadGroupMembersTask {
    String hxid;
    private static final String TAG = DownloadGroupMembersTask.class.getSimpleName();
    Context mContext;

    public DownloadGroupMembersTask(String hxid, Context context) {
        this.mContext = context;
        this.hxid = hxid;
    }
    public void execute() {
        final OkHttpUtils2<String> utils2 = new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_DOWNLOAD_GROUP_MEMBERS_BY_HXID)
                .addParam(I.Member.GROUP_HX_ID,hxid)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Result fromJson = Utils.getListResultFromJson(result, MemberUserAvatar.class);
                        Log.e(TAG, "result=" + fromJson);
                        List<MemberUserAvatar> list = (List<MemberUserAvatar>) fromJson.getRetData();
                        if (list != null && list.size() > 0) {
                            Log.e(TAG, "size=" + list.size());
                            /**
                             * 自己的方法实现
                             */
                            Map<String, MemberUserAvatar> members = new HashMap<String, MemberUserAvatar>();
                            Map<String, Map<String, MemberUserAvatar>> allMembers = new HashMap<String, Map<String, MemberUserAvatar>>();
                            for (MemberUserAvatar u:list) {
                                members.put(u.getMUserName(), u);
                            }
                            allMembers.put(hxid, members);
                            SuperWeChatApplication.getInstance().setGroupMembers(allMembers);
                            mContext.sendStickyBroadcast(new Intent("update_member_list"));
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "error="+error);

                    }
                });
    }
}
