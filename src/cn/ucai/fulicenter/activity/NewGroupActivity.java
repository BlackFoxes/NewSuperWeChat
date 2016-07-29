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
package cn.ucai.fulicenter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.SuperWeChatApplication;
import cn.ucai.fulicenter.bean.GroupAvatar;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.listener.OnSetAvatarListener;
import cn.ucai.fulicenter.utils.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

import com.easemob.exceptions.EaseMobException;

import java.io.File;

public class NewGroupActivity extends BaseActivity {
	private EditText groupNameEditText;
	private ProgressDialog progressDialog;
	private EditText introductionEditText;
	private CheckBox checkBox;
	private CheckBox memberCheckbox;
	private LinearLayout openInviteContainer;
	private static final String TAG = NewGroupActivity.class.getSimpleName();
	private OnSetAvatarListener mOnSetAvatarListener;
	private String avatarName;
	private ImageView groupAvatar;
	public static int CREATE_GROUP = 100;
	private RelativeLayout layout_parent;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		setListener();
	}

	private void setListener() {
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					openInviteContainer.setVisibility(View.INVISIBLE);
				}else{
					openInviteContainer.setVisibility(View.VISIBLE);
				}
			}
		});
		layout_parent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnSetAvatarListener = new OnSetAvatarListener(NewGroupActivity.this, R.id.group_layout, getAvatarName(), I.AVATAR_TYPE_GROUP_PATH);

			}
		});
	}

	private void initView() {
		setContentView(R.layout.activity_new_group);
		layout_parent = (RelativeLayout) findViewById(R.id.group_avatar);
		groupNameEditText = (EditText) findViewById(R.id.edit_group_name);
		introductionEditText = (EditText) findViewById(R.id.edit_group_introduction);
		checkBox = (CheckBox) findViewById(R.id.cb_public);
		memberCheckbox = (CheckBox) findViewById(R.id.cb_member_inviter);
		openInviteContainer = (LinearLayout) findViewById(R.id.ll_open_invite);
		groupAvatar = (ImageView) findViewById(R.id.iv_avatar);
	}

	private String getAvatarName() {
		avatarName = String.valueOf(System.currentTimeMillis());
		return avatarName;
	}

	/**
	 * @param v
	 */
	public void save(View v) {
		String str6 = getResources().getString(R.string.Group_name_cannot_be_empty);
		String name = groupNameEditText.getText().toString();
		if (TextUtils.isEmpty(name)) {
			Intent intent = new Intent(this, AlertDialog.class);
			intent.putExtra("msg", str6);
			startActivity(intent);
		} else {
			// 进通讯录选人
			startActivityForResult(new Intent(this, GroupPickContactsActivity.class).putExtra("groupName", name), CREATE_GROUP);
		}
	}
	String st2 ;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		st2 = getResources().getString(R.string.Failed_to_create_groups);
		if (resultCode != RESULT_OK) {
			return;
		}
		mOnSetAvatarListener.setAvatar(requestCode,data,groupAvatar);
		if (requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO) {
			Log.e(TAG, "REQUEST_CROP_PHOTO");

		}
		if (requestCode == CREATE_GROUP) {
			//新建群组
			createEMGroup(data);
		}
	}

	private void createEMGroup(final Intent data) {
		setProgressDialog();
		new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用sdk创建群组方法
                String groupName = groupNameEditText.getText().toString().trim();
                String desc = introductionEditText.getText().toString();
                String[] members = data.getStringArrayExtra("newmembers");
                EMGroup group;
                try {
                    if(checkBox.isChecked()){
                        //创建公开群，此种方式创建的群，可以自由加入
                        //创建公开群，此种方式创建的群，用户需要申请，等群主同意后才能加入此群
                        group= EMGroupManager.getInstance().createPublicGroup(groupName, desc, members, true,200);
                    }else{
                        //创建不公开群
                        group=EMGroupManager.getInstance().createPrivateGroup(groupName, desc, members, memberCheckbox.isChecked(),200);
                    }
					createAppGroup(groupName, desc, group.getGroupId(), members);
					Log.e(TAG, "groupId=" + group.getGroupId());
//					Log.e(TAG, "afterGroupId" + group.getGroupId());
					runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                } catch (final EaseMobException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(NewGroupActivity.this, st2 + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();
	}

	private void setProgressDialog() {
		String st1 = getResources().getString(R.string.Is_to_create_a_group_chat);
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(st1);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
	}

	private void createAppGroup(String groupName, String desc, final String groupId, final String[] members) {
		Log.e(TAG, "createAppGroup");
		Log.e(TAG, "groupName"+groupName);
		boolean isPublic = checkBox.isChecked();
		boolean isInvited = !isPublic;
		File file = new File(OnSetAvatarListener.getAvatarPath(NewGroupActivity.this, I.AVATAR_TYPE_GROUP_PATH), avatarName + I.AVATAR_SUFFIX_JPG);
		Log.e(TAG, "createAppGroup.file" + file.getAbsolutePath());
		String own = SuperWeChatApplication.getInstance().getUserName();
		final OkHttpUtils2<String> utils2=new OkHttpUtils2<>();
		utils2.setRequestUrl(I.REQUEST_CREATE_GROUP)
				.addParam(I.Group.HX_ID,groupId)
				.addParam(I.Group.NAME,groupName)
				.addParam(I.Group.OWNER,own)
				.addParam(I.Group.DESCRIPTION,desc)
				.addParam(I.Group.IS_PUBLIC, String.valueOf(isPublic))
				.addParam(I.Group.GROUP_ID,String.valueOf(isInvited))
				.targetClass(String.class)
				.addFile(file)
				.execute(new OkHttpUtils2.OnCompleteListener<String>() {
					@Override
					public void onSuccess(String result) {
						if (result != null) {
							Result result1 = Utils.getResultFromJson(result, GroupAvatar.class);
							GroupAvatar group= (GroupAvatar) result1.getRetData();
							if (result1 != null && result1.isRetMsg()) {
								if (members != null && members.length > 0) {
									addGroupMembers(groupId, members,group);
								} else {

									createGroupSuccess(group);
								}

							}
						}
					}

					@Override
					public void onError(String error) {
						Log.e(TAG, "error=" + error);
						progressDialog.dismiss();
						Toast.makeText(NewGroupActivity.this, st2 + error, Toast.LENGTH_LONG).show();
					}
				});
	}

	private void addGroupMembers(String groupId, String[] members,final GroupAvatar group) {
		String memberArr = "";
		for (String m : members) {
			memberArr +=m+"," ;
		}
		memberArr=memberArr.substring(0, memberArr.length() - 1);
		Log.e(TAG, "memberArr=" + memberArr);
		final OkHttpUtils2<String> utils2 = new OkHttpUtils2<>();
		utils2.setRequestUrl(I.REQUEST_ADD_GROUP_MEMBERS)
				.addParam(I.Member.GROUP_HX_ID,groupId)
				.addParam(I.Member.USER_NAME,memberArr)
				.targetClass(String.class)
				.execute(new OkHttpUtils2.OnCompleteListener<String>() {
					@Override
					public void onSuccess(String result) {
						if (result != null) {
							Result result2 = Utils.getResultFromJson(result, GroupAvatar.class);
							GroupAvatar groupAvatar= (GroupAvatar) result2.getRetData();
							if (result2 != null && result2.isRetMsg()) {

								createGroupSuccess(group);

							} else {
								progressDialog.dismiss();
								Toast.makeText(NewGroupActivity.this, st2, Toast.LENGTH_LONG).show();

							}
						}

					}

					@Override
					public void onError(String error) {
						Log.e(TAG, "error=" + error);
						progressDialog.dismiss();
						Toast.makeText(NewGroupActivity.this, st2 + error, Toast.LENGTH_LONG).show();

					}
				});




	}

	private void createGroupSuccess(GroupAvatar group) {
		SuperWeChatApplication.getInstance().getGroupAvatarMap().put(group.getMGroupHxid(), group);
		SuperWeChatApplication.getInstance().getGrouplist().add(group);
		runOnUiThread(new Runnable() {
            public void run() {
                progressDialog.dismiss();
                setResult(RESULT_OK);
                finish();
            }
        });
	}

	public void back(View view) {
		finish();
	}
}
