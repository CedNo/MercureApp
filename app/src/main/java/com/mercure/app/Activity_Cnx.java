package com.mercure.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Cnx extends AppCompatActivity {

    public CircularProgressButton btn;
    InterfaceServeur serveur;
    List<Utilisateur> listeUtilisateurs;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cnx);
        int transparent = Color.TRANSPARENT;
        EditText txtUtilisateur, txtMdp;

        btn = (CircularProgressButton) findViewById(R.id.bt_cnx);
        txtUtilisateur = findViewById(R.id.txtUtilisateur);
        txtMdp = findViewById(R.id.txtMdp);
        Bitmap ic_error = drawableToBitmap(getResources().getDrawable(R.drawable.ic_error));
        Bitmap ic_check = drawableToBitmap(getResources().getDrawable(R.drawable.ic_check));

        context = this;

        serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn.startAnimation();
                getUtilisateur();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(verifChamp(txtUtilisateur.getText().toString().trim(), txtMdp.getText().toString().trim()))
                        {
                            //btn.doneLoadingAnimation(transparent, ic_error);
                            txtUtilisateur.setBackgroundResource(R.drawable.txt_error);
                            txtMdp.setBackgroundResource(R.drawable.txt_error);
                            btn.revertAnimation();
                        }
                        else {
                            btn.doneLoadingAnimation(transparent, ic_check);
                            txtUtilisateur.setBackgroundResource(R.drawable.custom_input);
                            txtMdp.setBackgroundResource(R.drawable.custom_input);


                        }
                    }
                }, 2000);





                //btn.revertAnimation();
            }
        });

    }










    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        // We ask for the bounds if they have been set as they would be most
        // correct, then we check we are  > 0
        final int width = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().height() : drawable.getIntrinsicHeight();

        // Now we check we are > 0
        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width, height <= 0 ? 1 : height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public boolean verifChamp(String txtUtilisateur, String txtMdp){

        if(txtUtilisateur.equals(""))
        {
            return true;
        }
        else if (txtMdp.equals(""))
        {
            return true;
        }
        else if (listeUtilisateurs == null)
        {
            Toast.makeText(context, "Connexion indisponible veuillez réessayer plus tard", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(verifNomUtilisateur(txtUtilisateur) != true)
        {
            return true;
        }
        else return false;
    }

    public void getUtilisateur()
    {
        Call<List<Utilisateur>> call = serveur.getUtilisateurs();

        call.enqueue(new Callback<List<Utilisateur>>() {
            @Override
            public void onResponse(Call<List<Utilisateur>> call, Response<List<Utilisateur>> response) {
                listeUtilisateurs = response.body();

            }

            @Override
            public void onFailure(Call<List<Utilisateur>> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });


    }


    public boolean verifNomUtilisateur(String txtUtilisateur)
    {
        boolean boolUser = false;

        Iterator iterator = listeUtilisateurs.iterator();
        while(iterator.hasNext()) {
            Utilisateur user = (Utilisateur) iterator.next();
            if(txtUtilisateur.equals(user.getUsername()))
            {
                boolUser = true;
                break;
            }
            else boolUser = false;

        }

        return boolUser;

    }



}