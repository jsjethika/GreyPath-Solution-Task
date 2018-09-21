package in.jethika.greypathsolution.mytaskapplication.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;

import in.jethika.greypathsolution.mytaskapplication.CallBackForDrawable;
import in.jethika.greypathsolution.mytaskapplication.R;
import in.jethika.greypathsolution.mytaskapplication.asyTask.LoadBackground;
import in.jethika.greypathsolution.mytaskapplication.databinding.ItemRecyclerviewBinding;
import in.jethika.greypathsolution.mytaskapplication.quoteForTheDayModel.Quotes;
import in.jethika.greypathsolution.mytaskapplication.utility.Utils;

public class QuoteRecyclerViewAdapter extends RecyclerView.Adapter<QuoteRecyclerViewAdapter.ViewHolder> implements CallBackForDrawable {

    List<Quotes> quotesList;
    Activity activity;
    ItemRecyclerviewBinding itemRecyclerviewBinding;

    public QuoteRecyclerViewAdapter(Activity activity, List<Quotes> quotesList) {
        this.quotesList = quotesList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder (DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_recyclerview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        itemRecyclerviewBinding = holder.getItemRecyclerviewBinding();
        itemRecyclerviewBinding.itemTitle.setText(quotesList.get(position).getTitle());
        itemRecyclerviewBinding.itemText.setText(quotesList.get(position).getQuote());

        Utils.showProgressDialog(activity);
        new LoadBackground(this).execute(new String[]{quotesList.get(position).getBackground()});
    }

    @Override
    public int getItemCount() {
        return quotesList.size();
    }

    @Override
    public void setDrawableFromURL(Drawable drawable) {
        Utils.hideProgressDialog();
        itemRecyclerviewBinding.contentLayout.setBackgroundDrawable(drawable);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ItemRecyclerviewBinding itemRecyclerviewBinding;

        public ViewHolder(ItemRecyclerviewBinding itemRecyclerviewBinding) {
            super(itemRecyclerviewBinding.getRoot());
            this.itemRecyclerviewBinding = itemRecyclerviewBinding;
            itemRecyclerviewBinding.executePendingBindings();
        }

        public ItemRecyclerviewBinding getItemRecyclerviewBinding() {
            return itemRecyclerviewBinding;
        }
    }
}
