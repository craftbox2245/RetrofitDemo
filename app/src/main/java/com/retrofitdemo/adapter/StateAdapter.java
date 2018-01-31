package com.retrofitdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.retrofitdemo.R;
import com.retrofitdemo.model.StateModel;

import java.util.ArrayList;

/**
 * Created by CRAFT BOX on 10/10/2016.
 */
public class StateAdapter extends RecyclerView.Adapter<StateAdapter.ViewHolder> {
    private ArrayList<StateModel> data;
    private Context context;

    public interface intercommunication{
        public void changeData(int position, boolean flag);
    }

    public StateAdapter(Context context, ArrayList<StateModel> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_item_state, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.country.setText(data.get(i).getCountry());
        viewHolder.region.setText(data.get(i).getRegion());
        viewHolder.capital.setText(data.get(i).getCapital());

        /*viewHolder.radio.setTag(""+data.get(i).getId());
        viewHolder.radio.setChecked(data.get(i).isCheck());
        viewHolder.radio.setOnCheckedChangeListener(null);
        viewHolder.radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(viewHolder.radio.getTag().equals(""+data.get(i).getId()))
                {
                    if(b)
                    {
                        intercommunication intercommunication=(intercommunication)context;
                        intercommunication.changeData(i,b);
                    }
                    else
                    {
                        intercommunication intercommunication=(intercommunication)context;
                        intercommunication.changeData(i,b);
                    }
                }
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView country,region,capital;

        RadioButton radio;

        public ViewHolder(View view) {
            super(view);
            //radio = (RadioButton) view.findViewById(R.id.radio);
            country = (TextView)view.findViewById(R.id.single_item_state_country);
            capital = (TextView)view.findViewById(R.id.single_item_state_capital);
            region = (TextView)view.findViewById(R.id.single_item_state_region);
        }
    }
}
