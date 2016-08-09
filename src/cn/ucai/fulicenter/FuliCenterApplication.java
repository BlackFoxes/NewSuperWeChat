/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ucai.fulicenter;

import android.app.Application;
import android.content.Context;

import com.easemob.EMCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.MemberUserAvatar;
import cn.ucai.fulicenter.bean.UserAvatar;

public class FuLiCenterApplication extends Application {

	public static final String SERVER_ROOT ="http://10.0.2.2:8080/FuLiCenterServer/Server" ;
	public static Context applicationContext;
	private static FuLiCenterApplication instance;
	// login user name
	public final String PREF_USERNAME = "username";

	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";
	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

	@Override
	public void onCreate() {
		super.onCreate();
        applicationContext = this;
        instance = this;

        /**
         * this function will initialize the HuanXin SDK
         *
         * @return boolean true if caller can continue to call HuanXin related APIs after calling onInit, otherwise false.
         *
         * 环信初始化SDK帮助函数
         * 返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
         *
         * for example:
         * 例子：
         *
         * public class DemoHXSDKHelper extends HXSDKHelper
         *
         * HXHelper = new DemoHXSDKHelper();
         * if(HXHelper.onInit(context)){
         *     // do HuanXin related work
         * }
         */
        hxSDKHelper.onInit(applicationContext);
	}

	public static FuLiCenterApplication getInstance() {
		return instance;
	}


	/**
	 * 获取当前登陆用户名
	 *
	 * @return
	 */
	public String getUserName() {
	    return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 *
	 * @return
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 *
	 * @param
	 */
	public void setUserName(String username) {
	    hxSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 *
	 * @param pwd
	 */
	public void setPassword(String pwd) {
	    hxSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final boolean isGCM,final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
	    hxSDKHelper.logout(isGCM,emCallBack);
	}

	/**
	 * 全局变量中的用户信息
	 */
	private UserAvatar userAvatar;

	public UserAvatar getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(UserAvatar userAvatar) {
		this.userAvatar = userAvatar;
	}

	/**
	 * 全局变量中收藏商品的个数
	 */
	public int collectCount;

	public int getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(int collectCount) {
		this.collectCount = collectCount;
	}

	/**
	 * 全局变量中的Cart集合
	 */
	public List<CartBean> cartList = new ArrayList<>();

	public List<CartBean> getCartList() {
		return cartList;
	}

	public void setCartList(List<CartBean> cartList) {
		this.cartList = cartList;
	}

	/**
	 * 全局变量中的群组集合
	 */
	private List<UserAvatar> userList=new ArrayList<>();

	public List<UserAvatar> getUserList() {
		return userList;
	}

	public void setUserList(List<UserAvatar> userList) {
		this.userList = userList;
	}

	/**
	 * 全局变量中群组成员的Map集合
	 */
	private Map<String, Map<String, MemberUserAvatar>> groupMembers = new HashMap<>();



	public Map<String, Map<String, MemberUserAvatar>> getGroupMembers() {
		return groupMembers;
	}

	public void setGroupMembers(Map<String, Map<String, MemberUserAvatar>> groupMembers) {
		this.groupMembers = groupMembers;
	}

	/**
	 * 全局的当前登录用户的好友的Map集合
	 */
	private Map<String, UserAvatar> userAvatarMap=new HashMap<>();

	public Map<String, UserAvatar> getUserAvatarMap() {
		return userAvatarMap;
	}

	public void setUserAvatarMap(Map<String, UserAvatar> userAvatarMap) {
		this.userAvatarMap = userAvatarMap;
	}
	/**
	 * 全局的当前登录用户的群组的Map集合
	 */

}
