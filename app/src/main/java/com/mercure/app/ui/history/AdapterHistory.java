package com.mercure.app.ui.history;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.mercure.app.R;
import com.mercure.app.Trajet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.MonViewHolder> {

    List<Trajet> liste;
    Context mainContext;
    HistoryViewModel trajetViewModel;
    List<Integer> colors;

    public AdapterHistory(List<Trajet> liste, Context mainContext, HistoryViewModel trajetViewModel) {
        this.liste = liste;
        this.mainContext = mainContext;
        this.trajetViewModel = trajetViewModel;
        initiateColors();
    }

    @NonNull
    @Override
    public MonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.trajet_layout, parent, false);

        return new MonViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull MonViewHolder holder, int position) {
        Trajet trajet = liste.get(position);
        LocalDateTime dateTrajet = trajet.getDateTime();

        long secondsAgo = ChronoUnit.SECONDS.between(dateTrajet, LocalDateTime.now());
        long minutesAgo = ChronoUnit.MINUTES.between(dateTrajet, LocalDateTime.now());
        long hoursAgo = ChronoUnit.HOURS.between(dateTrajet, LocalDateTime.now());
        long daysAgo = ChronoUnit.DAYS.between(dateTrajet, LocalDateTime.now());
        long monthsAgo = ChronoUnit.MONTHS.between(dateTrajet, LocalDateTime.now());
        long yearsAgo = ChronoUnit.YEARS.between(dateTrajet, LocalDateTime.now());

        String txtAgo = "";

        if(yearsAgo > 0) {
            txtAgo = "Il y a " + yearsAgo + " an(s)";
        }
        else if(monthsAgo > 0) {
            txtAgo = "Il y a " + monthsAgo + " mois";
        }
        else if(daysAgo > 0) {
            txtAgo = "Il y a " + daysAgo + " jour(s)";
        }
        else if(hoursAgo > 0) {
            txtAgo = "Il y a " + hoursAgo + " heure(s)";
        }
        else if(minutesAgo > 0) {
            txtAgo = "Il y a " + minutesAgo + " minute(s)";
        }
        else if(secondsAgo >= 0) {
            txtAgo = "Il y a " + secondsAgo + " seconde(s)";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String txtDate = dateTrajet.format(formatter);
        String txtDistance = trajet.getDistance() + "m";
        Random rnd = new Random();
        int colorBackgroud = colors.get(rnd.nextInt(colors.size()));

        holder.tvDate.setText(txtDate);
        holder.tvDistance.setText(txtDistance);
        holder.tvAgo.setText(txtAgo);

        holder.cvTrajet.setCardBackgroundColor(colorBackgroud);
        holder.cvTrajet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                InfosTrajetFragment infosFrag = new InfosTrajetFragment();


                Bundle bundle = new Bundle();
                Trajet trajet = trajetViewModel.getTrajets().getValue().get(position);
                bundle.putSerializable("trajet", trajet);
                infosFrag.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, infosFrag, "infosFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    public class MonViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvDistance, tvAgo;
        CardView cvTrajet;

        public MonViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvDate);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvAgo = itemView.findViewById(R.id.tvAgo);
            cvTrajet = itemView.findViewById(R.id.cvTrajet);
        }
    }

    private void initiateColors() {
        colors = new ArrayList<Integer>();
        colors.add(ResourcesCompat.getColor(mainContext.getResources(), R.color.trajets_red1, null));
        colors.add(ResourcesCompat.getColor(mainContext.getResources(), R.color.trajets_red2, null));
        colors.add(ResourcesCompat.getColor(mainContext.getResources(), R.color.trajets_grey1, null));
        colors.add(ResourcesCompat.getColor(mainContext.getResources(), R.color.trajets_grey2, null));
        colors.add(ResourcesCompat.getColor(mainContext.getResources(), R.color.trajets_black, null));
        colors.add(ResourcesCompat.getColor(mainContext.getResources(), R.color.trajets_white, null));
    }
}
