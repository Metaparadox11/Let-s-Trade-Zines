package com.oliviamontoya.letstradezines;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.text.format.DateFormat;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView profileImg;
    TextView usernameDisplay;
    LinearLayout openTrades;
    LinearLayout messages;
    TextView followedFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        profileImg = (ImageView) findViewById(R.id.profileImg);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = database.getReference(firebaseUser.getUid());
        //DatabaseReference child = databaseReference.child(username.getText().toString());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user.profilePic.isEmpty()) {
                    profileImg.setImageResource(R.drawable.logo);
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
                                        profileImg.setImageBitmap(bMap);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle failed download
                                // ...
                            }
                        });
                    } catch(Exception e) {
                        Toast.makeText(Main2Activity.this, "Error creating file.",
                                Toast.LENGTH_SHORT).show();
                    }

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        usernameDisplay = (TextView) findViewById(R.id.usernameDisplay);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            usernameDisplay.setText(name);
        }

        openTrades = (LinearLayout) findViewById(R.id.openTrades);
        openTrades.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        TextView ot = new TextView(Main2Activity.this);
        ot.setText("Open Trades:");
        ot.setTextSize(22);
        ot.setTextColor(Color.WHITE);
        ot.setTypeface(Typeface.DEFAULT_BOLD);
        ot.setPadding(0, 0, 0, 0);
        openTrades.addView(ot);

        final Counter counter2 = new Counter();

        DatabaseReference databaseReference2 = database.getReference(user.getUid() + "/Trades");
        databaseReference2.orderByChild("timeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (counter2.getCount() != 0) {
                    openTrades.removeAllViewsInLayout();
                }

                final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    dataSnapshots.add(data);
                }
                //Collections.reverse(dataSnapshots);

                for (final DataSnapshot snapshot : dataSnapshots) {

                    final Trade trade = snapshot.getValue(Trade.class);

                    if ((((trade.getTradeAccepter().equals(user.getUid()) && trade.getTradeAccepterAccepted())
                            || (trade.getTradeInitiator().equals(user.getUid()) && trade.getTradeAccepterAccepted()))
                            && (trade.timePassed() && (!trade.getTradeAccepterCancelled() && !trade.getTradeInitiatorCancelled())
                            && (!trade.getTradeAccepterCompleted() ^ !trade.getTradeInitiatorCompleted())))
                            || (((trade.getTradeAccepter().equals(user.getUid()) && trade.getTradeAccepterAccepted())
                            || (trade.getTradeInitiator().equals(user.getUid()) && trade.getTradeAccepterAccepted()))
                            && (!trade.timePassed() && (!trade.getTradeAccepterCompleted() ^ !trade.getTradeInitiatorCompleted())))) {

                        // Open trades

                        if (trade.getTradeAccepter().equals(user.getUid()) || trade.getTradeInitiator().equals(user.getUid())) {
                            final ImageView msgIcon = new ImageView(Main2Activity.this);
                            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.small_image), getResources().getDimensionPixelSize(R.dimen.small_image));
                            msgIcon.setLayoutParams(imageParams);
                            msgIcon.setPadding(0,(int) getResources().getDimension(R.dimen.padding),0,0);
                            msgIcon.setImageResource(R.mipmap.ic_compare_arrows_black_48dp);

                            final LinearLayout layout2 = new LinearLayout(Main2Activity.this);
                            layout2.setPadding((int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.empty));
                            layout2.setOrientation(HORIZONTAL);
                            layout2.setMinimumWidth(MATCH_PARENT);
                            openTrades.addView(layout2);

                            layout2.addView(msgIcon);

                            final LinearLayout layout3 = new LinearLayout(Main2Activity.this);
                            layout3.setOrientation(VERTICAL);
                            layout3.setMinimumHeight(WRAP_CONTENT);
                            layout3.setMinimumWidth(MATCH_PARENT);
                            layout2.addView(layout3);

                            final TextView un = new TextView(Main2Activity.this);
                            DatabaseReference from = FirebaseDatabase.getInstance().getReference();
                            if (trade.getTradeInitiator().equals(user.getUid())) {
                                from = FirebaseDatabase.getInstance().getReference(trade.getTradeAccepter());
                            } else {
                                from = FirebaseDatabase.getInstance().getReference(trade.getTradeInitiator());
                            }
                            from.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot data) {
                                    User userFrom = data.getValue(User.class);
                                    un.setText(userFrom.username + " <-> " + user.getDisplayName());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            un.setTextSize(20);
                            un.setTextColor(Color.WHITE);
                            un.setTypeface(Typeface.DEFAULT_BOLD);
                            un.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),0);
                            layout3.addView(un);

                            if (trade.getTradeInitiator().equals(user.getUid())) {

                                TextView tradeInitiatorZines = new TextView(Main2Activity.this);
                                tradeInitiatorZines.setText("Zines Offered: " + trade.getTradeInitiatorZines());
                                tradeInitiatorZines.setTextSize(18);
                                tradeInitiatorZines.setTextColor(Color.WHITE);
                                tradeInitiatorZines.setPadding((int) getResources().getDimension(R.dimen.padding), 0, (int) getResources().getDimension(R.dimen.padding), 0);
                                layout3.addView(tradeInitiatorZines);

                                TextView tradeAccepterZines = new TextView(Main2Activity.this);
                                tradeAccepterZines.setText("Zines Wanted: " + trade.getTradeAccepterZines());
                                tradeAccepterZines.setTextSize(18);
                                tradeAccepterZines.setTextColor(Color.WHITE);
                                tradeAccepterZines.setPadding((int) getResources().getDimension(R.dimen.padding), 0, (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding));
                                layout3.addView(tradeAccepterZines);

                            } else {

                                TextView tradeAccepterZines = new TextView(Main2Activity.this);
                                tradeAccepterZines.setText("Zines Offered: " + trade.getTradeAccepterZines());
                                tradeAccepterZines.setTextSize(18);
                                tradeAccepterZines.setTextColor(Color.WHITE);
                                tradeAccepterZines.setPadding((int) getResources().getDimension(R.dimen.padding), 0, (int) getResources().getDimension(R.dimen.padding), 0);
                                layout3.addView(tradeAccepterZines);

                                TextView tradeInitiatorZines = new TextView(Main2Activity.this);
                                tradeInitiatorZines.setText("Zines Wanted: " + trade.getTradeInitiatorZines());
                                tradeInitiatorZines.setTextSize(18);
                                tradeInitiatorZines.setTextColor(Color.WHITE);
                                tradeInitiatorZines.setPadding((int) getResources().getDimension(R.dimen.padding), 0, (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding));
                                layout3.addView(tradeInitiatorZines);

                            }
                        }
                    }
                }
                counter2.increment();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        messages = (LinearLayout) findViewById(R.id.messages);
        messages.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        TextView m = new TextView(Main2Activity.this);
        m.setText("New Messages:");
        m.setTextSize(22);
        m.setTextColor(Color.WHITE);
        m.setTypeface(Typeface.DEFAULT_BOLD);
        m.setPadding(0, 0, 0, 0);
        messages.addView(m);

        final Counter counter = new Counter();

        DatabaseReference databaseReference3 = database.getReference(user.getUid() + "/Messages");
        databaseReference3.orderByChild("timeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (counter.getCount() != 0) {
                    messages.removeAllViewsInLayout();
                }

                final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Message message = data.getValue(Message.class);
                    if (message.getTo().equals(user.getUid())) {
                        dataSnapshots.add(data);
                    }
                }
                Collections.reverse(dataSnapshots);

                ArrayList<DataSnapshot> sublist = dataSnapshots;
                if (dataSnapshots.size() < 5) {
                    sublist = new ArrayList<DataSnapshot>(dataSnapshots.subList(0, dataSnapshots.size()));
                } else {
                    sublist = new ArrayList<DataSnapshot>(dataSnapshots.subList(0,5));
                }
                for (final DataSnapshot snapshot : sublist) {

                    final Message message = snapshot.getValue(Message.class);

                    final ImageView msgIcon = new ImageView(Main2Activity.this);
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.small_image), getResources().getDimensionPixelSize(R.dimen.small_image));
                    msgIcon.setLayoutParams(imageParams);
                    msgIcon.setPadding(0,(int) getResources().getDimension(R.dimen.padding),0,0);
                    msgIcon.setImageResource(R.mipmap.ic_chat_black_48dp);

                    final LinearLayout layout2 = new LinearLayout(Main2Activity.this);
                    layout2.setPadding((int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.empty));
                    layout2.setOrientation(HORIZONTAL);
                    layout2.setMinimumWidth(MATCH_PARENT);
                    messages.addView(layout2);

                    layout2.addView(msgIcon);

                    View.OnClickListener clickListener = new View.OnClickListener() {
                            public void onClick(View v) {
                                if (v.equals(layout2)) {
                                    Intent intent = new Intent(Main2Activity.this, LoadMessages.class);
                                    intent.putExtra("user", message.getTo());
                                    intent.putExtra("to", message.getFrom());
                                    startActivity(intent);
                                }
                            }
                    };
                    layout2.setOnClickListener(clickListener);

                    final LinearLayout layout3 = new LinearLayout(Main2Activity.this);
                    layout3.setOrientation(VERTICAL);
                    layout3.setMinimumHeight(WRAP_CONTENT);
                    layout3.setMinimumWidth(WRAP_CONTENT);
                    layout2.addView(layout3);

                    final TextView un = new TextView(Main2Activity.this);
                    DatabaseReference from = FirebaseDatabase.getInstance().getReference(message.getFrom());
                    from.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot data) {
                                User userFrom = data.getValue(User.class);

                                un.setText("From: " + userFrom.username);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                    });

                    un.setTextSize(20);
                    un.setTypeface(Typeface.DEFAULT_BOLD);
                    un.setTextColor(Color.WHITE);
                    un.setPadding(0,(int) getResources().getDimension(R.dimen.padding),0,0);
                    layout3.addView(un);

                    TextView timeStamp = new TextView(Main2Activity.this);
                    timeStamp.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getTimeStamp()));
                    timeStamp.setTextSize(16);
                    timeStamp.setTextColor(Color.WHITE);
                    timeStamp.setPadding(0,0,0,0);
                    layout3.addView(timeStamp);

                    TextView msg = new TextView(Main2Activity.this);
                    String encodedString = message.getMessageText();
                    Base64 base64 = new Base64();
                    String decodedVersion = new String(base64.decode(encodedString.getBytes()));
                    msg.setText(decodedVersion);
                    msg.setTextSize(18);
                    msg.setTextColor(Color.WHITE);
                    msg.setPadding(0,0,0,0);
                    layout3.addView(msg);
                }
                counter.increment();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        followedFeed = (TextView) findViewById(R.id.followedFeed);
        followedFeed.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        followedFeed.setTextColor(Color.WHITE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_faq) {
            Intent intent = new Intent(this, FAQ.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
        } else if (id == R.id.nav_trades) {
            Intent intent = new Intent(this, Trades.class);
            startActivity(intent);
        } else if (id == R.id.nav_messages) {
            Intent intent = new Intent(this, Messages.class);
            startActivity(intent);
        } else if (id == R.id.nav_followFeed) {

        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(this, Search.class);
            startActivity(intent);
        } else if (id == R.id.nav_logOut) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
