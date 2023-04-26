package com.example.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Helper.Authentication;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class Login extends AppCompatActivity {
    EditText email, password;
    ProgressBar progressBar2;
    private GoogleSignInClient signInClient;
    GoogleSignInOptions signInOptions;
    FirebaseAuth auth;
    ActivityResultLauncher<Intent> activityResultLauncher;
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            dashboardIntent(currentUser);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        progressBar2 = findViewById(R.id.progressBar2);
        auth = Authentication.auth;
        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        signInClient = GoogleSignIn.getClient(getApplicationContext(), signInOptions);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == Activity.RESULT_CANCELED){
                resetPage();
            }
            if (result.getResultCode() == Activity.RESULT_OK){
                Intent data  = result.getData();
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                    auth.signInWithCredential(credential).addOnSuccessListener(authResult -> {
                        if(task.isSuccessful()){
                            dashboardIntent(auth.getCurrentUser());
                        }
                        else{
                            Toast.makeText(Login.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (ApiException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void onClick_login_btn(View view) {
        if (email.getText().toString().isEmpty()) {
            email.setError("Enter Email!");
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            email.setError("Enter valid Email Address!");
        } else if (password.getText().toString().isEmpty()) {
            password.setError("Enter Password!");
        }  else if (!isConnected()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Internet Connection required for this operation!");
            alert.show();
        } else {
            Button btn = findViewById(R.id.login_btn);
            btn.setEnabled(false);
            auth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                dashboardIntent(auth.getCurrentUser());
                            } else {
                                btn.setEnabled(true);
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Login.this, "Login Failed: "+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public void onClickGoogleLogin(View view) {
        if (!isConnected()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Internet Connection required for this operation!");
            alert.show();
        }else {
            setLoadingView();
            Intent popup = signInClient.getSignInIntent();
            activityResultLauncher.launch(popup);
        }
    }

    public void onClick_signup(View view) {
        startActivity(new Intent(getApplicationContext(),SignUp.class));
    }

    public void setLoadingView(){
        email.setVisibility(View.INVISIBLE);
        password.setVisibility(View.INVISIBLE);
        findViewById(R.id.login_btn).setVisibility(View.INVISIBLE);
        findViewById(R.id.side_ellipse_login).setVisibility(View.INVISIBLE);
        findViewById(R.id.main_img_login).setVisibility(View.INVISIBLE);
        findViewById(R.id.Title_login).setVisibility(View.INVISIBLE);
        findViewById(R.id.or_divider2).setVisibility(View.INVISIBLE);
        findViewById(R.id.divider).setVisibility(View.INVISIBLE);
        findViewById(R.id.divider3).setVisibility(View.INVISIBLE);
        findViewById(R.id.google_login).setVisibility(View.INVISIBLE);
        findViewById(R.id.dont_acc).setVisibility(View.INVISIBLE);
        findViewById(R.id.signup_re).setVisibility(View.INVISIBLE);
        progressBar2.setVisibility(View.VISIBLE);

    }
    public void resetPage(){
        email.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        findViewById(R.id.login_btn).setVisibility(View.VISIBLE);
        findViewById(R.id.side_ellipse_login).setVisibility(View.VISIBLE);
        findViewById(R.id.main_img_login).setVisibility(View.VISIBLE);
        findViewById(R.id.Title_login).setVisibility(View.VISIBLE);
        findViewById(R.id.or_divider2).setVisibility(View.VISIBLE);
        findViewById(R.id.divider).setVisibility(View.VISIBLE);
        findViewById(R.id.divider3).setVisibility(View.VISIBLE);
        findViewById(R.id.google_login).setVisibility(View.VISIBLE);
        findViewById(R.id.dont_acc).setVisibility(View.VISIBLE);
        findViewById(R.id.signup_re).setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.INVISIBLE);
    }


    private void dashboardIntent(FirebaseUser user) {
        finish();
        Intent dashboard_intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(dashboard_intent);
    }
    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }
        return false;
    }
}