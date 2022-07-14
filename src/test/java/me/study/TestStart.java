package me.study;

import me.study.user.dao.UserDao;
import me.study.user.dao.UserDaoTest;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class TestStart {
    /*
    * JUnit 5 이상에서는 자바코드로 테스트 수행을 위해 JUnitCore 가 아니라 junit.platform 라이브러리를 활용한다.
    * https://junit.org/junit5/docs/current/user-guide/#launcher-api
    * */
    public static void main(String[] args) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(
                        selectPackage("me.study"),
                        selectClass(UserDao.class)
                )
                .filters(
                        includeClassNamePatterns(".*Test")
                )
                .build();

        SummaryGeneratingListener listener = new SummaryGeneratingListener();

        try (LauncherSession session = LauncherFactory.openSession()) {
            Launcher launcher = session.getLauncher();
            // Register a listener of your choice
            launcher.registerTestExecutionListeners(listener);
            // Discover tests and build a test plan
            TestPlan testPlan = launcher.discover(request);
            // Execute test plan
            launcher.execute(testPlan);
            // Alternatively, execute the request directly
            // launcher.execute(request);
        }

        TestExecutionSummary summary = listener.getSummary();
        // Do something with the summary...
        printReport(summary);
    }

    /*
    * 대충 구글링해서 긁어 온 메소드
    * https://www.programcreek.com/java-api-examples/?api=org.junit.platform.launcher.listeners.TestExecutionSummary
    * */
    private static void printReport(TestExecutionSummary summary) {
        System.out.println(
                "\n------------------------------------------" +
                        "\nTests started: " + summary.getTestsStartedCount() +
                        "\nTests failed: " + summary.getTestsFailedCount() +
                        "\nTests succeeded: " + summary.getTestsSucceededCount() +
                        "\n------------------------------------------"
        );

        if(summary.getTestsFailedCount() > 0) {
            for(TestExecutionSummary.Failure f: summary.getFailures()){
                System.out.println(f.getTestIdentifier().getSource() +
                        "\nException " + f.getException());
            }
        }
    }
}
