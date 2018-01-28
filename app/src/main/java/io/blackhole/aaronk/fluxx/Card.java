package io.blackhole.aaronk.fluxx;

import android.content.Context;

/**
 * Created by AaronK on 2018-01-15.
 */

// AJK TODO maybe make this abstract/an interface
class Card implements Comparable<Card> {
    public String name;
    public int type;
    CardView view;

    Card(Context context, String name, int type) {
        super();
        this.name = name;
        this.type = type;
        this.view = new CardView(context, name, type);
    }

    @Override
    public int compareTo(Card other) {
        int typeCompare = (this.type - other.type);
        if (typeCompare != 0)
            return typeCompare;
        else
            return this.name.compareTo(other.name);
    }
}
