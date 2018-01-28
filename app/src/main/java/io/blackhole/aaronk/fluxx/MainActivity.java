package io.blackhole.aaronk.fluxx;

import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    private List<Card> deck = new ArrayList<>();
    private Set<Card> hand = new TreeSet<>();
    private Set<Keeper> keepers = new TreeSet<>();
    private Goal currentGoal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources res = getResources();
        String[] keeperList = res.getStringArray(R.array.keepers);
        for (String k: keeperList) {
            deck.add(new Keeper(this, k));
        }

        String[] goalList = res.getStringArray(R.array.goals);
        for (String g: goalList) {
            String[] goalDescription = g.split("~");
            String name = goalDescription[0];
            String[] goalDescriptionTail = Arrays.copyOfRange(goalDescription, 1, goalDescription.length);
            // AJK TODO check why I'm using HashSet (here and elsewhere)
            Set<String> requiredKeeperNames = new HashSet<>(Arrays.asList(goalDescriptionTail));
            deck.add(new Goal(this, name, requiredKeeperNames));
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
        if (card instanceof Keeper) {
            hand.remove(card);
            keepers.add((Keeper) card);
        }
        else if (card instanceof Goal) {
            // AJK TODO put currentGoal (if any) in the discard pile
            hand.remove(card);
            this.currentGoal = (Goal) card;
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

        ConstraintLayout currentGoalView = findViewById(R.id.currentGoalHolder);
        currentGoalView.removeAllViews();
        if (currentGoal != null) {
            currentGoalView.addView(currentGoal.view);
            if (currentGoal.isSatisfied(keepers)) {
                View youWin = findViewById(R.id.youWin);
                youWin.setVisibility(View.VISIBLE);
            }
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
