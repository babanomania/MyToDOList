package com.babanomania.mytodolist.recycleViewUtil;

import android.view.View;

/**
 * Created by Shouvik on 23/08/2017.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
