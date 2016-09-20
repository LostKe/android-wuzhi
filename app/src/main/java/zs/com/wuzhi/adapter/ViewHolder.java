package zs.com.wuzhi.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by zhangshuqing on 16/8/21.
 */
public class ViewHolder {

    private SparseArray<View> mViews;
    private int mLayoutId;
    private View mConvertView;
    private int mPosition;
    private Callback mCaller;

    public interface Callback {
        LayoutInflater getInflate();
    }


    public ViewHolder(Callback caller, ViewGroup parent, int layoutId, int position) {
        this.mViews = new SparseArray<View>();
        this.mPosition = position;
        this.mLayoutId = layoutId;
        this.mCaller = caller;
        LayoutInflater inflater = caller.getInflate();
        this.mConvertView = inflater.inflate(layoutId, parent, false);
        this.mConvertView.setTag(this);
    }

    /**
     * 获取一个viewHolder
     *
     * @param caller      Caller
     * @param convertView view
     * @param parent      parent view
     * @param layoutId    布局资源id
     * @param position    索引
     * @return
     */
    public static ViewHolder getViewHolder(Callback caller, View convertView, ViewGroup parent, int layoutId, int position) {
        boolean needCreateView = false;
        ViewHolder vh = null;
        if (convertView == null) {
            needCreateView = true;
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        if (vh != null && (vh.mLayoutId != layoutId)) {
            needCreateView = true;
        }
        if (needCreateView) {
            return new ViewHolder(caller, parent, layoutId, position);
        }
        return (ViewHolder) convertView.getTag();
    }

    public int getPosition() {
        return this.mPosition;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    // 通过一个viewId来获取一个view
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    // 返回viewHolder的容器类
    public View getConvertView() {
        return this.mConvertView;
    }

    // 给TextView设置文字
    public void setText(int viewId, String text) {
        if (TextUtils.isEmpty(text)) return;
        TextView tv = getView(viewId);
        tv.setText(text);
        tv.setVisibility(View.VISIBLE);
    }

    // 给TextView设置文字
    public void setText(int viewId, SpannableString text) {
        if (text == null) return;
        TextView tv = getView(viewId);
        tv.setText(text);
        tv.setVisibility(View.VISIBLE);
    }

    public void setTextColor(int viewId, int textColor) {
        TextView tv = getView(viewId);
        tv.setTextColor(textColor);
        tv.setVisibility(View.VISIBLE);
    }

    public void setText(int viewId, Spanned text) {
        if (text == null) return;
        TextView tv = getView(viewId);
        tv.setText(text);
        tv.setVisibility(View.VISIBLE);
    }

    // 给TextView设置文字
    public void setText(int viewId, int textRes) {
        TextView tv = getView(viewId);
        tv.setText(textRes);
        tv.setVisibility(View.VISIBLE);
    }

    public void setText(int viewId, int textRes, int bgRes, int textColor) {
        TextView tv = getView(viewId);
        tv.setText(textRes);
        tv.setVisibility(View.VISIBLE);
        tv.setBackgroundResource(bgRes);
        tv.setTextColor(textColor);
    }

    public void setText(int viewId, String text, boolean gone) {
        if (TextUtils.isEmpty(text) && gone) {
            getView(viewId).setVisibility(View.GONE);
            return;
        }
        setText(viewId, text);
    }

    public void setText(int viewId, String text, int emptyRes) {
        TextView tv = getView(viewId);
        if (TextUtils.isEmpty(text)) {
            tv.setText(emptyRes);
        } else {
            tv.setText(text);
        }
        tv.setVisibility(View.VISIBLE);
    }

    public void setText(int viewId, String text, String emptyText) {
        TextView tv = getView(viewId);
        if (TextUtils.isEmpty(text)) {
            tv.setText(emptyText);
        } else {
            tv.setText(text);
        }
        tv.setVisibility(View.VISIBLE);
    }

    public void setImage(int viewId, int imgRes) {
        ImageView iv = getView(viewId);
        iv.setImageResource(imgRes);
    }

    public void setCircleImage(Context context,int viewId, String url){
        ImageView imageView = getView(viewId);
        Glide.with(context).load(url).into(imageView);

    }

    public void setOnClick(int viewId, View.OnClickListener onClickListener) {
        View view = getView(viewId);
        view.setOnClickListener(onClickListener);
    }

    public void setGone(int viewId) {
        getView(viewId).setVisibility(View.GONE);
    }

    public void setVisibility(int viewId) {
        getView(viewId).setVisibility(View.VISIBLE);
    }

    public void setInVisibility(int viewId) {
        getView(viewId).setVisibility(View.INVISIBLE);
    }

    public boolean isVisibility(int viewId) {
        return (getView(viewId).getVisibility()) == View.VISIBLE;
    }

    public void setEnabled(int viewId) {
        View view = getView(viewId);
        view.setEnabled(true);
    }

    public void setEnabled(int viewId, boolean isEnable) {
        View view = getView(viewId);
        view.setEnabled(isEnable);
    }

    public void setDisEnabled(int viewId) {
        View view = getView(viewId);
        view.setEnabled(false);
    }
}
