package io.blackhole.aaronk.fluxx;

import android.content.Context;


/**
 * Created by AaronK on 2018-01-28.
 */

public class Rule extends Card {
    RuleType ruleType;
    int ruleValue;

    public Rule(Context context, String name, RuleType ruleType, int ruleValue) {
        super(context, name, CardType.RULE);
        this.ruleValue = ruleValue;
        this.ruleType = ruleType;
    }

    public enum RuleType {
        DRAW, HAND_LIMIT, KEEPER_LIMIT, PLAY
    }
}
