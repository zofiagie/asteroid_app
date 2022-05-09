package com.example.asteroida;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;


public class MenuActivity extends AppCompatActivity {

    private final int MIN_DIAMETER = 20;
    private final int MAX_DIAMETER = 20000;

    private SeekBar diameterSeekBar;
    private EditText diameterTextView;
    private SeekBar velocitySeekBar;
    private EditText velocityTextView;
    private SeekBar angleSeekBar;
    private EditText angleTextView;

    String[] densitySpinner1Choices = {"Ice (1000 kg/m^3)", "Porous rock (1500 kg/m^3)", "Dense rock (3000 kg/m^3)", "Iron (8000 kg/m^3)"};
    int selectedDensitySpinner1Index = 0;

    String[] densitySpinner2Choices = {"Sedimentary rock (2500 kg/m^3)", "Igneous rocks (2750 kg/m^3)"};
    int selectedDensitySpinner2Index = 0;

    String[] directionSpinnerChoices = {"east", "west", "north", "south", "north-east", "north-west", "south-east", "south-west"};
    int selectedDirectionSpinnerIndex = 0;

    String[] entryLongitudeSpinnerChoices = {"W", "E"};
    int selectedEntryLongitudeSpinnerIndex = 0;
    private EditText longitudeDegreeTextView;
    private EditText longitudeMinuteTextView;
    private EditText longitudeSecondsTextView;

