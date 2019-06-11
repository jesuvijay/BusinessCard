package com.jesuraj.java.businesscard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ComposeActivity extends AppCompatActivity {


    private static final String TAG = "ComposeActivity";
    public static final int REQUEST_CODE_CAMERA = 101;
    private ImageView ivFrontView, ivBackView;
    private String frontImgPath, backImgPath;
    private EditText etName, etDesc, etComments;
    private boolean bImg = false;
    private String imgFilePath;
    private File photoFile = null;
    private String mCurrenPath = "";
    private Uri uri = null;
    private Button buttonSave;
    private CardViewModel cardViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        ivFrontView = findViewById(R.id.ivFrontView);
        ivBackView = findViewById(R.id.ivBackView);
        etName = findViewById(R.id.etName);
        etComments = findViewById(R.id.etComments);
        etDesc = findViewById(R.id.etDesc);
        buttonSave = findViewById(R.id.buttonSave);
        cardViewModel = ViewModelProviders.of(this).get(CardViewModel.class);
        ivFrontView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    bImg = true;
                    dispatchTakePictureIntent();
                }
            }
        });
        ivBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    bImg = false;
                    dispatchTakePictureIntent();
                }
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (!TextUtils.isEmpty(etName.getText()) && !TextUtils.isEmpty(etComments.getText()) && !TextUtils.isEmpty(etDesc.getText())) {
                String cName = etName.getText().toString();
                String cDescription = etDesc.getText().toString();
                String cComments = etComments.getText().toString();
                Card card = new Card(cName, cDescription, cComments, frontImgPath, backImgPath, Constants.getDateTime());
                cardViewModel.insert(card);
                resetCards();
//                } else
//                    Toast.makeText(ComposeActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetCards() {
        etComments.setText("");
        etDesc.setText("");
        etName.setText("");
        ivBackView.setImageDrawable(getResources().getDrawable(R.drawable.img));
        ivFrontView.setImageDrawable(getResources().getDrawable(R.drawable.img));

    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(ComposeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            return false;
        } else return true;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HH_mm_ss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = ComposeActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        // save the file path
        mCurrenPath = image.getAbsolutePath();
        return image;


    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(ComposeActivity.this.getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
                if (photoFile != null) {
                    Log.i(TAG, "dispatchTakePictureIntent: " + photoFile.getAbsolutePath());

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                        uri = FileProvider.getUriForFile(ComposeActivity.this, "com.jesuraj.java.businesscard", photoFile);
                    else
                        uri = Uri.fromFile(photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    ComposeActivity.this.startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA);
                }
            } catch (IOException e) {
                Log.i(TAG, "dispatchTakePictureIntent: " + e.toString());
            }


        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {

            Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            if (bImg) {

                ivFrontView.setImageBitmap(bitmap);
                frontImgPath = photoFile.getAbsolutePath();
//                        saveImage(bitmap, currentDateFormat());
            } else {
                ivBackView.setImageBitmap(bitmap);
                backImgPath = photoFile.getAbsolutePath();
//                        saveImage(bitmap, currentDateFormat());
            }

        }
    }


//    private void saveImage(Bitmap bitmap, String currentDate) {
//        File fileRoot = new File(root + "/" + getString(R.string.app_name));
//
//        if (!fileRoot.exists())
//            fileRoot.mkdirs();
//
//        File file = new File(fileRoot.getAbsolutePath(), currentDate + ".png");
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
//            fileOutputStream.flush();
//            fileOutputStream.close();
//            Toast.makeText(this, "image saved", Toast.LENGTH_SHORT).show();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            Log.d(TAG, "saveImage: " + e.toString());
//        } catch (IOException e) {
//            Log.d(TAG, "saveImage: " + e.toString());
//        }
//    }

//    private String currentDateFormat() {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss", Locale.US);
//        return simpleDateFormat.format(new Date());
//    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ComposeActivity.this, Manifest.permission.CAMERA)) {

        } else {
            ActivityCompat.requestPermissions(ComposeActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_CAMERA);

        }
    }
}
