package pcd.ass01.simengineconcurrent;

public class BagOfTasksTest {
    @Test
    public void testInstantiation() {
        var bagOfTasks = new BagOfTasks(10);
        assertNotNull(bagOfTasks);
    }
}
