package ua.kostenko.carinfo.consuming.queue;

import ua.kostenko.carinfo.consuming.persistent.SaveService;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SaveScheduler<T> {
    private final ScheduledExecutorService scheduledExecutorService;
    private final SaveService<T> saveService;


    public SaveScheduler(SaveService<T> saveService) {
        this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
        this.saveService = saveService;
    }

    public void schedule(List<T> temp, int seconds) {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            synchronized (temp) {
                if (!temp.isEmpty()) {
                    saveService.saveAllObjects(temp);
                }
                temp.clear();
            }
        }, 0, seconds, TimeUnit.SECONDS);
    }
}
