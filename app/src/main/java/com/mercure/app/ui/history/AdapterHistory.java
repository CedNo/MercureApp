package com.mercure.app.ui.history;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.mercure.app.R;
import com.mercure.app.Trajet;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.MonViewHolder> {

    List<Trajet> liste;
    Context mainContext;
    TrajetViewModel trajetViewModel;

    boolean isPageInscrit;

    public AdapterHistory(List<Trajet> liste, Context mainContext, TrajetViewModel trajetViewModel, boolean isPageInscrit) {
        this.liste = liste;
        this.mainContext = mainContext;
        this.trajetViewModel = trajetViewModel;
        this.isPageInscrit = isPageInscrit;
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
        Trajet e = liste.get(position);

//        holder.tvTitre.setText(e.getTitre());
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    public class MonViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitre;

        public MonViewHolder(@NonNull View itemView) {
            super(itemView);

//            tvTitre = itemView.findViewById(R.id.tvTitre);
        }
    }
}
