/*
 * LogText
 *
 * Makes client-supplied values safe to put in a log line.
 */
package util;

public final class LogText {

    private LogText() {
    }

    /** Strips CR/LF/tab and caps the length so a client value cannot forge log lines. */
    public static String sanitize(String value) {
        if (value == null) {
            return "null";
        }
        String clean = value.replaceAll("[\\r\\n\\t]", "_");
        return clean.length() > 64 ? clean.substring(0, 64) + "..." : clean;
    }
}
