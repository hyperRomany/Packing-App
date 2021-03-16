package com.example.packingapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.packingapp.R;
import com.example.packingapp.model.DriverModules.DriverPackages_Details_DB;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class DriverOrderpackages_rejectAdapter extends RecyclerView.Adapter<DriverOrderpackages_rejectAdapter.MyViewHolder> {

    private List<DriverPackages_Details_DB> ItemsList;
    DriverPackages_Details_DB packages_db;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_nu , txt_tracking_number , txt_package_items ,txt_package_price;
        LinearLayout main_linear_of_item;
        CheckBox checkBox;
        public MyViewHolder(View view) {
            super(view);
            txt_nu=  view.findViewById(R.id.txt_nu);
            checkBox = view.findViewById(R.id.checkbox_item);
            txt_tracking_number =  view.findViewById(R.id.txt_tracking_number);
            txt_package_items=view.findViewById(R.id.txt_package_items);
            txt_package_price=view.findViewById(R.id.txt_package_price);

            main_linear_of_item=view.findViewById(R.id.main_linear_of_item);
        }
    }


    public DriverOrderpackages_rejectAdapter(List<DriverPackages_Details_DB> moviesList) {
        this.ItemsList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item_for_driver_packages, parent, false);

        return new MyViewHolder(itemView);
    }

   // @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        packages_db = ItemsList.get(position);

        if (packages_db.getChecked_Item() ==null){
            packages_db.setChecked_Item(false);
        }
        if (packages_db.getChecked_Item()){
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
        });


        holder.txt_nu.setText(""+(position+1));
        holder.checkBox.setVisibility(View.INVISIBLE);
        holder.txt_package_price.setText(packages_db.getPACKAGE_PRICE());
        holder.txt_tracking_number.setText(packages_db.getTRACKING_NO());
        holder.txt_package_items.setText(packages_db.getCOUNT_ITEMS_PACKAGE());
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

    public List<DriverPackages_Details_DB> ReturnListOfPackages(){


        return ItemsList;

    }
}
