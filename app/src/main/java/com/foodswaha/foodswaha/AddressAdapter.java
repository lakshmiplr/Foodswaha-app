package com.foodswaha.foodswaha;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pharshar on 11/17/2015.
 */
public class AddressAdapter extends ArrayAdapter<Address> {
    private static final String TAG = "AddressAdapter";
    private static RadioButton selectedRadioButton;
    private static ImageButton selectedEditAddress;
    private static ImageButton selectedDeleteAddress;

    Context context;
    int layoutResourceId;
    List data = null;

    public AddressAdapter(Context context, int resource, List<Address> objects) {
        super(context, resource, objects);
        Log.e(TAG, " HotelMenuItemSubAdapter constructor  started.");
        this.context = context;
        this.layoutResourceId = resource;
        this.data = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        Holder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new Holder();
            holder.address = (TextView) row.findViewById(R.id.address);
            holder.radioButton = (RadioButton) row.findViewById(R.id.radioButton);
            holder.editAddress = (ImageButton) row.findViewById(R.id.editAddress);
            holder.deleteAddress = (ImageButton) row.findViewById(R.id.deleteAddress);
            final ImageButton finalEditAddress = holder.editAddress;
            final ImageButton finalDeleteAddress = holder.deleteAddress;
            final RadioButton finalRadioButton = holder.radioButton;
            holder.radioButton.setOnClickListener(new View.OnClickListener() {
                boolean checked = true;
                @Override
                public void onClick(View v) {
                    if(checked&&selectedRadioButton!=finalRadioButton){
                        if(selectedRadioButton!=null){
                            selectedRadioButton.setChecked(false);
                            selectedEditAddress.setVisibility(View.GONE);
                            selectedDeleteAddress.setVisibility(View.GONE);
                        }
                        selectedRadioButton = finalRadioButton;
                        selectedEditAddress = finalEditAddress;
                        selectedDeleteAddress = finalDeleteAddress;
                        finalRadioButton.setChecked(true);
                        selectedEditAddress.setVisibility(View.VISIBLE);
                        selectedDeleteAddress.setVisibility(View.VISIBLE);
                    }

                }
            });
            row.setTag(holder);
        }
        else{
            holder = (Holder)row.getTag();
        }

        holder.position.setText(String.valueOf(position));
        Address address = (Address)data.get(position);
        holder.address.setText(address.getFlatNumber()+","+address.getStreetName()+","+address.getArea()
        +","+address.getCity()+","+address.getLandmark());

        return row;
    }

    class Holder{
        TextView address;
        RadioButton radioButton;
        ImageButton editAddress;
        ImageButton deleteAddress;
        TextView position = new TextView(getContext());
    }
}
