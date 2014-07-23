package in.championswimmer.resizehandler;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omerjerk on 22/7/14.
 */
public class LeftPane extends RelativeLayout {

    private static final String TAG = "omerjerk";

    private Context context;

    List<ResolveInfo> resolveInfos;

    private RecentListAdapter mAdapter;
    private ListView recentListView;
    ActivityManager am;

    public LeftPane(Context context) {
        super(context);
        this.context = context;
        Log.d(TAG, "First constructor called");
    }

    public LeftPane(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Log.d(TAG, "Second constructor called");
    }

    public LeftPane(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        Log.d(TAG, "Third constructor called");
    }

    public void generateRecentList() {
        if (recentListView == null) {
            recentListView = (ListView) findViewById(R.id.recent_list_view);
            resolveInfos = new ArrayList<ResolveInfo>();
            mAdapter = new RecentListAdapter();
            recentListView.setAdapter(mAdapter);
        }
        if (am == null) {
            am = (ActivityManager) context
                    .getSystemService(Activity.ACTIVITY_SERVICE);
        }

        List<ActivityManager.RecentTaskInfo> recentTasks = am.getRecentTasks(Integer.MAX_VALUE,
                ActivityManager.RECENT_IGNORE_UNAVAILABLE);

        PackageManager pm = context.getPackageManager();
        for (ActivityManager.RecentTaskInfo info : recentTasks) {
            resolveInfos.add(pm.resolveActivity(info.baseIntent, 0));
        }
        mAdapter.notifyDataSetChanged();
    }

    private class RecentListAdapter extends BaseAdapter {

        LayoutInflater mInflater;
        PackageManager packageManager;

        public RecentListAdapter() {
            mInflater = LayoutInflater.from(context);
            packageManager = context.getPackageManager();
        }

        @Override
        public int getCount() {
            return resolveInfos.size();
        }

        @Override
        public Object getItem(int i) {
            return resolveInfos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder mHolder;
            if (view == null) {
                mHolder = new ViewHolder();
                view = mInflater.inflate(R.layout.row_left_pane, null);
                mHolder.iconImageView = (ImageView) view.findViewById(R.id.recent_image_view);
                mHolder.mTextView = (TextView) view.findViewById(R.id.package_name_text_view);
                view.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) view.getTag();
            }
            ResolveInfo info = resolveInfos.get(i);

            mHolder.mTextView.setText(info.loadLabel(packageManager));
            mHolder.iconImageView.setImageDrawable(info.loadIcon(packageManager));
            return view;
        }
    }

    private static class ViewHolder {
        //TODO: rename the following variable
        TextView mTextView;
        ImageView iconImageView;
    }
}
