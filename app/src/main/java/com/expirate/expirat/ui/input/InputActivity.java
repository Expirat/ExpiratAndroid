package com.expirate.expirat.ui.input;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.expirate.expirat.R;
import com.expirate.expirat.repository.groceries.GroceriesRepository;
import com.expirate.expirat.repository.groceries.local.LocalGroceriesDataSource;
import com.expirate.expirat.services.response.GroceriesItem;
import com.expirate.expirat.ui.BaseActiviy;
import com.expirate.expirat.utils.Constant;
import com.expirate.expirat.utils.StringUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

public class InputActivity extends BaseActiviy implements InputContract.View,
        DatePickerDialog.OnDateSetListener {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.name_input) EditText inputName;
    @Bind(R.id.buy_date_input) TextView inputBuyDate;
    @Bind(R.id.expired_date_input) TextView inputExpiredDate;
    @Bind(R.id.save_button) TextView btnSave;

    private InputContract.Presenter presenter;

    public static Intent createIntent(Context context) {
        return new Intent(context, InputActivity.class);
    }

    public static Intent createIntentWithBundle(Context context, long id) {
        Intent intent = createIntent(context);

        Bundle bundle = new Bundle();
        bundle.putLong(Constant.EXTRA_ID, id);

        intent.putExtras(bundle);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        ButterKnife.bind(this);

        new InputPresenter(
                new GroceriesRepository(LocalGroceriesDataSource.newInstance(this)),
                this);

        setupToolbar(toolbar, null, true);
        setupContent();
    }

    private void setupContent() {
        btnSave.setOnClickListener(v -> {

            hideSoftKeyboard();

            String productName = inputName.getText().toString();
            String buyDate = inputBuyDate.getText().toString();
            String expiredDate = inputExpiredDate.getText().toString();

            presenter.saveGrocery(productName, buyDate, expiredDate);
        });

        inputBuyDate.setText(StringUtils.getToday());

        inputExpiredDate.setText(StringUtils.getToday());

        inputBuyDate.setOnClickListener(v -> openDatePickerDialog(Constant.TAG_BUY_DATE));

        inputExpiredDate.setOnClickListener(v -> openDatePickerDialog(Constant.TAG_EXPIRED_DATE));
    }

    private void openDatePickerDialog(String tag) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), tag);
    }

    private void hideSoftKeyboard() {
        View view = checkNotNull(this.getCurrentFocus());

        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void setPresenter(InputContract.Presenter presenter) {
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
    public long getGrocerieId() {
        if (getIntent().getExtras() == null) {
            return -1;
        }

        return getIntent().getLongExtra(Constant.EXTRA_ID, -1);
    }

    @Override
    public void setData(GroceriesItem groceriesItem) {
        inputName.setText(groceriesItem.name());
        inputBuyDate.setText(StringUtils.getStringDate(groceriesItem.buyDate()));
        inputExpiredDate.setText(StringUtils.getStringDate(groceriesItem.expiredDate()));
    }

    @Override
    public void showError() {
        View view = checkNotNull(this.getCurrentFocus());

        Snackbar.make(view, R.string.error_msg_data_not_found, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void emptyProductName() {
        inputName.setError(getString(R.string.error_msg_name_empty));
        inputName.requestFocus();
    }

    @Override
    public void hideError() {
        inputName.setError(null);
    }

    @Override
    public void saveMessage() {
        View view = checkNotNull(this.getCurrentFocus());

        Snackbar.make(view, R.string.msg_data_saved, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void errorSameDateBuyAndExpired() {
        View view = checkNotNull(this.getCurrentFocus());

        Snackbar.make(view,
                R.string.error_msg_buy_date_same_as_expired_date,
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void errorBiggerDateBuyThanExpired() {
        View view = checkNotNull(this.getCurrentFocus());

        Snackbar.make(view,
                R.string.error_msg_buy_date_bigger_than_expired_date,
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String tag = view.getTag();

        switch (tag) {
            case Constant.TAG_BUY_DATE:
                inputBuyDate.setText(StringUtils.setDate(year, monthOfYear, dayOfMonth));
                break;
            case Constant.TAG_EXPIRED_DATE:
                inputExpiredDate.setText(StringUtils.setDate(year, monthOfYear, dayOfMonth));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
