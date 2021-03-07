package com.example.interestcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private SeekBar interestPeriod;
    private TextView yearsToGrow;
    private TextView compoundInterest;
    String selectedPeriod;
    private EditText regularPayments;
    private Button btnCalculate;
    private EditText initialInvestment;
    private EditText annualInterestRate;
    private Spinner depositFrequency;
    int period;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCalculate = findViewById(R.id.btn_calculate);

        initialInvestment = findViewById(R.id.value_initial);
        regularPayments = findViewById(R.id.value_regular);
        annualInterestRate = findViewById(R.id.value_annual_interest);

        depositFrequency = findViewById(R.id.value_frequency_of_deposit);
        compoundInterest = findViewById(R.id.txt_compound_interest);

        ArrayAdapter<CharSequence> frequencyAdapter = ArrayAdapter.createFromResource(this,R.array.frequency_of_deposit,R.layout.support_simple_spinner_dropdown_item);
        frequencyAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        depositFrequency.setAdapter(frequencyAdapter);
        initializeInterestPeriodBar();

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInputFields()){
                    calculateInterest();
                }
            }
        });
    }


    private boolean validateInputFields(){
        boolean valid = true;
        if(initialInvestment.getText().toString().trim().isEmpty()){
            valid = false;
            initialInvestment.setError( "Field is required!");
        }

        if(regularPayments.getText().toString().trim().isEmpty()){
            valid = false;
            regularPayments.setError( "Field is required!");
        }

        if(annualInterestRate.getText().toString().trim().isEmpty()){
            valid = false;
            annualInterestRate.setError( "Field is required!");
        }
        return valid;
    }

    private void initializeInterestPeriodBar(){
        interestPeriod = (SeekBar) findViewById(R.id.value_years_to_grow);
        yearsToGrow = (TextView) findViewById(R.id.txt_years_to_grow);

        interestPeriod.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        period = progress;
                        selectedPeriod = period + " yrs";
                        yearsToGrow.setText(selectedPeriod);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        yearsToGrow.setText(selectedPeriod);
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        yearsToGrow.setText(selectedPeriod);
                    }
                }
        );
    }

    private void calculateInterest(){
        compoundInterest.setText("");
        int principal, deposit, frequency_of_deposit=0, time_period;
        float interest;
        double amount;
        String frequency;
        double temp;
        String formatString;

        principal = Integer.parseInt(initialInvestment.getText().toString().trim());
        deposit = Integer.parseInt(regularPayments.getText().toString().trim());
        frequency = depositFrequency.getSelectedItem().toString();
        switch (frequency){
            case "Monthly": frequency_of_deposit = 12;
                break;
            case "Quarterly": frequency_of_deposit = 4;
                break;
            case "Semi Annually": frequency_of_deposit = 2;
                break;
            case "Annually": frequency_of_deposit = 1;
                break;
        }
        time_period = period;
        interest = Float.parseFloat(annualInterestRate.getText().toString().trim());
        interest = (interest/100);
        temp = Math.pow((1 + interest), time_period);
        amount = principal * temp + (deposit * frequency_of_deposit) * ((temp - 1)/interest);
        formatString = String.format("$%.2f", amount);
        compoundInterest.setText(formatString);
    }
}