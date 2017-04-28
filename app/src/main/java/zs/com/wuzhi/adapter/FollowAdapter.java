package zs.com.wuzhi.adapter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;


import com.kaopiz.kprogresshud.KProgressHUD;

import org.jsoup.helper.StringUtil;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import zs.com.wuzhi.R;
import zs.com.wuzhi.activity.DiaryActivity;
import zs.com.wuzhi.bean.PersonDiary;
import zs.com.wuzhi.bean.UserInfo;
import zs.com.wuzhi.db.DBHelper;
import zs.com.wuzhi.util.Constant;
import zs.com.wuzhi.util.WuzhiSprider;
import zs.com.wuzhi.widget.MenuDialog;
import zs.com.wuzhi.widget.PromptDialog;

/**
 * Created by zhangshuqing on 16/9/20.
 */
public class FollowAdapter extends BaseListAdapter<UserInfo>  {
    MenuDialog menuDialog;
    KProgressHUD hud;
    public FollowAdapter(Callback callback) {
        super(callback);
        hud=KProgressHUD.create(callback.getContext()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
    }

    @Override
    protected void convert(ViewHolder vh, UserInfo item, int position) {
        //设置头像
        vh.setCircleImage(mCallback.getContext(),R.id.follow_avatar,item.getAvatarUrl());
        //设置 用户名
        vh.setText(R.id.follow_name,item.getNickName());
        //设置 签名
        vh.setText(R.id.follow_signature,item.getSignature());
        vh.getView(R.id.follow_ll_attention).setOnClickListener(new FollowClickListener(position));
        vh.getView(R.id.follow_item_ll).setOnClickListener(new FollowClickListener(position));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.follow_item;
    }

   class FollowClickListener implements  View.OnClickListener{
       int position;
       FollowClickListener(int position){
           this.position=position;
       }

       @Override
       public void onClick(View v) {
           UserInfo userInfo= getDatas().get(position);
           switch (v.getId()){
               case  R.id.follow_ll_attention:
                   menuDialog=new MenuDialog(mCallback.getContext());
                   View view=menuDialog.getLayoutInflater().inflate(R.layout.follow_dialog,null);
                   OnMenuClickListener listener=new OnMenuClickListener(userInfo.getUserId(),position);
                   view.findViewById(R.id.menu2).setOnClickListener(listener);
                   view.findViewById(R.id.menu3).setOnClickListener(listener);
                   menuDialog.setContentView(view);
                   menuDialog.show();
                   menuDialog.setCancelable(true);
               break;
               case R.id.follow_item_ll:
                   String userId=userInfo.getUserId();
                   Intent intent=new Intent();
                   intent.setClass(mCallback.getContext(), DiaryActivity.class);
                   intent.putExtra(Constant.USER_ID,userId);
                   intent.putExtra(Constant.TOOL_BAR_TITLE,"关注列表");

                   mCallback.getContext().startActivity(intent);
                   break;
           }


       }
   }

    /**
     * 点击取消关注
     */
    class OnMenuClickListener implements View.OnClickListener{
        String  userId;
        int position;
        OnMenuClickListener(String  userId,int position){
            this.userId=userId;
            this.position=position;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.menu2:
                    hud.show();
                    final FollowHandler handler=new FollowHandler(FollowAdapter.this);
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            DBHelper db=new DBHelper(mCallback.getContext());
                            db.deleteFollowByPid(userId);
                            Message message=Message.obtain();
                            message.arg1=position;
                            handler.sendMessage(message);
                        }
                    });
                    break;
                case R.id.menu3:
                    menuDialog.dismiss();
                    break;
            }
        }
    }

   static class FollowHandler extends Handler{
       private WeakReference<FollowAdapter> current;
       FollowHandler(FollowAdapter mCurrent){
           this.current=new WeakReference<FollowAdapter>(mCurrent);
       }
       @Override
       public void handleMessage(Message msg) {

           int position=msg.arg1;
           FollowAdapter mAdpapter=current.get();
           mAdpapter.menuDialog.dismiss();
           //取消成功 移除 该item
           mAdpapter.removeItem(position);
           mAdpapter.hud.dismiss();
           PromptDialog dialog=new PromptDialog(mAdpapter.mCallback.getContext(),R.drawable.card_icon_addtogroup_confirm,"取消成功");
           dialog.showDialog();
           dialog.dismiss();
       }
   }
}
