package ua.kostenko.carinfo.importing.data.persistent.services;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.carinfoua.configuration.ApplicationProperties;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.RegistrationInformationEntity;
import ua.kostenko.carinfo.carinfoua.data.persistent.repositories.RegistrationInformationCrudRepository;
import ua.kostenko.carinfo.carinfoua.utils.csv.fields.RegistrationInformationCSV;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
@Slf4j
public class RegistrationInformationServiceDB implements RegistrationInformationService {
    private final RegistrationInformationCrudRepository registrationInformationCRUDRepository;
    private final ApplicationProperties applicationProperties;
    private final EntityManagerFactory factory;

    @Autowired
    public RegistrationInformationServiceDB(RegistrationInformationCrudRepository registrationInformationCRUDRepository, ApplicationProperties applicationProperties, EntityManagerFactory factory) {
        Preconditions.checkNotNull(registrationInformationCRUDRepository);
        Preconditions.checkNotNull(applicationProperties);
        Preconditions.checkNotNull(factory);
        this.registrationInformationCRUDRepository = registrationInformationCRUDRepository;
        this.applicationProperties = applicationProperties;
        this.factory = factory;
    }

    @Override
    public void saveAll(Collection<RegistrationInformationEntity> registrationInformationEntityList) {
        log.info("Saving list of RegistrationInformationEntity Objects, size: {}", registrationInformationEntityList.size());
        LocalTime before = LocalTime.now();
        log.info("Time when saving started: {}", before.toString());
        long batchSize = Long.valueOf(applicationProperties.APP_LOG_MAPPER_BATCH_SIZE);
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        AtomicInteger i = new AtomicInteger();
        registrationInformationEntityList.forEach(registrationInformationEntity -> {
            session.saveOrUpdate(registrationInformationEntity);
            int count = i.incrementAndGet();
            if (count % batchSize == 0 || count + 1 >= registrationInformationEntityList.size()) {
                session.flush();
                session.clear();
                i.set(0);
            }
        });
        transaction.commit();
        session.close();
        Duration duration = Duration.between(before, LocalTime.now());
        log.info("Saved list of RegistrationInformationEntity Objects, size: {}", registrationInformationEntityList.size());
        log.info("Time spent for saving: {} ms, {} sec, {} min", duration.toMillis(), duration.getSeconds(), duration.toMinutes());
    }

    private Session getSession() {
        SessionFactory sessionFactory = factory.unwrap(SessionFactory.class);
        if (sessionFactory == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        return sessionFactory.openSession();
    }

    @Override
    public boolean isDataWithLabelAndDateExists(String dataSetLabel, String date) {
        log.info("Checking existing records for date: {} and label {}", date, dataSetLabel);
        Preconditions.checkNotNull(date);
        Preconditions.checkNotNull(dataSetLabel);
        RegistrationInformationEntity firstByDataSetYear = registrationInformationCRUDRepository.findFirstByDataSetYear(date);
        return firstByDataSetYear != null && date.equalsIgnoreCase(firstByDataSetYear.getDataSetYear()) && dataSetLabel.equalsIgnoreCase(firstByDataSetYear.getDataSetLabel());
    }

}
