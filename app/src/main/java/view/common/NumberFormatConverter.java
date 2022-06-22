package view.common;

import static javafx.scene.control.TextFormatter.Change;
import javafx.util.StringConverter;

import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;

public final class NumberFormatConverter extends StringConverter<Number> {

    public static final String SECONDS_FORMAT = "%02d";
    public static final String REGEX = "^0*";
    private final UnaryOperator<Change> formatterUnaryOperator;

    public NumberFormatConverter() {
        this.formatterUnaryOperator = change -> {
            final String text = change.getText();

            if (text.matches("[\\d:]*")) {
                return change;
            }

            return null;
        };
    }

    @Override
    public String toString(final Number milliseconds) {
        final Long min = TimeUnit.MILLISECONDS.toMinutes(milliseconds.longValue());
        long sec = TimeUnit.MILLISECONDS.toSeconds(milliseconds.longValue());
        final Long ms = milliseconds.longValue() - TimeUnit.SECONDS.toMillis(sec);
        sec -= TimeUnit.MINUTES.toSeconds(min);
        if (ms.equals(0L)) {
            return String.format(SECONDS_FORMAT, min) + ":" + String.format(SECONDS_FORMAT, sec);
        }
        return String.format(SECONDS_FORMAT, min) + ":" + String.format(SECONDS_FORMAT, sec) + ":" + ms;
    }

    @Override
    public Number fromString(final String string) {
        final String[] s = string.split(":");
        final long min = s.length >= 1
               ? Long.decode(s[0].replaceFirst(REGEX, "").isEmpty()
                       ? "0" : s[0].replaceFirst(REGEX, "")) : 0;
        final long sec = s.length >= 2
               ? Long.decode(s[1].replaceFirst(REGEX, "").isEmpty()
                       ? "0" : s[1].replaceFirst(REGEX, "")) : 0;
        final long ms = s.length >= 3
               ? Long.decode(s[2].replaceFirst(REGEX, "").isEmpty()
                       ? "0" : s[2].replaceFirst(REGEX, "")) : 0;
        return TimeUnit.SECONDS.toMillis(
                TimeUnit.MINUTES.toSeconds(min) + sec) + ms;
    }

    public UnaryOperator<Change> getFormatterUnaryOperator() {
        return formatterUnaryOperator;
    }
}
