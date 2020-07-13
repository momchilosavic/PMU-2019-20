package com.example.cats.activities.modifyActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.KeyEventDispatcher;
import androidx.lifecycle.Observer;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cats.R;
import com.example.cats.activities.garageActivity.fragments.CarFragment;
import com.example.cats.database.AppDatabase;
import com.example.cats.database.entities.Car;
import com.example.cats.database.entities.Component;
import com.example.cats.utils.AppPrefs;
import com.example.cats.utils.MediaPlayer;
import com.example.cats.viewmodels.UserViewModel;

import java.util.List;

public class ModifyActivity extends AppCompatActivity
        implements CarFragment.OnCarFragmentListener, InventoryAdapter.OnListItemClickListener {

    private UserViewModel userViewModel;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = UserViewModel.getInstance(this);
        database = AppDatabase.getInstance(this);
        setContentView(R.layout.activity_modify);

        final ImageView bodyImageView = findViewById(R.id.body);

        userViewModel.getCarMutableLiveData().observe(this, new Observer<Car>() {
            int health = 0;
            int totalEnergy = 0;
            int usedEnergy = 0;
            int power = 0;
            @Override
            public void onChanged(Car car) {
                health = 0; totalEnergy = 0; usedEnergy = 0; power = 0;
                for(Component c : userViewModel.getComponentsListMutableLiveData().getValue()){
                    if(car.bodyId == c.id){
                        health += c.health;
                        totalEnergy = c.energy;
                    }
                    if(car.backWheelId == c.id || car.frontWheelId == c.id || car.componentId1 == c.id || car.componentId2 == c.id) {
                        health += c.health;
                        usedEnergy += c.energy;
                        power += c.power;
                    }
                }
                ((TextView)findViewById(R.id.healthInfo)).setText("HEALTH: " + health);
                ((TextView)findViewById(R.id.energyInfo)).setText("ENERGY: " + usedEnergy + "/" + totalEnergy);
                ((TextView)findViewById(R.id.powerInfo)).setText("POWER: " + power);
            }
        });

        bodyImageView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();
                Car car = userViewModel.getCarMutableLiveData().getValue();
                final int carId = car.id;
                final int userId = userViewModel.getUserMutableLiveData().getValue().id;
                switch(action){
                    case DragEvent.ACTION_DRAG_STARTED:
                        if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                            if(v.getBackground() == null) return false;
                            v.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                            v.invalidate();
                            return true;
                        }
                        return false;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        if(v.getBackground() == null) return false;
                        v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        v.invalidate();
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        if(v.getBackground() == null) return false;
                        v.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                        v.invalidate();
                        return true;
                    case DragEvent.ACTION_DROP:
                        String msg = event.getClipData().getItemAt(0).getText().toString();
                        final int type = Integer.valueOf(msg.split("#")[0]);
                        final int componentId = Integer.valueOf(msg.split("#")[1]);
                        if(type != Component.Type.WHEEL.ordinal() || car.frontWheelId <= 0 || car.backWheelId <= 0 && canAddComponent(car, componentId)){
                            switch (Component.Type.values()[type]){
                                case BODY:
                                    car.bodyId = componentId;
                                    userViewModel.updateCar(car);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            database.CarDao().updateBodyByUserId(userId, componentId);
                                        }
                                    });
                                    break;
                                case WHEEL:
                                    if((double)event.getX() < (double)v.getWidth() / 2 ){
                                       car.backWheelId = componentId;
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    database.CarDao().updateBackWheelByUserId(userId, componentId);
                                                }
                                            }).start();
                                        }
                                        else{
                                            car.frontWheelId = componentId;
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    database.CarDao().updateFrontWheelByUserId(userId, componentId);
                                                }
                                            }).start();
                                        }
                                    userViewModel.updateCar(car);
                                    break;
                                case BLADE: case CHAINSAW: case ROCKET: case STIGNER:
                                    car.componentId1 = componentId;
                                    userViewModel.updateCar(car);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            database.CarDao().updateComponent1ByUserId(userId, componentId);
                                        }
                                    }).start();
                                    break;
                                case FORKLIFT:
                                    car.componentId2 = componentId;
                                    userViewModel.updateCar(car);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            database.CarDao().updateComponent2ByUserId(userId, componentId);
                                        }
                                    }).start();
                                    break;
                            }
                        }
                        //TODO: GET INFO FROM ITEM
                        if(v.getBackground() == null) return false;
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        if(v.getBackground() == null) return false;
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        return true;
                    default: return false;
                }
            }
        });
