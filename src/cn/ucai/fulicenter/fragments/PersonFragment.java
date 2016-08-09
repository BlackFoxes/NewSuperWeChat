package cn.ucai.fulicenter.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.plus.model.people.Person;

import java.util.ArrayList;
import java.util.HashMap;

import cn.ucai.fulicenter.DemoHXSDKHelper;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.CollectActivity;
import cn.ucai.fulicenter.activity.FuLiCenterMainActivity;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.utils.UserUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonFragment extends Fragment {
    static final String TAG = PersonFragment.class.getSimpleName();
    FuLiCenterMainActivity mContext;
    ImageView mivUserAvatar,mivMSG;
    TextView mtvUserName,mtvSettings, mtvCollectCount;
    LinearLayout layoutUserCenter;
    RelativeLayout layoutCollect;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout;
        layout= inflater.inflate(R.layout.fragment_person, container, false);
        initView(layout);
        initData();
        setListener();
        updateCollectCountListener();
        return layout;
    }

    private void initData() {
        if (DemoHXSDKHelper.getInstance().isLogined()) {
            UserAvatar user = FuLiCenterApplication.getInstance().getUserAvatar();
            Log.e(TAG, "user=" + user);
            UserUtils.setAppCurrentUserNick(mtvUserName);
            UserUtils.setAppCurrentUserAvatar(mContext,mivUserAvatar);
        }
    }

    private void setListener() {
        MyClickListener myClickListener=new MyClickListener();
        mtvSettings.setOnClickListener(myClickListener);
        layoutUserCenter.setOnClickListener(myClickListener);
        layoutCollect.setOnClickListener(myClickListener);
    }

    class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (DemoHXSDKHelper.getInstance().isLogined()) {
                switch (v.getId()) {
                    case R.id.tv_setting:
                    case R.id.center_user_info:
                        startActivity(new Intent(mContext, SettingsActivity.class));
                        break;
                    case R.id.layout_center_collect:

                        startActivity(new Intent(mContext, CollectActivity.class));
                        break;
                }


            } else {
                Log.e(TAG, "the user is not Logined!");
            }

        }
    }

    private void initView(View layout) {
        mContext = (FuLiCenterMainActivity) getActivity();
        mivUserAvatar = (ImageView) layout.findViewById(R.id.iv_personal_avatar);
        mtvUserName = (TextView) layout.findViewById(R.id.tv_username);
        mtvSettings = (TextView) layout.findViewById(R.id.tv_setting);
        mivMSG = (ImageView) layout.findViewById(R.id.iv_personal_msg);
        mtvCollectCount = (TextView) layout.findViewById(R.id.collect_baobei);
        layoutUserCenter = (LinearLayout) layout.findViewById(R.id.center_user_info);
        layoutCollect = (RelativeLayout) layout.findViewById(R.id.layout_center_collect);
        initOrderList(layout);


    }

    private void initOrderList(View layout) {
        GridView gvOrderList = (GridView) layout.findViewById(R.id.center_user_order_lis);
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> order1 = new HashMap<String, Object>();
        order1.put("order", R.drawable.order_list1);
        data.add(order1);
        HashMap<String, Object> order2 = new HashMap<String, Object>();
        order2.put("order", R.drawable.order_list2);
        data.add(order2);
        HashMap<String, Object> order3 = new HashMap<String, Object>();
        order3.put("order", R.drawable.order_list3);
        data.add(order3);
        HashMap<String, Object> order4 = new HashMap<String, Object>();
        order4.put("order", R.drawable.order_list4);
        data.add(order4);
        HashMap<String, Object> order5 = new HashMap<String, Object>();
        order5.put("order", R.drawable.order_list5);
        data.add(order5);
        SimpleAdapter adapter = new SimpleAdapter(mContext, data, R.layout.adapter_simple,
                new String[]{"order"}, new int[]{R.id.iv_personal});
        gvOrderList.setAdapter(adapter);


    }

    UpdateContactCount mReceiver;

    class UpdateContactCount extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int collectCount = FuLiCenterApplication.getInstance().getCollectCount();
            Log.e(TAG, "collectCount=" + collectCount);
            mtvCollectCount.setText(String.valueOf(collectCount));

        }
    }

    private void updateCollectCountListener() {
        mReceiver = new UpdateContactCount();
        IntentFilter mFilter = new IntentFilter("update_collect");
        mContext.registerReceiver(mReceiver, mFilter);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mReceiver);
    }
}
