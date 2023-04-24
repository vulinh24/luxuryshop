package com.luxuryshop.kafka;

import com.luxuryshop.entities.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class UserLogKafka {

    public enum Action {
        VIEW("view", 2),
        BUY("buy", -1);
        private String label;
        private Integer point;
        private static Map<String, Integer> labelPoint = new HashMap<>();
        private static Map<Action, String> labelAction = new HashMap<>();

        Action(String label, Integer point) {
            this.label = label;
            this.point = point;
        }

        static {
            for (Action e : values()) {
                labelPoint.put(e.label, e.point);
                labelAction.put(e, e.label);
            }
        }

        public static Integer getPointFromLabel(String label) {
            return labelPoint.get(label);
        }

        public static String getLabel(Action action) {
            return labelAction.get(action);
        }

    }

    private Integer productId;
    private Integer ownerId;
    private String action;
}
