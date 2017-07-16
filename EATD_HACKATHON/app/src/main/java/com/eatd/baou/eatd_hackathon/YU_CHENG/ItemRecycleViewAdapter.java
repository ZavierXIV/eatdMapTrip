package com.eatd.baou.eatd_hackathon.YU_CHENG;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.eatd.baou.eatd_hackathon.R;
import com.eatd.baou.eatd_hackathon.Trans;

import java.util.List;

/**
 * Created by QwQ on 2017/7/15.
 */

public class ItemRecycleViewAdapter extends RecyclerView.Adapter<ItemRecycleViewAdapter.ViewHolder>{
    private List<String> list;
    private LayoutInflater mInFlater;
    public static Context context;
    public ItemRecycleViewAdapter(Context context,List<String> list){
        Log.i("TAG","SIZE"+list.size());
        this.context = context;
        this.list = list;
        mInFlater = LayoutInflater.from(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("TAG","onCreateViewHolder");
        View view = View.inflate(parent.getContext(), R.layout.infoitem, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Log.i("TAG","onBindViewHolder");
        holder.tv.setText(list.get(position));
        if (position == 0)
            holder.iv.setImageResource(R.mipmap.ic_account_balance_black_24dp);
        if(position == 1)
            holder.iv.setImageResource(R.mipmap.ic_local_phone_black_24dp);
        else if(position == 2) {
            holder.iv.setImageResource(R.mipmap.ic_place_black_24dp);

        }else if(position == 3)
            holder.iv.setImageResource(R.mipmap.ic_query_builder_black_24dp);
        else if(position == 4) {
            holder.iv.setImageResource(R.mipmap.ic_directions_bus_black_24dp);
            holder.tv.setText("更多交通推薦");
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Trans.class);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        Log.i("TAG","getItemCount");
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView iv;
        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.item_title);
            iv = (ImageView) itemView.findViewById(R.id.item_icon);

        }

    }
    public void add(String item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }
    public void remove(String item) {
        int position = list.indexOf(item);
        list.remove(position);
        notifyItemRemoved(position);
    }
}
