package cn.ucai.fulicenter.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteFullException;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.ucai.fulicenter.DemoHXSDKHelper;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragments.BoutiqueFragment;
import cn.ucai.fulicenter.fragments.CartFragment;
import cn.ucai.fulicenter.fragments.CategoryFragment;
import cn.ucai.fulicenter.fragments.NewGoodFragment;
import cn.ucai.fulicenter.fragments.PersonFragment;
import cn.ucai.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/8/1.
 */
public class FuLiCenterMainActivity extends BaseActivity {
    RadioButton mRbNewGoods,mRbBoutique,mRbCategory,mRbPerson, mRbCart;
    Fragment[] fragments;
    TextView tvCartHint;
    RadioButton[] mrbTabs;
    int index;
    int currentindex;
    FuLiCenterMainActivity mContext;
    static final int ACTION_LOGIN = 0;
    static final String TAG = FuLiCenterMainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fulicenter);
        mContext = this;
        initView();
        setListener();
        initFragments();
    }

    private void setListener() {
        initCartCountListener();
    }

    private void initFragments() {
        fragments = new Fragment[5];
        fragments[0] = new NewGoodFragment();
        fragments[1] = new BoutiqueFragment();
        fragments[2] = new CategoryFragment();
        fragments[3] = new CartFragment();
        fragments[4] = new PersonFragment();

    }

//    private void setListener() {
//        mRbNewGoods.check
//
//
//    }

    private void initView() {
        mRbBoutique = (RadioButton) findViewById(R.id.rb_boutique);
        mRbCart = (RadioButton) findViewById(R.id.rb_Cart);
        mRbPerson = (RadioButton) findViewById(R.id.rb_personal);
        mRbCategory = (RadioButton) findViewById(R.id.rb_category);
        mRbNewGoods = (RadioButton) findViewById(R.id.rb_new_goods);
        tvCartHint = (TextView) findViewById(R.id.cart_hint);
        mrbTabs = new RadioButton[5];
        mrbTabs[0] = mRbNewGoods;
        mrbTabs[1] = mRbBoutique;
        mrbTabs[2] = mRbCategory;
        mrbTabs[3] = mRbCart;
        mrbTabs[4] = mRbPerson;
    }
    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.rb_new_goods:
                index = 0;

                break;
            case R.id.rb_boutique:
                index = 1;

                break;
            case R.id.rb_category:
                index = 2;
                break;
            case R.id.rb_Cart:
                index = 3;
                break;
            case R.id.rb_personal:
                if (DemoHXSDKHelper.getInstance().isLogined()) {
                    index = 4;
                } else {
                    startActivity(new Intent(this,LoginActivity.class));
                }
                break;
        }
        if (currentindex != index) {
            currentindex = index;
            setRadioButtonStatus(currentindex);
            setCurrentFragment(currentindex);
        }
    }

    private void setCurrentFragment(int currentindex) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_fragment, fragments[currentindex]);
        transaction.commit();



    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ACTION_LOGIN) {
//            if (DemoHXSDKHelper.getInstance().isLogined()) {
//                Log.e("onActivityResult", "index=" + index);
//                index = 4;
//                currentindex = index;
//            }
//
//        }
//
//    }

    private void setRadioButtonStatus(int index) {
        for (int i=0;i<mrbTabs.length;i++) {
            if (index == i) {
                mrbTabs[i].setChecked(true);
            } else {
                mrbTabs[i].setChecked(false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "currentindex=" + currentindex);
        Log.e(TAG, "index=" + index);
        currentindex = index;
        if (DemoHXSDKHelper.getInstance().isLogined()) {
            currentindex =4;

        } else if (!DemoHXSDKHelper.getInstance().isLogined()&&currentindex == 4) {
            currentindex = 0;


        } else if (!DemoHXSDKHelper.getInstance().isLogined()) {
            tvCartHint.setText("0");
            tvCartHint.setVisibility(View.GONE);

        }
            setCurrentFragment(currentindex);
            setRadioButtonStatus(currentindex);

        }

    CartCountReceiver mReceiver = new CartCountReceiver();

    class CartCountReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            setCartCount();

        }
    }

    private void setCartCount() {
        int count = Utils.getCartCount();
        Log.e(TAG, "count=" + count);
        if (!DemoHXSDKHelper.getInstance().isLogined() || count == 0) {
            tvCartHint.setText("0");
            tvCartHint.setVisibility(View.GONE);

        } else {
            tvCartHint.setText(String.valueOf(count));
            tvCartHint.setVisibility(View.VISIBLE);

        }


    }

    private void initCartCountListener() {
        IntentFilter filter = new IntentFilter("update_cart_list");
        registerReceiver(mReceiver, filter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

}
