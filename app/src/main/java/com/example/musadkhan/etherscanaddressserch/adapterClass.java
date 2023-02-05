package com.example.musadkhan.etherscanaddressserch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class adapterClass extends RecyclerView.Adapter<adapterClass.ViewHolder>{

    ArrayList<model> modelArrayList;
    Context context;
    String hashNumberForClick,scannerName;

    public adapterClass(ArrayList<model> modelArrayList, Context context) {
        this.modelArrayList = modelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



        holder.transactionStatusTxt.setText(""+modelArrayList.get(position).getStatus());
        String cnf = modelArrayList.get(position).getStatus();
        if (cnf == "OUTGOING TRANSACTION")
        {
            holder.transactionStatusTxt.setTextColor(Color.RED);
        }
        else {
            holder.transactionStatusTxt.setTextColor(Color.GREEN);
        }




        if (modelArrayList.get(position).getBlockNumber() == 0)
        {
            holder.blockNoTxt.setVisibility(View.INVISIBLE);
            holder.tokenValueTxt.setText("Token ID : "+modelArrayList.get(position).getTokenAmount());
        }
        else
        {
            holder.blockNoTxt.setText("Block Number : "+modelArrayList.get(position).getBlockNumber());
            holder.tokenValueTxt.setText("Token Amount : "+modelArrayList.get(position).getTokenAmount()+"$");
        }

        holder.fromTxt.setText("From : "+modelArrayList.get(position).getFrom());
        holder.toTxt.setText("To : "+modelArrayList.get(position).getTo());
        holder.tokenNameTxt.setText("Token Name : "+modelArrayList.get(position).getTokenName());
        holder.tokenSymbolTxt.setText("Token Symbol : "+modelArrayList.get(position).getTokenSymbol());
        holder.timeStamptxt.setText("Time : "+modelArrayList.get(position).getTimeStamp());
        holder.hashNumberTxt.setPaintFlags(holder.hashNumberTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.hashNumberTxt.setTextColor(Color.BLUE);

        holder.hashNumberTxt.setText("Hash : "+modelArrayList.get(position).getHashNumber());
        hashNumberForClick = modelArrayList.get(position).getHashNumber();
        scannerName = modelArrayList.get(position).scannerName;

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView transactionStatusTxt,fromTxt,toTxt,blockNoTxt,tokenNameTxt,tokenSymbolTxt,tokenValueTxt,timeStamptxt,hashNumberTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            transactionStatusTxt = itemView.findViewById(R.id.transactionStatus);
            fromTxt = itemView.findViewById(R.id.from);
            toTxt = itemView.findViewById(R.id.to);
            blockNoTxt = itemView.findViewById(R.id.blockNumber);
            tokenNameTxt = itemView.findViewById(R.id.tokenName);
            tokenSymbolTxt = itemView.findViewById(R.id.tokenSymbol);
            tokenValueTxt = itemView.findViewById(R.id.tokenAmount);
            timeStamptxt = itemView.findViewById(R.id.timeStamp);
            hashNumberTxt = itemView.findViewById(R.id.hash);




            hashNumberTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(scannerName.equals("ether"))
                    {
                        Intent urlEtherIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://etherscan.io/tx/"+hashNumberForClick));
                        context.startActivity(urlEtherIntent);
                    }
                    else if(scannerName.equals("tron"))
                    {
                        Intent urlTronIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://tronscan.org/#/transaction/"+hashNumberForClick));
                        context.startActivity(urlTronIntent);
                    }
                    else if (scannerName.equals("bsc"))
                    {
                        Intent urlBscIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bscscan.com/tx/"+hashNumberForClick));
                        context.startActivity(urlBscIntent);
                    }
                    else{
                        Intent urlPolyIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://polygonscan.com/tx/"+hashNumberForClick));
                        context.startActivity(urlPolyIntent);
                    }
                }
            });
        }
    }
}
