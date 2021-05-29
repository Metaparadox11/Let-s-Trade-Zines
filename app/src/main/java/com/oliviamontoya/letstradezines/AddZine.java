package com.oliviamontoya.letstradezines;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class AddZine extends AppCompatActivity {

    TextView addZineText;
    Button uploadZinePicButton;
    TextView nameTitle;
    EditText nameEnter;
    TextView numPagesTitle;
    EditText numPagesEnter;
    TextView zineSizeTitle;
    EditText zineSizeEnter;
    TextView detailsTitle;
    EditText detailsEnter;
    TextView tagsTitle;
    EditText tagsEnter;
    Button submitAddZineButton;
    long timeStamp;
    int number;
    Boolean uploaded = false;

    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_zine);

        addZineText = (TextView) findViewById(R.id.addZineText);
        uploadZinePicButton = (Button) findViewById(R.id.uploadZinePicButton);
        uploadZinePicButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        uploadZinePicButton.setTextColor(Color.WHITE);
        nameTitle = (TextView) findViewById(R.id.nameTitle);
        nameEnter = (EditText) findViewById(R.id.nameEnter);
        numPagesTitle = (TextView) findViewById(R.id.numPagesTitle);
        numPagesEnter = (EditText) findViewById(R.id.numPagesEnter);
        zineSizeTitle = (TextView) findViewById(R.id.zineSizeTitle);
        zineSizeEnter = (EditText) findViewById(R.id.zineSizeEnter);
        detailsTitle = (TextView) findViewById(R.id.detailsTitle);
        detailsEnter = (EditText) findViewById(R.id.detailsEnter);
        tagsTitle = (TextView) findViewById(R.id.tagsTitle);
        tagsEnter = (EditText) findViewById(R.id.tagsEnter);
        submitAddZineButton = (Button) findViewById(R.id.submitAddZineButton);
        submitAddZineButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        submitAddZineButton.setTextColor(Color.WHITE);

        timeStamp = new Date().getTime();

    }

    public void addZineSubmit(View view) {
        final String zn = nameEnter.getText().toString();
        final String np = numPagesEnter.getText().toString();
        final String zs = zineSizeEnter.getText().toString();
        final String d = detailsEnter.getText().toString();
        final String t = tagsEnter.getText().toString();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRef = database.getReference(user.getUid() + "/Zines");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Zine zine = new Zine(user.getUid(), zn, np, zs, d, t);
                zine.setTimeStamp(timeStamp);

                if (!uploaded) {
                    number = ((int) dataSnapshot.getChildrenCount()) + 1;
                }

                DatabaseReference ref2 = database.getReference(user.getUid() + "/Zines/zine" + String.valueOf(zine.getTimeStamp()));
                ref2.setValue(zine);

                finish();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void uploadZinePic(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                Bitmap bitmapScaled = getResizedBitmap(bitmap, 300);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                StorageReference ref = storageRef.child("images/zines/" + user.getUid() + "/" + String.valueOf(timeStamp) + "ZinePic.jpg");


                ref.putFile(getImageUri(AddZine.this, bitmapScaled))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference myRef = database.getReference(user.getUid() + "/Zines");

                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        number = ((int) dataSnapshot.getChildrenCount()) + 1;
                                        //DatabaseReference myRef = database.getReference(user.getUid() + "/Zines/zine" + String.valueOf(number) + "/picUrl");
                                        //myRef.setValue(downloadUrl.toString());
                                        uploaded = true;
                                        Toast.makeText(AddZine.this, "Success!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(AddZine.this, "Failed to upload.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                //Setting image to ImageView
                //imgView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Tharaka Nirmana on StackOverflow, edited by me
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        if ((image.getByteCount() / 1000) > maxSize) {
            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 1) {
                width = maxSize;
                height = (int) (width / bitmapRatio);
            } else {
                height = maxSize;
                width = (int) (height * bitmapRatio);
            }
            return Bitmap.createScaledBitmap(image, width, height, true);
        }

        return image;
    }

    // https://colinyeoh.wordpress.com/2012/05/18/android-getting-image-uri-from-bitmap/
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
