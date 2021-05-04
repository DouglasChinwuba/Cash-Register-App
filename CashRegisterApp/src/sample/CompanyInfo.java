package sample;

public class CompanyInfo {
    /**
     * String to store company name
     */
    private String companyName;

    /**
     * String to store company address
     */
    private String companyAddress;

    /**
     * String to store company phone-number
     */
    private String companyPhoneNumber;

    /**
     * String to store company email-address
     */
    private String companyEmail;


    /**
     * Company name getter
     * @return company name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Company address getter
     * @return company address
     */
    public String getCompanyAddress() {
        return companyAddress;
    }

    /**
     * Company phone-number getter
     * @return conpany phone-number
     */
    public String getCompanyPhoneNumber() {
        return companyPhoneNumber;
    }

    /**
     * Company email getter
     * @return company email address
     */
    public String getCompanyEmail() {
        return companyEmail;
    }

    /**
     * Company name setter
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Company address setter
     * @param companyAddress
     */
    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    /**
     * Company phone-number setter
     * @param companyPhoneNumber
     */
    public void setCompanyPhoneNumber(String companyPhoneNumber) {
        this.companyPhoneNumber = companyPhoneNumber;
    }

    /**
     * Company email setter
     * @param companyEmail
     */
    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    /**
     * Return company info toString
     * @return companyInfo toString
     */
    public String processCompanyInfo(){
        StringBuilder sb = new StringBuilder();
        sb.append(getCompanyName() + "\n");
        sb.append(getCompanyAddress() +  "\n");
        sb.append(getCompanyPhoneNumber() + "\n");
        sb.append(getCompanyEmail());
        return sb.toString();
    }
}
