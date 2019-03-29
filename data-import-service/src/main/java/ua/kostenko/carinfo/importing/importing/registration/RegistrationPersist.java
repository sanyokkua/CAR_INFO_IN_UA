package ua.kostenko.carinfo.importing.importing.registration;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ua.kostenko.carinfo.importing.importing.Persist;
import ua.kostenko.carinfo.importing.csv.pojo.Registration;

@Slf4j
public class RegistrationPersist implements Persist<Registration> {

    @Override
    public void persist(@NonNull Registration record) {
        log.info("persist: Thread N: {}", Thread.currentThread().getId());

         log.info("persist: Record: {}", record.toString());
        /*
        String carModel = StringUtils.trim(record.get(CAR_MODEL.fieldName));
            String carBrand = StringUtils.trim(record.get(CAR_BRAND.fieldName));
            if (carBrand.contains(carModel)) {
                carBrand = StringUtils.remove(carBrand, carModel);
                carBrand = StringUtils.trim(carBrand);
            }
        * */
    }
}
