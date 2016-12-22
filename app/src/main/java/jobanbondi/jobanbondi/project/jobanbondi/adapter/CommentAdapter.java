package jobanbondi.jobanbondi.project.jobanbondi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import jobanbondi.jobanbondi.project.jobanbondi.R;
import jobanbondi.jobanbondi.project.jobanbondi.util.CommentClass;

/**
 * Created by toukir on 12/20/16.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    Context context;
    private List<CommentClass> commentList;

    public CommentAdapter(Context mContext, List<CommentClass> list){
        this.context = mContext;
        this.commentList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_row,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        CommentClass comment = commentList.get(position);

        holder.txtUserName.setText(comment.getUserName());
        holder.txtUserComment.setText(comment.getUserComment());

        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtUserName, txtUserComment;

        public MyViewHolder(View view) {
            super(view);
            txtUserName = (TextView) view.findViewById(R.id.txtUsername);
            txtUserComment = (TextView) view.findViewById(R.id.txtUserComment);
        }
    }
}
