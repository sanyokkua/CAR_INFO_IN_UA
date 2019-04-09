package ua.kostenko.carinfo.importing;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import ua.kostenko.carinfo.importing.importing.Initializer;
import ua.kostenko.carinfo.importing.importing.administrative.AdminObjImportInitializer;
import ua.kostenko.carinfo.importing.importing.centers.ServiceCenterInitializer;
import ua.kostenko.carinfo.importing.importing.registration.RegistrationImportInitializer;

import javax.annotation.Nonnull;
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
//        initializers.add(adminObjImportInitializer);
//        initializers.add(serviceCenterInitializer);
        initializers.add(registrationImportInitializer);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initDatabase() {
        initializers.forEach(Initializer::init);
    }

}
