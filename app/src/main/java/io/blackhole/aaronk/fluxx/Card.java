package io.blackhole.aaronk.fluxx;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by AaronK on 2018-01-15.
 */

abstract class Card implements Comparable<Card> {
    public String name;
    public CardType type;
    CardView view;

    Card(Context context, String name, CardType type) {
        super();
        this.name = name;
        this.type = type;
        this.view = new CardView(context, name, type);
    }

    @Override
    public int compareTo(@NonNull Card other) {
        int typeCompare = this.type.compareTo(other.type);
        if (typeCompare != 0)
            return typeCompare;
        else
            return this.name.compareTo(other.name);
    }

    public enum CardType {
        GOAL, KEEPER, RULE
    }
}
