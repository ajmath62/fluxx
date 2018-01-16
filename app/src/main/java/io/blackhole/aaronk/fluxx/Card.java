package io.blackhole.aaronk.fluxx;

/**
 * Created by AaronK on 2018-01-15.
 */

class Card implements Comparable<Card> {
    public String name;

    Card(String name) {
        super();
        this.name = name;
    }

    @Override
    public int compareTo(Card other) {
        return this.name.compareTo(other.name);
    }
}
