package com.example.cats.activities.welcomeActivity.fragments;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.cats.R;
import com.example.cats.activities.garageActivity.GarageActivity;
import com.example.cats.database.AppDatabase;
import com.example.cats.database.entities.Box;
import com.example.cats.database.entities.Car;
import com.example.cats.database.entities.Component;
import com.example.cats.database.entities.Stats;
import com.example.cats.database.entities.User;
import com.example.cats.utils.AESCrypt;
import com.example.cats.viewmodels.UserViewModel;

import java.util.List;
import java.util.Random;

public class WelcomeActivity
        extends AppCompatActivity
        implements LoginFragment.OnLoginListener, RegisterFragment.OnRegisterListener, ForgotPasswordFragment.OnForgotPasswordListener {
    private WelcomeFragment welcomeFragment;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private ForgotPasswordFragment forgotPasswordFragment;

    private Handler handler;
    private Runnable runnable;

    private AppDatabase database;
    private UserViewModel userViewModel;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        welcomeFragment = new WelcomeFragment();
        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();
        forgotPasswordFragment = new ForgotPasswordFragment();

        setContentView(R.layout.activity_welcome);

        database = AppDatabase.getInstance(this);
        userViewModel = UserViewModel.getInstance(this);


        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragmentHolder, welcomeFragment).commit();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragmentHolder, loginFragment).commit();
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public void onLogin(int userId) {
        System.err.println("USER ID OR: "  + userId);
        database.ComponentDao().getByUserId(userId).observe(this, new Observer<List<Component>>() {
            @Override
            public void onChanged(List<Component> components) {
                userViewModel.updateComponents(components);
            }
        });

        database.CarDao().getByUserId(userId).observe(this, new Observer<Car>() {
            @Override
            public void onChanged(Car car) {
                userViewModel.updateCar(car);
            }
        });

        database.BoxDao().getByUserId(userId).observe(this, new Observer<List<Box>>() {
            @Override
            public void onChanged(List<Box> boxes) {
                userViewModel.updateBoxes(boxes);
            }
        });

        database.StatsDao().getByUserId(userId).observe(this, new Observer<List<Stats>>() {
            @Override
            public void onChanged(List<Stats> stats) {
                userViewModel.updateStats(stats);
            }
        });

        startGarageActivity();
    }

    @Override
    public void onGotoRegister() {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragmentHolder, registerFragment).commit();
    }

    @Override
    public void onForgotPassword() {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragmentHolder, forgotPasswordFragment).commit();
    }

    @Override
    public void onRegister(final int userId) {
        //TODO: GENERATE COMPONENTS
        int bodyType = new Random().nextInt(3);
        int bodyHealth = new Random().nextInt(100) + 150;
        int bodyEnergy = new Random().nextInt(10) + 15;
        final Component body = new Component(userId, Component.Type.BODY.ordinal(), bodyType, bodyHealth, 0, bodyEnergy);
        new Thread(new Runnable() {
            @Override
            public void run() {
                database.ComponentDao().insert(body);
            }
        }).start();

        for(int i = 0; i < 2; i++){
            int wheelHealth = new Random().nextInt(5) + 15;
            int wheelType = new Random().nextInt(3);
            final Component wheel = new Component(userId, Component.Type.WHEEL.ordinal(), wheelType, wheelHealth, 0, 0);
            new Thread((new Runnable() {
                @Override
                public void run() {
                    database.ComponentDao().insert(wheel);
                }
            })).start();
        }

        for(int i = 0; i < 2; i++){
            int weaponType = new Random().nextInt(5) + 2;
            int weaponHealth = new Random().nextInt(10) + 15;
            int weaponPower = new Random().nextInt(30) + 20;
            int weaponEnergy = new Random().nextInt(7) + 5;
            final Component weapon = new Component(userId, weaponType, 0, weaponHealth, weaponType == Component.Type.FORKLIFT.ordinal() ? 0 : weaponPower, weaponEnergy);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    database.ComponentDao().insert(weapon);
                }
            }).start();
        }

        //TODO: UPDATE VIEWMODEL
        database.ComponentDao().getByUserId(userId).observe(this, new Observer<List<Component>>() {
            @Override
            public void onChanged(List<Component> components) {
                userViewModel.updateComponents(components);
            }
        });

        //TODO: UPDATE CAR
        userViewModel.getComponentsListMutableLiveData().observe(this, new Observer<List<Component>>() {
            @Override
            public void onChanged(List<Component> components) {
                for(Component component : components){
                    if(Component.Type.values()[component.type] == Component.Type.BODY){
                        final Car car = new Car(userId, component.id, 0, 0, 0, 0, 0);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                database.CarDao().insert(car);
                            }
                        }).start();
                    }
                }
            }
        });

        //TODO: UPDATE VIEWMDOEL
        database.CarDao().getByUserId(userId).observe(this, new Observer<Car>() {
            @Override
            public void onChanged(Car car) {
                userViewModel.updateCar(car);
            }
        });

        startGarageActivity();
    }

    @Override
    public void onGotoLogin() {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragmentHolder, loginFragment).commit();
    }

    @Override
    public void onForgotPassword(String mail) {
        database.UserDao().getPasswordByMail(mail).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                try {
                    String password = AESCrypt.decrypt(s);
                    //TODO: SEND MAIL PROGRAMATICALLY
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackToLogin() {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragmentHolder, loginFragment).commit();
    }

    private void startGarageActivity(){
        Intent intent = new Intent(this, GarageActivity.class);
        startActivity(intent);
    }
}
