package cma.otto.spacetaxi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RouteTest {

    private RouteParser routeParser;

    @BeforeEach
    void setUp() {
        List<Highway> highways = Arrays.asList(
                new Highway("A", "B", 1),
                new Highway("B", "C", 1),
                new Highway("B", "A", 1),
                new Highway("C", "B", 1)
        );
        routeParser = new RouteParser(highways);
    }

    @Test
    public void testFindLoop() {
        assertThat(routeParser.parse("A -> B -> A -> B").containsLoop()).isTrue();
    }

    @Test
    public void testFindLoop2() {
        assertThat(routeParser.parse("A -> B -> A").containsLoop()).isFalse();
    }

    @Test
    public void testFindLoop3() {
        assertThat(routeParser.parse("A -> B -> C -> B -> A -> B").containsLoop()).isTrue();
    }

    @Test
    public void testHasReachedTarget() {
        assertThat(routeParser.parse("A -> B").hasReached("B")).isTrue();
        assertThat(routeParser.parse("A -> B").hasReached("A")).isFalse();
    }

    @Test
    public void testHasReachedTargetEmptyRoute() {
        assertThat(new Route().hasReached("404")).isFalse();
    }

    /**
     * Test toString because it is used as visible output.
     */
    @Test
    public void testToStringForOutput() {
        assertThat(routeParser.parse("A -> B").toString()).isEqualTo("Route{2 steps: A -> B}(travel time: 1)");
    }
}
