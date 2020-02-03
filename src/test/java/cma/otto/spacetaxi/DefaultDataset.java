package cma.otto.spacetaxi;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class DefaultDataset {
    public static final List<Highway> highways = Arrays.asList(
            new Highway("Solar System", "Alpha Centauri", 5),
            new Highway("Alpha Centauri", "Sirius", 4),
            new Highway("Sirius", "Betelgeuse", 8),
            new Highway("Betelgeuse", "Sirius", 8),
            new Highway("Betelgeuse", "Vega", 6),
            new Highway("Solar System", "Betelgeuse", 5),
            new Highway("Sirius", "Vega", 2),
            new Highway("Vega", "Alpha Centauri", 3),
            new Highway("Solar System", "Vega", 7)
    );

    public static final Map<Object, List<Highway>> highwaysByStartsystem = highways.stream().collect(groupingBy(highway -> highway.start));
}