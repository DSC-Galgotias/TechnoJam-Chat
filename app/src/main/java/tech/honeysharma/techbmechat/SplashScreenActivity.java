package tech.honeysharma.techbmechat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import tech.honeysharma.techbmechat.Account.MainActivity;

/**
 * Created by Paris on 13/10/2018.
 */

public class SplashScreenActivity extends AppCompatActivity {

    ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mProgressBar = findViewById(R.id.progressBar);
        doAsyncWork();
    }

    private void doAsyncWork() {
        //TODO replace it with your async tasks
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openMainActivity();
            }
        }, 3000);
    }

    private void openMainActivity() {
        mProgressBar.setVisibility(View.GONE);
        final Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        Intent intent = new Intent(this, MainActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, bundle);
        } else {
            startActivity(intent);
        }
        finish();
    }
}
