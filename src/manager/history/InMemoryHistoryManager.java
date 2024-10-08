package manager.history;

import task.single.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> tasks;
    private Node<Task> head;
    private Node<Task> tail;

    public InMemoryHistoryManager() {
        tasks = new HashMap<>();
        head = null;
        tail = null;
    }

    @Override
    public void add(Task task) {
        remove(task.getId());
        Node<Task> node = linkLastNode(task);
        tasks.put(task.getId(), node);
    }

    @Override
    public void remove(int id) {
        final Node<Task> nodeToRemove = tasks.remove(id);
        if (nodeToRemove != null) {
            removeNode(nodeToRemove);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> result = new ArrayList<>(tasks.size());

        Node<Task> node = head;
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

    private Node<Task> linkLastNode(Task task) {
        Node<Task> tailOld = tail;
        Node<Task> tailNew = new Node<>(task, tailOld, null);

        if (tailOld == null) {
            head = tailNew;
        } else {
            tailOld.next = tailNew;
        }

        tail = tailNew;
        return tailNew;
    }

    private void removeNode(Node<Task> removedNode) {
        final Node<Task> nextFromRemoved = removedNode.next;
        final Node<Task> prevFromRemoved = removedNode.prev;

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

    private static class Node<E extends Task> {
        private final E data;
        private Node<E> prev;
        private Node<E> next;

        public Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }
}
