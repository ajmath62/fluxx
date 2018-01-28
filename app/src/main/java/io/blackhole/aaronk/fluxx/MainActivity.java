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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    private List<Card> deck = new ArrayList<>();
    private Set<Card> hand = new TreeSet<>();
    private Set<Keeper> keepers = new TreeSet<>();
    private Map<Rule.RuleType, Rule> rules = new HashMap<>();
    private Goal currentGoal;
    private GameState gameState;
    private boolean gameOver;
    private int cardsDrawn;
    private int cardsPlayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void newGame(View view) {
        gameState = GameState.START_GAME;
        gameOver = false;
        processGameState();
    }

    public void playCard(Card card) {
        if (gameState != GameState.PLAY)
            return;
        if (!hand.contains(card))
            return;
        Log.d("MainActivity", "Playing " + card.name);
        if (card instanceof Keeper) {
            hand.remove(card);
            keepers.add((Keeper) card);
        }
        else if (card instanceof Goal) {
            // AJK TODO put currentGoal (if any) in the discard pile
            hand.remove(card);
            currentGoal = (Goal) card;
        }
        else if (card instanceof Rule) {
            hand.remove(card);
            Rule cardAsRule = (Rule) card;
            Rule.RuleType ruleType = cardAsRule.ruleType;
            // AJK TODO put previous rule (if any) in the discard pile
            rules.put(ruleType, cardAsRule);
        }
        cardsPlayed ++;

        gameState = GameState.END_PLAY;
        processGameState();
    }

    private void drawCard() {
        if (deck.isEmpty()) {
            deck.addAll(hand);
            hand.clear();
        }
        Card card = deck.remove(deck.size() - 1);
        hand.add(card);
        cardsDrawn ++;
    }

    private void processGameState() {
        // AJK TODO think hard about other ways to do this than recursing on gameState
        if (gameState == GameState.START_GAME) {
            // Remove all cards from the game
            currentGoal = null;
            deck.clear();
            hand.clear();
            keepers.clear();
            rules.clear();

            // Add new cards to the deck
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

            String[] ruleList = res.getStringArray(R.array.rules);
            for (String r: ruleList) {
                String[] ruleDescription = r.split("~");
                String name = ruleDescription[0];
                Rule.RuleType type = Rule.RuleType.valueOf(ruleDescription[1]);
                int value = Integer.valueOf(ruleDescription[2]);
                deck.add(new Rule(this, name, type, value));
            }

            // Shuffle the deck
            Collections.shuffle(deck);

            // Draw up a three-card hand
            for (int i = 0; i < 3; i ++)
                drawCard();

            gameState = GameState.START_TURN;
            processGameState();
        }
        else if (gameState == GameState.START_TURN) {
            cardsDrawn = 0;
            cardsPlayed = 0;
            gameState = GameState.START_PLAY;
            processGameState();
        }
        else if (gameState == GameState.START_PLAY) {
            int cardsToDraw = getCardsLeftToDraw();
            for (int i = 0; i < cardsToDraw; i ++)
                drawCard();
            gameState = GameState.PLAY;
        }
        else if (gameState == GameState.PLAY) {
            // Nothing to do here, just wait until a card is chosen
        }
        else if (gameState == GameState.END_PLAY) {
            if (isGoalSatisfied())
                gameState = GameState.END_GAME;
            else if (getCardsLeftToPlay() > 0 && !hand.isEmpty())
                gameState = GameState.START_PLAY;
            else
                gameState = GameState.END_TURN;
            processGameState();
        }
        else if (gameState == GameState.END_TURN) {
            gameState = GameState.START_TURN;
            processGameState();
        }
        else if (gameState == GameState.END_GAME) {
            gameOver = true;
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
        }

        View youWin = findViewById(R.id.youWin);
        if (gameOver)
            youWin.setVisibility(View.VISIBLE);
        else
            youWin.setVisibility(View.INVISIBLE);
    }

    private boolean isGoalSatisfied() {
        return currentGoal != null && currentGoal.isSatisfied(keepers);
    }

    private int getCardsLeftToDraw() {
        return getTotalCardsToDraw() - cardsDrawn;
    }

    private int getCardsLeftToPlay() {
        return getTotalCardsToPlay() - cardsPlayed;
    }

    private int getTotalCardsToDraw() {
        if (rules.containsKey(Rule.RuleType.DRAW))
            return rules.get(Rule.RuleType.DRAW).ruleValue;
        else
            return 1;
    }

    private int getTotalCardsToPlay() {
        if (rules.containsKey(Rule.RuleType.PLAY))
            return rules.get(Rule.RuleType.PLAY).ruleValue;
        else
            return 1;
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

    private enum GameState {
        START_GAME, START_TURN, START_PLAY, PLAY, END_PLAY, END_TURN, END_GAME
    }
}
