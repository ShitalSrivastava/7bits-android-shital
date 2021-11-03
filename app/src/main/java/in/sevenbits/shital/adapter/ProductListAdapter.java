package in.sevenbits.shital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import in.sevenbits.shital.R;
import in.sevenbits.shital.databinding.ItemProductBinding;
import in.sevenbits.shital.interfaces.OnProductClickListener;
import in.sevenbits.shital.model.Product;
import in.sevenbits.shital.viewmodel.ItemProductViewModel;

public class ProductListAdapter extends ListAdapter<Product, ProductListAdapter.ListAdapterViewHolder> {

    private OnProductClickListener onProductClickListener;
    private Context context;

    public ProductListAdapter(Context context, OnProductClickListener onProductClickListener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.onProductClickListener = onProductClickListener;
    }

    private static final DiffUtil.ItemCallback<Product> DIFF_CALLBACK = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(Product oldItem, Product newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(Product oldItem, Product newItem) {
            return oldItem.isSelected() == newItem.isSelected();
        }
    };

    @NonNull
    @Override
    public ListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding itemProductBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_product,
                parent, false);
        return new ListAdapterViewHolder(itemProductBinding, onProductClickListener);
    }

    @Override
    public void onBindViewHolder(ListAdapterViewHolder holder, int position) {
        holder.bindProperty(getItem(position));
    }

    public class ListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ItemProductBinding itemProductBinding;
        private ItemProductViewModel itemProductViewModel;
        private OnProductClickListener onProductClickListener;
        private Product product;

        ListAdapterViewHolder(ItemProductBinding itemProductBinding, OnProductClickListener onProductClickListener) {
            super(itemProductBinding.getRoot());
            this.itemProductBinding = itemProductBinding;
            this.onProductClickListener = onProductClickListener;

            this.itemProductBinding.getRoot().setOnClickListener(this);
            this.itemProductBinding.getRoot().setOnLongClickListener(this);
        }

        void bindProperty(Product product) {
            this.product = product;
            itemProductViewModel = new ItemProductViewModel(((FragmentActivity) itemProductBinding.getRoot().getContext()).getApplication());
            itemProductViewModel.setProduct(product, getLayoutPosition());
            itemProductBinding.setItemProductViewModel(itemProductViewModel);

        }


        @Override
        public void onClick(View v) {
            onProductClickListener.onProductClicked(product.id);
        }

        @Override
        public boolean onLongClick(View v) {
            onProductClickListener.onProductLongClicked(product.id);
            return false;
        }
    }
}
