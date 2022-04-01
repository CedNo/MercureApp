package com.mercure.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.os.Vibrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Iterator;
import java.util.List;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Activity_Cnx extends AppCompatActivity {

    public CircularProgressButton btn;
    InterfaceServeur serveur;
    List<Utilisateur> listeUtilisateurs;
    Context context;
    EditText txtUtilisateur, txtMdp;
    ScrollView scrollView;
    ConstraintLayout layoutConnexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cnx);
        int transparent = Color.TRANSPARENT;
        txtUtilisateur = findViewById(R.id.txtUtilisateur);
        txtMdp = findViewById(R.id.txtMdp);
        scrollView = findViewById(R.id.scConnexion);
        layoutConnexion = findViewById(R.id.layoutConnexion);
        layoutConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(Activity_Cnx.this);
            }
        });

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        btn = (CircularProgressButton) findViewById(R.id.bt_cnx);
        Bitmap ic_error = drawableToBitmap(getResources().getDrawable(R.drawable.ic_error));
        Bitmap ic_check = drawableToBitmap(getResources().getDrawable(R.drawable.ic_check));

        context = this;

        serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn.startAnimation();
                getUtilisateur();

                //Enleve le rouge alentours des edittext
                txtUtilisateur.setBackgroundResource(R.drawable.custom_input);
                txtMdp.setBackgroundResource(R.drawable.custom_input);

                //Ferme le clavier quand on appuit sur le bouton cnx
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);

                //Unfocus les edittext
                txtUtilisateur.clearFocus();
                txtMdp.clearFocus();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(verifChamp(txtUtilisateur.getText().toString().trim(), txtMdp.getText().toString().trim()))
                        {
                            v.vibrate(400);
                            btn.revertAnimation();

                        }
                        else {
                            //Termine l'activité de cnx et démarre la main activity
                            btn.doneLoadingAnimation(transparent, ic_check);
                            txtUtilisateur.setBackgroundResource(R.drawable.custom_input);
                            txtMdp.setBackgroundResource(R.drawable.custom_input);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(Activity_Cnx.this, MainActivity.class));
                                    finish();
                                }
                            }, 2000);
                        }
                    }
                }, 2000);
            }
        });
            //scrollView.scrollTo(0, (int) btn.getY());
    }

// Methode qui converti un drawable en bitmap
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }


        final int width = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().height() : drawable.getIntrinsicHeight();


        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width, height <= 0 ? 1 : height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public boolean verifChamp(String utilisateur, String mdp){

        if(utilisateur.equals(""))
        {
            txtUtilisateur.setBackgroundResource(R.drawable.txt_error);
            Toast.makeText(context, "Vous devez remplir le champ nom d'utilisateur.", Toast.LENGTH_SHORT).show();
            if (mdp.equals("")){
                txtMdp.setBackgroundResource(R.drawable.txt_error);
            }
            return true;
        }
        else if (mdp.equals(""))
        {
            txtMdp.setBackgroundResource(R.drawable.txt_error);
            Toast.makeText(context, "Vous devez remplir le champ mot de passe.", Toast.LENGTH_SHORT).show();
            if (utilisateur.equals("")){
                txtUtilisateur.setBackgroundResource(R.drawable.txt_error);
            }
            return true;
        }
        else if (listeUtilisateurs == null)
        {
            Toast.makeText(context, "Connexion indisponible veuillez réessayer plus tard.", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(verifNomUtilisateur(utilisateur) != true)
        {
            txtUtilisateur.setBackgroundResource(R.drawable.txt_error);
            txtMdp.setBackgroundResource(R.drawable.txt_error);
            Toast.makeText(context, "Veuillez entrer un nom d'utilisateur/mot de passe valide.", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(verifMdp(mdp) != true)
        {
            txtUtilisateur.setBackgroundResource(R.drawable.txt_error);
            txtMdp.setBackgroundResource(R.drawable.txt_error);
            Toast.makeText(context, "Veuillez entrer un nom d'utilisateur/mot de passe valide.", Toast.LENGTH_SHORT).show();
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


    public boolean verifNomUtilisateur(String utilisateur)
    {
        boolean boolUser = false;

        Iterator iterator = listeUtilisateurs.iterator();
        while(iterator.hasNext()) {
            Utilisateur user = (Utilisateur) iterator.next();
            if(utilisateur.equals(user.getUsername()))
            {
                boolUser = true;
                break;
            }
            else boolUser = false;

        }

        return boolUser;
    }

    public boolean verifMdp(String mdp)
    {
        boolean boolMdp = false;
        String hash = encryptThisString(mdp);

        Iterator iterator = listeUtilisateurs.iterator();
        while(iterator.hasNext()) {
            Utilisateur user = (Utilisateur) iterator.next();
            if(hash.equals(user.getPassword()))
            {
                boolMdp = true;
                break;
            }
            else boolMdp = false;

        }

        return boolMdp;
    }

    public static String encryptThisString(String input)
    {
        try {
            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }
}