    String[] entryLatitudeSpinnerChoices = {"N", "S"};
    int selectedEntryLatitudeSpinnerIndex = 0;
    private EditText latitudeDegreeTextView;
    private EditText latitudeMinuteTextView;
    private EditText latitudeSecondsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_menu);


        //   ############################## DIAMETER #################################
        diameterSeekBar = (SeekBar) findViewById(R.id.diameterSeekBar);
        diameterTextView = (EditText) findViewById(R.id.diameter_text);
        diameterTextView.setText(String.valueOf(MIN_DIAMETER));

        // perform seek bar change listener event used for getting the progress value
        scaleStoneDiameterImage(MIN_DIAMETER);
        diameterSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = MIN_DIAMETER;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                diameterTextView.setText(String.valueOf(progressChangedValue));
                diameterTextView.setSelection(diameterTextView.getText().length());
                scaleStoneDiameterImage(progressChangedValue);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        diameterTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int value = Integer.parseInt(diameterTextView.getText().toString());

                if (value < MIN_DIAMETER) {
                    value = MIN_DIAMETER;
                }
                if (value > MAX_DIAMETER) {
                    value = MAX_DIAMETER;
                }

                diameterTextView.removeTextChangedListener(this);
                diameterTextView.setText(String.valueOf(value));
                diameterTextView.addTextChangedListener(this);
                diameterTextView.setSelection(diameterTextView.getText().length());

                diameterSeekBar.setProgress(value);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        //   ################################# VELOCITY #################################
        velocitySeekBar = (SeekBar) findViewById(R.id.velocitySeekBar);
        velocityTextView = (EditText) findViewById(R.id.velocity_text);
        velocityTextView.setText("5");

        // perform seek bar change listener event used for getting the progress value
        velocitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progressChangedValue = 5;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;

                velocityTextView.setText(String.valueOf(progressChangedValue));
                velocityTextView.setSelection(velocityTextView.getText().length());
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        velocityTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int value = Integer.parseInt(velocityTextView.getText().toString());

                if (value < 5) {
                    value = 5;
                }
                if (value > 25) {
                    value = 25;
                }

                velocityTextView.removeTextChangedListener(this);
                velocityTextView.setText(String.valueOf(value));
                velocityTextView.addTextChangedListener(this);
                velocityTextView.setSelection(velocityTextView.getText().length());

                velocitySeekBar.setProgress(value);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // ################################# ANGLE #################################
        angleSeekBar = (SeekBar) findViewById(R.id.angleSeekBar);
        angleTextView = (EditText) findViewById(R.id.angle_text);
        angleTextView.setText("5");

        ((TextView) findViewById(R.id.angle_supscript)).setText(Html.fromHtml(" <sup><small>o</small></sup>"));
        ((TextView) findViewById(R.id.angle_min_range_text)).setText(Html.fromHtml("5 <sup><small>o</small></sup>"));
        ((TextView) findViewById(R.id.angle_max_range_text)).setText(Html.fromHtml("90 <sup><small>o</small></sup>"));

        // perform seek bar change listener event used for getting the progress value
        angleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progressChangedValue = 5;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;

                angleTextView.setText(String.valueOf(progressChangedValue));
                angleTextView.setSelection(angleTextView.getText().length());
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        angleTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int value = Integer.parseInt(angleTextView.getText().toString());

                if (value < 5) {
                    value = 5;
                }
                if (value > 90) {
                    value = 90;
                }

                angleTextView.removeTextChangedListener(this);
                angleTextView.setText(String.valueOf(value));
                angleTextView.addTextChangedListener(this);
                angleTextView.setSelection(angleTextView.getText().length());

                angleSeekBar.setProgress(value);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // ################################# DENSITIES #################################
        Spinner options1 = (Spinner) findViewById(R.id.density_spinner1);
        if (options1 != null) {
            options1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedDensitySpinner1Index = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, densitySpinner1Choices);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            options1.setAdapter(adapter);
        }

        Spinner options2 = (Spinner) findViewById(R.id.density_spinner2);
        if (options2 != null) {
            options2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedDensitySpinner2Index = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, densitySpinner2Choices);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            options2.setAdapter(adapter);
        }


        // ################################# DIRECTION #################################
        Spinner options3 = (Spinner) findViewById(R.id.direction_spinner1);
        if (options3 != null) {
            options3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedDirectionSpinnerIndex = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, directionSpinnerChoices);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            options3.setAdapter(adapter);
        }


        // ################################# ENTRY THE ATMOSPHERE #################################
        ((TextView) findViewById(R.id.longitude_degree_text_view)).setText(Html.fromHtml(" <sup><small>o</small></sup>"));
        ((TextView) findViewById(R.id.latitude_degree_text_view)).setText(Html.fromHtml(" <sup><small>o</small></sup>"));

        longitudeDegreeTextView = (EditText) findViewById(R.id.longitude_degree_edit_text);
        longitudeMinuteTextView = (EditText) findViewById(R.id.longitude_minute_edit_text);
        longitudeSecondsTextView = (EditText) findViewById(R.id.longitude_second_edit_text);

        Spinner options4 = (Spinner) findViewById(R.id.longitude_spinner);
        if (options4 != null) {
            options4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedEntryLongitudeSpinnerIndex = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, entryLongitudeSpinnerChoices);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            options4.setAdapter(adapter);
            options4.setSelection(1); //Set E default
        }

        latitudeDegreeTextView = (EditText) findViewById(R.id.latitude_degree_edit_text);
        latitudeMinuteTextView = (EditText) findViewById(R.id.latitude_minute_edit_text);
        latitudeSecondsTextView = (EditText) findViewById(R.id.latitude_second_edit_text);

        Spinner options5 = (Spinner) findViewById(R.id.latitude_spinner);

        if (options5 != null) {
            options5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedEntryLatitudeSpinnerIndex = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, entryLatitudeSpinnerChoices);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            options5.setAdapter(adapter);
        }
    }



    public void scaleStoneDiameterImage(double progressChangedValue){
        ImageView imageView = (ImageView) findViewById(R.id.stone_image);
        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
        imageView.getLayoutParams().height = (int)(dimensionInDp * ((1.0/2.0) + ((double)progressChangedValue / (MAX_DIAMETER * 2.0)) ));
        imageView.getLayoutParams().width = (int)(dimensionInDp *  ((1.0/2.0) + ((double)progressChangedValue / (MAX_DIAMETER *2.0)) ));
    }



    public void openResultActivity(View v){
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("diameter", diameterTextView.getText().toString());
        intent.putExtra("velocity", velocityTextView.getText().toString());
        intent.putExtra("angle", angleTextView.getText().toString());
        intent.putExtra("projectileDensityListId", selectedDensitySpinner1Index);
        intent.putExtra("targetDensityListId", selectedDensitySpinner2Index);
        intent.putExtra("longitudeDegree", longitudeDegreeTextView.getText().toString());
        intent.putExtra("longitudeMinute", longitudeMinuteTextView.getText().toString());
        intent.putExtra("longitudeSeconds", longitudeSecondsTextView.getText().toString());
        intent.putExtra("longitudeLocationListId", selectedEntryLongitudeSpinnerIndex);
        intent.putExtra("latitudeDegree", latitudeDegreeTextView.getText().toString());
        intent.putExtra("latitudeMinute", latitudeMinuteTextView.getText().toString());
        intent.putExtra("latitudeSeconds", latitudeSecondsTextView.getText().toString());
        intent.putExtra("latitudeLocationListId", selectedEntryLatitudeSpinnerIndex);
        intent.putExtra("flightDirectionListId", selectedDirectionSpinnerIndex);
        startActivity(intent);
    }
}