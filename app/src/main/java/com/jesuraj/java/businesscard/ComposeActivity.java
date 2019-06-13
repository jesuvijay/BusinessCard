package com.jesuraj.java.businesscard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ComposeActivity extends AppCompatActivity implements RecyclerViewClickListener {


    private static final String TAG = "ComposeActivity";
    public static final int REQUEST_CODE_FR = 101;
    public static final int REQUEST_CODE_CAMERA = 156;
    public static final int REQUEST_CODE_BK = 102;
    public static final int REQUEST_CODE_PR = 103;
    private ImageView ivFrontView, ivBackView;
    private String frontImgPath, backImgPath;
    private EditText etName, etDesc, etComments;
    private String imgFilePath;
    private File photoFile = null;
    private String mCurrenPath = "";
    private Uri uri = null;
    private Button buttonSave, btnAdd;
    private CardViewModel cardViewModel;

    private RecyclerView recyclerView;
    private ProductAdaper prodcutAdaper;

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
        btnAdd = findViewById(R.id.btnAdd);
        cardViewModel = ViewModelProviders.of(this).get(CardViewModel.class);
        ivFrontView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    dispatchTakePictureIntent(REQUEST_CODE_FR);
                }
            }
        });

        ivBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    dispatchTakePictureIntent(REQUEST_CODE_BK);
                }
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cName = etName.getText().toString();
                String cDescription = etDesc.getText().toString();
                String cComments = etComments.getText().toString();
                Card card = new Card(cName, cDescription, cComments, frontImgPath, backImgPath, Constants.getDateTime(), attachProducts());
                cardViewModel.insert(card);
                resetCards();
//                } else
//                    Toast.makeText(ComposeActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView = findViewById(R.id.rvCardList);
        prodcutAdaper = new ProductAdaper(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(prodcutAdaper);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
//                    bImg = false;
                    dispatchTakePictureIntent(REQUEST_CODE_PR);
                }
            }
        });
    }

    private String attachProducts() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ProductData productData : prodcutAdaper.getPathList()) {
            stringBuilder.append(productData.getPath());
            stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }

    private void resetCards() {
        etComments.setText("");
        etDesc.setText("");
        etName.setText("");
        ivBackView.setImageDrawable(getResources().getDrawable(R.drawable.img));
        ivFrontView.setImageDrawable(getResources().getDrawable(R.drawable.img));
        prodcutAdaper.clearData();
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

    private void dispatchTakePictureIntent(int rCode) {
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
                    ComposeActivity.this.startActivityForResult(takePictureIntent, rCode);
                }
            } catch (IOException e) {
                Log.i(TAG, "dispatchTakePictureIntent: " + e.toString());
            }


        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            if (requestCode == REQUEST_CODE_FR) {
                ivFrontView.setImageBitmap(ThumbnailUtils.extractThumbnail(bitmap, 100, 100));
            } else if (requestCode == REQUEST_CODE_BK) {
                ivBackView.setImageBitmap(bitmap);
            } else
                prodcutAdaper.addData(new ProductData(photoFile.getAbsolutePath(), ThumbnailUtils.extractThumbnail(bitmap, 100, 100)));
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

    @Override
    public void onClick(View view, int position) {

    }

    @Override
    public void onLongClick(View view, int position) {

    }
}
