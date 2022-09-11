package com.tigan_lab.easy_clean.ModelClass;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private final int spanCount;
        private final int spacing;
        private final boolean includeEdge;

        public SpacesItemDecoration(int spanCount, int spacing, boolean includeEdge) {
                this.spanCount = spanCount;
                this.spacing = spacing;
                this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                int column = position % spanCount;

                if (includeEdge) {
                        outRect.left = spacing - column * spacing / spanCount;
                        outRect.right = (column + 1) * spacing / spanCount;

                        if (position < spanCount) {
                                outRect.top = spacing;
                        }
                        outRect.bottom = spacing;
                } else {
                        outRect.left = column * spacing / spanCount;
                        outRect.right = spacing - (column + 1) * spacing / spanCount;
                        if (position >= spanCount) {
                                outRect.top = spacing;
                        }
                }
        }
}