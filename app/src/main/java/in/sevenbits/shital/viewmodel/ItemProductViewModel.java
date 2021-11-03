package in.sevenbits.shital.viewmodel;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import in.sevenbits.shital.R;
import in.sevenbits.shital.model.Product;
import in.sevenbits.shital.util.AppUtil;

public class ItemProductViewModel extends AndroidViewModel {

    private Context context;
    public Product product;
    public int index;

    public ItemProductViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }

    public void setProduct(Product product, int index) {
        this.product = product;
        this.index = index;
    }

    public int getSelectionVisibility() {
        return product.isSelected() ? View.VISIBLE : View.GONE;
    }

    public int getSelectionForground() {
        return product.isSelected() ? Color.parseColor("#99000000") : Color.TRANSPARENT;
    }

    public String getProductThumbnail() {
        return product.image;
    }

    @BindingAdapter("productThumbnail")
    public static void setProductThumbnail(ImageView imageView, String url) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo);
        requestOptions.error(R.drawable.logo);

        Glide.with(imageView.getContext()).setDefaultRequestOptions(requestOptions).load(url).into(imageView);
    }

}
