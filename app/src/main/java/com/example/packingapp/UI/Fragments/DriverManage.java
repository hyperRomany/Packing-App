package com.example.packingapp.UI.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.packingapp.R;
import com.example.packingapp.databinding.ManageDriverBinding;
import com.example.packingapp.model.Message;
import com.example.packingapp.model.ResponseDriver;
import com.example.packingapp.viewmodel.DriverViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class DriverManage extends Fragment {
    ManageDriverBinding binding;
    DriverViewModel driverViewModel;
    List<String> categories;
    private View mLeak;
    ResponseDriver responseDriver;
    String State = "create";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = ManageDriverBinding.inflate(inflater, container, false);
        mLeak = binding.getRoot();
        driverViewModel = ViewModelProviders.of(this).get(DriverViewModel.class);
        driverViewModel.fetchDataVehicle();
        driverViewModel.fetchDataDriver();
        List<String> Status_list = new ArrayList<String>();
        categories = new ArrayList<String>();
        List<String> driver = new ArrayList<String>();

        Status_list.add("0");
        Status_list.add("1");

        ArrayAdapter spinnerAdapterStatus = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,
                Status_list);
        spinnerAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerStatus.setAdapter(spinnerAdapterStatus);

        ArrayAdapter spinnerAdapterDriver = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,
                driver);
        spinnerAdapterDriver.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.DriverID.setAdapter(spinnerAdapterDriver);


        driverViewModel.mutableLiveDataRead.observe(getViewLifecycleOwner(), (ResponseDriver responseDriver) -> {
            if (driver.size() == 0) {
                for (int i = 0; i < responseDriver.getRecords().size(); i++) {
                    if (i == 0)
                        driver.add(getResources().getString(R.string.choice_driver_id));
                    driver.add(responseDriver.getRecords().get(i).getDriverID()+"&"+responseDriver.getRecords().get(i).getNameArabic());
                }
            }
            this.responseDriver = responseDriver;
            spinnerAdapterDriver.notifyDataSetChanged();

        });

        binding.DriverID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                } else {
                    binding.NameArabic.setText(responseDriver.getRecords().get(position - 1).getNameArabic().toString());
                    binding.NameEnglish.setText(responseDriver.getRecords().get(position - 1).getNameEnglish().toString());
                    binding.spinnerStatus.setPrompt(responseDriver.getRecords().get(position - 1).getStatus().toString());
                    binding.Company.setText(responseDriver.getRecords().get(position - 1).getCompany().toString());
                    binding.Phone.setText(responseDriver.getRecords().get(position - 1).getPhone().toString());
                    binding.Address.setText(responseDriver.getRecords().get(position - 1).getAddress().toString());
                    binding.VechileID.setPrompt(responseDriver.getRecords().get(position - 1).getVechileID().toString());
                    binding.National.setText(responseDriver.getRecords().get(position - 1).getNational_ID().toString());
                    binding.Employeeid.setText(responseDriver.getRecords().get(position - 1).getEmployeeID().toString());
                    binding.create.setText("??????????");
                    State = "update";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,
                categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.VechileID.setAdapter(spinnerAdapter);
        driverViewModel.mutableLiveDataVehicle.observe(getViewLifecycleOwner(), responseVehicle -> {
            if (categories.size() == 0) {
                for (int i = 0; i < responseVehicle.getRecords().size(); i++) {
                    categories.add(responseVehicle.getRecords().get(i).getVechileID());
                }
            }
            spinnerAdapter.notifyDataSetChanged();
        });
        binding.create.setOnClickListener(v -> {
            if (State.equals("create"))
                create();
            else
                update();
        });


        return mLeak;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLeak = null;
    }


    public void create() {
        State = "create";
        driverViewModel.fetchdata(binding.NameArabic.getText().toString(), binding.NameEnglish.getText().toString(),
                binding.spinnerStatus.getSelectedItem().toString(), binding.Company.getText().toString(),
                binding.Phone.getText().toString(), binding.Address.getText().toString(),
                binding.VechileID.getSelectedItem().toString(),binding.National.getText().toString(),
                binding.Employeeid.getText().toString());
//        driverViewModel.mutableLiveData.observe(getViewLifecycleOwner(), message -> Toast.makeText(getContext(),
//                message.getMessage(), Toast.LENGTH_SHORT).show());


        driverViewModel.getDriverLiveData().observe(
                getViewLifecycleOwner(),new Observer<Message>() {

                    @Override
                    public void onChanged(Message message) {
                        Toast.makeText(getContext(), message.getMessage(), Toast.LENGTH_SHORT).show();
                        clear();
                    }
                }
        );
        driverViewModel.getDriverLiveData_error().observe(
                getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();

                    }
                }
        );
        binding.create.setText("??????????");

//        clear();
    }

    public void update() {
        String currentString = binding.DriverID.getSelectedItem().toString();
        String[] separated = currentString.split("&");
        driverViewModel.updateData(separated[0], binding.NameArabic.getText().toString(),
                binding.NameEnglish.getText().toString(), binding.spinnerStatus.getSelectedItem().toString(),
                binding.Company.getText().toString(), binding.Phone.getText().toString(),
                binding.Address.getText().toString(), binding.VechileID.getSelectedItem().toString(),
                binding.National.getText().toString(),binding.Employeeid.getText().toString());
        driverViewModel.mutableLiveData.observe(getViewLifecycleOwner(), message -> Toast.makeText(getContext(), message.getMessage(), Toast.LENGTH_SHORT).show());
        binding.create.setText("??????????");
        State = "create";
        clear();
    }

    public void clear() {
        binding.NameArabic.setText("");
        binding.NameEnglish.setText("");
        binding.Company.setText("");
        binding.Phone.setText("");
        binding.Address.setText("");
        binding.National.setText("");
        binding.Employeeid.setText("");
    }
}
