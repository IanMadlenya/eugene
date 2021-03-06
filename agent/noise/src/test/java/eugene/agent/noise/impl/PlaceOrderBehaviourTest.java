/**
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE.txt)
 */
package eugene.agent.noise.impl;

import eugene.market.client.Session;
import eugene.market.client.TopOfBookApplication;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

/**
 * Tests {@link PlaceOrderBehaviour}.
 *
 * @author Jakub D Kozlowski
 * @since 0.5
 */
public class PlaceOrderBehaviourTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void testConstructorNullOrderBook() {
        new PlaceOrderBehaviour(null, mock(Session.class));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testConstructorNullSession() {
        new PlaceOrderBehaviour(mock(TopOfBookApplication.class), null);
    }
}
