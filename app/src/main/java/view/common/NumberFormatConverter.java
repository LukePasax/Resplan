package view.common;

import javafx.scene.control.TextFormatter;
import static javafx.scene.control.TextFormatter.Change;
import javafx.util.StringConverter;

import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;

public class NumberFormatConverter extends StringConverter<Number> {

    private final UnaryOperator<Change> formatterUnaryOperator;

    public NumberFormatConverter() {
        this.formatterUnaryOperator = change -> {
            String text = change.getText();

            if (text.matches("[\\d:]*")) {
                return change;
            }

            return null;
        };
    }

    @Override
    public String toString(Number milliseconds) {
        Long min = TimeUnit.MILLISECONDS.toMinutes(milliseconds.longValue());
        long sec = TimeUnit.MILLISECONDS.toSeconds(milliseconds.longValue());
        Long ms = milliseconds.longValue()-TimeUnit.SECONDS.toMillis(sec);
        sec -= TimeUnit.MINUTES.toSeconds(min);
        if(ms.equals(0L)) {
            return String.format("%02d", min) + ":" + String.format("%02d", sec);
        }
        return String.format("%02d", min) + ":" + String.format("%02d", sec) + "(+" + ms + "ms)";
    }

    @Override
    public Number fromString(String string) {
        String[] s = string.split(":");
        long min = s.length >= 1 ?
                Long.decode(s[0].replaceFirst("^0*", "").isEmpty() ?
                        "0" : s[0].replaceFirst("^0*", "")) : 0;
        long sec = s.length >= 2 ?
                Long.decode(s[1].replaceFirst("^0*", "").isEmpty() ?
                        "0" : s[1].replaceFirst("^0*", "")) : 0;
        long ms = s.length >= 3 ?
                Long.decode(s[2].replaceFirst("^0*", "").isEmpty() ?
                        "0" : s[2].replaceFirst("^0*", "")) : 0;
        return TimeUnit.SECONDS.toMillis(
                TimeUnit.MINUTES.toSeconds(min) + sec) + ms;
    }

    public UnaryOperator<Change> getFormatterUnaryOperator() {
        return formatterUnaryOperator;
    }
}
