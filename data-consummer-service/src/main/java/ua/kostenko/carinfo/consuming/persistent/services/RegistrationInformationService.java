package ua.kostenko.carinfo.consuming.persistent.services;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.data.entities.RegistrationInformationEntity;
import ua.kostenko.carinfo.configuration.ApplicationProperties;
import ua.kostenko.carinfo.consuming.persistent.SaveService;
import ua.kostenko.carinfo.consuming.persistent.repositories.RegistrationInformationCrudRepository;

import javax.persistence.EntityManagerFactory;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class RegistrationInformationService implements SaveService<RegistrationInformationEntity> {
    private final ApplicationProperties applicationProperties;
    private final EntityManagerFactory factory;

    @Autowired
    public RegistrationInformationService(RegistrationInformationCrudRepository registrationInformationCRUDRepository, ApplicationProperties applicationProperties, EntityManagerFactory factory) {
        Preconditions.checkNotNull(registrationInformationCRUDRepository);
        Preconditions.checkNotNull(applicationProperties);
        Preconditions.checkNotNull(factory);
        this.applicationProperties = applicationProperties;
        this.factory = factory;
    }

    private Session getSession() {
        SessionFactory sessionFactory = factory.unwrap(SessionFactory.class);
        if (sessionFactory == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        return sessionFactory.openSession();
    }

    @Override
    public void saveAllObjects(Collection<RegistrationInformationEntity> objects) {
        log.info("Saving list of RegistrationInformationEntity Objects, size: {}", objects.size());
        LocalTime before = LocalTime.now();
        log.info("Time when saving started: {}", before.toString());
        long batchSize = Long.valueOf(applicationProperties.APP_LOG_MAPPER_BATCH_SIZE);
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        AtomicInteger i = new AtomicInteger();
        objects.forEach(registrationInformationEntity -> {
            session.saveOrUpdate(registrationInformationEntity);
            int count = i.incrementAndGet();
            if (count % batchSize == 0 || count + 1 >= objects.size()) {
                session.flush();
                session.clear();
                i.set(0);
            }
        });
        transaction.commit();
        session.close();
        Duration duration = Duration.between(before, LocalTime.now());
        log.info("Saved list of RegistrationInformationEntity Objects, size: {}", objects.size());
        log.info("Time spent for saving: {} ms, {} sec, {} min", duration.toMillis(), duration.getSeconds(), duration.toMinutes());
    }
}
