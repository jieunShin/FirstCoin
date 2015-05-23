package kookmin.cs.firstcoin.BP_order;


import java.util.ArrayList;
import java.util.HashMap;

import kookmin.cs.firstcoin.order.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class AdptMain extends BaseExpandableListAdapter {
   private Context context;
   private ArrayList<String> arrayGroup;
   private HashMap<String, ArrayList<String>> arrayChild;
   
   public AdptMain(Context context, ArrayList<String> arrayGroup, HashMap<String,ArrayList<String>> arrayChild){
      super();
      this.context = context;
      this.arrayGroup =arrayGroup;
      this.arrayChild = arrayChild;
   }

   @Override
   public int getGroupCount() {
      
      return arrayGroup.size();
   }

   @Override
   public int getChildrenCount(int groupPosition) {
      return arrayChild.get(arrayGroup.get(groupPosition)).size();
   }

   @Override
   public Object getGroup(int groupPosition) {
      return arrayGroup.get(groupPosition);
   }

   @Override
   public Object getChild(int groupPosition, int childPosition) {
      return arrayChild.get(arrayGroup.get(groupPosition)).get(childPosition);
   }

   @Override
   public long getGroupId(int groupPosition) {
      return groupPosition;
   }

   @Override
   public long getChildId(int groupPosition, int childPosition) {
      return childPosition;
   }

   @Override
   public boolean hasStableIds() {
      return false;
   }

   @Override
   public View getGroupView(int groupPosition, boolean isExpanded,
         View convertView, ViewGroup parent) {
      String groupName = arrayGroup.get(groupPosition);
      View v = convertView;
      
      if(v == null){
         LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         v= (RelativeLayout)inflater.inflate(R.layout.lvi_group,null);
      }
      TextView textGroup = (TextView)v.findViewById(R.id.text_group);
      textGroup.setText(groupName);
      return v;
   }

   @Override
   public View getChildView(int groupPosition, int childPosition,
         boolean isLastChild, View convertView, ViewGroup parent) {
      String childName = arrayChild.get(arrayGroup.get(groupPosition)).get(childPosition);
      View v = convertView;
      
      if(v == null){
         LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         v= (RelativeLayout)inflater.inflate(R.layout.lvi_child,null);
      }
      TextView textGroup = (TextView)v.findViewById(R.id.text_child);
      textGroup.setText(childName);
      return v;
   }

   @Override
   public boolean isChildSelectable(int groupPosition, int childPosition) {
      return false;
   }

}