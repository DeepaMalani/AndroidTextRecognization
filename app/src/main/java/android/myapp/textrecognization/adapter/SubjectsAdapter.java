package android.myapp.textrecognization.adapter;

import android.content.Context;
import android.myapp.textrecognization.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.SubjectsViewHolder> {

    private Context mContext;
    private List<Subject> mSubjects;
    private final int VIEW_TYPE_SUBJECTS_HEADER = 0;
    private final int VIEW_TYPE_SUBJECTS = 1;

     public SubjectsAdapter(Context context, List<Subject> subjects) {

        mContext = context;
        mSubjects = subjects;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
        {
            return VIEW_TYPE_SUBJECTS_HEADER;
        }
        else
            return VIEW_TYPE_SUBJECTS;
    }

    @NonNull
    @Override
    public SubjectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if (viewType == VIEW_TYPE_SUBJECTS_HEADER) {
            View itemView = LayoutInflater.from(mContext)
                    .inflate(R.layout.list_subject_header, parent, false);
            return new SubjectsViewHolder(itemView);
        }

        else {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View view = layoutInflater.inflate(R.layout.list_item_subjects, parent, false);

            return new SubjectsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectsViewHolder holder, int position) {

         if(holder.getItemViewType() == VIEW_TYPE_SUBJECTS) {
             holder.textViewSubjectName.setText(mSubjects.get(position - 1).getName());
             holder.editTextGrade.setText(mSubjects.get(position - 1).getGrade());
         }
    }

    @Override
    public int getItemCount() {
         if(mSubjects == null)
        return 0;
         else
             return 1 + mSubjects.size();
    }

    public class SubjectsViewHolder extends RecyclerView.ViewHolder{

         private TextView textViewSubjectName;
         private EditText editTextGrade;

        public SubjectsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSubjectName = itemView.findViewById(R.id.text_subject_name);
            editTextGrade = itemView.findViewById(R.id.edit_text_grade);

        }
    }
}
