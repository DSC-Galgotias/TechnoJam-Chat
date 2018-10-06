package tech.honeysharma.techbmechat.Blog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import tech.honeysharma.techbmechat.R;
import tech.honeysharma.techbmechat.Utility.Utility;

public class PostActivity extends AppCompatActivity {

    private ImageButton mSelectImage;
    private EditText title,desc;
    private Button submit;
    private static final int GALLERY_REQUEST=1;

    //Firebase variables
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private StorageReference mStorage;
    private DatabaseReference mDatabase,mDatabaseUser;
    private ProgressDialog mProgress;
    private Uri imageUri=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();

        mStorage= FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseUser=FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        mSelectImage=(ImageButton)findViewById(R.id.imageButton);
        title=(EditText)findViewById(R.id.editText);
        desc=(EditText)findViewById(R.id.editText2);
        submit=(Button)findViewById(R.id.button);
        mProgress= new ProgressDialog(this);
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }

            private void startPosting() {

                mProgress.setMessage("Posting your feed");

                final String titleval=title.getText().toString().trim();
                final String descval=desc.getText().toString().trim();

                if(!TextUtils.isEmpty(titleval) && !TextUtils.isEmpty(descval) && imageUri!=null){

                    mProgress.show();
                    StorageReference filePath=mStorage.child("Blog_Images").child(imageUri.getLastPathSegment());
                    filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            final Task<Uri> downloadUrl=taskSnapshot.getStorage().getDownloadUrl();
                                   // taskSnapshot.getDownloadUrl();
                            final DatabaseReference newPost=mDatabase.push();

                            mDatabaseUser.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("HH:mm,dd-MM-yyyy");
                                    String formattedDate = df.format(c.getTime());

                                    newPost.child("title").setValue(titleval);
                                    newPost.child("desc").setValue(descval);
                                    newPost.child("uid").setValue(mCurrentUser.getUid());
                                    newPost.child("image").setValue(downloadUrl.toString());
                                    newPost.child("Date").setValue(formattedDate);

                                    newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                startActivity(new Intent(PostActivity.this,BlogActivity.class));
                                                finish();
                                            }else{
                                                Toast.makeText(PostActivity.this,"Error posting your feed",Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            mProgress.dismiss();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        checkConnectivity(this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALLERY_REQUEST && resultCode==RESULT_OK){
            imageUri=data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(5,3)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri= result.getUri();
                mSelectImage.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    public void checkConnectivity(Context context){
        if(!Utility.isOnline(this)){
            Snackbar snackbar=Snackbar.make(findViewById(R.id.postParent),"No internet connection",Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkConnectivity(PostActivity.this);
                        }
                    });
            snackbar.show();
        }
    }
}
