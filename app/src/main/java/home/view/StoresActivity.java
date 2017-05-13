package home.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.liuxin.android_demos.R;

import home.model.Stores;

/**
 * Created by LIUXIN-HASEE on 2017/5/11.
 */

public class StoresActivity extends AppCompatActivity {
    private ImageView store_photo;
    private TextView store_title;
    private RatingBar store_rating;
    private TextView store_rating_value;
    private TextView store_tel;
    private TextView store_desc;
    private TextView store_time;
    private TextView store_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        store_photo = (ImageView) findViewById(R.id.store_photo);
        store_title = (TextView) findViewById(R.id.store_title);
        store_rating = (RatingBar) findViewById(R.id.store_rating);
        store_rating_value = (TextView) findViewById(R.id.store_rating_value);
        store_tel = (TextView) findViewById(R.id.store_tel);
        store_desc = (TextView) findViewById(R.id.store_desc);
        store_time = (TextView) findViewById(R.id.store_time);
        store_tag = (TextView) findViewById(R.id.store_tag);
        
        Intent intent = getIntent();
        int pos = Integer.valueOf(intent.getStringExtra("pos"));
        final Stores item = Stores.storesList.get(pos);

        store_photo.setImageBitmap(item.getImgBitmap());
        store_title.setText(item.getTitle());
        store_desc.setText(item.getDesc());
        store_rating.setStepSize(0.1f);
        store_rating.setRating(item.getRating());
        store_rating_value.setText(String.valueOf(item.getRating()));
        store_tel.setText(item.getTel());
        if (item.getTime().length() > 1)
            store_time.setText(item.getTime());
        store_tag.setText(item.getTag());

        Button btn_share = (Button) findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, item.getTitle() + ": " + item.getDesc());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, item.getTitle()));
            }
        });
    }
}

