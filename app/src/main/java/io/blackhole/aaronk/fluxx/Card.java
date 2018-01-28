package io.blackhole.aaronk.fluxx;

import android.content.Context;

/**
 * Created by AaronK on 2018-01-15.
 */

class Card implements Comparable<Card> {
    public String name;
    public int type = 0;
    CardView view;

    Card(Context context, String name) {
        super();
        this.name = name;
        this.view = new CardView(context, name, this.type);
    }

    @Override
    public int compareTo(Card other) {
        return this.name.compareTo(other.name);
    }
}
