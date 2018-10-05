package ua.kostenko.carinfo.carinfoua.utils;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResponseCreatorHelper {
  private static final Logger LOGGER = LoggerFactory.getLogger(ResponseCreatorHelper.class);
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
      LOGGER.info("Mapping RegistrationInformationEntity to CombinedInformation from object: {}", registrationInformationEntityList);
      Auto auto = new Auto(registrationInformation.getCarBrand(), registrationInformation.getCarModel(), registrationInformation.getCarMakeYear(), registrationInformation.getCarColor(),
                           registrationInformation.getCarKind(), registrationInformation.getCarBody(), registrationInformation.getCarPurpose(), registrationInformation.getCarFuel(),
                           registrationInformation.getCarEngineCapacity(), registrationInformation.getCarOwnWeight(), registrationInformation.getCarTotalWeight());
      LOGGER.info("Created Auto object: {}", auto);
      AdministrativeObjectEntity administrativeObject = administrativeObjectsCrudRepository.findById(registrationInformation.getAdministrativeObjectCode()).orElseGet(null);
      LOGGER.info("Found AdministrativeObjectEntity {}", administrativeObject);
      Registration registration = new Registration(Registration.PersonKind.getPersonKind(registrationInformation.getPerson()), administrativeObject.getTypeName(), administrativeObject.getName(),
                                                   registrationInformation.getOperationName(), registrationInformation.getRegistrationDate(), registrationInformation.getCarNewRegistrationNumber(),
                                                   csvRegionCodeImportTool.getRegion(registrationInformation.getCarNewRegistrationNumber()));
      LOGGER.info("Created Registration object: {}", registration);
      ServiceCenterEntity serviceCenterEntity = serviceCenterCrudRepository.findById(registrationInformation.getDepartmentCode()).orElseGet(null);
      LOGGER.info("Found ServiceCenterEntity object: {}", serviceCenterEntity);
      ServiceCenter serviceCenter = new ServiceCenter(serviceCenterEntity.getDepId(), serviceCenterEntity.getAddress(), serviceCenterEntity.getEmail(), registrationInformation.getDepartmentName());
      LOGGER.info("Create ServiceCenter object: {}", serviceCenter);
      return new CombinedInformation(auto, registration, serviceCenter);
    }).collect(Collectors.toList());
  }
}
