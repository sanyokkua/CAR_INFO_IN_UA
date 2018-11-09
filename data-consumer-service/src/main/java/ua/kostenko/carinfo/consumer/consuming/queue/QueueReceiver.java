package ua.kostenko.carinfo.consumer.consuming.queue;

public interface QueueReceiver {
    void receiveMessage(String message);
}
