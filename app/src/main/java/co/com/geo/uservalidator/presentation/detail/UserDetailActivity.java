package co.com.geo.uservalidator.presentation.detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.ProgressBar;

import java.util.List;

import javax.inject.Inject;

import co.com.geo.uservalidator.R;
import co.com.geo.uservalidator.data.model.IntentEntity;
import co.com.geo.uservalidator.data.model.UserEntity;
import co.com.geo.uservalidator.presentation.ValidatorApp;
import co.com.geo.uservalidator.presentation.login.LoginViewModel;

public class UserDetailActivity extends AppCompatActivity {

   public static final String PARAM_USER_NAME = "user_name";

    public static Intent intent(Context context, String username) {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra(PARAM_USER_NAME, username);
        return intent;
    }

    private String mUsername = "";

    private UserDetailViewModel detailViewModel;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    private ProgressBar myProgressBar;

    private IntentListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        inject();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mUsername = extras.getString(PARAM_USER_NAME);
        }

        this.setTitle(mUsername);
        setUpViewModel();
        initView();
        if (savedInstanceState == null) {
            detailViewModel.loadIntents(mUsername);
        }
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.intent_list_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator( new DefaultItemAnimator());
        this.mAdapter = new IntentListAdapter();
        recyclerView.setAdapter(this.mAdapter);

        this.myProgressBar = findViewById(R.id.intent_list_loading);

    }

    private void setUpViewModel() {
        detailViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserDetailViewModel.class);
        bindEvents();
    }

    private void bindEvents() {
        detailViewModel.loadingIntentsState.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    myProgressBar.setVisibility(View.VISIBLE);
                } else {
                    myProgressBar.setVisibility(View.GONE);
                }

            }
        });

        detailViewModel.intentsState.observe(this, new Observer<List<IntentEntity>>() {
            @Override
            public void onChanged(@Nullable List<IntentEntity> intentEntities) {
                mAdapter.submitList(intentEntities);
            }
        });
    }

    private void inject() {
        ((ValidatorApp) this.getApplication()).component.inject(this);
    }

}
