package manager.history;

import task.single.SingleTask;
import task.single.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> tasks;
    private Node head;
    private Node tail;

    public InMemoryHistoryManager() {
        tasks = new HashMap<>();
        head = null;
        tail = null;
    }

    @Override
    public void add(Task task) {
        remove(task.getId());
        Node node = linkLastNode(task);
        tasks.put(task.getId(), node);
    }

    @Override
    public void remove(int id) {
        final Node nodeToRemove = tasks.remove(id);
        if (nodeToRemove != null) {
            removeNode(nodeToRemove);
        }
    }

    @Override
    public List<SingleTask> getHistory() {
        List<SingleTask> result = new ArrayList<>(tasks.size());

        Node node = head;
        while (node != null) {
            result.add(node.data.getCopy());
            node = node.next;
        }

        return result;
    }

    public void clearHistory() {
        tail = null;
        head = null;
        tasks.clear();
    }

    private Node linkLastNode(Task task) {
        Node tailOld = tail;
        Node tailNew = new Node(task, tailOld, null);

        if (tailOld == null) {
            head = tailNew;
        } else {
            tailOld.next = tailNew;
        }

        tail = tailNew;
        return tailNew;
    }

    private void removeNode(Node removedNode) {
        final Node nextFromRemoved = removedNode.next;
        final Node prevFromRemoved = removedNode.prev;

        if (nextFromRemoved == null) {
            tail = prevFromRemoved;
        } else {
            nextFromRemoved.prev = prevFromRemoved;
        }

        if (prevFromRemoved == null) {
            head = nextFromRemoved;
        } else {
            prevFromRemoved.next = nextFromRemoved;
        }
    }

    private static class Node {
        private final Task data;
        private Node prev;
        private Node next;

        public Node(Task data, Node prev, Node next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }
}
