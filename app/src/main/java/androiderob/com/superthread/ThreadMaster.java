package androiderob.com.superthread;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

public class ThreadMaster<T> {

    private List<Node> nodes;
    private List<T> allItems;
    private Context context;
    private int threads;
    private int readyThreads = 0;

    private CompletionListener completionListener;

    public static ThreadMaster with(Context context) {
        return new ThreadMaster(context);
    }

    public static ThreadMaster with(int threads, Context context) {
        return new ThreadMaster(threads, context);
    }

    public ThreadMaster(Context context) {
        this(Runtime.getRuntime().availableProcessors(), context);
    }

    public ThreadMaster(int threads, Context context) {
        this.nodes = new ArrayList<>();
        this.context = context;
        int availableNodes = Runtime.getRuntime().availableProcessors();
        this.threads = threads > availableNodes ? availableNodes : threads;

        for (int i = 0; i < this.threads; i++) {
            this.nodes.add(new Node());
        }
    }

    public ThreadMaster setItems(List<T> items) {
        this.allItems = items;

        for (int i = 0; i < items.size(); i++) {
            nodes.get(i%threads).addItem(items.get(i));
        }

        return this;
    }

    public void run(final Executor executor) {
        for (int i = 0; i < nodes.size(); i++) {
            final int finalI = i;

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0, max = nodes.get(finalI).getItems().size(); j < max; j++) {
                        executor.execute(nodes.get(finalI).getItems().get(j));
                    }
                    readyThreads++;
                    checkEnd();
                }
            });
            thread.start();
        }
    }

    private void checkEnd() {
        if (readyThreads == threads && this.completionListener != null) {
            new Handler(context.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    completionListener.onComplete();
                }
            });

            readyThreads = 0;
        }
    }

    public class Node {

        private List<T> items;

        public Node() {
            this.items = new ArrayList<>();
        }

        public ThreadMaster.Node addItem(T item) {
            this.items.add(item);
            return this;
        }

        public List<T> getItems() {
            return items;
        }
    }

    public interface CompletionListener {
        public void onComplete();
    }

    public interface Executor<T> {
        public void execute(T item);
    }

    public ThreadMaster setCompletionListener(CompletionListener listener) {
        this.completionListener = listener;
        return this;
    }
}