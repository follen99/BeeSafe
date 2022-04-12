public class Report {
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

    public Report(Double longitude, String description, Integer gravity, Double latitude, Long phoneNumber, Integer urgency, String imagePath, String placeName, String dateTime, Integer id, String kindOfProblem, String fromUser) {
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

    public Report(Double longitude, String description, Integer gravity, Double latitude, Long phoneNumber, Integer urgency, String imagePath, String placeName, String dateTime, String kindOfProblem, String fromUser) {
        this.longitude = longitude;
        this.description = description;
        this.gravity = gravity;
        this.latitude = latitude;
        this.phoneNumber = phoneNumber;
        this.urgency = urgency;
        this.imagePath = imagePath;
        this.placeName = placeName;
        this.dateTime = dateTime;
        this.kindOfProblem = kindOfProblem;
        this.fromUser = fromUser;
    }

    public Report() {
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getGravity() {
        return gravity;
    }

    public void setGravity(Integer gravity) {
        this.gravity = gravity;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getUrgency() {
        return urgency;
    }

    public void setUrgency(Integer urgency) {
        this.urgency = urgency;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKindOfProblem() {
        return kindOfProblem;
    }

    public void setKindOfProblem(String kindOfProblem) {
        this.kindOfProblem = kindOfProblem;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }
}
