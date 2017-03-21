package io.qameta.allure.testrun;

import io.qameta.allure.ResultAggregator;
import io.qameta.allure.entity.TestCase;
import io.qameta.allure.entity.TestCaseResult;
import io.qameta.allure.entity.TestRun;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.qameta.allure.utils.ListUtils.compareBy;
import static io.qameta.allure.utils.ListUtils.computeIfAbsent;

/**
 * @author charlie (Dmitry Baev).
 */
public class TestRunStatisticAggregator implements ResultAggregator<List<TestRunStatistic>> {

    @Override
    public Supplier<List<TestRunStatistic>> supplier(final TestRun testRun, final TestCase testCase) {
        return ArrayList::new;
    }

    @Override
    public Consumer<List<TestRunStatistic>> aggregate(final TestRun testRun,
                                                      final TestCase testCase,
                                                      final TestCaseResult result) {
        return stats -> {
            final Predicate<TestRunStatistic> predicate = compareBy(TestRunStatistic::getUid, testRun::getUid);
            final Supplier<TestRunStatistic> supplier = () ->
                    new TestRunStatistic().withUid(testRun.getUid()).withName(testRun.getName());

            computeIfAbsent(stats, predicate, supplier)
                    .updateStatistic(result);
        };
    }
}
