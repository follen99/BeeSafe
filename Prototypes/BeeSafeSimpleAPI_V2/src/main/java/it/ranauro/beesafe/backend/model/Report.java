package it.ranauro.beesafe.backend.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * L'oggetto report
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Report   {
  
  private Double longitude;
  private String description;
  private Integer gravity;
  private Double latitude;
  private Long phoneNumber;
  private Integer urgency;
  private String imagePath;
  private String placeName;
  private String dateTime;
  private Integer id;
  private String kindOfProblem;
  private String fromUser;

  public Report () {

  }

  /**
   * costruttore usato per inizializzare l'oggetto report
   * @param longitude     la longitudine del report
   * @param description   la descrizione del report
   * @param gravity       la gravità del report
   * @param latitude      la latitudine del report
   * @param phoneNumber   il numero di telefono dell'utente che ha creato il report
   * @param urgency       l'urgenza del report
   * @param imagePath     il path dell'immagine che l'utente ha caricato con il report
   * @param placeName     il nome del posto dove è stato registrato il report
   * @param dateTime      la data ed ora del report
   * @param id            l'id del report
   * @param kindOfProblem il tipo di problema del report
   * @param fromUser      email dell'utente che ha creato il report
   */
  public Report (Double longitude, String description, Integer gravity, Double latitude, Long phoneNumber, Integer urgency, String imagePath, String placeName, String dateTime, Integer id, String kindOfProblem, String fromUser) {
    this.longitude = longitude;
    this.description = description;
    this.gravity = gravity;
    this.latitude = latitude;
    this.phoneNumber = phoneNumber;
    this.urgency = urgency;
    this.imagePath = imagePath;
    this.placeName = placeName;
    this.dateTime = dateTime;
    this.id = id;
    this.kindOfProblem = kindOfProblem;
    this.fromUser = fromUser;
  }

    
  @JsonProperty("longitude")
  public Double getLongitude() {
    return longitude;
  }
  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

    
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

    
  @JsonProperty("gravity")
  public Integer getGravity() {
    return gravity;
  }
  public void setGravity(Integer gravity) {
    this.gravity = gravity;
  }

    
  @JsonProperty("latitude")
  public Double getLatitude() {
    return latitude;
  }
  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

    
  @JsonProperty("phoneNumber")
  public Long getPhoneNumber() {
    return phoneNumber;
  }
  public void setPhoneNumber(Long phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

    
  @JsonProperty("urgency")
  public Integer getUrgency() {
    return urgency;
  }
  public void setUrgency(Integer urgency) {
    this.urgency = urgency;
  }

    
  @JsonProperty("imagePath")
  public String getImagePath() {
    return imagePath;
  }
  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

    
  @JsonProperty("placeName")
  public String getPlaceName() {
    return placeName;
  }
  public void setPlaceName(String placeName) {
    this.placeName = placeName;
  }

    
  @JsonProperty("dateTime")
  public String getDateTime() {
    return dateTime;
  }
  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }

    
  @JsonProperty("id")
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

    
  @JsonProperty("kindOfProblem")
  public String getKindOfProblem() {
    return kindOfProblem;
  }
  public void setKindOfProblem(String kindOfProblem) {
    this.kindOfProblem = kindOfProblem;
  }

    
  @JsonProperty("fromUser")
  public String getFromUser() {
    return fromUser;
  }
  public void setFromUser(String fromUser) {
    this.fromUser = fromUser;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Report report = (Report) o;
    return Objects.equals(longitude, report.longitude) &&
        Objects.equals(description, report.description) &&
        Objects.equals(gravity, report.gravity) &&
        Objects.equals(latitude, report.latitude) &&
        Objects.equals(phoneNumber, report.phoneNumber) &&
        Objects.equals(urgency, report.urgency) &&
        Objects.equals(imagePath, report.imagePath) &&
        Objects.equals(placeName, report.placeName) &&
        Objects.equals(dateTime, report.dateTime) &&
        Objects.equals(id, report.id) &&
        Objects.equals(kindOfProblem, report.kindOfProblem) &&
        Objects.equals(fromUser, report.fromUser);
  }

  @Override
  public int hashCode() {
    return Objects.hash(longitude, description, gravity, latitude, phoneNumber, urgency, imagePath, placeName, dateTime, id, kindOfProblem, fromUser);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Report {\n");
    
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    gravity: ").append(toIndentedString(gravity)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
    sb.append("    urgency: ").append(toIndentedString(urgency)).append("\n");
    sb.append("    imagePath: ").append(toIndentedString(imagePath)).append("\n");
    sb.append("    placeName: ").append(toIndentedString(placeName)).append("\n");
    sb.append("    dateTime: ").append(toIndentedString(dateTime)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    kindOfProblem: ").append(toIndentedString(kindOfProblem)).append("\n");
    sb.append("    fromUser: ").append(toIndentedString(fromUser)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
