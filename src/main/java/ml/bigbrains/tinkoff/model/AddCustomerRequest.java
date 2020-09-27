package ml.bigbrains.tinkoff.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
@ToString(callSuper = true)
public class AddCustomerRequest extends SignedCustomerKeyRequest {
    @JsonProperty("Email")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String email;

    @JsonProperty("Phone")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String phone;

    @JsonCreator
    public AddCustomerRequest(
            @JsonProperty(value = "CustomerKey", required = true) String customerKey,
            @JsonProperty(value = "TerminalKey", required = true) String terminalKey
    ) {
        super(customerKey, terminalKey);
    }

    @Override
    protected Map<String, String> getMapForSignature() {
        Map<String, String> data = new HashMap<>();
        if(StringUtils.isNotEmpty(email))
            data.put("Email", email);
        if(StringUtils.isNotEmpty(phone))
            data.put("Phone", phone);

        data.putAll(super.getMapForSignature());
        return data;
    }

    @Override
    public String getUri() {
        return "/e2c/AddCustomer";
    }
}
