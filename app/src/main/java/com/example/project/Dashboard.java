package com.example.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.project.Fragments.AboutUsFragment;
import com.example.project.Fragments.HomeFragment;
import com.example.project.Fragments.InsightFragment;
import com.example.project.Fragments.InvoiceFragment;
import com.example.project.Fragments.LogsFragment;
import com.example.project.Fragments.StockFragment;
import com.example.project.Helper.Authentication;
import com.example.project.Helper.UserData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Dashboard extends AppCompatActivity {
    private static final int MENU_HOME = 1;
    private static final int MENU_INVOICE = 2;
    private static final int MENU_STOCK = 3;
    private static final int MENU_INSIGHT = 4;
    private static final int MENU_LOGS = 5;
    ScaleAnimation scaleAnimation;
    FirebaseAuth auth;
    FirebaseUser user;
    private int selectedMenuItem = MENU_HOME;
    private GoogleSignInClient mGoogleSignInClient;
    TextView home_btn_tv,invoice_btn_tv,stock_btn_tv, insight_btn_tv, logs_btn_tv;
    LinearLayout home_btn,invoice_btn,stock_btn, insight_btn, logs_btn,nav_bar;
    DrawerLayout drawerLayout;
    NavigationView navView;
    GoogleSignInOptions options;
    Executor executor;
    public static UserData userData;
    private final View.OnClickListener menuBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int itemId = 0;
            int id = view.getId();
            if (id == R.id.home_btn) {
                itemId = MENU_HOME;
            } else if (id == R.id.invoice_btn) {
                itemId = MENU_INVOICE;
            } else if (id == R.id.stock_btn) {
                itemId = MENU_STOCK;
            } else if (id == R.id.insights_btn) {
                itemId = MENU_INSIGHT;
            } else if (id == R.id.logs_btn) {
                itemId = MENU_LOGS;
            }

            if (itemId != 0 && itemId != selectedMenuItem) {
                int finalItemId = itemId;
                executor.execute(() -> updateMenuSelection(finalItemId));
            }

            drawerLayout.closeDrawer(GravityCompat.START);
        }
    };
    @Override
    public void onBackPressed() {
        // Get the current fragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

        // Check if the current fragment is HomeFragment
        if (currentFragment instanceof HomeFragment) {
            // Call the default implementation of onBackPressed() to handle back press for activity
            if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START, true);
            } else {
                moveTaskToBack(true);
            }
        } else if (currentFragment instanceof AboutUsFragment) {
            back_to_home(getCurrentFocus());
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dashboard);
        drawerLayout = findViewById(R.id.drawer);
        navView = findViewById(R.id.side_nav);

        executor = Executors.newFixedThreadPool(5);
        executor.execute(this::side_nav_init);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        executor.execute(this::data);

        nav_bar = findViewById(R.id.nav_bar);
        home_btn = findViewById(R.id.home_btn);
        invoice_btn = findViewById(R.id.invoice_btn);
        stock_btn = findViewById(R.id.stock_btn);
        insight_btn = findViewById(R.id.insights_btn);
        logs_btn = findViewById(R.id.logs_btn);
        home_btn.setOnClickListener(menuBtnClickListener);
        invoice_btn.setOnClickListener(menuBtnClickListener);
        stock_btn.setOnClickListener(menuBtnClickListener);
        insight_btn.setOnClickListener(menuBtnClickListener);
        logs_btn.setOnClickListener(menuBtnClickListener);

        home_btn_tv = findViewById(R.id.home_btn_tv);
        invoice_btn_tv = findViewById(R.id.invoice_btn_tv);
        stock_btn_tv = findViewById(R.id.stock_btn_tv);
        insight_btn_tv = findViewById(R.id.insight_btn_tv);
        logs_btn_tv = findViewById(R.id.logs_btn_tv);

        scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        scaleAnimation.setDuration(200);
        scaleAnimation.setFillAfter(true);
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainerView, HomeFragment.class, null).commit();
        options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), options);

    }
    public void data(){
        String uname = Objects.requireNonNull(user.getEmail()).trim();
        userData = new UserData(uname);
    }
    private void side_nav_init() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.OpenDrawer, R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        View view = navView.getHeaderView(0);
        TextView name = view.findViewById(R.id.nav_name);
        TextView email = view.findViewById(R.id.nav_email);
        ImageView avatar = view.findViewById(R.id.nav_avtar);

        assert user != null;

        String displayName;
        String userEmail;
        Bitmap bitmap = null;

            // Load user data
        displayName = user.getDisplayName() != null ? user.getDisplayName() : "";
        userEmail = user.getEmail() != null ? user.getEmail() : "";

            // Load avatar image
        Uri url = user.getPhotoUrl();
        if (url != null) {
            try {
                bitmap = Glide.with(Dashboard.this)
                        .asBitmap()
                        .load(url)
                        .submit()
                        .get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Update UI on the main thread using LiveData
        final String finalDisplayName = displayName;
        final String finalUserEmail = userEmail;
        final Bitmap finalBitmap = bitmap;
        runOnUiThread(() -> {
            name.setText(finalDisplayName);
            email.setText(finalUserEmail);
            if (finalBitmap != null) {
                avatar.setImageBitmap(finalBitmap);
            } else {
                avatar.setImageResource(R.drawable.profile);
            }
        });

        executor.execute(()-> navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.logout) {
                logout();
            } else if (id == R.id.about_us) {
                nav_bar.setVisibility(View.GONE);
                new Thread(()-> getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainerView, AboutUsFragment.class, null).commit()).start();
            } else {
                Toast.makeText(Dashboard.this, "Rate app", Toast.LENGTH_SHORT).show();
            }
            drawerLayout.closeDrawer(GravityCompat.START, true);
            return true;
        }));
    }
    public void back_to_home(View view) {
        updateMenuSelection(MENU_HOME);
        nav_bar.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), androidx.navigation.ui.R.anim.nav_default_enter_anim));
        nav_bar.setVisibility(View.VISIBLE);
    }
    private void updateMenuSelection(int itemId) {
        // Set unselected background for all menu buttons
        runOnUiThread(() -> {
            home_btn.setBackgroundResource(R.color.transparent);
            invoice_btn.setBackgroundResource(R.color.transparent);
            stock_btn.setBackgroundResource(R.color.transparent);
            insight_btn.setBackgroundResource(R.color.transparent);
            logs_btn.setBackgroundResource(R.color.transparent);

            home_btn_tv.setVisibility(View.GONE);
            invoice_btn_tv.setVisibility(View.GONE);
            stock_btn_tv.setVisibility(View.GONE);
            insight_btn_tv.setVisibility(View.GONE);
            logs_btn_tv.setVisibility(View.GONE);
        });

        // Set selected background for the clicked menu button
        switch (itemId) {
            case MENU_HOME:
                load_fragment(HomeFragment.class);
                runOnUiThread(()->{
                    home_btn.setBackgroundResource(R.drawable.botton_nav_btn);
                    home_btn_tv.setVisibility(View.VISIBLE);
                    home_btn.startAnimation(scaleAnimation);
                });
                break;
            case MENU_INVOICE:
                load_fragment(InvoiceFragment.class);
                runOnUiThread(()->{
                    invoice_btn.setBackgroundResource(R.drawable.botton_nav_btn);
                    invoice_btn_tv.setVisibility(View.VISIBLE);
                    invoice_btn.startAnimation(scaleAnimation);
                });
                break;
            case MENU_STOCK:
                load_fragment(StockFragment.class);
                runOnUiThread(()->{
                    stock_btn.setBackgroundResource(R.drawable.botton_nav_btn);
                    stock_btn_tv.setVisibility(View.VISIBLE);
                    stock_btn.startAnimation(scaleAnimation);
                });
                break;
            case MENU_INSIGHT:
                load_fragment(InsightFragment.class);
                runOnUiThread(()->{
                    insight_btn.setBackgroundResource(R.drawable.botton_nav_btn);
                    insight_btn_tv.setVisibility(View.VISIBLE);
                    insight_btn.startAnimation(scaleAnimation);
                });
                break;
            case MENU_LOGS:
                load_fragment(LogsFragment.class);
                runOnUiThread(()->{
                    logs_btn.setBackgroundResource(R.drawable.botton_nav_btn);
                    logs_btn_tv.setVisibility(View.VISIBLE);
                    logs_btn.startAnimation(scaleAnimation);
                });
                break;
        }

        // Update selected menu item
        selectedMenuItem = itemId;
    }

    public void logout() {
        // Create a thread for Google Sign-In sign-out
        Thread googleSignOutThread = new Thread(() -> mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
            // Google Sign-In sign-out complete
        }));

        // Create a thread for Firebase Authentication sign-out
        Thread firebaseSignOutThread = new Thread(() -> Authentication.auth.signOut());

        // Start both threads concurrently
        googleSignOutThread.start();
        firebaseSignOutThread.start();

        // Wait for both threads to complete
        try {
            googleSignOutThread.join();
            firebaseSignOutThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Finish the current activity
        finish();

        // Start the Login activity
        Intent signOut_intent = new Intent(getApplicationContext(), Login.class);
        signOut_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(signOut_intent);
    }

    public void openDrawer(View view){
        drawerLayout.openDrawer(GravityCompat.START, true);
    }
    public void load_fragment(Class<? extends Fragment> fragmentClass) {
        executor.execute(()->{
            try {
                Fragment fragment = fragmentClass.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.enter,
                                R.anim.exit)
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainerView, fragment, null)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}