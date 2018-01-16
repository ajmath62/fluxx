package io.blackhole.aaronk.fluxx;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.TextView;

/**
 * Created by AaronK on 2018-01-15.
 */

class Card implements Comparable<Card> {
    public String name;
    public TextView view;

    Card(Context context, String name) {
        super();
        this.name = name;
        this.view = new TextView(context);
        this.view.setText(this.name);
        this.view.setTextSize(30);
    }

    @Override
    public int compareTo(Card other) {
        return this.name.compareTo(other.name);
    }
}
