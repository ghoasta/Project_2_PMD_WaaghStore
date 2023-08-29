package com.example.project2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Products_RV_Adapter extends RecyclerView.Adapter<Products_RV_Adapter.MyViewHolder> implements Filterable {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<ProductsModel> productsModels;
    ArrayList<ProductsModel> exampleListFull;

    public Products_RV_Adapter(Context context, ArrayList<ProductsModel> productsModels,
                               RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.productsModels = productsModels;
        this.recyclerViewInterface = recyclerViewInterface;
        exampleListFull = new ArrayList<>(productsModels);

    }

    @NonNull
    @Override
    public Products_RV_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row,parent,false);
        return new Products_RV_Adapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Products_RV_Adapter.MyViewHolder holder, int position) {
        holder.tvName.setText(productsModels.get(position).getProductName());
        holder.tvType.setText(productsModels.get(position).getProductType());
        holder.tvPrice.setText(productsModels.get(position).getProductPrice());
        holder.imageView.setImageResource(productsModels.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return productsModels.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<ProductsModel> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(exampleListFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(ProductsModel item : exampleListFull){
                    if (item.getProductName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            productsModels.clear();
            productsModels.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView tvName,tvType,tvPrice;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            tvName = itemView.findViewById(R.id.textView_basket_name);
            tvType = itemView.findViewById(R.id.textView_basketId);
            tvPrice = itemView.findViewById(R.id.textView_bakset_quantity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.OnItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
