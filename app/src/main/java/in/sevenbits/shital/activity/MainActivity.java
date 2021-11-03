package in.sevenbits.shital.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.sevenbits.shital.R;
import in.sevenbits.shital.adapter.ProductListAdapter;
import in.sevenbits.shital.base.BaseActivity;
import in.sevenbits.shital.databinding.ActivityMainBinding;
import in.sevenbits.shital.interfaces.OnProductClickListener;
import in.sevenbits.shital.model.Product;
import in.sevenbits.shital.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity implements OnProductClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MainViewModel viewModel;
    private ActivityMainBinding binding;
    private ProductListAdapter productListAdapter;
    private List<Product> productList = new ArrayList<>();
    private boolean inSelectionMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setToolbar(binding.toolbar, false, false);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        binding.setMainViewModel(viewModel);

        productListAdapter = new ProductListAdapter(this, this);
        binding.rvProducts.setAdapter(productListAdapter);

        binding.rvProducts.setLayoutManager(new GridLayoutManager(this, 2));


        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("bits-a17d6-default-rtdb");

        DatabaseReference productsRef = databaseRef.getRoot();
        productsRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList.clear();

                HashMap<String , HashMap<String, Object>> data = (HashMap<String, HashMap<String, Object>>) dataSnapshot.getValue(true);

                data.forEach((string, product) ->
                        productList.add(new Product((long)product.get("id"), (String) product.get("image"), (String) product.get("name")))
                );

                productListAdapter.submitList(productList);
                productListAdapter.notifyDataSetChanged();

                viewModel.isLoading.set(false);
                viewModel.messageVisibility.set(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
                viewModel.isLoading.set(false);
                if(productList.size() == 0){
                    viewModel.messageVisibility.set(true);
                }
            }
        });

        binding.toolbarSelection.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelectionMode();
            }
        });

        binding.toolbarSelection.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.action_select_all){
                    applySelectAll();
                }
                return false;
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_account) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, SplashActivity.class);
            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onProductLongClicked(long id) {
        for(Product product : productList){
            if(id == product.id) {
                int i = productList.indexOf(product);
                product.setSelected(!product.isSelected());
                productList.set(i, product);
            }
        }

       updateToolbarAndSelectionMode();

    }

    @Override
    public void onProductClicked(long id) {
        for(Product product : productList){
            if(id == product.id && inSelectionMode) {
                int i = productList.indexOf(product);
                product.setSelected(!product.isSelected());
                productList.set(i, product);
            }
        }
        updateToolbarAndSelectionMode();
    }

    private void updateToolbarAndSelectionMode(){
        productListAdapter.submitList(productList);
        productListAdapter.notifyDataSetChanged();
        int count = 0;
        for (Product product : productList){
            if(product.isSelected()){
                count++;
            }
        }

        inSelectionMode = count > 0;

        binding.toolbar.setVisibility(inSelectionMode ? View.GONE : View.VISIBLE);
        binding.toolbarSelection.setVisibility(inSelectionMode ? View.VISIBLE : View.GONE);

        binding.toolbarSelection.setTitle(count + " Selected");
    }

    private void clearSelectionMode(){
        for(Product product : productList){
            int i = productList.indexOf(product);
            product.setSelected(false);
            productList.set(i, product);
        }
        updateToolbarAndSelectionMode();
    }

    private void applySelectAll(){
        for(Product product : productList){
            int i = productList.indexOf(product);
            product.setSelected(true);
            productList.set(i, product);
        }
        updateToolbarAndSelectionMode();
    }
}