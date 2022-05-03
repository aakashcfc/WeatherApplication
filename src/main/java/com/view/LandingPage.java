package com.view;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import com.service.WeatherService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
//import com.vaadin.flow.component.UI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI(path = "")
public class LandingPage<cityName> extends UI {
    
	@Autowired
    private WeatherService weatherService;
    private VerticalLayout mainLayout;
    private NativeSelect<String> unitSelect;
    private TextField cityTextField;
    private Button searchButton;
    private Label location ;
    private Label currentTemp;
    private Label weatherDescription;
    private Label weatherMin;
    private Label weatherMax;
    private Label pressureLabel;
    private Label humidityLabel;
    private Label windSpeedLabel;
    private Label feelsLike;
    private Image iconImg;
    private HorizontalLayout Dashboard;
    private HorizontalLayout mainDescriptionLayout;
    private Image logo;
    private HorizontalLayout footer;


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setUpLayout();
        setHeader();
        setLogo();
        setForm();
        dashboardTitle();
        dashboardDetails();

        footer();
        searchButton.addClickListener(clickEvent -> {
           if (!cityTextField.getValue().equals("")){
               try {
                   updateUI();
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }else
               Notification.show("Enter a valid city name");
        });


    }



    public void setUpLayout() {
        
    	logo = new Image();
        iconImg = new Image();
        iconImg.setWidth("300px");
        iconImg.setHeight("300px");
        mainLayout = new VerticalLayout();
        mainLayout.setWidth("100%");
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        mainLayout.setStyleName("BackColorGrey");
        setContent(mainLayout);
    }
    
    private void setHeader(){
        
    	HorizontalLayout headerlayout = new HorizontalLayout();
        headerlayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Label Title = new Label("NORTHWESTERN MUTUAL WEATHER APPLICATION");
        Title.addStyleName(ValoTheme.LABEL_H1);
        Title.addStyleName(ValoTheme.LAYOUT_CARD);
        Title.addStyleName(ValoTheme.LABEL_COLORED);

        headerlayout.addComponents(Title);
        mainLayout.addComponents(headerlayout);


    }
    
    
    private void setLogo() {
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);

        logo.setSource(new ExternalResource("https://bcc-usa.com/wp-content/uploads/Northwestern-Mutual-Logo.jpg"));
        logo.setWidth("500px");
        logo.setHeight("440px");
        logo.setVisible(true);
        
