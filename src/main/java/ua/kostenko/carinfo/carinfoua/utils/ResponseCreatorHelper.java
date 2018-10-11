package ua.kostenko.carinfo.carinfoua.utils;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.AdministrativeObjectEntity;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.RegistrationInformationEntity;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.ServiceCenterEntity;
import ua.kostenko.carinfo.carinfoua.data.persistent.repositories.AdministrativeObjectsCrudRepository;
import ua.kostenko.carinfo.carinfoua.data.persistent.repositories.ServiceCenterCrudRepository;
import ua.kostenko.carinfo.carinfoua.data.presentation.Auto;
import ua.kostenko.carinfo.carinfoua.data.presentation.CombinedInformation;
import ua.kostenko.carinfo.carinfoua.data.presentation.Registration;
import ua.kostenko.carinfo.carinfoua.data.presentation.ServiceCenter;
import ua.kostenko.carinfo.carinfoua.utils.csv.tools.CsvRegionCodeImportTool;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ResponseCreatorHelper {
    private final AdministrativeObjectsCrudRepository administrativeObjectsCrudRepository;
    private final ServiceCenterCrudRepository serviceCenterCrudRepository;
    private final CsvRegionCodeImportTool csvRegionCodeImportTool;

    public ResponseCreatorHelper(AdministrativeObjectsCrudRepository administrativeObjectsCrudRepository, ServiceCenterCrudRepository serviceCenterCrudRepository,
                                 CsvRegionCodeImportTool csvRegionCodeImportTool) {
        Preconditions.checkNotNull(administrativeObjectsCrudRepository);
        Preconditions.checkNotNull(serviceCenterCrudRepository);
        Preconditions.checkNotNull(csvRegionCodeImportTool);
        this.administrativeObjectsCrudRepository = administrativeObjectsCrudRepository;
        this.serviceCenterCrudRepository = serviceCenterCrudRepository;
        this.csvRegionCodeImportTool = csvRegionCodeImportTool;
    }

    public List<CombinedInformation> getCombinedInformation(List<RegistrationInformationEntity> registrationInformationEntityList) {
        return registrationInformationEntityList.stream().map(registrationInformation -> {
            log.info("Mapping RegistrationInformationEntity to CombinedInformation from object: {}", registrationInformationEntityList);
            Auto auto = getAuto(registrationInformation);
            log.info("Created Auto object: {}", auto);
            AdministrativeObjectEntity administrativeObject = administrativeObjectsCrudRepository.findById(registrationInformation.getAdministrativeObjectCode()).orElseGet(null);
            log.info("Found AdministrativeObjectEntity {}", administrativeObject);
            Registration registration = getRegistration(registrationInformation, administrativeObject);
            log.info("Created Registration object: {}", registration);
            ServiceCenterEntity serviceCenterEntity = serviceCenterCrudRepository.findById(registrationInformation.getDepartmentCode()).orElseGet(null);
            log.info("Found ServiceCenterEntity object: {}", serviceCenterEntity);
            ServiceCenter serviceCenter = getServiceCenter(registrationInformation, serviceCenterEntity);
            log.info("Create ServiceCenter object: {}", serviceCenter);
            return new CombinedInformation(auto, registration, serviceCenter);
        }).collect(Collectors.toList());
    }

    private Auto getAuto(RegistrationInformationEntity registrationInformation) {
        return new Auto(registrationInformation.getCarBrand(), registrationInformation.getCarModel(), registrationInformation.getCarMakeYear(), registrationInformation.getCarColor(),
                        registrationInformation.getCarKind(), registrationInformation.getCarBody(), registrationInformation.getCarPurpose(), registrationInformation.getCarFuel(),
                        registrationInformation.getCarEngineCapacity(), registrationInformation.getCarOwnWeight(), registrationInformation.getCarTotalWeight());
    }

    private Registration getRegistration(RegistrationInformationEntity registrationInformation, AdministrativeObjectEntity administrativeObject) {
        return new Registration(Registration.PersonKind.getPersonKind(registrationInformation.getPerson()), administrativeObject.getTypeName(), administrativeObject.getName(),
                                registrationInformation.getOperationName(), registrationInformation.getRegistrationDate(), registrationInformation
                                                             .getCarNewRegistrationNumber(),
                                csvRegionCodeImportTool.getRegion(registrationInformation.getCarNewRegistrationNumber()));
    }

    private ServiceCenter getServiceCenter(RegistrationInformationEntity registrationInformation, ServiceCenterEntity serviceCenterEntity) {
        return new ServiceCenter(serviceCenterEntity.getDepId(), serviceCenterEntity.getAddress(), serviceCenterEntity.getEmail(), registrationInformation
                .getDepartmentName());
    }
}
