package com.example.asteroida;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioRecord;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;

public class ResultActivity extends AppCompatActivity implements OnMapReadyCallback {

    public final double CONST_H = 0.01;
    public final double CONST_ATMOSPHERE_HEIGHT = 100000;
    public final double CONST_MAX_TIME = 1000;

    public final double rho_a = 1.2; //kg/m^3
    public final double g_0 = 9.81; //m/s^2
    public final double C_d = 1;
    public final double sigma_ab = Math.pow(10, -8); //s^2/m^2
    public final double R_e = 6.371 * Math.pow(10, 6); //m
    public final double E = 4.184 * Math.pow(10, 6);
    public final double EARTH_G = 9.81;

    public final int MAP_LAYOUT_ID = 124324523; // random value
    public final int DATA_LAYOUT_ID = 745363645; // random value

    public double[] delta_p = {6900, 22000, 108219, 170272, 201298};
    public int[] effects_of_shock_wave_colors = {Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED};
    public String[] legend_impact_effects = {"99% mortality", "complete demolition of buildings", "partial demolition of buildings", "forests destruction ", "breaking windows"};

    private double radius;
    private double asteroidMass;
    private int velocity;
    private int angle;

    private int projectileDensityListId;
    private double projectileDensityValue;
    private String projectileDensityName;

    private int targetDensityListId;
    private double targetDensityValue;
    private String targetDensityName;

    private int longitudeDegree;
    private int longitudeMinute;
    private int longitudeSeconds;
    private int longitudeLocationListId;
    private String longitudeLocation;
    private int longitudeDegreeResult;
    private int longitudeMinuteResult;
    private int longitudeSecondsResult;
    private String longitudeLocationResult;

    private int latitudeDegree;
    private int latitudeMinute;
    private int latitudeSeconds;
    private int latitudeLocationListId;
    private String latitudeLocation;
    private int latitudeDegreeResult;
    private int latitudeMinuteResult;
    private int latitudeSecondsResult;
    private String latitudeLocationResult;

    private int flightDirectionListId;
    private String flightDirection;

    public double[] velocityVector;
    public double[] massVector;
    public double[] angleVector;
    public double[] yVector;
    public double[] xVector;
    public double[] timeVector;

    String[] densitySpinner1Choices = {"Ice (1000 kg/m^3)", "Porous rock (1500 kg/m^3)", "Dense rock (3000 kg/m^3)", "Iron (8000 kg/m^3)"};
    String[] densitySpinner2Choices = {"Sedimentary rock (2500 kg/m^3)", "Igneous rocks (2750 kg/m^3)"};
    String[] directionSpinnerChoices = {"E", "W", "N", "S", "NE", "NW", "SE", "SW"};

    String[] entryLatitudeSpinnerChoices = {"N", "S"};
    String[] entryLongitudeSpinnerChoices = {"W", "E"};

    String[] densityName1 = {"Ice", "Porous rock", "Dense rock", "Iron"};
    double[] densityValue1 = {1000, 1500, 3000, 8000};
    String[] densityName2 = {"Sedimentary rock", "Igneous rocks"};
    double[] densityValue2 = {2500, 2750};

    //variables for results
    double time;
    double impact_x;
    double impact_mass;
    double impact_velocity;
    double impact_angle;
    double impact_kinetic_energy;
    double impact_power_tnt;
    double[] effects_of_shock_wave;
    double impact_crater_radius;

    // for map
    private MapView mapView;
    private GoogleMap gmap;

