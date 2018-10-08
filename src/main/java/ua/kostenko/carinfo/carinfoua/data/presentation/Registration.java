package ua.kostenko.carinfo.carinfoua.data.presentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Registration {
    private PersonKind person; // e.g. P // P (person) or J (juridical)
    private String administrativeObjectTypeName;
    private String administrativeObjectName;
    private String operationName; // e.g. 440 - ПЕРЕРЕЄСТРАЦIЯ ПРИ ВТРАТІ СВIДОЦТВА ПРО РЕЄСТРАЦIЮ
    private String registrationDate; // date of operation  yyy-mm-dd 2018-01-01
    private String newRegistrationNumber;
    private String registrationNumberRegionName;

    public enum PersonKind {
        PERSON("P"),
        JURIDICAL("J");

        private final String key;

        PersonKind(String key) {
            this.key = key;
        }

        public static PersonKind getPersonKind(String person) {
            switch (person) {
                case "P":
                    return PERSON;
                case "J":
                    return JURIDICAL;
                default:
                    return PERSON;
            }
        }

        public String getKey() {
            return key;
        }
    }
}
