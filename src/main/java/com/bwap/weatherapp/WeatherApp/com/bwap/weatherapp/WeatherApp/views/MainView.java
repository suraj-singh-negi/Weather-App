package com.bwap.weatherapp.WeatherApp.com.bwap.weatherapp.WeatherApp.views;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import com.bwap.weatherapp.WeatherApp.com.bwap.weatherapp.WeatherApp.controller.WeatherService;

import java.util.ArrayList;

@SpringUI(path= "")
public class MainView extends UI {

    @Autowired
    private WeatherService weatherService;

    private VerticalLayout mainLyout;
    private NativeSelect<String> unitSelect;
    private TextField cityTextField;
    private Button searchButton;
    private HorizontalLayout dashboard;
    private Label location;
    private Label  currentTemp;
    private HorizontalLayout mainDescriptionLayout;
    private Label weatherDescription;
    private Label MaxWeather;
    private Label MinWeather;
    private Label Humidity;
    private Label Pressure;
    private Label Wind;
    private Label FeelsLike;
    private Image iconImg;

    protected void init(VaadinRequest vaadinRequest) {
        mainLayout();
        setHeader();
        setLogo();
        setForm();
        dashboardTitle();
        dashboardDetails();
        searchButton.addClickListener(clickEvent -> {
            if(!cityTextField.getValue().equals("")){
            try{
                updateUI();
            }catch (JSONException e){
                e.printStackTrace();
            }}
            else{
                Notification.show("Please Enter The City");
            }
        }
        );
    }

    private void mainLayout() {
        iconImg = new Image();
        mainLyout = new VerticalLayout();
        mainLyout.setWidth("100%");
        mainLyout.setSpacing(true);
        mainLyout.setMargin(true);
        mainLyout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setContent(mainLyout);
    }

    private void setHeader(){
        HorizontalLayout header = new HorizontalLayout();
        header.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Label title = new Label("Weather App");
        header.addComponent(title);
        mainLyout.addComponent(header);
    }

    private void setLogo(){
        HorizontalLayout logo= new HorizontalLayout();
        logo.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Image img= new Image(null,new ClassResource("/static/logo.png"));
        logo.setWidth("256px");
        logo.setHeight("256px");
        logo.setVisible(true);
        logo.addComponent(img);
        mainLyout.addComponent(logo);
    }

    private void setForm(){
        HorizontalLayout formLayout=new HorizontalLayout();
        formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        formLayout.setSpacing(true);
        formLayout.setMargin(true);

        unitSelect = new NativeSelect<>();
        ArrayList<String> items = new ArrayList<String>();
        items.add("C");
        items.add("F");
        unitSelect.setItems(items);
        unitSelect.setValue(items.get(1));
        formLayout.addComponent(unitSelect);

        cityTextField = new TextField();
        cityTextField.setWidth("80%");
        formLayout.addComponent(cityTextField);

        searchButton = new Button();
        searchButton.setIcon(VaadinIcons.SEARCH);

        formLayout.addComponent(searchButton);

        mainLyout.addComponent(formLayout);
    }

    private void dashboardTitle(){
        dashboard = new HorizontalLayout();
        dashboard.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        location = new Label("Currently in Delhi");
        location.addStyleName(ValoTheme.LABEL_H2);
        location.addStyleName(ValoTheme.LABEL_LIGHT);

        currentTemp = new Label("10C");
        currentTemp.setStyleName(ValoTheme.LABEL_BOLD);
        currentTemp.setStyleName(ValoTheme.LABEL_H1);

        dashboard.addComponents(location,iconImg,currentTemp);
    }

    private void dashboardDetails(){
        mainDescriptionLayout = new HorizontalLayout();
        mainDescriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        VerticalLayout description = new VerticalLayout();
        description.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        weatherDescription = new Label("Descrition: Clear Skies");
        weatherDescription.setStyleName(ValoTheme.LABEL_SUCCESS);

        description.addComponent(weatherDescription);

        MinWeather = new Label("Min:53");
        description.addComponent(MinWeather);

        MaxWeather = new Label("Max:53");
        description.addComponent(MaxWeather);

        VerticalLayout pressureLayout = new VerticalLayout();
        pressureLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Pressure = new Label("Pressure:230Pa");
        pressureLayout.addComponent(Pressure);

        Humidity = new Label("Humidity:23");
        pressureLayout.addComponent(Humidity);

        Wind = new Label("wind:230Pa");
        pressureLayout.addComponent(Wind);

        FeelsLike = new Label("FeelsLike:230Pa");
        pressureLayout.addComponent(FeelsLike);

        mainDescriptionLayout.addComponents(description,pressureLayout);

    }

    private void updateUI(){
        String city = cityTextField.getValue();
        String defaultUnit;
        location.setValue("Currently in "+city);
        weatherService.setCityName(city);
        if(unitSelect.getValue().equals("F")){
            weatherService.setUnit("imperials");
            unitSelect.setValue("F");
            defaultUnit="\u00b0"+"F";
        }
        else{
            weatherService.setUnit("metric");
            defaultUnit="\u00b0"+"C";
            unitSelect.setValue("C");
        }
        JSONObject mainObject=weatherService.returnMain();
        int temp=mainObject.getInt("temp");
        currentTemp.setValue(temp+defaultUnit);

        String iconCode = null;
        String weatherDescriptionNew = null;
        JSONArray jsonArray=weatherService.returnWeatherArray();
        for(int i=0;i<jsonArray.length();i++){
            JSONObject weatherobj=jsonArray.getJSONObject(i);
            iconCode = weatherobj.getString("icon");
            weatherDescriptionNew = weatherobj.getString("description");
        }

        iconImg.setSource(new ExternalResource("http://openweathermap.org/img/wn/"+iconCode+"@2x.png"));

        weatherDescription.setValue("Description: "+weatherDescriptionNew);
        MinWeather.setValue("Min Temp: "+ weatherService.returnMain().getInt("temp_min")+unitSelect.getValue());
        MaxWeather.setValue("Max Temp: "+ weatherService.returnMain().getInt("temp_max")+unitSelect.getValue());
        Pressure.setValue("Pressure: "+ weatherService.returnMain().getInt("pressure"));
        Humidity.setValue("Humidity: "+ weatherService.returnMain().getInt("humidity"));
        Wind.setValue("Wind: "+ weatherService.returnWind().getInt("speed"));
        FeelsLike.setValue("Feels Like: "+ weatherService.returnMain().getDouble("feels_like"));

        mainLyout.addComponents(dashboard, mainDescriptionLayout);
    }
}
