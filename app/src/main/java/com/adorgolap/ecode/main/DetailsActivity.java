package com.adorgolap.ecode.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adorgolap.ecode.R;
import com.adorgolap.ecode.helper.DatabaseHelper;
import com.adorgolap.ecode.helper.ECodeData;
import com.adorgolap.ecode.helper.Utils;

import static com.adorgolap.ecode.helper.Utils.SCREEN_HEIGHT;
import static com.adorgolap.ecode.helper.Utils.SCREEN_WIDTH;

/**
 * Created by ifta on 10/31/16.
 */

public class DetailsActivity extends AppCompatActivity {
    TextView tvCode,tvName, tvDescription, tvIsHalal;
    LinearLayout ll;
    Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        context = this;
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground();
        }


        tvCode  = (TextView) findViewById(R.id.tvCode);
        tvName = (TextView) findViewById(R.id.tvName);
        tvDescription = (TextView) findViewById(R.id.tvDesceiption);
        tvIsHalal = (TextView) findViewById(R.id.tvIsHalal);

        String code = getIntent().getStringExtra("code");
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.openDataBase();
        ECodeData eCodeData = databaseHelper.searchByECode(code);

        tvCode.setText(eCodeData.code);
        tvName.setText(eCodeData.name);
        tvDescription.setText(eCodeData.description);
        tvIsHalal.setText(eCodeData.halalStatus);
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
            ll = (LinearLayout) findViewById(R.id.llDetailsLayout);

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
        DetailsActivity.BackGroundSetterTask blt = new DetailsActivity.BackGroundSetterTask();
        blt.execute(R.drawable.bg_low);
    }

}
