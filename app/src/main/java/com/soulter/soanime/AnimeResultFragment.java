package com.soulter.soanime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

public class AnimeResultFragment extends Fragment {

    private IntentFilter intentFilter;

    private TextView animeTitle;
    private TextView animeDetails;
    private CardView titleFiled;
    private CardView detailsFiled;
    private CardView videoFiled;
    private CardView resultWarning;

    String animeTitleStr;
    String animeDetailsStr;
    Float resultSimilarity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//
        Bundle bundle = getArguments();

        if (bundle != null){
            animeTitleStr = bundle.getString("anime_result_title");
            animeDetailsStr = bundle.getString("anime_result_details");
            resultSimilarity = bundle.getFloat("anime_result_similarity");
            Log.v("tag","get: "+animeTitle+animeDetailsStr);

        }

        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_pager1, container, false);

//        SharedPreferences spfs = getActivity().getSharedPreferences("spfs", Context.MODE_PRIVATE);
//        spfs.getString("anime_data",)







        animeTitle = (TextView)view.findViewById(R.id.anime_title);
        animeDetails = (TextView)view.findViewById(R.id.anime_details);
        titleFiled = (CardView)view.findViewById(R.id.title_filed);
        detailsFiled = (CardView)view.findViewById(R.id.details_filed);
        videoFiled = view.findViewById(R.id.video_filed);
        resultWarning = view.findViewById(R.id.result_warning);

//        wvBookPlay = (WebView)view.findViewById(R.id.preview_video);

        /*
         * 如果是其他逻辑
         * 例如 recycleList等 就在这里面继续设置数据
         * */

        if (animeTitleStr != null && animeDetailsStr!=null){
            displayResult(animeTitleStr,animeDetailsStr,resultSimilarity);

        }

        return view;
    }




    public void displayResult(String title ,String details ,Float similarity){

        try {//草泥马戈壁要try还不提示 你妈死了

            if (similarity < 87){
                resultWarning.setVisibility(View.VISIBLE);

            }else{
                titleFiled.post(new Runnable() {
                    @Override
                    public void run() {
                        titleFiled.setPadding(28,28,28,28);
                    }
                });
            }

            videoFiled.post(new Runnable() {
                @Override
                public void run() {
                    videoFiled.setVisibility(View.VISIBLE);
                }
            });
            titleFiled.post(new Runnable() {
                @Override
                public void run() {
                    titleFiled.setVisibility(View.VISIBLE);
                }
            });
            detailsFiled.post(new Runnable() {
                @Override
                public void run() {
                    detailsFiled.setVisibility(View.VISIBLE);
                }
            });
            animeTitle.post(new Runnable() {
                @Override
                public void run() {
                    animeTitle.setText(title);

                }
            });

            animeDetails.post(new Runnable() {
                @Override
                public void run() {
                    animeDetails.setText(details);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onDestroyView() {
//        getActivity().unregisterReceiver(myReceiver);
        super.onDestroyView();
    }
}
