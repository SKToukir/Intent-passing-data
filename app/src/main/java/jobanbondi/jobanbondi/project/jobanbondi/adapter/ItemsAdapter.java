package jobanbondi.jobanbondi.project.jobanbondi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.net.URL;
import java.util.List;

import jobanbondi.jobanbondi.project.jobanbondi.R;
import jobanbondi.jobanbondi.project.jobanbondi.util.AppController;
import jobanbondi.jobanbondi.project.jobanbondi.util.ModelClass;

/**
 * Created by toukir on 12/6/16.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {

    Context context;
    private List<ModelClass> modelClasses;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ItemsAdapter(Context context, List<ModelClass> modelClasses){
        this.context = context;
        this.modelClasses = modelClasses;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.files_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String s="true";
        ModelClass model = modelClasses.get(position);


        if (model.getIsImage().contains("true")) {

            holder.txtComplain.setText(model.getComplain());
            holder.txtDate.setText(model.getDateTime());
            holder.thumbNail.setImageUrl(model.getFiles(), imageLoader);



        }else {
            holder.thumbNail.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.VISIBLE);
            holder.txtComplain.setText(model.getComplain());
            holder.txtDate.setText(model.getDateTime());

        }
        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return modelClasses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtComplain, txtDate, txtLocation;
        public ImageView videoView;
        public NetworkImageView thumbNail;

        public MyViewHolder(View view) {
            super(view);
            thumbNail = (NetworkImageView) view.findViewById(R.id.imageView);
            txtComplain = (TextView) view.findViewById(R.id.txtComplain);
            txtDate = (TextView) view.findViewById(R.id.txtDateTime);
            videoView = (ImageView) view.findViewById(R.id.VideoView);
        }
    }
//
//    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
//        int width = bm.getWidth();
//        int height = bm.getHeight();
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//// CREATE A MATRIX FOR THE MANIPULATION
//        Matrix matrix = new Matrix();
//// RESIZE THE BIT MAP
//        matrix.postScale(scaleWidth, scaleHeight);
//
//// "RECREATE" THE NEW BITMAP
//        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
//        return resizedBitmap;
//    }
}
