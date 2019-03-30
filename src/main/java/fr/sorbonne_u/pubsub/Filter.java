package fr.sorbonne_u.pubsub;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Filter {
    Message message;

    public enum Operator
    {
        LEASS("<") {
            @Override public Boolean apply(Integer val1, Integer val2) {
                return val1 < val2;
            }
        },
        GREATER(">") {
            @Override public Boolean apply(Integer val1, Integer val2) {
                return val1 > val2;
            }
        },
        EQUAL("==") {
            @Override public Boolean apply(Integer val1, Integer val2) {
                return val1 == val2;
            }
        },
        LEASSEQUAK("<=") {
            @Override public Boolean apply(Integer val1, Integer val2) {
                return val1 <= val2;
            }
        },
        GREATEREQUAL(">=") {
            @Override public Boolean apply(Integer val1, Integer val2) {
                return val1 >= val2;
            }
        },
        NOTEQUAL("!=") {
            @Override public Boolean apply(Integer val1, Integer val2) {
                return val1 != val2;
            }
        };

        private final String text;

        private Operator(String text) {
            this.text = text;
        }

        public abstract Boolean apply(Integer val1, Integer val2);

        @Override public String toString() {
            return text;
        }
    }

    public Filter(Message message){
        this.message = message;
    }

    public static Predicate<Message> filterByInteger(Operator op, String keyFilter, Integer filter) {
        return message -> op.apply((Integer) message.getProperties().get(keyFilter), filter);
    }


    public static Predicate<Message> filterByString(String keyFilter, String filter) {
        return message -> message.getProperties().get(keyFilter).equals(filter);
    }


    public static List<Message> filterMessages(List<Message> messages,
                                                 Predicate<Message> predicate){
        return messages.stream()
                .filter(predicate)
                .collect(Collectors.<Message>toList());
    }
}
