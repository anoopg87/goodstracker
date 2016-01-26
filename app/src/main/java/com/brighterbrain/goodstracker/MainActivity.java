package com.brighterbrain.goodstracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    DBhandler mdDBhandler;
    List<Asset> mAssetList;
    RecyclerView mAssetListView;
    RecyclerView.LayoutManager mLinearLayoutManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Asset List");
        mdDBhandler=new DBhandler(MainActivity.this);

        mAssetList= mdDBhandler.getAllAsset();

        mAssetListView=(RecyclerView)findViewById(R.id.assetListView);
        mLinearLayoutManage=new LinearLayoutManager(MainActivity.this);
        mAssetListView.setLayoutManager(mLinearLayoutManage);
        if(mAssetList.isEmpty()){
            ((TextView)findViewById(R.id.noAsset)).setVisibility(View.VISIBLE);
        }else{
            ((TextView)findViewById(R.id.noAsset)).setVisibility(View.GONE);
        }

        mAssetListView.setAdapter(new assetAdapter(mAssetList, new assetAdapter.onItemClickListener() {
            @Override
            public void OnClick(View view, int position) {

                 Asset item=mAssetList.get(position);
                Log.i("Asset id",item.get_ID());

                Intent in=new Intent(MainActivity.this,AssetDetails.class);
                in.putExtra(AssetDetails.ITEM,(Parcelable)item);
                startActivity(in);

            }
        }));




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in=new Intent(MainActivity.this,AssetDetails.class);
                startActivity(in);


            }
        });
    }


    public static class assetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        public static interface  onItemClickListener{
            void OnClick(View view,int position);
        }


        List<Asset> dataSet;
        public static onItemClickListener onClickListener;



        public assetAdapter(List<Asset> assetList,onItemClickListener onClickListener) {
            dataSet=assetList;
            this.onClickListener=onClickListener;
        }

        public static  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private   static ImageView imgAsset;
            private  static TextView txtName;
            private  static TextView txtCost;
            private onItemClickListener onItemClickLister;

            public void setOnItemClickLister(onItemClickListener onItemClickLister){
                this.onItemClickLister=onItemClickLister;

            }

            public ViewHolder(View itemView) {
                super(itemView);
                imgAsset= (ImageView) itemView.findViewById(R.id.imgAsset);
                txtName=(TextView)itemView.findViewById(R.id.txtName);
                txtCost=(TextView)itemView.findViewById(R.id.txtCost);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                onItemClickLister.OnClick(v,getAdapterPosition());

            }
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.asset_view_layout,parent,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ((ViewHolder)holder).txtName.setText(dataSet.get(position).getName());
            ((ViewHolder)holder).txtCost.setText(dataSet.get(position).getCost());
            if("".equals(dataSet.get(position).getImageUrl())){
                ((ViewHolder) holder).imgAsset.setImageResource(R.drawable.noimage);

            }else {
                ((ViewHolder) holder).imgAsset.setImageURI(Uri.parse(dataSet.get(position).getImageUrl()));
            }
            ((ViewHolder) holder).setOnItemClickLister(this.onClickListener);


        }

        @Override
        public int getItemCount() {
            return null!=dataSet?dataSet.size():0;
        }

        public Asset getItemAtPosition(int position){

            return null!=dataSet?dataSet.get(position):null;
        }
    }

}
