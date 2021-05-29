package com.oliviamontoya.letstradezines;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class FAQ extends AppCompatActivity {

    TextView faq1;
    TextView faq1a;
    TextView faq2;
    TextView faq2a;
    TextView faq3;
    TextView faq3a;
    TextView faq4;
    TextView faq4a;
    TextView faq5;
    TextView faq5a;
    TextView faq6;
    TextView faq6a;
    TextView faq7;
    TextView faq7a;
    TextView faq8;
    TextView faq8a;
    TextView faq9;

    Boolean open1 = false;
    Boolean open2 = false;
    Boolean open3 = false;
    Boolean open4 = false;
    Boolean open5 = false;
    Boolean open6 = false;
    Boolean open7 = false;
    Boolean open8 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        faq1 = (TextView) findViewById(R.id.faq1);
        faq1a = (TextView) findViewById(R.id.faq1a);

        LayoutParams params1 = faq1a.getLayoutParams();
        params1.height = 0;
        faq1a.setLayoutParams(params1);

        View.OnClickListener clickListener = new View.OnClickListener() {
            public void onClick(View v) {
                if (v.equals(faq1)) {
                    if (!open1) {
                        LayoutParams params = faq1a.getLayoutParams();
                        params.height = WRAP_CONTENT;
                        faq1a.setLayoutParams(params);
                        open1 = true;
                    } else {
                        LayoutParams params = faq1a.getLayoutParams();
                        params.height = 0;
                        faq1a.setLayoutParams(params);
                        open1 = false;
                    }
                }
            }
        };
        faq1.setOnClickListener(clickListener);

        faq2 = (TextView) findViewById(R.id.faq2);
        faq2a = (TextView) findViewById(R.id.faq2a);

        LayoutParams params2 = faq2a.getLayoutParams();
        params2.height = 0;
        faq2a.setLayoutParams(params2);

        View.OnClickListener clickListener2 = new View.OnClickListener() {
            public void onClick(View v) {
                if (v.equals(faq2)) {
                    if (!open2) {
                        LayoutParams params = faq2a.getLayoutParams();
                        params.height = WRAP_CONTENT;
                        faq2a.setLayoutParams(params);
                        open2 = true;
                    } else {
                        LayoutParams params = faq2a.getLayoutParams();
                        params.height = 0;
                        faq2a.setLayoutParams(params);
                        open2 = false;
                    }
                }
            }
        };
        faq2.setOnClickListener(clickListener2);

        faq3 = (TextView) findViewById(R.id.faq3);
        faq3a = (TextView) findViewById(R.id.faq3a);

        LayoutParams params3 = faq3a.getLayoutParams();
        params3.height = 0;
        faq3a.setLayoutParams(params3);

        View.OnClickListener clickListener3 = new View.OnClickListener() {
            public void onClick(View v) {
                if (v.equals(faq3)) {
                    if (!open3) {
                        LayoutParams params = faq3a.getLayoutParams();
                        params.height = WRAP_CONTENT;
                        faq3a.setLayoutParams(params);
                        open3 = true;
                    } else {
                        LayoutParams params = faq3a.getLayoutParams();
                        params.height = 0;
                        faq3a.setLayoutParams(params);
                        open3 = false;
                    }
                }
            }
        };
        faq3.setOnClickListener(clickListener3);

        faq4 = (TextView) findViewById(R.id.faq4);
        faq4a = (TextView) findViewById(R.id.faq4a);

        LayoutParams params4 = faq4a.getLayoutParams();
        params4.height = 0;
        faq4a.setLayoutParams(params4);

        View.OnClickListener clickListener4 = new View.OnClickListener() {
            public void onClick(View v) {
                if (v.equals(faq4)) {
                    if (!open4) {
                        LayoutParams params = faq4a.getLayoutParams();
                        params.height = WRAP_CONTENT;
                        faq4a.setLayoutParams(params);
                        open4 = true;
                    } else {
                        LayoutParams params = faq4a.getLayoutParams();
                        params.height = 0;
                        faq4a.setLayoutParams(params);
                        open4 = false;
                    }
                }
            }
        };
        faq4.setOnClickListener(clickListener4);

        faq5 = (TextView) findViewById(R.id.faq5);
        faq5a = (TextView) findViewById(R.id.faq5a);

        LayoutParams params5 = faq5a.getLayoutParams();
        params5.height = 0;
        faq5a.setLayoutParams(params5);

        View.OnClickListener clickListener5 = new View.OnClickListener() {
            public void onClick(View v) {
                if (v.equals(faq5)) {
                    if (!open5) {
                        LayoutParams params = faq5a.getLayoutParams();
                        params.height = WRAP_CONTENT;
                        faq5a.setLayoutParams(params);
                        open5 = true;
                    } else {
                        LayoutParams params = faq5a.getLayoutParams();
                        params.height = 0;
                        faq5a.setLayoutParams(params);
                        open5 = false;
                    }
                }
            }
        };
        faq5.setOnClickListener(clickListener5);

        faq6 = (TextView) findViewById(R.id.faq6);
        faq6a = (TextView) findViewById(R.id.faq6a);

        LayoutParams params6 = faq6a.getLayoutParams();
        params6.height = 0;
        faq6a.setLayoutParams(params6);

        View.OnClickListener clickListener6 = new View.OnClickListener() {
            public void onClick(View v) {
                if (v.equals(faq6)) {
                    if (!open6) {
                        LayoutParams params = faq6a.getLayoutParams();
                        params.height = WRAP_CONTENT;
                        faq6a.setLayoutParams(params);
                        open6 = true;
                    } else {
                        LayoutParams params = faq6a.getLayoutParams();
                        params.height = 0;
                        faq6a.setLayoutParams(params);
                        open6 = false;
                    }
                }
            }
        };
        faq6.setOnClickListener(clickListener6);

        faq7 = (TextView) findViewById(R.id.faq7);
        faq7a = (TextView) findViewById(R.id.faq7a);

        LayoutParams params7 = faq7a.getLayoutParams();
        params7.height = 0;
        faq7a.setLayoutParams(params7);

        View.OnClickListener clickListener7 = new View.OnClickListener() {
            public void onClick(View v) {
                if (v.equals(faq7)) {
                    if (!open7) {
                        LayoutParams params = faq7a.getLayoutParams();
                        params.height = WRAP_CONTENT;
                        faq7a.setLayoutParams(params);
                        open7 = true;
                    } else {
                        LayoutParams params = faq7a.getLayoutParams();
                        params.height = 0;
                        faq7a.setLayoutParams(params);
                        open7 = false;
                    }
                }
            }
        };
        faq7.setOnClickListener(clickListener7);

        faq8 = (TextView) findViewById(R.id.faq8);
        faq8a = (TextView) findViewById(R.id.faq8a);

        LayoutParams params8 = faq8a.getLayoutParams();
        params8.height = 0;
        faq8a.setLayoutParams(params8);

        View.OnClickListener clickListener8 = new View.OnClickListener() {
            public void onClick(View v) {
                if (v.equals(faq8)) {
                    if (!open8) {
                        LayoutParams params = faq8a.getLayoutParams();
                        params.height = WRAP_CONTENT;
                        faq8a.setLayoutParams(params);
                        open8 = true;
                    } else {
                        LayoutParams params = faq8a.getLayoutParams();
                        params.height = 0;
                        faq8a.setLayoutParams(params);
                        open8 = false;
                    }
                }
            }
        };
        faq8.setOnClickListener(clickListener8);

        faq9 = (TextView) findViewById(R.id.faq9);
    }
}
