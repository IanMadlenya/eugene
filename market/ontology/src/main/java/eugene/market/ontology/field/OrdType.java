package eugene.market.ontology.field;

import eugene.market.ontology.Field;
import jade.content.onto.annotations.Element;
import jade.content.onto.annotations.Slot;

/**
 * Order type.
 *
 * @author Jakub D Kozlowski
 * @since 0.2
 */
@Element(name = OrdType.TAG)
public final class OrdType extends Field<String> {

    public static final String TAG = "40";

    public static final Integer TAGi = 40;

    public static final String MARKET = "1";

    public static final String LIMIT = "2";

    public OrdType(String value) {
        super(value);
    }

    @Override
    @Slot(mandatory = true, permittedValues = { LIMIT, MARKET })
    public String getValue() {
        return super.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getTag() {
        return TAGi;
    }
}