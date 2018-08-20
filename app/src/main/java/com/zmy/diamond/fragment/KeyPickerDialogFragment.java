package com.zmy.diamond.fragment;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zmy.diamond.R;
import com.zmy.diamond.adapter.KeyListAdapter;
import com.zmy.diamond.utli.MyUtlis;

/**
 * 关键词选择 fragment 选择页面
 */
public class KeyPickerDialogFragment extends AppCompatDialogFragment implements
        View.OnClickListener, TextWatcher, Dialog.OnDismissListener {
    private View mContentView;
    private RecyclerView mRecyclerView;
    private TextView mOverlayTextView;

    private TextView mCancelBtn;
//    private ImageView mClearAllBtn;


    private EditText mSearchBox;


    private LinearLayoutManager mLayoutManager;
    private KeyListAdapter mAdapter;
    private boolean enableAnim = false;
    private int mAnimStyle = com.zaaach.citypicker.R.style.DefaultCityPickerAnimation;


    /**
     * 获取实例
     *
     * @param enable 是否启用动画效果
     * @return
     */
    public static KeyPickerDialogFragment newInstance(boolean enable) {
        final KeyPickerDialogFragment fragment = new KeyPickerDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean("cp_enable_anim", enable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, com.zaaach.citypicker.R.style.CityPickerStyle);

        Bundle args = getArguments();
        if (args != null) {
            enableAnim = args.getBoolean("cp_enable_anim");
        }


    }


    public void setAnimationStyle(@StyleRes int style) {
        this.mAnimStyle = style <= 0 ? com.zaaach.citypicker.R.style.DefaultCityPickerAnimation : style;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.dialog_key_picker, container, false);

        mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new KeyListAdapter(this, MyUtlis.getKeyItemData());
        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                //确保定位城市能正常刷新
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    mAdapter.refreshLocationItem();
//                }
//            }
//        });


        mSearchBox =   mContentView.findViewById(R.id.cp_search_box);
        mSearchBox.addTextChangedListener(this);
        mSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String key = mSearchBox.getText().toString().trim();
                    if (!TextUtils.isEmpty(key)) {
                        KeyboardUtils.hideSoftInput(mSearchBox);
                        dismiss(0, new City(key, "", "", ""));
                    }
                }
                return false;
            }
        });
        mCancelBtn = (TextView) mContentView.findViewById(R.id.cp_cancel);
//        mClearAllBtn = (ImageView) mContentView.findViewById(com.zaaach.citypicker.R.id.cp_clear_all);
        mCancelBtn.setOnClickListener(this);
//        mClearAllBtn.setOnClickListener(this);
        return mContentView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        if (window != null) {
            window.getDecorView().setPadding(0, 0, 0, 0);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            if (enableAnim) {
                window.setWindowAnimations(mAnimStyle);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

//            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }
        return dialog;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cp_cancel) {
            dismiss(-1, null);
        } else if (id == R.id.cp_clear_all) {
            mSearchBox.setText("");
        }
    }


    public void dismiss(int position, City data) {
        dismiss();
        if (mOnPickListener != null) {
            mOnPickListener.onPick(position, data);
        }
    }

    public OnPickListener mOnPickListener;

//    @Override
//    public void locate() {
//        if (mOnPickListener != null) {
//            mOnPickListener.onLocate();
//        }
//    }

    public void setOnPickListener(OnPickListener listener) {
        this.mOnPickListener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
