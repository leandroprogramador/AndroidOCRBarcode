package com.leandroaraujo.androidocr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class CropActivity extends AppCompatActivity {

    Bitmap bitmap;
    CropImageView cropImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        cropImageView = findViewById(R.id.cropImageView);
        try {
            bitmap = BitmapFactory.decodeStream(this
                    .openFileInput("orvil_temp_img"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        cropImageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crop_image_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.toString()){
            case "Cortar":
                Bitmap cropped = cropImageView.getCroppedImage();
                String imageName = "orvil_temp_img_cropped";
                try{
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    cropped.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    FileOutputStream fileOutputStream = openFileOutput(imageName, Context.MODE_PRIVATE);
                    fileOutputStream.write(bytes.toByteArray());
                    fileOutputStream.close();
                    setResult(RESULT_OK, new Intent(CropActivity.this, MainActivity.class).putExtra("action", "crop"));
                    finish();
                } catch (Exception ex){
                }
             break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED,returnIntent);
        finish();
    }
}
