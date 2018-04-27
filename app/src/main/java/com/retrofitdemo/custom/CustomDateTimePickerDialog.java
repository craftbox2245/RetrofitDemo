package com.cdms.craftbox.custom;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cdms.craftbox.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by CRAFT BOX on 2/27/2018.
 */

public class CustomDateTimePickerDialog {

    private SimpleDateFormat dateFormatter, dateFormatter1;
    Calendar date;
    Context context;
    String selectDate, selectTime;

    public CustomDateTimePickerDialog(Context context) {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.US);
        dateFormatter1 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        this.context = context;
    }

    public void showDateTimePicker(final TextView textView, final String hintText) {

        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        if (textView.getText().toString().equals("")) {
            //date = currentDate;
        } else {
            String[] temp = textView.getText().toString().split(" ");
            String temp_date = temp[0];
            String[] _date = temp_date.split("\\-");
            String day = _date[0];
            String month = _date[1];
            String year = _date[2];

            currentDate.set(Calendar.YEAR, Integer.parseInt(year));
            currentDate.set(Calendar.MONTH, Integer.parseInt(month));
            currentDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));

            date = Calendar.getInstance();
            date.set(Calendar.YEAR, Integer.parseInt(year));
            date.set(Calendar.MONTH, Integer.parseInt(month));
            date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        }

        /*<style name="DatePickerDialogTheme" parent="Theme.AppCompat.Light.Dialog">
        <item name="colorAccent">@color/colorPrimary</item>
        </style>*/

        DatePickerDialog d = new DatePickerDialog(context, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);

                final Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                TimePickerDialog time = new TimePickerDialog(context, R.style.DatePickerDialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        try {
                            selectDate = "" + dateFormatter1.format(date.getTime());
                            Date date1 = dateFormatter1.parse("" + dateFormatter1.format(date.getTime()));
                            Date date2 = dateFormatter1.parse("" + dateFormatter1.format(new Date()));
                            if (date1.equals(date2)) {
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                Date inTime = null;
                                selectTime = "" + hourOfDay + ":" + minute;
                                String current_time = sdf.format(currentDate.getTime());

                                inTime = sdf.parse(current_time);
                                Date outTime = sdf.parse("" + selectTime);
                                if (inTime.compareTo(outTime) < 0) {
                                    textView.setText(dateFormatter.format(date.getTime()));
                                    String[] split = dateFormatter.format(date.getTime()).split(" ");
                                    selectDate = split[0];
                                    selectTime = split[1] + " " + split[2];
                                } else {
                                    /*textView.setText(dateFormatter.format(date.getTime()));
                                    String[] split = dateFormatter.format(date.getTime()).split(" ");
                                    selectDate = split[0];
                                    selectTime = split[1] + " " + split[2];*/
                                    if(!hintText.equals(""))
                                    {
                                        textView.setHint("You Can Not Select Past Time");
                                        Toaster.show(context, "You Can Not Select Past Time", true, Toaster.DANGER);
                                    }
                                }
                            } else {
                                textView.setText(dateFormatter.format(date.getTime()));
                                String[] split = dateFormatter.format(date.getTime()).split(" ");
                                selectDate = split[0];
                                selectTime = split[1] + " " + split[2];
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false);
                time.show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH) - 1, currentDate.get(Calendar.DATE));
        d.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        d.show();
    }
}
