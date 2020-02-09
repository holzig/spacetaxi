package cma.otto.spacetaxi;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SmokeTest {

    @Test
    public void testRun() {
        assertThat(App.run(new String[]{"path", "Sirius -> Vega"})).isEqualTo(0);
        assertThat(App.run(new String[]{"path", "Vega -> Sirius"})).isEqualTo(1);
    }
}
