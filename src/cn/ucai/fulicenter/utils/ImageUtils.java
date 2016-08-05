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
package cn.ucai.fulicenter.utils;

import android.content.Context;
import android.widget.ImageView;

import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;
import com.squareup.picasso.Picasso;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.domain.User;

public class ImageUtils {
//	public static String getThumbnailImagePath(String imagePath) {
//		String path = imagePath.substring(0, imagePath.lastIndexOf("/") + 1);
//		path += "th" + imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.length());
//		EMLog.d("msg", "original image path:" + imagePath);
//		EMLog.d("msg", "thum image path:" + path);
//		return path;
//	}
	
	public static String getImagePath(String remoteUrl)
	{
		String imageName= remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1, remoteUrl.length());
		String path =PathUtil.getInstance().getImagePath()+"/"+ imageName;
        EMLog.d("msg", "image path:" + path);
        return path;
		
	}
	
	
	public static String getThumbnailImagePath(String thumbRemoteUrl) {
		String thumbImageName= thumbRemoteUrl.substring(thumbRemoteUrl.lastIndexOf("/") + 1, thumbRemoteUrl.length());
		String path =PathUtil.getInstance().getImagePath()+"/"+ "th"+thumbImageName;
        EMLog.d("msg", "thum image path:" + path);
        return path;
    }

//	public static void setUserAvatar(Context context, String username, ImageView imageView){
//		User user = getUserInfo(username);
//		if(user != null && user.getAvatar() != null){
//			Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.default_avatar).into(imageView);
//		}else{
//			Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
//		}
//	}
	public static void setGoodAvatar(String path, Context mContext, ImageView mGoodAvatar) {
		String url = I.DOWNLOAD_BOUTIQUE_IMG_URL+path;
		Picasso.with(mContext).load(url).placeholder(R.drawable.nopic).into(mGoodAvatar);

	}
	public static void setGroupAvatar(String path, Context mContext, ImageView mGoodAvatar) {
		String url = I.DOWNLOAD_DOWNLOAD_CATEGORY_GROUP_IMAGE_URL+path;
		Picasso.with(mContext).load(url).placeholder(R.drawable.nopic).into(mGoodAvatar);

	}
	public static void setChildAvatar(String path, Context mContext, ImageView mGoodAvatar) {
		String url = I.DOWNLOAD_DOWNLOAD_CATEGORY_CHILD_IMAGE_URL+path;
		Picasso.with(mContext).load(url).placeholder(R.drawable.nopic).into(mGoodAvatar);

	}

}
