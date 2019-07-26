package co.com.geo.uservalidator.presentation.login;


import android.Manifest;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import javax.inject.Inject;

import co.com.geo.uservalidator.R;
import co.com.geo.uservalidator.util.Navigator;
import co.com.geo.uservalidator.util.RegexHelper;
import co.com.geo.uservalidator.data.model.UserEntity;
import co.com.geo.uservalidator.presentation.ValidatorApp;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    public static final int USER_DETAIL_REQUEST_CODE = 1;
    public static final int PERMISION_LOCATION_REQUEST_CODE = 1;

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ProgressBar verificationLoading = null;
    private AlertDialog waitMessageDialog = null;
    private MaterialButton verifyButton = null;
    private MaterialButton listButton = null;
    private TextInputEditText passwordEditText = null;
    private TextInputEditText usernameEditText = null;

    @Inject
    public Navigator navigator;

    private LoginViewModel loginViewModel;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    private UserEntity verifiedUserEntity = null;
    private UserEntity userToVerify = null;
    private boolean mVerifyButtonClickPerformed = false;
    private boolean mCrateUserButtonClickPerformed = false;
    private boolean mBeginIntentProcessClickPerformed = false;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        inject();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        bindEvents();
        initView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        waitMessageDialog.dismiss();
    }

    private void inject() {
        ((ValidatorApp) getActivity().getApplication()).component.inject(this);
    }

    private void init() {
        setUpViewModel();
    }

    private void initView() {
        // Set an error if the password is less than 8 characters.
        this.verifyButton = getView().findViewById(R.id.next_button);
        this.listButton = getView().findViewById(R.id.list_button);
        this.passwordEditText = getView().findViewById(R.id.password_edit_text);
        this.usernameEditText = getView().findViewById(R.id.username_text_input);

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVerifyButtonClickPerformed = true;
                boolean isPasswordValid = isPasswordValid(passwordEditText.getText());
                boolean isUsernameValid = isUsernameValid(usernameEditText.getText());

                if (isPasswordValid && isUsernameValid) {
                    if (loginViewModel != null) {
                        loginViewModel.queryUserEntity(usernameEditText.getText().toString());
                    }
                } else {
                    if (!isPasswordValid) {
                        passwordEditText.setError(getString(R.string.val_error_password));
                    }

                    if (!isUsernameValid) {
                        usernameEditText.setError(getString(R.string.val_error_username));
                    }
                }
            }
        });

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifiedUserEntity != null) {
                    navigator.openUserDetail(getActivity(), verifiedUserEntity.getUsername(), USER_DETAIL_REQUEST_CODE);
                }
            }
        });


        // Clear the error once more than 8 characters are typed.
        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (isPasswordValid(passwordEditText.getText())) {
                    passwordEditText.setError(null); //Clear the error
                }
                return false;
            }
        });
        this.verificationLoading = new ProgressBar(getContext());
        this.waitMessageDialog = new AlertDialog.Builder(getContext())
                .setView(this.verificationLoading)
                .create();

    }

    private void setUpViewModel() {
        this.loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);
    }

    private void bindEvents() {
        loginViewModel.userEntityQueryState.observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(@Nullable UserEntity userEntity) {
                if (mVerifyButtonClickPerformed) {
                    if (userEntity == null) {
                        createUserDialog();
                    } else {
                        userToVerify = userEntity;
                        verifyUser();
                    }
                    mVerifyButtonClickPerformed = false;
                }
            }
        });

        loginViewModel.loadingQueryState.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                showLoading(aBoolean);
            }
        });

        loginViewModel.userSaveState.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean saved) {
                if (mCrateUserButtonClickPerformed) {
                    if (saved) {
                        Toast.makeText(getContext(), R.string.msg_saved, Toast.LENGTH_LONG).show();
                        verifyButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                    mCrateUserButtonClickPerformed = false;
                }
            }
        });

        loginViewModel.loadingSavingState.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                showLoadingSaving(aBoolean);
            }
        });

        /*
        loginViewModel.intentSaveState.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean saved) {
                listButton.setVisibility(View.VISIBLE); //cuando se guarde el intento, habilite para ver la lista de intentos.
            }
        });
        */

        loginViewModel.loadingLocationGeoNameIntentState.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean loading) {
                showLoadingLocation(loading);
            }
        });

        loginViewModel.LocationGeoNameIntentSavedState.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isSuccess) {
                if (mBeginIntentProcessClickPerformed) {
                    listButton.setVisibility(View.VISIBLE);
                    if (isSuccess) { //could save an intent for tue userToVerify
                        verifiedUserEntity = userToVerify;
                    }
                    mBeginIntentProcessClickPerformed = false;
                }
            }
        });
    }

    //when the user doesn't exist and ask to the user if want to create it
    private void createUserDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setMessage(R.string.msg_user_no_exist)
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        mCrateUserButtonClickPerformed = true;
                        createUser();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    //when the user decides to create the user with the provided data.
    private void createUser() {
        String username = this.usernameEditText.getText().toString();
        String password = this.passwordEditText.getText().toString();
        UserEntity userEntity = new UserEntity(username, password);
        loginViewModel.insertUserEntity(userEntity);
    }

    //when the user exists and you proceed to verify its credentials and begin the intent process saving.
    @AfterPermissionGranted(PERMISION_LOCATION_REQUEST_CODE)
    private void verifyUser() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {

            final boolean verifyStatus = userToVerify.getPassword().equals(this.passwordEditText.getText().toString());

            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mBeginIntentProcessClickPerformed = true;
                            dialogInterface.dismiss();
                            loginViewModel.beginIntentProcess(verifyStatus, userToVerify); //habiendo comprobado si coinciden las contrasenas
                            //guardar el intento preguntando coordenadas y fecha.
                        }
                    })
                    .create();

            if (verifyStatus) {
                alertDialog.setMessage(getString(R.string.msg_success));
            } else {
                alertDialog.setMessage(getString(R.string.msg_failed));
            }

            alertDialog.show();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale),
                    PERMISION_LOCATION_REQUEST_CODE, perms);
        }
    }



    //DIALOG methods
    //TODO create generic loading method
    private void showLoading(boolean isLoading) {
        this.waitMessageDialog.setMessage(getString(R.string.msg_verifying_user));
        if (this.waitMessageDialog != null) {
            if (isLoading) {
                waitMessageDialog.show();
            } else {
                waitMessageDialog.dismiss();
            }
        }
    }

    private void showLoadingSaving(boolean isLoading) {
        this.waitMessageDialog.setMessage(getString(R.string.msg_saving_user));
        if (this.waitMessageDialog != null) {
            if (isLoading) {
                waitMessageDialog.show();
            } else {
                waitMessageDialog.dismiss();
            }
        }
    }

    private void showLoadingLocation(boolean isLoading) {
        this.waitMessageDialog.setMessage(getString(R.string.msg_getting_location));
        if (this.waitMessageDialog != null) {
            if (isLoading) {
                waitMessageDialog.show();
            } else {
                waitMessageDialog.dismiss();
            }
        }
    }


    private Boolean isPasswordValid(Editable text) {
        return text != null && text.length() >= 8;
    }

    private Boolean isUsernameValid(Editable text) {
        return RegexHelper.isValidEmail(text.toString());
    }

}
