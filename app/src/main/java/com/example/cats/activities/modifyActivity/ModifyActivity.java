package com.example.cats.activities.modifyActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.KeyEventDispatcher;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cats.R;
import com.example.cats.activities.garageActivity.fragments.CarFragment;
import com.example.cats.database.entities.Component;
import com.example.cats.utils.AppPrefs;
import com.example.cats.utils.MediaPlayer;
import com.example.cats.viewmodels.UserViewModel;

public class ModifyActivity extends AppCompatActivity
        implements CarFragment.OnCarFragmentListener, InventoryAdapter.OnListItemClickListener {

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = UserViewModel.getInstance(this);
        setContentView(R.layout.activity_modify);
    }

    @Override
    public void onCarFragmentInteraction() {
        //TODO: NOTHING
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
            case FORKLIFT: resourceId = -1;
            case ROCKET: resourceId = R.drawable.rocket_launcher; break;
            case STIGNER: resourceId = R.drawable.stinger; break;
            case WHEEL: resourceId = R.drawable.wheel; break;
        }
        imageView.setImageResource(resourceId);
        imageView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayer.setDelayedPause();
        MediaPlayer.delayedPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaPlayer.resetDelayedPause();
        //if(AppPrefs.read(AppPrefs.SOUND_KEY, this))
            //MediaPlayer.start();
    }
}
