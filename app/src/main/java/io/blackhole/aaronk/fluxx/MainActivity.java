package io.blackhole.aaronk.fluxx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    private Set<Card> hand = new TreeSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        hand.add(new Card("Chocolate"));
        hand.add(new Card("Milk"));

        LinearLayout handView = findViewById(R.id.handView);
        for (Card c: hand) { // forEach
            TextView tv = new TextView(this);
            tv.setTextSize(30);
            tv.setText(c.name);
            handView.addView(tv);
        }
    }
}
