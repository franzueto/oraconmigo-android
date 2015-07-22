package org.guateora.oraconmigo.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.guateora.oraconmigo.R;
import org.guateora.oraconmigo.models.Testimonial;

import java.util.List;

/**
 * Created by franz on 7/11/2015.
 */
public class RVTestimonialsAdapter extends RecyclerView.Adapter<RVTestimonialsAdapter.TestimonialViewHolder>{

    private List<Testimonial> testimonials;
    private Context context;

    public RVTestimonialsAdapter(Context context, List<Testimonial> testimonials){
        this.context = context;
        this.testimonials = testimonials;
    }

    @Override
    public TestimonialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_testimonial, parent, false);
        TestimonialViewHolder tvh = new TestimonialViewHolder(v);
        return tvh;
    }

    @Override
    public void onBindViewHolder(TestimonialViewHolder holder, int position) {
        holder.testimonialName.setText(testimonials.get(position).getAuthor());
        holder.testimonialText.setText(testimonials.get(position).getText());

        Picasso.with(context)
                .load(testimonials.get(position).getAuthorPhotoURL())
                .placeholder(R.drawable.avatar)
                .into(holder.testimonialPhoto);
    }

    @Override
    public int getItemCount() {
        return testimonials.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class TestimonialViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView testimonialName;
        TextView testimonialText;
        ImageView testimonialPhoto;

        TestimonialViewHolder(View itemView) {
            super(itemView);
            testimonialName = (TextView)itemView.findViewById(R.id.testimonial_name);
            testimonialText = (TextView)itemView.findViewById(R.id.testimonial_text);
            testimonialPhoto = (ImageView)itemView.findViewById(R.id.testimonial_photo);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            /*Intent intent = new Intent(v.getContext(), ProfileActivity.class);
            Utils.savePreference(v.getContext(), Utils.SELECTED_CANDIDATE, String.valueOf(candidatos.get(getPosition()).id));
            //intent.putExtra(ProfileActivity.ID_CANDIDATO, String.valueOf(candidatos.get(getPosition()).id));
            v.getContext().startActivity(intent);*/
        }
    }
}
