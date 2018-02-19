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

    // AJK TODO once I've implemented a game state (hands, keepers etc.) this should take that
    abstract boolean isSatisfied(Set<Keeper> availableKeepers, int handSize);
}
