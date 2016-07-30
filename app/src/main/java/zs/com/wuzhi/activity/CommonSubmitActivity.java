package zs.com.wuzhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.wuzhi.R;
import zs.com.wuzhi.util.Constant;

/**
 * Created by zhangshuqing on 16/7/30.
 */
public class CommonSubmitActivity extends BaseToolBarActivity {

    String title;

    String content;

    @BindView(R.id.et_commonsubmit)
    EditText et_common_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        ButterKnife.bind(this);

        init();

    }

    private void init() {
        Intent intent= getIntent();
        Bundle bundle=intent.getExtras();
        String content=bundle.getString(Constant.CONTENT);
        et_common_submit.setText(content);
        //设置光标在文字的末尾处
        et_common_submit.setSelection(content.length());
    }


    @Override
    boolean isBackHomeVisible() {
        return true;
    }

    @Override
    String getToolBarTitle() {
        Intent intent= getIntent();
        Bundle bundle=intent.getExtras();
        title=bundle.getString(Constant.TITLE);
        return title;
    }

    @Override
    OnBackHomeClicklistener getOnBackHomeListener() {
        return new OnBackHomeClicklistener() {
            @Override
            public void backHomeClick() {
                finish();
            }
        };
    }

    @Override
    boolean needCompleteButton() {
       return true;
    }

    @Override
    OnCompleteClickListener getOnCompleteListener() {

        return new OnCompleteClickListener() {
            @Override
            public void onCompleteClick() {
                Toast.makeText(getApplicationContext(),title,Toast.LENGTH_SHORT).show();
            }
        };
    }
}
