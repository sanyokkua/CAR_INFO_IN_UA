package ua.kostenko.carinfo.consuming.queue;

public interface QueueReceiver {
    void receiveMessage(String message);
}
