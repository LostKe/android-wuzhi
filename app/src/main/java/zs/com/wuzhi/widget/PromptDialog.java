package zs.com.wuzhi.widget;

import android.content.Context;
import android.widget.ImageView;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 提示dialog
 *      图片，和title 可以自定义
 *      自动消失
 * Created by youx on 2016-09-20.
 */
public class PromptDialog extends KProgressHUD {

    public static   int DEFAULT_DISMISS_TIME=1000;
    private Context context;
    private int imgResId;
    private String title;

    public PromptDialog(Context context) {
        super(context);
    }

    /**
     *
     * @param context
     * @param imgResId  自定义图片
     * @param title      自定义title
     */
    public PromptDialog(Context context,int imgResId,String title){
        super(context);
        this.context=context;
        this.imgResId=imgResId;
        this.title=title;

    }

    public void setDismissTime(int distance){
        DEFAULT_DISMISS_TIME=distance;
    }


    public void  showDialog() {
        ImageView imageView=new ImageView(context);
        imageView.setImageResource(imgResId);
       final KProgressHUD hud= create(context).setCustomView(imageView)
                .setLabel(title).show();
        final  Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                hud.dismiss();
            }
        },DEFAULT_DISMISS_TIME);

    }
}
