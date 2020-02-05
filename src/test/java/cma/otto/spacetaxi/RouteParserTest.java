package cma.otto.spacetaxi;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RouteParserTest {

    private final Highway a_b = new Highway("A", "B", 1);
    private final Highway b_c = new Highway("B", "C", 2);
    private final Map<Object, List<Highway>> highways = Stream.of(a_b, b_c).collect(groupingBy(highway -> highway.start));
    private final RouteParser routeParser = new RouteParser(highways);

    @Test
    public void testSimpleRoute() {
        assertThat(routeParser.parse("A -> B")).isEqualTo(new Route(a_b));
    }

    @Test
    public void testMultipleSteps() {
        assertThat(routeParser.parse("A -> B -> C")).isEqualTo(new Route(a_b, b_c));
    }

    @Test
    public void testWhitespacesInRoute() {
        assertThat(routeParser.parse("A->   B -> \tC")).isEqualTo(new Route(a_b, b_c));
    }

    @Test
    public void testTooShortRoute() {
        Exception ex = assertThrows(NoSuchRouteException.class, () -> routeParser.parse("A"));
        assertThat(ex).hasMessage("NO SUCH ROUTE");
    }

}
