package com.oliviamontoya.letstradezines;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import static android.view.Gravity.TOP;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class RequestTrade extends AppCompatActivity {

    Counter children1;
    Counter children2;
    ArrayList<CheckBox> idList;
    ArrayList<CheckBox> idList2;
    ArrayList<String> zineList;
    ArrayList<String> zineList2;
    TextView zinesYoudLike;
    TextView zinesYoureOffering;
    String zinesYoudLikeString;
    String zinesYoureOfferingString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_trade);

        children1 = new Counter();
        children2 = new Counter();
        idList = new ArrayList<CheckBox>();
        idList2 = new ArrayList<CheckBox>();
        zineList = new ArrayList<String>();
        zineList2 = new ArrayList<String>();
        zinesYoudLikeString = "";
        zinesYoureOfferingString = "";

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = database.getReference(firebaseUser.getUid() + "/Zines");

        zinesYoureOffering = (TextView) findViewById(R.id.zinesYoureOffering);

        // logged in user's zines

        final LinearLayout gl = (LinearLayout) findViewById(R.id.zineGrid3);
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
                        if (counter.getCount() == children1.getCount() || (counter.getCount() + 1) == (int) dataSnapshot.getChildrenCount()) {

                            final Zine zine = data.getValue(Zine.class);

                            final LinearLayout ll2 = new LinearLayout(RequestTrade.this);
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
                                                final CheckBox box = new CheckBox(RequestTrade.this);
                                                ll2.addView(box);
                                                idList.add(box);
                                                zineList.add(zine.getZineName());

                                                Bitmap bMap = BitmapFactory.decodeFile(localFile.toString());
                                                ImageView zinePic = new ImageView(RequestTrade.this);
                                                zinePic.setImageBitmap(bMap);
                                                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.zine_image), getResources().getDimensionPixelSize(R.dimen.zine_image));
                                                zinePic.setLayoutParams(imageParams);
                                                ll2.addView(zinePic);

                                                final LinearLayout ll3 = new LinearLayout(RequestTrade.this);
                                                ll3.setOrientation(VERTICAL);
                                                ll3.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding));
                                                ll3.setMinimumHeight(WRAP_CONTENT);
                                                ll3.setMinimumWidth(WRAP_CONTENT);
                                                ll3.setGravity(TOP);
                                                ll2.addView(ll3);

                                                TextView zn = new TextView(RequestTrade.this);
                                                zn.setText(zine.getZineName());
                                                zn.setTextSize(20);
                                                zn.setTypeface(Typeface.DEFAULT_BOLD);
                                                ll3.addView(zn);

                                                TextView np = new TextView(RequestTrade.this);
                                                np.setText("Number of Pages: " + zine.getNumPages());
                                                np.setTextSize(18);
                                                ll3.addView(np);

                                                TextView zs = new TextView(RequestTrade.this);
                                                zs.setText("Zine Size: " + zine.getZineSize());
                                                zs.setTextSize(18);
                                                ll3.addView(zs);

                                                TextView zd = new TextView(RequestTrade.this);
                                                zd.setText("Description: " + zine.getDescriptionText());
                                                zd.setTextSize(18);
                                                ll3.addView(zd);

                                                children1.increment();
                                                counter.increment();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                            final CheckBox box = new CheckBox(RequestTrade.this);
                                            ll2.addView(box);
                                            idList.add(box);
                                            zineList.add(zine.getZineName());

                                            ImageView zinePic = new ImageView(RequestTrade.this);
                                            zinePic.setImageResource(R.drawable.logo);
                                            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.zine_image), getResources().getDimensionPixelSize(R.dimen.zine_image));
                                            zinePic.setLayoutParams(imageParams);
                                            ll2.addView(zinePic);

                                            final LinearLayout ll3 = new LinearLayout(RequestTrade.this);
                                            ll3.setOrientation(VERTICAL);
                                            ll3.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding));
                                            ll3.setMinimumHeight(WRAP_CONTENT);
                                            ll3.setMinimumWidth(WRAP_CONTENT);
                                            ll3.setGravity(TOP);
                                            ll2.addView(ll3);

                                            TextView zn = new TextView(RequestTrade.this);
                                            zn.setText(zine.getZineName());
                                            zn.setTextSize(20);
                                            zn.setTypeface(Typeface.DEFAULT_BOLD);
                                            ll3.addView(zn);

                                            TextView np = new TextView(RequestTrade.this);
                                            np.setText("Number of Pages: " + zine.getNumPages());
                                            np.setTextSize(18);
                                            ll3.addView(np);

                                            TextView zs = new TextView(RequestTrade.this);
                                            zs.setText("Zine Size: " + zine.getZineSize());
                                            zs.setTextSize(18);
                                            ll3.addView(zs);

                                            TextView zd = new TextView(RequestTrade.this);
                                            zd.setText("Description: " + zine.getDescriptionText());
                                            zd.setTextSize(18);
                                            ll3.addView(zd);

                                            children1.increment();
                                            counter.increment();
                                    }
                                });
                            } catch (Exception e) {
                                Toast.makeText(RequestTrade.this, "Error creating file.",
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

        zinesYoudLike = (TextView) findViewById(R.id.zinesYoudLike);

        // other user's zines

        final LinearLayout gl2 = (LinearLayout) findViewById(R.id.zineGrid4);
        DatabaseReference zines2 = database.getReference(getIntent().getStringExtra("to") + "/Zines");

        zines2.addValueEventListener(new ValueEventListener() {
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
                        if (counter.getCount() == children2.getCount() || (counter.getCount() + 1) == (int) dataSnapshot.getChildrenCount()) {

                            final Zine zine = data.getValue(Zine.class);

                            final LinearLayout ll2 = new LinearLayout(RequestTrade.this);
                            ll2.setOrientation(HORIZONTAL);
                            ll2.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding));
                            ll2.setMinimumHeight(WRAP_CONTENT);
                            ll2.setMinimumWidth(WRAP_CONTENT);
                            ll2.setGravity(TOP);
                            gl2.addView(ll2);

                            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("images/zines/" + getIntent().getStringExtra("to") + "/" + String.valueOf(zine.getTimeStamp()) + "ZinePic.jpg");
                            try {
                                final File localFile = File.createTempFile("zinePic", "jpg");
                                mStorageRef.getFile(localFile)
                                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                final CheckBox box = new CheckBox(RequestTrade.this);
                                                ll2.addView(box);
                                                idList2.add(box);
                                                zineList2.add(zine.getZineName());

                                                Bitmap bMap = BitmapFactory.decodeFile(localFile.toString());
                                                ImageView zinePic = new ImageView(RequestTrade.this);
                                                zinePic.setImageBitmap(bMap);
                                                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.zine_image), getResources().getDimensionPixelSize(R.dimen.zine_image));
                                                zinePic.setLayoutParams(imageParams);
                                                ll2.addView(zinePic);

                                                final LinearLayout ll3 = new LinearLayout(RequestTrade.this);
                                                ll3.setOrientation(VERTICAL);
                                                ll3.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding));
                                                ll3.setMinimumHeight(WRAP_CONTENT);
                                                ll3.setMinimumWidth(WRAP_CONTENT);
                                                ll3.setGravity(TOP);
                                                ll2.addView(ll3);

                                                TextView zn = new TextView(RequestTrade.this);
                                                zn.setText(zine.getZineName());
                                                zn.setTextSize(20);
                                                zn.setTypeface(Typeface.DEFAULT_BOLD);
                                                ll3.addView(zn);

                                                TextView np = new TextView(RequestTrade.this);
                                                np.setText("Number of Pages: " + zine.getNumPages());
                                                np.setTextSize(18);
                                                ll3.addView(np);

                                                TextView zs = new TextView(RequestTrade.this);
                                                zs.setText("Zine Size: " + zine.getZineSize());
                                                zs.setTextSize(18);
                                                ll3.addView(zs);

                                                TextView zd = new TextView(RequestTrade.this);
                                                zd.setText("Description: " + zine.getDescriptionText());
                                                zd.setTextSize(18);
                                                ll3.addView(zd);

                                                children2.increment();
                                                counter.increment();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                            final CheckBox box = new CheckBox(RequestTrade.this);
                                            ll2.addView(box);
                                            idList2.add(box);
                                            zineList2.add(zine.getZineName());

                                            ImageView zinePic = new ImageView(RequestTrade.this);
                                            zinePic.setImageResource(R.drawable.logo);
                                            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.zine_image), getResources().getDimensionPixelSize(R.dimen.zine_image));
                                            zinePic.setLayoutParams(imageParams);
                                            ll2.addView(zinePic);

                                            final LinearLayout ll3 = new LinearLayout(RequestTrade.this);
                                            ll3.setOrientation(VERTICAL);
                                            ll3.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding));
                                            ll3.setMinimumHeight(WRAP_CONTENT);
                                            ll3.setMinimumWidth(WRAP_CONTENT);
                                            ll3.setGravity(TOP);
                                            ll2.addView(ll3);

                                            TextView zn = new TextView(RequestTrade.this);
                                            zn.setText(zine.getZineName());
                                            zn.setTextSize(20);
                                            zn.setTypeface(Typeface.DEFAULT_BOLD);
                                            ll3.addView(zn);

                                            TextView np = new TextView(RequestTrade.this);
                                            np.setText("Number of Pages: " + zine.getNumPages());
                                            np.setTextSize(18);
                                            ll3.addView(np);

                                            TextView zs = new TextView(RequestTrade.this);
                                            zs.setText("Zine Size: " + zine.getZineSize());
                                            zs.setTextSize(18);
                                            ll3.addView(zs);

                                            TextView zd = new TextView(RequestTrade.this);
                                            zd.setText("Description: " + zine.getDescriptionText());
                                            zd.setTextSize(18);
                                            ll3.addView(zd);

                                            children2.increment();
                                            counter.increment();
                                    }
                                });
                            } catch (Exception e) {
                                Toast.makeText(RequestTrade.this, "Error creating file.",
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

        Button button = (Button) findViewById(R.id.submitTradeRequestButton);
        button.setTextColor(Color.WHITE);
        button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    public void submitTradeRequest(View view) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final long ts = new Date().getTime();

        for (int i = 0; i < zineList.size(); i++) {
            CheckBox box = idList.get(i);
            if (box.isChecked()) {
                if (zinesYoureOfferingString.equals("")) {
                    zinesYoureOfferingString = zineList.get(i);
                } else {
                    zinesYoureOfferingString = zinesYoureOfferingString + "," + zineList.get(i);
                }
            }
        }

        for (int i = 0; i < zineList2.size(); i++) {
            CheckBox box = idList2.get(i);
            if (box.isChecked()) {
                if (zinesYoudLikeString.equals("")) {
                    zinesYoudLikeString = zineList2.get(i);
                } else {
                    zinesYoudLikeString = zinesYoudLikeString + "," + zineList2.get(i);
                }
            }
        }

        if (zinesYoureOfferingString.equals("") || zinesYoudLikeString.equals("")) {
            Toast.makeText(RequestTrade.this, "You need at least one zine on each side.",
                    Toast.LENGTH_LONG).show();
        } else {
            Trade trade = new Trade(user.getUid(), getIntent().getStringExtra("to"), zinesYoureOfferingString, zinesYoudLikeString);
            trade.setTimeStamp(ts);

            DatabaseReference userRef = database.getReference(user.getUid() + "/Trades/trade" + String.valueOf(trade.getTimeStamp()));
            DatabaseReference tradeUserRef = database.getReference(getIntent().getStringExtra("to") + "/Trades/trade" + String.valueOf(trade.getTimeStamp()));

            userRef.setValue(trade);
            tradeUserRef.setValue(trade);

            finish();
        }
    }

}
