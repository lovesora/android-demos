package home.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.liuxin.android_demos.R;

import java.util.List;

import home.model.Stores;

/**
 * Created by LIUXIN-HASEE on 2017/5/11.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.StoresViewHolder> {
    private List<Stores> stores;
    private Context context;

    public RecyclerViewAdapter(List<Stores> stores, Context context) {
        this.stores = stores;
        this.context = context;
    }

    //自定义ViewHolder类
    static class StoresViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        CardView cardView;
        ImageView store_photo;
        TextView store_title;
        RatingBar store_rating;
        TextView store_rating_value;
        TextView store_tel;
        TextView store_desc;
        Button share;
        Button readMore;

        public StoresViewHolder(final View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.card_view_container);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            store_photo = (ImageView) itemView.findViewById(R.id.store_photo);
            store_title = (TextView) itemView.findViewById(R.id.store_title);
            store_rating = (RatingBar) itemView.findViewById(R.id.store_rating);
            store_rating_value = (TextView) itemView.findViewById(R.id.store_rating_value);
            store_tel = (TextView) itemView.findViewById(R.id.store_tel);
            store_desc = (TextView) itemView.findViewById(R.id.store_desc);
            share = (Button) itemView.findViewById(R.id.btn_share);
            readMore = (Button) itemView.findViewById(R.id.btn_more);
            //设置TextView背景为半透明
            store_title.setBackgroundColor(Color.argb(0x33, 0, 0, 0));
        }

    }

    @Override
    public RecyclerViewAdapter.StoresViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.poi_card_item, viewGroup, false);
        StoresViewHolder svh = new StoresViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.StoresViewHolder storesViewHolder, int i) {
        final int j = i;

        storesViewHolder.store_photo.setImageBitmap(stores.get(i).getImgBitmap());
        storesViewHolder.store_title.setText(stores.get(i).getTitle());
        storesViewHolder.store_desc.setText(stores.get(i).getDesc());
        storesViewHolder.store_rating.setStepSize(0.1f);
        storesViewHolder.store_rating.setRating(stores.get(i).getRating());
        storesViewHolder.store_rating_value.setText(String.valueOf(stores.get(i).getRating()));
        storesViewHolder.store_tel.setText(stores.get(i).getTel());


        //为btn_share btn_readMore cardView设置点击事件
        storesViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StoresActivity.class);
                intent.putExtra("pos", String.valueOf(j));
                context.startActivity(intent);
            }
        });

        storesViewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, stores.get(j).getTitle() + ": " + stores.get(j).getDesc());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(intent, stores.get(j).getTitle()));
            }
        });

        storesViewHolder.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StoresActivity.class);
                intent.putExtra("pos", String.valueOf(j));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

}
