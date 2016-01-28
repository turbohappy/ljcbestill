package church.lifejourney.bestillknow.helper;

import android.support.v7.widget.RecyclerView;

/**
 * Created by bdavis on 1/28/16.
 */
public abstract class VerticalBoundsScrollListener extends RecyclerView.OnScrollListener {
    @Override
    public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (!recyclerView.canScrollVertically(-1)) {
            onScrolledToTop();
        } else if (!recyclerView.canScrollVertically(1)) {
            onScrolledToBottom();
        }
        // don't care otherwise
    }

    public abstract void onScrolledToTop();

    public abstract void onScrolledToBottom();
}
