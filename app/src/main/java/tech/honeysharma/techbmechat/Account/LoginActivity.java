package tech.honeysharma.techbmechat.Account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import tech.honeysharma.techbmechat.R;
import tech.honeysharma.techbmechat.Utility.StringUtils;


/**
 * Created by Honey Sharma
 */
public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private TextInputLayout mLoginEmail;
    private TextInputLayout mLoginPassword;

    private Button mLogin_btn;

    private ProgressDialog mLoginProgress;

    private FirebaseAuth mAuth;

    private DatabaseReference mUserDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");


        mLoginProgress = new ProgressDialog(this);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        mLoginEmail = (TextInputLayout) findViewById(R.id.login_email);
        mLoginPassword = (TextInputLayout) findViewById(R.id.login_password);
        mLogin_btn = (Button) findViewById(R.id.login_btn);

        mLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mLoginEmail.getEditText().getText().toString();
                String password = mLoginPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && StringUtils.isEmailValid(email)){
                    mLoginProgress.setTitle("Logging In");
                    mLoginProgress.setMessage("Please wait while we check your credentials.");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();

                    loginUser(email, password);
                } else {
                    //TODO show error dialog
                }
            }
        });

        if (mLoginPassword.getEditText() != null) {
            mLoginPassword.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        mLogin_btn.callOnClick();
                    }
                    return false;
                }
            });
        }
    }



    private void loginUser(String email, String password) {


            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        mLoginProgress.dismiss();

                        String current_user_id = mAuth.getCurrentUser().getUid();
                        String deviceToken = FirebaseInstanceId.getInstance().getToken();

                        if (mAuth.getCurrentUser().isEmailVerified()) {


                            if (!TextUtils.isEmpty(getSharedPreferences("APP_PREF", MODE_PRIVATE).getString("name", null))) {

                                //   mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

                                String device_token = FirebaseInstanceId.getInstance().getToken();

                                HashMap<String, String> userMap = new HashMap<>();
                                userMap.put("name", getSharedPreferences("APP_PREF", MODE_PRIVATE).getString("name", null));
                                userMap.put("status", "Hi there I'm using TechnoJam Chat App.");
                                userMap.put("image", "default");
                                userMap.put("thumb_image", "default");
                                userMap.put("device_token", device_token);

                                Log.e("A", "onComplete: ");

                                mUserDatabase.child(mAuth.getCurrentUser().getUid()).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Log.e("B", "onComplete: ");
                                        if(task.isSuccessful()){

                                            Log.e("C", "onComplete: is Successfull");
                                            Log.e("RegisterActivity", "onComplete: Data added to db");
                                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(mainIntent);
                                            finish();
                                            /**  Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                             mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                             startActivity(mainIntent);
                                             finish(); */
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("D", "onFailure: " + e.getMessage());
                                    }
                                });

                            } else {

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }






                        } else {

                            Toast.makeText(LoginActivity.this, "Account not verified yet! Unable to Login!", Toast.LENGTH_SHORT).show();
                        }


                    } else {

                        mLoginProgress.hide();

                        String task_result = task.getException().getMessage().toString();

                        Toast.makeText(LoginActivity.this, "Error : " + task_result, Toast.LENGTH_LONG).show();

                    }

                }
            });







    }
}
