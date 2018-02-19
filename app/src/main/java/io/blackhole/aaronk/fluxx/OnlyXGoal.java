package io.blackhole.aaronk.fluxx;

import android.content.Context;

import java.util.Set;

/**
 * Created by AaronK on 2018-02-19.
 */

class OnlyXGoal extends Goal {
    private String neededKeeperName;

    OnlyXGoal(Context context, String name, String neededKeeperName) {
        super(context, name);
        this.neededKeeperName = neededKeeperName;
    }

    boolean isSatisfied(Set<Keeper> availableKeepers, int handSize) {
        // The goal is only satisfied if there is only one keeper and that keeper is named
        // neededKeeperName. Since sets don't support indexing, and since this is a set of Keepers,
        // not Strings, we make an iterator and check the first element.
        return availableKeepers.size() == 1
                && availableKeepers.iterator().next().name.equals(neededKeeperName);
    }
}
