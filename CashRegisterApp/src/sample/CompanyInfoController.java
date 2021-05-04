package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CompanyInfoController {

    /**
     * company name field
     */
    @FXML
    TextField companyNameField;

    /**
     * company address field
     */
    @FXML
    TextField companyAddressField;

    /**
     * company phone-number field
     */
    @FXML
    TextField companyPhoneNumberField;

    /**
     * company email field
     */
    @FXML
    TextField companyEmailField;

    /**
     * Returns company name
     * @return company name
     */
    public String getCompanyName(){
        return companyNameField.getText();
    }

    /**
     * Returns company address
     * @return company address
     */
    public String getCompanyAddress(){
        return companyAddressField.getText();
    }

    /**
     * Returns company phone-number
     * @return company phone number
     */
    public String getCompanyPhoneNumber(){
        return companyPhoneNumberField.getText();
    }

    /**
     * Returns company email address
     * @return
     */
    public String getCompanyEmail(){
        return companyEmailField.getText();
    }

    /**
     * Returns full company info string
     * @return full company info toString
     */
    public String processCompanyInfo(){
        StringBuilder fullCompanyInfo = new StringBuilder();
        fullCompanyInfo.append(companyNameField.getText() + "\n");
        fullCompanyInfo.append(companyAddressField.getText() +  "\n");
        fullCompanyInfo.append(companyPhoneNumberField.getText() + "\n");
        fullCompanyInfo.append(companyEmailField.getText());
        return fullCompanyInfo.toString();
    }
}
