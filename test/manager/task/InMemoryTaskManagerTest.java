package manager.task;

import manager.Managers;
import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends AbstractTaskManagerTest<TaskManager> {

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }
}