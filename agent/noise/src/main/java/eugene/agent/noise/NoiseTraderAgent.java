package eugene.agent.noise;

import eugene.agent.noise.impl.PlaceOrderBehaviour;
import eugene.market.book.OrderBook;
import eugene.market.client.ApplicationAdapter;
import eugene.market.client.Session;
import eugene.simulation.agent.Simulation;
import eugene.simulation.ontology.Start;
import eugene.simulation.ontology.Stop;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static eugene.market.book.OrderBooks.defaultOrderBook;
import static eugene.market.client.Applications.orderBookApplication;
import static eugene.market.client.Applications.proxy;
import static eugene.market.client.Sessions.initiate;

/**
 * Implements the Noise Trader Agent.
 *
 * @author Jakub D Kozlowski
 * @since 0.5
 */
public class NoiseTraderAgent extends Agent {

    @Override
    public void setup() {

        checkNotNull(getArguments());
        checkArgument(getArguments().length == 1);
        checkArgument(getArguments()[0] instanceof Simulation);

        final Simulation simulation = (Simulation) getArguments()[0];

        final OrderBook orderBook = defaultOrderBook();
        addBehaviour(
                initiate(
                        proxy(
                                orderBookApplication(orderBook),
                                new ApplicationAdapter() {
                                    
                                    private Behaviour b = null;
                                    
                                    @Override
                                    public void onStart(final Start start, final Agent agent, final Session session) {
                                        b = new PlaceOrderBehaviour(orderBook, session);
                                        agent.addBehaviour(b);
                                    }
                                    
                                    @Override
                                    public void onStop(final Stop stop, final Agent agent, final Session session) {
                                        if (null != b) {
                                            agent.removeBehaviour(b);
                                        }
                                    }
                                }
                        ),
                        simulation));
    }
}
