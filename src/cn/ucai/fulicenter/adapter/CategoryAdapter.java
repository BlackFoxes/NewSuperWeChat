package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.utils.ImageUtils;

/**
 * Created by Administrator on 2016/8/4.
 */
public class CategoryAdapter extends BaseExpandableListAdapter {
    List<CategoryGroupBean> groupList;
    List<ArrayList<CategoryChildBean>> childList;
    Context mContext;
    public CategoryAdapter(List<CategoryGroupBean> groupList, List<ArrayList<CategoryChildBean>> childList, Context mContext) {
        this.mContext = mContext;
        this.groupList = groupList;
        this.childList = childList;
    }
    @Override
    public int getGroupCount() {
        return groupList!=null?groupList.size():0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        if (groupList.get(groupPosition) != null)
            return groupList.get(groupPosition);
        return null;
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        if (childList.get(groupPosition)!=null
                &&childList.get(groupPosition).get(childPosition)!=null)
        return childList.get(groupPosition).get(childPosition);
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupListHolder mGroupListHolder = new GroupListHolder();
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_group_list, null);
            mGroupListHolder.ivGroupAvatar = (ImageView) convertView.findViewById(R.id.ivCategoryGroup);
            mGroupListHolder.ivGroupExpand = (ImageView) convertView.findViewById(R.id.ivCagegoryExpand);
            mGroupListHolder.tvGroupTitle = (TextView) convertView.findViewById(R.id.tvCategoryGroup);
            convertView.setTag(mGroupListHolder);
        } else {
            mGroupListHolder= (GroupListHolder) convertView.getTag();
        }
        CategoryGroupBean group = groupList.get(groupPosition);
        mGroupListHolder.tvGroupTitle.setText(group.getName());
        ImageUtils.setGroupAvatar(group.getImageUrl(),mContext,mGroupListHolder.ivGroupAvatar);
        if (isExpanded) {
            mGroupListHolder.ivGroupExpand.setImageResource(R.drawable.expand_off);
        } else {
            mGroupListHolder.ivGroupExpand.setImageResource(R.drawable.expand_on);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildListHolder mChildListHolder = new ChildListHolder();
        CategoryChildBean child = childList.get(groupPosition).get(childPosition);
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_child_list, null);
            mChildListHolder.ivChildeAvatar = (ImageView) convertView.findViewById(R.id.ivChildCagegory);
            mChildListHolder.tvChildCagegory = (TextView) convertView.findViewById(R.id.tvChildCagegory);
            convertView.setTag(mChildListHolder);

        } else {
            mChildListHolder = (ChildListHolder) convertView.getTag();
        }
        ImageUtils.setChildAvatar(child.getImageUrl(),mContext,mChildListHolder.ivChildeAvatar);
        mChildListHolder.tvChildCagegory.setText(child.getName());
        return convertView;
    }
    class GroupListHolder {
        ImageView ivGroupAvatar;
        TextView tvGroupTitle;
        ImageView ivGroupExpand;
    }

    class ChildListHolder {
        ImageView ivChildeAvatar;
        TextView tvChildCagegory;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
