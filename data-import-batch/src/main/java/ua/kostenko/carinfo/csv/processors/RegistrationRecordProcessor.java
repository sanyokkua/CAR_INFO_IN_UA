package ua.kostenko.carinfo.csv.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import ua.kostenko.carinfo.csv.records.RegistrationRecord;

public class RegistrationRecordProcessor implements ItemProcessor<RegistrationRecord, RegistrationRecord> {

    @Override
    public RegistrationRecord process(@NonNull RegistrationRecord registrationRecord) throws Exception {
        return registrationRecord;
    }
}
