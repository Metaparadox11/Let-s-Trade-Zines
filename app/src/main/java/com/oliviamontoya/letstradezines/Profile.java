package com.oliviamontoya.letstradezines;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static android.view.Gravity.CENTER_HORIZONTAL;
import static android.view.Gravity.TOP;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class Profile extends AppCompatActivity {

    ImageView profilePic;
    TextView username;
    TextView birthYear;
    TextView gender;
    TextView location;

    TextView completedTrades;
    TextView tradeRating;

    TextView profile;
    TextView zinesILike;
    TextView zinesIMake;

    TextView perzinesTV;
    TextView fanzinesTV;
    TextView artZinesTV;
    TextView compZinesTV;
    TextView chapbooksTV;
    TextView minicomicsTV;
    TextView queerZinesTV;
    TextView feministZinesTV;
    TextView politicalZinesTV;
    TextView musicZinesTV;
    TextView fictionZinesTV;
    TextView otherTV;
    TextView perzinesTV2;
    TextView fanzinesTV2;
    TextView artZinesTV2;
    TextView compZinesTV2;
    TextView chapbooksTV2;
    TextView minicomicsTV2;
    TextView queerZinesTV2;
    TextView feministZinesTV2;
    TextView politicalZinesTV2;
    TextView musicZinesTV2;
    TextView fictionZinesTV2;
    TextView otherTV2;

    String email;
    String birthYear2;
    String gender2;
    String location2;
    String profileInfo;
    Boolean perzines;
    Boolean fanzines;
    Boolean artZines;
    Boolean compZines;
    Boolean chapbooks;
    Boolean minicomics;
    Boolean queerZines;
    Boolean feministZines;
    Boolean politicalZines;
    Boolean musicZines;
    Boolean fictionZines;
    Boolean other;
    Boolean perzines2;
    Boolean fanzines2;
    Boolean artZines2;
    Boolean compZines2;
    Boolean chapbooks2;
    Boolean minicomics2;
    Boolean queerZines2;
    Boolean feministZines2;
    Boolean politicalZines2;
    Boolean musicZines2;
    Boolean fictionZines2;
    Boolean other2;
    Counter children;
    ArrayList<String> zinesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        children = new Counter();
        zinesList = new ArrayList<String>();
        profilePic = (ImageView) findViewById(R.id.profilePic);

        OnClickListener clickListener = new OnClickListener() {
            public void onClick(View v) {
                if (v.equals(profilePic)) {
                    Intent intent = new Intent(Profile.this, UploadProfilePic.class);
                    startActivity(intent);
                }
            }
        };
        profilePic.setOnClickListener(clickListener);

        username = (TextView) findViewById(R.id.username);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = "";
        if (user != null) {
            name = user.getDisplayName();
            username.setText(name);
        }

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = database.getReference(firebaseUser.getUid());
        //DatabaseReference child = databaseReference.child(username.getText().toString());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user.profilePic.isEmpty()) {
                    profilePic.setImageResource(R.drawable.logo);
                } else {

                    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                    try {
                        final File localFile = File.createTempFile("profilePic", "jpg");
                        StorageReference ref = mStorageRef.child("images/" + firebaseUser.getUid() + "ProfilePic.jpg");
                        ref.getFile(localFile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bMap = BitmapFactory.decodeFile(localFile.toString());
                                        profilePic.setImageBitmap(bMap);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle failed download
                                // ...
                            }
                        });
                    } catch(Exception e) {
                        Toast.makeText(Profile.this, "Error creating file.",
                                Toast.LENGTH_SHORT).show();
                    }

                }

                email = user.email;
                birthYear2 = user.birthYear;
                gender2 = user.gender;
                location2 = user.location;
                profileInfo = user.profileInfo;
                perzines = user.perzines;
                perzines2 = user.perzines2;
                fanzines = user.fanzines;
                fanzines2 = user.fanzines2;
                artZines = user.artZines;
                artZines2 = user.artZines2;
                compZines = user.compZines;
                compZines2 = user.compZines2;
                chapbooks = user.chapbooks;
                chapbooks2 = user.chapbooks2;
                minicomics = user.minicomics;
                minicomics2 = user.minicomics2;
                queerZines = user.queerZines;
                queerZines2 = user.queerZines2;
                feministZines = user.feministZines;
                feministZines2 = user.feministZines2;
                politicalZines = user.politicalZines;
                politicalZines2 = user.politicalZines2;
                musicZines = user.musicZines;
                musicZines2 = user.musicZines2;
                fictionZines = user.fictionZines;
                fictionZines2 = user.fictionZines2;
                other = user.other;
                other2 = user.other2;

                birthYear = (TextView) findViewById(R.id.birthYear);
                birthYear.setText("Birth Year: " + birthYear2);
                gender = (TextView) findViewById(R.id.gender);
                gender.setText("Gender: " + gender2);
                location = (TextView) findViewById(R.id.location);
                location.setText("Location: " + location2);
                
                completedTrades = (TextView) findViewById(R.id.completedTrades);

                final Counter counter = new Counter();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference ref = database.getReference(firebaseUser.getUid() + "/Trades");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            final Trade trade = data.getValue(Trade.class);

                            if (dataSnapshot.hasChildren()) {
                                if ((((trade.getTradeAccepter().equals(firebaseUser.getUid()) && trade.getTradeAccepterAccepted())
                                        || (trade.getTradeInitiator().equals(firebaseUser.getUid()) && trade.getTradeAccepterAccepted()))
                                        && ((trade.getTradeAccepterCompleted() && trade.getTradeInitiatorCompleted())))
                                        || (((trade.getTradeAccepter().equals(firebaseUser.getUid()) && trade.getTradeAccepterAccepted())
                                        || (trade.getTradeInitiator().equals(firebaseUser.getUid()) && trade.getTradeAccepterAccepted()))
                                        && ((trade.getTradeAccepterCompleted() ^ trade.getTradeInitiatorCompleted())
                                        && (trade.getTradeAccepterCancelled() || trade.getTradeInitiatorCancelled()) && trade.timePassed()))) {
                                    counter.increment();
                                }
                            }
                        }

                        completedTrades.setText("Completed Trades: " + counter.getCount());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                tradeRating = (TextView) findViewById(R.id.tradeRating);

                final ArrayList<String> array = new ArrayList<String>();
                final StringHolder str = new StringHolder();
                DatabaseReference ref2 = database.getReference(firebaseUser.getUid() + "/Ratings");
                ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            StringHolder rating = data.getValue(StringHolder.class);
                            array.add(rating.getString());
                        }
                        float avg = (float)0.0;
                        int count = 0;
                        for (String string : array) {
                            avg += Float.parseFloat(string);
                            count++;
                        }
                        if (count == 0) count++;
                        str.setString(String.valueOf(avg / count));
                        tradeRating.setText("Trade Rating: " + str.getString());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                profile = (TextView) findViewById(R.id.profile);
                profile.setText(profileInfo);

                zinesILike = (TextView) findViewById(R.id.zinesILike);

                perzinesTV = (TextView) findViewById(R.id.perzines);
                fanzinesTV = (TextView) findViewById(R.id.fanzines);
                artZinesTV = (TextView) findViewById(R.id.artZines);
                compZinesTV = (TextView) findViewById(R.id.compZines);
                chapbooksTV = (TextView) findViewById(R.id.chapbooks);
                minicomicsTV = (TextView) findViewById(R.id.minicomics);
                queerZinesTV = (TextView) findViewById(R.id.queerZines);
                feministZinesTV = (TextView) findViewById(R.id.feministZines);
                politicalZinesTV = (TextView) findViewById(R.id.politicalZines);
                musicZinesTV = (TextView) findViewById(R.id.musicZines);
                fictionZinesTV = (TextView) findViewById(R.id.fictionZines);
                otherTV = (TextView) findViewById(R.id.other);

                if (perzines) {perzinesTV.setText("- Perzines (Personal Zines)");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) perzinesTV.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    perzinesTV.setLayoutParams(params);
                }
                if (fanzines) {fanzinesTV.setText("- Fanzines");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) fanzinesTV.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    fanzinesTV.setLayoutParams(params);
                }
                if (artZines) {artZinesTV.setText("- Art Zines/Photo Zines");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) artZinesTV.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    artZinesTV.setLayoutParams(params);
                }
                if (compZines) {compZinesTV.setText("- Compilation Zines");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) compZinesTV.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    compZinesTV.setLayoutParams(params);
                }
                if (chapbooks) {chapbooksTV.setText("- Poetry Zines/Chapbooks");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) chapbooksTV.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    chapbooksTV.setLayoutParams(params);
                }
                if (minicomics) {minicomicsTV.setText("- Minicomics");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) minicomicsTV.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    minicomicsTV.setLayoutParams(params);
                }
                if (queerZines) {queerZinesTV.setText("- Queer/LGBT+ Zines");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) queerZinesTV.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    queerZinesTV.setLayoutParams(params);
                }
                if (feministZines) {feministZinesTV.setText("- Feminist Zines");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) feministZinesTV.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    feministZinesTV.setLayoutParams(params);
                }
                if (politicalZines) {politicalZinesTV.setText("- Political Zines");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) politicalZinesTV.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    politicalZinesTV.setLayoutParams(params);
                }
                if (musicZines) {musicZinesTV.setText("- Music Zines");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) musicZinesTV.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    musicZinesTV.setLayoutParams(params);
                }
                if (fictionZines) {fictionZinesTV.setText("- Fiction Zines");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) fictionZinesTV.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    fictionZinesTV.setLayoutParams(params);
                }
                if (other) {otherTV.setText("- Other");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) otherTV.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    otherTV.setLayoutParams(params);
                }

                zinesIMake = (TextView) findViewById(R.id.zinesIMake);

                perzinesTV2 = (TextView) findViewById(R.id.perzines2);
                fanzinesTV2 = (TextView) findViewById(R.id.fanzines2);
                artZinesTV2 = (TextView) findViewById(R.id.artZines2);
                compZinesTV2 = (TextView) findViewById(R.id.compZines2);
                chapbooksTV2 = (TextView) findViewById(R.id.chapbooks2);
                minicomicsTV2 = (TextView) findViewById(R.id.minicomics2);
                queerZinesTV2 = (TextView) findViewById(R.id.queerZines2);
                feministZinesTV2 = (TextView) findViewById(R.id.feministZines2);
                politicalZinesTV2 = (TextView) findViewById(R.id.politicalZines2);
                musicZinesTV2 = (TextView) findViewById(R.id.musicZines2);
                fictionZinesTV2 = (TextView) findViewById(R.id.fictionZines2);
                otherTV2 = (TextView) findViewById(R.id.other2);

                if (perzines2) {perzinesTV2.setText("- Perzines (Personal Zines)");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) perzinesTV2.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    perzinesTV2.setLayoutParams(params);
                }
                if (fanzines2) {fanzinesTV2.setText("- Fanzines");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) fanzinesTV2.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    fanzinesTV2.setLayoutParams(params);
                }
                if (artZines2) {artZinesTV2.setText("- Art Zines/Photo Zines");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) artZinesTV2.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    artZinesTV2.setLayoutParams(params);
                }
                if (compZines2) {compZinesTV2.setText("- Compilation Zines");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) compZinesTV2.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    compZinesTV2.setLayoutParams(params);
                }
                if (chapbooks2) {chapbooksTV2.setText("- Poetry Zines/Chapbooks");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) chapbooksTV2.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    chapbooksTV2.setLayoutParams(params);
                }
                if (minicomics2) {minicomicsTV2.setText("- Minicomics");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) minicomicsTV2.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    minicomicsTV2.setLayoutParams(params);
                }
                if (queerZines2) {queerZinesTV2.setText("- Queer/LGBT+ Zines");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) queerZinesTV2.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    queerZinesTV2.setLayoutParams(params);
                }
                if (feministZines2) {feministZinesTV2.setText("- Feminist Zines");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) feministZinesTV2.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    feministZinesTV2.setLayoutParams(params);
                }
                if (politicalZines2) {politicalZinesTV2.setText("- Political Zines");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) politicalZinesTV2.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    politicalZinesTV2.setLayoutParams(params);
                }
                if (musicZines2) {musicZinesTV2.setText("- Music Zines");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) musicZinesTV2.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    musicZinesTV2.setLayoutParams(params);
                }
                if (fictionZines2) {fictionZinesTV2.setText("- Fiction Zines");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) fictionZinesTV2.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    fictionZinesTV2.setLayoutParams(params);
                }
                if (other2) {otherTV2.setText("- Other");}
                else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) otherTV2.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.empty);
                    otherTV2.setLayoutParams(params);
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference2 = database.getReference(firebaseUser.getUid() + "/Zines");
        final TextView numZines = new TextView(Profile.this);
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Button addZineButton = (Button) findViewById(R.id.addZineButton);
                addZineButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                addZineButton.setTextColor(Color.WHITE);

                TextView numZines = (TextView) findViewById(R.id.numZines);
                numZines.setTextSize(20);
                numZines.setTypeface(Typeface.DEFAULT_BOLD);
                numZines.setGravity(CENTER_HORIZONTAL);
                numZines.setText("ZINES (" + dataSnapshot.getChildrenCount() + ")");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final LinearLayout gl = (LinearLayout) findViewById(R.id.zineGrid);
        DatabaseReference zines = database.getReference(firebaseUser.getUid() + "/Zines");

        zines.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                //for (DataSnapshot data : dataSnapshot.getChildren()) {
                //    dataSnapshots.add(data);
                //}
                //Collections.reverse(dataSnapshots);

                final Counter counter = new Counter();
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    if (data.getChildrenCount() > 1) {
                        if (counter.getCount() == children.getCount() || (counter.getCount() + 1) == (int) dataSnapshot.getChildrenCount()) {

                            final Zine zine = data.getValue(Zine.class);

                            final LinearLayout ll2 = new LinearLayout(Profile.this);
                            ll2.setOrientation(HORIZONTAL);
                            ll2.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding));
                            ll2.setMinimumHeight(WRAP_CONTENT);
                            ll2.setMinimumWidth(WRAP_CONTENT);
                            ll2.setGravity(TOP);
                            gl.addView(ll2);

                            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("images/zines/" + firebaseUser.getUid() + "/" + String.valueOf(zine.getTimeStamp()) + "ZinePic.jpg");
                            try {
                                final File localFile = File.createTempFile("zinePic", "jpg");
                                mStorageRef.getFile(localFile)
                                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                Bitmap bMap = BitmapFactory.decodeFile(localFile.toString());
                                                ImageView zinePic = new ImageView(Profile.this);
                                                zinePic.setImageBitmap(bMap);
                                                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.zine_image), getResources().getDimensionPixelSize(R.dimen.zine_image));
                                                zinePic.setLayoutParams(imageParams);
                                                ll2.addView(zinePic);

                                                final LinearLayout ll3 = new LinearLayout(Profile.this);
                                                ll3.setOrientation(VERTICAL);
                                                ll3.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding));
                                                ll3.setMinimumHeight(WRAP_CONTENT);
                                                ll3.setMinimumWidth(WRAP_CONTENT);
                                                ll3.setGravity(TOP);
                                                ll2.addView(ll3);

                                                TextView zn = new TextView(Profile.this);
                                                zn.setText(zine.getZineName());
                                                zn.setTextSize(20);
                                                zn.setTypeface(Typeface.DEFAULT_BOLD);
                                                ll3.addView(zn);

                                                TextView np = new TextView(Profile.this);
                                                np.setText("Number of Pages: " + zine.getNumPages());
                                                np.setTextSize(18);
                                                ll3.addView(np);

                                                TextView zs = new TextView(Profile.this);
                                                zs.setText("Zine Size: " + zine.getZineSize());
                                                zs.setTextSize(18);
                                                ll3.addView(zs);

                                                TextView zd = new TextView(Profile.this);
                                                zd.setText("Description: " + zine.getDescriptionText());
                                                zd.setTextSize(18);
                                                ll3.addView(zd);

                                                children.increment();
                                                counter.increment();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                ImageView zinePic = new ImageView(Profile.this);
                                                zinePic.setImageResource(R.drawable.logo);
                                                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.zine_image), getResources().getDimensionPixelSize(R.dimen.zine_image));
                                                zinePic.setLayoutParams(imageParams);
                                                ll2.addView(zinePic);

                                                final LinearLayout ll3 = new LinearLayout(Profile.this);
                                                ll3.setOrientation(VERTICAL);
                                                ll3.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding));
                                                ll3.setMinimumHeight(WRAP_CONTENT);
                                                ll3.setMinimumWidth(WRAP_CONTENT);
                                                ll3.setGravity(TOP);
                                                ll2.addView(ll3);

                                                TextView zn = new TextView(Profile.this);
                                                zn.setText(zine.getZineName());
                                                zn.setTextSize(20);
                                                zn.setTypeface(Typeface.DEFAULT_BOLD);
                                                ll3.addView(zn);

                                                TextView np = new TextView(Profile.this);
                                                np.setText("Number of Pages: " + zine.getNumPages());
                                                np.setTextSize(18);
                                                ll3.addView(np);

                                                TextView zs = new TextView(Profile.this);
                                                zs.setText("Zine Size: " + zine.getZineSize());
                                                zs.setTextSize(18);
                                                ll3.addView(zs);

                                                TextView zd = new TextView(Profile.this);
                                                zd.setText("Description: " + zine.getDescriptionText());
                                                zd.setTextSize(18);
                                                ll3.addView(zd);

                                                children.increment();
                                                counter.increment();
                                            }
                                });
                            } catch (Exception e) {
                                Toast.makeText(Profile.this, "Error creating file.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            counter.increment();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void addZine(View view) {
        Intent intent = new Intent(this, AddZine.class);
        startActivity(intent);
    }
}
