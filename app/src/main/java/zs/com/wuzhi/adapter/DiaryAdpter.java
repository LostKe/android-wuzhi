package zs.com.wuzhi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import zs.com.wuzhi.R;
import zs.com.wuzhi.bean.Diary;

/**
 * Created by zhangshuqing on 16/8/24.
 */
public class DiaryAdpter extends BaseAdapter {

    List<Diary> list;

    Context context;

    ViewHolder viewHolder;

    public DiaryAdpter(List<Diary> list,Context context){
        this.list=list;
        this.context=context;
    }

    @Override
    public int getCount() {
        return list.size();
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
        Diary diary=list.get(position);
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.diary_item,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_content= (TextView) convertView.findViewById(R.id.tv_diary_content);
            viewHolder.tv_time= (TextView) convertView.findViewById(R.id.tv_diary_time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_content.setText(diary.getContent());
        viewHolder.tv_time.setText(diary.getTime());
        return convertView;
    }

    final class ViewHolder {
        public TextView tv_time;
        public TextView tv_content;
    }
}
