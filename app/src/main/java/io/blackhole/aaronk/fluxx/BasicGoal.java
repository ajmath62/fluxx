package io.blackhole.aaronk.fluxx;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by AaronK on 2018-02-19.
 */

class BasicGoal extends Goal {
    private Set<String> requiredKeeperNames;

    BasicGoal(Context context, String name, Set<String> requiredKeeperNames) {
        super(context, name);
        this.requiredKeeperNames = requiredKeeperNames;
    }

    boolean isSatisfied(Set<Keeper> availableKeepers, int handSize) {
        // Make a set of the available keepers' names
        Set<String> availableKeeperNames = new HashSet<>();
        for (Keeper keeper: availableKeepers)
            availableKeeperNames.add(keeper.name);

        // If any of the required keepers isn't in that set, the goal is not satisfied
        for (String name: requiredKeeperNames)
            if (!availableKeeperNames.contains(name))
                return false;

        // If they all are in that set, the goal is satisfied
        return true;
    }
}
