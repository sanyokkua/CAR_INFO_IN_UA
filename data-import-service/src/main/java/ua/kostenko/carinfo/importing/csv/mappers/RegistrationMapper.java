package ua.kostenko.carinfo.importing.csv.mappers;

import lombok.NonNull;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import ua.kostenko.carinfo.importing.csv.pojo.RegistrationRecord;
import ua.kostenko.carinfo.importing.csv.structure.RegistrationHeaders;

public class RegistrationMapper implements Mapper<RegistrationRecord> {
    private final RegistrationHeaders headers;

    public RegistrationMapper(@NonNull RegistrationHeaders headers) {
        this.headers = headers;
    }

    @Override
    public RegistrationRecord map(CSVRecord csvRecord) {
        return RegistrationRecord.builder()
                                 .PERSON(csvRecord.get(headers.getBODY()))
                                 .REG_ADDR_KOATUU(getLong(csvRecord.get(headers.getREG_ADDR_KOATUU())))
                                 .OPER_CODE(getLong(csvRecord.get(headers.getOPER_CODE())))
                                 .OPER_NAME(csvRecord.get(headers.getOPER_NAME()))
                                 .D_REG(csvRecord.get(headers.getD_REG()))
                                 .DEP_CODE(getLong(csvRecord.get(headers.getDEP_CODE())))
                                 .DEP(csvRecord.get(headers.getDEP()))
                                 .BRAND(csvRecord.get(headers.getBRAND()))
                                 .MODEL(csvRecord.get(headers.getMODEL()))
                                 .MAKE_YEAR(getLong(csvRecord.get(headers.getMAKE_YEAR())))
                                 .COLOR(csvRecord.get(headers.getCOLOR()))
                                 .KIND(csvRecord.get(headers.getKIND()))
                                 .BODY(csvRecord.get(headers.getBODY()))
                                 .PURPOSE(csvRecord.get(headers.getPURPOSE()))
                                 .FUEL(csvRecord.get(headers.getFUEL()))
                                 .CAPACITY(getLong(csvRecord.get(headers.getCAPACITY())))
                                 .OWN_WEIGHT(getLong(csvRecord.get(headers.getOWN_WEIGHT())))
                                 .TOTAL_WEIGHT(getLong(csvRecord.get(headers.getTOTAL_WEIGHT())))
                                 .N_REG_NEW(csvRecord.get(headers.getN_REG_NEW()))
                                 .build();
    }

    private Long getLong(String number) {
        Long result = null;
        try {
            result = Long.valueOf(StringUtils.trim(number));
        } catch (Exception ex) {
            //ignore
        }
        return result;
    }
}
