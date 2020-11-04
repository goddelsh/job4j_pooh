package broker;


import broker.implementation.PoohBrokerConcurrent;
import broker.structs.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PoohBrokerTest {


    private PoohBroker<User, String, String> poohBroker;

    @Before
    public void prepareBroker() {
        poohBroker = new PoohBrokerConcurrent<>();

        poohBroker.addMessage("tickets", "first ticket");
        poohBroker.addMessage("tickets", "second ticket");
        poohBroker.addMessage("tickets", "third  ticket");
        poohBroker.addMessage("orders", "first order");
        poohBroker.addMessage("orders", "second order");

        poohBroker.addTopic("news", "politics");
        poohBroker.addTopic("news", "economics");
        poohBroker.addTopic("news", "social");
        poohBroker.addTopic("talks", "celebrity");
        poohBroker.addTopic("talks", "technology");
    }

    @Test
    public void testGetMessage() {
        assertThat(poohBroker.getMessage("tickets"), is("first ticket"));
        assertThat(poohBroker.getMessage("tickets"), is("second ticket"));

        assertThat(poohBroker.getMessage("orders"), is("first order"));

        assertThat(poohBroker.getMessage("tickets"), is("third  ticket"));

        assertThat(poohBroker.getMessage("orders"), is("second order"));
    }

    @Test
    public void testGetTopic() {
        User firstUser = new User(){
            @Override
            public String toString() {
                return "first";
            }
        };

        User secondUser = new User(){
            @Override
            public String toString() {
                return "second";
            }
        };

        List<User> users = List.of(firstUser, secondUser);
        ExecutorService pool = Executors.newFixedThreadPool(2);

        for (User user : users) {
            pool.execute(() -> {
                assertThat(poohBroker.getTopic(user, "news"), is("politics"));
                assertThat(poohBroker.getTopic(user, "news"), is("economics"));

                assertThat(poohBroker.getTopic(user, "talks"), is("celebrity"));
                assertThat(poohBroker.getTopic(user, "news"), is("social"));
                assertThat(poohBroker.getTopic(user, "talks"), is("technology"));
            });
        }

        pool.shutdown();
        while (!pool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}