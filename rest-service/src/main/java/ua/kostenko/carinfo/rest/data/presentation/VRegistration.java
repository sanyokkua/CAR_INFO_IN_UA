package ua.kostenko.carinfo.rest.data.presentation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.kostenko.carinfo.common.api.records.Registration;
import ua.kostenko.carinfo.rest.data.RegionCodeEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VRegistration {

    private PersonKind person;
    private String administrativeObjectTypeName;
    private String administrativeObjectName;
    private String operationName;
    private String registrationDate;
    private String newRegistrationNumber;
    @Builder.Default
    private RegionCodeEntity registrationNumberRegionName = new RegionCodeEntity("", "");

    public static VRegistration map(Registration registration) {
        return VRegistration.builder()
                .person(PersonKind.getPersonKind(registration.getPersonType()))
                .administrativeObjectTypeName(registration.getAdminObjType())
                .administrativeObjectName(registration.getAdminObjName())
                .operationName(registration.getOperationName())
                .registrationDate(registration.getRegistrationDate().toString())
                .newRegistrationNumber(registration.getRegistrationNumber())
                .build();
    }

    public enum PersonKind {
        PERSON, JURIDICAL;

        static PersonKind getPersonKind(String person) {
            switch (person) {
                case "J" :
                    return JURIDICAL;
                default:
                    return PERSON;
            }
        }
    }
}
