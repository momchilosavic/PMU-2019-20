package com.example.cats.activities.garageActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cats.R;
import com.example.cats.activities.garageActivity.fragments.CarFragment;
import com.example.cats.activities.modifyActivity.ModifyActivity;
import com.example.cats.database.entities.Car;
import com.example.cats.database.entities.Component;
import com.example.cats.database.entities.Stats;
import com.example.cats.utils.AppPrefs;
import com.example.cats.utils.MediaPlayer;
import com.example.cats.viewmodels.UserViewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class GarageActivity extends AppCompatActivity implements CarFragment.OnCarFragmentListener {
    private UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);

        userViewModel = UserViewModel.getInstance(this);

        ImageView settings = findViewById(R.id.garageActivitySettingsButton);
        ImageView box = findViewById(R.id.box);
        TextView time = findViewById(R.id.time);
        TextView count = findViewById(R.id.count);
        TextView nextBox = findViewById(R.id.nextBox);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.garageActivityCarFragmentHolder);
        TextView fight = findViewById(R.id.fight);
        TextView stats = findViewById(R.id.stats);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSettingsDialog();
            }
        });

        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: BOX
            }
        });

        //TODO: box time timer

        fight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: START FIGHT
            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createStatsDialog();
            }
        });

        MediaPlayer.create(this);
        if(AppPrefs.read(AppPrefs.SOUND_KEY, this)) MediaPlayer.start();
    }

    @Override
    public void onCarFragmentInteraction() {
        Intent intent = new Intent(this, ModifyActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void createSettingsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("SETTINGS");

        String[] keys = new String[]{"Sound?", "Manual controls?"};
        final boolean[] values = new boolean[]{AppPrefs.read(AppPrefs.SOUND_KEY, this), AppPrefs.read(AppPrefs.CONTROLS_KEY, this)};
        final Activity activity = this;
        builder.setMultiChoiceItems(keys, values, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                values[which] = isChecked;
                if(which == 0) {
                    AppPrefs.write(AppPrefs.SOUND_KEY, isChecked, activity);
                    if(isChecked) MediaPlayer.start();
                    else MediaPlayer.stop();
                }
                else AppPrefs.write(AppPrefs.CONTROLS_KEY, isChecked, activity);
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createStatsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("STATS");

        List<Stats> list = userViewModel.getStatsListMutableLiveData().getValue();
        int wins = 0;
        int lost = 0;
        for(Stats stats : list){
            if(stats.result == Stats.Result.WIN.ordinal()) wins++;
            else lost++;
        }
        builder.setMessage("Won: " + wins + "\nLost: " + lost);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayer.release();
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
