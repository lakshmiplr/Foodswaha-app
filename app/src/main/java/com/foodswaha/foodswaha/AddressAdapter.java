package com.foodswaha.foodswaha;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by pharshar on 11/17/2015.
 */
public class AddressAdapter extends ArrayAdapter<Address> {
    private static final String TAG = "AddressAdapter";
    private static final String POST_ADDRESS_DETAILS_UPDATE_URL = "http://104.199.135.27:8080/address/update";
    private static RadioButton selectedRadioButton;
    private static ImageButton selectedEditAddress;
    private static ImageButton selectedDeleteAddress;

    Context context;
    int layoutResourceId;
    List data = null;

    public AddressAdapter(Context context, int resource, List<Address> objects) {
        super(context, resource, objects);
        Log.e(TAG, " AddressAdapter constructor  started.");
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
            final TextView finalFlatNumber = holder.flatNumber;
            final TextView finalStreetName = holder.streetName;
            final TextView finalArea = holder.area;
            final TextView finalCity = holder.city;
            final TextView finalLandMark = holder.landMark;
            final TextView finalId = holder.id;
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
            holder.editAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editAddressIntent = new Intent(getContext(), AddAddressActivity.class);
                    editAddressIntent.putExtra("edit","true");
                    editAddressIntent.putExtra("flatNumber", finalFlatNumber.getText());
                    editAddressIntent.putExtra("streetDetails",finalStreetName.getText());
                    editAddressIntent.putExtra("area",finalArea.getText());
                    editAddressIntent.putExtra("city",finalCity.getText());
                    editAddressIntent.putExtra("landMark",finalLandMark.getText());
                    editAddressIntent.putExtra("id",finalId.getText());
                    getContext().startActivity(editAddressIntent);
                }
            });
            holder.deleteAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getContext());
                    alertDialogBuilder
                            .setMessage("Delete address?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    JSONObject addressObject = LoginActivity.getAddressJSONObject();
                                    JSONArray addressesJSONArray = addressObject.optJSONArray("addresses");
                                    JSONArray newAddressJSONArray = removeAddress(addressesJSONArray, Integer.parseInt(finalId.getText().toString()));
                                    try {
                                        addressObject.put("addresses", newAddressJSONArray);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    LoginActivity.setAddressJSONObject(addressObject);
                                    sendUpdateAddressRequestToServer(addressObject);
                                    DisplayAddressActivity.getAddressList().remove(Integer.parseInt(finalId.getText().toString()));
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });
            row.setTag(holder);
        }
        else{
            holder = (Holder)row.getTag();
        }

        Address address = (Address)data.get(position);
        holder.flatNumber.setText(address.getFlatNumber());
        holder.streetName.setText(address.getStreetName());
        holder.area.setText(address.getArea());
        holder.city.setText(address.getCity());
        holder.landMark.setText(address.getLandmark());
        holder.id.setText(Integer.toString(position));
        if(position==0){
            holder.radioButton.setChecked(true);
            holder.editAddress.setVisibility(View.VISIBLE);
            holder.deleteAddress.setVisibility(View.VISIBLE);
            selectedRadioButton = holder.radioButton;
            selectedEditAddress = holder.editAddress;
            selectedDeleteAddress = holder.deleteAddress;
        }

        holder.address.setText(address.getFlatNumber()+","+address.getStreetName()+","+address.getArea()
        +","+address.getCity()+","+address.getLandmark());

        return row;
    }
    public static JSONArray removeAddress( JSONArray addressArray,int id) {

        JSONArray tempJSOnArray=new JSONArray();
        try{
            for(int i=0;i<addressArray.length();i++){
                addressArray.get(i);
                if(i!=id)

                    tempJSOnArray.put(addressArray.get(i));
            }
        }catch (Exception e){e.printStackTrace();}
        return tempJSOnArray;
    }

    class Holder{
        TextView id = new TextView(getContext());
        TextView address;
        TextView flatNumber = new TextView(getContext());
        TextView streetName = new TextView(getContext());
        TextView area =new TextView(getContext());
        TextView city = new TextView(getContext());
        TextView landMark= new TextView(getContext());
        RadioButton radioButton;
        ImageButton editAddress;
        ImageButton deleteAddress;
    }
    private void sendUpdateAddressRequestToServer(JSONObject addressObject){
        JsonObjectRequest jsonObjectPost = new JsonObjectRequest(Request.Method.POST, POST_ADDRESS_DETAILS_UPDATE_URL,addressObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                        }
                        catch (Exception e) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleyRequestQueueFactory.getInstance().getRequestQueue().add(jsonObjectPost);
    }
}
