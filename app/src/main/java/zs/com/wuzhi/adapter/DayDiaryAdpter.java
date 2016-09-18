package zs.com.wuzhi.adapter;

import zs.com.wuzhi.R;
import zs.com.wuzhi.bean.DiarySummary;

/**
 * Created by zhangshuqing on 16/8/21.
 */
public class DayDiaryAdpter extends BaseListAdapter<DiarySummary> {

    public DayDiaryAdpter(Callback callback) {
        super(callback);
    }

    @Override
    protected void convert(ViewHolder vh, DiarySummary item, int position) {
        //设置日期
        vh.setText(R.id.tv_my_diary_time,item.getCurrentDay());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.my_diary_item;
    }

}
