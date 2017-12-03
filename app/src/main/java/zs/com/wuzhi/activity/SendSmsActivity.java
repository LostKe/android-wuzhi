package zs.com.wuzhi.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.wuzhi.R;
import zs.com.wuzhi.adapter.BaseListAdapter;
import zs.com.wuzhi.adapter.UserPhoneAdapter;
import zs.com.wuzhi.bean.User;
import zs.com.wuzhi.util.Constant;

/**
 * Created by zhangshuqing on 2017/12/3.
 */
public class SendSmsActivity extends BaseToolBarActivity implements View.OnClickListener, BaseListAdapter.Callback {

    private static final String TAG = "SendSmsActivity";

    private static final String SENT_SMS_ACTION = "sms_send_action";
    private static final String KEY_PHONENUM = "phone_num";
    private static final String INDEX = "index";

    @BindView(R.id.bt_select_all)
    Button bt_select_all;

    @BindView(R.id.bt_select_un_all)
    Button bt_select_un_all;

    @BindView(R.id.bt_send_bottom)
    Button bt_send_bottom;

    @BindView(R.id.lv_user_phone)
    protected ListView mListView;


    KProgressHUD hud;

    List<User> userList = new ArrayList<>();
    List<User> needSendList = new ArrayList<>();

    UserPhoneAdapter adapter;

    String smsMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userList.clear();
        setContentView(R.layout.activity_phone_detail);
        ButterKnife.bind(this);
        bt_send_bottom.setOnClickListener(this);
        bt_select_all.setOnClickListener(this);
        bt_select_un_all.setOnClickListener(this);


        hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please wait").setCancellable(true);
        hud.show();
        adapter = new UserPhoneAdapter(this);
        mListView.setAdapter(adapter);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras().getBundle(Constant.SETTING_BUNDLE);
        String userMsg = bundle.getString("USER_PHONE");
        smsMsg = bundle.getString("SMS_MSG");
        JSONArray array = JSONArray.parseArray(userMsg);
        for (int i = 0; i < array.size(); i++) {
            User user = new User();
            JSONObject object = array.getJSONObject(i);
            user.setUserName(object.getString("userName"));
            user.setPhone(object.getString("phone"));
            userList.add(user);
        }
        adapter.addItem(userList);
        hud.dismiss();

        mReceiver = new SMSSendResultReceiver();
        IntentFilter filter = new IntentFilter(SENT_SMS_ACTION);
        registerReceiver(mReceiver, filter);
    }


    @Override
    boolean isBackHomeVisible() {
        return true;
    }

    @Override
    String getToolBarTitle() {
        return "联系人列表";
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send_bottom:
                //Toast.makeText(this, "发送消息", Toast.LENGTH_SHORT).show();
                needSendList.clear();
                for (User user : userList) {
                    if (user.isSelect()) {
                        needSendList.add(user);
                    }
                }

                if(needSendList.size()==0){
                    Toast.makeText(this,"最少选择一个发送",Toast.LENGTH_SHORT).show();
                    return;
                }

                String dialogMsg = "选择了" + needSendList.size() + "个用户确认发送";
                boolean alertDilog=false;
                new AlertDialog.Builder(this).setTitle(dialogMsg)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //放到线程里面去做
                                final Handler handler=new ConformHandler();
                                Executors.newSingleThreadExecutor().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        Message message=Message.obtain();
                                        message.arg1=1;
                                        handler.sendMessage(message);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;
            case R.id.bt_select_all:
                for (User user : userList) {
                    user.setSelect(true);
                }
                adapter.notifyDataSetChanged();
                //全选
                break;
            case R.id.bt_select_un_all:
                for (User user : userList) {
                    user.setSelect(user.isSelect() ? false : true);
                }
                adapter.notifyDataSetChanged();
                //反选
                break;
        }
    }


    @Override
    public Context getContext() {
        return this;
    }


    /**
     * 直接调用短信接口发短信    如果群发可以循环调用
     *
     * @param phoneNumber
     * @param message
     */
    public void sendSMS(int position, String phoneNumber, String message) {
        try {

            adapter.notifyDataSetChanged();
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        //拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        Intent itSend = new Intent(SENT_SMS_ACTION);
        itSend.putExtra(KEY_PHONENUM, phoneNumber);
        itSend.putExtra(INDEX, position);
        PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0, itSend, PendingIntent.FLAG_UPDATE_CURRENT);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, sentPI, null);
        }
    }


    SMSSendResultReceiver mReceiver;

    // 监听短信发送状态
    class SMSSendResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (SENT_SMS_ACTION.equals(intent.getAction())) {
                int index = intent.getIntExtra(INDEX, 0);
                User user = userList.get(index);
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        // 发送成功
                        user.setState(1);
                        //Toast.makeText(context, "Send Message to " + phoneNum + " success!", Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();
                        //更改状态
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                    default:
                        // 发送失败
                        //Toast.makeText(context, "Send Message to " + phoneNum + " fail!", Toast.LENGTH_LONG).show();
                        user.setState(2);
                        adapter.notifyDataSetChanged();
                        //更改状态
                        break;
                }

            }


        }
    }


    class ConformHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            int confirm=msg.arg1;
            if(confirm==1){
                // 点击“确认”后的操作
                for (int i = 0; i < userList.size(); i++) {
                    User user = userList.get(i);
                    if(user.isSelect()){
                        sendSMS(i, user.getPhone(), smsMsg);
                    }

                }
            }

        }
    }


}