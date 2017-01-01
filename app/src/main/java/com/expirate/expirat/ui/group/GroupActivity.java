package com.expirate.expirat.ui.group;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.expirate.expirat.R;
import com.expirate.expirat.repository.groceries.GroceriesRepository;
import com.expirate.expirat.repository.groceries.local.LocalGroceriesDataSource;
import com.expirate.expirat.services.response.GroceriesItem;
import com.expirate.expirat.services.response.TypesItem;
import com.expirate.expirat.ui.BaseActiviy;
import com.expirate.expirat.ui.input.InputActivity;
import com.expirate.expirat.ui.widget.EmptyView;
import com.expirate.expirat.utils.ColorUtils;
import com.expirate.expirat.utils.Constant;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

public class GroupActivity extends BaseActiviy implements GroupContract.View,
        GroceriesAdapter.ClickListener {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.recyclerview) RecyclerView recyclerView;
    @Bind(R.id.fab_add) FloatingActionButton addFab;
    @Bind(R.id.empty_view) EmptyView emptyView;

    private GroupContract.Presenter presenter;

    private GroceriesAdapter adapter;

    public static Intent createIntent(Context context) {
        return new Intent(context, GroupActivity.class);
    }

    public static Intent createIntentWithBundle(Context context, long id) {
        Intent intent = createIntent(context);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.EXTRA_TYPE_ID, id);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        new GroupPresenter(new GroceriesRepository(LocalGroceriesDataSource.newInstance(this)), this);

        setupToolbar(toolbar, null, true);
        setupContent();
    }

    private void setupContent() {
        addFab.setOnClickListener(v -> openInputActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new GroceriesAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void openInputActivity() {
        Intent intent = InputActivity.createIntent(this);
        startActivity(intent);
    }

    @Override
    public void setPresenter(GroupContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    protected void onPause() {
        presenter.unSubscribe();
        super.onPause();
    }

    @Override
    public void showLoading(boolean onLoading) {

    }

    @Override
    public void showList(List<GroceriesItem> groceriesItems) {
        emptyView.hide();
        adapter.setItems(groceriesItems);
    }

    @Override
    public void showError() {
        View view = checkNotNull(this.getCurrentFocus());
        Snackbar.make(view, "Oops, something bad happend :O", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showEmpty() {
       emptyView.show();
    }

    @Override
    public long getTypeId() {
        if (getIntent().getExtras() == null) {
            return -1;
        }
        return getIntent().getLongExtra(Constant.EXTRA_TYPE_ID, -1);
    }

    @Override
    public void setToolbarTitle(TypesItem typesItem) {
        if (getSupportActionBar() == null) {
            throw new IllegalStateException("Activity must implement toolbar");
        }

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(ColorUtils.colorById(this, (int) typesItem.id())));

        getSupportActionBar().setTitle(typesItem.typesName());
    }

    @Override
    public void onItemClickListener(long id) {
        Intent intent = InputActivity.createIntentWithBundle(this, id);
        startActivity(intent);
    }

    @Override
    public void onItemMoreClickListener(long id, View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_pop_up, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_edit:
                    onItemClickListener(id);
                    return true;
                case R.id.action_delete:
                    confirmDelete(id);
                    return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void confirmDelete(long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete this item?");
        builder.setPositiveButton("Yes", (dialog, which) -> presenter.deleteGrocery(id));
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
