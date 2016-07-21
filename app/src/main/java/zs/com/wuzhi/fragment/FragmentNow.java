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
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

import zs.com.wuzhi.R;
import zs.com.wuzhi.activity.DiaryActivity;
import zs.com.wuzhi.bean.Item;
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragement_now,null);
        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        initSwipeRefreshLayout(view);
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
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        gridView= (GridView) root.findViewById(R.id.gridView);
        gridView.setOnItemClickListener(this);
        //请求服务器获取文件 子线程进行
        Handler handler=new MessageHandler();
        new InitViewThread(handler).start();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Item item=_items.get(position);
        String userId= ConvertUtil.getUserId(item.getUrl());
        Toast.makeText(getContext(),userId,Toast.LENGTH_SHORT).show();
        Intent intent=new Intent();
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


       public GridViewAdpter(){

        }

        @Override
        public int getCount() {
            return _items.size();
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
     * hadler 更新UI
     */
    class MessageHandler extends  Handler{
        @Override
        public void handleMessage(Message msg) {
            _items= (List<Item>) msg.getData().getSerializable("items");
            adapterItem(_items);
            gridViewAdapter=new GridViewAdpter();
            gridView.setAdapter(gridViewAdapter);
            gridViewAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 动态的减少items的数量来满足
     * 屏幕的每一行中不会有空白
     * @param items
     */
    private void adapterItem(List<Item> items){
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
        Handler handler=new MessageHandler();
        new InitViewThread(handler).start();

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
