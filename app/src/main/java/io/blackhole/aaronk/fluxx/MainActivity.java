package io.blackhole.aaronk.fluxx;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    private List<Card> deck = new ArrayList<>();
    private Set<Card> hand = new TreeSet<>();
    private Set<Card> keepers = new TreeSet<>();

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

    public void playCard(Card card) {
        if (!hand.contains(card)) {
            return;
        }
        Log.d("MainActivity", "Playing " + card.name);
        if (card.type == 0) {
            hand.remove(card);
            keepers.add(card);
        }
        update();
    }

    private void update() {
        LinearLayout handView = findViewById(R.id.handView);
        handView.removeAllViews();
        for (Card c: hand) {
            c.view.setOnClickListener(new CardClick(c));
            handView.addView(c.view);
        }

        LinearLayout keeperView = findViewById(R.id.keeperView);
        keeperView.removeAllViews();
        for (Card c: keepers) {
            keeperView.addView(c.view);
        }
    }

    private class CardClick implements View.OnClickListener {
        private Card card;

        CardClick(Card card) {
            this.card = card;
        }

        @Override
        public void onClick(View view) {
            playCard(card);
        }
    }
}
