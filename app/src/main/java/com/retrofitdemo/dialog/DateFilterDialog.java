package com.retrofitdemo.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;


import com.retrofitdemo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by CB-PHP-1 on 8/9/2016.
 */
public class DateFilterDialog extends DialogFragment {

    public interface InterfaceCommunicator {
        void filterDate(String toDate, String fromDate);
    }

    TextView todate;
    TextView fromdate;
    Button submit, cancel, clear;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    public static DateFilterDialog newInstance(Context context) {
        DateFilterDialog f = new DateFilterDialog();
        // Supply num input as an argument.
        /*Bundle args = new Bundle();
        args.putString("type",type);
        f.setArguments(args);*/
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*this.type=getArguments().getString("type");*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_filter, container, false);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        fromdate = (TextView) v.findViewById(R.id.fromdate);
        todate = (TextView) v.findViewById(R.id.todate);
        submit = (Button) v.findViewById(R.id.submit);
        cancel = (Button) v.findViewById(R.id.cancel);
        clear = (Button) v.findViewById(R.id.clear);

        todate.setText("" + dateFormatter.format(new Date()));
        String dt = todate.getText().toString();

        Calendar c = Calendar.getInstance();

        try {
            c.setTime(dateFormatter.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, -8);  // number of days to minus
        dt = dateFormatter.format(c.getTime());
        fromdate.setText(dt.toString());
        setFromDate();
        setToDate();

        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });
        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog.show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InterfaceCommunicator i = (InterfaceCommunicator) getActivity();
                i.filterDate("", "");
                dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InterfaceCommunicator i = (InterfaceCommunicator) getActivity();
                i.filterDate(todate.getText().toString(), fromdate.getText().toString());
                dismiss();
            }
        });

        return v;
    }

    private void setFromDate() {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.add(Calendar.DATE, -8);
        fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromdate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setToDate() {
        Calendar newCalendar = Calendar.getInstance();
        toDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                todate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_filter, null);

        final Dialog d = new Dialog(getActivity());
        //d.requestWindowFeature(Window.FEATURE_LEFT_ICON);

        d.setContentView(root);
        d.getWindow().setTitleColor(getResources().getColor(R.color.colorPrimary));
        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);


        return d;
    }

}
