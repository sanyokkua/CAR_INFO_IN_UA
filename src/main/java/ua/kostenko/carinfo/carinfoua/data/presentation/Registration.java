package ua.kostenko.carinfo.carinfoua.data.presentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.RegionCodeEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Registration {
    private PersonKind person;
    private String administrativeObjectTypeName;
    private String administrativeObjectName;
    private String operationName;
    private String registrationDate;
    private String newRegistrationNumber;
    private RegionCodeEntity registrationNumberRegionName;

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
    }
}
