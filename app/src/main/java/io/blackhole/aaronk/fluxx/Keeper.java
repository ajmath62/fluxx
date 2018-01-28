package io.blackhole.aaronk.fluxx;

import android.content.Context;

/**
 * Created by AaronK on 2018-01-27.
 */

public class Keeper extends Card {
    public Keeper(Context context, String name) {
        // AJK TODO Make the 0/1 here be referred to as "Keeper" and "Goal", like an enum
        super(context, name, 0);
    }
}
