package com.httvc.zxingdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.httvc.zxingdemo.zxing.CaptureActivity;
import com.httvc.zxingdemo.zxing.common.BitmapUtils;
import com.httvc.zxingdemo.zxing.encode.QRCodeEncoder;

public class MainActivity extends AppCompatActivity {
    final int REQUEST_CAMERA = 101;
    private ImageView iv;

    private static final String USE_VCARD_KEY = "USE_VCARD";

    private QRCodeEncoder qrCodeEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.sss);

        findViewById(R.id.scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                    }else{
                        Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
                        startActivityForResult(openCameraIntent, 0);
                    }
                }else {
                    Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
                    startActivityForResult(openCameraIntent, 0);
                }
            }
        });


        findViewById(R.id.generate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String contentString = "第一次生成123adg";
                if (!contentString.equals("")) {
                    //根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
                    Bitmap qrCodeBitmap = null;
                    qrCodeBitmap = BitmapUtils.getCompressedBitmap(contentString);
                    iv.setImageBitmap(qrCodeBitmap);
                } else {
                    Toast.makeText(MainActivity.this, "Text can not be empty", Toast.LENGTH_SHORT).show();
                }
                 launchSearch("第一次生成123adg");
            }
        });
    }

    private void launchSearch(String text) {
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        int width = displaySize.x;
        int height = displaySize.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 7 / 8;

        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap(text);
            if (bitmap == null) {
                qrCodeEncoder = null;
                return;
            }

            // 最后编码成为一个二维码图片展示出来
        //    ImageView view = (ImageView) findViewById(R.id.image_view);
            iv.setImageBitmap(bitmap);

           /* TextView contents = (TextView) findViewById(R.id.contents_text_view);
            if (intent.getBooleanExtra(Intents.Encode.SHOW_CONTENTS, true)) {
                contents.setText(qrCodeEncoder.getDisplayContents());
                setTitle(qrCodeEncoder.getTitle());
            } else {
                contents.setText("");
                setTitle("");
            }*/
        } catch (WriterException e) {
            /*Log.w(TAG, "Could not encode barcode", e);
            showErrorMessage(R.string.msg_encode_contents_failed);*/
            qrCodeEncoder = null;
        }
      /*  Intent intent = new Intent(Intents.Encode.ACTION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intents.Encode.TYPE, Contents.Type.TEXT);
        intent.putExtra(Intents.Encode.DATA, text);
        intent.putExtra(Intents.Encode.FORMAT, BarcodeFormat.QR_CODE.toString());
        startActivity(intent);*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 取得權限
                    Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
                    startActivityForResult(openCameraIntent, 0);
                } else {
                    // 未取得權限
                    Toast.makeText(MainActivity.this,"没有获得权限",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

