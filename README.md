# MiniDash

**MiniDash** is a full-stack dashboard application that aggregates live data from multiple external APIs, including weather, cryptocurrency, and news. Built with Spring Boot (Java backend) and React (frontend), it provides a centralized view for real-time public data.

## Features

- 🌤 **Weather**: Fetch current weather data by city using the OpenWeather API
- 📰 **News**: Get top news articles filtered by topic using the News API
- 💰 **Cryptocurrency**: Display top 5 cryptocurrencies by market cap from the CoinGecko API
- ✅ Fully unit-tested services and controllers using Mockito and MockMvc
- ✅ Properties and secrets externalized using `ExternalApiProperties`
- 📦 Planned Dockerization and AWS deployment (coming soon)

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.5, WebClient, JUnit 5, Mockito, MockMvc
- **Frontend**: React (in progress)
- **Testing**: JUnit, Mockito, Spring Boot Test
- **External APIs**:
  - OpenWeatherMap
  - NewsAPI
  - CoinGecko

## Project Structure

```
src/
└── main/
    ├── java/com/platform/
    │   ├── controller/        # REST controllers for Weather, News, Crypto
    │   ├── service/           # Services for calling external APIs
    │   └── config/            # ExternalApiProperties config binding
    └── resources/
        └── application.properties
└── test/
    └── java/com/platform/
        ├── controller/        # Controller tests with MockMvc
        └── service/           # Service tests with mocked WebClient
```

## API Endpoints

| Endpoint             | Method | Query Param | Description                      |
|---------------------|--------|-------------|----------------------------------|
| `/api/weather`      | GET    | `city`      | Returns current weather by city |
| `/api/news`         | GET    | `topic`     | Returns news articles by topic  |
| `/api/crypto/top`   | GET    | —           | Returns top 5 cryptocurrencies  |

## Configuration

Secrets and external base URLs are managed in `application.properties` using:

```properties
external.weather.api-key=your_openweather_api_key
external.weather.base-url=https://api.openweathermap.org/data/2.5

external.news.api-key=your_news_api_key
external.news.base-url=https://newsapi.org/v2

external.crypto.base-url=https://api.coingecko.com/api/v3
```

### Environment Variables (for Docker & AWS)

Secrets will be moved to `.env` or AWS Secrets Manager in the deployment phase.

## Testing

Run all unit tests using:

```bash
./mvnw test
```

- Services are tested with mocked WebClient chains
- Controllers are tested using MockMvc

---

## Roadmap

- [x] External API integration
- [x] Unit tests with full coverage
- [ ] React frontend integration
- [ ] Dockerize the application
- [ ] Deploy to AWS (ECS or EKS)