        logoLayout.addComponents(logo);
        mainLayout.addComponents(logoLayout);
    }
    
    
    private void setForm(){
        HorizontalLayout formLayout = new HorizontalLayout();
        formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        formLayout.setSpacing(true);
        formLayout.setMargin(true);

        //Selection Components
        unitSelect = new NativeSelect<>();
        unitSelect.setWidth("100px");
        ArrayList<String> items = new ArrayList<>();
        items.add("C");
        items.add("F");

        unitSelect.setItems(items);
        unitSelect.setValue(items.get(0));
        formLayout.addComponents(unitSelect);


        //cityTextField
        cityTextField = new TextField();
        cityTextField.setWidth("180%");
        formLayout.addComponents(cityTextField);

        //Search Icon
        searchButton = new Button();
        searchButton.setIcon(VaadinIcons.SEARCH);
        formLayout.addComponent(searchButton);
        mainLayout.addComponents(formLayout);
    }
    
    
    private void dashboardTitle() {
        Dashboard = new HorizontalLayout();
        Dashboard.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        location = new Label("Currently in");
        location.addStyleName(ValoTheme.LABEL_H2);
        location.addStyleName(ValoTheme.LABEL_LIGHT);

        currentTemp = new Label("10F");
        currentTemp.setStyleName(ValoTheme.LABEL_BOLD);
        currentTemp.setStyleName(ValoTheme.LABEL_H1);
        Dashboard.addComponents(location,iconImg, currentTemp);


    }
    
    private void dashboardDetails(){
        mainDescriptionLayout = new HorizontalLayout();
        mainDescriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

       
        VerticalLayout descriptionLayout = new VerticalLayout();
        descriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

       
        weatherDescription = new Label("Description: Clear Skies");
        weatherDescription.setStyleName(ValoTheme.LABEL_SUCCESS);
        descriptionLayout.addComponents(weatherDescription);

       
        weatherMin = new Label("Min:53");
        descriptionLayout.addComponents(weatherMin);
        
        weatherMax = new Label("Max:22");
        descriptionLayout.addComponents(weatherMax);


        VerticalLayout pressureLayout = new VerticalLayout();
        pressureLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        pressureLabel = new Label("Pressure:123Pa");
        pressureLayout.addComponents(pressureLabel);

        humidityLabel = new Label("Humidity:34");
        pressureLayout.addComponents(humidityLabel);

        windSpeedLabel = new Label("124/hr");
        pressureLayout.addComponents(windSpeedLabel);

        feelsLike = new Label("Feels Like:");
        pressureLayout.addComponents(feelsLike);

        mainDescriptionLayout.addComponents(descriptionLayout, pressureLayout);

    }

    //footer
    private void footer(){
        
    	footer = new HorizontalLayout();
        footer.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
        footer.setSpacing(true);
        footer.setMargin(true);
        footer.setWidth("100%");
        footer.setHeight("40px");
        Label description = new Label();
        description.setValue("Submission for coding challenge 2022 by AAKASH SARNOBAT");
        footer.addComponents(description);
        mainLayout.addComponents(footer);
    }


    //call on search button click
    private void updateUI() throws JSONException {
        
        String city = cityTextField.getValue();
        String defaultUnit;
        weatherService.setCityName(city);

        //Checking Units and Assigning value to API url using setUnit()
        if(unitSelect.getValue().equals("F")){
            weatherService.setUnit( "imperials");
            unitSelect.setValue("F");
            defaultUnit = "\u00b0"+"F";
            }else {
            weatherService.setUnit("metric");
            defaultUnit = "\u00b0" + "C";
            unitSelect.setValue("C");
        }

   
        location.setValue("THE WEATHER NOW IN "+city.toUpperCase());
        JSONObject mainObject = weatherService.returnMainObject();
        int temp = mainObject.getInt("temp");
        currentTemp.setValue(temp + defaultUnit);

        String iconCode = null;
        String weatherDescriptionNew = null;
        JSONArray jsonArray = weatherService.returnWeatherArray();
         for (int i = 0; i< jsonArray.length(); i++){
            JSONObject weatherObject = jsonArray.getJSONObject(i);
            iconCode = weatherObject.getString("icon");
            weatherDescriptionNew = weatherObject.getString("description");
           
          }
        //Setting icon between city name and temp
         iconImg.setSource(new ExternalResource("http://openweathermap.org/img/wn/"+iconCode+"@2x.png"));
         
        //Setting Icon as Main Logo
        logo.setSource(new ExternalResource("http://openweathermap.org/img/wn/" + iconCode + "@2x.png"));


        //updating Weather Description
        weatherDescription.setValue("Description: "+weatherDescriptionNew);

        //Updating Max Temp
        weatherMax.setValue("Max Temp: "+weatherService.returnMainObject().getInt("temp_max")+"\u00b0" +unitSelect.getValue());
        //Updating Min Temp
        weatherMin.setValue("Min Temp: "+weatherService.returnMainObject().getInt("temp_min")+"\u00b0" +unitSelect.getValue());
        //Updating Pressure
        pressureLabel.setValue("Pressure: "+weatherService.returnMainObject().getInt("pressure"));
        //Updating Humidity
        humidityLabel.setValue("Humidity: "+weatherService.returnMainObject().getInt("humidity"));

        //Updating Wind
        windSpeedLabel.setValue("Wind: "+weatherService.returnWindObject().getInt("speed")+"m/s");
        //Updating Feels Like
        feelsLike.setValue("Feels Like: "+weatherService.returnMainObject().getDouble("feels_like"));



        //Adding Dashboard and main Description
        //it will only appear when the search button is clicked
        mainLayout.addComponents(Dashboard,mainDescriptionLayout,footer);
    }

}
