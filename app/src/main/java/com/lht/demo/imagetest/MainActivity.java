package com.lht.demo.imagetest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        iv = (ImageView) findViewById(R.id.iv);
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / (1024 * 1024));
        Log.d("lht", "Max memory is:" + maxMemory + "MB");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.mipmap.aa, options);
        int imageHight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;
        Log.d("lht", "===============imageHight:" + imageHight);
        Log.d("lht", "===============imageWidth:" + imageWidth);
        Log.d("lht", "===============imageType:" + imageType);

        Bitmap bp = decodeSampleBitmapFromResource(getResources(), R.mipmap.aa, 100, 100);
        iv.setImageBitmap(bp);

        FileOutputStream f = null;
        BufferedOutputStream bos = null;
        try {

            Log.d("lht", "============1");
            File f1 = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/test.jpg");
            Log.d("lht", "============2");
            Log.d("lht", "============3");
            f = new FileOutputStream(f1);
            Log.d("lht", "============4");
            bos = new BufferedOutputStream(f);
            Log.d("lht", "============5");
            bp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            Log.d("lht", "============6");
            bos.flush();
            Log.d("lht", "============7");
            bos.close();
            Log.d("lht", "============8");
            f.close();
            Log.d("lht", "============9");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //原图片的宽和高
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            //计算出实际宽高和目标宽高比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            //选择图片比率中小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高一定大于等于目标的宽和高
            inSampleSize = heightRatio > widthRatio ? widthRatio : heightRatio;
        }
        return inSampleSize;
    }


    public static Bitmap decodeSampleBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        //只有当inJustDecodeBounds为true时能解析图片获得相应的图片宽高和内存,并且此时decodeResource方法
        // 返回的对象为null，当inJustDecodeBounds为false时返回的对象才为Bitmap对象
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        //使用新定义的inSampleSize值再次解析图片（即压缩图片）
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);

    }
}
