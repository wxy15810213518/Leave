package com.xhly.leave.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通用的ViewHolder类
 * 
 * @author shkstart
 * 
 */
public class ViewHolder extends RecyclerView.ViewHolder{

	//代表当前行的view对象
	private View convertView;
	//用来替代Map<Integer,Object>的容器, 效率比map高
	private SparseArray<View> views;

	private ViewHolder(Context context, View itemView, ViewGroup parent) {
		super(itemView);
		this.convertView = itemView;
		this.convertView.setTag(this);//保存当前viewholder到convertView中
		views = new SparseArray<View>();
	}


	public static ViewHolder getHolder(Context context, ViewGroup parent,
			int layoutId) {
			View  itemView = View.inflate(context,layoutId,null);

			ViewHolder holder = new ViewHolder(context,itemView,parent);
			return holder;
	}

	/**
	 * 根据视图id得到对应的视图对象
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {
		View view = views.get(viewId);
		if (view == null) {
			view = convertView.findViewById(viewId);
			views.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 得到当前对应的convertView
	 * @return
	 */
	public View getConvertView() {
		return convertView;
	}

	public ViewHolder setText(int viewId, String text) {
		TextView textView = getView(viewId);
		textView.setText(text);
		return this;
	}

	/**
	 * 设置drawable图片
	 * @param viewId
	 * @param drawable
	 */
	public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
		ImageView imageView = getView(viewId);
		imageView.setImageDrawable(drawable);
		return this;
	}

	/**
	 * 设置资源图片
	 * @param viewId
	 * @param resourceId
	 */
	public ViewHolder setImageResource(int viewId, int resourceId) {
		ImageView imageView = getView(viewId);
		imageView.setImageResource(resourceId);
		return this;
	}

	/**
	 * 设置点击监听
	 * @param viewId
	 * @param listener
	 * @return
	 */
	public ViewHolder setOnclickListener(int viewId, View.OnClickListener listener) {
		getView(viewId).setOnClickListener(listener);
		return this;
	}

	public ViewHolder setVisibility(int viewId, int visibility) {
		getView(viewId).setVisibility(visibility);
		return this;
	}

}
