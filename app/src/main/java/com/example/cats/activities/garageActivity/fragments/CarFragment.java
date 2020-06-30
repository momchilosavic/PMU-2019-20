package com.example.cats.activities.garageActivity.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.cats.R;
import com.example.cats.database.entities.Car;
import com.example.cats.database.entities.Component;
import com.example.cats.viewmodels.UserViewModel;

public class CarFragment extends Fragment {

    private OnCarFragmentListener mListener;
    private UserViewModel userViewModel;

    public CarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_car, container, false);
        userViewModel = UserViewModel.getInstance(getActivity());

        userViewModel.getCarMutableLiveData().observe(this, new Observer<Car>() {
            @Override
            public void onChanged(Car car) {
                if(car == null) return;
                int bodyType = -1;
                int frontWheelType = -1; int backWheelType = -1;
                int component1type = -1; int component2type = -1; int component3type = -1;
                for(Component component : userViewModel.getComponentsListMutableLiveData().getValue()){
                    if(component.id == car.bodyId) bodyType = component.type;
                    if(component.id == car.frontWheelId) frontWheelType = component.type;
                    if(component.id == car.backWheelId) backWheelType = component.type;
                    if(component.id == car.componentId1) component1type = component.type;
                    if(component.id == car.componentId2) component2type = component.type;
                    if(component.id == car.componentId3) component3type = component.type;
                }
                //TODO: add images by type
                if(bodyType >= 0) {
                    ((ImageView)view.findViewById(R.id.body)).setBackground(getResources().getDrawable(R.drawable.body));
                    if (frontWheelType >= 0) ((ImageView)view.findViewById(R.id.frontWheel)).setBackground(getResources().getDrawable(R.drawable.wheel));
                    if (backWheelType >= 0) ((ImageView)view.findViewById(R.id.frontWheel)).setBackground(getResources().getDrawable(R.drawable.wheel));
                    if (component1type >= 0) {
                        switch (Component.Type.values()[component1type]) {
                            case BLADE:
                                ((ImageView)view.findViewById(R.id.component1)).setBackground(getResources().getDrawable(R.drawable.weapon1));break;
                            case ROCKET:
                                ((ImageView)view.findViewById(R.id.component1)).setBackground(getResources().getDrawable(R.drawable.rocket_launcher));break;
                            case STIGNER:
                                ((ImageView)view.findViewById(R.id.component1)).setBackground(getResources().getDrawable(R.drawable.stinger));break;
                            case CHAINSAW:
                                ((ImageView)view.findViewById(R.id.component1)).setBackground(getResources().getDrawable(R.drawable.chainsaw));break;
                        }
                    }
                    if (component2type >= 0) {switch (Component.Type.values()[component2type]) {
                            case FORKLIFT:
                                ((ImageView)view.findViewById(R.id.component2)).setBackground(getResources().getDrawable(R.drawable.weapon1));break;
                        }
                    }
                }
            }
        });

        ((ImageView)view.findViewById(R.id.body)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCarFragmentInteraction();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCarFragmentListener) {
            mListener = (OnCarFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCarFragmentListener {
        // TODO: Update argument type and name
        void onCarFragmentInteraction();
    }
}
