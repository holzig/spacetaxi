package cma.otto.spacetaxi;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RouteTest {

    @Test
    public void testFindLoop() {
        List<Highway> highways = Arrays.asList(new Highway("A", "B", 1), new Highway("B", "C", 1), new Highway("B", "A", 1), new Highway("C", "B", 1));
        assertThat(new RouteParser(highways).parse("A -> B -> A -> B").containsLoop()).isTrue();
    }

    @Test
    public void testFindLoop2() {
        List<Highway> highways = Arrays.asList(
                new Highway("A", "B", 1),
                new Highway("B", "C", 1),
                new Highway("B", "A", 1),
                new Highway("C", "B", 1)
        );
        assertThat(new RouteParser(highways).parse("A -> B -> A").containsLoop()).isFalse();
    }

    @Test
    public void testFindLoop3() {
        List<Highway> highways = Arrays.asList(
                new Highway("A", "B", 1),
                new Highway("B", "C", 1),
                new Highway("B", "A", 1),
                new Highway("C", "B", 1)
        );
        assertThat(new RouteParser(highways).parse("A -> B -> C -> B -> A -> B").containsLoop()).isTrue();
    }
}
