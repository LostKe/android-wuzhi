package zs.com.wuzhi.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.wuzhi.R;

/**
 * Created by zhangshuqing on 16/7/25.
 */
public class LoginActivity extends BaseToolBarActivity implements View.OnClickListener {

    @BindView(R.id.et_username)
    EditText userName;

    @BindView(R.id.et_password)
    EditText password;

    @BindView(R.id.btn_login)
    Button bt_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    boolean isBackHomeVisible() {
        return false;
    }

    @Override
    String getToolBarTitle() {
        return "登录";
    }

    @Override
    OnBackHomeClicklistener getOnBackHomeListener() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:

                break;
        }
    }
}
