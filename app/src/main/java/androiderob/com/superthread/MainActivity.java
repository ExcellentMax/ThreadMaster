package androiderob.com.superthread;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<>();

        for (int i = 0; i < 1000000; i++) {
            Item item = new Item();
            item.setId(i).setX(i*2).setY(i*3);
            items.add(item);
        }

        calculate();
    }

    private void done() {
        for (int i = 0, max = items.size(); i < max; i++) {
            Item it = items.get(i);

            if (it.getId() % 100000 == 0) {
                Log.e("ITEM", it.toString());
            }
        }
    }

    private void calculate() {
        ThreadMaster.with(2, this)
                .setItems(items)
                .setCompletionListener(new ThreadMaster.CompletionListener() {
                    @Override
                    public void onComplete() {
                        done();
                        calculateAgain();
                    }
                })
                .run(new ThreadMaster.Executor() {
                    @Override
                    public void execute(Object item) {
                        Item aitem = (Item) item;
                        aitem.heavyOperation();
                    }
                });
    }

    private void calculateAgain() {
        ThreadMaster.with(this)
                .setItems(items)
                .setCompletionListener(new ThreadMaster.CompletionListener() {
                    @Override
                    public void onComplete() {
                        done();
                    }
                })
                .run(new ThreadMaster.Executor() {
                    @Override
                    public void execute(Object item) {
                        Item aitem = (Item) item;
                        aitem.heavyOperation();
                    }
                });
    }
}
