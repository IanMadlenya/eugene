package eugene.market.esma.impl;

import eugene.market.book.Order;
import eugene.market.esma.impl.execution.ExecutionEngine;
import eugene.market.ontology.field.enums.OrdType;
import eugene.market.ontology.field.enums.Side;
import eugene.market.ontology.message.NewOrderSingle;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility methods for dealing with {@link Order}s.
 *
 * @author Jakub D Kozlowski
 * @since 0.2
 */
public final class Orders {

    /**
     * Gets an instance of {@link Order} constructed from this {@link NewOrderSingle}.
     *
     * @param newOrderSingle instance of {@link NewOrderSingle} to initialize an {@link Order} with.
     *
     * @return {@link Order} initialized from this {@link NewOrderSingle}.
     */
    public static Order newOrder(final ExecutionEngine executionEngine, final NewOrderSingle newOrderSingle) throws
                                                                                                             NewOrderSingleValidationException {

        try {
            checkNotNull(executionEngine);
            checkNotNull(newOrderSingle);

            return new Order(executionEngine.getOrderID(), OrdType.getOrdType(newOrderSingle),
                             Side.getSide(newOrderSingle), newOrderSingle.getOrderQty().getValue(),
                             (null == newOrderSingle.getPrice() || null == newOrderSingle.getPrice().getValue()) ?
                                     Order.NO_PRICE :
                                     newOrderSingle.getPrice().getValue());
        }
        catch (Exception e) {
            throw new NewOrderSingleValidationException(e);
        }
    }

    /**
     * Indicates that {@link NewOrderSingle} message was formed incorrectly.
     */
    public static class NewOrderSingleValidationException extends Exception {

        public NewOrderSingleValidationException(final Throwable throwable) {
            super("NewOrderSingle message was formed incorrectly", throwable);
        }
    }
}