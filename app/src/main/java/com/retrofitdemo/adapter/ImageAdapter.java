package com.retrofitdemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.retrofitdemo.R;

import java.util.ArrayList;

/**
 * Created by jaykishan on 2/22/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    ArrayList<Bitmap> b;
    Context mContext;


    public interface image {
        public void image(int actual);
    }


    public ImageAdapter(ArrayList<Bitmap> b, Context mContext) {
        super();
        this.b = b;
        this.mContext = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_images, parent, false);
        ViewHolder vi = new ViewHolder(v);
        return vi;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position != 0) {
            holder.btnCancle.setImageBitmap(b.get(position));
        } else {
            holder.btnCancle.setImageDrawable(mContext.getResources().getDrawable(R.drawable.add_more));
        }
        holder.btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    image intercommunitoter = (image) mContext;
                    intercommunitoter.image(1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return b.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView btnCancle;

        public ViewHolder(View vi) {
            super(vi);
            btnCancle = (ImageView) vi.findViewById(R.id.img);

        }
    }
}
