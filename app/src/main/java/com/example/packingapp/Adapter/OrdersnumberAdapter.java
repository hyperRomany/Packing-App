package com.example.packingapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.packingapp.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class OrdersnumberAdapter extends RecyclerView.Adapter<OrdersnumberAdapter.MyViewHolder> {

    private List<String> ItemsList;
String ordersnumber;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        public TextView txt_nu , txt_tracking_number;
        LinearLayout main_linear_of_item;
        public MyViewHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.checkbox_item);
            txt_nu=  view.findViewById(R.id.txt_nu);
            txt_tracking_number =  view.findViewById(R.id.txt_tracking_number);
            main_linear_of_item=view.findViewById(R.id.main_linear_of_item);
        }
    }


    public OrdersnumberAdapter(List<String> moviesList) {
        this.ItemsList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item_for_packed_packages, parent, false);

        return new MyViewHolder(itemView);
    }

   // @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
         ordersnumber = ItemsList.get(position);
        /* if (TrackingnumbersListDB.getChecked_Item() ==null){
             TrackingnumbersListDB.setChecked_Item(false);
         }
        if (TrackingnumbersListDB.getChecked_Item()){
            holder.checkBox.setChecked(true);
  //          holder.main_linear_of_item.setBackgroundColor(R.color.red);
        }else {
            holder.checkBox.setChecked(false);
  //          holder.main_linear_of_item.setBackgroundColor(R.color.third_white);

        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
      //      @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()){
                    //po_item.setChecked_Item(true);
                    ItemsList.get(position).setChecked_Item(true);
                  //  Log.e("editChecked",""+ItemsList.get(position).getMATERIAL1());
    //                holder.main_linear_of_item.setBackgroundColor(R.color.red);

                }else if (!holder.checkBox.isChecked()){
                    //po_item.setChecked_Item(false);
                    ItemsList.get(position).setChecked_Item(false);
    //                holder.main_linear_of_item.setBackgroundColor(R.color.third_white);
                }
            }
        });*/

        holder.checkBox.setVisibility(View.VISIBLE);
        holder.txt_nu.setText(""+(position+1));

        holder.txt_tracking_number.setText(ordersnumber);
        //android:textIsSelectable="true"
//        holder.txt_ean11.setTextIsSelectable(true);
//
//        holder.txt_nu.requestFocus();
       // Log.e("btn_editChecked000",""+ItemsList.size());

    }

    @Override
    public int getItemCount() {
        return ItemsList.size();
    }

    public List<String> ReturnListOfPackages(){
        return ItemsList;

    }
}
