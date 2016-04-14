package gaffer.tuple.function.context;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import gaffer.function2.mock.MockTransform;
import gaffer.tuple.impl.MapTuple;
import gaffer.tuple.view.Reference;
import gaffer.tuple.view.View;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotSame;

public class FunctionContextTest {
    private static final ObjectMapper MAPPER = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        return mapper;
    }

    protected String serialise(Object object) throws IOException {
        return MAPPER.writeValueAsString(object);
    }

    protected <T> T deserialise(String json, Class<T> type) throws IOException {
        return MAPPER.readValue(json, type);
    }

    @Test
    public void canSelectAndProject() {
        String outputValue = "O";
        String inputValue = "I";
        MockFunctionContext context = new MockFunctionContext();
        MockTransform mock = new MockTransform(outputValue);
        Reference<String> selection = new Reference("a");
        Reference<String> projection = new Reference("b", "c");

        context.setFunction(mock);
        context.setSelection(selection);
        context.setProjection(projection);

        MapTuple<String> tuple = new MapTuple<>();
        tuple.put("a", inputValue);

        context.project(tuple, context.getFunction().transform(context.select(tuple)));

        assertEquals("Unexpected value at reference a", inputValue, tuple.get("a"));
        assertEquals("Unexpected value at reference b", inputValue, tuple.get("b"));
        assertEquals("Unexpected value at reference c", outputValue, tuple.get("c"));
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        MockFunctionContext context = new MockFunctionContext();
        MockTransform mock = new MockTransform("a");
        Reference<String> selection = new Reference("a");
        Reference<String> projection = new Reference("b", "c");

        context.setFunction(mock);
        context.setSelection(selection);
        context.setProjection(projection);

        String json = serialise(context);
        MockFunctionContext deserialisedContext = deserialise(json, MockFunctionContext.class);

        // check deserialisation
        assertNotNull(deserialisedContext);
        Reference<String> deserialisedSelection = deserialisedContext.getSelection();
        Reference<String> deserialisedProjection = deserialisedContext.getProjection();
        assertNotNull(deserialisedContext.getFunction());
        assertNotSame(mock, deserialisedContext.getFunction());
        assertNotSame(context, deserialisedContext);
        assertNotSame(selection, deserialisedSelection);
        assertNotSame(projection, deserialisedProjection);
        assertTrue(deserialisedSelection.isFieldReference());
        assertEquals("a", deserialisedSelection.getField());
        assertTrue(deserialisedProjection.isTupleReference());
        Reference<String>[] deserialisedProjectionFields = deserialisedProjection.getTupleReferences();
        assertEquals(2, deserialisedProjectionFields.length);
        assertEquals("b", deserialisedProjectionFields[0].getField());
        assertEquals("c", deserialisedProjectionFields[1].getField());
    }
}
