package com.mercure.app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TrajetDAO {

    @Query("SELECT * FROM trajets ORDER BY id")
    public Trajet[] getTrajets();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void ajouterPlusieursTrajets(List<Trajet> trajets);

    @Query("SELECT * FROM trajets ORDER BY ID DESC LIMIT 1")
    public Trajet getLastTrajet();

    @Query("SELECT COUNT(*) FROM trajets")
    public int getNombreTrajets();

    @Query("DELETE FROM trajets")
    public int deleteTrajets();

}
