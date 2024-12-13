# Weather-Forecast

This repository contains Java scripts for forecasting weather based on location using the `openweathermap.org` API.

## How to Use

**Create config.properties file**: Add your API key to the file

```bash
api.key=YOUR_API_KEY
```

**Compile and Run WeatherApp.java**: 

```bash
javac WeatherApp.java

java WeatherApp
```

**Example Output**:

```
Enter city name: Linz

Weather in Linz:
Temperature: 0.84 °C
Humidity: 84%
Description: broken clouds
```