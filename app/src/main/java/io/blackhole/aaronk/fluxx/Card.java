package io.blackhole.aaronk.fluxx;

import android.content.Context;

/**
 * Created by AaronK on 2018-01-15.
 */

class Card implements Comparable<Card> {
    public String name;
    CardView view;

    Card(Context context, String name) {
        super();
        this.name = name;
        this.view = new CardView(context, name, 0);
    }

    @Override
    public int compareTo(Card other) {
        return this.name.compareTo(other.name);
    }
}
