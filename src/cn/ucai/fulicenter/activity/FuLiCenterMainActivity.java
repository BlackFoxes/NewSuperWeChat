package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragments.BoutiqueFragment;
import cn.ucai.fulicenter.fragments.CartFragment;
import cn.ucai.fulicenter.fragments.CategoryFragment;
import cn.ucai.fulicenter.fragments.NewGoodFragment;
import cn.ucai.fulicenter.fragments.PersonFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fulicenter);
        initView();
        initFragments();
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
                index = 4;
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

    private void setRadioButtonStatus(int index) {
        for (int i=0;i<mrbTabs.length;i++) {
            if (index == i) {
                mrbTabs[i].setChecked(true);
            } else {
                mrbTabs[i].setChecked(false);
            }
        }
    }


}
