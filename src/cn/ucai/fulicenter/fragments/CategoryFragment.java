package cn.ucai.fulicenter.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.FuLiCenterMainActivity;
import cn.ucai.fulicenter.adapter.CategoryAdapter;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.utils.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    ExpandableListView mExpandableListView;
    CategoryAdapter mAdapter;
    ArrayList<CategoryGroupBean> mGroupList;
    FuLiCenterMainActivity mContext;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;
    static final String TAG = CategoryFragment.class.getSimpleName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_cagegory, container, false);
        mContext = (FuLiCenterMainActivity) getContext();
        initView(inflate);
        initData();
        return inflate;
    }
    private void initData() {
        findCategoryGroupList(new OkHttpUtils2.OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                Log.e(TAG, "group,result.size=" + result.length);
                if (result != null && result.length != 0) {
                    ArrayList<CategoryGroupBean> categoryGroupList = Utils.array2List(result);
                    mGroupList.addAll(categoryGroupList);
                    mAdapter.notifyDataSetChanged();
                    Log.e(TAG, "group,categoryGroupList=" + categoryGroupList);
                    int i = 0;
                    for (CategoryGroupBean g : categoryGroupList) {
                        mChildList.add(new ArrayList<CategoryChildBean>());
                        findCategoryChildList(g.getId(),i);
                        i++;
                    }
                }
            }
            @Override
            public void onError(String error) {
                Log.e(TAG, "group,error=" + error);

            }
        });
    }

    private void findCategoryGroupList( OkHttpUtils2.OnCompleteListener<CategoryGroupBean[]> listener) {

        OkHttpUtils2<CategoryGroupBean[]> utils2 = new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(listener);
    }

    private void findCategoryChildList(int groupCategoryId,final int i) {
        OkHttpUtils2<CategoryChildBean[]> utils2 = new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID, String.valueOf(groupCategoryId))
                .addParam(I.PAGE_ID, String.valueOf(I.PAGE_ID_DEFAULT))
                .addParam(I.PAGE_SIZE, String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(CategoryChildBean[].class)
                .execute(new OkHttpUtils2.OnCompleteListener<CategoryChildBean[]>() {
                    @Override
                    public void onSuccess(CategoryChildBean[] childResult) {
                        Log.e(TAG, "child,result=" + childResult);
                        if (childResult != null) {
                            ArrayList<CategoryChildBean> childList = Utils.array2List(childResult);
                            Log.e(TAG, "childList.size=" + childList.size());
                            mChildList.set(i, childList);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "child,error=" + error);

                    }
                });

    }

    private void initView(View inflate) {
        mGroupList = new ArrayList<CategoryGroupBean>();
        mChildList = new ArrayList<ArrayList<CategoryChildBean>>();
        mExpandableListView = (ExpandableListView) inflate.findViewById(R.id.epCagegory);
        mAdapter = new CategoryAdapter(mGroupList, mChildList, mContext);
        mExpandableListView.setGroupIndicator(null);
        mExpandableListView.setAdapter(mAdapter);
    }

}
