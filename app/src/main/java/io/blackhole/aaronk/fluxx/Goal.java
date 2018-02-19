package io.blackhole.aaronk.fluxx;

import android.content.Context;

import java.util.Set;

/**
 * Created by AaronK on 2018-01-27.
 */

abstract class Goal extends Card {

    Goal(Context context, String name) {
        super(context, name, CardType.GOAL);
    }

    abstract boolean isSatisfied(Set<Keeper> availableKeepers);
}
