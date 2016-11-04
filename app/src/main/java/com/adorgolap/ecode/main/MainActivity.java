package com.adorgolap.ecode.main;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.adorgolap.ecode.R;
import com.adorgolap.ecode.adapter.ListViewAdapter;
import com.adorgolap.ecode.helper.DatabaseHelper;
import com.adorgolap.ecode.helper.ECodeData;
import com.adorgolap.ecode.helper.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.adorgolap.ecode.helper.Utils.SCREEN_HEIGHT;
import static com.adorgolap.ecode.helper.Utils.SCREEN_WIDTH;

public class MainActivity extends AppCompatActivity {
    LinearLayout ll;
    Context context;
    ListView lv;
    EditText etEcode;
    ListViewAdapter adapter;
    ArrayList<ECodeData> eCodesAndNamesForFilteringLowerCaseCopy;
    ArrayList<ECodeData> eCodesAndNamesForFilteringIntactCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground();
        }
        populateList();
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void populateList() {
        PopulateListTask task = new PopulateListTask();
        task.execute();
        etEcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (adapter != null && charSequence.length() >= 2) {
                    ArrayList<ECodeData> filteredAdapterData =
                            Utils.filterData(eCodesAndNamesForFilteringIntactCopy,
                                    eCodesAndNamesForFilteringLowerCaseCopy,
                                    charSequence.toString().toLowerCase());
                    adapter = new ListViewAdapter(context, filteredAdapterData);
                    lv.setAdapter(adapter);
                } else {
                    adapter = new ListViewAdapter(context, eCodesAndNamesForFilteringIntactCopy);
                    lv.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                TextView tv = (TextView) view.findViewById(R.id.tvCodeListItem);
                Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                i.putExtra("code", tv.getText());
                startActivity(i);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_about:
                Intent i = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(i);
                break;
            case R.id.action_rate:
                Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                }
                break;
            case R.id.actionAmjadKhan:
                Intent j = new Intent(MainActivity.this, AmzadKhanList.class);
                startActivity(j);
                break;
        }
        return true;
    }

    private class PopulateListTask extends AsyncTask<Void, Void, ArrayList<ECodeData>> {
        @Override
        protected void onPreExecute() {
            lv = (ListView) findViewById(R.id.listView);
            lv.setTextFilterEnabled(true);
            etEcode = (EditText) findViewById(R.id.etEcode);
        }

        @Override
        protected ArrayList<ECodeData> doInBackground(Void... voids) {
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            ArrayList<ECodeData> eCodesAndName = new ArrayList<ECodeData>();
            try {
                databaseHelper.createDataBase();
                databaseHelper.openDataBase();
                eCodesAndName = databaseHelper.getEcodes();
                databaseHelper.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Collections.sort(eCodesAndName, new Comparator<ECodeData>() {
                @Override
                public int compare(ECodeData eCodeData, ECodeData t1) {
                    return eCodeData.code.compareTo(t1.code);
                }
            });
            return eCodesAndName;
        }

        @Override
        protected void onPostExecute(ArrayList<ECodeData> eCodesAndName) {
            eCodesAndNamesForFilteringIntactCopy = eCodesAndName;
            eCodesAndNamesForFilteringLowerCaseCopy = saveDataForFiltering(eCodesAndName);//save a copy for LCS filtering
            adapter = new ListViewAdapter(context, eCodesAndName);
            lv.setAdapter(adapter);
        }
    }

    private ArrayList<ECodeData> saveDataForFiltering(ArrayList<ECodeData> eCodesAndName) {
        ArrayList<ECodeData> temp = new ArrayList<ECodeData>();
        for (ECodeData eCodeData : eCodesAndName) {
            temp.add(new ECodeData(eCodeData.name.toLowerCase(), eCodeData.code.toLowerCase()));
        }
        return temp;
    }

    class BackGroundSetterTask extends AsyncTask<Integer, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Integer... params) {
            int drawableId = params[0];
            return Utils.decodeSampledBitmapFromResource(getResources(),
                    drawableId, SCREEN_WIDTH / 10, SCREEN_HEIGHT / 10);
        }

        @Override
        protected void onPreExecute() {
            calculateScreenSize();
            ll = (LinearLayout) findViewById(R.id.content_main);

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            BitmapDrawable background = new BitmapDrawable(context.getResources(), bitmap);
            if (Build.VERSION.SDK_INT >= 16) {
                ll.setBackground(background);
            }
        }
    }

    private void setBackground() {
        BackGroundSetterTask blt = new BackGroundSetterTask();
        blt.execute(R.drawable.bg_low);
    }

    private void calculateScreenSize() {
        if(Utils.SCREEN_HEIGHT == 0 || Utils.SCREEN_WIDTH == 0) {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            Display d = wm.getDefaultDisplay();
            Point s = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                d.getSize(s);
                Utils.SCREEN_WIDTH = s.x;
                Utils.SCREEN_HEIGHT = s.y;

            } else {
                Utils.SCREEN_HEIGHT = d.getHeight();
                Utils.SCREEN_WIDTH = d.getWidth();

            }
        }
    }
}
