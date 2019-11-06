package com.hfad.organizationofthefestival;

import android.widget.ArrayAdapter;

public class AdminListAdapter extends ArrayAdapter<String> {
    public AdminListAdapter() {
        super(ArrayAdapter.this, R.layout.admin_row_layout, data);
    }
}
