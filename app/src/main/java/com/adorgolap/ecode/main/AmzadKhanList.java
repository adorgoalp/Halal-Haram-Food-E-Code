package com.adorgolap.ecode.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.adorgolap.ecode.R;
import com.adorgolap.ecode.adapter.ListViewAdapter;
import com.adorgolap.ecode.helper.ECodeData;
import com.adorgolap.ecode.helper.Utils;

import java.util.ArrayList;

import static com.adorgolap.ecode.helper.Utils.SCREEN_HEIGHT;
import static com.adorgolap.ecode.helper.Utils.SCREEN_WIDTH;

/**
 * Created by ifta on 11/3/16.
 */

public class AmzadKhanList extends AppCompatActivity {
    Context context;
    LinearLayout ll;
    ListView lv;
    ListViewAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        setContentView(R.layout.amjad_khan_list);
        context = this;
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground();
        }
        lv = (ListView) findViewById(R.id.lvAmjadKhan);
        String all = "E100, E110, E120, E 140, E141, E153, E210, " +
                "E213, E214, E216, E234, E252, E270, E280, E325, E326, " +
                "E327, E334, E335, E336, E337, E422, E430, E431, E432, E433, " +
                "E434, E435, E436, E440, E470, E471, E472, E473, E474, E475, " +
                "E476, E477, E478, E481, E482, E483, E491, E492, E493, E494, " +
                "E495, E542, E570, E572, E631, E635, E904";
        all = all.replaceAll(" ","");
        String[] listItems = all.split(",");
        ArrayList<ECodeData> data = new ArrayList<ECodeData>();
        for(int i = 0 ; i < listItems.length ;i++)
        {
            data.add(new ECodeData(listItems[i],"Status: Haram according to the article.\nAllah (swt) knows the best."));
        }
        adapter = new ListViewAdapter(context,data);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                TextView tv = (TextView) view.findViewById(R.id.tvCodeListItem);
                Intent i = new Intent(AmzadKhanList.this, DetailsActivity.class);
                i.putExtra("code", tv.getText());
                startActivity(i);
            }
        });
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
            ll = (LinearLayout) findViewById(R.id.llAmjadKhan);

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
        AmzadKhanList.BackGroundSetterTask blt = new AmzadKhanList.BackGroundSetterTask();
        blt.execute(R.drawable.bg_low);
    }
}
