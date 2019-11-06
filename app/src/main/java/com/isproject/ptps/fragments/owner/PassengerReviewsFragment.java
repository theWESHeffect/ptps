package com.isproject.ptps.fragments.owner;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.isproject.ptps.DataModelsAdapter;
import com.isproject.ptps.DataObject;
import com.isproject.ptps.NumberPlate;
import com.isproject.ptps.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PassengerReviewsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<DataObject> mDataObjects = new ArrayList<>();

    public PassengerReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_passenger_reviews, container, false);
        recyclerView = view.findViewById(R.id.licencePlatesRecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lean = new LinearLayoutManager(getContext());
        lean.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(lean);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String userUid = FirebaseAuth.getInstance().getUid();
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").child(userUid)
                .child("vehicles");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                NumberPlate plate = dataSnapshot.getValue(NumberPlate.class);
                mDataObjects.add(plate);

                DataModelsAdapter adapter = new DataModelsAdapter(mDataObjects, null, getContext());
                adapter.setmCallback(new DataModelsAdapter.OnNumberPlateClicked() {
                    @Override
                    public void sendNumberPlate(String numberPlate) {
                        //Toast.makeText(getContext(), "" + numberPlate + " clicked!", Toast.LENGTH_SHORT).show();

                        Fragment fragment = new ReviewsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("LICENCE_PLATE", numberPlate);
                        fragment.setArguments(bundle);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.fl_content, fragment);
                        ft.commit();
                    }
                });
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
