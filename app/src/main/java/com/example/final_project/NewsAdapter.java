package com.example.final_project;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.example.final_project.SQLiteDatabaseHelper;
import com.example.final_project.Details;
import com.example.final_project.NasaModel;
import com.example.final_project.R;
import com.example.final_project.Utils;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<NasaModel>{

    private Context context;
    private List<NasaModel> data;
    private SQLiteDatabaseHelper helper;

    public NewsAdapter(Context context, List<NasaModel> data) {
        super(context, R.layout.news_item, data);
        this.context = context;
        this.data = data;
        helper = new SQLiteDatabaseHelper(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View rowView = LayoutInflater.from(context).inflate(R.layout.news_item, parent,false);

        NasaModel model = data.get(position);

        ImageView image = rowView.findViewById(R.id.image);
        TextView title = rowView.findViewById(R.id.title);
        TextView date = rowView.findViewById(R.id.date);
        TextView explanation = rowView.findViewById(R.id.explanation);
        LinearLayout root = rowView.findViewById(R.id.drag_item);
        AppCompatImageView right_view = rowView.findViewById(R.id.right_view);

        Typeface typeface = ResourcesCompat.getFont(context,R.font.montserrat_bold);

        title.setText(model.getTitle());
        explanation.setText(model.getExplanation());
        date.setText(Utils.formatDate(model.getDate()));
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.image_placeholder);
        requestOptions.error(R.drawable.image_placeholder);
        Glide.with(context).load(model.getUrl()).apply(requestOptions).into(image);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Details.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("data",model);
                context.startActivity(intent);
            }
        });

        right_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.removeFromViewed(String.valueOf(model.getId()));
                data.remove(model);
                Snackbar.make(rowView,context.getResources().getString(R.string.deleted),Snackbar.LENGTH_LONG).show();
                notifyDataSetChanged();
            }
        });

        title.setTypeface(typeface);

        return rowView;
    }

}
