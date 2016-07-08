/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xhly.leave.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xhly.leave.base.CommonBaseAdapter;
import com.xhly.leave.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonHeaderRecyclerAdapter<T> extends CommonBaseAdapter {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private LayoutInflater mInflater;
    private ArrayList<T> mItems;
    private View mHeaderView;

    private Context context;
    private List items;
    private int layoutId;


    public CommonHeaderRecyclerAdapter(Context context, ArrayList<T> items, int layoutId, View headerView) {
       super(context,items,layoutId);
        this.context = context;
        this.layoutId = layoutId;
        mInflater = LayoutInflater.from(context);
        mItems = items;
        mHeaderView = headerView;
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null) {
            return mItems.size();
        } else {
            return mItems.size()+1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            return new HeaderViewHolder(mHeaderView);
        } else {
            return ViewHolder.getHolder(context,parent,layoutId);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            convert((ViewHolder) viewHolder, position);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

}
