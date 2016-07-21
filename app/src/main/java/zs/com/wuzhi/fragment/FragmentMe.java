package zs.com.wuzhi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zs.com.wuzhi.R;

/**
 * Created by zhangshuqing on 16/7/19.
 */
public class FragmentMe extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_me,null);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
