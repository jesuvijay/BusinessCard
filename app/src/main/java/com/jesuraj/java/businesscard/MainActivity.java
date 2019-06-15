package com.jesuraj.java.businesscard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements CardListAdapter.CardClickListener {
    private static final String TAG = "MainActivity";
    public static final String CARD_DETAILS = "card_details";
    private CardListAdapter cardListAdapter;
    private RecyclerView recyclerView;
    private CardViewModel cardViewModel;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        cardListAdapter = new CardListAdapter(this, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(cardListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardViewModel = ViewModelProviders.of(this).get(CardViewModel.class);
        cardViewModel.getListAllCards().observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(List<Card> cards) {
                cardListAdapter.setCardList(cards);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//               FragmentManager fragmentManager=getSupportFragmentManager();
//               DialogFragment composeFragment=new ComposeFragment();
//               composeFragment.show(fragmentManager,"name");
                Intent intent = new Intent(MainActivity.this, ComposeActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.export_csv:
                // export operation
                new ExportCsv(this, cardListAdapter.getCardList()).execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onClick(int position) {
        showDetails(position);
    }

    @Override
    public void onLongClick(final int position) {
//        new android.app.AlertDialog()
        new android.app.AlertDialog.Builder(MainActivity.this).setMessage("Do you want to delete this data ?")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Card card = cardListAdapter.getItem(position);
                        deleteAllFiles(card);
                        cardViewModel.delete(card);

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void deleteAllFiles(Card card) {
        try {
            // backCard image
            new File(card.getBimgpath()).delete();

            new File(card.getFimgpath()).delete();

            String[] mProductImg = card.getProductPhotos().split(";");
            for (String mdata : mProductImg) {
                new File(mdata).delete();
            }
            Toast.makeText(this, "Files deleted", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d(TAG, "deleteAllFiles: ");
            Toast.makeText(this, "Unable to delete files", Toast.LENGTH_SHORT).show();
        }


    }


    private void showDetails(int position) {
        Intent intenr = new Intent(this, ComposeActivity.class);
        intenr.putExtra(CARD_DETAILS, cardListAdapter.getItem(position));
        startActivity(intenr);
    }

    private static class ExportCsv extends AsyncTask<Void, Void, Void> {

        File exportdir;
        private List<Card> cardLis;
        private String SEPARATOR = ",";
        private String DELIMITER = "\n";
        private WeakReference<MainActivity> activityWeakReference;


        public ExportCsv(MainActivity activity, List<Card> cardLis) {
            this.cardLis = cardLis;
            activityWeakReference = new WeakReference<MainActivity>(activity);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            exportdir = new File(Environment.getExternalStorageDirectory(), "/BusinessCard/Excel_Report");
            if (!exportdir.exists()) {
                exportdir.mkdirs();

            }
            File file = new File(exportdir, "businessCard.csv");
            if (file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file);
//                int usrid, String cmpyname, String description, String comments, String fimgpath, String bimgpath, String datetime
                fileWriter.append("Id").append(SEPARATOR)
                        .append("Company name").append(SEPARATOR)
                        .append("Description").append(SEPARATOR)
                        .append("Comments").append(SEPARATOR)
                        .append("FrontImagePath").append(SEPARATOR)
                        .append("BackImagePath").append(SEPARATOR)
                        .append("ProductPhotos").append(SEPARATOR)
                        .append("Datetime").append(SEPARATOR)
                        .append(DELIMITER);

                for (Card card : cardLis) {
//                    fileWriter.append(String.valueOf(card.getUsrid()));
//                    fileWriter.append(SEPARATOR);
//                    fileWriter.append(card.getCmpyname());
//                    fileWriter.append(SEPARATOR);
//                    fileWriter.append(card.getDescription());
//                    fileWriter.append(SEPARATOR);
//                    fileWriter.append(card.getComments());
//                    fileWriter.append(SEPARATOR);
//                    fileWriter.append((card.getFimgpath().trim()));
////                    fileWriter.append(SEPARATOR);
//                    fileWriter.append((card.getBimgpath()));
//                    fileWriter.append(SEPARATOR);
//                    fileWriter.append(DELIMITER);

                    fileWriter.append(java.lang.String.format(Locale.US, "%d%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s", card.getUsrid(), SEPARATOR, removeSplitters(card.getCmpyname()), SEPARATOR, card.getDescription(), SEPARATOR, removeSplitters(card.getComments()), SEPARATOR, removeFILimiters(card.getFimgpath()), SEPARATOR, removeFILimiters(card.getBimgpath()), SEPARATOR, removeDelimiters(card.getProductPhotos()), SEPARATOR, card.getDatetime(), DELIMITER));
                }
                fileWriter.flush();
                fileWriter.close();

            } catch (IOException e) {
                Log.d(TAG, "doInBackground: " + e.toString());

            }

            return null;
        }

        public String removeSplitters(String mdata) {
            return mdata.replaceAll("\n", ";");
        }

        private String removeFILimiters(String mData) {
            String[] sData = mData.split("/");
            String sdjsd = sData[sData.length - 1];
            return sdjsd;
        }

        private String removeDelimiters(String productPhotos) {

            String[] mData = productPhotos.split(";");
            StringBuilder stringBuilder = new StringBuilder();
            for (String data : mData) {
                String[] inData = data.split("/");
                stringBuilder.append(inData[inData.length - 1]);
                stringBuilder.append("; ");

            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(activityWeakReference.get(), "Export successful", Toast.LENGTH_SHORT).show();
        }
    }
}
