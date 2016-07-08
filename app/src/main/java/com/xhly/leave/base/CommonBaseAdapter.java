package com.xhly.leave.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * 通用的baseAdapter
 * @author shkstart
 *
 * @param <T>
 */
public abstract class CommonBaseAdapter<T> extends RecyclerView.Adapter {

	private Context context;
	private List<T> data;
	private int layoutId;

	public CommonBaseAdapter(Context context, List<T> data, int layoutId) {
		this.context = context;
		this.data = data;
		this.layoutId = layoutId;
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = ViewHolder.getHolder(context,parent,layoutId);
		return holder;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //调用未实现的抽象方法设置数据
        convert((ViewHolder)holder, position);
	}

	public abstract void convert(ViewHolder holder, int position);
}
