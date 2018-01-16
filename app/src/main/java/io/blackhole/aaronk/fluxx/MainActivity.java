package io.blackhole.aaronk.fluxx;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    private List<Card> deck = new ArrayList<>();
    private Set<Card> hand = new TreeSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources res = getResources();
        String[] keeperList = res.getStringArray(R.array.keepers);
        for (String k: keeperList) { // forEach
            deck.add(new Card(this, k));
        }
    }

    public void drawCard(View view) {
        if (deck.isEmpty()) {
            deck.addAll(hand);
            hand.clear();
        }
        Card card = deck.remove(deck.size() - 1);
        hand.add(card);
        update();
    }

    private void update() {
        LinearLayout handView = findViewById(R.id.handView);
        handView.removeAllViews();
        for (Card c: hand) {
            handView.addView(c.view);
        }
    }
}
