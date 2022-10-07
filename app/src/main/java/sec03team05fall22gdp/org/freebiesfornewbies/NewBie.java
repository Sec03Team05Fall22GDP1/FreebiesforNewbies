package sec03team05fall22gdp.org.freebiesfornewbies;

public class NewBie {

    private String firstName, lastName, eMail, phoneNumber, dateOfBirth, addressLine, cityName, stateName, countryName, zipCode;
    private String userName, passWord;

    public NewBie(String firstname, String lastname, String email, String phone, String dob, String addressLine, String city, String State, String country, String zipcode, String username,String password){
        this.firstName = firstname;
        this.lastName = lastname;
        this.eMail = email;
        this.phoneNumber = phone;
        this.dateOfBirth = dob;
        this.addressLine = addressLine;
        this.cityName = city;
        this.stateName = State;
        this.countryName = country;
        this.zipCode = zipcode;
        this.userName = username;
        this.passWord = password;
    }
    public NewBie(){}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
