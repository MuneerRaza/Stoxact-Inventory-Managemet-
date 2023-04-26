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
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Helper.Authentication;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class SignUp extends AppCompatActivity {

    EditText name,email,password,confirm_password;
    ProgressBar bar;
    private GoogleSignInClient signInClient;
    GoogleSignInOptions signInOptions;
    ActivityResultLauncher<Intent> activityResultLauncher;
    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        name = findViewById(R.id.full_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.password_confirm);
        bar = findViewById(R.id.progressBar);


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
                            Toast.makeText(SignUp.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (ApiException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public void onClickRegister(View view) {
        if (name.getText().toString().isEmpty()) {
            name.setError("Enter Name!");
        } else if (email.getText().toString().isEmpty()) {
            email.setError("Enter Email!");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Enter valid Email Address!");
        } else if (password.getText().toString().isEmpty()) {
            password.setError("Enter Password!");
        } else if (password.length() < 8) {
            password.setError("Length should be 8!");
        } else if (confirm_password.getText().toString().isEmpty()) {
            confirm_password.setError("Confirm Password!");
        } else if (!password.getText().toString().equals(confirm_password.getText().toString())) {
            confirm_password.setBackgroundColor(getColor(R.color.red));
            Toast.makeText(this, "Password doesn't match!", Toast.LENGTH_SHORT).show();
        } else if (!isConnected()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Internet Connection required for this operation!");
            alert.show();
        } else {
            Button btn = findViewById(R.id.register_btn);
            btn.setEnabled(false);
            auth.createUserWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim()).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name.getText().toString().trim())
                            .build();

                    Objects.requireNonNull(user).updateProfile(profileUpdates)
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    dashboardIntent(user);
                                }else {
                                    btn.setEnabled(true);
                                    Toast.makeText(SignUp.this, "Sign up Failed: " + Objects.requireNonNull(Objects.requireNonNull(task1.getException()).getMessage()), Toast.LENGTH_LONG).show();
                                }
                            });

                }
                else {
                    btn.setEnabled(true);
                    Toast.makeText(SignUp.this, "Sign up Failed: " + Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()), Toast.LENGTH_LONG).show();
                }
            });

        }
    }
    public void onClickGoogleSignup(View view) {
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
    private void dashboardIntent(FirebaseUser user) {
        finish();
        Intent dashboard_intent = new Intent(getApplicationContext(), Dashboard.class);
        dashboard_intent.putExtra("user",user);
        startActivity(dashboard_intent);
    }
    public void setLoadingView(){
        name.setVisibility(View.INVISIBLE);
        email.setVisibility(View.INVISIBLE);
        password.setVisibility(View.INVISIBLE);
        confirm_password.setVisibility(View.INVISIBLE);
        findViewById(R.id.register_btn).setVisibility(View.INVISIBLE);
        findViewById(R.id.side_ellipse_sign_up).setVisibility(View.INVISIBLE);
        findViewById(R.id.main_image_sign_up).setVisibility(View.INVISIBLE);
        findViewById(R.id.Title_sign_up).setVisibility(View.INVISIBLE);
        findViewById(R.id.or_divider).setVisibility(View.INVISIBLE);
        findViewById(R.id.divider1).setVisibility(View.INVISIBLE);
        findViewById(R.id.divider2).setVisibility(View.INVISIBLE);
        findViewById(R.id.google_signup).setVisibility(View.INVISIBLE);
        findViewById(R.id.already_acc).setVisibility(View.INVISIBLE);
        findViewById(R.id.login_re).setVisibility(View.INVISIBLE);
        bar.setVisibility(View.VISIBLE);

    }
    public void resetPage(){
        name.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        confirm_password.setVisibility(View.VISIBLE);
        findViewById(R.id.register_btn).setVisibility(View.VISIBLE);
        findViewById(R.id.side_ellipse_sign_up).setVisibility(View.VISIBLE);
        findViewById(R.id.main_image_sign_up).setVisibility(View.VISIBLE);
        findViewById(R.id.Title_sign_up).setVisibility(View.VISIBLE);
        findViewById(R.id.or_divider).setVisibility(View.VISIBLE);
        findViewById(R.id.divider1).setVisibility(View.VISIBLE);
        findViewById(R.id.divider2).setVisibility(View.VISIBLE);
        findViewById(R.id.google_signup).setVisibility(View.VISIBLE);
        findViewById(R.id.already_acc).setVisibility(View.VISIBLE);
        findViewById(R.id.login_re).setVisibility(View.VISIBLE);
        bar.setVisibility(View.INVISIBLE);
    }
    public void onClick_login(View view) {
        startActivity(new Intent(getApplicationContext(),Login.class));
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