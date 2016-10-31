package com.adorgolap.ecode.main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.adorgolap.ecode.R;
import com.adorgolap.ecode.helper.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Context context;
    ListView lv;
    EditText etEcode;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
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
//        DatabaseHelper databaseHelper = new DatabaseHelper(context);
//        try {
//            databaseHelper.createDataBase();
//            databaseHelper.openDataBase();
//            ArrayList<String> eCodes = databaseHelper.getEcodes();
//            Toast.makeText(context,"Size = "+eCodes.size(),Toast.LENGTH_LONG).show();
//            databaseHelper.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        PopulateListTask task = new PopulateListTask();
        task.execute();
        etEcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(adapter != null) {
                    MainActivity.this.adapter.getFilter().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                TextView tv = (TextView) view;
                Intent i = new Intent(MainActivity.this,DetailsActivity.class);
                i.putExtra("code",tv.getText());
                startActivity(i);
            }
        });

    }

    private void initialize() {
        lv = (ListView) findViewById(R.id.listView);
        etEcode = (EditText) findViewById(R.id.etEcode);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class PopulateListTask extends AsyncTask<Void, Void, ArrayList<String>>
    {
        @Override
        protected void onPreExecute() {
            lv = (ListView) findViewById(R.id.listView);
            lv.setTextFilterEnabled(true);
            etEcode = (EditText) findViewById(R.id.etEcode);
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            ArrayList<String> eCodes = new ArrayList<String>();
            try {
            databaseHelper.createDataBase();
            databaseHelper.openDataBase();
            eCodes = databaseHelper.getEcodes();
            databaseHelper.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
            return eCodes;
        }

        @Override
        protected void onPostExecute(ArrayList<String> eCodes) {
            adapter = new ArrayAdapter<String>(context,android.R.layout.simple_expandable_list_item_1, eCodes);
            lv.setAdapter(adapter);
            adapter.getFilter().filter(etEcode.getText());
        }
    }
}
