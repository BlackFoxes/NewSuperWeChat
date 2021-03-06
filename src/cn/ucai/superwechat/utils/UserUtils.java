package cn.ucai.superwechat.utils;

import android.content.Context;
import android.content.Entity;
import android.text.TextUtils;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ucai.superwechat.I;
import cn.ucai.superwechat.SuperWeChatApplication;
import cn.ucai.superwechat.applib.controller.HXSDKHelper;
import cn.ucai.superwechat.DemoHXSDKHelper;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.bean.MemberUserAvatar;
import cn.ucai.superwechat.bean.UserAvatar;
import cn.ucai.superwechat.domain.User;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class UserUtils {
	private static final String TAG = UserUtils.class.getSimpleName();

	/**
     * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
     * @param username
     * @return
     */
    public static User getUserInfo(String username){
        User user = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList().get(username);
        if(user == null){
            user = new User(username);
        }
            
        if(user != null){
            //demo没有这些数据，临时填充
        	if(TextUtils.isEmpty(user.getNick()))
        		user.setNick(username);
        }
        return user;
    }
	public static UserAvatar getAppUserInfo(String username){
		UserAvatar user = SuperWeChatApplication.getInstance().getUserAvatarMap().get(username);
		Log.e(TAG, "getAppUserInfo,user=" + user);
		if(SuperWeChatApplication.getInstance().getUserAvatarMap().get(username) == null){
			user = new UserAvatar();
		}

		return user;
	}
    
    /**
     * 设置用户头像
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
    	User user = getUserInfo(username);
        if(user != null && user.getAvatar() != null){
            Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.default_avatar).into(imageView);
        }else{
            Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
        }
    }
	public static void setAppUserAvatar(Context context, String username, ImageView imageView){
		String path = "";
		if(path != null && username != null){
			path = getUserAvatarPath(username);
			Log.e("UserUtils", "path="+path);
			Picasso.with(context).load(path).placeholder(R.drawable.default_avatar).into(imageView);
		}else{
			Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
		}
	}
	public static String getUserAvatarPath(String username) {
		StringBuilder path = new StringBuilder(I.SERVER_ROOT);
		path.append(I.QUESTION).append(I.KEY_REQUEST)
				.append(I.EQL).append(I.REQUEST_DOWNLOAD_AVATAR)
				.append(I.AND)
				.append(I.NAME_OR_HXID).append(I.EQL)
				.append(username)
				.append(I.AND)
				.append(I.AVATAR_TYPE)
				.append(I.EQL)
				.append(I.AVATAR_TYPE_USER_PATH);
		return path.toString();


	}



	public static void setAppGroupAvatar(Context context, String hxid, ImageView imageView){
		String path = "";
		if(path != null && hxid != null){
			path = getGroupAvatarPath(hxid);
			Log.e("UserUtils", "path="+path);
			Picasso.with(context).load(path).placeholder(R.drawable.default_avatar).into(imageView);
		}else{
			Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
		}
	}
	public static String getGroupAvatarPath(String hxid) {
		StringBuilder path = new StringBuilder(I.SERVER_ROOT);
		path.append(I.QUESTION).append(I.KEY_REQUEST)
				.append(I.EQL).append(I.REQUEST_DOWNLOAD_AVATAR)
				.append(I.AND)
				.append(I.NAME_OR_HXID).append(I.EQL)
				.append(hxid)
				.append(I.AND)
				.append(I.AVATAR_TYPE)
				.append(I.EQL)
				.append(I.AVATAR_TYPE_GROUP_PATH);
		return path.toString();


	}


	/**
     * 设置当前用户头像
     */
	public static void setCurrentUserAvatar(Context context, ImageView imageView) {
		User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
		if (user != null && user.getAvatar() != null) {
			Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.default_avatar).into(imageView);
		} else {
			Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
		}
	}

	public static void setAppCurrentUserAvatar(Context context, ImageView imageView) {
		String userName = SuperWeChatApplication.getInstance().getUserName();
		setAppUserAvatar(context,userName,imageView);
	}
    
    /**
     * 设置用户好友列表昵称
     */
    public static void setAppUserNick(String username,TextView textView){
		Log.e(TAG, "setAppUserNick,username=" + username);
		UserAvatar user = getAppUserInfo(username);
		if (user.getMUserNick() != null) {
			setAppUserNickByUserAvatar(user, textView);
		} else {

			textView.setText(username);
		}
	}
	public static void setAppUserNickByUserAvatar(UserAvatar user,TextView textView){
		if (user != null) {
			if (user.getMUserNick() != null) {
				textView.setText(user.getMUserNick());
			} else {
				textView.setText(user.getMUserName());
			}
		}
	}
    /**
     * 设置当前用户昵称
     */
    public static void setCurrentUserNick(TextView textView){
    	User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
    	if(textView != null){
    		textView.setText(user.getNick());
    	}
    }
	public static void setAppCurrentUserNick(TextView textView){
		UserAvatar user = SuperWeChatApplication.getInstance().getUserAvatar();
		if (textView != null && user != null) {
			if (user.getMUserNick() != null) {

				textView.setText(user.getMUserNick());
			}else {
				textView.setText(user.getMUserName());
			}
		}
	}

	/**
	 * 设置用户的昵称
	 *
	 * @param username
	 * @param textView
     */
	public static void setUserNick(String username,TextView textView){
		User user = getUserInfo(username);
		if(user != null){
			textView.setText(user.getNick());
		}else{
			textView.setText(username);
		}
	}
    
    /**
     * 保存或更新某个用户
     * @param
     */
	public static void saveUserInfo(User newUser) {
		if (newUser == null || newUser.getUsername() == null) {
			return;
		}
		((DemoHXSDKHelper) HXSDKHelper.getInstance()).saveContact(newUser);
	}

	/**
	 * 设置群组成员的昵称
	 * @param hxid
	 * @param username
	 * @param
     */
	public static MemberUserAvatar getMemberInfo(String hxid,String username){
		Map<String, Map<String, MemberUserAvatar>> groupMembers = SuperWeChatApplication.getInstance().getGroupMembers();
		MemberUserAvatar memberUserAvatar = null;
		if (groupMembers != null) {
			 memberUserAvatar =groupMembers.get(hxid).get(username);
		}
		return memberUserAvatar;
	}

	public static void setAppMemberNick(String hxid, String username, TextView tvNickName) {
		MemberUserAvatar memberInfo = getMemberInfo(hxid, username);
		tvNickName.setText(memberInfo.getMUserNick());
	}
}
