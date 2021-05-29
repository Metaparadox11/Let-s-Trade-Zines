package com.oliviamontoya.letstradezines;

import android.graphics.Color;
import android.graphics.Typeface;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class Trades extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trades);

        final Counter pageCounter = new Counter();

        final LinearLayout layout = (LinearLayout) findViewById(R.id.tradesLayout);
        final LinearLayout tradeLayout1 = (LinearLayout) findViewById(R.id.tradesLayout1);
        final TextView tradeLayout1t = (TextView) findViewById(R.id.tradesLayout1t);
        final LinearLayout tradeLayout2 = (LinearLayout) findViewById(R.id.tradesLayout2);
        final TextView tradeLayout2t = (TextView) findViewById(R.id.tradesLayout2t);
        final LinearLayout tradeLayout3 = (LinearLayout) findViewById(R.id.tradesLayout3);
        final TextView tradeLayout3t = (TextView) findViewById(R.id.tradesLayout3t);
        final LinearLayout tradeLayout4 = (LinearLayout) findViewById(R.id.tradesLayout4);
        final TextView tradeLayout4t = (TextView) findViewById(R.id.tradesLayout4t);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(user.getUid() + "/Trades");
        databaseReference.orderByChild("timeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (pageCounter.getCount() != 0) {
                    recreate();
                }
                pageCounter.increment();

                final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    dataSnapshots.add(data);
                }
                Collections.reverse(dataSnapshots);

                final int numChildren = (int) dataSnapshot.getChildrenCount();
                final Counter counter = new Counter();
                for (final DataSnapshot snapshot : dataSnapshots) {

                    final Trade trade = snapshot.getValue(Trade.class);

                    if (trade.getTradeAccepter().equals(user.getUid()) && !trade.getTradeAccepterAccepted()) {

                        // Trades suggested to you that you haven't accepted

                        final ImageView msgIcon = new ImageView(Trades.this);
                        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.search_image2), getResources().getDimensionPixelSize(R.dimen.search_image2));
                        msgIcon.setLayoutParams(imageParams);
                        msgIcon.setImageResource(R.mipmap.ic_compare_arrows_black_48dp);

                        final LinearLayout layout2 = new LinearLayout(Trades.this);
                        layout2.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty));
                        layout2.setOrientation(HORIZONTAL);
                        layout2.setMinimumWidth(MATCH_PARENT);
                        tradeLayout1.addView(layout2);

                        layout2.addView(msgIcon);

                        final LinearLayout layout3 = new LinearLayout(Trades.this);
                        layout3.setOrientation(VERTICAL);
                        layout3.setMinimumHeight(WRAP_CONTENT);
                        layout3.setMinimumWidth(MATCH_PARENT);
                        layout2.addView(layout3);

                        DatabaseReference from = FirebaseDatabase.getInstance().getReference(trade.getTradeInitiator());
                        from.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot data) {
                                User userFrom = data.getValue(User.class);
                                TextView un = new TextView(Trades.this);
                                un.setText("From: " + userFrom.username);
                                un.setTextSize(20);
                                un.setTypeface(Typeface.DEFAULT_BOLD);
                                un.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                layout3.addView(un);

                                TextView timeStamp = new TextView(Trades.this);
                                timeStamp.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", trade.getTimeStamp()));
                                timeStamp.setTextSize(18);
                                timeStamp.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                layout3.addView(timeStamp);

                                TextView tradeInitiatorZines = new TextView(Trades.this);
                                tradeInitiatorZines.setText("Zines Offered: " + trade.getTradeInitiatorZines());
                                tradeInitiatorZines.setTextSize(18);
                                tradeInitiatorZines.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                layout3.addView(tradeInitiatorZines);

                                TextView tradeAccepterZines = new TextView(Trades.this);
                                tradeAccepterZines.setText("Zines Wanted: " + trade.getTradeAccepterZines());
                                tradeAccepterZines.setTextSize(18);
                                tradeAccepterZines.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                layout3.addView(tradeAccepterZines);

                                Button button = new Button(Trades.this);
                                button.setText("Accept");
                                button.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                button.setTextSize(18);
                                button.setTextColor(Color.WHITE);
                                button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                layoutParams.setMargins((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.empty));
                                button.setLayoutParams(layoutParams);
                                button.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v)
                                    {
                                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(trade.getTradeInitiator() + "/Trades/trade" + trade.getTimeStamp());
                                        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference(user.getUid() + "/Trades/trade" + trade.getTimeStamp());

                                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot data2) {
                                                Trade t = data2.getValue(Trade.class);
                                                t.setTradeAccepterAccepted(true);
                                                ref.setValue(t);
                                                ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot data3) {
                                                        Trade t2 = data3.getValue(Trade.class);
                                                        t2.setTradeAccepterAccepted(true);
                                                        ref2.setValue(t2);
                                                        recreate();
                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });
                                layout3.addView(button);

                                Button button2 = new Button(Trades.this);
                                button2.setText("Reject");
                                button2.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                button2.setTextSize(18);
                                button2.setTextColor(Color.WHITE);
                                button2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                layoutParams2.setMargins((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                button2.setLayoutParams(layoutParams2);
                                button2.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v)
                                    {
                                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(trade.getTradeInitiator() + "/Trades/trade" + trade.getTimeStamp());
                                        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference(user.getUid() + "/Trades/trade" + trade.getTimeStamp());
                                        ref.removeValue();
                                        ref2.removeValue();
                                        recreate();
                                    }
                                });
                                layout3.addView(button2);

                                counter.increment();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.orderByChild("timeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    dataSnapshots.add(data);
                }
                Collections.reverse(dataSnapshots);

                final int numChildren = (int) dataSnapshot.getChildrenCount();
                final Counter counter = new Counter();
                for (final DataSnapshot snapshot : dataSnapshots) {

                    final Trade trade = snapshot.getValue(Trade.class);

                    if (trade.getTradeInitiator().equals(user.getUid()) && !trade.getTradeAccepterAccepted()) {

                        // Trades you suggested that haven't been accepted

                        final ImageView msgIcon = new ImageView(Trades.this);
                        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.search_image2), getResources().getDimensionPixelSize(R.dimen.search_image2));
                        msgIcon.setLayoutParams(imageParams);
                        msgIcon.setImageResource(R.mipmap.ic_compare_arrows_black_48dp);

                        final LinearLayout layout2 = new LinearLayout(Trades.this);
                        layout2.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty));
                        layout2.setOrientation(HORIZONTAL);
                        layout2.setMinimumWidth(MATCH_PARENT);
                        tradeLayout2.addView(layout2);

                        layout2.addView(msgIcon);

                        final LinearLayout layout3 = new LinearLayout(Trades.this);
                        layout3.setOrientation(VERTICAL);
                        layout3.setMinimumHeight(WRAP_CONTENT);
                        layout3.setMinimumWidth(MATCH_PARENT);
                        layout2.addView(layout3);

                        DatabaseReference from = FirebaseDatabase.getInstance().getReference(trade.getTradeAccepter());
                        from.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot data) {
                                User userFrom = data.getValue(User.class);
                                TextView un = new TextView(Trades.this);
                                un.setText("To: " + userFrom.username);
                                un.setTextSize(20);
                                un.setTypeface(Typeface.DEFAULT_BOLD);
                                un.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),0);
                                layout3.addView(un);

                                TextView timeStamp = new TextView(Trades.this);
                                timeStamp.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", trade.getTimeStamp()));
                                timeStamp.setTextSize(18);
                                timeStamp.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                layout3.addView(timeStamp);

                                TextView tradeInitiatorZines = new TextView(Trades.this);
                                tradeInitiatorZines.setText("Zines Offered: " + trade.getTradeInitiatorZines());
                                tradeInitiatorZines.setTextSize(18);
                                tradeInitiatorZines.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                layout3.addView(tradeInitiatorZines);

                                TextView tradeAccepterZines = new TextView(Trades.this);
                                tradeAccepterZines.setText("Zines Wanted: " + trade.getTradeAccepterZines());
                                tradeAccepterZines.setTextSize(18);
                                tradeAccepterZines.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                layout3.addView(tradeAccepterZines);

                                Button button = new Button(Trades.this);
                                button.setText("Cancel");
                                button.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                button.setTextSize(18);
                                button.setTextColor(Color.WHITE);
                                button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                layoutParams.setMargins((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                button.setLayoutParams(layoutParams);
                                button.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v)
                                    {
                                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(trade.getTradeInitiator() + "/Trades/trade" + trade.getTimeStamp());
                                        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference(user.getUid() + "/Trades/trade" + trade.getTimeStamp());
                                        ref.removeValue();
                                        ref2.removeValue();
                                        recreate();
                                    }
                                });
                                layout3.addView(button);

                                counter.increment();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.orderByChild("timeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    dataSnapshots.add(data);
                }
                Collections.reverse(dataSnapshots);

                final int numChildren = (int) dataSnapshot.getChildrenCount();
                final Counter counter = new Counter();
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

                        if (trade.getTradeAccepter().equals(user.getUid())) {
                            final ImageView msgIcon = new ImageView(Trades.this);
                            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.search_image2), getResources().getDimensionPixelSize(R.dimen.search_image2));
                            msgIcon.setLayoutParams(imageParams);
                            msgIcon.setImageResource(R.mipmap.ic_compare_arrows_black_48dp);

                            final LinearLayout layout2 = new LinearLayout(Trades.this);
                            layout2.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty));
                            layout2.setOrientation(HORIZONTAL);
                            layout2.setMinimumWidth(MATCH_PARENT);
                            tradeLayout3.addView(layout2);

                            layout2.addView(msgIcon);

                            final LinearLayout layout3 = new LinearLayout(Trades.this);
                            layout3.setOrientation(VERTICAL);
                            layout3.setMinimumHeight(WRAP_CONTENT);
                            layout3.setMinimumWidth(MATCH_PARENT);
                            layout2.addView(layout3);

                            DatabaseReference from = FirebaseDatabase.getInstance().getReference(trade.getTradeInitiator());
                            from.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot data) {
                                    User userFrom = data.getValue(User.class);
                                    TextView un = new TextView(Trades.this);
                                    un.setText("Trade Initiator: " + userFrom.username + "\nTrade Accepter: " + user.getDisplayName());
                                    un.setTextSize(20);
                                    un.setTypeface(Typeface.DEFAULT_BOLD);
                                    un.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(un);

                                    TextView timeStamp = new TextView(Trades.this);
                                    timeStamp.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", trade.getTimeStamp()));
                                    timeStamp.setTextSize(18);
                                    timeStamp.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(timeStamp);

                                    TextView tradeInitiatorCompleted = new TextView(Trades.this);
                                    tradeInitiatorCompleted.setText("Trade Initiator Completed: " + trade.getTradeInitiatorCompleted().toString());
                                    tradeInitiatorCompleted.setTextSize(18);
                                    tradeInitiatorCompleted.setPadding((int) getResources().getDimension(R.dimen.padding), 0, (int) getResources().getDimension(R.dimen.padding), 0);
                                    layout3.addView(tradeInitiatorCompleted);

                                    TextView tradeInitiatorCancelled = new TextView(Trades.this);
                                    tradeInitiatorCancelled.setText("Trade Initiator Cancelled: " + trade.getTradeInitiatorCancelled().toString());
                                    tradeInitiatorCancelled.setTextSize(18);
                                    tradeInitiatorCancelled.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(tradeInitiatorCancelled);

                                    TextView tradeAccepterCompleted = new TextView(Trades.this);
                                    tradeAccepterCompleted.setText("Trade Accepter Completed: " + trade.getTradeAccepterCompleted().toString());
                                    tradeAccepterCompleted.setTextSize(18);
                                    tradeAccepterCompleted.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(tradeAccepterCompleted);

                                    TextView tradeAccepterCancelled = new TextView(Trades.this);
                                    tradeAccepterCancelled.setText("Trade Accepter Cancelled: " + trade.getTradeAccepterCancelled().toString());
                                    tradeAccepterCancelled.setTextSize(18);
                                    tradeAccepterCancelled.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(tradeAccepterCancelled);

                                    TextView tradeInitiatorZines = new TextView(Trades.this);
                                    tradeInitiatorZines.setText("Zines Offered: " + trade.getTradeInitiatorZines());
                                    tradeInitiatorZines.setTextSize(18);
                                    tradeInitiatorZines.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(tradeInitiatorZines);

                                    TextView tradeAccepterZines = new TextView(Trades.this);
                                    tradeAccepterZines.setText("Zines Wanted: " + trade.getTradeAccepterZines());
                                    tradeAccepterZines.setTextSize(18);
                                    tradeAccepterZines.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                    layout3.addView(tradeAccepterZines);

                                    if (!trade.getTradeAccepterCompleted() && !trade.getTradeAccepterCancelled()) {
                                        Button button = new Button(Trades.this);
                                        button.setText("Mark Completed");
                                        button.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                        button.setTextSize(18);
                                        button.setTextColor(Color.WHITE);
                                        button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        layoutParams.setMargins((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.empty),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                        button.setLayoutParams(layoutParams);
                                        button.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(trade.getTradeInitiator() + "/Trades/trade" + trade.getTimeStamp());
                                                final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference(user.getUid() + "/Trades/trade" + trade.getTimeStamp());

                                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot data2) {
                                                        Trade t = data2.getValue(Trade.class);
                                                        t.setTradeAccepterCompleted(true);
                                                        ref.setValue(t);
                                                        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot data3) {
                                                                Trade t2 = data3.getValue(Trade.class);
                                                                t2.setTradeAccepterCompleted(true);
                                                                ref2.setValue(t2);
                                                                recreate();
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        });
                                        layout3.addView(button);
                                    }

                                    if (!trade.getTradeInitiatorCancelled() && !trade.getTradeAccepterCancelled()) {
                                        Button button = new Button(Trades.this);
                                        button.setText("Cancel");
                                        button.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                        button.setTextSize(18);
                                        button.setTextColor(Color.WHITE);
                                        button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        layoutParams.setMargins((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.empty),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                        button.setLayoutParams(layoutParams);
                                        button.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(trade.getTradeInitiator() + "/Trades/trade" + trade.getTimeStamp());
                                                final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference(user.getUid() + "/Trades/trade" + trade.getTimeStamp());

                                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot data2) {
                                                        Trade t = data2.getValue(Trade.class);
                                                        t.setTradeAccepterCancelled(true);
                                                        ref.setValue(t);
                                                        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot data3) {
                                                                Trade t2 = data3.getValue(Trade.class);
                                                                t2.setTradeAccepterCancelled(true);
                                                                ref2.setValue(t2);
                                                                recreate();
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        });
                                        layout3.addView(button);
                                    } else if (trade.getTradeInitiatorCancelled() && !trade.getTradeAccepterCancelled()) {
                                        Button button = new Button(Trades.this);
                                        button.setText("Cancel");
                                        button.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                        button.setTextSize(18);
                                        button.setTextColor(Color.WHITE);
                                        button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        layoutParams.setMargins((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.empty),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                        button.setLayoutParams(layoutParams);
                                        button.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v)
                                            {
                                                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(trade.getTradeInitiator() + "/Trades/trade" + trade.getTimeStamp());
                                                final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference(user.getUid() + "/Trades/trade" + trade.getTimeStamp());
                                                ref.removeValue();
                                                ref2.removeValue();
                                                recreate();
                                            }
                                        });
                                        layout3.addView(button);
                                    }

                                    counter.increment();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else if (trade.getTradeInitiator().equals(user.getUid())) {
                            final ImageView msgIcon = new ImageView(Trades.this);
                            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.search_image2), getResources().getDimensionPixelSize(R.dimen.search_image2));
                            msgIcon.setLayoutParams(imageParams);
                            msgIcon.setImageResource(R.mipmap.ic_compare_arrows_black_48dp);

                            final LinearLayout layout2 = new LinearLayout(Trades.this);
                            layout2.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty));
                            layout2.setOrientation(HORIZONTAL);
                            layout2.setMinimumWidth(MATCH_PARENT);
                            tradeLayout3.addView(layout2);

                            layout2.addView(msgIcon);

                            final LinearLayout layout3 = new LinearLayout(Trades.this);
                            layout3.setOrientation(VERTICAL);
                            layout3.setMinimumHeight(WRAP_CONTENT);
                            layout3.setMinimumWidth(MATCH_PARENT);
                            layout2.addView(layout3);

                            DatabaseReference from = FirebaseDatabase.getInstance().getReference(trade.getTradeAccepter());
                            from.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot data) {
                                    User userFrom = data.getValue(User.class);
                                    TextView un = new TextView(Trades.this);
                                    un.setText("Trade Initiator: " + user.getDisplayName() + "\nTrade Accepter: " + userFrom.username);
                                    un.setTextSize(20);
                                    un.setTypeface(Typeface.DEFAULT_BOLD);
                                    un.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(un);

                                    TextView timeStamp = new TextView(Trades.this);
                                    timeStamp.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", trade.getTimeStamp()));
                                    timeStamp.setTextSize(18);
                                    timeStamp.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(timeStamp);

                                    TextView tradeInitiatorCompleted = new TextView(Trades.this);
                                    tradeInitiatorCompleted.setText("Trade Initiator Completed: " + trade.getTradeInitiatorCompleted().toString());
                                    tradeInitiatorCompleted.setTextSize(18);
                                    tradeInitiatorCompleted.setPadding((int) getResources().getDimension(R.dimen.padding), 0, (int) getResources().getDimension(R.dimen.padding), 0);
                                    layout3.addView(tradeInitiatorCompleted);

                                    TextView tradeInitiatorCancelled = new TextView(Trades.this);
                                    tradeInitiatorCancelled.setText("Trade Initiator Cancelled: " + trade.getTradeInitiatorCancelled().toString());
                                    tradeInitiatorCancelled.setTextSize(18);
                                    tradeInitiatorCancelled.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(tradeInitiatorCancelled);

                                    TextView tradeAccepterCompleted = new TextView(Trades.this);
                                    tradeAccepterCompleted.setText("Trade Accepter Completed: " + trade.getTradeAccepterCompleted().toString());
                                    tradeAccepterCompleted.setTextSize(18);
                                    tradeAccepterCompleted.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(tradeAccepterCompleted);

                                    TextView tradeAccepterCancelled = new TextView(Trades.this);
                                    tradeAccepterCancelled.setText("Trade Accepter Cancelled: " + trade.getTradeAccepterCancelled().toString());
                                    tradeAccepterCancelled.setTextSize(18);
                                    tradeAccepterCancelled.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(tradeAccepterCancelled);

                                    TextView tradeInitiatorZines = new TextView(Trades.this);
                                    tradeInitiatorZines.setText("Zines Offered: " + trade.getTradeInitiatorZines());
                                    tradeInitiatorZines.setTextSize(18);
                                    tradeInitiatorZines.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(tradeInitiatorZines);

                                    TextView tradeAccepterZines = new TextView(Trades.this);
                                    tradeAccepterZines.setText("Zines Wanted: " + trade.getTradeAccepterZines());
                                    tradeAccepterZines.setTextSize(18);
                                    tradeAccepterZines.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                    layout3.addView(tradeAccepterZines);

                                    if (!trade.getTradeInitiatorCompleted() && !trade.getTradeInitiatorCancelled()) {
                                        Button button = new Button(Trades.this);
                                        button.setText("Mark Completed");
                                        button.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                        button.setTextSize(18);
                                        button.setTextColor(Color.WHITE);
                                        button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        layoutParams.setMargins((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.empty),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                        button.setLayoutParams(layoutParams);
                                        button.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(trade.getTradeAccepter() + "/Trades/trade" + trade.getTimeStamp());
                                                final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference(user.getUid() + "/Trades/trade" + trade.getTimeStamp());

                                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot data2) {
                                                        Trade t = data2.getValue(Trade.class);
                                                        t.setTradeInitiatorCompleted(true);
                                                        ref.setValue(t);
                                                        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot data3) {
                                                                Trade t2 = data3.getValue(Trade.class);
                                                                t2.setTradeInitiatorCompleted(true);
                                                                ref2.setValue(t2);
                                                                recreate();
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        });
                                        layout3.addView(button);
                                    }

                                    if (!trade.getTradeAccepterCancelled() && !trade.getTradeInitiatorCancelled()) {
                                        Button button = new Button(Trades.this);
                                        button.setText("Cancel");
                                        button.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                        button.setTextSize(18);
                                        button.setTextColor(Color.WHITE);
                                        button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        layoutParams.setMargins((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.empty),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                        button.setLayoutParams(layoutParams);
                                        button.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(trade.getTradeAccepter() + "/Trades/trade" + trade.getTimeStamp());
                                                final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference(user.getUid() + "/Trades/trade" + trade.getTimeStamp());

                                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot data2) {
                                                        Trade t = data2.getValue(Trade.class);
                                                        t.setTradeInitiatorCancelled(true);
                                                        ref.setValue(t);
                                                        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot data3) {
                                                                Trade t2 = data3.getValue(Trade.class);
                                                                t2.setTradeInitiatorCancelled(true);
                                                                ref2.setValue(t2);
                                                                recreate();
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        });
                                        layout3.addView(button);
                                    } else if (trade.getTradeAccepterCancelled() && !trade.getTradeInitiatorCancelled()) {
                                        Button button = new Button(Trades.this);
                                        button.setText("Cancel");
                                        button.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                        button.setTextSize(18);
                                        button.setTextColor(Color.WHITE);
                                        button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        layoutParams.setMargins((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.empty),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                        button.setLayoutParams(layoutParams);
                                        button.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v)
                                            {
                                                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(trade.getTradeAccepter() + "/Trades/trade" + trade.getTimeStamp());
                                                final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference(user.getUid() + "/Trades/trade" + trade.getTimeStamp());
                                                ref.removeValue();
                                                ref2.removeValue();
                                                recreate();
                                            }
                                        });
                                        layout3.addView(button);
                                    }

                                    counter.increment();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.orderByChild("timeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    dataSnapshots.add(data);
                }
                Collections.reverse(dataSnapshots);

                final int numChildren = (int) dataSnapshot.getChildrenCount();
                final Counter counter = new Counter();
                for (final DataSnapshot snapshot : dataSnapshots) {

                    final Trade trade = snapshot.getValue(Trade.class);

                    if ((((trade.getTradeAccepter().equals(user.getUid()) && trade.getTradeAccepterAccepted())
                            || (trade.getTradeInitiator().equals(user.getUid()) && trade.getTradeAccepterAccepted()))
                            && ((trade.getTradeAccepterCompleted() && trade.getTradeInitiatorCompleted())))
                            || (((trade.getTradeAccepter().equals(user.getUid()) && trade.getTradeAccepterAccepted())
                            || (trade.getTradeInitiator().equals(user.getUid()) && trade.getTradeAccepterAccepted()))
                            && ((trade.getTradeAccepterCompleted() ^ trade.getTradeInitiatorCompleted())
                            && (trade.getTradeAccepterCancelled() || trade.getTradeInitiatorCancelled()) && trade.timePassed()))) {

                        // Completed trades

                        if (trade.getTradeAccepter().equals(user.getUid())) {
                            final ImageView msgIcon = new ImageView(Trades.this);
                            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.search_image2), getResources().getDimensionPixelSize(R.dimen.search_image2));
                            msgIcon.setLayoutParams(imageParams);
                            msgIcon.setImageResource(R.mipmap.ic_compare_arrows_black_48dp);

                            final LinearLayout layout2 = new LinearLayout(Trades.this);
                            layout2.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty));
                            layout2.setOrientation(HORIZONTAL);
                            layout2.setMinimumWidth(MATCH_PARENT);
                            tradeLayout4.addView(layout2);

                            layout2.addView(msgIcon);

                            final LinearLayout layout3 = new LinearLayout(Trades.this);
                            layout3.setOrientation(VERTICAL);
                            layout3.setMinimumHeight(WRAP_CONTENT);
                            layout3.setMinimumWidth(MATCH_PARENT);
                            layout2.addView(layout3);

                            DatabaseReference from = FirebaseDatabase.getInstance().getReference(trade.getTradeInitiator());
                            from.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot data) {
                                    User userFrom = data.getValue(User.class);
                                    TextView un = new TextView(Trades.this);
                                    un.setText("Trade Initiator: " + userFrom.username + "\nTrade Accepter: " + user.getDisplayName());
                                    un.setTextSize(20);
                                    un.setTypeface(Typeface.DEFAULT_BOLD);
                                    un.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(un);

                                    TextView timeStamp = new TextView(Trades.this);
                                    timeStamp.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", trade.getTimeStamp()));
                                    timeStamp.setTextSize(18);
                                    timeStamp.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(timeStamp);

                                    TextView tradeInitiatorCompleted = new TextView(Trades.this);
                                    tradeInitiatorCompleted.setText("Trade Initiator Completed: " + trade.getTradeInitiatorCompleted().toString());
                                    tradeInitiatorCompleted.setTextSize(18);
                                    tradeInitiatorCompleted.setPadding((int) getResources().getDimension(R.dimen.padding), 0, (int) getResources().getDimension(R.dimen.padding), 0);
                                    layout3.addView(tradeInitiatorCompleted);

                                    TextView tradeInitiatorCancelled = new TextView(Trades.this);
                                    tradeInitiatorCancelled.setText("Trade Initiator Cancelled: " + trade.getTradeInitiatorCancelled().toString());
                                    tradeInitiatorCancelled.setTextSize(18);
                                    tradeInitiatorCancelled.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(tradeInitiatorCancelled);

                                    TextView tradeAccepterCompleted = new TextView(Trades.this);
                                    tradeAccepterCompleted.setText("Trade Accepter Completed: " + trade.getTradeAccepterCompleted().toString());
                                    tradeAccepterCompleted.setTextSize(18);
                                    tradeAccepterCompleted.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(tradeAccepterCompleted);

                                    TextView tradeAccepterCancelled = new TextView(Trades.this);
                                    tradeAccepterCancelled.setText("Trade Accepter Cancelled: " + trade.getTradeAccepterCancelled().toString());
                                    tradeAccepterCancelled.setTextSize(18);
                                    tradeAccepterCancelled.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(tradeAccepterCancelled);

                                    TextView tradeInitiatorZines = new TextView(Trades.this);
                                    tradeInitiatorZines.setText("Zines Offered: " + trade.getTradeInitiatorZines());
                                    tradeInitiatorZines.setTextSize(18);
                                    tradeInitiatorZines.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(tradeInitiatorZines);

                                    TextView tradeAccepterZines = new TextView(Trades.this);
                                    tradeAccepterZines.setText("Zines Wanted: " + trade.getTradeAccepterZines());
                                    tradeAccepterZines.setTextSize(18);
                                    tradeAccepterZines.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                    layout3.addView(tradeAccepterZines);

                                    final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference(trade.getTradeInitiator() + "/Ratings/rating" + trade.getTimeStamp());
                                    dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot data3) {
                                            if (!data3.exists()) {
                                                final EditText et = new EditText(Trades.this);
                                                et.setHint("0-5 Star Rating");
                                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                layoutParams.setMargins((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.empty),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                                et.setLayoutParams(layoutParams);
                                                layout3.addView(et);

                                                Button button = new Button(Trades.this);
                                                button.setText("Submit Trade Star Rating");
                                                button.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                                button.setTextSize(18);
                                                button.setTextColor(Color.WHITE);
                                                button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                                button.setLayoutParams(layoutParams);
                                                button.setOnClickListener(new View.OnClickListener() {
                                                    public void onClick(View v) {
                                                        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot data2) {
                                                                StringHolder rating = new StringHolder(et.getText().toString());
                                                                if (isRating(rating.getString())) {
                                                                    dbr.setValue(rating);
                                                                    recreate();
                                                                } else {
                                                                    Toast.makeText(Trades.this, "Not a valid rating.",
                                                                            Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                });
                                                layout3.addView(button);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    counter.increment();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else if (trade.getTradeInitiator().equals(user.getUid())) {
                            final ImageView msgIcon = new ImageView(Trades.this);
                            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.search_image2), getResources().getDimensionPixelSize(R.dimen.search_image2));
                            msgIcon.setLayoutParams(imageParams);
                            msgIcon.setImageResource(R.mipmap.ic_compare_arrows_black_48dp);

                            final LinearLayout layout2 = new LinearLayout(Trades.this);
                            layout2.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty));
                            layout2.setOrientation(HORIZONTAL);
                            layout2.setMinimumWidth(MATCH_PARENT);
                            tradeLayout4.addView(layout2);

                            layout2.addView(msgIcon);

                            final LinearLayout layout3 = new LinearLayout(Trades.this);
                            layout3.setOrientation(VERTICAL);
                            layout3.setMinimumHeight(WRAP_CONTENT);
                            layout3.setMinimumWidth(MATCH_PARENT);
                            layout2.addView(layout3);

                            DatabaseReference from = FirebaseDatabase.getInstance().getReference(trade.getTradeAccepter());
                            from.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot data) {
                                    User userFrom = data.getValue(User.class);
                                    TextView un = new TextView(Trades.this);
                                    un.setText("Trade Initiator: " + user.getDisplayName() + "\nTrade Accepter: " + userFrom.username);
                                    un.setTextSize(20);
                                    un.setTypeface(Typeface.DEFAULT_BOLD);
                                    un.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(un);

                                    TextView timeStamp = new TextView(Trades.this);
                                    timeStamp.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", trade.getTimeStamp()));
                                    timeStamp.setTextSize(18);
                                    timeStamp.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(timeStamp);

                                    TextView tradeInitiatorCompleted = new TextView(Trades.this);
                                    tradeInitiatorCompleted.setText("Trade Initiator Completed: " + trade.getTradeInitiatorCompleted().toString());
                                    tradeInitiatorCompleted.setTextSize(18);
                                    tradeInitiatorCompleted.setPadding((int) getResources().getDimension(R.dimen.padding), 0, (int) getResources().getDimension(R.dimen.padding), 0);
                                    layout3.addView(tradeInitiatorCompleted);

                                    TextView tradeInitiatorCancelled = new TextView(Trades.this);
                                    tradeInitiatorCancelled.setText("Trade Initiator Cancelled: " + trade.getTradeInitiatorCancelled().toString());
                                    tradeInitiatorCancelled.setTextSize(18);
                                    tradeInitiatorCancelled.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(tradeInitiatorCancelled);

                                    TextView tradeAccepterCompleted = new TextView(Trades.this);
                                    tradeAccepterCompleted.setText("Trade Accepter Completed: " + trade.getTradeAccepterCompleted().toString());
                                    tradeAccepterCompleted.setTextSize(18);
                                    tradeAccepterCompleted.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(tradeAccepterCompleted);

                                    TextView tradeAccepterCancelled = new TextView(Trades.this);
                                    tradeAccepterCancelled.setText("Trade Accepter Cancelled: " + trade.getTradeAccepterCancelled().toString());
                                    tradeAccepterCancelled.setTextSize(18);
                                    tradeAccepterCancelled.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(tradeAccepterCancelled);

                                    TextView tradeInitiatorZines = new TextView(Trades.this);
                                    tradeInitiatorZines.setText("Zines Offered: " + trade.getTradeInitiatorZines());
                                    tradeInitiatorZines.setTextSize(18);
                                    tradeInitiatorZines.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                                    layout3.addView(tradeInitiatorZines);

                                    TextView tradeAccepterZines = new TextView(Trades.this);
                                    tradeAccepterZines.setText("Zines Wanted: " + trade.getTradeAccepterZines());
                                    tradeAccepterZines.setTextSize(18);
                                    tradeAccepterZines.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                    layout3.addView(tradeAccepterZines);

                                    final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference(trade.getTradeAccepter() + "/Ratings/rating" + trade.getTimeStamp());
                                    dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot data3) {
                                            if (!data3.exists()) {
                                                final EditText et = new EditText(Trades.this);
                                                et.setHint("0-5 Star Rating");
                                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                layoutParams.setMargins((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.empty),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                                et.setLayoutParams(layoutParams);
                                                layout3.addView(et);

                                                Button button = new Button(Trades.this);
                                                button.setText("Submit Trade Star Rating");
                                                button.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                                                button.setTextSize(18);
                                                button.setTextColor(Color.WHITE);
                                                button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                                button.setLayoutParams(layoutParams);
                                                button.setOnClickListener(new View.OnClickListener() {
                                                    public void onClick(View v) {
                                                        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot data2) {
                                                                StringHolder rating = new StringHolder(et.getText().toString());
                                                                if (isRating(rating.getString())) {
                                                                    dbr.setValue(rating);
                                                                    recreate();
                                                                } else {
                                                                    Toast.makeText(Trades.this, "Not a valid rating.",
                                                                            Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                });
                                                layout3.addView(button);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    counter.increment();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public Boolean isRating(String rating) {
        float f = Float.parseFloat(rating);
        if (Float.compare(f, (float)0.0) >= 0 && Float.compare(f, (float)5.0) <= 0) {
            return true;
        }
        return false;
    }
}
