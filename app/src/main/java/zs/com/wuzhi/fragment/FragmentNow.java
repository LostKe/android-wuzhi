package zs.com.wuzhi.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;

import zs.com.wuzhi.R;
import zs.com.wuzhi.activity.DiaryActivity;
import zs.com.wuzhi.bean.Item;
import zs.com.wuzhi.util.Constant;
import zs.com.wuzhi.util.ConvertUtil;
import zs.com.wuzhi.util.WuzhiSprider;

/**
 * Created by zhangshuqing on 16/7/18.
 */
public class FragmentNow extends Fragment implements AdapterView.OnItemClickListener{

    SwipeRefreshLayout swipeRefreshLayout;

    GridView gridView;
    GridViewAdpter gridViewAdapter;
    List<Item> _items;

    KProgressHUD hud;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragement_now,null);
        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        initSwipeRefreshLayout(view);
        hud=KProgressHUD.create(getContext()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please wait")
                .setCancellable(true);
        hud.show();

        return view;
    }

    private void initSwipeRefreshLayout(View root) {
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();

            }
        });

        gridView= (GridView) root.findViewById(R.id.gridView);
        gridView.setOnItemClickListener(this);

        gridViewAdapter=new GridViewAdpter();
        gridView.setAdapter(gridViewAdapter);
        //请求服务器获取文件 子线程进行
        Handler handler=new MessageHandler(this);
        new InitViewThread(handler).start();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Item item=_items.get(position);
        String userId= ConvertUtil.getUserId(item.getHref());
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        //给子Activity 传递用户id
        bundle.putString(Constant.USER_ID,userId);
        intent.putExtras(bundle);
        intent.setClass(getContext(), DiaryActivity.class);
        startActivity(intent);
    }


    /**
     * 从网络获取数据 传递给UI线程
     */
    class InitViewThread extends Thread{
        Handler handler;
        InitViewThread(Handler handler){
            this.handler=handler;
        }
        @Override
        public void run() {
            List<Item> items=WuzhiSprider.getNowImg();
            Message message= Message.obtain();
            Bundle bundle=new Bundle();
            bundle.putSerializable("items",(Serializable) items);
            message.setData(bundle);
            handler.sendMessage(message);

        }
    }


    class GridViewAdpter extends BaseAdapter{
        ViewHolder holder;

        @Override
        public int getCount() {
            return _items==null?0:_items.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=LayoutInflater.from(getContext()).inflate(R.layout.grid_item,null);
                holder=new ViewHolder();
                holder.iv= (ImageView) convertView.findViewById(R.id.squareLayout_iv);
                holder.tv= (TextView) convertView.findViewById(R.id.squareLayout_tv);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
            Item item=_items.get(position);
            holder.tv.setText(item.getTag());

            Glide.with(getContext()).load(item.getUrl()).into(holder.iv);
            return convertView;
        }

        final class ViewHolder{
            public ImageView iv;
            public TextView tv;
        }
    }

    /**
     *Handler 定义为static类型原因
     * 当Activity执行了onDestroy,由于线程以及Handler的HandleMessage的存在，使得系统本希望进行此Activity的内存回收不能实现
     * (非静态的内部类中隐性的持有外部类的引用，导致可能存在内存泄露的问题)
     * 将Handler 定义为静态内部类的形式，这样可以使与外部类解耦，不再持有外部内的引用
     * 同时由于Handler中的handlerMessage一般都会多少需要访问或修改Activity的属性，此时，
     * 需要在Handler内部定义指向此Activity的WeakReference，使其不会影响到Activity的内存回收同时，
     * 可以在正常情况下访问到Activity的属性。
     *
     * hadler 更新UI
     */
    static class MessageHandler extends  Handler{

        private WeakReference<FragmentNow> current;
        MessageHandler(FragmentNow mCurrent){
            this.current=new WeakReference<FragmentNow>(mCurrent);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentNow currentObj=current.get();
            currentObj._items= (List<Item>) msg.getData().getSerializable("items");
            currentObj.adapterItem(currentObj._items);
            currentObj.gridViewAdapter.notifyDataSetChanged();
            currentObj.hud.dismiss();
            if(currentObj.swipeRefreshLayout!=null){
                currentObj.swipeRefreshLayout.setRefreshing(false);
            }

        }
    }

    /**
     * 动态的减少items的数量来满足
     * 屏幕的每一行中不会有空白
     * @param items
     */
    private  void adapterItem(List<Item> items){
        WindowManager windowManager= (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width=windowManager.getDefaultDisplay().getWidth();//屏幕宽度
        int img_width= (int) getContext().getResources().getDimension(R.dimen.itemsize);
        int cloum=width/img_width;
        int itemSize=items.size();
        int mode=itemSize%cloum;
        if(mode>0){
            for (int i=0;i<mode;i++){
                items.remove(itemSize-1-i);
            }
        }

    }

    /**
     * 刷新触发操作
     */
    public void refresh(){
        Handler handler=new MessageHandler(this);
        new InitViewThread(handler).start();

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
