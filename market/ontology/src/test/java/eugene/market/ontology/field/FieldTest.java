package eugene.market.ontology.field;

import eugene.market.ontology.Field;
import jade.content.onto.annotations.Element;
import jade.content.onto.annotations.Slot;
import org.junit.BeforeClass;
import org.junit.Test;
import quickfix.ConfigError;
import quickfix.DataDictionary;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for inconsistencies in the {@link Field} classes.
 *
 * @author Jakub D Kozlowski
 * @since 0.2
 */
public class FieldTest {

    private static final String FIELDS_PACKAGE = FieldTest.class.getPackage().getName();

    private static final String FIX50XML = "FIX44.xml";

    private static final String GET_VALUE_METHOD = "getValue";

    private static Set<Class<Field<?>>> fields;

    @BeforeClass
    public static void setupDictionary() throws ConfigError {
        System.out.println(FieldTest.class.getClassLoader().getResource(FIX50XML).getPath());
        final DataDictionary dictionary = new DataDictionary(FIX50XML);
        fields = new HashSet<Class<Field<?>>>();
        for (int field : dictionary.getOrderedFields()) {
            final String className = dictionary.getFieldName(field);
            try {
                final Class<Field<?>> fieldClass = (Class<Field<?>>) Class.forName(FIELDS_PACKAGE + "." + className);
                fields.add(fieldClass);
            }
            catch (ClassNotFoundException e) {
            }
        }
    }

    /**
     * Tests whether {@link Field}s correctly implement {@link Field#isEnumField()}.
     */
    @Test
    public void testIsEnum() throws IllegalAccessException, InstantiationException, NoSuchMethodException {
        for (Class<Field<?>> fieldClass : fields) {
            final Field<?> field = fieldClass.newInstance();
            final Method getValueMethod = fieldClass.getMethod(GET_VALUE_METHOD, null);
            if (field.isEnumField()) {
                final Set<String> actualValues = new TreeSet<String>(field.getEnumSet());
                final Set<String> expectedValue =
                        new TreeSet<String>(Arrays.asList(getValueMethod.getAnnotation(Slot.class)
                                                                        .permittedValues()));

                assertFalse(field.getEnumSet().isEmpty());
                assertEquals(actualValues, expectedValue);
            }
            else {
                assertTrue(field.getEnumSet().isEmpty());
                assertEquals(0, getValueMethod.getAnnotation(Slot.class)
                                              .permittedValues().length);
            }
        }
    }

    /**
     * Tests whether {@link Field}s return  same value from {@link Field#getTag()} as is set in {@link Element}
     * annotation.
     */
    @Test
    public void testTag() throws IllegalAccessException, InstantiationException {
        for (Class<Field<?>> fieldClass : fields) {
            final Field<?> field = fieldClass.newInstance();
            assertEquals(fieldClass.getAnnotation(Element.class).name(), field.getTag().toString());
        }
    }

    /**
     * Tests whether {@link Field}s are declared final.
     *
     */
    @Test
    public void testIsFinal() {
        for (Class<Field<?>> fieldClass : fields) {
            assertTrue(Modifier.isFinal(fieldClass.getModifiers()));
        }
    }
}