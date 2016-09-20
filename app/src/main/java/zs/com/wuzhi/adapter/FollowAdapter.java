package zs.com.wuzhi.adapter;

import zs.com.wuzhi.R;
import zs.com.wuzhi.bean.UserInfo;

/**
 * Created by zhangshuqing on 16/9/20.
 */
public class FollowAdapter extends BaseListAdapter<UserInfo> {

    public FollowAdapter(Callback callback) {

        super(callback);
    }

    @Override
    protected void convert(ViewHolder vh, UserInfo item, int position) {
        //设置头像
        vh.setCircleImage(mCallback.getContext(),R.id.follow_avatar,item.getAvatarUrl());
        //设置 用户名
        vh.setText(R.id.follow_name,item.getNickName());
        //设置 签名
        vh.setText(R.id.follow_signature,item.getSignature());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.follow_item;
    }
}
