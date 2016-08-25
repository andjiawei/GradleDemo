package com.netsite.galleryanimation;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.netsite.galleryanimation.widget.FoldLayout;

/**
 * Created by vincentpaing on 6/7/16.
 */
public class OverlapFragment extends Fragment {

  int resourceId;
  static final String ARG_RES_ID = "ARG_RES_ID";

  public static OverlapFragment newInstance(int resourceId) {
    OverlapFragment overlapFragment = new OverlapFragment();
    Bundle bundle = new Bundle();
    bundle.putInt(ARG_RES_ID, resourceId);
    overlapFragment.setArguments(bundle);
    return overlapFragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    resourceId = getArguments().getInt(ARG_RES_ID);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.item_gallery, container, false);
    final FoldLayout foldLayout= (FoldLayout) rootView.findViewById(R.id.fold_layout);
    final Button button=(Button)rootView.findViewById(R.id.button);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

//         foldLayout.startAnamation();
        ObjectAnimator.ofFloat(foldLayout, "factor", 1, 0, 1).setDuration(3000).start();
      }
    });

    return rootView;
  }
}
