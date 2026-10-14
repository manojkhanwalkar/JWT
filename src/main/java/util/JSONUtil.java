/*
 * JSONUtil
 *
 * One shared Jackson ObjectMapper with helpers that convert objects to and from
 * JSON. Clients use it to build request bodies and parse responses, and services
 * use it for nested JSON payloads.
 *
 * ObjectMapper is thread-safe once configured, so a single static instance is shared.
 * Polymorphic "default typing" has been turned off on purpose: it was not needed
 * by any payload, and it is a known remote-code-execution vector when untrusted
 * JSON is deserialized.
 */
package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.UncheckedIOException;

public final class JSONUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    private static final ObjectWriter PRETTY_WRITER = MAPPER.writerWithDefaultPrettyPrinter();

    private JSONUtil() {
    }

    /**
     * Converts an object to a compact JSON string.
     *
     * @throws UncheckedIOException if the object cannot be serialized
     */
    public static String toJSON(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Parses a JSON string into an instance of {@code clazz}.
     *
     * @throws UncheckedIOException if the JSON is malformed or does not match the type
     */
    public static <T> T fromJSON(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Converts an object to indented, human-readable JSON (for logging and debugging).
     *
     * @throws UncheckedIOException if the object cannot be serialized
     */
    public static String toPrettyJSON(Object object) {
        try {
            return PRETTY_WRITER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }
}
