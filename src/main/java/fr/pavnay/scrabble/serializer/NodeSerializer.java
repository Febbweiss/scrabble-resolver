package fr.pavnay.scrabble.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import fr.pavnay.scrabble.Node;

public class NodeSerializer extends StdSerializer<Node> {
	private static final long serialVersionUID = -5136242177329213286L;

	public NodeSerializer() {
        this(null);
    }
   
    public NodeSerializer(Class<Node> t) {
        super(t);
    }

	@Override
	public void serialize(Node node, JsonGenerator generator, SerializerProvider provider) throws IOException {
		generator.writeStartObject();
		generator.writeObjectField("nodes", node.getNodes());
		generator.writeObjectField("words", node.getWords());
		generator.writeEndObject();
	}
}