    //other
    LayoutInflater inflater;
    FrameLayout frameLayoutForResult;
    View mapViewContainer;
    View dataViewContainer;
    Button buttonMap;
    Button buttonData;
    int currentlyDisplayedLayout = MAP_LAYOUT_ID;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_result);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Intent intent = getIntent();

        radius = Double.parseDouble(Objects.requireNonNull(intent.getStringExtra("diameter"))) / 2;
        velocity = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("velocity"))) * 1000;
        angle = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("angle")));

        projectileDensityListId = intent.getIntExtra("projectileDensityListId", 0);
        projectileDensityValue = densityValue1[projectileDensityListId];
        projectileDensityName = densityName1[projectileDensityListId];

        targetDensityListId = intent.getIntExtra("targetDensityListId", 0);
        targetDensityValue = densityValue2[targetDensityListId];
        targetDensityName = densityName2[targetDensityListId];

        longitudeDegree = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("longitudeDegree")));
        longitudeMinute = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("longitudeMinute")));
        longitudeSeconds = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("longitudeSeconds")));
        longitudeLocationListId = intent.getIntExtra("longitudeLocationListId", 0);
        longitudeLocation = entryLongitudeSpinnerChoices[longitudeLocationListId];

        latitudeDegree = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("latitudeDegree")));
        latitudeMinute = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("latitudeMinute")));
        latitudeSeconds = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("latitudeSeconds")));
        latitudeLocationListId = intent.getIntExtra("latitudeLocationListId", 0);
        latitudeLocation = entryLatitudeSpinnerChoices[latitudeLocationListId];

        flightDirectionListId = intent.getIntExtra("flightDirectionListId", 0);
        flightDirection = directionSpinnerChoices[flightDirectionListId];

        asteroidMass = (4.0 / 3.0) * Math.PI * radius * radius * radius * projectileDensityValue;

        dataViewContainer = inflater.inflate(R.layout.data_layout, null, false); //prepare view from xlm

        mapViewContainer = inflater.inflate(R.layout.map_layout, null, false);
        frameLayoutForResult = (FrameLayout) findViewById(R.id.result_container);
        frameLayoutForResult.addView(mapViewContainer);

        mapView = findViewById(R.id.map_view);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("12312412");
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        //run calculations
        ComputingTask computingTask = new ComputingTask();
        computingTask.start();

        buttonMap = (Button) findViewById(R.id.button_map);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentlyDisplayedLayout != MAP_LAYOUT_ID) {
                    frameLayoutForResult.removeAllViews();
                    frameLayoutForResult.addView(mapViewContainer);
                    currentlyDisplayedLayout = MAP_LAYOUT_ID;
                }
            }
        });

        buttonData = (Button) findViewById(R.id.button_data);
        buttonData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentlyDisplayedLayout != DATA_LAYOUT_ID) {
                    frameLayoutForResult.removeAllViews();
                    frameLayoutForResult.addView(dataViewContainer);
                    configureDataLayout();
                    currentlyDisplayedLayout = DATA_LAYOUT_ID;
                }
            }
        });
    }



    public void getResultGeographicalCoordinates(int degrees_long, int minutes_long, int seconds_long, String direct_long, int degrees_lati, int minutes_lati, int seconds_lati, String direct_lati, String flight, double x) {
        double latitude = (degrees_lati + minutes_lati / 60. + seconds_lati / 3600.) * Math.PI / 180.;
        double longitude = (degrees_long + minutes_long / 60. + seconds_long / 3600.) * Math.PI / 180.;

        // take negative E i N dla W i S (longitude -180...180E, latitude -90...90N)
        if (direct_long.equals("W")) {
            longitude = -longitude;
            direct_long = "E";
        }

        if (direct_lati.equals("S")) {
            latitude = -latitude;
            direct_lati = "N";
        }

        double bearing = 0;

        switch (flight) {
            case "N":
                bearing = 0;
                break;
            case "E":
                bearing = 90;
                break;
            case "S":
                bearing = 180;
                break;
            case "W":
                bearing = 270;
                break;
            case "NE":
                bearing = 45;
                break;
            case "SE":
                bearing = 135;
                break;
            case "SW":
                bearing = 225;
                break;
            case "NW":
                bearing = 315;
                break;
        }

        bearing = bearing * Math.PI / 180;
        double R = 6.37814 * 1000;
        double sigma = x / R;

        // Calculation based on https://www.movable-type.co.uk/scripts/latlong.html
        double latitude_start = Math.asin(Math.sin(latitude) * Math.cos(sigma) + Math.cos(latitude) * Math.sin(sigma) * Math.cos(bearing));
        double longitude_start = longitude + Math.atan2(Math.sin(bearing) * Math.sin(sigma) * Math.cos(latitude), Math.cos(sigma) - Math.sin(latitude) * Math.sin(latitude_start));

        longitude_start = longitude_start * 180 / Math.PI;
        latitude_start = latitude_start * 180 / Math.PI;
        longitude_start = (longitude_start + 540) % 360 - 180;

        if (longitude_start < 0) {
            longitude_start = -longitude_start;
            direct_long = "W";
        }
        if (latitude_start < 0) {
            latitude_start = -latitude_start;
            direct_lati = "S";
        }

        latitudeDegreeResult = (int) Math.floor(latitude_start);
        longitudeDegreeResult = (int) Math.floor(longitude_start);

        latitudeMinuteResult = (int) Math.floor((latitude_start - latitudeDegreeResult) * 60);
        longitudeMinuteResult = (int) Math.floor((longitude_start - longitudeDegreeResult) * 60);

        latitudeSecondsResult = (int) Math.floor(((latitude_start - latitudeDegreeResult) * 60 - latitudeMinuteResult) * 60);
        longitudeSecondsResult = (int) Math.floor(((longitude_start - longitudeDegreeResult) * 60 - longitudeMinuteResult) * 60);

        latitudeLocationResult = direct_lati;
        longitudeLocationResult = direct_long;
    }


    public double funcV(double t, double v, double m, double theta, double y, double x) {
        double r = Math.pow((m / asteroidMass), 1.0 / 3.0) * radius;
        double rho = rho_a * Math.exp(-y / 8400);
        return (-1) * (C_d * (Math.PI * (r * r)) * rho * (v * v)) / (m) + (g_0 * Math.pow((R_e / (R_e + y)), 2)) * Math.sin(theta);
    }


    public double funcM(double t, double v, double m, double theta, double y, double x) {
        double r = Math.pow((m / asteroidMass), 1.0 / 3.0) * radius;
        double rho = rho_a * Math.exp(-y / 8400);
        return (-1) * (1.0 / 2.0) * sigma_ab * C_d * rho * (Math.PI * (r * r)) * v * v * v;
    }


    public double funcTheta(double t, double v, double m, double theta, double y, double x) {
        return ((v / (R_e + y)) + ((g_0 * Math.pow((R_e / (R_e + y)), 2)) / v)) * Math.cos(theta);
    }


    public double funcY(double t, double v, double m, double theta, double y, double x) {
        return -v * Math.sin(theta);
    }


    public double funcX(double t, double v, double m, double theta, double y, double x) {
        return v * Math.cos(theta);
    }



    public void timeVectorGenerator(double maxTime) {
        timeVector = new double[(int) (maxTime * (1 / CONST_H))];
        timeVector[0] = 0;
        for (int i = 1; i < timeVector.length; i++) {
            timeVector[i] = timeVector[i - 1] + CONST_H;
        }
    }



    public void rk4_method_5(double h) {
        velocityVector = new double[timeVector.length];
        massVector = new double[timeVector.length];
        angleVector = new double[timeVector.length];
        yVector = new double[timeVector.length];
        xVector = new double[timeVector.length];

        velocityVector[0] = velocity;
        massVector[0] = asteroidMass;
        angleVector[0] = Math.toRadians((double) angle);
        yVector[0] = CONST_ATMOSPHERE_HEIGHT;
        xVector[0] = 0;

        for (int j = 0; j < timeVector.length - 1; j++) {
            double k_1 = funcV(timeVector[j], velocityVector[j], massVector[j], angleVector[j], yVector[j], xVector[j]) * h;
            double l_1 = funcM(timeVector[j], velocityVector[j], massVector[j], angleVector[j], yVector[j], xVector[j]) * h;
            double m_1 = funcTheta(timeVector[j], velocityVector[j], massVector[j], angleVector[j], yVector[j], xVector[j]) * h;
            double n_1 = funcY(timeVector[j], velocityVector[j], massVector[j], angleVector[j], yVector[j], xVector[j]) * h;
            double o_1 = funcX(timeVector[j], velocityVector[j], massVector[j], angleVector[j], yVector[j], xVector[j]) * h;

            double k_2 = funcV(timeVector[j] + 0.5 * h, velocityVector[j] + 0.5 * k_1, massVector[j] + 0.5 * l_1, angleVector[j] + 0.5 * m_1, yVector[j] + 0.5 * n_1, xVector[j] + 0.5 * o_1) * h;
            double l_2 = funcM(timeVector[j] + 0.5 * h, velocityVector[j] + 0.5 * k_1, massVector[j] + 0.5 * l_1, angleVector[j] + 0.5 * m_1, yVector[j] + 0.5 * n_1, xVector[j] + 0.5 * o_1) * h;
            double m_2 = funcTheta(timeVector[j] + 0.5 * h, velocityVector[j] + 0.5 * k_1, massVector[j] + 0.5 * l_1, angleVector[j] + 0.5 * m_1, yVector[j] + 0.5 * n_1, xVector[j] + 0.5 * o_1) * h;
            double n_2 = funcY(timeVector[j] + 0.5 * h, velocityVector[j] + 0.5 * k_1, massVector[j] + 0.5 * l_1, angleVector[j] + 0.5 * m_1, yVector[j] + 0.5 * n_1, xVector[j] + 0.5 * o_1) * h;
            double o_2 = funcX(timeVector[j] + 0.5 * h, velocityVector[j] + 0.5 * k_1, massVector[j] + 0.5 * l_1, angleVector[j] + 0.5 * m_1, yVector[j] + 0.5 * n_1, xVector[j] + 0.5 * o_1) * h;

            double k_3 = funcV(timeVector[j] + 0.5 * h, velocityVector[j] + 0.5 * k_2, massVector[j] + 0.5 * l_2, angleVector[j] + 0.5 * m_2, yVector[j] + 0.5 * n_2, xVector[j] + 0.5 * o_2) * h;
            double l_3 = funcM(timeVector[j] + 0.5 * h, velocityVector[j] + 0.5 * k_2, massVector[j] + 0.5 * l_2, angleVector[j] + 0.5 * m_2, yVector[j] + 0.5 * n_2, xVector[j] + 0.5 * o_2) * h;
            double m_3 = funcTheta(timeVector[j] + 0.5 * h, velocityVector[j] + 0.5 * k_2, massVector[j] + 0.5 * l_2, angleVector[j] + 0.5 * m_2, yVector[j] + 0.5 * n_2, xVector[j] + 0.5 * o_2) * h;
            double n_3 = funcY(timeVector[j] + 0.5 * h, velocityVector[j] + 0.5 * k_2, massVector[j] + 0.5 * l_2, angleVector[j] + 0.5 * m_2, yVector[j] + 0.5 * n_2, xVector[j] + 0.5 * o_2) * h;
            double o_3 = funcX(timeVector[j] + 0.5 * h, velocityVector[j] + 0.5 * k_2, massVector[j] + 0.5 * l_2, angleVector[j] + 0.5 * m_2, yVector[j] + 0.5 * n_2, xVector[j] + 0.5 * o_2) * h;

            double k_4 = funcV(timeVector[j] + h, velocityVector[j] + k_3, massVector[j] + l_3, angleVector[j] + m_3, yVector[j] + n_3, xVector[j] + o_3) * h;
            double l_4 = funcM(timeVector[j] + h, velocityVector[j] + k_3, massVector[j] + l_3, angleVector[j] + m_3, yVector[j] + n_3, xVector[j] + o_3) * h;
            double m_4 = funcTheta(timeVector[j] + h, velocityVector[j] + k_3, massVector[j] + l_3, angleVector[j] + m_3, yVector[j] + n_3, xVector[j] + o_3) * h;
            double n_4 = funcY(timeVector[j] + h, velocityVector[j] + k_3, massVector[j] + l_3, angleVector[j] + m_3, yVector[j] + n_3, xVector[j] + o_3) * h;
            double o_4 = funcX(timeVector[j] + h, velocityVector[j] + k_3, massVector[j] + l_3, angleVector[j] + m_3, yVector[j] + n_3, xVector[j] + o_3) * h;

            velocityVector[j + 1] = velocityVector[j] + (1.0 / 6.0) * (k_1 + 2 * k_2 + 2 * k_3 + k_4);
            massVector[j + 1] = massVector[j] + (1.0 / 6.0) * (l_1 + 2 * l_2 + 2 * l_3 + l_4);
            angleVector[j + 1] = angleVector[j] + (1.0 / 6.0) * (m_1 + 2 * m_2 + 2 * m_3 + m_4);
            yVector[j + 1] = yVector[j] + (1.0 / 6.0) * (n_1 + 2 * n_2 + 2 * n_3 + n_4);
            xVector[j + 1] = xVector[j] + (1.0 / 6.0) * (o_1 + 2 * o_2 + 2 * o_3 + o_4);
        }
    }



    public double find_impact_time() throws Exception {
        for (int i = 0; i < yVector.length - 1; i++) {
            if (yVector[i] * yVector[i + 1] < 0) {
                return Math.round((timeVector[i] + timeVector[i + 1]) / 2 * 100.0) / 100.0;
            }
        }
        throw new Exception("Max time too short");
    }

    public double find_impact_x() throws Exception {
        for (int i = 0; i < yVector.length - 1; i++) {
            if (yVector[i] * yVector[i + 1] < 0) {
                return Math.round((xVector[i] + xVector[i + 1]) / 2 * 100.0) / 100.0;
            }
        }
        throw new Exception("Max time too short");
    }

    public double find_impact_mass() throws Exception {
        for (int i = 0; i < yVector.length - 1; i++) {
            if (yVector[i] * yVector[i + 1] < 0) {
                return Math.round((massVector[i] + massVector[i + 1]) / 2 * 100.0) / 100.0;
            }
        }
        throw new Exception("Max time too short");
    }

    public double find_impact_velocity() throws Exception {
        for (int i = 0; i < yVector.length - 1; i++) {
            if (yVector[i] * yVector[i + 1] < 0) {
                return Math.round((velocityVector[i] + velocityVector[i + 1]) / 2 * 100.0) / 100.0;
            }
        }
        throw new Exception("Max time too short");
    }

    public double find_impact_angle() throws Exception {
        for (int i = 0; i < yVector.length - 1; i++) {
            if (yVector[i] * yVector[i + 1] < 0) {
                return Math.round((angleVector[i] + angleVector[i + 1]) / 2 * 100.0) / 100.0;
            }
        }
        throw new Exception("Max time too short");
    }

    public double find_impact_kinetic_energy() throws Exception {
        double tempImpactVelocity = find_impact_velocity();
        return (find_impact_mass() * tempImpactVelocity * tempImpactVelocity) / 2;
    }

    public double find_impact_power_TNT() throws Exception {
        return (find_impact_kinetic_energy() / E);
    }

    public double[] effects_of_shock_wave() throws Exception {
        double q = find_impact_power_TNT();
        double[] r_results = new double[delta_p.length];
        for (int i = 0; i < delta_p.length; i++) {
            double p = delta_p[i] / 98066.5;
            r_results[i] = (1 / p * 0.00529134 * Math.pow(47250000 * p * p * q + Math.sqrt(22325625 * Math.pow(10, 8) * p * p * p * p * q * q + 3504043125.0 * Math.pow(10, 5) * p * p * p * q * q + 19373146312500.0 * p * p * q * q)
                    + 5127750 * p * q + 297754 * q, 1.0 / 3.0) - (0.00839947 * (-32250 * p * Math.pow(q, 2.0 / 3.0) - 2809 * Math.pow(q, 2.0 / 3.0))) / (p * Math.pow(47250000 * p * p * q +
                    Math.sqrt(2232562500000000.0 * p * p * p * p * q * q + 350404312500000.0 * p * p * p * q * q + 19373146312500.0 * p * p * q * q) +
                    5127750 * p * q + 297754 * q, 1.0 / 3.0)) + 0.353333 * Math.pow(q, (1.0 / 3.0)) / p); // equation solved by wolfram alpha
        }
        return r_results;
    }

    public double find_impact_crater_radius() {
        return 0.97 * Math.pow(impact_kinetic_energy / (densityValue2[targetDensityListId] * EARTH_G), 1.0 / 4.0) / 2;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void configureMap() {
        double impactLongitudeForMapsSDK = longitudeDegreeResult;
        double impactLatitudeForMapsSDK = latitudeDegreeResult;

        if (longitudeLocationResult.equals("W")) {
            impactLongitudeForMapsSDK = -impactLongitudeForMapsSDK;
        }
        impactLongitudeForMapsSDK += (longitudeMinuteResult * 60 + longitudeSecondsResult) / 3600.0;

        if (latitudeLocationResult.equals("S")) {
            impactLatitudeForMapsSDK = -impactLatitudeForMapsSDK;
        }
        impactLatitudeForMapsSDK += (latitudeMinuteResult * 60 + latitudeSecondsResult) / 3600.0;

        gmap.getUiSettings().setZoomControlsEnabled(true);
        LatLng latLng = new LatLng(impactLatitudeForMapsSDK, impactLongitudeForMapsSDK);

        gmap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Impact place"));

        gmap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(impact_crater_radius)
                .strokeColor(R.color.colorGray)
                .fillColor(R.color.colorGray));

        for (int i = 0; i < effects_of_shock_wave.length; i++) {

            gmap.addCircle(new CircleOptions()
                    .center(latLng)
                    .radius(effects_of_shock_wave[i])
                    .strokeColor(effects_of_shock_wave_colors[i]));
        }
        gmap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }


    public void configureDataLayout() {
        TextView textView;
        textView = (TextView) findViewById(R.id.data_layout_initial_diameter);
        textView.setText(String.valueOf((int) radius * 2));

        DecimalFormat formatter = new DecimalFormat("0.####E0");
        textView = (TextView) findViewById(R.id.data_layout_initial_mass);
        textView.setText(String.valueOf(formatter.format(asteroidMass)));

        textView = (TextView) findViewById(R.id.data_layout_initial_velocity);
        textView.setText(String.valueOf(velocity / 1000));

        textView = (TextView) findViewById(R.id.data_layout_initial_angle);
        textView.setText(String.valueOf(angle));

        textView = (TextView) findViewById(R.id.data_layout_initial_projectile_density);
        textView.setText(String.valueOf(densityValue1[projectileDensityListId]) + "\n" + densityName1[projectileDensityListId]);

        textView = (TextView) findViewById(R.id.data_layout_initial_target_density);
        textView.setText(String.valueOf(densityValue2[targetDensityListId]) + "\n" + densityName2[targetDensityListId]);

        //results
        double newRadius = Math.pow((3 * impact_mass) / (4 * Math.PI * densityValue1[projectileDensityListId]), 1.0 / 3.0);

        textView = (TextView) findViewById(R.id.data_layout_result_diameter);
        textView.setText(String.valueOf(String.format("%.2f", newRadius * 2)));

        textView = (TextView) findViewById(R.id.data_layout_result_mass);
        textView.setText(String.valueOf(formatter.format(impact_mass)));

        textView = (TextView) findViewById(R.id.data_layout_result_velocity);
        textView.setText(String.valueOf(String.format("%.4f", impact_velocity / 1000)));

        textView = (TextView) findViewById(R.id.data_layout_result_angle);
        textView.setText(String.valueOf(String.format("%.2f", impact_angle * 180 / Math.PI)));

        textView = (TextView) findViewById(R.id.data_layout_result_crater_diameter);
        textView.setText(String.valueOf(String.format("%.2f", impact_crater_radius * 2)));

        // legend
        for (int i = 0; i < legend_impact_effects.length; i++) {
            String id = "data_layout_result_impact_legend_";
            id += String.valueOf(i + 1);
            textView = (TextView) findViewById(getResources().getIdentifier(id, "id", getPackageName()));
            textView.setText(String.valueOf(legend_impact_effects[i]));
        }
    }


    private class ComputingTask extends Thread {
        @Override
        public void run() {
            timeVectorGenerator(CONST_MAX_TIME);
            rk4_method_5(CONST_H);
            try {
                time = find_impact_time();
                impact_x = find_impact_x();
                impact_mass = find_impact_mass();
                impact_velocity = find_impact_velocity();
                impact_angle = find_impact_angle();
                impact_kinetic_energy = find_impact_kinetic_energy();
                impact_power_tnt = find_impact_power_TNT();
                effects_of_shock_wave = effects_of_shock_wave();
                impact_crater_radius = find_impact_crater_radius();
            } catch (Exception e) {
                Toast.makeText(ResultActivity.this, "Something went wrong, please try another settings", Toast.LENGTH_LONG).show();
            }
            getResultGeographicalCoordinates(longitudeDegree, longitudeMinute, longitudeSeconds, longitudeLocation, latitudeDegree, latitudeMinute, latitudeSeconds, latitudeLocation, flightDirection, impact_x / 1000);

            runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    configureMap();
                }
            });
        }
    }
}