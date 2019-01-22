package cz.intik.overflowpagerindicator;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.intik.overflowindicator.OverflowPagerIndicator;
import cz.intik.overflowindicator.SimpleSnapHelper;

/**
 * @author Petr Introvic <introvic.petr@gmail.com>
 * created 03.10.2017.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerWithIndicator(R.id.recycler_view, R.id.view_pager_indicator, 20, 1000);
        initRecyclerWithIndicator(R.id.recycler_view_simple, R.id.view_pager_indicator_simple, 5, 1000);
    }

    private void initRecyclerWithIndicator(int recyclerViewId, int indicatorViewId, final int itemCount, int delay) {
        RecyclerView recyclerView = findViewById(recyclerViewId);
        final OverflowPagerIndicator overflowPagerIndicator = findViewById(indicatorViewId);
        final Adapter adapter = new Adapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        overflowPagerIndicator.attachToRecyclerView(recyclerView);
        overflowPagerIndicator.registerDrawableIndicator(R.drawable.ic_play_circle_outline_black_24dp, 0);
        overflowPagerIndicator.registerDrawableIndicator(R.drawable.ic_image_black_24dp, 1);

        new SimpleSnapHelper(overflowPagerIndicator).attachToRecyclerView(recyclerView);

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.updateItemCount(itemCount);
            }
        }, delay);
    }

    private static class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        // Some material colors for recycler view items background
        private String[] colors = new String[]{"#2196F3", "#00BCD4", "#4CAF50", "#CDDC39", "#FFC107", "#FF5722"};
        private int itemCount = 0;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            FrameLayout view = new FrameLayout(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int color = Color.parseColor(colors[position % colors.length]);

            holder.itemView.setBackgroundColor(color);
        }

        @Override
        public int getItemCount() {
            return itemCount;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 1){
                return 1; //video drawable
            }
            else if (position == 3) {
                return 99; //default getDotDrawable (viewType unknown)
            }
            return super.getItemViewType(position);
        }

        void updateItemCount(int newCount) {
            itemCount = newCount;

            notifyDataSetChanged();
        }
    }

}
