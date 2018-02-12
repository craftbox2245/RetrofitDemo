package com.retrofitdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.retrofitdemo.R;
import com.retrofitdemo.model.GridModel;
import com.retrofitdemo.model.StateModel;

import java.util.ArrayList;

/**
 * Created by CRAFT BOX on 10/10/2016.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private ArrayList<GridModel> data;
    private Context context;

    public GridAdapter(Context context, ArrayList<GridModel> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_grid, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img;

        public ViewHolder(View view) {
            super(view);
            //radio = (RadioButton) view.findViewById(R.id.radio);
            img = (ImageView) view.findViewById(R.id.view_img);
        }
    }
}
