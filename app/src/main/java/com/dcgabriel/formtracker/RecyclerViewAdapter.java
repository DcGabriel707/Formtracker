package com.dcgabriel.formtracker;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static String TAG = "RecyclerViewAdapter";
    private Context mContext;
    private ArrayList<Forms> formsList;
    private EntryClickedInterface entryListener;
    private int UPDATE_ITEM_REQUEST = 2; //test

    public RecyclerViewAdapter(Context mContext, EntryClickedInterface entryListener, ArrayList<Forms> formsList) {
        Log.d(TAG, "RecyclerViewAdapter: **********************************");
        this.mContext = mContext;
        this.formsList = formsList;
        this.entryListener = entryListener; //ScholarshipTabFragment.this?? cast into EntryClickedInterface

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: **********************************");
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.entry_card_layout, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    //remove the "final" from the position parameter if the uri list is removed
    public void onBindViewHolder(ViewHolder holder, final int position) { //called everytime a new item on the list is created. binds the data to the layout?
        Log.d(TAG, "onBindViewHolder: **********************************");

        //removes a view in the cardView if it is empty
        if ((formsList.get(position).getDeadline() == null) || (formsList.get(position).getDeadline().equals(""))) {
            Log.d(TAG, "onBindViewHolder: before setting deadline to invisible****************");

            holder.deadlineCardView.setVisibility(View.INVISIBLE); //hides the deadlineCardView
            //holder.relativeLayoutCardView.removeView(holder.deadlineCardView); //removes the deadlineCardView. the status Card will replace
        }

        holder.nameTextView.setText(formsList.get(position).getName());
        holder.companyTextView.setText(formsList.get(position).getCompany());
        holder.detailsTextView.setText(formsList.get(position).getDetails());
        holder.deadlineTextView.setText(formsList.get(position).getDeadline());
        holder.statusTextView.setText(formsList.get(position).getStatus());

        //handles clicks on recycler view entry
        holder.relativeLayoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onBindViewHolder: onClick: *******************");
                entryListener.editEntry(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return formsList.size(); // what if one entry doesnt have a name?? must make sure all entry have names. nameList will be used to count how many entries there are
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView, companyTextView, detailsTextView, deadlineTextView, statusTextView;
        private RelativeLayout relativeLayoutCardView;
        private CardView deadlineCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder: **********************************");
            nameTextView = itemView.findViewById(R.id.entryCardNameTextView);
            companyTextView = itemView.findViewById(R.id.entryCardCompanyTextView);
            detailsTextView = itemView.findViewById(R.id.entryCardDetailsTextView);
            deadlineTextView = itemView.findViewById(R.id.entryCardDeadlineTextView);
            statusTextView = itemView.findViewById(R.id.entryCardStatusTextView);
            relativeLayoutCardView = itemView.findViewById(R.id.relativeLayoutInsideCard);
            deadlineCardView = itemView.findViewById(R.id.entryDeadlineCardView);
        }
    }

    public interface EntryClickedInterface {
        void editEntry(int pos);
    }


}
