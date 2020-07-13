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
import com.example.cats.database.AppDatabase;
import com.example.cats.database.entities.Car;
import com.example.cats.database.entities.Component;
import com.example.cats.viewmodels.UserViewModel;

public class CarFragment extends Fragment {

    private OnCarFragmentListener mListener;
    private UserViewModel userViewModel;
    AppDatabase database;

    public CarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_car, container, false);
        userViewModel = UserViewModel.getInstance(getActivity());
        database = AppDatabase.getInstance(getContext());
        userViewModel.getCarMutableLiveData().observe(this, new Observer<Car>() {
            @Override
            public void onChanged(Car car) {
                if(car == null) return;
                final int carId = car.id;
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
                    final Car finalCar = car;
                    //TODO: ON LONG CLICK REMOVE FROM CAR AND UPDATE DATABASE
                    //((ImageView)view.findViewById(R.id.body)).setBackground(getResources().getDrawable(R.drawable.body));

                    ((ImageView)view.findViewById(R.id.body)).setImageDrawable(getResources().getDrawable(R.drawable.body));
                    ((ImageView)view.findViewById(R.id.body)).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            System.out.println("BODY CLICKED");
                            return false;
                        }
                    });
                    if (frontWheelType > 0) {
                        ((ImageView)view.findViewById(R.id.frontWheel)).setBackground(getResources().getDrawable(R.drawable.wheel));
                        ((ImageView)view.findViewById(R.id.frontWheel)).setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                System.out.println("DETECTED LONG CLICK");
                                onLongClickListener(finalCar.frontWheelId);
                                return true;
                            }
                        });
                    }
                    else{
                        ((ImageView)view.findViewById(R.id.frontWheel)).setBackground(null);
                    }
                    if (backWheelType > 0) {
                        ((ImageView)view.findViewById(R.id.backWheel)).setBackground(getResources().getDrawable(R.drawable.wheel));
                        ((ImageView)view.findViewById(R.id.backWheel)).setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                System.out.println("DETECTED LONG CLICK");
                                onLongClickListener(finalCar.backWheelId);
                                return true;
                            }
                        });
                    }
                    else{
                        ((ImageView)view.findViewById(R.id.backWheel)).setBackground(null);
                    }

                    ((ImageView)view.findViewById(R.id.component1chainsaw)).setBackground(null);
                    ((ImageView)view.findViewById(R.id.component1stinger)).setBackground(null);
                    ((ImageView)view.findViewById(R.id.component1rocked)).setBackground(null);
                    ((ImageView)view.findViewById(R.id.component1blade)).setBackground(null);
                    if (component1type >= 0) {
                        switch (Component.Type.values()[component1type]) {
                            case BLADE:
                                ((ImageView)view.findViewById(R.id.component1blade)).setBackground(getResources().getDrawable(R.drawable.weapon1));
                                ((ImageView)view.findViewById(R.id.component1blade)).setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        onLongClickListener(finalCar.componentId1);
                                        return true;
                                    }
                                });
                                break;
                            case ROCKET:
                                ((ImageView)view.findViewById(R.id.component1rocked)).setBackground(getResources().getDrawable(R.drawable.rocket_launcher));
                                ((ImageView)view.findViewById(R.id.component1rocked)).setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        onLongClickListener(finalCar.componentId1);
                                        return true;
                                    }
                                });
                                break;
                            case STIGNER:
                                ((ImageView)view.findViewById(R.id.component1stinger)).setBackground(getResources().getDrawable(R.drawable.stinger));
                                ((ImageView)view.findViewById(R.id.component1stinger)).setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        onLongClickListener(finalCar.componentId1);
                                        return true;
                                    }
                                });
                                break;
                            case CHAINSAW:
                                ((ImageView)view.findViewById(R.id.component1chainsaw)).setBackground(getResources().getDrawable(R.drawable.chainsaw));
                                ((ImageView)view.findViewById(R.id.component1chainsaw)).setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        onLongClickListener(finalCar.componentId1);
                                        return true;
                                    }
                                });
                                break;
                        }

                    }
                    if (component2type >= 0) {switch (Component.Type.values()[component2type]) {
                            case FORKLIFT:
                                ((ImageView)view.findViewById(R.id.component2)).setBackground(getResources().getDrawable(R.drawable.forklift));break;
                        }
                        ((ImageView)view.findViewById(R.id.component2)).setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                onLongClickListener(finalCar.componentId2);
                                return true;
                            }
                        });
                    }
                    else{
                        ((ImageView)view.findViewById(R.id.component2)).setBackground(null);
                    }
                }
                else{
                    //((ImageView)view.findViewById(R.id.body)).setBackground(null);
                    ((ImageView)view.findViewById(R.id.body)).setImageDrawable(null);
                }
            }
        });

        ((ImageView)view.findViewById(R.id.body)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCarFragmentInteraction();
            }
        });

        ((ImageView)view.findViewById(R.id.backWheel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCarFragmentInteraction();
            }
        });

        ((ImageView)view.findViewById(R.id.frontWheel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCarFragmentInteraction();
            }
        });

        view.findViewById(R.id.component1blade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCarFragmentInteraction();
            }
        });
        view.findViewById(R.id.component1rocked).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCarFragmentInteraction();
            }
        });
        view.findViewById(R.id.component1stinger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCarFragmentInteraction();
            }
        });
        view.findViewById(R.id.component1chainsaw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCarFragmentInteraction();
            }
        });

        view.findViewById(R.id.component2).setOnClickListener(new View.OnClickListener() {
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

    private void onLongClickListener(final int componentId){
        Car car = userViewModel.getCarMutableLiveData().getValue();
        final Car finalCar = car;
        final int userId = userViewModel.getUserMutableLiveData().getValue().id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*if(componentId == finalCar.backWheelId) database.CarDao().updateBackWheelByUserId(userId, 0);
                if(componentId == finalCar.frontWheelId) database.CarDao().updateFrontWheelByUserId(userId, 0);
                if(componentId == finalCar.componentId1) database.CarDao().updateComponent1ByUserId(userId, 0);
                if(componentId == finalCar.componentId2) database.CarDao().updateComponent2ByUserId(userId, 0);*/
                database.CarDao().update(finalCar);
            }
        }).start();
        if(componentId == car.backWheelId) car.backWheelId = 0;
        if(componentId == car.frontWheelId) car.frontWheelId = 0;
        if(componentId == car.componentId1) car.componentId1 = 0;
        if(componentId == car.componentId2) car.componentId2 = 0;
        userViewModel.updateCar(car);


    }

    public interface OnCarFragmentListener {
        // TODO: Update argument type and name
        void onCarFragmentInteraction();
    }
}
