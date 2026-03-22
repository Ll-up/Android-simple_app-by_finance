package com.example.myapplication_imiate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication_imiate.database.dbhelper;
import com.example.myapplication_imiate.entiy.HotSaleItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, HotSaleAdapter.OnReserveClickListener {

    private ViewPager2 mVpBanner;
    private LinearLayout mLlIndicator; // 指示器容器
    private BannerAdapter mAdapter;
    private List<Integer> mImageList; // 轮播图图片集合
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private int mCurrentPosition = 0; // 当前轮播位置
    private static final long CAROUSEL_INTERVAL = 3000; // 轮播间隔（3秒）
    private dbhelper mdbhelper;

    // 自动轮播的Runnable
    private Runnable mCarouselRunnable = new Runnable() {
        @Override
        public void run() {
            mCurrentPosition++;
            mVpBanner.setCurrentItem(mCurrentPosition, true); // 平滑切换
            mHandler.postDelayed(this, CAROUSEL_INTERVAL); // 循环执行
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 1. 初始化控件
        mVpBanner = findViewById(R.id.vp_banner);
        mLlIndicator = findViewById(R.id.ll_indicator);
        findViewById(R.id.ll_1).setOnClickListener(this);
        findViewById(R.id.ll_2).setOnClickListener(this);
        findViewById(R.id.ll_3).setOnClickListener(this);
        findViewById(R.id.ll_4).setOnClickListener(this);


        mdbhelper = dbhelper.getInstance(this);
        mdbhelper.openReadLink();
        mdbhelper.openWriteLink();

        // 2. 准备轮播图数据（替换成你的图片资源）
        mImageList = new ArrayList<>();
        mImageList.add(R.drawable.banner1); // 请替换成你自己的图片
        mImageList.add(R.drawable.banner2);
        mImageList.add(R.drawable.banner3);

        // 3. 设置适配器
        mAdapter = new BannerAdapter(mImageList);
        mVpBanner.setAdapter(mAdapter);

        // 4. 初始化指示器
        initIndicator();

        // 5. 设置ViewPager2滑动监听，更新指示器
        mVpBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // 更新指示器选中状态
                int realPosition = position % mImageList.size();
                updateIndicator(realPosition);
                mCurrentPosition = position;
            }
        });

        setupHotSaleList();
    }

    private void setupHotSaleList() {
        RecyclerView rv = findViewById(R.id.rv_hot_sale);
        rv.setLayoutManager(new LinearLayoutManager(this)); //设置布局管理器，决定列表的排列方式
        rv.setHasFixedSize(true); //优化性能，告知 RecyclerView 列表项高度固定
        List<HotSaleItem> itemes = mdbhelper.queryAllHotSaleItem();

//        List<HotSaleItem> items = new ArrayList<>();
//        items.add(new HotSaleItem(
//                "推荐",
//                "政盈11号24个月",
//                "6.9%~6.9%",
//                "期限：24个月",
//                "起投金额：100万"
//        ));
//        items.add(new HotSaleItem(
//                "推荐",
//                "宝能华府24个月",
//                "6.5%~7.9%",
//                "期限：24个月",
//                "起投金额：10万"
//        ));
//        items.add(new HotSaleItem(
//                "推荐",
//                "宝能华府15个⽉",
//                "6.35%~7.2%",
//                "期限：15个月",
//                "起投金额：10万"
//        ));
//        items.add(new HotSaleItem(
//                "推荐",
//                "宝能华府18个⽉",
//                "6.3%~7.3%",
//                "期限：18个月",
//                "起投金额：10万"
//        ));
//        items.add(new HotSaleItem(
//                "推荐",
//                "宝能华府18个⽉",
//                "6.3%~7.3%",
//                "期限：18个月",
//                "起投金额：10万"
//        ));
//        items.add(new HotSaleItem(
//                "推荐",
//                "宝能华府18个⽉",
//                "6.3%~7.3%",
//                "期限：18个月",
//                "起投金额：10万"
//        ));

        HotSaleAdapter adapter = new HotSaleAdapter(itemes, this);
        rv.setAdapter(adapter);
    }

    private void initIndicator() {
        mLlIndicator.removeAllViews();
        for (int i = 0; i < mImageList.size(); i++) {
            ImageView iv = new ImageView(this);
            // 设置指示器样式（选中/未选中）
            iv.setImageResource(i == 0 ? R.drawable.indicator_selected : R.drawable.indicator_unselected);
            // 设置指示器大小和间距
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dp2px(8), dp2px(8));
            params.setMargins(dp2px(4), 0, dp2px(4), 0);
            mLlIndicator.addView(iv, params);
        }
    }

    // 更新指示器选中状态
    private void updateIndicator(int position) {
        for (int i = 0; i < mLlIndicator.getChildCount(); i++) {
            ImageView iv = (ImageView) mLlIndicator.getChildAt(i);
            iv.setImageResource(i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected);
        }
    }

    // 启动自动轮播
    private void startCarousel() {
        mHandler.postDelayed(mCarouselRunnable, CAROUSEL_INTERVAL);
    }

    // 停止自动轮播（防止内存泄漏）
    private void stopCarousel() {
        mHandler.removeCallbacks(mCarouselRunnable);
    }

    // dp转px（适配不同屏幕）
    private int dp2px(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    // 页面启动时开始轮播
    @Override
    protected void onStart() {
        super.onStart();
        startCarousel();
    }

    // 页面销毁时停止轮播
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCarousel();
        mHandler.removeCallbacksAndMessages(null);
        if (mdbhelper != null) {
            mdbhelper.closeLink();
        }
    }
    //监控点击事件
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_1) {
            Toast.makeText(this, "点击了标签1", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.ll_2) {
            Toast.makeText(this, "点击了标签2", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.ll_3) {
            Toast.makeText(this, "点击了预约", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,reservation.class);
            startActivity(intent);

        } else if (id == R.id.ll_4) {
            Toast.makeText(this, "点击了标签4", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onReserve(HotSaleItem item) {
        Toast.makeText(this, "预约：" + item.getName(), Toast.LENGTH_SHORT).show();
        mdbhelper.setreservation(item, 1);
    }
}