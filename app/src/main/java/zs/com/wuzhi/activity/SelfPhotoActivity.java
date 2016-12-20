package zs.com.wuzhi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import zs.com.wuzhi.R;
import zs.com.wuzhi.application.AppApplication;
import zs.com.wuzhi.util.Constant;
import zs.com.wuzhi.util.ImageUtil;
import zs.com.wuzhi.util.WuzhiApi;
import zs.com.wuzhi.widget.MenuDialog;
import zs.com.wuzhi.widget.TouchImageView;

/**
 * 个人头像
 * Created by zhangshuqing on 16/9/14.
 */
public class SelfPhotoActivity extends BaseToolBarActivity {


    private Uri origUri;
    private File protraitFile;
    private Bitmap protraitBitmap;
    private String protraitPath;
    MenuDialog menuDialog;

    KProgressHUD hud;


    TouchImageView mTouchImageView;

    private String mImageUrl;
    protected static final int REQUEST_CODE_GETIMAGE_BYCROP = 1;
    protected static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 2;
    protected static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 3;

    private final static String FILE_SAVEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/wuzhi/Portrait/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_browse);
        ButterKnife.bind(this);
        hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false);
        setToolBarMenu(ToolBarMenu.MORE);
        init();

    }

    private void init() {
        hud.show();
        mImageUrl = getIntent().getStringExtra(Constant.SELF_IMG_URL);
        mImageUrl = mImageUrl.replaceAll("small_", "");//转换成获取大图的链接
        mTouchImageView = (TouchImageView) findViewById(R.id.photoview);
        loadImage(mTouchImageView, mImageUrl);

    }

    private void loadImage(final TouchImageView mTouchImageView, String mImageUrl) {
        Glide.with(this).load(mImageUrl).asBitmap().error(R.drawable.load_img_error).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mTouchImageView.setImageBitmap(resource);
                hud.dismiss();
                mTouchImageView.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    boolean isBackHomeVisible() {
        return true;
    }

    @Override
    String getToolBarTitle() {
        return "头像";
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
    OnMenuActionClickListener getOnMenuActionClickListener() {
        return new OnMenuActionClickListener() {
            @Override
            public void onClick() {
                //弹出pop 画面
                menuDialog = new MenuDialog(SelfPhotoActivity.this);
                menuDialog.show();
                menuDialog.setCancelable(true);
                menuDialog.setOnMenuClickListener(new MenuDialog.OnMenuClickListener() {
                    @Override
                    public void onClick(TextView menuItem) {
                        switch (menuItem.getId()) {
                            case R.id.menu1:
                                startCamerMakerPhoto();
                                break;
                            case R.id.menu2:
                                startImagePick();
                                break;
                            case R.id.menu3:
                                menuDialog.dismiss();
                                break;
                        }

                    }


                });
            }
        };
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_GETIMAGE_BYCROP://相册选图
                startActionCrop(data.getData());
                break;
            case REQUEST_CODE_GETIMAGE_BYCAMERA://相机照相
                startActionCrop(origUri);
                break;
            case REQUEST_CODE_GETIMAGE_BYSDCARD://上传
                Bundle extras=data.getExtras();
                if(extras!=null){
                    Bitmap photo=extras.getParcelable("data");
                }
                uploadImg();
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImg() {
        hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("正在上传...").setCancellable(false);
        hud.show();

        if(!TextUtils.isEmpty(protraitPath) && protraitFile.exists()){
            protraitBitmap=ImageUtil.loadImgThumbnail(protraitPath,200,200);
        }else{
            Toast.makeText(this, "图像不存在，请重新操作", Toast.LENGTH_SHORT).show();
            return;
        }

        if(protraitBitmap!=null){

            try {
                WuzhiApi.updateAvatar(AppApplication.context(),protraitFile, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        //更新当前图像
                        mTouchImageView.setImageBitmap(BitmapFactory.decodeFile(protraitPath));
                        Toast.makeText(getApplicationContext(), "头像更新成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getApplicationContext(), "头像更新失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hud.dismiss();
                        menuDialog.dismiss();
                    }
                });
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "图像不存在，请重新操作", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }


    }


    private void startImagePick() {
        Intent intent = new Intent();
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_GETIMAGE_BYCROP);
    }


    //调用相机功能
    private void startCamerMakerPhoto() {
        if (!checkSdCardMounted()) {
            Toast.makeText(this, "无法保存照片，请检查SD卡是否挂载", Toast.LENGTH_SHORT).show();
            return;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = "wuzhi_" + timeStamp + ".jpg";// 照片命名
        File out = new File(FILE_SAVEPATH, fileName);
        origUri = Uri.fromFile(out);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//camera应用
        intent.putExtra(MediaStore.EXTRA_OUTPUT, origUri);
        startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYCAMERA);
    }


    /**
     * 裁剪
     *
     * @param data 原始图片
     */
    private void startActionCrop(Uri data) {
        WindowManager windowManager= (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width=windowManager.getDefaultDisplay().getWidth();//屏幕宽度
        int height=windowManager.getDefaultDisplay().getHeight();
//        Intent intent = new Intent("com.android.camera.action.CROP");//调用系统的图片裁剪功能
//
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){//原本uri返回的是file:///...来着的 android4.4返回的是content:///...
//            //这里需要 指定到file
//            String path=ImageUtil.getImagePath(data,SelfPhotoActivity.this);
//            intent.setDataAndType(Uri.fromFile(new File(path)),"image/*");
//        }else {
//            intent.setDataAndType(data, "image/*");
//        }

        UCrop.Options options = new UCrop.Options();
        UCrop.of(data, getUploadTempFile())
                .withMaxResultSize(width, height)
                .withOptions(options)
                .withAspectRatio(width, height)
                .start(this,REQUEST_CODE_GETIMAGE_BYSDCARD);

//        intent.putExtra("output", this.getUploadTempFile());
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", width);// 裁剪框比例
//        intent.putExtra("aspectY", height);
//        intent.putExtra("outputX", width);// 输出图片大小
//        intent.putExtra("outputY", height);
//        intent.putExtra("scale", true);// 去黑边
//        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
//        //intent.putExtra("return-data", true); //将数据保留到Bitma 中返回
//        startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYSDCARD);//裁剪完成 开始上传图片
    }


    // 裁剪头像的绝对路径
    private Uri getUploadTempFile() {
        if (!checkSdCardMounted()) {
            Toast.makeText(this, "无法保存上传的头像，请检查SD卡是否挂载", Toast.LENGTH_SHORT).show();
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        // 照片命名
        String cropFileName = "wuzhi_" + timeStamp + ".jpg";
        // 裁剪头像的绝对路径
        protraitPath = FILE_SAVEPATH + cropFileName;
        protraitFile = new File(protraitPath);
        return Uri.fromFile(protraitFile);
    }




    private boolean checkSdCardMounted() {
        boolean r = false;
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            r = true;
            File savedir = new File(FILE_SAVEPATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        }
        return r;
    }
}
