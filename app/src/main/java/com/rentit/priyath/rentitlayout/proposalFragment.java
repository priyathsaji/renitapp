package com.rentit.priyath.rentitlayout;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class proposalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    ArrayList<proposalAndHistoryData> proposalAndHistoryDatas;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayoutManager linearLayoutManager;
    Context context;
    globalData globaldata;
    String url;
    public proposalFragment() {
        // Required empty public constructor
    }

    public static proposalFragment newInstance(String param1, String param2) {
        proposalFragment fragment = new proposalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        globaldata = (globalData)getActivity().getApplicationContext();
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_proposal, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.proposalRecyclerview);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        progressBar = (ProgressBar)view.findViewById(R.id.proposalprogressbar);

        proposalAndHistoryDatas = new ArrayList<>();

        myAsyncTask task = new myAsyncTask();
        task.execute("priyathsaji");


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public class myAsyncTask extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... params) {
            HttpGet httpGet = new HttpGet();
            String link = "http://rentitapi.herokuapp.com/get_proposal?toId="+globaldata.getUserId();
            try {
                String Response = httpGet.getData(link);
                return Response;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String result){
            try {
                   JSONArray jsonArray = new JSONArray(result);
                   for(int i=0;i<jsonArray.length();i++) {
                        JSONObject js = null;
                        js = jsonArray.getJSONObject(i);
                        proposalAndHistoryData data = new proposalAndHistoryData();
                        data.name = js.getString("name");
                        data.productname = js.getString("productName");
                        data.phoneNumber = js.getString("phoneNumber");
                        data.Rent = js.getInt("rent");
                        data.CusomerId = js.getString("fromId");
                        data.OwnerId = js.getString("toId");
                       //data.type = js.getInt("type");
                       data.productId=js.getString("productId");
                        data.Status = "";
                       data.isproposal = true;
                       proposalAndHistoryDatas.add(data);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            proposalAdapter adapter = new proposalAdapter(proposalAndHistoryDatas, context) {
                @Override
                public void button1function(proposalAndHistoryData data) {

                }

                @Override
                public void button2function(proposalAndHistoryData data) {
                    url = "http://rentitapi.herokuapp.com/disapprove?fromId="+globaldata.getUserId()+"&toId="+data.CusomerId+"&productId="+data.productId;
                    decisionTask task = new decisionTask();
                    task.execute(1);
                }

                @Override
                public void button3function(proposalAndHistoryData data) {
                    url = "http://rentitapi.herokuapp.com/approve?fromId="+globaldata.getUserId()+"&toId="+data.CusomerId+"&productId="+data.productId+"&type="+data.type;
                    decisionTask task = new decisionTask();
                    task.execute(1);
                }
            };
            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);


        }
    }

    public class decisionTask extends AsyncTask<Integer,Void,String>{

        @Override
        protected String doInBackground(Integer... params) {
            HttpGet httpGet = new HttpGet();
            try {
                String response = httpGet.getData(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
