package zs.com.wuzhi.adapter;

import android.view.View;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.wang.avi.AVLoadingIndicatorView;

import zs.com.wuzhi.R;
import zs.com.wuzhi.bean.User;

/**
 * Created by zhangshuqing on 16/9/20.
 */
public class UserPhoneAdapter extends BaseListAdapter<User>  {

    KProgressHUD hud;

    public UserPhoneAdapter(Callback callback) {
        super(callback);
        hud=KProgressHUD.create(callback.getContext()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
    }



    @Override
    protected void convert(ViewHolder vh, User item, int position) {
        vh.setText(R.id.user_detail_uerName,item.getUserName());
        vh.setText(R.id.user_detail_phone,item.getPhone());
        vh.setChecked(R.id.checkbox_user_item,item.isSelect());
        vh.getView(R.id.checkbox_user_item).setOnClickListener(new CheckBoxClickListener(position));

        AVLoadingIndicatorView avLoadingIndicatorView = vh.getView(R.id.loading_view);

        switch (item.getState()){
            case 0:
                avLoadingIndicatorView.hide();
                vh.setImage(R.id.img_send_state,R.drawable.ic_access_time);
                break;
            case 1:
                vh.setVisibility(R.id.img_send_state);
                vh.setImage(R.id.img_send_state,R.drawable.ic_check_circle);
                avLoadingIndicatorView.hide();
                break;
            case 2:
                vh.setVisibility(R.id.img_send_state);
                vh.setImage(R.id.img_send_state,R.drawable.ic_error_outline);
                avLoadingIndicatorView.hide();
                break;
            case 3:
                avLoadingIndicatorView.show();
                vh.setInVisibility(R.id.img_send_state);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_detail_item;
    }


    class CheckBoxClickListener implements  View.OnClickListener{
        int position;
        CheckBoxClickListener(int position){
            this.position=position;
        }

        @Override
        public void onClick(View v) {
            User user=getDatas().get(position);
            user.setSelect(user.isSelect()?false:true);

        }
    }



}
