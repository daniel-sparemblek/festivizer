package com.hfad.organizationofthefestival.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.leader.Auction;

import java.util.List;

public class LeaderJobAuctionAdapter extends ArrayAdapter<Auction> {

    private Context context;
    private List<Auction> auctions;

    public LeaderJobAuctionAdapter(@NonNull Context context, List<Auction> auctions) {
        super(context, R.layout.leader_screen_job_auc_row_layout, auctions);
        this.context = context;
        this.auctions = auctions;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.leader_screen_job_auc_row_layout, parent, false);
        }

        Auction currentAuction = auctions.get(position);
        TextView auctionInformation = view.findViewById(R.id.txt);
        Button btnAccept = view.findViewById(R.id.accept);
        Button btnDecline = view.findViewById(R.id.decline);
        auctionInformation.setText("Worker" + currentAuction.getEndTime());
        return view;
    }
}
