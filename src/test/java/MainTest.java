package org.example;

import org.example.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class MainTest {

    Task task1;
    Task task2;
    Task task3;
    TaskData taskData;
    Set<Task> taskSet1;
    Set<Task> taskSet2;
    Set<Task> taskSet3;

    @BeforeEach
    void setUp() {
        // Task objelerini oluşturma
        task1 = new Task("Java Collections", "Write List Interface", "Ann", Priority.LOW, Status.IN_QUEUE);
        task2 = new Task("Java Collections", "Write Set Interface", "Ann", Priority.MED, Status.ASSIGNED);
        task3 = new Task("Java Collections", "Write Map Interface", "Bob", Priority.HIGH, Status.IN_QUEUE);

        // Her set'e kendi taskını ekleme (Senin kodunda hepsi taskSet1'e eklenmişti, düzelttim)
        taskSet1 = new HashSet<>();
        taskSet1.add(task1);

        taskSet2 = new HashSet<>();
        taskSet2.add(task2);

        taskSet3 = new HashSet<>();
        taskSet3.add(task3);

        // TaskData objesini başlatma
        taskData = new TaskData(taskSet1, taskSet2, taskSet3, new HashSet<>());
    }

    @DisplayName("Task sınıfı doğru access modifiers sahip mi")
    @Test
    public void testTaskAccessModifiers() throws NoSuchFieldException {
        Field assigneeFields = task1.getClass().getDeclaredField("assignee");
        Field descriptionsFields = task1.getClass().getDeclaredField("description");
        Field projectFields = task1.getClass().getDeclaredField("project");
        Field priorityFields = task1.getClass().getDeclaredField("priority");
        Field statusFields = task1.getClass().getDeclaredField("status");

        // 2 değeri 'private' erişim belirleyicisini temsil eder
        assertEquals(2, assigneeFields.getModifiers());
        assertEquals(2, descriptionsFields.getModifiers());
        assertEquals(2, projectFields.getModifiers());
        assertEquals(2, priorityFields.getModifiers());
        assertEquals(2, statusFields.getModifiers());
    }

    @DisplayName("Task sınıfı doğru typelara sahip mi")
    @Test
    public void testTaskTypes() {
        assertThat(task1.getAssignee(), instanceOf(String.class));
        assertThat(task1.getDescription(), instanceOf(String.class));
        assertThat(task1.getPriority(), instanceOf(Priority.class));
        assertThat(task1.getProject(), instanceOf(String.class));
        assertThat(task1.getStatus(), instanceOf(Status.class));
    }

    @DisplayName("TaskData getTasks method doğru çalışıyor mu ?")
    @Test
    public void testGetTasksMethod() {
        assertEquals(taskSet1, taskData.getTasks("ann"));
        assertEquals(taskSet2, taskData.getTasks("bob"));
        assertEquals(taskSet3, taskData.getTasks("carol"));
    }

    @DisplayName("TaskData getUnion method doğru çalışıyor mu ?")
    @Test
    public void testGetUnionMethod() {
        Set<Task> s1 = new HashSet<>();
        s1.add(task1);
        Set<Task> s2 = new HashSet<>();
        s2.add(task2);

        // TaskData içindeki getUnion metodunun List<Set<Task>> aldığını varsayıyoruz
        Set<Task> totals = taskData.getUnion(List.of(s1, s2));
        assertEquals(2, totals.size());
    }

    @DisplayName("TaskData getIntersect() method doğru çalışıyor mu ?")
    @Test
    public void testGetIntersectMethod() {
        Set<Task> s1 = new HashSet<>();
        s1.add(task1);
        s1.add(task2);
        Set<Task> s2 = new HashSet<>();
        s2.add(task2);

        // Metod ismi getIntersect olarak güncellendi
        Set<Task> intersections = taskData.getIntersect(s1, s2);

        assertEquals(1, intersections.size());
        assertEquals(task2, intersections.iterator().next());
    }

    @DisplayName("TaskData getDifference() method doğru çalışıyor mu ?")
    @Test
    public void testGetDifferenceMethod() {
        Set<Task> s1 = new HashSet<>();
        s1.add(task1);
        s1.add(task2);
        Set<Task> s2 = new HashSet<>();
        s2.add(task2);

        // Metod ismi getDifference olarak güncellendi
        Set<Task> differences = taskData.getDifference(s1, s2);

        assertEquals(1, differences.size());
        assertEquals(task1, differences.iterator().next());
    }

    @DisplayName("findUniqueWords doğru çalışıyor mu ?")
    @Test
    public void testFindUniqueWordsMethod() {
        Set<String> uniqueWords = StringSet.findUniqueWords();
        // Kelime sayısı temizleme yöntemine göre değişebilir, 143 genel bir beklentidir
        assertEquals(143, uniqueWords.size());

        List<String> results = uniqueWords.stream().collect(Collectors.toList());
        assertEquals("a", results.get(0));
        assertEquals("wrote", results.get(results.size() - 1));
    }
}