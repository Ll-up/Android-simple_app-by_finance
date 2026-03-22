package com.example.myapplication_imiate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//声明这是一个 RecyclerView 的适配器，专门给轮播图用
//核心作用是把 “轮播图图片数据” 和 “RecyclerView（ViewPager2）的显示控件” 绑定起来，“数据 + 容器 + 绑定” 三层架构
public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder>{//指定这个适配器用的 “视图容器” 是我们自己写的 BannerViewHolder指定这个适配器用的 “视图容器” 是我们自己写的 BannerViewHolder

    // 轮播图图片资源（你可以换成网络图片，这里用本地drawable举例）
    //这两行代码是轮播图适配器的“数据入口” —— 把轮播图的图片资源列表（比如 R.drawable.banner1、banner2）传递给适配器，让适配器知道 “要显示哪些图片”
    private List<Integer> mImageList;
    public BannerAdapter(List<Integer> imageList) {
        mImageList = imageList;
    }

    //创建 “视图容器”（造盒子）
    @NonNull
    @Override
    public BannerAdapter.BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加载单个轮播图item布局（这里直接用ImageView，也可以自定义布局）
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_banner, parent, false);//LayoutInflater：安卓的 “布局加载器”，作用是把 xml 布局文件（或系统自带布局）转换成内存中的 View 对象（比如把一个写好的 xml 布局变成能显示的按钮、图片控件）；
        ImageView iv = (ImageView) view;
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP); // 图片铺满
        return new BannerViewHolder(iv);
    }

    //给 “盒子装数据”（填内容）
    @Override
    public void onBindViewHolder(@NonNull BannerAdapter.BannerViewHolder holder, int position) {
        // 设置图片（取模实现无限轮播）
        int realPosition = position % mImageList.size();// 通过取模把超大的 position 映射到真实图片索引，实现图片循环显示；
        holder.ivBanner.setImageResource(mImageList.get(realPosition));// 是数据绑定的核心：把图片资源设置到 ImageView，完成轮播图显示；
    }

    //告诉 RecyclerView “有多少个 item”（定数量）
    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        // 2. 单个轮播图item里的控件：ImageView（显示轮播图片）
        ImageView ivBanner;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView); //构造方法
            ivBanner = (ImageView) itemView;// 把itemView转成ImageView，赋值给变量
        }
    }
}
