package com.simplexx.wnp.util.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simplexx.wnp.baselib.basemvp.BasePresenter;
import com.simplexx.wnp.baselib.basemvp.IView;
import com.simplexx.wnp.baselib.exception.NetWorkException;
import com.simplexx.wnp.baselib.executor.ActionRequest;
import com.simplexx.wnp.util.PresenterUtil;
import com.simplexx.wnp.util.executor.ThreadExecutor;
import com.simplexx.wnp.util.ui.dialog.ActionLoadingDialogFragment;

/**
 * Created by wnp on 2018/7/3.
 */

public class PresenterDialogFragment<T extends BasePresenter<E>, E extends IView> extends BaseDialogFragment implements IView {
    T presenter;

    protected T createPresenter() {
        presenter = PresenterUtil.createPresenter(this);
        return presenter;
    }

    public T getPresenter() {
        return presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        presenter.onViewCreate();
        return view;
    }

    @Override
    public void onNeedLogin(boolean otherDevice) {
        getBaseActivity().onNeedLogin(otherDevice);
    }

    @Override
    public void onException(Exception e) {
        getBaseActivity().onException(e);
    }

    @Override
    public void onException(Exception e, boolean finish) {
        getBaseActivity().onException(e, finish);
    }

    @Override
    public void onException(ActionRequest request, NetWorkException e) {
        if (this.isAdded()) {
            //todo Show ActionReloadDialog
        }
    }

    @Override
    public void onWarn(String message) {
        getBaseActivity().onWarn(message);
    }

    @Override
    public void hideKeyBoard() {
        getBaseActivity().hideKeyBoard();
    }

    @Override
    public void runAction(ActionRequest request) {
        getBaseActivity().runAction(request);
    }

    @Override
    public boolean viewDestroyed() {
        return this.isDetached();
    }

    @Override
    public void showLoadingView(final ActionRequest request) {
        ThreadExecutor.runInMain(new Runnable() {
            @Override
            public void run() {
                if (PresenterDialogFragment.this.isAdded()) {
                    ActionLoadingDialogFragment.singleShow(getBaseActivity(), request);
                }
            }
        });
    }

    @Override
    public void dismissLoadingView() {
        ThreadExecutor.runInMain(new Runnable() {
            @Override
            public void run() {
                if (PresenterDialogFragment.this.isAdded()) {
                    ActionLoadingDialogFragment.dismiss(getBaseActivity());
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onViewStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onViewResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onViewPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onViewStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onViewDestroy();
        hideKeyBoard();
    }
}
