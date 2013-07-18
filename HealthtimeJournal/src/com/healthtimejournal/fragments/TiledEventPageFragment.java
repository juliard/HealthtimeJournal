package com.healthtimejournal.fragments;

import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridLayout.Spec;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.healthtimejournal.R;


public class TiledEventPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String JSON_STRING = "JSON_DATA";

    private int mPage;
    private JSONArray json;
    
    private int src[] = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5};//for debug purpose only
    private int n = 0;//for debug purpose only

    public static TiledEventPageFragment create(int page, JSONArray json) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(JSON_STRING, json.toString());
        TiledEventPageFragment fragment = new TiledEventPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Page", String.valueOf(mPage));
        mPage = getArguments().getInt(ARG_PAGE);
        try {
			json = new JSONArray(getArguments().getString(JSON_STRING));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Log.d("Length", String.valueOf(json.length()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
    	Point size = new Point();
    	getActivity().getWindowManager().getDefaultDisplay().getSize(size);
    	int screenWidth = size.x;
    	int halfScreenWidth = (int)(screenWidth *0.5);
    	int quarterScreenWidth = (int)(halfScreenWidth * 0.5);
    	
    	View view = inflater.inflate(R.layout.event_grid, container, false);
        ScrollView scroll = (ScrollView) view;
    	
    	GridLayout grid = (GridLayout)scroll.findViewById(R.id.timeline_gridlayout);
        grid.setColumnCount(2);
        grid.setRowCount(100);//for debug purpose only (should be dynamic)
        
        n = 0;//for debug purpose only
        int spec_col = 0, spec_row = 0;
        for(int i = 0; i < json.length(); i++){
        	Spec row = null, col = null;
        	int width = 0, height = 0;
        	final FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.event_item, container, false);
        	
        	row = GridLayout.spec(spec_row);
			col = GridLayout.spec(spec_col, 1);
			width = quarterScreenWidth * 2;
			height = quarterScreenWidth;
			spec_col++;
			
			if(spec_col == 2){
				spec_col = 0;
				spec_row ++;
			}
        	//for debug purpose only
        	/*switch(i){
        		case 0:
        			row = GridLayout.spec(0, 2);
        			col = GridLayout.spec(0, 2);
        			width = screenWidth;
        			height = quarterScreenWidth * 2;
        			break;
        		case 1:
        			row = GridLayout.spec(2);
        			col = GridLayout.spec(0, 1);
        			width = quarterScreenWidth * 2;
        			height = quarterScreenWidth;
        			break;
        		case 2:
        			row = GridLayout.spec(2);
        			col = GridLayout.spec(1, 1);
        			width = quarterScreenWidth * 2;
        			height = quarterScreenWidth;
        			break;
        		case 3:
        			row = GridLayout.spec(3, 2);
        			col = GridLayout.spec(0, 1);
        			width = quarterScreenWidth * 2;
        			height = quarterScreenWidth * 2;
        			break;
        		case 4:
        			row = GridLayout.spec(3);
        			col = GridLayout.spec(1, 1);
        			width = quarterScreenWidth * 2;
        			height = quarterScreenWidth;
        			break;
        		case 5:
        			row = GridLayout.spec(4);
        			col = GridLayout.spec(1, 1);
        			width = quarterScreenWidth * 2;
        			height = quarterScreenWidth;
        			break;
        	}//for debug purpose only*/
        	try {
				layout.setTag(json.getJSONObject(i).getString("post_content"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), (CharSequence) layout.getTag(), Toast.LENGTH_SHORT).show();
				}
			});
        	
            grid.addView(layout, populateGrid(layout, width, height, row, col, i));
            n++;//for debug purpose only
        }
        
        return view;
    }
    
    private GridLayout.LayoutParams populateGrid(FrameLayout layout, int width, int height, GridLayout.Spec row, GridLayout.Spec col, int count){
    	ImageView img = (ImageView) layout.findViewById(R.id.timeline_tile_img);
    	GridLayout.LayoutParams params = new GridLayout.LayoutParams(row, col);
        params.width = width;
        params.height = height;
        
        TextView text = (TextView) layout.findViewById(R.id.timeline_item_title);
    	try {
			text.setText(json.getJSONObject(count).getString("post_content"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        img.setImageResource(src[n%5]);//for debug purpose only
        
        return params;
    }
}