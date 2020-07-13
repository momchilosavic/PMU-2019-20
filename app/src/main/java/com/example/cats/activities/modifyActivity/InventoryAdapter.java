package com.example.cats.activities.modifyActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cats.R;
import com.example.cats.database.entities.Component;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.partImage);
        }
    }

    private List<Component> componentList;
    private OnListItemClickListener mListener;

    public InventoryAdapter(Context context, List<Component> list){
        this.componentList = list;
        if(context instanceof OnListItemClickListener)
            this.mListener = (OnListItemClickListener)context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View componentView = inflater.inflate(R.layout.view_holder, parent, false);

        ViewHolder viewHolder = new ViewHolder(componentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Component component = componentList.get(position);

        final ImageView imageView = holder.imageView;
        int resourceId = -1;
        switch(Component.Type.values()[component.type]){
            case BODY: resourceId = R.drawable.body; break;
            case BLADE: resourceId = R.drawable.weapon1; break;
            case CHAINSAW: resourceId = R.drawable.chainsaw; break;
            case FORKLIFT: resourceId = R.drawable.forklift; break;
            case ROCKET: resourceId = R.drawable.rocket_launcher; break;
            case STIGNER: resourceId = R.drawable.stinger; break;
            case WHEEL: resourceId = R.drawable.wheel; break;
        }
        imageView.setImageResource(resourceId);
        imageView.setTag(String.valueOf(String.valueOf(component.type)) + "#" + String.valueOf(component.id));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Component c = componentList.get(position);
                mListener.onItemClick(c.type, c.health, c.energy, c.power);

            }
        });

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Component c = componentList.get(position);
                mListener.onItemClick(c.type, c.health, c.energy, c.power);
                ClipData.Item item = new ClipData.Item((String)imageView.getTag());
                ClipData dragData = new ClipData((String)imageView.getTag(), new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(imageView);
                v.startDrag(dragData, myShadow, null, 0);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return componentList.size();
    }

    public interface OnListItemClickListener {
        // TODO: Update argument type and name
        void onItemClick(int type, int health, int energy, int power);
        void onItemDropped(Component c, View view);
    }
}
