package com.example.cats.database.prepopulate;

import android.os.HandlerThread;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.cats.database.AppDatabase;
import com.example.cats.database.entities.Component;

public class ComponentsCallback extends RoomDatabase.Callback {
    @Override
    public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        super.onCreate(db);

        /*new Thread(new Runnable() {
            @Override
            public void run() {

                ((AppDatabase)db).ComponentDao().insert(
                        new Component(Component.Type.BODY.ordinal(), Component.Subtype.LOW.ordinal(), 150, 0, 10, true),
                        new Component(Component.Type.BODY.ordinal(), Component.Subtype.MEDIUM.ordinal(), 250, 0, 12, true),
                        new Component(Component.Type.BODY, Component.Subtype.HIGH, 200, 0, 15, true),
                        new Component(Component.Type.WHEEL, Component.Subtype.LOW, 10, 0, 0, true),
                        new Component(Component.Type.WHEEL, Component.Subtype.MEDIUM, 20, 0, 0, true),
                        new Component(Component.Type.WHEEL, Component.Subtype.HIGH, 30, 0, 0, true),
                        new Component(Component.Type.ROCKET, Component.Subtype.LOW, 5, 5, 3, true),
                        new Component(Component.Type.FORKLIFT, Component.Subtype.LOW, 20, 0, 4, true),
                        new Component(Component.Type.STIGNER, Component.Subtype.LOW, 10, 4, 7, true),
                        new Component(Component.Type.CHAINSAW, Component.Subtype.LOW, 15, 3, 5, true)
                );
            }
        }).start();*/
    }
}
