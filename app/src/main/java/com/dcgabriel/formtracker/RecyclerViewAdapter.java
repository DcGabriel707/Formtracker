package com.dcgabriel.formtracker;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.CalendarContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

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


        String deadline = formsList.get(position).getDeadline();

        //if deadline is empty
        if ((formsList.get(position).getDeadline() == null) || (formsList.get(position).getDeadline().equals(""))) {
            Log.d(TAG, "onBindViewHolder: before setting deadline to invisible****************");
            deadline = mContext.getString(R.string.noDeadline);

            //holder.deadlineCardView.setVisibility(View.INVISIBLE); //hides the deadlineCardView
            //holder.relativeLayoutCardView.removeView(holder.deadlineCardView); //removes the deadlineCardView. the status Card will replace
        } else {
            //sets the preferred date format
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("Settings", MODE_PRIVATE);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(mContext.getString(R.string.MMddyyyy), Locale.US);
            SimpleDateFormat preferredDateFormat = new SimpleDateFormat(sharedPreferences.getString("DateFormat", ""), Locale.US);
            try {
                Date date = simpleDateFormat.parse(deadline);
                deadline = preferredDateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(mContext, "ParceException catched", Toast.LENGTH_SHORT).show();
                System.exit(1);
            }
        }

        holder.nameTextView.setText(formsList.get(position).getName());
        holder.companyTextView.setText(formsList.get(position).getCompany());
        holder.detailsTextView.setText(formsList.get(position).getDetails());
        holder.deadlineTextView.setText(deadline);
        holder.statusTextView.setText(formsList.get(position).getStatus());


        //handles clicks on recycler view entry
        holder.relativeLayoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onBindViewHolder: onClick: *******************");
                entryListener.editEntry(position);
            }
        });

        //todo fixx
        holder.nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String website = formsList.get(position).getWebsite();
                if (website == null || website.equals("")) {
                    website = formsList.get(position).getName();
                }
                Intent launch = new Intent(Intent.ACTION_WEB_SEARCH);
                launch.putExtra(SearchManager.QUERY, website);
                mContext.startActivity(launch);
            }
        });

        holder.deadlineCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: *******************");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(mContext.getString(R.string.dateFormat), Locale.US);

                Date date;
                Calendar cal = Calendar.getInstance();

                Intent shareDate = new Intent(Intent.ACTION_INSERT);
                shareDate.setData(CalendarContract.Events.CONTENT_URI);
                shareDate.putExtra(CalendarContract.Events.TITLE, formsList.get(position).getName() + " application deadline");
                shareDate.putExtra(CalendarContract.Events.DESCRIPTION, "TEST");


                //if there is a deadline
                if (!(formsList.get(position).getDeadline() == null)) {
                    try {
                        date = simpleDateFormat.parse(formsList.get(position).getDeadline());
                        cal.setTime(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, "ParceException catched", Toast.LENGTH_SHORT).show();
                        System.exit(1);
                    }
                    shareDate.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis());
                    shareDate.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis());
                    shareDate.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                    mContext.startActivity(shareDate);
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.noDeadline), Toast.LENGTH_SHORT).show();
                }
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
