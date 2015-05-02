package com.graduate.infocollect.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.graduate.cancerinfocollect.R;
import com.graduate.infocollect.db.DBHelper;
import com.graduate.infocollect.entity.Contact;
import com.graduate.infocollect.entity.MedicalData;

public class ItemListActivity extends BaseActivity {
	private ListView mListview;
	private List<MedicalData> mList = new ArrayList<MedicalData>();
	private TextView tv_name;
	private ItemsAdapter adapter;
	private Contact contact;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itemlist);
		initView();
		initData();
	}
	
	private void initView() {
		tv_name = (TextView)findViewById(R.id.title_name);
		mListview = (ListView)findViewById(R.id.listview);
	}
	
	private void initData() {
		contact = (Contact)getIntent().getExtras().get(Contact.CONTACT);
		tv_name.setText(contact.getName());
		
	}
	
	public void onShowProfile(View v) {
		Intent intent = new Intent(ItemListActivity.this, ProfileActivity.class);
		startActivity(intent);
	}
	
	public void onShowChart(View v) {
		if(mList.size() <= 1) {
			return;
		}
		Intent intent = new Intent(ItemListActivity.this, ChartActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initAdapter();
		adapter.notifyDataSetChanged();
		
	}
	
	private void initAdapter() {
		mList.clear();
		mList.add(new MedicalData("-1"));
		mList.addAll(DBHelper.getInstance().getMedicalList(contact.getId()));
		adapter = new ItemsAdapter(ItemListActivity.this, mList);
		mListview.setAdapter(adapter);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	public class ItemsAdapter extends BaseAdapter {
		private List<MedicalData> mList;
		private LayoutInflater mInflater;
		
		public ItemsAdapter(Context context, List<MedicalData> mList) {
			super();
			this.mInflater = LayoutInflater.from(context);
			this.mList = mList;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = new ViewHolder();
			if(convertView == null) {
				convertView = mInflater.inflate(R.layout.item_medical, null);
				viewHolder.no = ((TextView)convertView.findViewById(R.id.no));
				viewHolder.psa = ((TextView)convertView.findViewById(R.id.psa));
				viewHolder.ca = ((TextView)convertView.findViewById(R.id.ca));
				viewHolder.afp = ((TextView)convertView.findViewById(R.id.afp));
				convertView.setTag(viewHolder);
			}
			else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			MedicalData entity = mList.get(position);
			if(!entity.getContactId().equals("-1")) {
				viewHolder.no.setText(position + "");
				// System.out.println(entity.getPSA()+"");
				viewHolder.psa.setText(entity.getPSA());
				viewHolder.ca.setText(entity.getCA());
				viewHolder.afp.setText(entity.getAFP());
			}
			return convertView;
		}
		
	}
	
	private static class ViewHolder {
		private TextView no, psa, ca, afp;
	}
}