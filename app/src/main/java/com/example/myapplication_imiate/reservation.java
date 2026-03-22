package com.example.myapplication_imiate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication_imiate.database.dbhelper;
import com.example.myapplication_imiate.entiy.HotSaleItem;

import java.util.List;


public class reservation extends AppCompatActivity implements View.OnClickListener,
        HotSaleAdapter.OnCancelClickListener {

    private dbhelper mdbhelper;
    private List<HotSaleItem> mReservedItems;
    private HotSaleAdapter mHotSaleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        findViewById(R.id.iv_back).setOnClickListener(this);
        mdbhelper = dbhelper.getInstance(this);
        mdbhelper.openReadLink();
        mdbhelper.openWriteLink();
        setupHotSaleList();
    }

    private void setupHotSaleList() {
        RecyclerView rv_res = findViewById(R.id.rv_hot_res);
        rv_res.setLayoutManager(new LinearLayoutManager(this)); //设置布局管理器，决定列表的排列方式
        rv_res.setHasFixedSize(true); //优化性能，告知 RecyclerView 列表项高度固定
        mReservedItems = mdbhelper.queryspecHotSaleItem();
        mHotSaleAdapter = new HotSaleAdapter(
                mReservedItems,
                R.layout.item_hot_sale_reserved,
                null,
                this);
        rv_res.setAdapter(mHotSaleAdapter);
    }

    @Override
    public void onCancelReserve(HotSaleItem item) {
        // 1. 修改数据库：将该商品的reservation字段设为0（取消预约）
        mdbhelper.setreservation(item, 0);
        // 2. 查找该商品在已预约列表中的位置
        int pos = mReservedItems.indexOf(item);
        if (pos >= 0) {
            mReservedItems.remove(pos);
            //通知适配器删除pos位置的Item，RecyclerView刷新界面
            mHotSaleAdapter.notifyItemRemoved(pos);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.iv_back){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }
}
