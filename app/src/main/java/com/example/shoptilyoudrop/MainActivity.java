package com.example.shoptilyoudrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button logout;
    private EditText price;
    private AutoCompleteTextView food;
    private EditText store;
    private Button addName;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ArrayAdapter adapter;
    private Button checkout;
    private String fName;
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, savedInstanceState);
        setContentView(R.layout.activity_main);
        logout = findViewById(R.id.logout);
        price = findViewById(R.id.price);
        store = findViewById(R.id.store);
        food = findViewById(R.id.food);
        addName = findViewById(R.id.add);
        checkout = findViewById(R.id.checkOut);

        signInButton = findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });



        Intent intent = getIntent();
        String tempName = "";

       if (intent.getStringExtra("Test") != null) {
            tempName = intent.getStringExtra("Test");
        }
        if (tempName != null) {
            fName = tempName.replace(".", ",");
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
            }
        });
        addName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodToText = food.getText().toString();
                String priceToText = price.getText().toString();
                String storeToText = store.getText().toString();
                if (foodToText.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Invalid Food", Toast.LENGTH_SHORT).show();
                } else if (storeToText.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Invalid Store", Toast.LENGTH_SHORT).show();
                } else if (priceToText.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Invalid Price", Toast.LENGTH_SHORT).show();
                } else {
                    if (!fName.isEmpty()) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference mRef = database.getReference("").child("Database");
                        mRef.child(fName).child(foodToText).child(storeToText).setValue(Double.parseDouble(priceToText));
                        Toast.makeText(MainActivity.this, "Added to " + fName, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid Account", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        final ArrayList<String> list = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("").child("Database").child(fName);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    list.add(postSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        food.setAdapter(adapter);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try{
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(MainActivity.this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        }
        catch (ApiException e) {

            Toast.makeText(MainActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }

    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                else {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

            }
        });
    }

    private void updateUI(FirebaseUser fUser){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account !=  null){
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();

            Toast.makeText(MainActivity.this,personName + personEmail ,Toast.LENGTH_SHORT).show();
        }

    }

    public AutoCompleteTextView getFood() {
        return food;
    }

    public EditText getPrice() {
        return price;
    }

    public EditText getStore() {
        return store;
    }

    public void setfName(String newName) {
        fName = newName;
    }
}
