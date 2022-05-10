package com.example.packingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.packingapp.C0747R;
import com.example.packingapp.model.ItemDetailForRecycle;
import java.util.List;

public class MissedItemsAdapter extends RecyclerView.Adapter<MyMissedItemsHolder> {
    private Context context;
    private List<ItemDetailForRecycle> items;
    /* access modifiers changed from: private */
    public OnCopyClickListener listener;

    public MissedItemsAdapter(List<ItemDetailForRecycle> list, OnCopyClickListener onCopyClickListener, Context context2) {
        this.items = list;
        this.listener = onCopyClickListener;
        this.context = context2;
    }

    public MyMissedItemsHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyMissedItemsHolder(LayoutInflater.from(this.context).inflate(C0747R.layout.missed_items_info, viewGroup, false));
    }

    public void onBindViewHolder(MyMissedItemsHolder myMissedItemsHolder, int i) {
        int i2 = i + 1;
        final ItemDetailForRecycle itemDetailForRecycle = this.items.get(i);
        myMissedItemsHolder.item_id.setText(String.valueOf(i2));
        myMissedItemsHolder.item_barcode.setText(itemDetailForRecycle.getBarcode());
        myMissedItemsHolder.item_name.setText(itemDetailForRecycle.getName());
        myMissedItemsHolder.copy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MissedItemsAdapter.this.listener.onClicked(itemDetailForRecycle.getBarcode());
            }
        });
    }

    public int getItemCount() {
        return this.items.size();
    }

    public class MyMissedItemsHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public ImageView copy;
        /* access modifiers changed from: private */
        public TextView item_barcode;
        /* access modifiers changed from: private */
        public TextView item_id;
        /* access modifiers changed from: private */
        public TextView item_name;

        public MyMissedItemsHolder(View view) {
            super(view);
            this.item_id = (TextView) view.findViewById(C0747R.C0749id.txt_missed_item_id);
            this.item_barcode = (TextView) view.findViewById(C0747R.C0749id.txt_missed_info_barcode);
            this.item_name = (TextView) view.findViewById(C0747R.C0749id.txt_missed_info_name);
            this.copy = (ImageView) view.findViewById(C0747R.C0749id.btn_copy);
        }
    }
}
