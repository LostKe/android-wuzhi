package zs.com.wuzhi.activity;

import android.os.Bundle;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.wuzhi.R;
import zs.com.wuzhi.widget.SwitchView;

/**
 * Created by zhangshuqing on 16/7/24.
 */
public class PrimaryActivity extends BaseToolBarActivity implements SwitchView.OnStateChangedListener{

    @BindView(R.id.primary_switch)
    SwitchView switchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
       switchView.setOnStateChangedListener(this);
    }

    @Override
    boolean isBackHomeVisible() {
        return true;
    }


    @Override
    String getToolBarTitle() {
        return "隐私";
    }

    @Override
    public OnBackHomeClicklistener getOnBackHomeListener() {
        OnBackHomeClicklistener onBackHomeClicklistener = new OnBackHomeClicklistener() {
            @Override
            public void backHomeClick() {
                finish();
            }
        };
        return onBackHomeClicklistener;
    }

    @Override
    public void toggleToOn() {
        //打开开关
        Toast.makeText(this,"on",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toggleToOff() {
        //关闭开关
        Toast.makeText(this,"off",Toast.LENGTH_SHORT).show();
    }
}
