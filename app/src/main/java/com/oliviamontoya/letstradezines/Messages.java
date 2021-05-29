package com.oliviamontoya.letstradezines;

import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.codec.binary.Base64;

import java.util.ArrayList;
import java.util.Collections;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class Messages extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        final LinearLayout layout = (LinearLayout) findViewById(R.id.layout2);

        final Counter counter = new Counter();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(user.getUid() + "/Messages");
        databaseReference.orderByChild("timeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (counter.getCount() != 0) {
                    recreate();
                }
                counter.increment();

                final ArrayList<DataSnapshot> dataSnapshots = new ArrayList<DataSnapshot>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    dataSnapshots.add(data);
                }
                Collections.reverse(dataSnapshots);

                final int numChildren = (int) dataSnapshot.getChildrenCount();
                final Counter counter2 = new Counter();
                for (final DataSnapshot snapshot : dataSnapshots) {

                    final Message message = snapshot.getValue(Message.class);

                    if (message.getTo().equals(user.getUid())) {

                        final ImageView msgIcon = new ImageView(Messages.this);
                        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.search_image2), getResources().getDimensionPixelSize(R.dimen.search_image2));
                        msgIcon.setLayoutParams(imageParams);
                        msgIcon.setImageResource(R.mipmap.ic_chat_black_48dp);

                        final LinearLayout layout2 = new LinearLayout(Messages.this);
                        layout2.setPadding((int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.padding), (int) getResources().getDimension(R.dimen.empty));
                        layout2.setOrientation(HORIZONTAL);
                        layout2.setMinimumWidth(MATCH_PARENT);
                        layout.addView(layout2);

                        layout2.addView(msgIcon);

                        View.OnClickListener clickListener = new View.OnClickListener() {
                            public void onClick(View v) {
                                if (v.equals(layout2)) {
                                    Intent intent = new Intent(Messages.this, LoadMessages.class);
                                    intent.putExtra("user", message.getTo());
                                    intent.putExtra("to", message.getFrom());
                                    startActivity(intent);
                                }
                            }
                        };
                        layout2.setOnClickListener(clickListener);

                        final LinearLayout layout3 = new LinearLayout(Messages.this);
                        layout3.setOrientation(VERTICAL);
                        layout3.setMinimumHeight(WRAP_CONTENT);
                        layout3.setMinimumWidth(WRAP_CONTENT);
                        layout2.addView(layout3);

                        final TextView un = new TextView(Messages.this);

                        DatabaseReference from = FirebaseDatabase.getInstance().getReference(message.getFrom());
                        from.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot data) {
                                User userFrom = data.getValue(User.class);

                                un.setText(userFrom.username);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        un.setTextSize(20);
                        un.setTypeface(Typeface.DEFAULT_BOLD);
                        un.setPadding((int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding),0);
                        layout3.addView(un);

                        TextView timeStamp = new TextView(Messages.this);
                        timeStamp.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getTimeStamp()));
                        timeStamp.setTextSize(16);
                        timeStamp.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),0);
                        layout3.addView(timeStamp);

                        TextView msg = new TextView(Messages.this);
                        String encodedString = message.getMessageText();
                        Base64 base64 = new Base64();
                        String decodedVersion = new String(base64.decode(encodedString.getBytes()));
                        msg.setText(decodedVersion);
                        msg.setTextSize(18);
                        msg.setPadding((int) getResources().getDimension(R.dimen.padding),0,(int) getResources().getDimension(R.dimen.padding),(int) getResources().getDimension(R.dimen.padding));
                        layout3.addView(msg);

                        counter2.increment();
                        if (counter2.getCount() == numChildren) {
                            TextView padding = new TextView(Messages.this);
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
}
