package io.blackhole.aaronk.fluxx;

import android.content.Context;
import android.util.Log;

import java.util.Set;

/**
 * Created by AaronK on 2018-02-19.
 */

class CardCountGoal extends Goal {
    private int numberRequired;
    private WhichCards whichCards;

    CardCountGoal(Context context, String name, String type, int numberRequired) {
        super(context, name);
        this.numberRequired = numberRequired;
        this.whichCards = WhichCards.valueOf(type);
    }

    boolean isSatisfied(Set<Keeper> availableKeepers, int handSize) {
        Log.d("numberRequired", Integer.toString(numberRequired));
        Log.d("handSize", Integer.toString(handSize));
        Log.d("keeperSize", Integer.toString(availableKeepers.size()));
        switch (whichCards) {
            case HAND:
                return handSize >= numberRequired;
            case KEEPERS:
                return availableKeepers.size() >= numberRequired;
            default:
                return false;
        }
    }

    private enum WhichCards {
        HAND, KEEPERS
    }
}
