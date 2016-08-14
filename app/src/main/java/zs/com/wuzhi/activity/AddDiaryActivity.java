package zs.com.wuzhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpResponseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import zs.com.wuzhi.R;
import zs.com.wuzhi.application.AppApplication;
import zs.com.wuzhi.util.ResponseUtil;
import zs.com.wuzhi.util.WuzhiApi;

/**
 * Created by zhangshuqing on 16/8/6.
 */
public class AddDiaryActivity  extends BaseToolBarActivity{

    @BindView(R.id.tv_add_diary_content)
    EditText tv_add_diary_content;

    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        hud=KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("正在发表...").setCancellable(true);
        ButterKnife.bind(this);
    }

    @Override
    boolean isBackHomeVisible() {
        return true;
    }

    @Override
    String getToolBarTitle() {
        return "日记";
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
               final String diaryContent=tv_add_diary_content.getText().toString();
                if(TextUtils.isEmpty(diaryContent)){
                    Toast.makeText(getApplicationContext(),"什么都没有写哟",Toast.LENGTH_SHORT).show();
                    return;
                }
                //检查登录状态
                if(!AppApplication.context().isLogin()){
                    Toast.makeText(getApplicationContext(),"未登录",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                }else{

                    hud.show();
                    //获取crsToken
                    WuzhiApi.getDiaryHtml(new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String diaryHtml=new String(responseBody);
                            String crsyToken= ResponseUtil.getCsrfToken(diaryHtml);
                            if(TextUtils.isEmpty(crsyToken)){
                                hud.dismiss();
                                Toast.makeText(getApplicationContext(),"服务器错误，请稍后再试!",Toast.LENGTH_SHORT).show();
                            }else {
                                WuzhiApi.postDiary(diaryContent,crsyToken, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        String s=new String(responseBody);
                                        Toast.makeText(getApplicationContext(),"发表失败，请稍后在试!",Toast.LENGTH_SHORT).show();
                                        hud.dismiss();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        if(302==statusCode){
                                            //获取到重定向状态码，表示成功
                                            finish();
                                        }else{
                                            Toast.makeText(getApplicationContext(),"发表失败，请稍后在试!",Toast.LENGTH_SHORT).show();
                                        }
                                        hud.dismiss();
                                    }
                                });


                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            hud.dismiss();
                            Toast.makeText(getApplicationContext(),"服务器错误，请稍后再试!",Toast.LENGTH_SHORT).show();
                        }
                    });




                }
            }
        };
    }
}
