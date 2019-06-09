package com.jesuraj.java.businesscard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ComposeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ComposeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "ComposeFragment";
    public  static final int REQUEST_CODE_CAMERA = 101;
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

    private OnFragmentInteractionListener mListener;

    public ComposeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComposeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComposeFragment newInstance(String param1, String param2) {
        ComposeFragment fragment = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_compose, container, false);
        ivFrontView = view.findViewById(R.id.ivFrontView);
        ivBackView = view.findViewById(R.id.ivBackView);
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

                if (!TextUtils.isEmpty(etName.getText()) && !TextUtils.isEmpty(etComments.getText()) && !TextUtils.isEmpty(etDesc.getText())) {
                    String cName = etName.getText().toString();
                    String cDescription = etDesc.getText().toString();
                    String cComments = etComments.getText().toString();
                    Card card = new Card(cName, cDescription, cComments, frontImgPath, backImgPath, Constants.getDateTime());
                    cardViewModel.insert(card);
                    resetCards();
                } else
                    Toast.makeText(getActivity(), "Fields are empty", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void resetCards() {
        etComments.setText("");
        etDesc.setText("");
        etName.setText("");
        ivBackView.setImageDrawable(getResources().getDrawable(R.drawable.img));
        ivFrontView.setImageDrawable(getResources().getDrawable(R.drawable.img));

    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            return false;
        } else return true;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HH_mm_ss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        // save the file path
        mCurrenPath = image.getAbsolutePath();
        return image;


    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
                if (photoFile != null) {
                    Log.i(TAG, "dispatchTakePictureIntent: " + photoFile.getAbsolutePath());

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                        uri = FileProvider.getUriForFile(getActivity(), "com.jesuraj.java.businesscard", photoFile);
                    else
                        uri = Uri.fromFile(photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    getActivity().startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA);
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_CAMERA);

        }
    }
}
