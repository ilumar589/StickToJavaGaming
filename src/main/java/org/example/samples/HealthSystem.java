package org.example.samples;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.primitive.IntLists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.ImmutableIntList;
import org.eclipse.collections.api.list.primitive.MutableIntList;

public record HealthSystem(
        ImmutableIntList playerHealthStorage,
        ImmutableList<String> playerNameStorage// not used, just for demo purposes
) {

    // when value objects are here we can remove these lists, creating new value records will have allocations eliminated by the runtime
    private static final MutableIntList playerHealthTempStorage = IntLists.mutable.empty();
    private static final MutableList<String> playerNameTempStorage = Lists.mutable.empty();

    public HealthSystem() {
        this(IntLists.immutable.empty(), Lists.immutable.empty());
    }

    public HealthSystem {
        assert playerHealthStorage.size() == playerNameStorage.size() : "Player health info must be the same length as the storage capacity";
    }

    public HealthSystem changeHealthBy(final int offset) {
        return new HealthSystem(playerHealthStorage
                .collectInt(value -> value + offset, IntLists.mutable.empty())
                .toImmutable(), playerNameStorage);

    }

    public HealthSystem changeHealthByWithError() {
        throw new RuntimeException();
    }

    // not thread safe
    public HealthSystem addPlayer(String name, int health) {
        playerNameTempStorage.add(name);
        playerHealthTempStorage.add(health);

        return this;
    }

    public HealthSystem build() {
        final var system = new HealthSystem(playerHealthTempStorage.toImmutable(), playerNameTempStorage.toImmutable());
        playerHealthTempStorage.clear();
        playerNameTempStorage.clear();

        return system;
    }
}
