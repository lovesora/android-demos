package home.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import tool.ImgTools;

public class Stores {
    public static List<Stores> storesList = new ArrayList<>();

    private String title;
    private String desc;
    private String storeUrl;
    private Bitmap imgBitmap;
    private float rating;
    private String tel;
    private String time;
    private String tag;

    public Stores() {
    }

    public void setImgBitMap(Context context, final Handler handler) {
        new Thread() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(storeUrl).get();
                    Elements ele = doc.select("img.head-img");
                    URL url=new URL(ele.attr("src"));
                    InputStream is= url.openStream();
                    //从InputStream流中解析出图片
                    imgBitmap = ImgTools.compressScale(BitmapFactory.decodeStream(is));

                    handler.sendEmptyMessage(0x001);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("error", " src");
                }
            }
        }.start();
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

    public float getRating() {
        return rating;
    }

    public String getTel() {
        return tel;
    }

    public String getTime() {
        return time;
    }

    public String getTag() {
        return tag;
    }

    public Stores setTitle(String title) {
        this.title = title;
        return this;
    }

    public Stores setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public Stores setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
        return this;
    }

    public Stores setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
        return this;
    }

    public Stores setRating(float rating) {
        this.rating = rating;
        return this;
    }

    public Stores setTel(String tel) {
        this.tel = tel;
        return this;
    }

    public Stores setTime(String time) {
        this.time = time;
        return this;
    }

    public Stores setTag(String tag) {
        this.tag = tag;
        return this;
    }
}
