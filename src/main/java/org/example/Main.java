package org.example;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.example.book.engine.Engine;
import org.example.book.engine.WindowOptions;
import org.example.book.game.GameLogic;
import org.example.output.Error;
import org.example.output.Success;
import org.example.samples.HealthSystem;
import org.example.streamUtils.StreamUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public final class Main {
    public static void main(String[] args) {
//        parallelSystemRoot();
//        new GettingStarted();
//        new HelloTriangle();
//        eclipseParallelExample();

        final var gameLogic = new GameLogic();
        try (final var engine = new Engine("test", WindowOptions.getDefault(), gameLogic)) {
            engine.start();
        }
    }

    private static void parallelSystemRoot() {
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            final var playerSystem = new HealthSystem()
                    .addPlayer("J1", 100)
                    .addPlayer("J2", 20)
                    .addPlayer("J3", 87)
                    .addPlayer("J4", 15)
                    .addPlayer("J5", 66)
                    .addPlayer("J6", 11)
                    .build();

            final Callable<HealthSystem> changeHealthByTask1 = () -> playerSystem.changeHealthBy(1);
            final Callable<HealthSystem> changeHealthByTask2 = () -> playerSystem.changeHealthBy(-1);
            final Callable<HealthSystem> changeHealthByTask3 = () -> playerSystem.changeHealthBy(99);
            final Callable<HealthSystem> changeHealthByTask4 = () -> playerSystem.changeHealthBy(-99);
            final Callable<HealthSystem> changeHealthByTask5 = playerSystem::changeHealthByWithError;

            runPlayerSystemInALoop(executor.submit(changeHealthByTask1),
                    executor.submit(changeHealthByTask2),
                    executor.submit(changeHealthByTask3),
                    executor.submit(changeHealthByTask4),
                    executor.submit(changeHealthByTask5));
        }
    }

    @SafeVarargs
    private static void runPlayerSystemInALoop(Future<HealthSystem>... heathSystemTasks) {
        int i = 0;


        do {
            // stupid way, a lot of redundant conversions
//            final var healthSystemStreamResults = Lists.immutable.fromStream(Arrays.stream(heathSystemTasks)
//                    .map(StreamUtils::liftWithValue));

            final var healthSystemStreamResults = Lists.immutable
                    .of(heathSystemTasks)
                    .collect(StreamUtils::liftWithValue);

            healthSystemStreamResults.forEach(result -> {
                if (result instanceof Success(var r)) {
                    System.out.println(r);
                }

                if (result instanceof Error(var errorMessage)) {
                    System.err.println(errorMessage);
                }
            });

            i++;
        } while (i < 10);
    }

    private static void eclipseParallelExample() {
        final var integers = FastList
                .newListWith(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .asUnmodifiable();
        int batchSize = 2;
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            final var parallelListIterable = integers.asParallel(executor, batchSize);
            // deferred evaluation
            final var evenNumbers = parallelListIterable.select(each -> each % 2 == 0);
            // deferred evaluation
            final var evenStrings = evenNumbers.collect(String::valueOf);
            // forced evaluation
            final var strings = evenStrings.toList().asUnmodifiable();

            // these deferred actions can be used in an ecs architecture ?
            System.out.println(strings);
        }
    }
}