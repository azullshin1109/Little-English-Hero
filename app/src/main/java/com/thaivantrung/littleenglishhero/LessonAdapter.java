package com.thaivantrung.littleenglishhero;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder>{
    Context context;
    ArrayList <LessonModel> list;

    public LessonAdapter(Context context, ArrayList<LessonModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LessonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lesson, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonAdapter.ViewHolder holder, int position) {
        LessonModel lesson = list.get(position);

        holder.imgLesson.setImageResource(
                lesson.getImage()
        );

        holder.txtLesson.setText(
                lesson.getLesson()
        );

        holder.txtTitle.setText(
                lesson.getTitle()
        );

        holder.txtActivity.setText(
                lesson.getActivity()
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgLesson;
        TextView txtLesson, txtTitle, txtActivity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgLesson = itemView.findViewById(R.id.imgLesson);

            txtLesson = itemView.findViewById(R.id.txtLesson);

            txtTitle = itemView.findViewById(R.id.txtTitle);

            txtActivity = itemView.findViewById(R.id.txtActivity);
        }
    }
}
