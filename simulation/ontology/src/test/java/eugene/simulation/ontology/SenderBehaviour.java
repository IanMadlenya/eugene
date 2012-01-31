package eugene.simulation.ontology;

import eugene.market.ontology.Defaults;
import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility {@link Behaviour} for testing {@link SimulationOntology}.
 *
 * @author Jakub D Kozlowski
 * @since 0.2
 */
public class SenderBehaviour extends OneShotBehaviour {

    public final Set<AgentAction> sent = new HashSet<AgentAction>();

    public final Set<AgentAction> failed = new HashSet<AgentAction>();

    private final Set<AgentAction> toSend;

    public SenderBehaviour(final Set<AgentAction> toSend) {
        this.toSend = toSend;
    }

    @Override
    public void action() {
        for (AgentAction msg : toSend) {
            try {
                final Action a = new Action(new AID(Defaults.RECEIVER_AGENT, AID.ISLOCALNAME), msg);
                final ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
                aclMessage.addReceiver(new AID(Defaults.RECEIVER_AGENT, AID.ISLOCALNAME));
                aclMessage.setOntology(SimulationOntology.getInstance().getName());
                aclMessage.setLanguage(SimulationOntology.LANGUAGE);
                myAgent.getContentManager().fillContent(aclMessage, a);
                myAgent.send(aclMessage);
                sent.add(msg);
            }
            catch (Exception e1) {
                failed.add(msg);
            }
        }
    }
}
