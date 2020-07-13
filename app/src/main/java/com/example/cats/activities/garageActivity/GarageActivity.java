package com.example.cats.activities.garageActivity;

import androidx.annotation.Nullable;
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
import android.widget.Toast;

import com.example.cats.R;
import com.example.cats.activities.garageActivity.fragments.CarFragment;
import com.example.cats.activities.modifyActivity.ModifyActivity;
import com.example.cats.database.AppDatabase;
import com.example.cats.database.entities.Box;
import com.example.cats.database.entities.Car;
import com.example.cats.database.entities.Component;
import com.example.cats.database.entities.Stats;
import com.example.cats.utils.AppPrefs;
import com.example.cats.utils.MediaPlayer;
import com.example.cats.utils.UnityCommunication;
import com.example.cats.viewmodels.UserViewModel;

import org.w3c.dom.Text;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GarageActivity extends AppCompatActivity implements CarFragment.OnCarFragmentListener {
    private UserViewModel userViewModel;
    private AppDatabase database;
    private Timer timer;
    private TextView nextBox;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001){
            final int result = data.getIntExtra("result", 0);
            final Stats stats = new Stats(userViewModel.getUserMutableLiveData().getValue().id, result, String.valueOf(System.currentTimeMillis()));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    database.StatsDao().insert(stats);
                }
            }).start();
            userViewModel.addStats(stats);

            if(result == 0) return;
            int cnt = 0;
            for(Stats s: userViewModel.getStatsListMutableLiveData().getValue()){
                if(s.result == 1) cnt++;
            }
            if(userViewModel.getBoxListMutableLiveData().getValue().size() >= 5) return;
            if(cnt> 0 && cnt % 3 == 0){
                Box b = new Box(userViewModel.getUserMutableLiveData().getValue().id); System.out.println("BOX USER ID: " + b.userId + " - USER ID: " + userViewModel.getUserMutableLiveData().getValue().id);
                final Box bc = b;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        database.BoxDao().insert(bc);
                    }
                }).start();
                userViewModel.addBox(b);
            }
            nextBox.setText("Next box in: " + String.valueOf(3 - (cnt % 3)) + ((cnt % 3 == 2) ? " win" : " wins"));
        }
    }

    private Box bx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);

        userViewModel = UserViewModel.getInstance(this);
        database = AppDatabase.getInstance(this);

        ImageView settings = findViewById(R.id.garageActivitySettingsButton);
        ImageView box = findViewById(R.id.box);
        final TextView time = findViewById(R.id.time);
        final TextView count = findViewById(R.id.count);
        nextBox = findViewById(R.id.nextBox);
        final Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.garageActivityCarFragmentHolder);
        TextView fight = findViewById(R.id.fight);
        TextView stats = findViewById(R.id.stats);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSettingsDialog();
            }
        });



        //TODO: BOX ON CLICK
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ready = true;
                boolean remove = false;
                for(Box b : userViewModel.getBoxListMutableLiveData().getValue()){
                    bx = b;
                    System.out.println("HERE!!!");
                    if(b.time < Long.MAX_VALUE) {
                        ready = false;
                        long remaining = 15l * 60 * 1000 - (System.currentTimeMillis() - Long.valueOf(b.time));
                        if (remaining < 0) {
                            b.isValid = false;
                            final Box bc = b;
                            int type = new Random().nextInt(7);
                            int health = type == 0 ? new Random().nextInt(200) + 50 : new Random().nextInt(30);
                            int energy = type == 0 ? new Random().nextInt(20) + 10 : new Random().nextInt(6) + 6;
                            int power = type > 2 ? new Random().nextInt(30) + 10 : 0;
                            final Component component = new Component(userViewModel.getUserMutableLiveData().getValue().id, type, 0, health, power, energy);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    database.BoxDao().update(bc);
                                    database.ComponentDao().insert(component);
                                }
                            }).start();
                            userViewModel.addComponents(component);
                            Toast.makeText(getApplicationContext(), "Component successfully added!", Toast.LENGTH_SHORT).show();
                            remove = true;
                        }
                        break;
                    }
                }
                if(remove) userViewModel.removeBox(bx);
                if(userViewModel.getBoxListMutableLiveData().getValue().size() > 0 && ready){
                    //TODO: add time to box;
                    Box b = userViewModel.getBoxListMutableLiveData().getValue().get(0);
                    b.time = System.currentTimeMillis();
                    final Box bc = b;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            database.BoxDao().update(bc);
                        }
                    }).start();
                }
            }
        });

        userViewModel.getBoxListMutableLiveData().observe(this, new Observer<List<Box>>() {
            @Override
            public void onChanged(List<Box> boxes) {
                count.setText("Count:  " + String.valueOf(boxes.size()));
            }
        });

        //TODO: box time timer
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                boolean click = true;
                for(Box b : userViewModel.getBoxListMutableLiveData().getValue()){
                    if(b.time < Long.MAX_VALUE){
                        click = false;
                        long remaining = 15l * 60 * 1000 - (System.currentTimeMillis() - Long.valueOf(b.time));
                        long h = remaining / (60*60*1000);
                        long min = (remaining / (60*1000)) % 60;
                        long s = (remaining / 1000) % 60;
                        if(remaining < 2l*60*60*1000 && remaining >= 0){
                            time.setText("Time: " + /*h + "h " +*/ min + "min " + s + "s ");
                        }
                        else{
                            time.setText("READY");
                        }
                        break;
                    }
                    if(click){
                        time.setText("CLICK");
                    }
                }
            }
        }, 0, 1000);

        final Activity currActivity = this;
        fight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car car = userViewModel.getCarMutableLiveData().getValue();

                final Intent intent = new Intent();
                try {
                    intent.setClass(getApplicationContext(), Class.forName("com.unity3d.player.UnityPlayerActivity"));
                    intent.putExtra("useControls", AppPrefs.read(AppPrefs.CONTROLS_KEY, currActivity));
                    UnityCommunication.useControls = AppPrefs.read(AppPrefs.CONTROLS_KEY, currActivity);
                    System.out.println("CONTROLS? " + intent.getBooleanExtra("useControls", false));

                    intent.putExtra("nameLeft", userViewModel.getUserMutableLiveData().getValue().username);
                    int health = 0;
                    for(Component c : userViewModel.getComponentsListMutableLiveData().getValue()){
                            if(c.id == car.bodyId) health += c.health;
                            if(c.id == car.frontWheelId){
                                health += c.health;
                                intent.putExtra("hasFrontWheelLeft", true);
                                UnityCommunication.hasFrontWheelLeft = true;
                            }
                            if(c.id == car.backWheelId){
                                health += c.health;
                                intent.putExtra("hasBackWheelLeft", true);
                                UnityCommunication.hasBackWheelLeft = true;
                            }
                            if(c.id == car.componentId1){
                                health += c.health;
                                intent.putExtra("componentOneTypeLeft", c.type - 2);
                                intent.putExtra("powerLeft", c.power);
                                UnityCommunication.componentOneTypeLeft = c.type - 2;
                                UnityCommunication.powerLeft = c.power;
                            }
                            if(c.id == car.componentId2){
                                health += c.health;
                                intent.putExtra("hasForkliftLeft", true);
                                UnityCommunication.hasForkliftLeft = true;
                            }
                            intent.putExtra("healthLeft", health);
                            UnityCommunication.healthLeft = health;
                    }


                    String names[] = new String[]{"Richard", "John", "Annie", "Marc"};
                    intent.putExtra("nameRight", names[new Random().nextInt(4)]);
                    intent.putExtra("healthRight", health + new Random().nextInt(50) - 25);
                    intent.putExtra("powerRight", new Random().nextInt(15) + 15);
                    intent.putExtra("hasFrontWheelRight", new Random().nextBoolean());
                    intent.putExtra("hasBackWheelRight", new Random().nextBoolean());
                    intent.putExtra("hasForkliftRight", new Random().nextBoolean());
                    intent.putExtra("componentOneTypeRight", new Random().nextInt(4));

                    startActivityForResult(intent, 1001);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });



        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createStatsDialog();
            }
        });

        MediaPlayer.create(this);

        if(AppPrefs.read(AppPrefs.SOUND_KEY, this))
            MediaPlayer.start();
        else MediaPlayer.stop();
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
        //if(!AppPrefs.read(AppPrefs.SOUND_KEY, this))
            MediaPlayer.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaPlayer.create(this);
        if(AppPrefs.read(AppPrefs.SOUND_KEY, this))
            MediaPlayer.start();
        else MediaPlayer.stop();
    }
}
