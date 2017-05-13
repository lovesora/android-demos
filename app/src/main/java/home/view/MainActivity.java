package home.view;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.liuxin.android_demos.R;

import java.util.List;

import home.model.Stores;

public class MainActivity extends AppCompatActivity {
    private NestedScrollView mScrollView;
    private ImageView mIcLocationMap;

    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private PoiSearch mPoiSearch;
    private OnGetPoiSearchResultListener poiListener = null;

    private MyRecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    private LocationClient mLocationClient = null;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x001) {
                //显示从网上下载的图片
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        mScrollView = (NestedScrollView) findViewById(R.id.scrollView);
        mIcLocationMap = (ImageView) findViewById(R.id.ic_location_map);
        mIcLocationMap.setBackgroundColor(Color.argb(0x33, 0, 0, 0));
        mIcLocationMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stores.storesList.clear();
                initLocation();
            }
        });

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mPoiSearch = PoiSearch.newInstance();

        initBaiduMap();
        initRecyclerView();
        initLocation();
    }

    protected void initBaiduMap() {
        /**
         * init baidumap
         */
        poiListener = new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                List<PoiInfo> allPoi = poiResult.getAllPoi();
                for (PoiInfo info : allPoi) {
                    PoiSearch poiSearch = PoiSearch.newInstance();
                    poiSearch.setOnGetPoiSearchResultListener(poiListener);
                    poiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(info.uid));
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                Stores store = new Stores()
                        .setTitle(poiDetailResult.getName())
                        .setTel(poiDetailResult.getTelephone())
                        .setDesc(poiDetailResult.getAddress())
                        .setStoreUrl(poiDetailResult.getDetailUrl())
                        .setRating((float) poiDetailResult.getOverallRating())
                        .setTag(poiDetailResult.getTag())
                        .setTime(poiDetailResult.getShopHours());
                store.setImgBitMap(getApplicationContext(), handler);
                Stores.storesList.add(store);

                LatLng point = new LatLng(poiDetailResult.getLocation().latitude, poiDetailResult.getLocation().longitude);
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.location);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .icon(bitmap);
                //在地图上添加Marker，并显示
                mBaiduMap.addOverlay(option);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
            }
        };
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);

        /**
         * solution of the conflict between baidumap and scrollview
         */
        View v = mMapView.getChildAt(0);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    mScrollView.requestDisallowInterceptTouchEvent(false);
                }else{
                    mScrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
    }

    protected void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = (MyRecyclerView) findViewById(R.id.recyclerView);

        adapter = new RecyclerViewAdapter(Stores.storesList, MainActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        //use recyclerview in nestedscrollview
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void initLocation() {
        mLocationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                try {
                    LatLng position = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());

                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(position)
                            .zoom(15);

                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                    mPoiSearch.searchNearby(new PoiNearbySearchOption()
                            .location(position)
                            .pageNum(0)
                            .keyword("美食")
                            .radius(2000));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
        mLocationClient.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mLocationClient.stop();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
