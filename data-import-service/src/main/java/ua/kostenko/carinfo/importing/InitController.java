package ua.kostenko.carinfo.importing;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import ua.kostenko.carinfo.importing.importing.Initializer;
import ua.kostenko.carinfo.importing.importing.administrative.AdminObjImportInitializer;
import ua.kostenko.carinfo.importing.importing.centers.ServiceCenterInitializer;
import ua.kostenko.carinfo.importing.importing.registration.RegistrationImportInitializer;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

@Controller
@Slf4j
public class InitController {
    private final List<Initializer> initializers;

    @Autowired
    public InitController(@NonNull @Nonnull ServiceCenterInitializer serviceCenterInitializer,
                          @NonNull @Nonnull AdminObjImportInitializer adminObjImportInitializer,
                          @NonNull @Nonnull RegistrationImportInitializer registrationImportInitializer) {

        initializers = new LinkedList<>();
        initializers.add(adminObjImportInitializer);
        initializers.add(serviceCenterInitializer);
        initializers.add(registrationImportInitializer);
    }

    @Scheduled(cron = "0 0 0,12 1,14 * *")//twice per month on 1st and 14th of month on 12 o'clock
    @EventListener(ApplicationReadyEvent.class)
    public void initDatabase() {
        initializers.forEach(InitController::initialize);
    }

    private static void initialize(Initializer initializer) {
        String className = initializer.getClass().getSimpleName();
        LocalTime before = LocalTime.now();
        log.info("Initializing of {}. Start time: {}", className, before.toString());
        initializer.init();
        LocalTime after = LocalTime.now();
        log.info("Initialization of {} finished. Finish Time: {}, duration: in minutes {}, in seconds {}, in millis {}", className,
                 after.toString(),
                 Duration.between(before, after).toMinutes(),
                 Duration.between(before, after).getSeconds(),
                 Duration.between(before, after).toMillis()
        );
    }

}
