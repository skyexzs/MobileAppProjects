package com.example.notesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.model.Notes;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    private List<Notes> notesList;
    private Context context;
    private OnNoteClickListener onNoteClickListener;

    public NotesAdapter(List<Notes> notesList, Context context, OnNoteClickListener onNoteClickListener) {
        this.notesList = notesList;
        this.context = context;
        this.onNoteClickListener = onNoteClickListener;
    }

    @NonNull
    @Override
    public NotesAdapter.NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.notes_item, parent, false);
        return new NotesViewHolder(itemView, onNoteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NotesViewHolder holder, int position) {
        holder.notesTitleTextView.setText(notesList.get(position).getTitle());
        holder.notesContentTextView.setText(notesList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView notesTitleTextView;
        public TextView notesContentTextView;
        public OnNoteClickListener onNoteClickListener;

        public NotesViewHolder(@NonNull View itemView, OnNoteClickListener onNoteClickListener) {
            super(itemView);
            notesTitleTextView = itemView.findViewById(R.id.notesTitleTextView);
            notesContentTextView = itemView.findViewById(R.id.notesContentTextView);
            this.onNoteClickListener = onNoteClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteClickListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteClickListener {
        void onNoteClick(int position);
    }
}
