package com.example.myapplication_imiate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication_imiate.entiy.HotSaleItem;

import java.util.List;

public class HotSaleAdapter extends RecyclerView.Adapter<HotSaleAdapter.HotSaleViewHolder> {

    public interface OnReserveClickListener {
        void onReserve(HotSaleItem item);
    }

    public interface OnCancelClickListener {
        void onCancelReserve(HotSaleItem item);
    }

    private final List<HotSaleItem> mItems;
    @LayoutRes
    private final int mItemLayoutRes;
    private final OnReserveClickListener mReserveListener;
    private final OnCancelClickListener mCancelListener;

    /** 首页：默认 item 布局，文案为「去预约」 */
    public HotSaleAdapter(@NonNull List<HotSaleItem> items, @NonNull OnReserveClickListener listener) {
        this(items, R.layout.item_hot_sale, listener, null);
    }

    /**
     * @param itemLayoutRes 列表项布局，须与 {@link R.layout#item_hot_sale} 使用相同 id；
     *                      预约页请使用 {@link R.layout#item_hot_sale_reserved}（按钮为「取消预约」）。
     */
    public HotSaleAdapter(@NonNull List<HotSaleItem> items,
                          @LayoutRes int itemLayoutRes,
                          @Nullable OnReserveClickListener reserveListener,
                          @Nullable OnCancelClickListener cancelListener) {
        mItems = items;
        mItemLayoutRes = itemLayoutRes;
        mReserveListener = reserveListener;
        mCancelListener = cancelListener;
    }

    @NonNull
    @Override
    public HotSaleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mItemLayoutRes, parent, false);
        return new HotSaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotSaleViewHolder holder, int position) {
        final HotSaleItem item = mItems.get(position);
        holder.tvTag.setText(item.getTag());
        holder.tvName.setText(item.getName());
        holder.tvPercent.setText(item.getPercentRange());
        holder.tvPeriod.setText(item.getPeriod());
        holder.tvMinInvest.setText(item.getMinInvestment());

        holder.btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mReserveListener != null) {
                    mReserveListener.onReserve(item);
                } else if (mCancelListener != null) {
                    mCancelListener.onCancelReserve(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    static class HotSaleViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTag;
        final TextView tvPercent;
        final TextView tvName;
        final TextView tvPeriod;
        final TextView tvMinInvest;
        final Button btnReserve;

        HotSaleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTag = itemView.findViewById(R.id.tv_hot_tag);
            tvPercent = itemView.findViewById(R.id.tv_hot_percent);
            tvName = itemView.findViewById(R.id.tv_hot_name);
            tvPeriod = itemView.findViewById(R.id.tv_hot_period);
            tvMinInvest = itemView.findViewById(R.id.tv_hot_min_invest);
            btnReserve = itemView.findViewById(R.id.btn_hot_reserve);
        }
    }
}
