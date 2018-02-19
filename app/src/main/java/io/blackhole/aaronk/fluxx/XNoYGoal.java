package io.blackhole.aaronk.fluxx;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by AaronK on 2018-02-19.
 */

class XNoYGoal extends Goal {
    private String neededKeeperName;
    private String forbiddenKeeperName;

    XNoYGoal(Context context, String name, String neededKeeperName, String forbiddenKeeperName) {
        super(context, name);
        this.neededKeeperName = neededKeeperName;
        this.forbiddenKeeperName = forbiddenKeeperName;
    }

    boolean isSatisfied(Set<Keeper> availableKeepers, int handSize) {
        // Make a set of the available keepers' names
        Set<String> availableKeeperNames = new HashSet<>();
        for (Keeper keeper: availableKeepers)
            availableKeeperNames.add(keeper.name);

        // The goal is only satisfied if the needed keeper is in the set and the
        // forbidden keeper is not.
        // AJK TODO only if NOBODY has the forbidden keeper
        return availableKeeperNames.contains(neededKeeperName)
                && !availableKeeperNames.contains(forbiddenKeeperName);
    }
}
