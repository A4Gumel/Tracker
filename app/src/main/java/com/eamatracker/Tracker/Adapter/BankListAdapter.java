package com.eamatracker.Tracker.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eamatracker.Models.Bank;
import com.eamatracker.Models.BankList;
import com.eamatracker.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class BankListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  implements Filterable {

    private static final String TAG = "BankListAdapter";

    private Context mContext;
    List<Bank> banksList;
    List<Bank> allBanksList;
    private OnItemClickListener mListener;

    public BankListAdapter(Context mContext, List<Bank> banksList) {
        this.mContext = mContext;
        this.banksList = banksList;
        this.allBanksList = new ArrayList<>(banksList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.banks_list_layout, parent, false);

        BankListVh bankListVh = new BankListVh(view, mListener);

        return bankListVh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Bank bank = banksList.get(position);

        populateBank((BankListVh) holder, bank);


    }


    @Override
    public int getItemCount() {
        return banksList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {

        //run on the background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Bank> filteredList = new ArrayList<>();

            if (charSequence.toString().isEmpty()) {

                filteredList.addAll(allBanksList);

            } else {

                for (Bank bank: allBanksList) {

                    if (bank.getName()
                            .toLowerCase()
                            .contains(charSequence
                                    .toString().toLowerCase())) {

                        filteredList.add(bank);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;


            return filterResults;
        }

        //runs on ui thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            banksList.clear();
            banksList.addAll((Collection<? extends Bank>) filterResults.values);
            notifyDataSetChanged();

        }
    };


    public interface OnItemClickListener {

        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        mListener = listener;

    }


    public static class BankListVh extends RecyclerView.ViewHolder {

        TextView bankName;
        RadioButton selectBank;


        public BankListVh(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            bankName = itemView.findViewById(R.id.bankName);
            selectBank = itemView.findViewById(R.id.bankRadioBtn);

            itemView.setOnClickListener(view -> {

                if (listener != null) {

                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {

                        listener.onItemClick(position);
                    }
                }


            });
        }
    }

    private void populateBank(BankListVh holder, Bank bank) {

        holder.bankName.setText(bank.getName());
    }
}
