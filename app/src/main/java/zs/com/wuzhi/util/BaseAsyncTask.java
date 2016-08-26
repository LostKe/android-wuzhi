package zs.com.wuzhi.util;

import android.os.AsyncTask;

import java.net.URL;

/**
 *
 * 解决 非UI线程不能直接更新UI的组件
 * 相对于Handler来说 显得更加轻量级
 *
 * Created by youx on 2016-08-26.
 *
 * Params:启动任务执行的输入参数的类型
 * Progress:后台任务完成的进度值的类型
 * Result:后台任务完成后返回结果的类型
 */
public class BaseAsyncTask extends AsyncTask<URL,Integer,String> {
    @Override
    protected String doInBackground(URL... params) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
