package com.oliviamontoya.letstradezines;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Map;
import java.util.TreeMap;

import static android.view.Gravity.TOP;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class Search extends AppCompatActivity {

    LinearLayout layout;
    EditText etsearch;
    Spinner searchSpinner;
    Spinner searchSpinner2;
    Spinner searchSpinner3;
    Button submitSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etsearch = (EditText) findViewById(R.id.etsearch);
        searchSpinner = (Spinner) findViewById(R.id.searchSpinner);
        searchSpinner2 = (Spinner) findViewById(R.id.searchSpinner2);
        searchSpinner3 = (Spinner) findViewById(R.id.searchSpinner3);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) searchSpinner2.getLayoutParams();
        params.width = getResources().getDimensionPixelSize(R.dimen.empty);
        searchSpinner2.setLayoutParams(params);

        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) searchSpinner3.getLayoutParams();
        params2.width = getResources().getDimensionPixelSize(R.dimen.empty);
        searchSpinner3.setLayoutParams(params2);

        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position,long id) {
                String item = searchSpinner.getSelectedItem().toString();
                String[] array = getResources().getStringArray(R.array.search_options);
                if (item.equals(array[0])) {
                    LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) searchSpinner3.getLayoutParams();
                    params3.width = getResources().getDimensionPixelSize(R.dimen.empty);
                    searchSpinner3.setLayoutParams(params3);

                    LinearLayout.LayoutParams params4 = (LinearLayout.LayoutParams) searchSpinner2.getLayoutParams();
                    params4.width = WRAP_CONTENT;
                    searchSpinner2.setLayoutParams(params4);
                } else if (item.equals(array[1])) {
                    LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) searchSpinner2.getLayoutParams();
                    params3.width = getResources().getDimensionPixelSize(R.dimen.empty);
                    searchSpinner2.setLayoutParams(params3);

                    LinearLayout.LayoutParams params4 = (LinearLayout.LayoutParams) searchSpinner3.getLayoutParams();
                    params4.width = WRAP_CONTENT;
                    searchSpinner3.setLayoutParams(params4);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        submitSearch = (Button) findViewById(R.id.submitSearch);
        submitSearch.setTextColor(Color.WHITE);
        submitSearch.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        //final ImageView profilePic = new ImageView(Search.this);
        //final TextView text = new TextView(Search.this);
        layout = (LinearLayout) findViewById(R.id.layout);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        databaseReference.orderByChild("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    dataSnapshots.add(data);
                }
                //Collections.reverse(dataSnapshots);

                final int numChildren = (int) dataSnapshot.getChildrenCount();
                final Counter counter = new Counter();
                for (final DataSnapshot snapshot : dataSnapshots) {

                    final User user = snapshot.getValue(User.class);

                    final ImageView profilePic = new ImageView(Search.this);
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.search_image), getResources().getDimensionPixelSize(R.dimen.search_image));
                    profilePic.setLayoutParams(imageParams);

                    if (user.profilePic.isEmpty()) {

                        final LinearLayout layout2 = new LinearLayout(Search.this);
                        layout2.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty));
                        layout2.setOrientation(HORIZONTAL);
                        layout2.setMinimumWidth(MATCH_PARENT);
                        layout.addView(layout2);
                        profilePic.setImageResource(R.drawable.logo);
                        layout2.addView(profilePic);

                        View.OnClickListener clickListener = new View.OnClickListener() {
                            public void onClick(View v) {
                                if (v.equals(layout2)) {
                                    Intent intent = new Intent(Search.this, ViewProfile.class);
                                    intent.putExtra("uid", snapshot.getKey());
                                    startActivity(intent);
                                }
                            }
                        };
                        layout2.setOnClickListener(clickListener);

                        final LinearLayout layout3 = new LinearLayout(Search.this);
                        layout3.setOrientation(VERTICAL);
                        layout3.setMinimumHeight(WRAP_CONTENT);
                        layout3.setMinimumWidth(WRAP_CONTENT);
                        layout2.addView(layout3);

                        TextView un = new TextView(Search.this);
                        un.setText(user.username);
                        un.setTextSize(20);
                        un.setTypeface(Typeface.DEFAULT_BOLD);
                        un.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);
                        layout3.addView(un);

                        TextView birthYear = new TextView(Search.this);
                        birthYear.setText("Birth Year: " + user.birthYear);
                        birthYear.setTextSize(18);
                        birthYear.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);
                        layout3.addView(birthYear);

                        TextView gender = new TextView(Search.this);
                        gender.setText("Gender: " + user.gender);
                        gender.setTextSize(18);
                        gender.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);
                        layout3.addView(gender);

                        TextView location = new TextView(Search.this);
                        location.setText("Location: " + user.location);
                        location.setTextSize(18);
                        location.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);
                        layout3.addView(location);

                        final TextView completedTrades = new TextView(Search.this);
                        completedTrades.setTextSize(18);
                        completedTrades.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);

                        final Counter counter2 = new Counter();
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference(snapshot.getKey() + "/Trades");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    final Trade trade = data.getValue(Trade.class);

                                    if (dataSnapshot.hasChildren()) {
                                        if ((trade.getTradeAccepterAccepted() && ((trade.getTradeAccepterCompleted() && trade.getTradeInitiatorCompleted())))
                                                || (((trade.getTradeAccepter().equals(snapshot.getKey()) && trade.getTradeAccepterAccepted())
                                                || (trade.getTradeInitiator().equals(snapshot.getKey()) && trade.getTradeAccepterAccepted()))
                                                && ((trade.getTradeAccepterCompleted() ^ trade.getTradeInitiatorCompleted())
                                                && (trade.getTradeAccepterCancelled() || trade.getTradeInitiatorCancelled()) && trade.timePassed()))) {
                                            counter2.increment();
                                        }
                                    }
                                }

                                completedTrades.setText("Completed Trades: " + counter2.getCount());
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        layout3.addView(completedTrades);

                        final TextView tradeRating = new TextView(Search.this);
                        tradeRating.setTextSize(18);
                        tradeRating.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);

                        final ArrayList<String> array = new ArrayList<String>();
                        final StringHolder str = new StringHolder();
                        DatabaseReference ref2 = database.getReference(snapshot.getKey() + "/Ratings");
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
                        layout3.addView(tradeRating);

                        counter.increment();
                        if (counter.getCount() == numChildren) {
                            TextView padding = new TextView(Search.this);
                            padding.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                            layout.addView(padding);
                        }
                    } else {

                        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

                        final LinearLayout layout2 = new LinearLayout(Search.this);
                        layout2.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty));
                        layout2.setOrientation(HORIZONTAL);
                        layout2.setMinimumWidth(MATCH_PARENT);
                        layout.addView(layout2);

                        View.OnClickListener clickListener = new View.OnClickListener() {
                            public void onClick(View v) {
                                if (v.equals(layout2)) {
                                    Intent intent = new Intent(Search.this, ViewProfile.class);
                                    intent.putExtra("uid", snapshot.getKey());
                                    startActivity(intent);
                                }
                            }
                        };
                        layout2.setOnClickListener(clickListener);

                        try {

                            //TextView ss = new TextView(Search.this);
                            //LinearLayout.LayoutParams ssParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                            //ss.setLayoutParams(ssParams);
                            //ss.setText(snapshot.getKey());
                            //layout.addView(ss);

                            final File localFile = File.createTempFile("profilePic", "jpg");
                            StorageReference ref = mStorageRef.child("images/" + snapshot.getKey() + "ProfilePic.jpg");
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
                                            Toast.makeText(Search.this, "Error downloading image.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                            });

                        } catch (Exception e) {
                            Toast.makeText(Search.this, "Error creating file.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        layout2.addView(profilePic);

                        final LinearLayout layout3 = new LinearLayout(Search.this);
                        layout3.setOrientation(VERTICAL);
                        layout3.setMinimumHeight(WRAP_CONTENT);
                        layout3.setMinimumWidth(WRAP_CONTENT);
                        layout2.addView(layout3);

                        TextView un = new TextView(Search.this);
                        un.setText(user.username);
                        un.setTextSize(20);
                        un.setTypeface(Typeface.DEFAULT_BOLD);
                        un.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);
                        layout3.addView(un);

                        TextView birthYear = new TextView(Search.this);
                        birthYear.setText("Birth Year: " + user.birthYear);
                        birthYear.setTextSize(18);
                        birthYear.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);
                        layout3.addView(birthYear);

                        TextView gender = new TextView(Search.this);
                        gender.setText("Gender: " + user.gender);
                        gender.setTextSize(18);
                        gender.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);
                        layout3.addView(gender);

                        TextView location = new TextView(Search.this);
                        location.setText("Location: " + user.location);
                        location.setTextSize(18);
                        location.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);
                        layout3.addView(location);

                        final TextView completedTrades = new TextView(Search.this);
                        completedTrades.setTextSize(18);
                        completedTrades.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);

                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference(snapshot.getKey() + "/Trades");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final Counter counter2 = new Counter();
                                if (dataSnapshot.hasChildren()) {
                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        final Trade trade = data.getValue(Trade.class);
                                        if ((trade.getTradeAccepterAccepted() && ((trade.getTradeAccepterCompleted() && trade.getTradeInitiatorCompleted())))
                                                || (((trade.getTradeAccepter().equals(snapshot.getKey()) && trade.getTradeAccepterAccepted())
                                                || (trade.getTradeInitiator().equals(snapshot.getKey()) && trade.getTradeAccepterAccepted()))
                                                && ((trade.getTradeAccepterCompleted() ^ trade.getTradeInitiatorCompleted())
                                                && (trade.getTradeAccepterCancelled() || trade.getTradeInitiatorCancelled()) && trade.timePassed()))) {
                                            counter2.increment();
                                        }
                                    }
                                }

                                completedTrades.setText("Completed Trades: " + counter2.getCount());
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        layout3.addView(completedTrades);

                        final TextView tradeRating = new TextView(Search.this);
                        tradeRating.setTextSize(18);
                        tradeRating.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);

                        final ArrayList<String> array = new ArrayList<String>();
                        final StringHolder str = new StringHolder();
                        DatabaseReference ref2 = database.getReference(snapshot.getKey() + "/Ratings");
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
                        layout3.addView(tradeRating);

                        counter.increment();
                        if (counter.getCount() == numChildren) {
                            TextView padding = new TextView(Search.this);
                            padding.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                            layout.addView(padding);
                        }

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void onSearch(View view) {
        layout.removeAllViewsInLayout();
        final String search = etsearch.getText().toString();
        String first = searchSpinner.getSelectedItem().toString();
        String[] array = getResources().getStringArray(R.array.search_options);
        if (first.equals(array[0])) {
            // profile search
            String second = searchSpinner2.getSelectedItem().toString();
            String[] array2 = getResources().getStringArray(R.array.search_options2);
            if (second.equals(array2[0])) {
                // username search
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference();
                databaseReference.orderByChild("username").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (search.isEmpty()) {
                                dataSnapshots.add(data);
                            } else {
                                User user = data.getValue(User.class);
                                if (user.username.toLowerCase().contains(search.toLowerCase())) {
                                    dataSnapshots.add(data);
                                }
                            }
                        }

                        final int numChildren = (int) dataSnapshots.size();
                        for (final DataSnapshot snapshot : dataSnapshots) {
                            profileSearch(snapshot, numChildren);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else if (second.equals(array2[1])) {
                // birth year search
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference();
                databaseReference.orderByChild("birthYear").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (search.isEmpty()) {
                                dataSnapshots.add(data);
                            } else {
                                User user = data.getValue(User.class);
                                if (user.birthYear.toLowerCase().contains(search.toLowerCase())) {
                                    dataSnapshots.add(data);
                                }
                            }
                        }

                        final int numChildren = (int) dataSnapshots.size();
                        for (final DataSnapshot snapshot : dataSnapshots) {
                            profileSearch(snapshot, numChildren);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else if (second.equals(array2[2])) {
                // gender search
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference();
                databaseReference.orderByChild("gender").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (search.isEmpty()) {
                                dataSnapshots.add(data);
                            } else {
                                User user = data.getValue(User.class);
                                if (user.gender.toLowerCase().contains(search.toLowerCase())) {
                                    dataSnapshots.add(data);
                                }
                            }
                        }

                        final int numChildren = (int) dataSnapshots.size();
                        for (final DataSnapshot snapshot : dataSnapshots) {
                            profileSearch(snapshot, numChildren);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else if (second.equals(array2[3])) {
                // location search
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference();
                databaseReference.orderByChild("location").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (search.isEmpty()) {
                                dataSnapshots.add(data);
                            } else {
                                User user = data.getValue(User.class);
                                if (user.location.toLowerCase().contains(search.toLowerCase())) {
                                    dataSnapshots.add(data);
                                }
                            }
                        }

                        final int numChildren = (int) dataSnapshots.size();
                        for (final DataSnapshot snapshot : dataSnapshots) {
                            profileSearch(snapshot, numChildren);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else if (second.equals(array2[4])) {
                // profile details search
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference();
                databaseReference.orderByChild("profileInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (search.isEmpty()) {
                                dataSnapshots.add(data);
                            } else {
                                User user = data.getValue(User.class);
                                if (user.profileInfo.toLowerCase().contains(search.toLowerCase())) {
                                    dataSnapshots.add(data);
                                }
                            }
                        }

                        final int numChildren = (int) dataSnapshots.size();
                        for (final DataSnapshot snapshot : dataSnapshots) {
                            profileSearch(snapshot, numChildren);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else if (second.equals(array2[5])) {
                // zines liked search
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference();
                databaseReference.orderByChild("username").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (search.isEmpty()) {
                                dataSnapshots.add(data);
                            } else {
                                User user = data.getValue(User.class);
                                if (search.toLowerCase().contains("perzine") || search.toLowerCase().contains("personal")) {
                                    if (!dataSnapshots.contains(data) && user.perzines) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("fan")) {
                                    if (!dataSnapshots.contains(data) && user.fanzines) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("art") || search.toLowerCase().contains("photo")) {
                                    if (!dataSnapshots.contains(data) && user.artZines) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("comp")) {
                                    if (!dataSnapshots.contains(data) && user.compZines) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("poetry") || search.toLowerCase().contains("chap")) {
                                    if (!dataSnapshots.contains(data) && user.chapbooks) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("comic") || search.toLowerCase().contains("cartoon") || search.toLowerCase().contains("comix")) {
                                    if (!dataSnapshots.contains(data) && user.minicomics) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("queer") || search.toLowerCase().contains("LGBT")) {
                                    if (!dataSnapshots.contains(data) && user.queerZines) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("feminist") || search.toLowerCase().contains("grrrl") || search.toLowerCase().contains("girl")) {
                                    if (!dataSnapshots.contains(data) && user.feministZines) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("politic") || search.toLowerCase().contains("govern")) {
                                    if (!dataSnapshots.contains(data) && user.politicalZines) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("music") || search.toLowerCase().contains("band")) {
                                    if (!dataSnapshots.contains(data) && user.musicZines) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("fiction") || search.toLowerCase().contains("story") || search.toLowerCase().contains("prose")) {
                                    if (!dataSnapshots.contains(data) && user.fictionZines) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("other")) {
                                    if (!dataSnapshots.contains(data) && user.other) {
                                        dataSnapshots.add(data);
                                    }
                                }
                            }
                        }

                        final int numChildren = (int) dataSnapshots.size();
                        for (final DataSnapshot snapshot : dataSnapshots) {
                            profileSearch(snapshot, numChildren);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else if (second.equals(array2[6])) {
                // zines made search
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference();
                databaseReference.orderByChild("username").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (search.isEmpty()) {
                                dataSnapshots.add(data);
                            } else {
                                User user = data.getValue(User.class);
                                if (search.toLowerCase().contains("perzine") || search.toLowerCase().contains("personal")) {
                                    if (!dataSnapshots.contains(data) && user.perzines2) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("fan")) {
                                    if (!dataSnapshots.contains(data) && user.fanzines2) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("art") || search.toLowerCase().contains("photo")) {
                                    if (!dataSnapshots.contains(data) && user.artZines2) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("comp")) {
                                    if (!dataSnapshots.contains(data) && user.compZines2) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("poetry") || search.toLowerCase().contains("chap")) {
                                    if (!dataSnapshots.contains(data) && user.chapbooks2) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("comic") || search.toLowerCase().contains("cartoon") || search.toLowerCase().contains("comix")) {
                                    if (!dataSnapshots.contains(data) && user.minicomics2) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("queer") || search.toLowerCase().contains("LGBT")) {
                                    if (!dataSnapshots.contains(data) && user.queerZines2) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("feminist") || search.toLowerCase().contains("grrrl") || search.toLowerCase().contains("girl")) {
                                    if (!dataSnapshots.contains(data) && user.feministZines2) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("politic") || search.toLowerCase().contains("govern")) {
                                    if (!dataSnapshots.contains(data) && user.politicalZines2) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("music") || search.toLowerCase().contains("band")) {
                                    if (!dataSnapshots.contains(data) && user.musicZines2) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("fiction") || search.toLowerCase().contains("story") || search.toLowerCase().contains("prose")) {
                                    if (!dataSnapshots.contains(data) && user.fictionZines2) {
                                        dataSnapshots.add(data);
                                    }
                                }
                                if (search.toLowerCase().contains("other")) {
                                    if (!dataSnapshots.contains(data) && user.other2) {
                                        dataSnapshots.add(data);
                                    }
                                }
                            }
                        }

                        final int numChildren = (int) dataSnapshots.size();
                        for (final DataSnapshot snapshot : dataSnapshots) {
                            profileSearch(snapshot, numChildren);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        } else if (first.equals(array[1])) {
            // zine search
            String second = searchSpinner3.getSelectedItem().toString();
            String[] array2 = getResources().getStringArray(R.array.search_options3);
            if (second.equals(array2[0])) {
                // zine name search
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final Map<String,ArrayList<DataSnapshot>> ds = new TreeMap<String,ArrayList<DataSnapshot>>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            for (DataSnapshot data2 : data.child("Zines").getChildren()) {
                                Zine zine = data2.getValue(Zine.class);
                                String i = zine.getZineName();

                                if (ds.containsKey(i)) {
                                    ArrayList<DataSnapshot> al = ds.get(i);
                                    al.add(data2);
                                    ds.put(i, al);
                                } else {
                                    ArrayList<DataSnapshot> al = new ArrayList<DataSnapshot>();
                                    al.add(data2);
                                    ds.put(i, al);
                                }
                            }
                        }

                        final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                        for (ArrayList<DataSnapshot> al : ds.values()) {
                            for (DataSnapshot data : al) {
                                if (search.isEmpty()) {
                                    dataSnapshots.add(data);
                                } else {
                                    Zine zine = data.getValue(Zine.class);
                                    if (zine.getZineName().toLowerCase().contains(search.toLowerCase())) {
                                        dataSnapshots.add(data);
                                    }
                                }
                            }
                        }

                        final int numChildren = (int) dataSnapshots.size();
                        for (final DataSnapshot snapshot : dataSnapshots) {
                            zineSearch(snapshot, numChildren);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else if (second.equals(array2[1])) {
                // num pages search
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final Map<Integer,ArrayList<DataSnapshot>> ds = new TreeMap<Integer,ArrayList<DataSnapshot>>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            for (DataSnapshot data2 : data.child("Zines").getChildren()) {
                                Zine zine = data2.getValue(Zine.class);
                                Integer i = Integer.parseInt(zine.getNumPages());

                                if (ds.containsKey(i)) {
                                    ArrayList<DataSnapshot> al = ds.get(i);
                                    al.add(data2);
                                    ds.put(i, al);
                                } else {
                                    ArrayList<DataSnapshot> al = new ArrayList<DataSnapshot>();
                                    al.add(data2);
                                    ds.put(i, al);
                                }
                            }
                        }

                        final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                        for (ArrayList<DataSnapshot> al : ds.values()) {
                            for (DataSnapshot data : al) {
                                if (search.isEmpty()) {
                                    dataSnapshots.add(data);
                                } else {
                                    Zine zine = data.getValue(Zine.class);
                                    if (String.valueOf(zine.getNumPages()) == String.valueOf(search)) {
                                        dataSnapshots.add(data);
                                    }
                                }
                            }
                        }

                        final int numChildren = (int) dataSnapshots.size();
                        for (final DataSnapshot snapshot : dataSnapshots) {
                            zineSearch(snapshot, numChildren);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else if (second.equals(array2[2])) {
                // zine size search
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final Map<String,ArrayList<DataSnapshot>> ds = new TreeMap<String,ArrayList<DataSnapshot>>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            for (DataSnapshot data2 : data.child("Zines").getChildren()) {
                                Zine zine = data2.getValue(Zine.class);
                                String i = zine.getZineSize();

                                if (ds.containsKey(i)) {
                                    ArrayList<DataSnapshot> al = ds.get(i);
                                    al.add(data2);
                                    ds.put(i, al);
                                } else {
                                    ArrayList<DataSnapshot> al = new ArrayList<DataSnapshot>();
                                    al.add(data2);
                                    ds.put(i, al);
                                }
                            }
                        }

                        final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                        for (ArrayList<DataSnapshot> al : ds.values()) {
                            for (DataSnapshot data : al) {
                                if (search.isEmpty()) {
                                    dataSnapshots.add(data);
                                } else {
                                    Zine zine = data.getValue(Zine.class);
                                    if (zine.getZineSize().toLowerCase().contains(search.toLowerCase())) {
                                        dataSnapshots.add(data);
                                    }
                                }
                            }
                        }

                        final int numChildren = (int) dataSnapshots.size();
                        for (final DataSnapshot snapshot : dataSnapshots) {
                            zineSearch(snapshot, numChildren);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else if (second.equals(array2[3])) {
                // zine description search
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final Map<String,ArrayList<DataSnapshot>> ds = new TreeMap<String,ArrayList<DataSnapshot>>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            for (DataSnapshot data2 : data.child("Zines").getChildren()) {
                                Zine zine = data2.getValue(Zine.class);
                                String i = zine.getDescriptionText();

                                if (ds.containsKey(i)) {
                                    ArrayList<DataSnapshot> al = ds.get(i);
                                    al.add(data2);
                                    ds.put(i, al);
                                } else {
                                    ArrayList<DataSnapshot> al = new ArrayList<DataSnapshot>();
                                    al.add(data2);
                                    ds.put(i, al);
                                }
                            }
                        }

                        final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                        for (ArrayList<DataSnapshot> al : ds.values()) {
                            for (DataSnapshot data : al) {
                                if (search.isEmpty()) {
                                    dataSnapshots.add(data);
                                } else {
                                    Zine zine = data.getValue(Zine.class);
                                    if (zine.getDescriptionText().toLowerCase().contains(search.toLowerCase())) {
                                        dataSnapshots.add(data);
                                    }
                                }
                            }
                        }

                        final int numChildren = (int) dataSnapshots.size();
                        for (final DataSnapshot snapshot : dataSnapshots) {
                            zineSearch(snapshot, numChildren);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else if (second.equals(array2[4])) {
                // zine tags search
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final Map<String,ArrayList<DataSnapshot>> ds = new TreeMap<String,ArrayList<DataSnapshot>>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            for (DataSnapshot data2 : data.child("Zines").getChildren()) {
                                Zine zine = data2.getValue(Zine.class);
                                String i = zine.getTags();

                                if (ds.containsKey(i)) {
                                    ArrayList<DataSnapshot> al = ds.get(i);
                                    al.add(data2);
                                    ds.put(i, al);
                                } else {
                                    ArrayList<DataSnapshot> al = new ArrayList<DataSnapshot>();
                                    al.add(data2);
                                    ds.put(i, al);
                                }
                            }
                        }

                        final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                        for (ArrayList<DataSnapshot> al : ds.values()) {
                            for (DataSnapshot data : al) {
                                if (search.isEmpty()) {
                                    dataSnapshots.add(data);
                                } else {
                                    Zine zine = data.getValue(Zine.class);
                                    if (zine.getTags().toLowerCase().contains(search.toLowerCase())) {
                                        dataSnapshots.add(data);
                                    }
                                }
                            }
                        }

                        final int numChildren = (int) dataSnapshots.size();
                        for (final DataSnapshot snapshot : dataSnapshots) {
                            zineSearch(snapshot, numChildren);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    public void profileSearch(final DataSnapshot snapshot, int numChildren) {

        final User user = snapshot.getValue(User.class);

        final Counter counter = new Counter();

        final ImageView profilePic = new ImageView(Search.this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.search_image), getResources().getDimensionPixelSize(R.dimen.search_image));
        profilePic.setLayoutParams(imageParams);

        if (user.profilePic.isEmpty()) {

            final LinearLayout layout2 = new LinearLayout(Search.this);
            layout2.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty));
            layout2.setOrientation(HORIZONTAL);
            layout2.setMinimumWidth(MATCH_PARENT);
            layout.addView(layout2);
            profilePic.setImageResource(R.drawable.logo);
            layout2.addView(profilePic);

            View.OnClickListener clickListener = new View.OnClickListener() {
                public void onClick(View v) {
                    if (v.equals(layout2)) {
                        Intent intent = new Intent(Search.this, ViewProfile.class);
                        intent.putExtra("uid", snapshot.getKey());
                        startActivity(intent);
                    }
                }
            };
            layout2.setOnClickListener(clickListener);

            final LinearLayout layout3 = new LinearLayout(Search.this);
            layout3.setOrientation(VERTICAL);
            layout3.setMinimumHeight(WRAP_CONTENT);
            layout3.setMinimumWidth(WRAP_CONTENT);
            layout2.addView(layout3);

            TextView un = new TextView(Search.this);
            un.setText(user.username);
            un.setTextSize(20);
            un.setTypeface(Typeface.DEFAULT_BOLD);
            un.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);
            layout3.addView(un);

            TextView birthYear = new TextView(Search.this);
            birthYear.setText("Birth Year: " + user.birthYear);
            birthYear.setTextSize(18);
            birthYear.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);
            layout3.addView(birthYear);

            TextView gender = new TextView(Search.this);
            gender.setText("Gender: " + user.gender);
            gender.setTextSize(18);
            gender.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);
            layout3.addView(gender);

            TextView location = new TextView(Search.this);
            location.setText("Location: " + user.location);
            location.setTextSize(18);
            location.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);
            layout3.addView(location);

            final TextView completedTrades = new TextView(Search.this);
            completedTrades.setTextSize(18);
            completedTrades.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);

            final Counter counter2 = new Counter();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference(snapshot.getKey() + "/Trades");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        final Trade trade = data.getValue(Trade.class);

                        if (dataSnapshot.hasChildren()) {
                            if ((trade.getTradeAccepterAccepted() && ((trade.getTradeAccepterCompleted() && trade.getTradeInitiatorCompleted())))
                                    || (((trade.getTradeAccepter().equals(snapshot.getKey()) && trade.getTradeAccepterAccepted())
                                    || (trade.getTradeInitiator().equals(snapshot.getKey()) && trade.getTradeAccepterAccepted()))
                                    && ((trade.getTradeAccepterCompleted() ^ trade.getTradeInitiatorCompleted())
                                    && (trade.getTradeAccepterCancelled() || trade.getTradeInitiatorCancelled()) && trade.timePassed()))) {
                                counter2.increment();
                            }
                        }
                    }

                    completedTrades.setText("Completed Trades: " + counter2.getCount());
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            layout3.addView(completedTrades);

            final TextView tradeRating = new TextView(Search.this);
            tradeRating.setTextSize(18);
            tradeRating.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);

            final ArrayList<String> array = new ArrayList<String>();
            final StringHolder str = new StringHolder();
            DatabaseReference ref2 = database.getReference(snapshot.getKey() + "/Ratings");
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
            layout3.addView(tradeRating);

            counter.increment();
            if (counter.getCount() == numChildren) {
                TextView padding = new TextView(Search.this);
                padding.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                layout.addView(padding);
            }
        } else {

            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

            final LinearLayout layout2 = new LinearLayout(Search.this);
            layout2.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty));
            layout2.setOrientation(HORIZONTAL);
            layout2.setMinimumWidth(MATCH_PARENT);
            layout.addView(layout2);

            View.OnClickListener clickListener = new View.OnClickListener() {
                public void onClick(View v) {
                    if (v.equals(layout2)) {
                        Intent intent = new Intent(Search.this, ViewProfile.class);
                        intent.putExtra("uid", snapshot.getKey());
                        startActivity(intent);
                    }
                }
            };
            layout2.setOnClickListener(clickListener);

            try {

                //TextView ss = new TextView(Search.this);
                //LinearLayout.LayoutParams ssParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                //ss.setLayoutParams(ssParams);
                //ss.setText(snapshot.getKey());
                //layout.addView(ss);

                final File localFile = File.createTempFile("profilePic", "jpg");
                StorageReference ref = mStorageRef.child("images/" + snapshot.getKey() + "ProfilePic.jpg");
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
                        Toast.makeText(Search.this, "Error downloading image.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                Toast.makeText(Search.this, "Error creating file.",
                        Toast.LENGTH_SHORT).show();
            }

            layout2.addView(profilePic);

            final LinearLayout layout3 = new LinearLayout(Search.this);
            layout3.setOrientation(VERTICAL);
            layout3.setMinimumHeight(WRAP_CONTENT);
            layout3.setMinimumWidth(WRAP_CONTENT);
            layout2.addView(layout3);

            TextView un = new TextView(Search.this);
            un.setText(user.username);
            un.setTextSize(20);
            un.setTypeface(Typeface.DEFAULT_BOLD);
            un.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);
            layout3.addView(un);

            TextView birthYear = new TextView(Search.this);
            birthYear.setText("Birth Year: " + user.birthYear);
            birthYear.setTextSize(18);
            birthYear.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);
            layout3.addView(birthYear);

            TextView gender = new TextView(Search.this);
            gender.setText("Gender: " + user.gender);
            gender.setTextSize(18);
            gender.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);
            layout3.addView(gender);

            TextView location = new TextView(Search.this);
            location.setText("Location: " + user.location);
            location.setTextSize(18);
            location.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);
            layout3.addView(location);

            final TextView completedTrades = new TextView(Search.this);
            completedTrades.setTextSize(18);
            completedTrades.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference(snapshot.getKey() + "/Trades");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final Counter counter2 = new Counter();
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            final Trade trade = data.getValue(Trade.class);
                            if ((trade.getTradeAccepterAccepted() && ((trade.getTradeAccepterCompleted() && trade.getTradeInitiatorCompleted())))
                                    || (((trade.getTradeAccepter().equals(snapshot.getKey()) && trade.getTradeAccepterAccepted())
                                    || (trade.getTradeInitiator().equals(snapshot.getKey()) && trade.getTradeAccepterAccepted()))
                                    && ((trade.getTradeAccepterCompleted() ^ trade.getTradeInitiatorCompleted())
                                    && (trade.getTradeAccepterCancelled() || trade.getTradeInitiatorCancelled()) && trade.timePassed()))) {
                                counter2.increment();
                            }
                        }
                    }

                    completedTrades.setText("Completed Trades: " + counter2.getCount());
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            layout3.addView(completedTrades);

            final TextView tradeRating = new TextView(Search.this);
            tradeRating.setTextSize(18);
            tradeRating.setPadding((int) getResources().getDimension(R.dimen.padding),0,0,0);

            final ArrayList<String> array = new ArrayList<String>();
            final StringHolder str = new StringHolder();
            DatabaseReference ref2 = database.getReference(snapshot.getKey() + "/Ratings");
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
            layout3.addView(tradeRating);

            counter.increment();
            if (counter.getCount() == numChildren) {
                TextView padding = new TextView(Search.this);
                padding.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                layout.addView(padding);
            }

        }
    }

    public void zineSearch(final DataSnapshot snapshot, final int numChildren) {

        final Zine zine = snapshot.getValue(Zine.class);

        final Counter counter = new Counter();

        final ImageView zinePic = new ImageView(Search.this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.search_image), getResources().getDimensionPixelSize(R.dimen.search_image));
        zinePic.setLayoutParams(imageParams);

        final LinearLayout ll2 = new LinearLayout(Search.this);
        ll2.setOrientation(HORIZONTAL);
        ll2.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding));
        ll2.setMinimumHeight(WRAP_CONTENT);
        ll2.setMinimumWidth(WRAP_CONTENT);
        ll2.setGravity(TOP);
        layout.addView(ll2);

        View.OnClickListener clickListener = new View.OnClickListener() {
            public void onClick(View v) {
                if (v.equals(ll2)) {
                    Intent intent = new Intent(Search.this, ViewProfile.class);
                    intent.putExtra("uid", zine.getUser());
                    startActivity(intent);
                }
            }
        };
        ll2.setOnClickListener(clickListener);

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("images/zines/" + zine.getUser() + "/" + String.valueOf(zine.getTimeStamp()) + "ZinePic.jpg");
        try {
            final File localFile = File.createTempFile("zinePic", "jpg");
            mStorageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bMap = BitmapFactory.decodeFile(localFile.toString());
                            ImageView zinePic = new ImageView(Search.this);
                            zinePic.setImageBitmap(bMap);
                            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.zine_image), getResources().getDimensionPixelSize(R.dimen.zine_image));
                            zinePic.setLayoutParams(imageParams);
                            ll2.addView(zinePic);

                            final LinearLayout ll3 = new LinearLayout(Search.this);
                            ll3.setOrientation(VERTICAL);
                            ll3.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding));
                            ll3.setMinimumHeight(WRAP_CONTENT);
                            ll3.setMinimumWidth(WRAP_CONTENT);
                            ll3.setGravity(TOP);
                            ll2.addView(ll3);

                            TextView zn = new TextView(Search.this);
                            zn.setText(zine.getZineName());
                            zn.setTextSize(20);
                            zn.setTypeface(Typeface.DEFAULT_BOLD);
                            ll3.addView(zn);

                            final TextView zu = new TextView(Search.this);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = database.getReference(zine.getUser());
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    zu.setText("User: " + user.username);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });

                            zu.setTextSize(18);
                            ll3.addView(zu);

                            TextView np = new TextView(Search.this);
                            np.setText("Number of Pages: " + zine.getNumPages());
                            np.setTextSize(18);
                            ll3.addView(np);

                            TextView zs = new TextView(Search.this);
                            zs.setText("Zine Size: " + zine.getZineSize());
                            zs.setTextSize(18);
                            ll3.addView(zs);

                            TextView zd = new TextView(Search.this);
                            zd.setText("Description: " + zine.getDescriptionText());
                            zd.setTextSize(18);
                            ll3.addView(zd);

                            counter.increment();
                            if (counter.getCount() == numChildren) {
                                TextView padding = new TextView(Search.this);
                                padding.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                layout.addView(padding);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    ImageView zinePic = new ImageView(Search.this);
                    zinePic.setImageResource(R.drawable.logo);
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.zine_image), getResources().getDimensionPixelSize(R.dimen.zine_image));
                    zinePic.setLayoutParams(imageParams);
                    ll2.addView(zinePic);

                    final LinearLayout ll3 = new LinearLayout(Search.this);
                    ll3.setOrientation(VERTICAL);
                    ll3.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding));
                    ll3.setMinimumHeight(WRAP_CONTENT);
                    ll3.setMinimumWidth(WRAP_CONTENT);
                    ll3.setGravity(TOP);
                    ll2.addView(ll3);

                    TextView zn = new TextView(Search.this);
                    zn.setText(zine.getZineName());
                    zn.setTextSize(20);
                    zn.setTypeface(Typeface.DEFAULT_BOLD);
                    ll3.addView(zn);

                    final TextView zu = new TextView(Search.this);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = database.getReference(zine.getUser());
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            zu.setText("User: " + user.username);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    zu.setTextSize(18);
                    ll3.addView(zu);

                    TextView np = new TextView(Search.this);
                    np.setText("Number of Pages: " + zine.getNumPages());
                    np.setTextSize(18);
                    ll3.addView(np);

                    TextView zs = new TextView(Search.this);
                    zs.setText("Zine Size: " + zine.getZineSize());
                    zs.setTextSize(18);
                    ll3.addView(zs);

                    TextView zd = new TextView(Search.this);
                    zd.setText("Description: " + zine.getDescriptionText());
                    zd.setTextSize(18);
                    ll3.addView(zd);

                    counter.increment();
                    if (counter.getCount() == numChildren) {
                        TextView padding = new TextView(Search.this);
                        padding.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                        layout.addView(padding);
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(Search.this, "Error creating file.",
                    Toast.LENGTH_SHORT).show();
        }


    }

}
