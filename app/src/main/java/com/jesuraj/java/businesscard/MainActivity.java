package com.jesuraj.java.businesscard;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CardListAdapter cardListAdapter;
    private RecyclerView recyclerView;
    private CardViewModel cardViewModel;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerView);
        floatingActionButton=findViewById(R.id.floatingActionButton);
        cardListAdapter=new CardListAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(cardListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardViewModel=ViewModelProviders.of(this).get(CardViewModel.class);
        cardViewModel.getListAllCards().observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(List<Card> cards) {
                cardListAdapter.setCardList(cards);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager=getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                ComposeFragment composeFragment=new ComposeFragment();
                fragmentTransaction.replace(R.id.coordinator, composeFragment);
                fragmentTransaction.commit();
            }
        });



    }


}
