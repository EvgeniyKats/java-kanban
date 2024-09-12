package manager.history;

import task.single.SingleTask;

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
    public void add(SingleTask task) {
        remove(task.getId());
        Node node = linkLastNode(task);
        tasks.put(task.getId(), node);
    }

    @Override
    public void remove(int id) {
        if (tasks.containsKey(id)) {
            Node node = tasks.remove(id);
            removeNode(node);
        }
    }

    @Override
    public List<SingleTask> getHistory() {
        List<SingleTask> result = new ArrayList<>(tasks.size());

        Node node = head;
        for (int i = 0; i < tasks.size(); i++) {
            result.add(node.data.getCopy());
            node = node.next;
        }

        return result;
    }

    private Node linkLastNode(SingleTask task) {
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
        Node removedPrev = removedNode.prev;
        Node removedNext = removedNode.next;

        if (removedPrev != null) {
            if (removedNext == null) {
                tail = removedPrev;
            } else {
                removedPrev.next = removedNext;
            }
        }

        if (removedNext != null) {
            if (removedPrev == null) {
                head = removedNext;
            } else {
                removedNext.prev = removedPrev;
            }
        }
    }

    private static class Node {
        private final SingleTask data;
        private Node prev;
        private Node next;

        public Node(SingleTask data, Node prev, Node next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }
}
