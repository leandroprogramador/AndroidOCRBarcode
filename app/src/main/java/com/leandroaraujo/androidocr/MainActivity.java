package com.leandroaraujo.androidocr;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    MyCamera myCamera;
    ImageView imageView;
    TextView txtRecognizer;
    TextRecognizer recognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myCamera = new MyCamera(MainActivity.this);
        imageView = findViewById(R.id.imageView);
        txtRecognizer = findViewById(R.id.textView);

        recognizer = new TextRecognizer.Builder(getApplicationContext()).build();


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPermissions();
            }
        });
    }


    private void getPermissions(){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED  ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else{
                myCamera.dispatchTakePictureIntent();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    myCamera.dispatchTakePictureIntent();
                } else{
                    Toast.makeText(this, "É necessário dar as permissões.", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == myCamera.getRequestCode()){
            try {
                Bitmap bm1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(myCamera.getmCurrentPhotoPath())));
                String imageName = "orvil_temp_img";
                try{
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bm1.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    FileOutputStream fileOutputStream = openFileOutput(imageName, Context.MODE_PRIVATE);
                    fileOutputStream.write(bytes.toByteArray());
                    fileOutputStream.close();
                    startActivityForResult(new Intent(MainActivity.this, CropActivity.class),001);
                } catch (Exception ex){


                }
//

            } catch (java.io.IOException e) {
                Toast.makeText(getApplicationContext(), "Não foi possível capturar a foto!", Toast.LENGTH_LONG).show();
            }
        }

        if(data!= null){
            if(data.hasExtra("action")) {
                Bitmap bitmapCropped = null;
                try {
                    bitmapCropped = BitmapFactory.decodeStream(this
                            .openFileInput("orvil_temp_img_cropped"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Frame imageFrame = new Frame.Builder().setBitmap(bitmapCropped).build();
                final SparseArray<TextBlock> textBlock = recognizer.detect(imageFrame);
                txtRecognizer.setText("");
                if(textBlock.size() > 0){
                    txtRecognizer.post(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder stringBuilder = new StringBuilder();
                            for(int i = 0; i< textBlock.size(); i++){
                                TextBlock item = textBlock.valueAt(i);
                                stringBuilder.append(item.getValue());
                                stringBuilder.append("\n");
                            }
                            txtRecognizer.setText(stringBuilder.toString().replaceAll("[^\\d.]", ""));
                        }
                    });
                }

            }
        }
    }
}
