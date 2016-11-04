package com.adorgolap.ecode.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adorgolap.ecode.R;
import com.adorgolap.ecode.helper.Utils;

import static com.adorgolap.ecode.helper.Utils.SCREEN_HEIGHT;
import static com.adorgolap.ecode.helper.Utils.SCREEN_WIDTH;

/**
 * Created by ifta on 11/3/16.
 */

public class AboutActivity extends AppCompatActivity{
    Context context;
    LinearLayout ll;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        context = this;
        setBackground();
        TextView tvAboutText = (TextView)findViewById(R.id.tvAboutText);
        Linkify.addLinks(tvAboutText, Linkify.ALL);
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
            ll = (LinearLayout) findViewById(R.id.llAboutLayout);

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
        AboutActivity.BackGroundSetterTask blt = new AboutActivity.BackGroundSetterTask();
        blt.execute(R.drawable.bg_low);
    }
}
