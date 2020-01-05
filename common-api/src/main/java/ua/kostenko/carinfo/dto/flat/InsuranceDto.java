package ua.kostenko.carinfo.dto.flat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsuranceDto {

    private String companyName;
    private String companyStatus;
    private String phoneNumber;
    private String companyEmail;
    private String companyAddress;
    private String carRegistrationNumber;
    private String carVinNumber;
    private String carType;
    private String carBrandAndModel;
}
