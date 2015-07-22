package org.guateora.oraconmigo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.guateora.oraconmigo.R;
import org.guateora.oraconmigo.adapters.RVTestimonialsAdapter;
import org.guateora.oraconmigo.models.Testimonial;
import org.guateora.oraconmigo.utils.ParseConstants;
import org.guateora.oraconmigo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by franz on 7/20/2015.
 */
public class FragmentTestimonials extends Fragment {

    private static final String TAG = "FragmentTestimonials";

    private List<Testimonial> testimonials;

    private RVTestimonialsAdapter adapter;
    private RecyclerView rv_testimonials;

    private ProgressBar progressBar;

    public static FragmentTestimonials newInstance() { return new FragmentTestimonials(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_testimonials, container, false);

        rv_testimonials = (RecyclerView) view.findViewById(R.id.rv_testimonials);
        rv_testimonials.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv_testimonials.setLayoutManager(llm);

        progressBar = (ProgressBar) view.findViewById(R.id.testimonials_progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        getTestimonialsData(view);

        return view;
    }

    private void getTestimonialsData(final View parent){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.TABLE_TESTIMONIAL);
        query.include(ParseConstants.TABLE_TESTIMONIAL_FIELD_USER);
        query.whereEqualTo(ParseConstants.TABLE_TESTIMONIAL_FIELD_APPROVED, true);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> listTestimonials, ParseException e) {
                if(e == null){
                    testimonials = new ArrayList<Testimonial>();
                    for(ParseObject testimonial : listTestimonials){
                        ParseUser user = testimonial.getParseUser(ParseConstants.TABLE_TESTIMONIAL_FIELD_USER);

                        testimonials.add(
                                new Testimonial(
                                        user.getString(ParseConstants.TABLE_USER_FIELD_NAME)
                                        , testimonial.getString(ParseConstants.TABLE_TESTIMONIAL_FIELD_TEXT)
                                        , String.format(Utils.FACEBOOK_SQUARE_PICTURE, user.getString(ParseConstants.TABLE_USER_FIELD_FBID))
                                )
                        );
                    }

                    handleTestimonialsList(testimonials);
                } else{
                    Snackbar
                            .make(parent, R.string.error_unknown, Snackbar.LENGTH_LONG)
                            .setAction(R.string.try_again, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getTestimonialsData(parent);
                                }
                            })
                            .show();
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }

    private void handleTestimonialsList(final List<Testimonial> testimonials){
        this.testimonials = testimonials;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);

                adapter = new RVTestimonialsAdapter(getActivity(), testimonials);
                rv_testimonials.setAdapter(adapter);
            }
        });
    }
}
