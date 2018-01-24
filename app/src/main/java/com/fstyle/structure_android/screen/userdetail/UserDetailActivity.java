package com.fstyle.structure_android.screen.userdetail;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.fstyle.structure_android.R;
import com.fstyle.structure_android.data.model.User;
import com.fstyle.structure_android.data.repository.UserRepository;
import com.fstyle.structure_android.data.source.local.UserLocalDataSource;
import com.fstyle.structure_android.data.source.remote.UserRemoteDataSource;
import com.fstyle.structure_android.screen.BaseActivity;
import com.fstyle.structure_android.screen.edituser.EditUserActivity;
import com.fstyle.structure_android.utils.Constant;
import com.fstyle.structure_android.utils.navigator.Navigator;
import com.fstyle.structure_android.utils.rx.BaseSchedulerProvider;
import com.fstyle.structure_android.utils.rx.SchedulerProvider;

/**
 * UserDetail Screen.
 */
public class UserDetailActivity extends BaseActivity implements UserDetailContract.View {

    private UserDetailContract.Presenter mPresenter;

    private TextView mTextViewUserLogin;
    private ImageView mImageViewAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        mTextViewUserLogin = findViewById(R.id.tvUserLogin);
        mImageViewAvatar = findViewById(R.id.imgAvatar);

        UserRepository userRepository =
                UserRepository.getInstance(UserLocalDataSource.getInstance(this),
                        UserRemoteDataSource.getInstance(getApplicationContext()));
        BaseSchedulerProvider schedulerProvider = SchedulerProvider.getInstance();

        mPresenter = new UserDetailPresenter(userRepository, schedulerProvider);
        mPresenter.setView(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    protected void onStop() {
        mPresenter.onStop();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Navigator navigator = new Navigator(this);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.ARGS_AVATAR_URL, getAvatarUrl());
                navigator.startActivity(EditUserActivity.class, bundle);
                break;
            case R.id.action_delete:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getAvatarUrl() {
        return getIntent().getStringExtra(Constant.ARGS_AVATAR_URL);
    }

    @Override
    public void showUser(User user) {
        mTextViewUserLogin.setText(user.getLogin());
        Glide.with(this)
                .load(user.getAvatarUrl())
                .placeholder(R.mipmap.ic_launcher_round)
                .into(mImageViewAvatar);
    }

    @Override
    public void showGetUserError(Throwable throwable) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
