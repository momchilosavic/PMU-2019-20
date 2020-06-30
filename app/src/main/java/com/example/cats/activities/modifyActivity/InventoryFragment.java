package com.example.cats.activities.modifyActivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cats.R;
import com.example.cats.database.entities.Component;
import com.example.cats.viewmodels.UserViewModel;

import java.util.List;

public class InventoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private InventoryAdapter inventoryAdapter;
    private UserViewModel userViewModel;

    public InventoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        userViewModel = UserViewModel.getInstance(getActivity());

        recyclerView = view.findViewById(R.id.inventoryRecyclerViewHolder);
        userViewModel.getComponentsListMutableLiveData().observe(this, new Observer<List<Component>>() {
            @Override
            public void onChanged(List<Component> components) {
                inventoryAdapter = new InventoryAdapter((ModifyActivity)getActivity(), components);
                recyclerView.setAdapter(inventoryAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
