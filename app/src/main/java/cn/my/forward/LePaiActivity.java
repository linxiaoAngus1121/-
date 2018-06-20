package cn.my.forward;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.my.forward.mvp.sourcequery.mvp.presenter.SourcePresenter;
import cn.my.forward.mvp.sourcequery.mvp.utils.MyLog;
import cn.my.forward.mvp.sourcequery.mvp.view.ILePaiView;

public class LePaiActivity extends AppCompatActivity implements SurfaceHolder.Callback, ILePaiView,
        View.OnClickListener {
    private static final int DEPOT_CODE = 1;
    private static final int FRONT = 1;//前置摄像头标记
    private static final int BACK = 2;//后置摄像头标记
    private static final String path = Environment.getExternalStorageDirectory().getPath() + File
            .separator +
            "12306.jpeg";
    private int CURRENTCAMERATYPE = -1;//当前打开的摄像头标记
    private static int USEDEFAULTWIDTH = 1080;
    private static int USEDEFAULTHEIGHT = 1920;
    private static final int DEFAULTWIDTH = 1080;
    private static final int DEFAULTHEIGHT = 1920;
    private Uri imageUri;
    private String imagePath;
    private TextView mTv;
    private SurfaceHolder surfaceHolder;
    private ProgressDialog dialog;
    private Camera camera;//摄像头
    private int frontIndex = 0;
    private int bakcIndex = 0;
    private SourcePresenter presenter = new SourcePresenter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_le_pai);
        mTv = (TextView) findViewById(R.id.tv_showresult);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.show_camera);
        ImageView mIv = (ImageView) findViewById(R.id.iv_lepai_tranform);
        ImageView mIvforTakePicture = (ImageView) findViewById(R.id.iv_lepai_takepicture);
        surfaceView.setOnClickListener(this);
        mIvforTakePicture.setOnClickListener(this);
        mIv.setOnClickListener(this);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);    //不显示标题
            actionBar.setDisplayHomeAsUpEnabled(true);      //显示返回的那个箭头
        }
    }

    private void initCamera() {
        camera.startPreview();
        camera.setDisplayOrientation(90);//将预览旋转90度
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DEPOT_CODE: //打开图片选择哪张图片
                if (resultCode == RESULT_CANCELED) {
                    return;//没有选择图片
                }
                if (Build.VERSION.SDK_INT >= 19) {  //7.0
                    handlerImageOnkitKat(data);
                } else {
                    handlerImagebeforekitKat(data);
                }
                presenter.lePai(imagePath);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lepai, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.photoAlbum:
                //选择图片后直接上传评分，不需要点击上传按钮了
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, DEPOT_CODE);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handlerImagebeforekitKat(Intent data) {
        imageUri = data.getData();
        imagePath = getImagePath(imageUri, null);
    }

    private String getImagePath(Uri externalContentUri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(externalContentUri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void handlerImageOnkitKat(Intent data) {
        imageUri = data.getData();
        if (DocumentsContract.isDocumentUri(this, imageUri)) {
            String documentId = DocumentsContract.getDocumentId(imageUri);
            if ("com.android.providers.media.documents".equals(imageUri.getAuthority())) {
                String id = documentId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.downloads.documents".equals(imageUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse
                        ("content://downloads/public_downloads"), Long.valueOf(documentId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            imagePath = getImagePath(imageUri, null);
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            imagePath = imageUri.getPath();
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        opemCamera(CURRENTCAMERATYPE);
    }

    private void opemCamera(int index) {
        try {
            if (index == -1) {  //第一次进来
                //先使用前置的
                int CammeraIndex = FindFrontCamera();
                if (CammeraIndex != -1) {
                    frontIndex = CammeraIndex;
                }
                if (CammeraIndex == -1) {
                    //如果前置不存在，再使用后置的
                    CammeraIndex = FindBackCamera();
                    bakcIndex = CammeraIndex;
                }
                camera = Camera.open(CammeraIndex);
                setCameraParameters();
                camera.setPreviewDisplay(surfaceHolder);
            } else if (index == BACK) { //要打开后置的了
                if (bakcIndex == 0) {
                    bakcIndex = FindBackCamera();
                }
                camera = Camera.open(bakcIndex);
                setCameraParameters();
                camera.setPreviewDisplay(surfaceHolder);
            } else {  //打开前置
                camera = Camera.open(frontIndex);
                setCameraParameters();
                camera.setPreviewDisplay(surfaceHolder);
                CURRENTCAMERATYPE = FRONT;  //这里要把他的当前状态设置为前置
            }
            initCamera();
        } catch (Exception e) {
            if (null != camera) {
                camera.release();
                camera = null;
            }
            e.printStackTrace();
            Toast.makeText(LePaiActivity.this, "启动摄像头失败,请开启摄像头权限", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置拍照的质量
     */
    private void setCameraParameters() {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        parameters.setPictureFormat(ImageFormat.JPEG);
        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
        if (sizes == null) {
            Toast.makeText(this, "您的相机暂不支持高分辨率，可能影响评分", Toast.LENGTH_SHORT).show();
            return;
        }
        int size = sizes.size();
        //遍历找出合适的分辨率
        for (int i = 0; i < size; i++) {
            int width = sizes.get(i).width;
            int height = sizes.get(i).height;
            MyLog.i(width + "/////123+++++" + height);
            if (width == DEFAULTHEIGHT && height == DEFAULTWIDTH) {
                MyLog.i("找到了1080*1920");
                break;
            }
            if (i == size - 1) {    //到最后还是没有找到，则采用最高分辨率那个,第一个的像素是最高的
                USEDEFAULTHEIGHT = sizes.get(0).width;
                USEDEFAULTWIDTH = sizes.get(0).height;
                MyLog.i("没找到1080*1920，使用支持最高的" + USEDEFAULTHEIGHT + USEDEFAULTWIDTH);
            }
        }
        parameters.setJpegQuality(95);  //设置图片质量
        //宽一定要大于高，所以这里要反过来
        parameters.setPictureSize(USEDEFAULTHEIGHT, USEDEFAULTWIDTH);
        camera.setParameters(parameters);
    }

    /**
     * 选择前置摄像头
     *
     * @return 是否存在，不存在则返回-1
     */
    private int FindFrontCamera() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                CURRENTCAMERATYPE = FRONT;
                return camIdx;
            }
        }
        return -1;
    }

    /**
     * 选择后置摄像头
     *
     * @return 是否存在，不存在则返回-1
     */
    private int FindBackCamera() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                CURRENTCAMERATYPE = BACK;
                return camIdx;
            }
        }
        return -1;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        initCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        MyLog.i("surfaceDestroyed");
        if (null != camera) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void showDialog() {
        if (dialog == null) {
            dialog = ProgressDialog.show(LePaiActivity.this, "提示",
                    "客官请稍等，正在努力上传", true,
                    false, null);
        } else {
            dialog.show();
        }
    }

    @Override
    public void showInformationSuccess(String s) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        //回显数据成功
        // Toast.makeText(this, "数据显示成功", Toast.LENGTH_SHORT).show();
        mTv.setText(s);
        mTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void showInformationFailure(String s) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        //回显数据失败
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    /**
     * 切换前置和后置镜头
     */
    private void changeCamera() {
        camera.stopPreview();
        camera.release();
        if (CURRENTCAMERATYPE == FRONT) {
            opemCamera(BACK);
        } else if (CURRENTCAMERATYPE == BACK) {
            opemCamera(FRONT);
        }
    }


    @Override
    protected void onDestroy() {
        presenter.clearAll();
        presenter = null;
        surfaceHolder = null;
        File file = new File(path);
        if (file.exists()) {
            boolean b = file.delete();
            if (b) {
                MyLog.i("删除文件成功");
            }
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_lepai_tranform:    //切换前后摄像头
                changeCamera();
                break;
            case R.id.show_camera:      //对焦
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success) {
                            initCamera();//实现相机的参数初始化
                            camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
                        }
                    }
                });
                break;
            case R.id.iv_lepai_takepicture:     //拍照
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        Matrix m = new Matrix();
                        if (CURRENTCAMERATYPE == FRONT) {   //当前是前置的
                            m.setRotate(-90, (float) bitmap.getWidth() / 2, (float) bitmap
                                    .getHeight()
                                    / 2);
                        } else {                            //后置的
                            m.setRotate(90, (float) bitmap.getWidth() / 2, (float) bitmap
                                    .getHeight()
                                    / 2);
                        }
                        final Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                                bitmap.getHeight(), m, true);
                        FileOutputStream outputStream = null;
                        try {
                            outputStream = new FileOutputStream(path);
                            bm.compress(Bitmap.CompressFormat.JPEG, 95, outputStream);
                            Toast.makeText(LePaiActivity.this, "拍照成功", Toast.LENGTH_SHORT)
                                    .show();
                            initCamera();

                            presenter.lePai(path);

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (outputStream != null) {
                                try {
                                    bitmap.recycle();
                                    bm.recycle();
                                    outputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mTv.getVisibility() == View.VISIBLE) {
            mTv.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

}
