package com.leandroaraujo.androidocr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by leandro.araujo on 05/02/2018.
 */

public class MyCamera {
    private Activity context;
    private int REQUEST_TAKE_PHOTO = 100;


    private String mCurrentPhotoPath;
    public MyCamera(Activity context) {
        this.context = context;
    }

    public void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager() ) != null){
            File photoFile = null;
            try{
                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                photoFile = File.createTempFile("_photoApp", ".jpg", storageDir);
                mCurrentPhotoPath = "file:" + photoFile.getAbsolutePath();
            } catch (IOException exception){
                Toast.makeText(context, "Erro ao tirar a foto", Toast.LENGTH_SHORT).show();
            }

            if(photoFile != null){
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                context.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public int getRequestCode() {
        return REQUEST_TAKE_PHOTO;
    }

    public String getmCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }






}
