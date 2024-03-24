package pcd.ass01.simengineconcurrent;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;

public class BagOfTasks {
    
    private final List<Runnable> tasks;

    public BagOfTasks() {
        tasks = new ArrayList<>();
    }

    synchronized public void addTask(Runnable task) {
        tasks.add(task);
    }

    @SuppressWarnings("null")
    synchronized public Optional<Runnable> getTask() {
        if (tasks.isEmpty()) {
            return Optional.absent();
        }
        return Optional.of(tasks.remove(0));
    }
}
