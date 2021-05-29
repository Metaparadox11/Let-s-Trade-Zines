package com.oliviamontoya.letstradezines;

import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.codec.binary.Base64;

import java.util.Map;
import java.util.TreeMap;

import static android.view.Gravity.RIGHT;

public class LoadMessages extends AppCompatActivity {

    FloatingActionButton fab;
    EditText et;
    TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_messages);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        et = (EditText) findViewById(R.id.et);

        DatabaseReference from = FirebaseDatabase.getInstance().getReference(getIntent().getStringExtra("to"));
        from.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot h) {
                User userTo = h.getValue(User.class);
                header = (TextView) findViewById(R.id.header);
                header.setText("Messaging " + userTo.username);
                View.OnClickListener clickListener = new View.OnClickListener() {
                    public void onClick(View v) {
                        if (v.equals(header)) {
                            Intent intent = new Intent(LoadMessages.this, ViewProfile.class);
                            intent.putExtra("uid", h.getKey());
                            startActivity(intent);
                        }
                    }
                };
                header.setOnClickListener(clickListener);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Load previous messages

        final LinearLayout messagesLayout = (LinearLayout) findViewById(R.id.messagesLayout);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(getIntent().getStringExtra("user") + "/Messages");
        databaseReference.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                Map<String, Message> map = new TreeMap<String, Message>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message m = snapshot.getValue(Message.class);
                    map.put(String.valueOf(m.getTimeStamp()), m);
                }

                for (final Message value: map.values()) {

                    if ((value.getTo().equals(getIntent().getStringExtra("to"))) && (value.getFrom().equals(getIntent().getStringExtra("user")))) {

                        final TextView unTV = new TextView(LoadMessages.this);

                        DatabaseReference from = FirebaseDatabase.getInstance().getReference(value.getFrom());
                        from.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot data) {
                                User userFrom = data.getValue(User.class);

                                unTV.setText(userFrom.username);

                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        unTV.setTextSize(20);
                        unTV.setTypeface(Typeface.DEFAULT_BOLD);
                        unTV.setGravity(RIGHT);
                        unTV.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),0);
                        messagesLayout.addView(unTV);

                        TextView messageTV = new TextView(LoadMessages.this);
                        String encodedString = value.getMessageText();
                        Base64 base64 = new Base64();
                        String decodedVersion = new String(base64.decode(encodedString.getBytes()));
                        messageTV.setText(decodedVersion);
                        messageTV.setTextSize(18);
                        messageTV.setGravity(RIGHT);
                        messageTV.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                        messagesLayout.addView(messageTV);

                        TextView timeStampTV = new TextView(LoadMessages.this);
                        timeStampTV.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", value.getTimeStamp()));
                        timeStampTV.setTextSize(16);
                        timeStampTV.setGravity(RIGHT);
                        timeStampTV.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                        messagesLayout.addView(timeStampTV);

                        final ScrollView scroll = (ScrollView) findViewById(R.id.scroll);
                        scroll.post(new Runnable() {
                            @Override
                            public void run() {
                                scroll.fullScroll(View.FOCUS_DOWN);
                            }
                        });

                    } else if (((value.getTo().equals(getIntent().getStringExtra("user"))) && (value.getFrom().equals(getIntent().getStringExtra("to"))))) {
                        final TextView unTV = new TextView(LoadMessages.this);

                        DatabaseReference from = FirebaseDatabase.getInstance().getReference(value.getFrom());
                        from.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot data) {
                                User userFrom = data.getValue(User.class);

                                unTV.setText(userFrom.username);

                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        unTV.setTextSize(20);
                        unTV.setTypeface(Typeface.DEFAULT_BOLD);
                        unTV.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),0);
                        messagesLayout.addView(unTV);

                        TextView messageTV = new TextView(LoadMessages.this);
                        String encodedString = value.getMessageText();
                        Base64 base64 = new Base64();
                        String decodedVersion = new String(base64.decode(encodedString.getBytes()));
                        messageTV.setText(decodedVersion);
                        messageTV.setTextSize(18);
                        messageTV.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                        messagesLayout.addView(messageTV);

                        TextView timeStampTV = new TextView(LoadMessages.this);
                        timeStampTV.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", value.getTimeStamp()));
                        timeStampTV.setTextSize(16);
                        timeStampTV.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                        messagesLayout.addView(timeStampTV);

                        final ScrollView scroll = (ScrollView) findViewById(R.id.scroll);
                        scroll.post(new Runnable() {
                            @Override
                            public void run() {
                                scroll.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Add new message onClick of fab

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Message message = new Message(et.getText().toString(), getIntent().getStringExtra("to"), user.getUid());
                String unencodedString = message.getMessageText();
                Base64 base64 = new Base64();
                String encodedVersion = new String(base64.encode(unencodedString.getBytes()));
                message.setMessage(encodedVersion);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(user.getUid() + "/Messages/" + message.getTimeStamp() + user.getUid());
                databaseReference.setValue(message);
                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference(getIntent().getStringExtra("to") + "/Messages/" + message.getTimeStamp() + user.getUid());
                databaseReference2.setValue(message);

                // Clear the input
                et.setText("");
                recreate();

                final ScrollView scroll = (ScrollView) findViewById(R.id.scroll);
                scroll.post(new Runnable() {
                    @Override
                    public void run() {
                        scroll.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });
    }
}