/*
        backWheelImageView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();
                Car car = userViewModel.getCarMutableLiveData().getValue();
                final int carId = car.id;
                switch(action){
                    case DragEvent.ACTION_DRAG_STARTED:
                        if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                            if(v.getBackground() == null) return false;
                            v.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                            v.invalidate();
                            return true;
                        }
                        return false;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        if(v.getBackground() == null) return false;
                        v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        v.invalidate();
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        if(v.getBackground() == null) return false;
                        v.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                        v.invalidate();
                        return true;
                    case DragEvent.ACTION_DROP:
                        String msg = event.getClipData().getItemAt(0).getText().toString();
                        final int type = Integer.valueOf(msg.split("#")[0]);
                        final int componentId = Integer.valueOf(msg.split("#")[1]);
                        if(type == Component.Type.WHEEL.ordinal()){
                            car.backWheelId = componentId;
                            userViewModel.updateCar(car);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    database.CarDao().backWheelId(carId, componentId);
                                }
                            });
                        }
                        //TODO: GET INFO FROM ITEM
                        if(v.getBackground() == null) return false;
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        if(v.getBackground() == null) return false;
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        return true;
                    default: return false;
                }
            }
        });

        frontWheelImageView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();
                Car car = userViewModel.getCarMutableLiveData().getValue();
                final int carId = car.id;
                switch(action){
                    case DragEvent.ACTION_DRAG_STARTED:
                        if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                            if(v.getBackground() == null) return false;
                            v.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                            v.invalidate();
                            return true;
                        }
                        return false;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        if(v.getBackground() == null) return false;
                        v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        v.invalidate();
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        if(v.getBackground() == null) return false;
                        v.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                        v.invalidate();
                        return true;
                    case DragEvent.ACTION_DROP:
                        String msg = event.getClipData().getItemAt(0).getText().toString();
                        final int type = Integer.valueOf(msg.split("#")[0]);
                        final int componentId = Integer.valueOf(msg.split("#")[1]);
                        if(type == Component.Type.WHEEL.ordinal()){
                           car.frontWheelId = componentId;
                           userViewModel.updateCar(car);
                           new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    database.CarDao().frontWheelId(carId, componentId);
                                }
                           });
                        }
                        //TODO: GET INFO FROM ITEM
                        if(v.getBackground() == null) return false;
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        if(v.getBackground() == null) return false;
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        return true;
                    default: return false;
                }
            }
        });
 */

    }


    private boolean canAddComponent(Car car, int componentId){
        int bodyEnergy = 0;
        int componentEnergy = 0;
        for(Component c : userViewModel.getComponentsListMutableLiveData().getValue()){
            if(car.bodyId == c.id) bodyEnergy = c.energy;
            if(car.componentId1 == c.id) componentEnergy = c.energy;
        }
        return bodyEnergy >= componentEnergy;
    }

    @Override
    public void onCarFragmentInteraction() {
        //TODO: REMOVE FROM CAR FRAGMENT
    }

    @Override
    public void onItemClick(int type, int health, int energy, int power) {
        //TODO: UPDATE PART FRAGMENT
        ImageView imageView = findViewById(R.id.fragmentPartImage);
        TextView healthTextView = findViewById(R.id.health);
        TextView energyTextView = findViewById(R.id.energy);
        TextView powerTextView = findViewById(R.id.power);

        healthTextView.setText("HEALTH: " + String.valueOf(health));
        healthTextView.setVisibility(View.VISIBLE);
        energyTextView.setText("ENERGY: " + String.valueOf(energy));
        energyTextView.setVisibility(View.VISIBLE);
        powerTextView.setText("POWER: " + String.valueOf(power));
        powerTextView.setVisibility(View.VISIBLE);

        int resourceId = -1;
        switch(Component.Type.values()[type]){
            case BODY: resourceId = R.drawable.body; break;
            case BLADE: resourceId = R.drawable.weapon1; break;
            case CHAINSAW: resourceId = R.drawable.chainsaw; break;
            case FORKLIFT: resourceId = R.drawable.forklift; break;
            case ROCKET: resourceId = R.drawable.rocket_launcher; break;
            case STIGNER: resourceId = R.drawable.stinger; break;
            case WHEEL: resourceId = R.drawable.wheel; break;
        }
        imageView.setImageResource(resourceId);
        imageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemDropped(final Component c, View view) {
        int type = c.type;
        Car car = userViewModel.getCarMutableLiveData().getValue();
        final int userId = car.userId;
        switch(Component.Type.values()[type]){
            case BODY:
                if(checkCollision(findViewById(R.id.paper), view)){
                    car.bodyId = c.id;
                    userViewModel.updateCar(car);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            database.CarDao().updateBodyByUserId(userId, c.id);
                        }
                    }).start();
                    Toast.makeText(this, "COLLISION", Toast.LENGTH_LONG).show();
                }
        }
    }


    private boolean checkCollision(View v1, View v2){
        Rect r1 = new Rect(v1.getLeft(), v1.getTop(), v1.getRight(), v1.getBottom());
        Rect r2 = new Rect(v2.getLeft(), v2.getTop(), v2.getRight(), v2.getBottom());
        return r1.intersect(r2);
    }
}
