# Sääsovellus – Room-välimuisti

## Mitä Room tekee (Entity–DAO–Database–Repository–ViewModel–UI)

- Entity: määrittelee tietokantaan tallennettavan datan rakenteen (WeatherEntity).
- DAO: sisältää SQL-kyselyt ja funktiot datan tallentamiseen ja lukemiseen (WeatherDao).
- Database: yhdistää kaikki DAO:t ja luo Room-tietokannan (AppDatabase).
- Repository: päättää, haetaanko data Roomista vai API:sta ja kapseloi välimuistilogiikan.
- ViewModel: kutsuu repositorya ja tarjoaa UI:lle tilan (State).
- UI: näyttää ViewModelin tilan ja päivittyy automaattisesti, kun data muuttuu.

## Projektin rakenne

data/
  model/WeatherEntity.kt 
  model/ForecastResponse.kt
  model/WeatherResponse.kt
  local/WeatherDao.kt  
  local/AppDatabase.kt
  local/AppModule
  remote/WeatherApi.kt  
  remote/RetrofitInstance.kt
  remote/AuthInterceptor
  repository/WeatherRepository.kt  

viewModel/
  WeatherViewModel.kt  

ui/
  WeatherScreen.kt  
  WeatherForecastScreen.kt  
  MapScreen.kt  
  ForecastSection.kt
  WeatherResultSection.kt

Data/
  CloudServiceApp.kt
  MainActivity.kt

## Miten datavirta kulkee

1. UI pyytää ViewModelilta säätiedot.
2. ViewModel kutsuu WeatherRepositorya.
3. Repository tarkistaa Room-tietokannasta, onko tallennettu data tuoretta.
4. Jos data on tuoretta, se palautetaan Roomista.
5. Jos data on vanhaa tai puuttuu, repository hakee uuden datan API:sta.
6. Repository tallentaa uuden datan Roomiin.
7. ViewModel päivittää UI-tilan.
8. UI näyttää päivitetyn datan.

## (Sää) Miten välimuistilogiikka toimii

- WeatherEntity sisältää aikaleiman (timestamp).
- Repository laskee, onko tallennettu data alle 30 minuuttia vanhaa.
- Jos on, data palautetaan Roomista ilman API-kutsua.
- Jos ei ole, tehdään uusi API-kutsu ja tallennetaan tulos Roomiin.
