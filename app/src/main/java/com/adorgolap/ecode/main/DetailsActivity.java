package com.adorgolap.ecode.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.adorgolap.ecode.R;
import com.adorgolap.ecode.helper.DatabaseHelper;
import com.adorgolap.ecode.helper.ECodeData;

/**
 * Created by ifta on 10/31/16.
 */

public class DetailsActivity extends AppCompatActivity {
    TextView tvCode,tvName, tvDescription, tvIsHalal;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.details);
        tvCode  = (TextView) findViewById(R.id.tvCode);
        tvName = (TextView) findViewById(R.id.tvName);
        tvDescription = (TextView) findViewById(R.id.tvDesceiption);
        tvIsHalal = (TextView) findViewById(R.id.tvIsHalal);

        String code = getIntent().getStringExtra("code");
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.openDataBase();
        ECodeData eCodeData = databaseHelper.searchByECode(code);

        tvCode.setText(eCodeData.getCode());
        tvName.setText(eCodeData.getName());
        tvDescription.setText(eCodeData.getDescription());
        tvIsHalal.setText(eCodeData.getHalalStatus());
    }
}
