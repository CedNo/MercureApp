package com.mercure.app;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Trajet.class}, version = 1)
public abstract class TrajetsDB extends RoomDatabase {
    public abstract TrajetDAO tdao();
}
