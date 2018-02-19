package io.blackhole.aaronk.fluxx;

import android.content.Context;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by AaronK on 2018-02-19.
 */

class GoalFactory {

    static Goal getGoal(Context context, String description) {
        // AJK TODO for now, assume BasicGoal no matter what
        String[] goalDescription = description.split("~");
        String name = goalDescription[0];
        String[] goalDescriptionTail = Arrays.copyOfRange(goalDescription, 1, goalDescription.length);
        // AJK TODO check why I'm using HashSet (here and elsewhere)
        Set<String> requiredKeeperNames = new HashSet<>(Arrays.asList(goalDescriptionTail));
        return new BasicGoal(context, name, requiredKeeperNames);
    }
}
