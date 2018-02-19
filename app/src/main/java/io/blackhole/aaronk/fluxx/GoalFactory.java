package io.blackhole.aaronk.fluxx;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by AaronK on 2018-02-19.
 */

class GoalFactory {

    @NonNull
    static Goal getGoal(Context context, String description) {
        String[] goalDescription = description.split("~");
        String[] nameAndType = goalDescription[0].split("#");
        if (nameAndType.length == 1) {  // no type specified, we assume BasicGoal
            String name = nameAndType[0];
            String[] goalDescriptionTail = Arrays.copyOfRange(
                    goalDescription, 1, goalDescription.length
            );
            // AJK TODO check why I'm using HashSet (here and elsewhere)
            Set<String> requiredKeeperNames = new HashSet<>(Arrays.asList(goalDescriptionTail));
            return new BasicGoal(context, name, requiredKeeperNames);
        } else {
            String name = nameAndType[0];
            String type = nameAndType[1];
            switch (type) {
                case "WITHOUT":
                    return new XNoYGoal(context, name, goalDescription[1], goalDescription[2]);
                case "ONLY":
                    return new OnlyXGoal(context, name, goalDescription[1]);
                default:
                    return new CardCountGoal(context, name, type,
                            Integer.valueOf(goalDescription[1]));
            }
        }
    }
}
