package com.retrofitdemo.custom;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import com.retrofitdemo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by CRAFT BOX on 3/1/2018.
 */

public class CustomDatePicker {

    private SimpleDateFormat dateFormatter;
    Context context;
    DatePickerDialog toDatePickerDialog;

    public int blank=0;
    public int birthdate=1;
    public int anniversary=2;

    public CustomDatePicker(Context context) {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        this.context = context;
    }

    public void setToDate(int type,final TextView textView, final String hintText) {

        Calendar newCalendar = Calendar.getInstance();
        if (textView.getText().toString().equals("")) {
            //date = currentDate;
        } else {
            try {
                String[] temp = textView.getText().toString().split(" ");
                String temp_date = temp[0];
                String[] _date = temp_date.split("\\-");
                String day = _date[0];
                String month = _date[1];
                String year = _date[2];

                newCalendar.set(Calendar.YEAR, Integer.parseInt(year));
                newCalendar.set(Calendar.MONTH, Integer.parseInt(month)-1);
                newCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
            } catch (Exception e) {
                e.printStackTrace();
                newCalendar = Calendar.getInstance();
            }
        }

        toDatePickerDialog = new DatePickerDialog(context, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                textView.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        if(type==0)
        {
            toDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        }
        else if(type==1)
        {
            newCalendar = Calendar.getInstance();
            newCalendar.add(Calendar.YEAR, -18);
            toDatePickerDialog.getDatePicker().setMaxDate(newCalendar.getTimeInMillis() - 1000);
        }
        else if(type==2)
        {
            toDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }

        toDatePickerDialog.show();

    }

    /*<style name="DatePickerDialogTheme" parent="Theme.AppCompat.Light.Dialog">
    <item name="colorAccent">@color/colorPrimary</item>
    </style>*/
}
