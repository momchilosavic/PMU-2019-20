package com.example.cats.viewmodels;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.example.cats.database.entities.Box;
import com.example.cats.database.entities.Car;
import com.example.cats.database.entities.Component;
import com.example.cats.database.entities.Stats;
import com.example.cats.database.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserViewModel extends ViewModel {
    MutableLiveData<User> userMutableLiveData;
    MutableLiveData<Car> carMutableLiveData;
    MutableLiveData<List<Box>> boxListMutableLiveData;
    MutableLiveData<List<Stats>> statsListMutableLiveData;
    MutableLiveData<List<Component>> componentsListMutableLiveData;

    private static UserViewModel singletonInstance = null;

    public static UserViewModel getInstance(FragmentActivity fragment){
        if(singletonInstance == null){
            singletonInstance = ViewModelProviders.of(fragment).get(UserViewModel.class);
        }
        return singletonInstance;
    }

    public UserViewModel(){
        userMutableLiveData = new MutableLiveData<>();
        carMutableLiveData = new MutableLiveData<>();
        boxListMutableLiveData = new MutableLiveData<>((List<Box>)(new ArrayList<Box>()));
        statsListMutableLiveData = new MutableLiveData<>((List<Stats>)(new ArrayList<Stats>()));
        componentsListMutableLiveData = new MutableLiveData<>((List<Component>)(new ArrayList<Component>()));
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void setUserMutableLiveData(MutableLiveData<User> userMutableLiveData) {
        this.userMutableLiveData = userMutableLiveData;
    }

    public MutableLiveData<Car> getCarMutableLiveData() {
        return carMutableLiveData;
    }

    public void setCarMutableLiveData(MutableLiveData<Car> carMutableLiveData) {
        this.carMutableLiveData = carMutableLiveData;
    }

    public MutableLiveData<List<Box>> getBoxListMutableLiveData() {
        return boxListMutableLiveData;
    }

    public void setBoxListMutableLiveData(MutableLiveData<List<Box>> boxListMutableLiveData) {
        this.boxListMutableLiveData = boxListMutableLiveData;
    }

    public MutableLiveData<List<Stats>> getStatsListMutableLiveData() {
        return statsListMutableLiveData;
    }

    public void setStatsListMutableLiveData(MutableLiveData<List<Stats>> statsListMutableLiveData) {
        this.statsListMutableLiveData = statsListMutableLiveData;
    }

    public MutableLiveData<List<Component>> getComponentsListMutableLiveData() {
        return componentsListMutableLiveData;
    }

    public void setComponentsListMutableLiveData(MutableLiveData<List<Component>> componentsListMutableLiveData) {
        this.componentsListMutableLiveData = componentsListMutableLiveData;
    }

    public void updateUser(User user){
        this.userMutableLiveData.setValue(user);
    }

    public void updateCar(Car car){
        this.carMutableLiveData.setValue(car);
    }

    public void updateBoxes(List<Box> boxes){
        this.boxListMutableLiveData.setValue(boxes);
    }

    public void updateStats(List<Stats> stats){
        this.statsListMutableLiveData.setValue(stats);
    }

    public void addStats(Stats stats){
        List<Stats> list = statsListMutableLiveData.getValue();
        list.add(stats);
        statsListMutableLiveData.setValue(list);
    }

    public void addBox(Box box){
        List<Box> list = boxListMutableLiveData.getValue();
        list.add(box);
        boxListMutableLiveData.setValue(list);
    }

    public void removeBox(Box box){
        List<Box> list = boxListMutableLiveData.getValue();
        list.remove(box);
        boxListMutableLiveData.setValue(list);
    }

    public void updateComponents(List<Component> components){
        this.componentsListMutableLiveData.setValue(components);
    }

    public void addComponents(Component component){
        List<Component> list = this.componentsListMutableLiveData.getValue();
        list.add(component);
        this.componentsListMutableLiveData.setValue(this.componentsListMutableLiveData.getValue());
    }
}
