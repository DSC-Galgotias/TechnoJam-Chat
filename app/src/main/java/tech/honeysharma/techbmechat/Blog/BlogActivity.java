package tech.honeysharma.techbmechat.Blog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import tech.honeysharma.techbmechat.Account.AboutActivity;
import tech.honeysharma.techbmechat.Account.SettingsActivity;
import tech.honeysharma.techbmechat.Account.StartActivity;
import tech.honeysharma.techbmechat.Chat.UsersActivity;
import tech.honeysharma.techbmechat.R;
import tech.honeysharma.techbmechat.Utility.Utility;

public class BlogActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private DatabaseReference mUserRef;
    private FirebaseAuth auth;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CustomFragmentPagerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        auth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("TechnoJam Chat");

        tabLayout = findViewById(R.id.main_tabs);
        viewPager = findViewById(R.id.viewPager);

        mAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (auth.getCurrentUser()!=null)
        {
            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());
        }






    }

    @Override
    protected void onStart() {
        super.onStart();

    }



    @Override
    protected void onResume() {
        checkConnectivity(this);
        super.onResume();
    }

    @Override
    protected void onRestart() {
        checkConnectivity(this);
        super.onRestart();
    }

    @Override
    protected void onPostResume() {
        checkConnectivity(this);
        super.onPostResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.main_logout_btn){

            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);

            FirebaseAuth.getInstance().signOut();
            sendToStart();

        }

        if(item.getItemId() == R.id.main_settings_btn){

            Intent settingsIntent = new Intent(BlogActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);

        }

        if(item.getItemId() == R.id.main_all_btn){

            Intent settingsIntent = new Intent(BlogActivity.this, UsersActivity.class);
            startActivity(settingsIntent);

        }

        if (item.getItemId() == R.id.about_btn) {
            Intent settingsIntent = new Intent(BlogActivity.this, AboutActivity.class);
            startActivity(settingsIntent);
        }

        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return true;
    }

    private void sendToStart() {

        Intent startIntent = new Intent(BlogActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();

    }

    public void checkConnectivity(final Context context){
        if(!Utility.isOnline(context)){
            Snackbar snackbar=Snackbar.make(findViewById(R.id.drawer),"No internet connection",Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkConnectivity(context);
                        }
                    });
            snackbar.show();
        }
    }
}
